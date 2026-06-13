package com.shnok.javaserver.gameserver.data.manager;

import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.shnok.javaserver.commons.data.StatSet;
import com.shnok.javaserver.commons.data.xml.IXmlReader;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.enums.CabalType;
import com.shnok.javaserver.gameserver.enums.SpawnType;
import com.shnok.javaserver.gameserver.enums.actors.TowerType;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.location.ArtifactSpawnLocation;
import com.shnok.javaserver.gameserver.model.location.SpawnLocation;
import com.shnok.javaserver.gameserver.model.location.TowerSpawnLocation;
import com.shnok.javaserver.gameserver.model.pledge.Clan;
import com.shnok.javaserver.gameserver.model.residence.castle.Castle;
import com.shnok.javaserver.gameserver.model.residence.castle.Siege;
import com.shnok.javaserver.gameserver.model.zone.type.SiegeZone;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

/**
 * Loads and stores {@link Castle}s informations, using database and XML informations.
 */
public final class CastleManager implements IXmlReader
{
	private static final String LOAD_CASTLES = "SELECT * FROM castle ORDER BY id";
	private static final String LOAD_OWNER = "SELECT clan_id FROM clan_data WHERE hasCastle=?";
	private static final String LOAD_TRAPS = "SELECT * FROM castle_trapupgrade WHERE castleId=?";
	private static final String LOAD_DOORS = "SELECT * FROM castle_doorupgrade WHERE castleId=?";
	
	private static final String RESET_CERTIFICATES = "UPDATE castle SET certificates=300";
	
	private final Map<Integer, Castle> _castles = new HashMap<>();
	
	protected CastleManager()
	{
		// Build Castle objects with static data.
		load();
		
		// Add dynamic data.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(LOAD_CASTLES);
			ResultSet rs = ps.executeQuery())
		{
			while (rs.next())
			{
				final Castle castle = _castles.get(rs.getInt("id"));
				if (castle == null)
					continue;
				
				castle.setSiegeDate(Calendar.getInstance());
				castle.getSiegeDate().setTimeInMillis(rs.getLong("siegeDate"));
				castle.setTimeRegistrationOver(rs.getBoolean("regTimeOver"));
				castle.setCurrentTaxPercent(rs.getInt("currentTaxPercent"), false);
				castle.setNextTaxPercent(rs.getInt("nextTaxPercent"), false);
				castle.setTreasury(rs.getLong("treasury"));
				castle.setTaxRevenue(rs.getLong("taxRevenue"));
				castle.setSeedIncome(rs.getLong("seedIncome"));
				castle.setLeftCertificates(rs.getInt("certificates"), false);
				
				try (PreparedStatement ps1 = con.prepareStatement(LOAD_OWNER);
					PreparedStatement ps2 = con.prepareStatement(LOAD_TRAPS);
					PreparedStatement ps3 = con.prepareStatement(LOAD_DOORS))
				{
					ps1.setInt(1, castle.getId());
					
					try (ResultSet rs1 = ps1.executeQuery())
					{
						while (rs1.next())
						{
							final int ownerId = rs1.getInt("clan_id");
							if (ownerId > 0)
							{
								final Clan clan = ClanTable.getInstance().getClan(ownerId);
								if (clan != null)
									castle.setOwnerId(ownerId);
							}
						}
					}
					
					ps2.setInt(1, castle.getId());
					
					try (ResultSet rs2 = ps2.executeQuery())
					{
						while (rs2.next())
							castle.getControlTowers().get(rs2.getInt("towerIndex")).setUpgradeLevel(rs2.getInt("level"));
					}
					
					// Generate siege entity. Launch it before door upgrade to avoid NPE.
					castle.launchSiege();
					
					ps3.setInt(1, castle.getId());
					
					try (ResultSet rs3 = ps3.executeQuery())
					{
						while (rs3.next())
							castle.upgradeDoor(rs3.getInt("doorId"), rs3.getInt("hp"), false);
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to load castles.", e);
		}
	}
	
	@Override
	public void load()
	{
		parseFile("data/xml/castles.xml");
		LOGGER.info("Loaded {} castles.", _castles.size());
	}
	
	@Override
	public void parseDocument(Document doc, Path path)
	{
		forEach(doc, "list", listNode -> forEach(listNode, "castle", castleNode ->
		{
			final StatSet set = parseAttributes(castleNode);
			forEach(castleNode, "tax", taxNode -> addAttributes(set, taxNode.getAttributes()));
			
			final Castle castle = new Castle(set);
			
			forEach(castleNode, "artifacts", artifactsNode -> forEach(artifactsNode, "artifact", artifactNode ->
			{
				final NamedNodeMap artifactAttrs = artifactNode.getAttributes();
				final int npcId = parseInteger(artifactAttrs, "id");
				final SpawnLocation pos = parseSpawnLocation(artifactAttrs, "pos");
				
				final ArtifactSpawnLocation asl = new ArtifactSpawnLocation(npcId, castle);
				asl.set(pos);
				
				castle.getArtifacts().add(asl);
			}));
			forEach(castleNode, "controlTowers", controlTowersNode -> forEach(controlTowersNode, "controlTower", towerNode ->
			{
				final NamedNodeMap towerAttrs = towerNode.getAttributes();
				final String alias = parseString(towerAttrs, "alias");
				final TowerType type = parseEnum(towerAttrs, TowerType.class, "type");
				
				final TowerSpawnLocation tsl = new TowerSpawnLocation(type, alias, castle);
				
				forEach(towerNode, "position", positionNode ->
				{
					final NamedNodeMap attrs = positionNode.getAttributes();
					tsl.set(parseInteger(attrs, "x"), parseInteger(attrs, "y"), parseInteger(attrs, "z"));
				});
				forEach(towerNode, "stats", statNode ->
				{
					final NamedNodeMap attrs = statNode.getAttributes();
					tsl.setStats(parseDouble(attrs, "hp"), parseDouble(attrs, "pDef"), parseDouble(attrs, "mDef"));
				});
				forEach(towerNode, "zones", zoneNode -> tsl.setZones(parseString(zoneNode.getAttributes(), "val").split(";")));
				
				castle.getControlTowers().add(tsl);
			}));
			forEach(castleNode, "gates", gatesNode -> castle.setDoors(parseString(gatesNode.getAttributes(), "val")));
			forEach(castleNode, "npcs", npcsNode -> castle.setNpcs(parseString(npcsNode.getAttributes(), "val")));
			forEach(castleNode, "spawns", spawnsNode -> forEach(spawnsNode, "spawn", spawnNode -> castle.addSpawn(parseEnum(spawnNode.getAttributes(), SpawnType.class, "type"), parseLocation(spawnNode))));
			forEach(castleNode, "tickets", ticketsNode -> forEach(ticketsNode, "ticket", ticketNode -> castle.addTicket(parseAttributes(ticketNode))));
			
			// Feed castles Map.
			_castles.put(castle.getId(), castle);
		}));
	}
	
	public Castle getCastleById(int castleId)
	{
		return _castles.get(castleId);
	}
	
	public Castle getCastleByOwner(Clan clan)
	{
		return _castles.values().stream().filter(c -> c.getOwnerId() == clan.getClanId()).findFirst().orElse(null);
	}
	
	public Castle getCastleByAlias(String alias)
	{
		return _castles.values().stream().filter(c -> c.getAlias().equalsIgnoreCase(alias)).findFirst().orElse(null);
	}
	
	public Castle getCastle(int x, int y, int z)
	{
		return _castles.values().stream().filter(c -> c.getSiegeZone().isInsideZone(x, y, z)).findFirst().orElse(null);
	}
	
	public Castle getCastle(WorldObject object)
	{
		return getCastle(object.getX(), object.getY(), object.getZ());
	}
	
	public Collection<Castle> getCastles()
	{
		return _castles.values();
	}
	
	public void validateTaxes(CabalType sealStrifeOwner)
	{
		int maxTax;
		switch (sealStrifeOwner)
		{
			case DAWN:
				maxTax = 25;
				break;
			
			case DUSK:
				maxTax = 5;
				break;
			
			default:
				maxTax = 15;
				break;
		}
		
		_castles.values().stream().filter(c -> c.getCurrentTaxPercent() > maxTax).forEach(c -> c.setCurrentTaxPercent(maxTax, true));
	}
	
	/**
	 * @param object : The {@link WorldObject} to check.
	 * @return True if the {@link WorldObject} set as parameter is inside an ACTIVE {@link SiegeZone}, or false otherwise.
	 */
	public Siege getActiveSiege(WorldObject object)
	{
		return getActiveSiege(object.getX(), object.getY(), object.getZ());
	}
	
	/**
	 * @param x : The X coord to test.
	 * @param y : The Y coord to test.
	 * @param z : The Z coord to test.
	 * @return True if coords are set inside an ACTIVE {@link SiegeZone}, or false otherwise.
	 */
	public Siege getActiveSiege(int x, int y, int z)
	{
		for (Castle castle : _castles.values())
		{
			final Siege siege = castle.getSiege();
			if (siege.isInProgress() && castle.getSiegeZone().isInsideZone(x, y, z))
				return siege;
		}
		return null;
	}
	
	/**
	 * Reset all castles certificates. Reset the memory value, and run a unique query.
	 */
	public void resetCertificates()
	{
		// Reset memory. Don't use the inner save.
		for (Castle castle : _castles.values())
			castle.setLeftCertificates(300, false);
		
		// Update all castles with a single query.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(RESET_CERTIFICATES))
		{
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Failed to reset certificates.", e);
		}
	}
	
	public void spawnEntities()
	{
		_castles.values().forEach(castle ->
		{
			// Spawn control towers.
			castle.getControlTowers().forEach(TowerSpawnLocation::spawnMe);
			
			// Spawn artifacts.
			castle.getArtifacts().forEach(ArtifactSpawnLocation::spawnMe);
		});
	}
	
	/**
	 * Update taxes for all {@link Castle}s.<br>
	 * <br>
	 * For none owned castle :
	 * <ul>
	 * <li>Reset all vars as default.</li>
	 * <li>Use default tax rate for both current and next vars.</li>
	 * </ul>
	 * For owned castle :
	 * <ul>
	 * <li>Increase treasury based on tax revenue and seed income.</li>
	 * <li>Reset tax revenue and seed income vars.</li>
	 * <li>Set current tax using next tax rate.</li>
	 * </ul>
	 */
	public void updateTaxes()
	{
		_castles.values().forEach(Castle::updateTaxes);
	}
	
	public static final CastleManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static final class SingletonHolder
	{
		protected static final CastleManager INSTANCE = new CastleManager();
	}
}