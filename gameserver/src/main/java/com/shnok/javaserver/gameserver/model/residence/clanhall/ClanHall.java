package com.shnok.javaserver.gameserver.model.residence.clanhall;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;
import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.data.xml.ClanHallDecoData;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.Residence;
import com.shnok.javaserver.gameserver.model.zone.type.ClanHallZone;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.PledgeShowInfoUpdate;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;

/**
 * In Lineage 2, there are special building for clans: clan halls.<br>
 * <br>
 * Clan halls give the owning clan some useful benefits. There are 2 types of clan halls: auctionable and contestable. A clan can own only 1 hall at the same time.
 * <ul>
 * <li>Auctionable clan halls can be found in any big township, excluding starting villages, Oren and Heine. Any clan can purchase a hall via auction if they can afford it.</li>
 * <li>Some clan halls come into players possession only once they're conquered. Just like clan halls available via purchase, they are used for making items, teleportation, casting auras etc.</li>
 * </ul>
 */
public class ClanHall extends Residence
{
	protected static final CLogger LOGGER = new CLogger(ClanHall.class.getName());
	
	private static final String DELETE_FUNCTIONS = "DELETE FROM clanhall_functions WHERE hall_id=?";
	private static final String UPDATE_CH = "UPDATE clanhall SET ownerId=?, paidUntil=?, paid=?, sellerBid=?, sellerName=?, sellerClanName=?, endDate=? WHERE id=?";
	
	private static final int ONE_DAY = 86400000; // One day
	private static final int ONE_WEEK = 604800000; // One week
	
	public static final int FUNC_RESTORE_HP = 1;
	public static final int FUNC_RESTORE_MP = 2;
	public static final int FUNC_RESTORE_EXP = 4;
	public static final int FUNC_TELEPORT = 5;
	public static final int FUNC_DECO_CURTAINS = 7;
	public static final int FUNC_SUPPORT_MAGIC = 9;
	public static final int FUNC_DECO_FIXTURES = 11;
	public static final int FUNC_CREATE_ITEM = 12;
	
	private final Map<Integer, ClanHallFunction> _functions = new ConcurrentHashMap<>();
	
	private final String _desc;
	
	private final int _auctionMin;
	private final int _deposit;
	private final int _lease;
	private final int _size;
	private final int _grade;
	
	private ScheduledFuture<?> _feeTask;
	private Auction _auction;
	private ClanHallZone _zone;
	private long _paidUntil;
	private boolean _isPaid;
	
	public ClanHall(StatSet set)
	{
		super(set);
		
		_desc = set.getString("desc");
		_townName = set.getString("loc");
		_auctionMin = set.getInteger("auctionMin", 0);
		_deposit = set.getInteger("deposit", 0);
		_lease = set.getInteger("lease", 0);
		_size = set.getInteger("size", 0);
		_grade = set.getInteger("grade", 0);
	}
	
	public final String getDesc()
	{
		return _desc;
	}
	
	public final int getAuctionMin()
	{
		return _auctionMin;
	}
	
	public final int getDeposit()
	{
		return _deposit;
	}
	
	public final int getLease()
	{
		return _lease;
	}
	
	public final int getSize()
	{
		return _size;
	}
	
	public final int getGrade()
	{
		return _grade;
	}
	
	public final Auction getAuction()
	{
		return _auction;
	}
	
	public final void setAuction(Auction auction)
	{
		_auction = auction;
	}
	
	public final long getPaidUntil()
	{
		return _paidUntil;
	}
	
	public void setPaidUntil(long paidUntil)
	{
		_paidUntil = paidUntil;
	}
	
	public final boolean getPaid()
	{
		return _isPaid;
	}
	
	public void setPaid(boolean isPaid)
	{
		_isPaid = isPaid;
	}
	
	public ClanHallZone getZone()
	{
		return _zone;
	}
	
	public void setZone(ClanHallZone zone)
	{
		_zone = zone;
	}
	
	/**
	 * @return the {@link List} of all {@link ClanHallFunction}s this {@link ClanHall} owns.
	 */
	public final Map<Integer, ClanHallFunction> getFunctions()
	{
		return _functions;
	}
	
	/**
	 * @param type : The type of {@link ClanHallFunction} we search.
	 * @return the {@link ClanHallFunction} associated to the type.
	 */
	public ClanHallFunction getFunction(int type)
	{
		return _functions.get(type);
	}
	
	/**
	 * @param type : The type of {@link ClanHallFunction} we search.
	 * @return the level associated to the {@link ClanHallFunction} if found, or 0 otherwise.
	 */
	public int getFunctionLevel(int type)
	{
		final ClanHallFunction chf = getFunction(type);
		return (chf != null) ? chf.getLvl() : 0;
	}
	
	/**
	 * Free this {@link ClanHall}.
	 * <ul>
	 * <li>Remove the {@link ClanHall} from the {@link Clan}.</li>
	 * <li>Reset all variables to default.</li>
	 * <li>Delete {@link ClanHallFunction}s, and update the database.</li>
	 * </ul>
	 */
	public void free()
	{
		// Cancel fee task, if existing.
		if (_feeTask != null)
		{
			_feeTask.cancel(false);
			_feeTask = null;
		}
		
		// Do some actions on previous owner, if any.
		final Clan clan = ClanTable.getInstance().getClan(_ownerId);
		if (clan != null)
		{
			// Set the clan hall id back to 0.
			clan.setClanHallId(0);
			
			// Refresh Clan Action panel.
			clan.broadcastToMembers(new PledgeShowInfoUpdate(clan));
		}
		
		_ownerId = 0;
		_paidUntil = 0;
		_isPaid = false;
		
		// Remove all related functions.
		removeAllFunctions();
		
		// Close all doors.
		closeDoors();
		
		if (_auction != null)
		{
			// Remove existing bids.
			_auction.removeBids(null);
			
			// Reset auction to initial values if existing.
			_auction.reset(true);
			
			// Launch the auction task.
			_auction.startAutoTask();
		}
		
		// Update dabase.
		updateDb();
	}
	
	/**
	 * Set {@link ClanHall} {@link Clan} owner. If previous owner was existing, do some actions on it.
	 * @param clan : The new {@link ClanHall} owner.
	 */
	public void setOwner(Clan clan)
	{
		if (_auction != null)
		{
			// Send back all losers bids, clear the Bidders Map.
			_auction.removeBids(clan);
			
			// Reset variables.
			_auction.reset(false);
		}
		
		// Verify that Clan isn't null.
		if (clan == null)
		{
			if (_auction != null)
				_auction.startAutoTask();
			
			return;
		}
		
		// Do some actions on previous owner, if any.
		final Clan owner = ClanTable.getInstance().getClan(_ownerId);
		if (owner != null)
		{
			// Set the clan hall id back to 0.
			owner.setClanHallId(0);
			
			// Refresh Clan Action panel.
			owner.broadcastToMembers(new PledgeShowInfoUpdate(owner));
		}
		
		// Remove all related functions.
		removeAllFunctions();
		
		// Close all doors.
		closeDoors();
		
		clan.setClanHallId(_id);
		
		_ownerId = clan.getClanId();
		_paidUntil = System.currentTimeMillis() + ONE_WEEK;
		_isPaid = true;
		
		// Initialize the Fee task for this Clan. The previous Fee task is dropped.
		initializeFeeTask();
		
		// Refresh Clan Action panel.
		clan.broadcastToMembers(new PledgeShowInfoUpdate(clan));
		
		// Teleport out all outsiders (potential previous owners).
		banishForeigners();
		
		// Save all informations into database.
		updateDb();
	}
	
	/**
	 * Banish all {@link Player}s from that {@link ClanHall} zone.
	 */
	public void banishForeigners()
	{
		if (_zone != null)
			_zone.banishForeigners(getOwnerId());
	}
	
	/**
	 * Remove all {@link ClanHallFunction}s linked to this {@link ClanHall}.
	 */
	public void removeAllFunctions()
	{
		// Remove all ClanHallFunctions from memory.
		_functions.clear();
		
		// Update db.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(DELETE_FUNCTIONS))
		{
			ps.setInt(1, getId());
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't delete all clan hall functions.", e);
		}
	}
	
	/**
	 * Update a {@link ClanHallFunction} linked to this {@link ClanHall}. If it doesn't exist, generate it and save it on database.
	 * @param player : The {@link Player} who requested the change.
	 * @param type : The type of {@link ClanHallFunction} to update.
	 * @param lvl : The new level to set.
	 * @param lease : The associated lease taken from {@link Player} inventory.
	 * @param rate : The new rate to set.
	 * @return True if the {@link ClanHallFunction} has been successfully updated.
	 */
	public boolean updateFunction(Player player, int type, int lvl, int lease, long rate)
	{
		// Player doesn't exist.
		if (player == null)
			return false;
		
		// A lease exists, but the Player can't pay it using its inventory adena.
		if (lease > 0 && !player.destroyItemByItemId(57, lease, true))
			return false;
		
		ClanHallFunction chf = _functions.get(type);
		if (chf == null)
		{
			// Generate a ClanHallFunction and save it on the database.
			chf = new ClanHallFunction(this, type, lvl, lease, rate, System.currentTimeMillis() + rate);
			chf.dbSave();
			
			// Add the ClanHallFunction on memory.
			_functions.put(type, chf);
			
			return true;
		}
		
		// Both lease and level are set to 0, we remove the function.
		if (lvl == 0 && lease == 0)
			chf.removeFunction();
		// Refresh the function.
		else
			chf.refreshFunction(lease, lvl);
		
		return true;
	}
	
	/**
	 * Save all related informations of this {@link ClanHall} into database.
	 */
	public void updateDb()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_CH))
		{
			ps.setInt(1, _ownerId);
			ps.setLong(2, _paidUntil);
			ps.setInt(3, (_isPaid) ? 1 : 0);
			
			if (_auction != null)
			{
				if (_auction.getSeller() != null)
				{
					ps.setInt(4, _auction.getSeller().getBid());
					ps.setString(5, _auction.getSeller().getName());
					ps.setString(6, _auction.getSeller().getClanName());
				}
				else
				{
					ps.setInt(4, 0);
					ps.setString(5, "");
					ps.setString(6, "");
				}
				ps.setLong(7, _auction.getEndDate());
			}
			else
			{
				ps.setInt(4, 0);
				ps.setString(5, "");
				ps.setString(6, "");
				ps.setLong(7, 0);
			}
			ps.setInt(8, _id);
			ps.execute();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't update clan hall.", e);
		}
	}
	
	/**
	 * Initialize Fee Task.
	 */
	public void initializeFeeTask()
	{
		// Cancel fee task, if existing. We don't care setting it to null, since it is fed just after.
		if (_feeTask != null)
			_feeTask.cancel(false);
		
		// Take current time.
		long time = System.currentTimeMillis();
		
		// If time didn't past yet, calculate the difference and apply it on the Fee task. Otherwise, run it instantly.
		time = (_paidUntil > time) ? _paidUntil - time : 0;
		
		// Run the Fee task with the given, calculated, time.
		_feeTask = ThreadPool.schedule(this::payFee, time);
	}
	
	private void payFee()
	{
		// Don't bother if ClanHall is already free.
		if (isFree())
			return;
		
		// Clan can't be retrieved, we free the ClanHall.
		final Clan clan = ClanTable.getInstance().getClan(getOwnerId());
		if (clan == null)
		{
			free();
			return;
		}
		
		// We got enough adena, mark the ClanHall as being paid, send back the task one week later.
		if (clan.getWarehouse().getAdena() >= getLease())
		{
			// Delete the adena.
			clan.getWarehouse().destroyItemByItemId(57, getLease());
			
			// Run the task one week later.
			_feeTask = ThreadPool.schedule(this::payFee, ONE_WEEK);
			
			// Refresh variables. Force _isPaid to be set on true, in case we return from grace period.
			_paidUntil += ONE_WEEK;
			_isPaid = true;
			
			// Save all informations into database.
			updateDb();
		}
		// Not enough adena ; grace period, we will retest it one day later.
		else if (_isPaid)
		{
			// Run the task one day later.
			_feeTask = ThreadPool.schedule(this::payFee, ONE_DAY);
			
			// Refresh variables.
			_paidUntil += ONE_DAY;
			_isPaid = false;
			
			// Save all informations into database.
			updateDb();
			
			// Send message to all Clan members.
			clan.broadcastToMembers(SystemMessage.getSystemMessage(SystemMessageId.PAYMENT_FOR_YOUR_CLAN_HALL_HAS_NOT_BEEN_MADE_PLEASE_MAKE_PAYMENT_TO_YOUR_CLAN_WAREHOUSE_BY_S1_TOMORROW).addNumber(getLease()));
		}
		// If ClanHall was already under failed payment, we free the ClanHall immediately.
		else
		{
			// Free the ClanHall.
			free();
			
			// Send message to all Clan members.
			clan.broadcastToMembers(SystemMessage.getSystemMessage(SystemMessageId.THE_CLAN_HALL_FEE_IS_ONE_WEEK_OVERDUE_THEREFORE_THE_CLAN_HALL_OWNERSHIP_HAS_BEEN_REVOKED));
		}
	}
	
	public final int getDecoDepth(int funcType)
	{
		final ClanHallFunction chf = getFunction(funcType);
		if (chf == null)
			return 0;
		
		return ClanHallDecoData.getInstance().getDecoDepth(funcType, chf.getFuncLvl());
	}
}