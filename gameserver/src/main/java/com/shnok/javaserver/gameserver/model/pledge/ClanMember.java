package com.shnok.javaserver.gameserver.model.pledge;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.pool.ConnectionPool;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.enums.actors.ClassRace;
import com.shnok.javaserver.gameserver.enums.actors.Sex;
import com.shnok.javaserver.gameserver.model.actor.Player;

public class ClanMember
{
	private static final CLogger LOGGER = new CLogger(ClanMember.class.getName());
	
	private static final String UPDATE_PLEDGE = "UPDATE characters SET subpledge=? WHERE obj_id=?";
	private static final String UPDATE_POWER_GRADE = "UPDATE characters SET power_grade=? WHERE obj_id=?";
	private static final String UPDATE_SPONSOR = "UPDATE characters SET apprentice=?,sponsor=? WHERE obj_Id=?";
	
	private final Clan _clan;
	
	private Player _player;
	
	private String _name;
	private String _title;
	
	private Sex _sex;
	private ClassRace _race;
	
	private int _objectId;
	private int _level;
	private int _classId;
	private int _pledgeType;
	private int _powerGrade;
	private int _apprentice;
	private int _sponsor;
	
	/**
	 * Restore a {@link ClanMember} from the database.
	 * @param clan : The {@link Clan} holding this {@link ClanMember}.
	 * @param rs : The {@link ResultSet} used to feed data.
	 * @throws SQLException if the columnLabel is not valid or a database error occurs.
	 */
	public ClanMember(Clan clan, ResultSet rs) throws SQLException
	{
		_clan = clan;
		
		_name = rs.getString("char_name");
		_title = rs.getString("title");
		
		_sex = Sex.VALUES[rs.getInt("sex")];
		_race = ClassRace.VALUES[rs.getInt("race")];
		
		_objectId = rs.getInt("obj_Id");
		_level = rs.getInt("level");
		_classId = rs.getInt("classid");
		_pledgeType = rs.getInt("subpledge");
		_powerGrade = rs.getInt("power_grade");
		_apprentice = rs.getInt("apprentice");
		_sponsor = rs.getInt("sponsor");
	}
	
	/**
	 * Create a {@link ClanMember} based on a {@link Player} instance.
	 * @param clan : The {@link Clan} holding this {@link ClanMember}.
	 * @param player : The {@link Player} instance used to create the {@link ClanMember}.
	 */
	public ClanMember(Clan clan, Player player)
	{
		_clan = clan;
		
		_player = player;
		
		_name = player.getName();
		_title = player.getTitle();
		
		_sex = player.getAppearance().getSex();
		_race = player.getRace();
		
		_objectId = player.getObjectId();
		_level = player.getStatus().getLevel();
		_classId = player.getClassId().getId();
		_pledgeType = player.getPledgeType();
		_powerGrade = player.getPowerGrade();
		_apprentice = 0;
		_sponsor = 0;
	}
	
	public void setPlayerInstance(Player player)
	{
		if (player == null && _player != null)
		{
			_name = _player.getName();
			_title = _player.getTitle();
			
			_sex = _player.getAppearance().getSex();
			_race = _player.getRace();
			
			_objectId = _player.getObjectId();
			_level = _player.getStatus().getLevel();
			_classId = _player.getClassId().getId();
			_powerGrade = _player.getPowerGrade();
			_pledgeType = _player.getPledgeType();
			_apprentice = _player.getApprentice();
			_sponsor = _player.getSponsor();
		}
		
		// Add Clan skills.
		if (player != null)
		{
			_clan.checkAndAddClanSkills(player);
			
			if (_clan.getLevel() >= Config.MINIMUM_CLAN_LEVEL && player.isClanLeader())
				player.addSiegeSkills();
		}
		_player = player;
	}
	
	public Clan getClan()
	{
		return _clan;
	}
	
	public Player getPlayerInstance()
	{
		return _player;
	}
	
	public String getName()
	{
		return (_player != null) ? _player.getName() : _name;
	}
	
	public String getTitle()
	{
		return (_player != null) ? _player.getTitle() : _title;
	}
	
	public Sex getSex()
	{
		return (_player != null) ? _player.getAppearance().getSex() : _sex;
	}
	
	public ClassRace getRace()
	{
		return (_player != null) ? _player.getRace() : _race;
	}
	
	public int getObjectId()
	{
		return (_player != null) ? _player.getObjectId() : _objectId;
	}
	
	public int getLevel()
	{
		return (_player != null) ? _player.getStatus().getLevel() : _level;
	}
	
	public void refreshLevel()
	{
		if (_player != null)
			_level = _player.getStatus().getLevel();
	}
	
	public int getClassId()
	{
		return (_player != null) ? _player.getClassId().getId() : _classId;
	}
	
	public boolean isOnline()
	{
		return _player != null && _player.getClient() != null && !_player.getClient().isDetached();
	}
	
	public int getPledgeType()
	{
		return (_player != null) ? _player.getPledgeType() : _pledgeType;
	}
	
	public void setPledgeType(int pledgeType)
	{
		_pledgeType = pledgeType;
		
		if (_player != null)
			_player.setPledgeType(pledgeType);
		else
		{
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE_PLEDGE))
			{
				ps.setInt(1, _pledgeType);
				ps.setInt(2, getObjectId());
				ps.executeUpdate();
			}
			catch (Exception e)
			{
				LOGGER.error("Couldn't update ClanMember pledge.", e);
			}
		}
	}
	
	public int getPowerGrade()
	{
		return (_player != null) ? _player.getPowerGrade() : _powerGrade;
	}
	
	public void setPowerGrade(int powerGrade)
	{
		_powerGrade = powerGrade;
		
		if (_player != null)
			_player.setPowerGrade(powerGrade);
		else
		{
			try (Connection con = ConnectionPool.getConnection();
				PreparedStatement ps = con.prepareStatement(UPDATE_POWER_GRADE))
			{
				ps.setInt(1, _powerGrade);
				ps.setInt(2, getObjectId());
				ps.executeUpdate();
			}
			catch (Exception e)
			{
				LOGGER.error("Couldn't update ClanMember power grade.", e);
			}
		}
	}
	
	public int getSponsor()
	{
		return (_player != null) ? _player.getSponsor() : _sponsor;
	}
	
	public int getApprentice()
	{
		return (_player != null) ? _player.getApprentice() : _apprentice;
	}
	
	public String getApprenticeOrSponsorName()
	{
		if (_player != null)
		{
			_apprentice = _player.getApprentice();
			_sponsor = _player.getSponsor();
		}
		
		if (_apprentice != 0)
		{
			ClanMember apprentice = _clan.getClanMember(_apprentice);
			if (apprentice != null)
				return apprentice.getName();
			
			return "Error";
		}
		
		if (_sponsor != 0)
		{
			ClanMember sponsor = _clan.getClanMember(_sponsor);
			if (sponsor != null)
				return sponsor.getName();
			
			return "Error";
		}
		return "";
	}
	
	public void setApprenticeAndSponsor(int apprenticeId, int sponsorId)
	{
		_apprentice = apprenticeId;
		_sponsor = sponsorId;
	}
	
	/**
	 * Store the apprentice and sponsor informations related to this {@link ClanMember} into database.
	 * @param apprentice : The apprentice objectId to set.
	 * @param sponsor : The sponsor objectId to set.
	 */
	public void saveApprenticeAndSponsor(int apprentice, int sponsor)
	{
		try (Connection con = ConnectionPool.getConnection();
			PreparedStatement ps = con.prepareStatement(UPDATE_SPONSOR))
		{
			ps.setInt(1, apprentice);
			ps.setInt(2, sponsor);
			ps.setInt(3, getObjectId());
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			LOGGER.error("Couldn't update ClanMember sponsor/apprentice.", e);
		}
	}
	
	/**
	 * Calculate the pledge level of the {@link Player} set as parameter.
	 * @param player : The {@link Player} to test.
	 * @return The pledge class associated to the {@link Player} set as parameter.
	 */
	public static int calculatePledgeClass(Player player)
	{
		int pledgeClass = 0;
		
		final Clan clan = player.getClan();
		if (clan != null)
		{
			switch (clan.getLevel())
			{
				case 4:
					if (player.isClanLeader())
						pledgeClass = 3;
					break;
				
				case 5:
					if (player.isClanLeader())
						pledgeClass = 4;
					else
						pledgeClass = 2;
					break;
				
				case 6:
					switch (player.getPledgeType())
					{
						case -1:
							pledgeClass = 1;
							break;
						
						case 100, 200:
							pledgeClass = 2;
							break;
						
						case 0:
							if (player.isClanLeader())
								pledgeClass = 5;
							else
								switch (clan.getLeaderSubPledge(player.getObjectId()))
								{
									case 100, 200:
										pledgeClass = 4;
										break;
									
									case -1:
									default:
										pledgeClass = 3;
										break;
								}
							break;
					}
					break;
				
				case 7:
					switch (player.getPledgeType())
					{
						case -1:
							pledgeClass = 1;
							break;
						
						case 100, 200:
							pledgeClass = 3;
							break;
						
						case 1001, 1002, 2001, 2002:
							pledgeClass = 2;
							break;
						
						case 0:
							if (player.isClanLeader())
								pledgeClass = 7;
							else
								switch (clan.getLeaderSubPledge(player.getObjectId()))
								{
									case 100, 200:
										pledgeClass = 6;
										break;
									
									case 1001, 1002, 2001, 2002:
										pledgeClass = 5;
										break;
									
									case -1:
									default:
										pledgeClass = 4;
										break;
								}
							break;
					}
					break;
				
				case 8:
					switch (player.getPledgeType())
					{
						case -1:
							pledgeClass = 1;
							break;
						
						case 100, 200:
							pledgeClass = 4;
							break;
						
						case 1001, 1002, 2001, 2002:
							pledgeClass = 3;
							break;
						
						case 0:
							if (player.isClanLeader())
								pledgeClass = 8;
							else
								switch (clan.getLeaderSubPledge(player.getObjectId()))
								{
									case 100, 200:
										pledgeClass = 7;
										break;
									
									case 1001, 1002, 2001, 2002:
										pledgeClass = 6;
										break;
									
									case -1:
									default:
										pledgeClass = 5;
										break;
								}
							break;
					}
					break;
				
				default:
					pledgeClass = 1;
					break;
			}
		}
		
		if (player.isHero() && pledgeClass < 8)
			pledgeClass = 8;
		else if (player.isNoble() && pledgeClass < 5)
			pledgeClass = 5;
		
		return pledgeClass;
	}
}