package com.shnok.javaserver.gameserver.model.pledge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.communitybbs.CommunityBoard;
import com.shnok.javaserver.gameserver.communitybbs.model.Forum;
import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.data.cache.CrestCache;
import com.shnok.javaserver.gameserver.data.manager.CastleManager;
import com.shnok.javaserver.gameserver.data.manager.RelationManager;
import com.shnok.javaserver.gameserver.data.sql.ClanTable;
import com.shnok.javaserver.gameserver.enums.CrestType;
import com.shnok.javaserver.gameserver.enums.PrivilegeType;
import com.shnok.javaserver.gameserver.enums.SiegeSide;
import com.shnok.javaserver.gameserver.enums.bbs.ForumAccess;
import com.shnok.javaserver.gameserver.enums.bbs.ForumType;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.itemcontainer.ClanWarehouse;
import com.shnok.javaserver.gameserver.model.itemcontainer.ItemContainer;
import com.shnok.javaserver.gameserver.model.residence.castle.Castle;
import com.shnok.javaserver.gameserver.model.residence.castle.Siege;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PledgeReceiveSubPledgeCreated;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PledgeShowInfoUpdate;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PledgeShowMemberListAll;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PledgeShowMemberListDeleteAll;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PledgeShowMemberListUpdate;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PledgeSkillList;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.PledgeSkillListAdd;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.SkillList;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.actor.UserInfo;
import com.shnok.javaserver.gameserver.skills.L2Skill;

/**
 * Clan system is one of the major features of the game. Clans unite players and let them influence the world of Lineage 2.<br>
 * <br>
 * A clan is made up of a clan leader (commonly known as a lord) and a number of {@link ClanMember}s.
 */
public class Clan
{
	private static final CLogger LOGGER = new CLogger(Clan.class.getName());
	
	private static final String LOAD_MEMBERS = "SELECT char_name,level,classid,obj_Id,title,power_grade,subpledge,apprentice,sponsor,sex,race FROM characters WHERE clanid=?";
	private static final String LOAD_SUBPLEDGES = "SELECT sub_pledge_id,name,leader_id FROM clan_subpledges WHERE clan_id=?";
	private static final String LOAD_PRIVILEGES = "SELECT ranking,privs FROM clan_privs WHERE clan_id=?";
	private static final String LOAD_SKILLS = "SELECT skill_id,skill_level FROM clan_skills WHERE clan_id=?";
	
	private static final String SET_MEMBER_POWER_GRADE = "UPDATE characters SET power_grade=? WHERE obj_Id=?";
	
	private static final String REMOVE_MEMBER = "UPDATE characters SET clanid=0, title=?, clan_join_expiry_time=?, clan_create_expiry_time=?, wantspeace=0, subpledge=0, lvl_joined_academy=0, apprentice=0, sponsor=0 WHERE obj_Id=?";
	private static final String REMOVE_APPRENTICE = "UPDATE characters SET apprentice=0 WHERE apprentice=?";
	private static final String REMOVE_SPONSOR = "UPDATE characters SET sponsor=0 WHERE sponsor=?";
	
	private static final String UPDATE_CLAN = "UPDATE clan_data SET leader_id=?,new_leader_id=?,ally_id=?,ally_name=?,reputation_score=?,ally_penalty_expiry_time=?,ally_penalty_type=?,char_penalty_expiry_time=?,dissolving_expiry_time=? WHERE clan_id=?";
	private static final String STORE_CLAN = "INSERT INTO clan_data (clan_id,clan_name,clan_level,hasCastle,ally_id,ally_name,leader_id,new_leader_id,crest_id,crest_large_id,ally_crest_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String UPDATE_NOTICE = "UPDATE clan_data SET enabled=?,notice=? WHERE clan_id=?";
	private static final String UPDATE_INTRODUCTION = "UPDATE clan_data SET introduction=? WHERE clan_id=?";
	
	private static final String ADD_OR_UPDATE_SKILL = "INSERT INTO clan_skills (clan_id,skill_id,skill_level) VALUES (?,?,?) ON DUPLICATE KEY UPDATE skill_level=VALUES(skill_level)";
	private static final String REMOVE_SKILL = "DELETE FROM clan_skills WHERE clan_id=? AND skill_id=?";
	private static final String REMOVE_ALL_SKILLS = "DELETE FROM clan_skills WHERE clan_id=?";
	
	private static final String INSERT_SUBPLEDGE = "INSERT INTO clan_subpledges (clan_id,sub_pledge_id,name,leader_id) values (?,?,?,?)";
	private static final String UPDATE_SUBPLEDGE = "UPDATE clan_subpledges SET leader_id=?, name=? WHERE clan_id=? AND sub_pledge_id=?";
	
	private static final String ADD_OR_UPDATE_PRIVILEGE = "INSERT INTO clan_privs (clan_id,ranking,privs) VALUES (?,?,?) ON DUPLICATE KEY UPDATE privs=VALUES(privs)";
	
	private static final String UPDATE_CRP = "UPDATE clan_data SET reputation_score=? WHERE clan_id=?";
	private static final String UPDATE_AUCTION = "UPDATE clan_data SET auction_bid_at=? WHERE clan_id=?";
	private static final String UPDATE_CLAN_LEVEL = "UPDATE clan_data SET clan_level = ? WHERE clan_id = ?";
	private static final String UPDATE_CLAN_CREST = "UPDATE clan_data SET crest_id = ? WHERE clan_id = ?";
	private static final String UPDATE_LARGE_CREST = "UPDATE clan_data SET crest_large_id = ? WHERE clan_id = ?";
	
	// Ally Penalty Types
	public static final int PENALTY_TYPE_CLAN_LEAVED = 1;
	public static final int PENALTY_TYPE_CLAN_DISMISSED = 2;
	public static final int PENALTY_TYPE_DISMISS_CLAN = 3;
	public static final int PENALTY_TYPE_DISSOLVE_ALLY = 4;
	
	// Sub-unit types
	public static final int SUBUNIT_ACADEMY = -1;
	public static final int SUBUNIT_ROYAL1 = 100;
	public static final int SUBUNIT_ROYAL2 = 200;
	public static final int SUBUNIT_KNIGHT1 = 1001;
	public static final int SUBUNIT_KNIGHT2 = 1002;
	public static final int SUBUNIT_KNIGHT3 = 2001;
	public static final int SUBUNIT_KNIGHT4 = 2002;
	
	private static final int MAX_NOTICE_LENGTH = 8192;
	private static final int MAX_INTRODUCTION_LENGTH = 300;
	
	private final Map<Integer, ClanMember> _members = new ConcurrentHashMap<>();
	private final Map<Integer, Long> _warPenaltyExpiryTime = new ConcurrentHashMap<>();
	private final Map<Integer, L2Skill> _skills = new ConcurrentHashMap<>();
	private final Map<Integer, Integer> _privileges = new ConcurrentHashMap<>();
	private final Map<Integer, SubPledge> _subPledges = new ConcurrentHashMap<>();
	
	private final Set<Integer> _atWarWith = ConcurrentHashMap.newKeySet();
	private final Set<Integer> _atWarAttackers = ConcurrentHashMap.newKeySet();
	
	private final ItemContainer _warehouse = new ClanWarehouse(this);
	
	private String _name;
	private int _clanId;
	private ClanMember _leader;
	private int _newLeaderId;
	private String _allyName;
	private int _allyId;
	private int _level;
	private int _castleId;
	private int _chId;
	private int _crestId;
	private int _crestLargeId;
	private int _allyCrestId;
	private int _auctionBiddedAt;
	private long _allyPenaltyExpiryTime;
	private int _allyPenaltyType;
	private long _charPenaltyExpiryTime;
	private long _dissolvingExpiryTime;
	
	private Forum _annBoard; // Clan Announcements
	private Forum _cbbBoard; // Clan Community Board
	
	private int _reputationScore;
	private int _rank;
	
	private String _notice;
	private boolean _noticeEnabled;
	
	private String _introduction;
	
	private int _siegeKills;
	private int _siegeDeaths;
	
	private Npc _flag;
	
	/**
	 * Constructor used to restore an existing {@link Clan}. Infos are fetched using database.
	 * @param clanId : The clanId informations to restore.
	 * @param leaderId : The clan leader objectId.
	 */
	public Clan(int clanId, int leaderId)
	{
		_clanId = clanId;
		
		try (Connection con = ConnectionPool.getConnection())
		{
			// Load members.
			try (PreparedStatement ps = con.prepareStatement(LOAD_MEMBERS))
			{
				ps.setInt(1, _clanId);
				
				try (ResultSet rs = ps.executeQuery())
				{
					while (rs.next())
					{
						final ClanMember member = new ClanMember(this, rs);
						if (member.getObjectId() == leaderId)
							setLeader(member);
						else
							_members.put(member.getObjectId(), member);
					}
				}
			}
			
			// Load subpledges.
			try (PreparedStatement ps = con.prepareStatement(LOAD_SUBPLEDGES))
			{
				ps.setInt(1, _clanId);
				
				try (ResultSet rs = ps.executeQuery())
				{
					while (rs.next())
					{
						final int id = rs.getInt("sub_pledge_id");
						
						_subPledges.put(id, new SubPledge(id, rs.getString("name"), rs.getInt("leader_id")));
					}
				}
			}
			
			// Load privileges.
			try (PreparedStatement ps = con.prepareStatement(LOAD_PRIVILEGES))
			{
				ps.setInt(1, _clanId);
				
				try (ResultSet rs = ps.executeQuery())
				{
					while (rs.next())
						_privileges.put(rs.getInt("ranking"), rs.getInt("privs"));
				}
			}
			
			// Load clan skills.
			try (PreparedStatement ps = con.prepareStatement(LOAD_SKILLS))
			{
				ps.setInt(1, _clanId);
				
				try (ResultSet rs = ps.executeQuery())
				{
					while (rs.next())
					{
						int id = rs.getInt("skill_id");
						int level = rs.getInt("skill_level");
						
						final L2Skill skill = SkillTable.getInstance().getInfo(id, level);
						if (skill == null)
							continue;
						
						_skills.put(id, skill);
					}
				}
			}
		}
		catch (Exception e)
		{
			LOGGER.error("Error while restoring clan.", e);
		}
		
		if (_crestId != 0 && CrestCache.getInstance().getCrest(CrestType.PLEDGE, _crestId) == null)
		{
			LOGGER.warn("Removing non-existent crest for clan {}, crestId: {}.", toString(), _crestId);
			changeClanCrest(0);
		}
		
		if (_crestLargeId != 0 && CrestCache.getInstance().getCrest(CrestType.PLEDGE_LARGE, _crestLargeId) == null)
		{
			LOGGER.warn("Removing non-existent large crest for clan {}, crestLargeId: {}.", toString(), _crestLargeId);
			changeLargeCrest(0);
		}
		
		if (_allyCrestId != 0 && CrestCache.getInstance().getCrest(CrestType.ALLY, _allyCrestId) == null)
		{
			LOGGER.warn("Removing non-existent ally crest for clan {}, allyCrestId: {}.", toString(), _allyCrestId);
			changeAllyCrest(0, true);
		}
		
		_warehouse.restore();
	}
	
	/**
	 * A constructor called to create a new {@link Clan}. It feeds all privileges ranks with CP_NOTHING.
	 * @param clanId : A valid id to use.
	 * @param clanName : A valid clan name.
	 */
	public Clan(int clanId, String clanName)
	{
		_clanId = clanId;
		_name = clanName;
		
		_notice = "";
		_introduction = "";
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	public int getClanId()
	{
		return _clanId;
	}
	
	public void setClanId(int clanId)
	{
		_clanId = clanId;
	}
	
	public ClanMember getLeader()
	{
		return _leader;
	}
	
	public int getLeaderId()
	{
		return _leader.getObjectId();
	}
	
	public String getLeaderName()
	{
		return (_leader == null) ? "" : _leader.getName();
	}
	
	public void setLeader(ClanMember leader)
	{
		_leader = leader;
		_members.put(leader.getObjectId(), leader);
	}
	
	public void setNewLeader(ClanMember member)
	{
		// Store references for future usage.
		final Player newLeader = member.getPlayerInstance();
		final Player exLeader = getLeader().getPlayerInstance();
		
		// Store references for future usage.
		final int newLeaderId = member.getObjectId();
		final int exLeaderId = getLeaderId();
		
		// Update leader and reset _newLeaderId references.
		_leader = member;
		_newLeaderId = 0;
		
		// Store in database.
		updateClanInDB();
		
		// If the ex leader is found, make special actions.
		if (exLeader != null)
		{
			if (exLeader.isFlying())
				exLeader.dismount();
			
			if (_level >= Config.MINIMUM_CLAN_LEVEL)
				exLeader.removeSiegeSkills();
			
			exLeader.setPowerGrade(6);
			exLeader.setPledgeClass(ClanMember.calculatePledgeClass(exLeader));
			exLeader.broadcastUserInfo();
			exLeader.checkItemRestriction();
		}
		
		// If the new leader is found, make special actions.
		if (newLeader != null)
		{
			newLeader.setPowerGrade(0);
			newLeader.setPledgeClass(ClanMember.calculatePledgeClass(newLeader));
			
			if (_level >= Config.MINIMUM_CLAN_LEVEL)
				newLeader.addSiegeSkills();
			
			newLeader.broadcastUserInfo();
		}
		
		// Update power grades of both ex and new leader.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(SET_MEMBER_POWER_GRADE))
		{
			ps.setInt(1, 0);
			ps.setInt(2, newLeaderId);
			ps.addBatch();
			
			ps.setInt(1, 6);
			ps.setInt(2, exLeaderId);
			ps.addBatch();
			
			ps.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't update clan privs.", e);
		}
		
		broadcastClanStatus();
		broadcastToMembers(SystemMessage.getSystemMessage(SystemMessageId.CLAN_LEADER_PRIVILEGES_HAVE_BEEN_TRANSFERRED_TO_S1).addString(member.getName()));
	}
	
	public int getNewLeaderId()
	{
		return _newLeaderId;
	}
	
	public void setNewLeaderId(int objectId, boolean storeInDb)
	{
		_newLeaderId = objectId;
		
		if (storeInDb)
			updateClanInDB();
	}
	
	public String getAllyName()
	{
		return _allyName;
	}
	
	public void setAllyName(String allyName)
	{
		_allyName = allyName;
	}
	
	public int getAllyId()
	{
		return _allyId;
	}
	
	public void setAllyId(int allyId)
	{
		_allyId = allyId;
	}
	
	public int getAllyCrestId()
	{
		return _allyCrestId;
	}
	
	public void setAllyCrestId(int allyCrestId)
	{
		_allyCrestId = allyCrestId;
	}
	
	public int getLevel()
	{
		return _level;
	}
	
	/**
	 * Sets the {@link Clan} level and updates the clan forum if it's needed.
	 * @param level : The clan level to set.
	 */
	public void setLevel(int level)
	{
		_level = level;
		
		if (Config.ENABLE_COMMUNITY_BOARD && _level >= 2)
		{
			if (_annBoard == null)
				_annBoard = CommunityBoard.getInstance().getOrCreateForum(ForumType.CLAN_ANN, ForumAccess.READ, _clanId);
			
			if (_cbbBoard == null)
				_cbbBoard = CommunityBoard.getInstance().getOrCreateForum(ForumType.CLAN_CBB, ForumAccess.READ, _clanId);
		}
	}
	
	public int getCastleId()
	{
		return _castleId;
	}
	
	public boolean hasCastle()
	{
		return _castleId > 0;
	}
	
	public void setCastle(int castle)
	{
		_castleId = castle;
	}
	
	public int getClanHallId()
	{
		return _chId;
	}
	
	public boolean hasClanHall()
	{
		return _chId > 0;
	}
	
	public void setClanHallId(int id)
	{
		_chId = id;
	}
	
	public int getCrestId()
	{
		return _crestId;
	}
	
	public void setCrestId(int crestId)
	{
		_crestId = crestId;
	}
	
	public int getCrestLargeId()
	{
		return _crestLargeId;
	}
	
	public void setCrestLargeId(int crestLargeId)
	{
		_crestLargeId = crestLargeId;
	}
	
	public int getSiegeKills()
	{
		return _siegeKills;
	}
	
	public int getSiegeDeaths()
	{
		return _siegeDeaths;
	}
	
	public void setSiegeKills(int value)
	{
		_siegeKills = value;
	}
	
	public void setSiegeDeaths(int value)
	{
		_siegeDeaths = value;
	}
	
	public Npc getFlag()
	{
		return _flag;
	}
	
	public void setFlag(Npc flag)
	{
		// If we set flag as null and clan flag is currently existing, delete it.
		if (flag == null && _flag != null)
			_flag.deleteMe();
		
		_flag = flag;
	}
	
	public long getAllyPenaltyExpiryTime()
	{
		return _allyPenaltyExpiryTime;
	}
	
	public int getAllyPenaltyType()
	{
		return _allyPenaltyType;
	}
	
	public void setAllyPenaltyExpiryTime(long expiryTime, int penaltyType)
	{
		_allyPenaltyExpiryTime = expiryTime;
		_allyPenaltyType = penaltyType;
	}
	
	public long getCharPenaltyExpiryTime()
	{
		return _charPenaltyExpiryTime;
	}
	
	public void setCharPenaltyExpiryTime(long time)
	{
		_charPenaltyExpiryTime = time;
	}
	
	public long getDissolvingExpiryTime()
	{
		return _dissolvingExpiryTime;
	}
	
	public void setDissolvingExpiryTime(long time)
	{
		_dissolvingExpiryTime = time;
	}
	
	public Forum getAnnBoard()
	{
		return _annBoard;
	}
	
	public Forum getCbbBoard()
	{
		return _cbbBoard;
	}
	
	/**
	 * @param objectId : The required clan member objectId.
	 * @return the {@link ClanMember} for a given objectId.
	 */
	public ClanMember getClanMember(int objectId)
	{
		return _members.get(objectId);
	}
	
	/**
	 * @param name : The required clan member name.
	 * @return the {@link ClanMember} for a given name.
	 */
	public ClanMember getClanMember(String name)
	{
		return _members.values().stream().filter(m -> m.getName().equals(name)).findFirst().orElse(null);
	}
	
	/**
	 * @param objectId : The member objectId.
	 * @return true if the {@link ClanMember} objectId given as parameter is in the _members list.
	 */
	public boolean isMember(int objectId)
	{
		return _members.containsKey(objectId);
	}
	
	/**
	 * Add a {@link ClanMember} to this {@link Clan}, based on a {@link Player}.
	 * <ul>
	 * <li>Generate the ClanMember object, link it to the Player object.</li>
	 * <li>Calculate and set the player pledge class.</li>
	 * <li>Update the siege flag.</li>
	 * <li>Send packets</li>
	 * </ul>
	 * @param player : The player to add on clan.
	 */
	public void addClanMember(Player player)
	{
		final ClanMember member = new ClanMember(this, player);
		member.setPlayerInstance(player);
		
		_members.put(member.getObjectId(), member);
		
		player.setClan(this);
		player.setPledgeClass(ClanMember.calculatePledgeClass(player));
		
		// Update siege flag, if needed.
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			final Siege siege = castle.getSiege();
			if (!siege.isInProgress())
				continue;
			
			if (siege.checkSide(this, SiegeSide.ATTACKER))
				player.setSiegeState((byte) 1);
			else if (siege.checkSides(this, SiegeSide.DEFENDER, SiegeSide.OWNER))
				player.setSiegeState((byte) 2);
		}
		
		player.sendPacket(new PledgeShowMemberListUpdate(player));
		player.sendPacket(new UserInfo(player));
	}
	
	/**
	 * Remove a {@link ClanMember} from this {@link Clan}.
	 * @param objectId : The objectId of the member that will be removed.
	 * @param clanJoinExpiryTime : The time penalty to join a clan.
	 */
	public void removeClanMember(int objectId, long clanJoinExpiryTime)
	{
		final ClanMember exMember = _members.remove(objectId);
		if (exMember == null)
			return;
		
		// Sub-unit leader withdraws, position becomes vacant and leader should appoint new via NPC.
		final int subPledgeId = getLeaderSubPledge(objectId);
		if (subPledgeId != 0)
		{
			final SubPledge pledge = getSubPledge(subPledgeId);
			if (pledge != null)
			{
				pledge.setLeaderId(0);
				
				updateSubPledgeInDB(pledge);
			}
		}
		
		// Remove apprentice, if any.
		if (exMember.getApprentice() != 0)
		{
			final ClanMember apprentice = getClanMember(exMember.getApprentice());
			if (apprentice != null)
			{
				if (apprentice.getPlayerInstance() != null)
					apprentice.getPlayerInstance().setSponsor(0);
				else
					apprentice.setApprenticeAndSponsor(0, 0);
				
				apprentice.saveApprenticeAndSponsor(0, 0);
			}
		}
		
		// Remove sponsor, if any.
		if (exMember.getSponsor() != 0)
		{
			final ClanMember sponsor = getClanMember(exMember.getSponsor());
			if (sponsor != null)
			{
				if (sponsor.getPlayerInstance() != null)
					sponsor.getPlayerInstance().setApprentice(0);
				else
					sponsor.setApprenticeAndSponsor(0, 0);
				
				sponsor.saveApprenticeAndSponsor(0, 0);
			}
		}
		exMember.saveApprenticeAndSponsor(0, 0);
		
		// Unequip castle related items.
		if (hasCastle())
			CastleManager.getInstance().getCastleById(_castleId).checkItemsForMember(exMember);
		
		if (exMember.isOnline())
		{
			Player player = exMember.getPlayerInstance();
			
			// Clean title only for non nobles.
			if (!player.isNoble())
				player.setTitle("");
			
			// Setup active warehouse to null.
			if (player.getActiveWarehouse() != null)
				player.setActiveWarehouse(null);
			
			player.setApprentice(0);
			player.setSponsor(0);
			player.setSiegeState((byte) 0);
			
			if (player.isClanLeader())
			{
				player.removeSiegeSkills();
				player.setClanCreateExpiryTime(System.currentTimeMillis() + Config.CLAN_CREATE_DAYS * 86400000L);
			}
			
			// Remove clan skills.
			for (L2Skill skill : _skills.values())
				player.removeSkill(skill.getId(), false);
			
			player.sendPacket(new SkillList(player));
			player.setClan(null);
			
			// Players leaving from clan academy have no penalty.
			if (exMember.getPledgeType() != SUBUNIT_ACADEMY)
				player.setClanJoinExpiryTime(clanJoinExpiryTime);
			
			player.setPledgeClass(ClanMember.calculatePledgeClass(player));
			player.broadcastUserInfo();
			
			// Disable clan tab.
			player.sendPacket(PledgeShowMemberListDeleteAll.STATIC_PACKET);
		}
		else
		{
			try (Connection con = ConnectionPool.getConnection())
			{
				try (PreparedStatement ps = con.prepareStatement(REMOVE_MEMBER))
				{
					ps.setString(1, "");
					ps.setLong(2, clanJoinExpiryTime);
					ps.setLong(3, (_leader.getObjectId() == objectId) ? System.currentTimeMillis() + Config.CLAN_CREATE_DAYS * 86400000L : 0);
					ps.setInt(4, exMember.getObjectId());
					ps.executeUpdate();
				}
				
				try (PreparedStatement ps = con.prepareStatement(REMOVE_APPRENTICE))
				{
					ps.setInt(1, exMember.getObjectId());
					ps.executeUpdate();
				}
				
				try (PreparedStatement ps = con.prepareStatement(REMOVE_SPONSOR))
				{
					ps.setInt(1, exMember.getObjectId());
					ps.executeUpdate();
				}
			}
			catch (Exception e)
			{
				LOGGER.error("Error while removing clan member.", e);
			}
		}
	}
	
	public Collection<ClanMember> getMembers()
	{
		return _members.values();
	}
	
	public int getMembersCount()
	{
		return _members.size();
	}
	
	public int getSubPledgeMembersCount(int pledgeType)
	{
		return (int) _members.values().stream().filter(m -> m.getPledgeType() == pledgeType).count();
	}
	
	public String getSubPledgeLeaderName(int pledgeType)
	{
		if (pledgeType == 0)
			return _leader.getName();
		
		SubPledge subPledge = _subPledges.get(pledgeType);
		int leaderId = subPledge.getLeaderId();
		
		if (subPledge.getId() == SUBUNIT_ACADEMY || leaderId == 0)
			return "";
		
		if (!_members.containsKey(leaderId))
		{
			LOGGER.warn("SubPledge leader {} is missing from clan: {}.", leaderId, toString());
			return "";
		}
		
		return _members.get(leaderId).getName();
	}
	
	/**
	 * @param pledgeType : The id of the pledge type.
	 * @return the maximum number of members allowed for a given pledgeType.
	 */
	public int getMaxNrOfMembers(int pledgeType)
	{
		switch (pledgeType)
		{
			case 0:
				switch (_level)
				{
					case 0:
						return 10;
					
					case 1:
						return 15;
					
					case 2:
						return 20;
					
					case 3:
						return 30;
					
					default:
						return 40;
				}
				
			case SUBUNIT_ACADEMY, SUBUNIT_ROYAL1, SUBUNIT_ROYAL2:
				return 20;
			
			case SUBUNIT_KNIGHT1, SUBUNIT_KNIGHT2, SUBUNIT_KNIGHT3, SUBUNIT_KNIGHT4:
				return 10;
		}
		return 0;
	}
	
	public Player[] getOnlineMembers()
	{
		List<Player> list = new ArrayList<>();
		for (ClanMember temp : _members.values())
		{
			if (temp != null && temp.isOnline())
				list.add(temp.getPlayerInstance());
		}
		return list.toArray(new Player[list.size()]);
	}
	
	/**
	 * @return the online clan member count.
	 */
	public int getOnlineMembersCount()
	{
		return (int) _members.values().stream().filter(ClanMember::isOnline).count();
	}
	
	/**
	 * Update all {@link Clan} informations.
	 */
	public void updateClanInDB()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_CLAN))
		{
			ps.setInt(1, _leader.getObjectId());
			ps.setInt(2, _newLeaderId);
			ps.setInt(3, _allyId);
			ps.setString(4, _allyName);
			ps.setInt(5, _reputationScore);
			ps.setLong(6, _allyPenaltyExpiryTime);
			ps.setInt(7, _allyPenaltyType);
			ps.setLong(8, _charPenaltyExpiryTime);
			ps.setLong(9, _dissolvingExpiryTime);
			ps.setInt(10, _clanId);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while updating clan.", e);
		}
	}
	
	/**
	 * Store informations of a newly created {@link Clan}.
	 */
	public void store()
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(STORE_CLAN))
		{
			ps.setInt(1, _clanId);
			ps.setString(2, _name);
			ps.setInt(3, _level);
			ps.setInt(4, _castleId);
			ps.setInt(5, _allyId);
			ps.setString(6, _allyName);
			ps.setInt(7, _leader.getObjectId());
			ps.setInt(8, _newLeaderId);
			ps.setInt(9, _crestId);
			ps.setInt(10, _crestLargeId);
			ps.setInt(11, _allyCrestId);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while storing clan.", e);
		}
	}
	
	/**
	 * @return The {@link String} notice of this {@link Clan}.
	 */
	public String getNotice()
	{
		return _notice;
	}
	
	public boolean isNoticeEnabled()
	{
		return _noticeEnabled;
	}
	
	/**
	 * Set both the related _noticeEnabled boolean flag and the notice of this {@link Clan}.
	 * @param notice : The new {@link String} content to set as notice.
	 * @param enabled : If true, the notice will be enabled.
	 * @param saveOnDb : If true, we save it on database.
	 */
	public void setNotice(String notice, boolean enabled, boolean saveOnDb)
	{
		_notice = StringUtil.trim(notice, MAX_NOTICE_LENGTH, "");
		_noticeEnabled = enabled;
		
		if (saveOnDb)
		{
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE_NOTICE))
			{
				ps.setBoolean(1, _noticeEnabled);
				ps.setString(2, _notice);
				ps.setInt(3, _clanId);
				ps.executeUpdate();
			}
			catch (Exception e)
			{
				LOGGER.error("Error while storing notice.", e);
			}
		}
	}
	
	/**
	 * Set the related _noticeEnabled boolean flag of this {@link Clan}, and store it in database.
	 * @param enabled : If true, the notice will be enabled.
	 * @see #setNotice(String, boolean, boolean)
	 */
	public void setNotice(boolean enabled)
	{
		setNotice(_notice, enabled, true);
	}
	
	/**
	 * Set the notice of this {@link Clan}, and store it in database.
	 * @param notice : The new {@link String} content to set as notice.
	 * @see #setNotice(String, boolean, boolean)
	 */
	public void setNotice(String notice)
	{
		setNotice(notice, _noticeEnabled, true);
	}
	
	/**
	 * @return The {@link String} introduction of this {@link Clan}.
	 */
	public String getIntroduction()
	{
		return _introduction;
	}
	
	/**
	 * Set the introduction of this {@link Clan}, and store it in database.
	 * @param introduction : The new {@link String} content to set as introduction.
	 * @param saveOnDb : If true, we save it on database.
	 */
	public void setIntroduction(String introduction, boolean saveOnDb)
	{
		_introduction = StringUtil.trim(introduction, MAX_INTRODUCTION_LENGTH, "");
		
		if (saveOnDb)
		{
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE_INTRODUCTION))
			{
				ps.setString(1, _introduction);
				ps.setInt(2, _clanId);
				ps.executeUpdate();
			}
			catch (Exception e)
			{
				LOGGER.error("Error while storing introduction.", e);
			}
		}
	}
	
	/**
	 * @return the {@link Map} with all known {@link Clan} {@link L2Skill}s.
	 */
	public final Map<Integer, L2Skill> getClanSkills()
	{
		return _skills;
	}
	
	/**
	 * Add a new {@link L2Skill} to the {@link Clan}.
	 * @param skill : The {@link L2Skill} to add.
	 * @param needRefresh : If True, a status refresh occured. Bypass {@link Player} {@link L2Skill} acquisition.
	 * @return True if the operation was successful (the {@link L2Skill} wasn't existing or at a lower level), or false otherwise.
	 */
	public boolean addClanSkill(L2Skill skill, boolean needRefresh)
	{
		// Integrity check.
		if (skill == null)
			return false;
		
		// Don't try to add skill if the exact same level already exists.
		final L2Skill existingSkill = _skills.get(skill.getId());
		if (existingSkill != null && existingSkill.getLevel() == skill.getLevel())
			return false;
		
		// Update db.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(ADD_OR_UPDATE_SKILL))
		{
			ps.setInt(1, _clanId);
			ps.setInt(2, skill.getId());
			ps.setInt(3, skill.getLevel());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while storing a clan skill.", e);
			return false;
		}
		
		// Replace or add the skill.
		_skills.put(skill.getId(), skill);
		
		final PledgeSkillListAdd psla = new PledgeSkillListAdd(skill);
		final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.CLAN_SKILL_S1_ADDED).addSkillName(skill.getId());
		
		// Test each online member, reward the skill if checks are successful.
		for (Player player : getOnlineMembers())
		{
			if (!needRefresh && checkAndAddClanSkill(player, skill))
			{
				player.addSkill(skill, false);
				player.sendPacket(new SkillList(player));
			}
			player.sendPacket(psla);
			player.sendPacket(sm);
		}
		return true;
	}
	
	/**
	 * Remove a {@link L2Skill} based on its id from the {@link Clan}.
	 * @param id : The id to remove, if existing.
	 * @return True if the operation was successful (the {@link L2Skill} was deleted), or false otherwise.
	 */
	public boolean removeClanSkill(int id)
	{
		// Don't try to remove skill if not existing.
		final L2Skill skill = _skills.remove(id);
		if (skill == null)
			return false;
		
		// Update db.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(REMOVE_SKILL))
		{
			ps.setInt(1, _clanId);
			ps.setInt(2, id);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while removing a clan skill.", e);
			return false;
		}
		
		final PledgeSkillList psl = new PledgeSkillList(this);
		
		// Test each online member, remove the skill.
		for (Player player : getOnlineMembers())
		{
			player.removeSkill(id, false);
			player.sendPacket(new SkillList(player));
			
			player.sendPacket(psl);
		}
		return true;
	}
	
	/**
	 * Add all existing {@link Clan} {@link L2Skill}s to this {@link Clan}.
	 * @return True if the operation was successful (at least one {@link L2Skill} was rewarded), or false otherwise.
	 */
	public boolean addAllClanSkills()
	{
		// Build the complete List of skills to reward. If all skills are already learnt, don't process it.
		final List<L2Skill> list = Arrays.stream(SkillTable.getInstance().getClanSkills()).filter(s -> !_skills.containsKey(s.getId()) || _skills.get(s.getId()).getLevel() < s.getLevel()).toList();
		if (list.isEmpty())
			return false;
		
		// Update db.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(ADD_OR_UPDATE_SKILL))
		{
			ps.setInt(1, _clanId);
			
			for (L2Skill skill : list)
			{
				ps.setInt(2, skill.getId());
				ps.setInt(3, skill.getLevel());
				ps.addBatch();
			}
			ps.executeBatch();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while adding all clan skills.", e);
			return false;
		}
		
		// Replace or add the skill.
		for (L2Skill skill : list)
			_skills.put(skill.getId(), skill);
		
		final PledgeSkillList psl = new PledgeSkillList(this);
		
		// Test each online member, reward skills if checks are successful.
		for (Player player : getOnlineMembers())
		{
			if (checkAndAddClanSkills(player))
				player.sendPacket(new SkillList(player));
			
			player.sendPacket(psl);
		}
		return true;
	}
	
	/**
	 * Remove all existing {@link Clan} {@link L2Skill}s from this {@link Clan}.
	 * @return True if the operation was successful (all {@link L2Skill}s are deleted), or false otherwise.
	 */
	public boolean removeAllClanSkills()
	{
		// Don't try to remove skills if empty.
		if (_skills.isEmpty())
			return false;
		
		// Update db.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(REMOVE_ALL_SKILLS))
		{
			ps.setInt(1, _clanId);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while removing all clan skills.", e);
			return false;
		}
		
		final PledgeSkillList psl = new PledgeSkillList(this);
		
		// Test each online member, remove all skills.
		for (Player player : getOnlineMembers())
		{
			for (L2Skill skill : _skills.values())
				player.removeSkill(skill.getId(), false);
			
			player.sendPacket(new SkillList(player));
			player.sendPacket(psl);
		}
		
		// Clear memory.
		_skills.clear();
		
		return true;
	}
	
	/**
	 * Check olympiad state, clan reputation and pledge rank, then reward the {@link L2Skill} to the {@link Player} set as parameter if everything is ok.
	 * @param player : The {@link Player} to test.
	 * @param skill : The {@link L2Skill} to test.
	 * @return True if the {@link Clan} {@link L2Skill} could be properly added to the {@link Player}, or false otherwise.
	 */
	public boolean checkAndAddClanSkill(Player player, L2Skill skill)
	{
		if (_reputationScore <= 0 || player.isInOlympiadMode())
			return false;
		
		if (skill.getMinPledgeClass() > player.getPledgeClass())
			return false;
		
		player.addSkill(skill, false);
		return true;
	}
	
	/**
	 * Check olympiad state, clan reputation and pledge rank, then reward all {@link Clan} {@link L2Skill}s to the {@link Player} set as parameter if everything is ok.
	 * @param player : The {@link Player} to test.
	 * @return True if all {@link Clan} {@link L2Skill}s could be properly added to the {@link Player}, or false otherwise.
	 */
	public boolean checkAndAddClanSkills(Player player)
	{
		if (_reputationScore <= 0 || player.isInOlympiadMode())
			return false;
		
		for (L2Skill skill : _skills.values())
		{
			if (skill.getMinPledgeClass() <= player.getPledgeClass())
				player.addSkill(skill, false);
		}
		return true;
	}
	
	public void broadcastToMembers(L2GameServerPacket... packets)
	{
		for (Player member : getOnlineMembers())
		{
			for (L2GameServerPacket packet : packets)
				member.sendPacket(packet);
		}
	}
	
	public void broadcastToAllyMembers(L2GameServerPacket... packets)
	{
		for (Clan clan : ClanTable.getInstance().getClanAllies(_allyId))
			clan.broadcastToMembers(packets);
	}
	
	public void broadcastToMembersExcept(Player player, L2GameServerPacket... packets)
	{
		for (Player member : getOnlineMembers())
		{
			if (member != player)
			{
				for (L2GameServerPacket packet : packets)
					member.sendPacket(packet);
			}
		}
	}
	
	@Override
	public String toString()
	{
		return _name + "[" + _clanId + "]";
	}
	
	public ItemContainer getWarehouse()
	{
		return _warehouse;
	}
	
	public boolean isAtWarWith(int id)
	{
		return _atWarWith.contains(id);
	}
	
	public boolean isAtWarAttacker(int id)
	{
		return _atWarAttackers.contains(id);
	}
	
	public void setEnemyClan(int clanId)
	{
		_atWarWith.add(clanId);
	}
	
	public void setAttackerClan(int clanId)
	{
		_atWarAttackers.add(clanId);
	}
	
	public void deleteEnemyClan(int clanId)
	{
		_atWarWith.remove(Integer.valueOf(clanId));
	}
	
	public void deleteAttackerClan(int clanId)
	{
		_atWarAttackers.remove(Integer.valueOf(clanId));
	}
	
	public void addWarPenaltyTime(int clanId, long expiryTime)
	{
		_warPenaltyExpiryTime.put(clanId, expiryTime);
	}
	
	public boolean hasWarPenaltyWith(int clanId)
	{
		if (!_warPenaltyExpiryTime.containsKey(clanId))
			return false;
		
		return _warPenaltyExpiryTime.get(clanId) > System.currentTimeMillis();
	}
	
	public Map<Integer, Long> getWarPenalty()
	{
		return _warPenaltyExpiryTime;
	}
	
	public boolean isAtWar()
	{
		return !_atWarWith.isEmpty();
	}
	
	public Set<Integer> getWarList()
	{
		return _atWarWith;
	}
	
	public Set<Integer> getAttackerList()
	{
		return _atWarAttackers;
	}
	
	public void broadcastClanStatus()
	{
		for (Player member : getOnlineMembers())
		{
			member.sendPacket(PledgeShowMemberListDeleteAll.STATIC_PACKET);
			member.sendPacket(new PledgeShowMemberListAll(this, 0));
			
			for (SubPledge sp : getAllSubPledges())
				member.sendPacket(new PledgeShowMemberListAll(this, sp.getId()));
			
			member.sendPacket(new UserInfo(member));
		}
	}
	
	public boolean isSubPledgeLeader(int objectId)
	{
		for (SubPledge sp : getAllSubPledges())
		{
			if (sp.getLeaderId() == objectId)
				return true;
		}
		
		return false;
	}
	
	/**
	 * Retrieve subPledge by type
	 * @param pledgeType
	 * @return the subpledge object.
	 */
	public final SubPledge getSubPledge(int pledgeType)
	{
		return _subPledges.get(pledgeType);
	}
	
	/**
	 * Retrieve subPledge by name
	 * @param pledgeName
	 * @return the subpledge object.
	 */
	public final SubPledge getSubPledge(String pledgeName)
	{
		return _subPledges.values().stream().filter(s -> s.getName().equalsIgnoreCase(pledgeName)).findFirst().orElse(null);
	}
	
	/**
	 * Retrieve all subPledges.
	 * @return an array containing all subpledge objects.
	 */
	public final SubPledge[] getAllSubPledges()
	{
		return _subPledges.values().toArray(new SubPledge[_subPledges.values().size()]);
	}
	
	public SubPledge createSubPledge(Player player, int type, int leaderId, String subPledgeName)
	{
		final int pledgeType = getAvailablePledgeTypes(type);
		if (pledgeType == 0)
		{
			player.sendPacket((type == SUBUNIT_ACADEMY) ? SystemMessageId.CLAN_HAS_ALREADY_ESTABLISHED_A_CLAN_ACADEMY : SystemMessageId.YOU_DO_NOT_MEET_CRITERIA_IN_ORDER_TO_CREATE_A_MILITARY_UNIT);
			return null;
		}
		
		if (_leader.getObjectId() == leaderId)
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_MEET_CRITERIA_IN_ORDER_TO_CREATE_A_MILITARY_UNIT);
			return null;
		}
		
		if (pledgeType != SUBUNIT_ACADEMY && ((_reputationScore < 5000 && pledgeType < SUBUNIT_KNIGHT1) || (_reputationScore < 10000 && pledgeType > SUBUNIT_ROYAL2)))
		{
			player.sendPacket(SystemMessageId.THE_CLAN_REPUTATION_SCORE_IS_TOO_LOW);
			return null;
		}
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(INSERT_SUBPLEDGE))
		{
			ps.setInt(1, _clanId);
			ps.setInt(2, pledgeType);
			ps.setString(3, subPledgeName);
			ps.setInt(4, (pledgeType != SUBUNIT_ACADEMY) ? leaderId : 0);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error creating subpledge.", e);
			return null;
		}
		
		final SubPledge subPledge = new SubPledge(pledgeType, subPledgeName, leaderId);
		_subPledges.put(pledgeType, subPledge);
		
		if (pledgeType != SUBUNIT_ACADEMY)
		{
			if (pledgeType < SUBUNIT_KNIGHT1) // royal
				takeReputationScore(5000);
			else if (pledgeType > SUBUNIT_ROYAL2) // knight
				takeReputationScore(10000);
		}
		
		broadcastToMembers(new PledgeShowInfoUpdate(this), new PledgeReceiveSubPledgeCreated(subPledge, this));
		
		return subPledge;
	}
	
	public int getAvailablePledgeTypes(int pledgeType)
	{
		if (_subPledges.get(pledgeType) != null)
		{
			switch (pledgeType)
			{
				case SUBUNIT_ACADEMY:
					return 0;
				
				case SUBUNIT_ROYAL1:
					pledgeType = getAvailablePledgeTypes(SUBUNIT_ROYAL2);
					break;
				
				case SUBUNIT_ROYAL2:
					return 0;
				
				case SUBUNIT_KNIGHT1:
					pledgeType = getAvailablePledgeTypes(SUBUNIT_KNIGHT2);
					break;
				
				case SUBUNIT_KNIGHT2:
					pledgeType = getAvailablePledgeTypes(SUBUNIT_KNIGHT3);
					break;
				
				case SUBUNIT_KNIGHT3:
					pledgeType = getAvailablePledgeTypes(SUBUNIT_KNIGHT4);
					break;
				
				case SUBUNIT_KNIGHT4:
					return 0;
			}
		}
		return pledgeType;
	}
	
	public void updateSubPledgeInDB(SubPledge pledge)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_SUBPLEDGE))
		{
			ps.setInt(1, pledge.getLeaderId());
			ps.setString(2, pledge.getName());
			ps.setInt(3, _clanId);
			ps.setInt(4, pledge.getId());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error updating subpledge.", e);
		}
	}
	
	public int getPrivilegesByRank(int rank)
	{
		return _privileges.getOrDefault(rank, PrivilegeType.NONE.getMask());
	}
	
	/**
	 * Update ranks privileges.
	 * @param rank : The rank to edit.
	 * @param privs : The privileges to be set.
	 */
	public void setPrivilegesForRank(int rank, int privs)
	{
		// Avoid to bother with invalid rankings.
		if (rank < 1 || rank > 9)
			return;
		
		// Special treatment for rank 9, which is Academy Rank.
		if (rank == 9)
		{
			// Limit the passed privs in terms of possible rights.
			privs = (privs & PrivilegeType.SP_WAREHOUSE_SEARCH.getMask()) + (privs & PrivilegeType.CHP_ENTRY_EXIT_RIGHTS.getMask()) + (privs & PrivilegeType.CHP_USE_FUNCTIONS.getMask()) + (privs & PrivilegeType.CP_ENTRY_EXIT_RIGHTS.getMask()) + (privs & PrivilegeType.CP_USE_FUNCTIONS.getMask());
		}
		
		// Replace the privileges.
		_privileges.put(rank, privs);
		
		broadcastClanStatus();
		
		// Update database.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(ADD_OR_UPDATE_PRIVILEGE))
		{
			ps.setInt(1, _clanId);
			ps.setInt(2, rank);
			ps.setInt(3, privs);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while storing ranking.", e);
		}
	}
	
	public int getLeaderSubPledge(int leaderId)
	{
		int id = 0;
		for (SubPledge sp : _subPledges.values())
		{
			if (sp.getLeaderId() == 0)
				continue;
			
			if (sp.getLeaderId() == leaderId)
				id = sp.getId();
		}
		return id;
	}
	
	public int getReputationScore()
	{
		return _reputationScore;
	}
	
	/**
	 * Add the value to the total amount of the {@link Clan}'s reputation score.<br>
	 * @param value : The value to add to current amount.
	 * @return True if a status refresh occured during the operation.
	 */
	public synchronized boolean addReputationScore(int value)
	{
		return setReputationScore(_reputationScore + value);
	}
	
	/**
	 * Removes the value to the total amount of this {@link Clan}'s reputation score.<br>
	 * @param value : The value to remove to current amount.
	 * @return True if a status refresh occured during the operation.
	 */
	public synchronized boolean takeReputationScore(int value)
	{
		return setReputationScore(_reputationScore - value);
	}
	
	/**
	 * Add or remove a given value from this {@link Clan}'s current reputation score.<br>
	 * <br>
	 * If needed, process a refresh of {@link Clan} status.
	 * @param value : The total amount to set to _reputationScore.
	 * @return True if a status refresh occured (being positive value reached negative, or negative value reached positive).
	 */
	private boolean setReputationScore(int value)
	{
		// Don't add any CRPs to clans with low level.
		if (_level < 5)
			return false;
		
		// That check is used to see if it needs a refresh.
		final boolean needRefresh = (_reputationScore > 0 && value <= 0) || (value > 0 && _reputationScore <= 0);
		
		// Store the online members (used in 2 positions, can't merge)
		final Player[] members = getOnlineMembers();
		
		_reputationScore = Math.clamp(value, -100000000, 100000000);
		
		// Refresh clan windows of all clan members, and reward/remove skills.
		if (needRefresh)
		{
			final Collection<L2Skill> skills = _skills.values();
			
			if (_reputationScore <= 0)
			{
				for (Player member : members)
				{
					member.sendPacket(SystemMessageId.REPUTATION_POINTS_0_OR_LOWER_CLAN_SKILLS_DEACTIVATED);
					
					for (L2Skill sk : skills)
						member.removeSkill(sk.getId(), false);
					
					member.sendPacket(new SkillList(member));
				}
			}
			else
			{
				for (Player member : members)
				{
					member.sendPacket(SystemMessageId.CLAN_SKILLS_WILL_BE_ACTIVATED_SINCE_REPUTATION_IS_0_OR_HIGHER);
					
					for (L2Skill sk : skills)
					{
						if (sk.getMinPledgeClass() <= member.getPledgeClass())
							member.addSkill(sk, false);
					}
					
					member.sendPacket(new SkillList(member));
				}
			}
		}
		
		// Points reputation update for all.
		final PledgeShowInfoUpdate infoRefresh = new PledgeShowInfoUpdate(this);
		for (Player member : members)
			member.sendPacket(infoRefresh);
		
		// Save the amount on the database.
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_CRP))
		{
			ps.setInt(1, _reputationScore);
			ps.setInt(2, _clanId);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while updating clan reputation points.", e);
		}
		return needRefresh;
	}
	
	public int getRank()
	{
		return _rank;
	}
	
	public void setRank(int rank)
	{
		_rank = rank;
	}
	
	public int getAuctionBiddedAt()
	{
		return _auctionBiddedAt;
	}
	
	public void setAuctionBiddedAt(int id)
	{
		_auctionBiddedAt = id;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_AUCTION))
		{
			ps.setInt(1, id);
			ps.setInt(2, _clanId);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while updating clan auction.", e);
		}
	}
	
	/**
	 * @param player : The potential Player recruiter of a Clan.
	 * @param target : The potential Player recruit of a Clan.
	 * @param pledgeType : The Clan sub-unit where the target Player is invited.
	 * @return true if, for given conditions, a {@link Player} can recruit another Player in its {@link Clan}.
	 */
	public boolean checkClanJoinCondition(Player player, Player target, int pledgeType)
	{
		if (player == null)
			return false;
		
		if (!player.hasClanPrivileges(PrivilegeType.SP_INVITE))
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return false;
		}
		
		if (target == null)
		{
			player.sendPacket(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET);
			return false;
		}
		
		if (player.getObjectId() == target.getObjectId())
		{
			player.sendPacket(SystemMessageId.CANNOT_INVITE_YOURSELF);
			return false;
		}
		
		if (target.isBlockingAll())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_BLOCKED_EVERYTHING).addCharName(target));
			return false;
		}
		
		if (RelationManager.getInstance().isInBlockList(target, player))
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ADDED_YOU_TO_IGNORE_LIST).addCharName(target));
			return false;
		}
		
		if (target.getClanId() != 0)
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_WORKING_WITH_ANOTHER_CLAN).addCharName(target));
			return false;
		}
		
		if (_charPenaltyExpiryTime > System.currentTimeMillis())
		{
			player.sendPacket(SystemMessageId.YOU_MUST_WAIT_BEFORE_ACCEPTING_A_NEW_MEMBER);
			return false;
		}
		
		if (target.getClanJoinExpiryTime() > System.currentTimeMillis())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_MUST_WAIT_BEFORE_JOINING_ANOTHER_CLAN).addCharName(target));
			return false;
		}
		
		if ((target.getStatus().getLevel() > 40 || target.getClassId().getLevel() >= 2) && pledgeType == SUBUNIT_ACADEMY)
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DOESNOT_MEET_REQUIREMENTS_TO_JOIN_ACADEMY).addCharName(target));
			player.sendPacket(SystemMessageId.ACADEMY_REQUIREMENTS);
			return false;
		}
		
		if (getSubPledgeMembersCount(pledgeType) >= getMaxNrOfMembers(pledgeType))
		{
			if (pledgeType == 0)
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CLAN_IS_FULL).addString(getName()));
			else
				player.sendPacket(SystemMessageId.SUBCLAN_IS_FULL);
			
			return false;
		}
		return true;
	}
	
	/**
	 * @param player : The player alliance launching the invitation.
	 * @param target : The target clan to recruit.
	 * @return true if target's clan meet conditions to join player's alliance.
	 */
	public static boolean checkAllyJoinCondition(Player player, Player target)
	{
		if (player == null)
			return false;
		
		if (player.getAllyId() == 0 || !player.isClanLeader() || player.getClanId() != player.getAllyId())
		{
			player.sendPacket(SystemMessageId.FEATURE_ONLY_FOR_ALLIANCE_LEADER);
			return false;
		}
		
		final Clan leaderClan = player.getClan();
		if (leaderClan.getAllyPenaltyType() == PENALTY_TYPE_DISMISS_CLAN && leaderClan.getAllyPenaltyExpiryTime() > System.currentTimeMillis())
		{
			player.sendPacket(SystemMessageId.CANT_INVITE_CLAN_WITHIN_1_DAY);
			return false;
		}
		
		if (target == null)
		{
			player.sendPacket(SystemMessageId.SELECT_USER_TO_INVITE);
			return false;
		}
		
		if (player.getObjectId() == target.getObjectId())
		{
			player.sendPacket(SystemMessageId.CANNOT_INVITE_YOURSELF);
			return false;
		}
		
		if (target.getClan() == null)
		{
			player.sendPacket(SystemMessageId.TARGET_MUST_BE_IN_CLAN);
			return false;
		}
		
		if (!target.isClanLeader())
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_IS_NOT_A_CLAN_LEADER).addCharName(target));
			return false;
		}
		
		final Clan targetClan = target.getClan();
		if (target.getAllyId() != 0)
		{
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CLAN_ALREADY_MEMBER_OF_S2_ALLIANCE).addString(targetClan.getName()).addString(targetClan.getAllyName()));
			return false;
		}
		
		if (targetClan.getAllyPenaltyExpiryTime() > System.currentTimeMillis())
		{
			if (targetClan.getAllyPenaltyType() == PENALTY_TYPE_CLAN_LEAVED)
			{
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_CANT_ENTER_ALLIANCE_WITHIN_1_DAY).addString(targetClan.getName()));
				return false;
			}
			
			if (targetClan.getAllyPenaltyType() == PENALTY_TYPE_CLAN_DISMISSED)
			{
				player.sendPacket(SystemMessageId.CANT_ENTER_ALLIANCE_WITHIN_1_DAY);
				return false;
			}
		}
		
		// Check if clans are registered as opponents on the same siege.
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			if (castle.getSiege().isOnOppositeSide(leaderClan, targetClan))
			{
				player.sendPacket(SystemMessageId.OPPOSING_CLAN_IS_PARTICIPATING_IN_SIEGE);
				return false;
			}
		}
		
		if (leaderClan.isAtWarWith(targetClan.getClanId()))
		{
			player.sendPacket(SystemMessageId.MAY_NOT_ALLY_CLAN_BATTLE);
			return false;
		}
		
		if (ClanTable.getInstance().getClanAllies(player.getAllyId()).size() >= Config.MAX_NUM_OF_CLANS_IN_ALLY)
		{
			player.sendPacket(SystemMessageId.YOU_HAVE_EXCEEDED_THE_LIMIT);
			return false;
		}
		
		return true;
	}
	
	public void createAlly(Player player, String allyName)
	{
		if (player == null)
			return;
		
		if (!player.isClanLeader())
		{
			player.sendPacket(SystemMessageId.ONLY_CLAN_LEADER_CREATE_ALLIANCE);
			return;
		}
		
		if (_allyId != 0)
		{
			player.sendPacket(SystemMessageId.ALREADY_JOINED_ALLIANCE);
			return;
		}
		
		if (_level < 5)
		{
			player.sendPacket(SystemMessageId.TO_CREATE_AN_ALLY_YOU_CLAN_MUST_BE_LEVEL_5_OR_HIGHER);
			return;
		}
		
		if (_allyPenaltyType == PENALTY_TYPE_DISSOLVE_ALLY && _allyPenaltyExpiryTime > System.currentTimeMillis())
		{
			player.sendPacket(SystemMessageId.CANT_CREATE_ALLIANCE_10_DAYS_DISOLUTION);
			return;
		}
		
		if (_dissolvingExpiryTime > System.currentTimeMillis())
		{
			player.sendPacket(SystemMessageId.YOU_MAY_NOT_CREATE_ALLY_WHILE_DISSOLVING);
			return;
		}
		
		if (!StringUtil.isAlphaNumeric(allyName))
		{
			player.sendPacket(SystemMessageId.INCORRECT_ALLIANCE_NAME);
			return;
		}
		
		if (allyName.length() > 16 || allyName.length() < 2)
		{
			player.sendPacket(SystemMessageId.INCORRECT_ALLIANCE_NAME_LENGTH);
			return;
		}
		
		if (ClanTable.getInstance().isAllyExists(allyName))
		{
			player.sendPacket(SystemMessageId.ALLIANCE_ALREADY_EXISTS);
			return;
		}
		
		for (Castle castle : CastleManager.getInstance().getCastles())
		{
			if (castle.getSiege().isInProgress() && castle.getSiege().checkSides(this))
			{
				player.sendPacket(SystemMessageId.NO_ALLY_CREATION_WHILE_SIEGE);
				return;
			}
		}
		
		_allyId = _clanId;
		_allyName = allyName;
		setAllyPenaltyExpiryTime(0, 0);
		updateClanInDB();
		
		player.sendPacket(new UserInfo(player));
	}
	
	public void dissolveAlly(Player player)
	{
		if (_allyId == 0)
		{
			player.sendPacket(SystemMessageId.NO_CURRENT_ALLIANCES);
			return;
		}
		
		if (!player.isClanLeader() || _clanId != _allyId)
		{
			player.sendPacket(SystemMessageId.FEATURE_ONLY_FOR_ALLIANCE_LEADER);
			return;
		}
		
		// For every clan in alliance, check if it is currently registered on siege.
		for (Clan clan : ClanTable.getInstance().getClanAllies(_allyId))
		{
			for (Castle castle : CastleManager.getInstance().getCastles())
			{
				if (castle.getSiege().isInProgress() && castle.getSiege().checkSides(clan))
				{
					player.sendPacket(SystemMessageId.CANNOT_DISSOLVE_ALLY_WHILE_IN_SIEGE);
					return;
				}
			}
		}
		
		broadcastToAllyMembers(SystemMessage.getSystemMessage(SystemMessageId.ALLIANCE_DISOLVED));
		
		long currentTime = System.currentTimeMillis();
		for (Clan clan : ClanTable.getInstance().getClans())
		{
			if (clan.getAllyId() == _allyId && clan.getClanId() != _clanId)
			{
				clan.setAllyId(0);
				clan.setAllyName(null);
				clan.setAllyPenaltyExpiryTime(0, 0);
				clan.updateClanInDB();
			}
		}
		
		_allyId = 0;
		_allyName = null;
		changeAllyCrest(0, false);
		setAllyPenaltyExpiryTime(currentTime + Config.CREATE_ALLY_DAYS_WHEN_DISSOLVED * 86400000L, PENALTY_TYPE_DISSOLVE_ALLY);
		updateClanInDB();
		
		// The clan leader should take the XP penalty of a full death.
		player.applyDeathPenalty(false, false);
	}
	
	public boolean levelUpClan(Player player)
	{
		if (!player.isClanLeader())
		{
			player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
			return false;
		}
		
		if (System.currentTimeMillis() < _dissolvingExpiryTime)
		{
			player.sendPacket(SystemMessageId.CANNOT_RISE_LEVEL_WHILE_DISSOLUTION_IN_PROGRESS);
			return false;
		}
		
		boolean increaseClanLevel = false;
		
		switch (_level)
		{
			case 0: // upgrade to 1
				if (player.getStatus().getSp() >= 30000 && player.reduceAdena(650000, true))
				{
					player.removeExpAndSp(0, 30000);
					increaseClanLevel = true;
				}
				break;
			
			case 1: // upgrade to 2
				if (player.getStatus().getSp() >= 150000 && player.reduceAdena(2500000, true))
				{
					player.removeExpAndSp(0, 150000);
					increaseClanLevel = true;
				}
				break;
			
			case 2:// upgrade to 3
				if (player.getStatus().getSp() >= 500000 && player.destroyItemByItemId(1419, 1, true))
				{
					player.removeExpAndSp(0, 500000);
					increaseClanLevel = true;
				}
				break;
			
			case 3: // upgrade to 4
				if (player.getStatus().getSp() >= 1400000 && player.destroyItemByItemId(3874, 1, true))
				{
					player.removeExpAndSp(0, 1400000);
					increaseClanLevel = true;
				}
				break;
			
			case 4: // upgrade to 5
				if (player.getStatus().getSp() >= 3500000 && player.destroyItemByItemId(3870, 1, true))
				{
					player.removeExpAndSp(0, 3500000);
					increaseClanLevel = true;
				}
				break;
			
			case 5:
				if (_reputationScore >= 10000 && getMembersCount() >= 30)
				{
					takeReputationScore(10000);
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DEDUCTED_FROM_CLAN_REP).addNumber(10000));
					increaseClanLevel = true;
				}
				break;
			
			case 6:
				if (_reputationScore >= 20000 && getMembersCount() >= 80)
				{
					takeReputationScore(20000);
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DEDUCTED_FROM_CLAN_REP).addNumber(20000));
					increaseClanLevel = true;
				}
				break;
			
			case 7:
				if (_reputationScore >= 40000 && getMembersCount() >= 120)
				{
					takeReputationScore(40000);
					player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_DEDUCTED_FROM_CLAN_REP).addNumber(40000));
					increaseClanLevel = true;
				}
				break;
		}
		
		if (!increaseClanLevel)
		{
			player.sendPacket(SystemMessageId.FAILED_TO_INCREASE_CLAN_LEVEL);
			return false;
		}
		
		changeLevel(_level + 1);
		return true;
	}
	
	public void changeLevel(int level)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_CLAN_LEVEL))
		{
			ps.setInt(1, level);
			ps.setInt(2, _clanId);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while updating clan level.", e);
		}
		
		setLevel(level);
		
		if (_leader.isOnline())
		{
			Player leader = _leader.getPlayerInstance();
			if (3 < level)
				leader.addSiegeSkills();
			else if (4 > level)
				leader.removeSiegeSkills();
			
			if (4 < level)
				leader.sendPacket(SystemMessageId.CLAN_CAN_ACCUMULATE_CLAN_REPUTATION_POINTS);
		}
		
		broadcastToMembers(new PledgeShowInfoUpdate(this), SystemMessage.getSystemMessage(SystemMessageId.CLAN_LEVEL_INCREASED));
	}
	
	/**
	 * Change the clan crest. Old crest is removed. New crest id is saved to database.
	 * @param crestId : The new crest id is set and saved to database.
	 */
	public void changeClanCrest(int crestId)
	{
		// Delete previous crest if existing.
		if (_crestId != 0)
			CrestCache.getInstance().removeCrest(CrestType.PLEDGE, _crestId);
		
		_crestId = crestId;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_CLAN_CREST))
		{
			ps.setInt(1, crestId);
			ps.setInt(2, _clanId);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while updating clan crest.", e);
		}
		
		for (Player member : getOnlineMembers())
			member.broadcastUserInfo();
	}
	
	/**
	 * Change the ally crest. Old crest is removed. New crest id is saved to database.
	 * @param crestId : The new crest id is set and saved to database.
	 * @param onlyThisClan : If false, do it for the ally aswell.
	 */
	public void changeAllyCrest(int crestId, boolean onlyThisClan)
	{
		String query = "UPDATE clan_data SET ally_crest_id = ? WHERE clan_id = ?";
		int allyId = _clanId;
		if (!onlyThisClan)
		{
			// Delete previous crest if existing.
			if (_allyCrestId != 0)
				CrestCache.getInstance().removeCrest(CrestType.ALLY, _allyCrestId);
			
			query = "UPDATE clan_data SET ally_crest_id = ? WHERE ally_id = ?";
			allyId = _allyId;
		}
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(query))
		{
			ps.setInt(1, crestId);
			ps.setInt(2, allyId);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while updating ally crest.", e);
		}
		
		if (onlyThisClan)
		{
			_allyCrestId = crestId;
			for (Player member : getOnlineMembers())
				member.broadcastUserInfo();
		}
		else
		{
			for (Clan clan : ClanTable.getInstance().getClans())
			{
				if (clan.getAllyId() == _allyId)
				{
					clan.setAllyCrestId(crestId);
					for (Player member : clan.getOnlineMembers())
						member.broadcastUserInfo();
				}
			}
		}
	}
	
	/**
	 * Change the large crest. Old crest is removed. New crest id is saved to database.
	 * @param crestId : The new crest id is set and saved to database.
	 */
	public void changeLargeCrest(int crestId)
	{
		// Delete previous crest if existing.
		if (_crestLargeId != 0)
			CrestCache.getInstance().removeCrest(CrestType.PLEDGE_LARGE, _crestLargeId);
		
		_crestLargeId = crestId;
		
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_LARGE_CREST))
		{
			ps.setInt(1, crestId);
			ps.setInt(2, _clanId);
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Error while updating large crest.", e);
		}
		
		for (Player member : getOnlineMembers())
			member.broadcastUserInfo();
	}
	
	/**
	 * Verify if the clan is registered to any siege.
	 * @return true if the clan is registered or owner of a castle
	 */
	public boolean isRegisteredOnSiege()
	{
		for (Castle castle : CastleManager.getInstance().getCastles())
			if (castle.getSiege().checkSides(this))
				return true;
			
		return false;
	}
}