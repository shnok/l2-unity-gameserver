package com.shnok.javaserver.gameserver.data.manager;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.data.xml.IXmlReader;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.enums.ManorStatus;
import com.shnok.javaserver.gameserver.model.itemcontainer.ItemContainer;
import com.shnok.javaserver.gameserver.model.manor.CropProcure;
import com.shnok.javaserver.gameserver.model.manor.Seed;
import com.shnok.javaserver.gameserver.model.manor.SeedProduction;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.pledge.ClanMember;
import com.shnok.javaserver.gameserver.model.residence.castle.Castle;
import com.shnok.javaserver.gameserver.network.SystemMessageId;

import org.w3c.dom.Document;

/**
 * Loads and stores Manor {@link Seed}s informations for all {@link Castle}s, using database and XML informations.
 */
public class CastleManorManager implements IXmlReader
{
	private static final String LOAD_PROCURE = "SELECT * FROM castle_manor_procure WHERE castle_id=?";
	private static final String LOAD_PRODUCTION = "SELECT * FROM castle_manor_production WHERE castle_id=?";
	
	private static final String UPDATE_PRODUCTION = "UPDATE castle_manor_production SET amount = ? WHERE castle_id = ? AND seed_id = ? AND next_period = 0";
	private static final String UPDATE_PROCURE = "UPDATE castle_manor_procure SET amount = ? WHERE castle_id = ? AND crop_id = ? AND next_period = 0";
	
	private static final String TRUNCATE_PRODUCTS = "TRUNCATE castle_manor_production";
	private static final String INSERT_PRODUCT = "INSERT INTO castle_manor_production VALUES (?, ?, ?, ?, ?, ?)";
	
	private static final String TRUNCATE_PROCURES = "TRUNCATE castle_manor_procure";
	private static final String INSERT_CROP = "INSERT INTO castle_manor_procure VALUES (?, ?, ?, ?, ?, ?, ?)";
	
	private ManorStatus _mode = ManorStatus.APPROVED;
	
	private Calendar _nextModeChange = null;
	
	private final Map<Integer, Seed> _seeds = new HashMap<>();
	
	private final Map<Integer, List<CropProcure>> _procure = new HashMap<>();
	private final Map<Integer, List<CropProcure>> _procureNext = new HashMap<>();
	private final Map<Integer, List<SeedProduction>> _production = new HashMap<>();
	private final Map<Integer, List<SeedProduction>> _productionNext = new HashMap<>();
	
	protected CastleManorManager()
	{
		if (!Config.ALLOW_MANOR)
		{
			_mode = ManorStatus.DISABLED;
			LOGGER.info("Manor system is deactivated.");
			return;
		}
		
		// Load static data.
		load();
		
		// Load dynamic data.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement stProduction = con.prepareStatement(LOAD_PRODUCTION);
			PreparedStatement stProcure = con.prepareStatement(LOAD_PROCURE))
		{
			for (Castle castle : CastleManager.getInstance().getCastles())
			{
				// Seed production
				final List<SeedProduction> pCurrent = new ArrayList<>();
				final List<SeedProduction> pNext = new ArrayList<>();
				
				stProduction.clearParameters();
				stProduction.setInt(1, castle.getId());
				
				try (ResultSet rs = stProduction.executeQuery())
				{
					while (rs.next())
					{
						final SeedProduction sp = new SeedProduction(rs.getInt("seed_id"), rs.getInt("amount"), rs.getInt("price"), rs.getInt("start_amount"));
						if (rs.getBoolean("next_period"))
							pNext.add(sp);
						else
							pCurrent.add(sp);
					}
				}
				_production.put(castle.getId(), pCurrent);
				_productionNext.put(castle.getId(), pNext);
				
				// Seed procure
				final List<CropProcure> current = new ArrayList<>();
				final List<CropProcure> next = new ArrayList<>();
				
				stProcure.clearParameters();
				stProcure.setInt(1, castle.getId());
				
				try (ResultSet rs = stProcure.executeQuery())
				{
					while (rs.next())
					{
						final CropProcure cp = new CropProcure(rs.getInt("crop_id"), rs.getInt("amount"), rs.getInt("reward_type"), rs.getInt("start_amount"), rs.getInt("price"));
						if (rs.getBoolean("next_period"))
							next.add(cp);
						else
							current.add(cp);
					}
				}
				_procure.put(castle.getId(), current);
				_procureNext.put(castle.getId(), next);
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error restoring manor data.", e);
		}
		
		// Set mode and start timer
		final Calendar currentTime = Calendar.getInstance();
		final int hour = currentTime.get(Calendar.HOUR_OF_DAY);
		final int min = currentTime.get(Calendar.MINUTE);
		final int maintenanceMin = Config.MANOR_REFRESH_MIN + Config.MANOR_MAINTENANCE_MIN;
		
		if ((hour >= Config.MANOR_REFRESH_TIME && min >= maintenanceMin) || hour < Config.MANOR_APPROVE_TIME || (hour == Config.MANOR_APPROVE_TIME && min <= Config.MANOR_APPROVE_MIN))
			_mode = ManorStatus.MODIFIABLE;
		else if (hour == Config.MANOR_REFRESH_TIME && (min >= Config.MANOR_REFRESH_MIN && min < maintenanceMin))
			_mode = ManorStatus.MAINTENANCE;
		
		// Schedule mode change
		scheduleModeChange();
		
		// Schedule autosave
		ThreadPool.scheduleAtFixedRate(this::storeMe, Config.MANOR_SAVE_PERIOD_RATE, Config.MANOR_SAVE_PERIOD_RATE);
		
		LOGGER.debug("Current Manor mode is: {}.", _mode.toString());
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/manors.xml");
		LOGGER.info("Loaded {} seeds.", _seeds.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "manor", manorNode ->
		{
			final int castleId = parseInteger(manorNode.getAttributes(), "id");
			forEach(manorNode, "crop", cropNode ->
			{
				final StatSet set = parseAttributes(cropNode);
				set.set("castleId", castleId);
				
				_seeds.put(set.getInteger("seedId"), new Seed(set));
			});
		}));
	}
	
	private final void scheduleModeChange()
	{
		// Calculate next mode change
		_nextModeChange = Calendar.getInstance();
		_nextModeChange.set(Calendar.SECOND, 0);
		
		switch (_mode)
		{
			case MODIFIABLE:
				_nextModeChange.set(Calendar.HOUR_OF_DAY, Config.MANOR_APPROVE_TIME);
				_nextModeChange.set(Calendar.MINUTE, Config.MANOR_APPROVE_MIN);
				if (_nextModeChange.before(Calendar.getInstance()))
				{
					_nextModeChange.add(Calendar.DATE, 1);
				}
				break;
			
			case MAINTENANCE:
				_nextModeChange.set(Calendar.HOUR_OF_DAY, Config.MANOR_REFRESH_TIME);
				_nextModeChange.set(Calendar.MINUTE, Config.MANOR_REFRESH_MIN + Config.MANOR_MAINTENANCE_MIN);
				break;
			
			case APPROVED:
				_nextModeChange.set(Calendar.HOUR_OF_DAY, Config.MANOR_REFRESH_TIME);
				_nextModeChange.set(Calendar.MINUTE, Config.MANOR_REFRESH_MIN);
				break;
		}
		
		// Schedule mode change
		ThreadPool.schedule(this::changeMode, (_nextModeChange.getTimeInMillis() - System.currentTimeMillis()));
	}
	
	public final void changeMode()
	{
		switch (_mode)
		{
			case APPROVED:
				// Change mode
				_mode = ManorStatus.MAINTENANCE;
				
				// Update manor period
				for (Castle castle : CastleManager.getInstance().getCastles())
				{
					final Clan owner = ClanTable.getInstance().getClan(castle.getOwnerId());
					if (owner == null)
						continue;
					
					long amount = 0;
					
					for (CropProcure crop : _procure.get(castle.getId()))
					{
						if (crop.getStartAmount() > 0)
						{
							// Adding bought crops to clan warehouse
							if (crop.getStartAmount() != crop.getAmount())
							{
								int count = (int) ((crop.getStartAmount() - crop.getAmount()) * 0.9);
								if (count < 1 && Rnd.nextInt(99) < 90)
									count = 1;
								
								if (count > 0)
								{
									final Seed seed = getSeedByCrop(crop.getId());
									if (seed != null)
										owner.getWarehouse().addItem(seed.getMatureId(), count);
								}
							}
							
							// Reserved and not used money giving back to treasury
							if (crop.getAmount() > 0)
								amount += crop.getAmount() * crop.getPrice();
						}
					}
					
					// Add the total amount to the castle seed income.
					castle.riseSeedIncome(amount);
					
					// Change next period to current and prepare next period data
					final List<SeedProduction> nextProduction = _productionNext.get(castle.getId());
					final List<CropProcure> nextProcure = _procureNext.get(castle.getId());
					
					_production.put(castle.getId(), nextProduction);
					_procure.put(castle.getId(), nextProcure);
					
					if (castle.getTreasury() < getManorCost(castle.getId(), false))
					{
						_productionNext.put(castle.getId(), Collections.emptyList());
						_procureNext.put(castle.getId(), Collections.emptyList());
					}
					else
					{
						final List<SeedProduction> production = new ArrayList<>(nextProduction);
						for (SeedProduction s : production)
							s.setAmount(s.getStartAmount());
						
						_productionNext.put(castle.getId(), production);
						
						final List<CropProcure> procure = new ArrayList<>(nextProcure);
						for (CropProcure cr : procure)
							cr.setAmount(cr.getStartAmount());
						
						_procureNext.put(castle.getId(), procure);
					}
				}
				
				// Save changes
				storeMe();
				break;
			
			case MAINTENANCE:
				// Notify clan leader about manor mode change
				for (Castle castle : CastleManager.getInstance().getCastles())
				{
					final Clan owner = ClanTable.getInstance().getClan(castle.getOwnerId());
					if (owner != null)
					{
						final ClanMember clanLeader = owner.getLeader();
						if (clanLeader != null && clanLeader.isOnline())
							clanLeader.getPlayerInstance().sendPacket(SystemMessageId.THE_MANOR_INFORMATION_HAS_BEEN_UPDATED);
					}
				}
				_mode = ManorStatus.MODIFIABLE;
				break;
			
			case MODIFIABLE:
				_mode = ManorStatus.APPROVED;
				
				for (Castle castle : CastleManager.getInstance().getCastles())
				{
					final Clan owner = ClanTable.getInstance().getClan(castle.getOwnerId());
					if (owner == null)
						continue;
					
					int slots = 0;
					final ItemContainer cwh = owner.getWarehouse();
					
					for (CropProcure crop : _procureNext.get(castle.getId()))
					{
						if (crop.getStartAmount() > 0 && cwh.getItemsByItemId(getSeedByCrop(crop.getId()).getMatureId()) == null)
							slots++;
					}
					
					if (!cwh.validateCapacity(slots))
					{
						final long manorCost = getManorCost(castle.getId(), true);
						if (!castle.editTreasury(-manorCost, true))
						{
							_productionNext.get(castle.getId()).clear();
							_procureNext.get(castle.getId()).clear();
							
							// Notify clan leader
							final ClanMember clanLeader = owner.getLeader();
							if (clanLeader != null && clanLeader.isOnline())
								clanLeader.getPlayerInstance().sendPacket(SystemMessageId.THE_AMOUNT_IS_NOT_SUFFICIENT_AND_SO_THE_MANOR_IS_NOT_IN_OPERATION);
						}
					}
				}
				break;
		}
		scheduleModeChange();
		
		LOGGER.debug("Manor mode changed to: {}.", _mode.toString());
	}
	
	public final void setNextSeedProduction(List<SeedProduction> list, int castleId)
	{
		_productionNext.put(castleId, list);
	}
	
	public final void setNextCropProcure(List<CropProcure> list, int castleId)
	{
		_procureNext.put(castleId, list);
	}
	
	public static final void updateCurrentProduction(int castleId, Collection<SeedProduction> items)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_PRODUCTION))
		{
			for (SeedProduction sp : items)
			{
				ps.setLong(1, sp.getAmount());
				ps.setInt(2, castleId);
				ps.setInt(3, sp.getId());
				ps.addBatch();
			}
			ps.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.error("Unable to store manor data.", e);
		}
	}
	
	public static final void updateCurrentProcure(int castleId, Collection<CropProcure> items)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_PROCURE))
		{
			for (CropProcure sp : items)
			{
				ps.setLong(1, sp.getAmount());
				ps.setInt(2, castleId);
				ps.setInt(3, sp.getId());
				ps.addBatch();
			}
			ps.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.error("Unable to store manor data.", e);
		}
	}
	
	public final List<SeedProduction> getSeedProduction(int castleId, boolean nextPeriod)
	{
		return (nextPeriod) ? _productionNext.get(castleId) : _production.get(castleId);
	}
	
	public final SeedProduction getSeedProduct(int castleId, int seedId, boolean nextPeriod)
	{
		for (SeedProduction sp : getSeedProduction(castleId, nextPeriod))
		{
			if (sp.getId() == seedId)
				return sp;
		}
		return null;
	}
	
	public final List<CropProcure> getCropProcure(int castleId, boolean nextPeriod)
	{
		return (nextPeriod) ? _procureNext.get(castleId) : _procure.get(castleId);
	}
	
	public final CropProcure getCropProcure(int castleId, int cropId, boolean nextPeriod)
	{
		for (CropProcure cp : getCropProcure(castleId, nextPeriod))
		{
			if (cp.getId() == cropId)
				return cp;
		}
		return null;
	}
	
	public final long getManorCost(int castleId, boolean nextPeriod)
	{
		final List<CropProcure> procure = getCropProcure(castleId, nextPeriod);
		final List<SeedProduction> production = getSeedProduction(castleId, nextPeriod);
		
		long total = 0;
		for (SeedProduction seed : production)
		{
			final Seed s = getSeed(seed.getId());
			total += (s == null) ? 1 : (s.getSeedReferencePrice() * seed.getStartAmount());
		}
		for (CropProcure crop : procure)
		{
			total += (crop.getPrice() * crop.getStartAmount());
		}
		return total;
	}
	
	public final boolean storeMe()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ds = con.prepareStatement(TRUNCATE_PRODUCTS);
			PreparedStatement is = con.prepareStatement(INSERT_PRODUCT);
			PreparedStatement dp = con.prepareStatement(TRUNCATE_PROCURES);
			PreparedStatement ip = con.prepareStatement(INSERT_CROP))
		{
			// Delete old seeds
			ds.executeUpdate();
			
			// Current production
			for (Map.Entry<Integer, List<SeedProduction>> entry : _production.entrySet())
			{
				for (SeedProduction sp : entry.getValue())
				{
					is.setInt(1, entry.getKey());
					is.setInt(2, sp.getId());
					is.setLong(3, sp.getAmount());
					is.setLong(4, sp.getStartAmount());
					is.setLong(5, sp.getPrice());
					is.setBoolean(6, false);
					is.addBatch();
				}
			}
			
			// Next production
			for (Map.Entry<Integer, List<SeedProduction>> entry : _productionNext.entrySet())
			{
				for (SeedProduction sp : entry.getValue())
				{
					is.setInt(1, entry.getKey());
					is.setInt(2, sp.getId());
					is.setLong(3, sp.getAmount());
					is.setLong(4, sp.getStartAmount());
					is.setLong(5, sp.getPrice());
					is.setBoolean(6, true);
					is.addBatch();
				}
			}
			
			// Execute production batch
			is.executeBatch();
			
			// Delete old procure
			dp.executeUpdate();
			
			// Current procure
			for (Map.Entry<Integer, List<CropProcure>> entry : _procure.entrySet())
			{
				for (CropProcure cp : entry.getValue())
				{
					ip.setInt(1, entry.getKey());
					ip.setInt(2, cp.getId());
					ip.setLong(3, cp.getAmount());
					ip.setLong(4, cp.getStartAmount());
					ip.setLong(5, cp.getPrice());
					ip.setInt(6, cp.getReward());
					ip.setBoolean(7, false);
					ip.addBatch();
				}
			}
			
			// Next procure
			for (Map.Entry<Integer, List<CropProcure>> entry : _procureNext.entrySet())
			{
				for (CropProcure cp : entry.getValue())
				{
					ip.setInt(1, entry.getKey());
					ip.setInt(2, cp.getId());
					ip.setLong(3, cp.getAmount());
					ip.setLong(4, cp.getStartAmount());
					ip.setLong(5, cp.getPrice());
					ip.setInt(6, cp.getReward());
					ip.setBoolean(7, true);
					ip.addBatch();
				}
			}
			
			// Execute procure batch
			ip.executeBatch();
			
			return true;
		}
		catch (Exception e)
		{
			LOGGER.error("Unable to store manor data.", e);
			return false;
		}
	}
	
	public final void resetManorData(int castleId)
	{
		if (_mode == ManorStatus.DISABLED)
			return;
		
		_procure.get(castleId).clear();
		_procureNext.get(castleId).clear();
		_production.get(castleId).clear();
		_productionNext.get(castleId).clear();
	}
	
	public final boolean isUnderMaintenance()
	{
		return _mode == ManorStatus.MAINTENANCE;
	}
	
	public final boolean isManorApproved()
	{
		return _mode == ManorStatus.APPROVED;
	}
	
	public final boolean isModifiablePeriod()
	{
		return _mode == ManorStatus.MODIFIABLE;
	}
	
	public final String getCurrentModeName()
	{
		return _mode.toString();
	}
	
	public final String getNextModeChange()
	{
		return new SimpleDateFormat("dd/MM HH:mm:ss").format(_nextModeChange.getTime());
	}
	
	public final List<Seed> getCrops()
	{
		final List<Seed> seeds = new ArrayList<>();
		final List<Integer> cropIds = new ArrayList<>();
		for (Seed seed : _seeds.values())
		{
			if (!cropIds.contains(seed.getCropId()))
			{
				seeds.add(seed);
				cropIds.add(seed.getCropId());
			}
		}
		cropIds.clear();
		return seeds;
	}
	
	public final Set<Seed> getSeedsForCastle(int castleId)
	{
		return _seeds.values().stream().filter(s -> s.getCastleId() == castleId).collect(Collectors.toSet());
	}
	
	public final Set<Integer> getCropIds()
	{
		return _seeds.values().stream().map(Seed::getCropId).collect(Collectors.toSet());
	}
	
	public final Seed getSeed(int seedId)
	{
		return _seeds.get(seedId);
	}
	
	public final Seed getSeedByCrop(int cropId, int castleId)
	{
		return _seeds.values().stream().filter(s -> s.getCastleId() == castleId && s.getCropId() == cropId).findFirst().orElse(null);
	}
	
	public final Seed getSeedByCrop(int cropId)
	{
		return _seeds.values().stream().filter(s -> s.getCropId() == cropId).findFirst().orElse(null);
	}
	
	public final int getSeedRewardByCrop(int cropId, int rewardId)
	{
		final Seed seed = getSeedByCrop(cropId);
		if (seed != null)
			return (rewardId == 1) ? seed.getReward1() : seed.getReward2();
		
		return 0;
	}
	
	public static final CastleManorManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final CastleManorManager INSTANCE = new CastleManorManager();
	}
}