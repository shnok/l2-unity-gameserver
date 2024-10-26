package com.shnok.javaserver.gameserver.model.actor.instance;

import java.text.SimpleDateFormat;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.gameserver.data.xml.ClanHallDecoData;
import net.sf.l2j.gameserver.enums.PrivilegeType;
import net.sf.l2j.gameserver.enums.TeleportType;
import net.sf.l2j.gameserver.enums.actors.NpcTalkCond;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.ai.type.ClanHallManagerNpcAI;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;
import net.sf.l2j.gameserver.model.pledge.Clan;
import net.sf.l2j.gameserver.model.residence.clanhall.ClanHall;
import net.sf.l2j.gameserver.model.residence.clanhall.ClanHallFunction;
import net.sf.l2j.gameserver.model.residence.clanhall.SiegableHall;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.ActionFailed;
import net.sf.l2j.gameserver.network.serverpackets.ClanHallDecoration;
import net.sf.l2j.gameserver.network.serverpackets.NpcHtmlMessage;
import net.sf.l2j.gameserver.network.serverpackets.WarehouseDepositList;
import net.sf.l2j.gameserver.network.serverpackets.WarehouseWithdrawList;

public class ClanHallManagerNpc extends Merchant
{
	private static final String REMOVE_HP = "[<a action=\"bypass -h npc_%objectId%_manage recovery hp_cancel\">Remove</a>]";
	private static final String HP_GRADE_1 = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 2\">40%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 5\">100%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 8\">160%</a>]";
	private static final String HP_GRADE_2 = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 4\">80%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 7\">140%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 10\">200%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 260\">260%</a>]";
	private static final String HP_GRADE_3 = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 4\">80%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 6\">120%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 9\">180%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 12\">240%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 15\">300%</a>]";
	private static final String HP_GRADE_2_SCH = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 25\">300%</a>]";
	private static final String HP_GRADE_3_SCH = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 25\">300%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_hp 30\">400%</a>]";
	
	private static final String REMOVE_EXP = "[<a action=\"bypass -h npc_%objectId%_manage recovery exp_cancel\">Remove</a>]";
	private static final String EXP_GRADE_1 = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 1\">5%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 3\">15%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 6\">30%</a>]";
	private static final String EXP_GRADE_2 = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 1\">5%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 3\">15%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 5\">25%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 8\">40%</a>]";
	private static final String EXP_GRADE_3 = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 3\">15%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 5\">25%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 7\">35%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 10\">50%</a>]";
	private static final String EXP_GRADE_2_SCH = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 19\">45%</a>]";
	private static final String EXP_GRADE_3_SCH = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 19\">45%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_exp 20\">50%</a>]";
	
	private static final String REMOVE_MP = "[<a action=\"bypass -h npc_%objectId%_manage recovery mp_cancel\">Remove</a>]";
	private static final String MP_GRADE_1 = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 1\">5%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 3\">15%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 5\">25%</a>]";
	private static final String MP_GRADE_2 = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 1\">5%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 3\">15%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 6\">30%</a>]";
	private static final String MP_GRADE_3 = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 1\">5%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 3\">15%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 6\">30%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 8\">40%</a>]";
	private static final String MP_GRADE_2_SCH = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 18\">40%</a>]";
	private static final String MP_GRADE_3_SCH = "[<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 18\">40%</a>][<a action=\"bypass -h npc_%objectId%_manage recovery edit_mp 20\">50%</a>]";
	
	private static final String REMOVE_SUPPORT = "[<a action=\"bypass -h npc_%objectId%_manage other support_cancel\">Remove</a>]";
	private static final String SUPPORT_GRADE_1 = "[<a action=\"bypass -h npc_%objectId%_manage other edit_support 1\">Level 1</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_support 2\">Level 2</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_support 4\">Level 4</a>]";
	private static final String SUPPORT_GRADE_2 = "[<a action=\"bypass -h npc_%objectId%_manage other edit_support 3\">Level 3</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_support 4\">Level 4</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_support 5\">Level 5</a>]";
	private static final String SUPPORT_GRADE_3 = "[<a action=\"bypass -h npc_%objectId%_manage other edit_support 3\">Level 3</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_support 5\">Level 5</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_support 7\">Level 7</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_support 8\">Level 8</a>]";
	private static final String SUPPORT_GRADE_2_SCH = "[<a action=\"bypass -h npc_%objectId%_manage other edit_support 15\">Level 5</a>]";
	private static final String SUPPORT_GRADE_3_SCH = "[<a action=\"bypass -h npc_%objectId%_manage other edit_support 15\">Level 5</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_support 18\">Level 8</a>]";
	
	private static final String REMOVE_ITEM = "[<a action=\"bypass -h npc_%objectId%_manage other item_cancel\">Remove</a>]";
	private static final String ITEM = "[<a action=\"bypass -h npc_%objectId%_manage other edit_item 1\">Level 1</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_item 2\">Level 2</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_item 3\">Level 3</a>]";
	private static final String ITEM_SCH = "[<a action=\"bypass -h npc_%objectId%_manage other edit_item 11\">Level 1</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_item 12\">Level 2</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_item 13\">Level 3</a>]";
	
	private static final String REMOVE_TELE = "[<a action=\"bypass -h npc_%objectId%_manage other tele_cancel\">Remove</a>]";
	private static final String TELE = "[<a action=\"bypass -h npc_%objectId%_manage other edit_tele 1\">Level 1</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_tele 2\">Level 2</a>]";
	private static final String TELE_SCH = "[<a action=\"bypass -h npc_%objectId%_manage other edit_tele 11\">Level 1</a>][<a action=\"bypass -h npc_%objectId%_manage other edit_tele 12\">Level 2</a>]";
	
	private static final String REMOVE_CURTAINS = "[<a action=\"bypass -h npc_%objectId%_manage deco curtains_cancel\">Remove</a>]";
	private static final String CURTAINS = "[<a action=\"bypass -h npc_%objectId%_manage deco edit_curtains 1\">Level 1</a>][<a action=\"bypass -h npc_%objectId%_manage deco edit_curtains 2\">Level 2</a>]";
	
	private static final String REMOVE_FIXTURES = "[<a action=\"bypass -h npc_%objectId%_manage deco fixtures_cancel\">Remove</a>]";
	private static final String FIXTURES = "[<a action=\"bypass -h npc_%objectId%_manage deco edit_fixtures 1\">Level 1</a>][<a action=\"bypass -h npc_%objectId%_manage deco edit_fixtures 2\">Level 2</a>]";
	
	private static final String NONE = "none";
	
	public ClanHallManagerNpc(int objectId, NpcTemplate template)
	{
		super(objectId, template);
	}
	
	@Override
	public ClanHallManagerNpcAI getAI()
	{
		return (ClanHallManagerNpcAI) _ai;
	}
	
	@Override
	public void setAI()
	{
		_ai = new ClanHallManagerNpcAI(this);
	}
	
	@Override
	public boolean isWarehouse()
	{
		return true;
	}
	
	@Override
	public void onBypassFeedback(Player player, String command)
	{
		final NpcTalkCond condition = getNpcTalkCond(player);
		if (condition != NpcTalkCond.OWNER)
			return;
		
		final StringTokenizer st = new StringTokenizer(command, " ");
		final String actualCommand = st.nextToken();
		
		String val = (st.hasMoreTokens()) ? st.nextToken() : "";
		
		if (actualCommand.equalsIgnoreCase("banish_foreigner"))
		{
			if (!validatePrivileges(player, PrivilegeType.CHP_RIGHT_TO_DISMISS))
				return;
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			if (val.equalsIgnoreCase("list"))
				html.setFile("data/html/clanHallManager/banish-list.htm");
			else if (val.equalsIgnoreCase("banish"))
			{
				getClanHall().banishForeigners();
				html.setFile("data/html/clanHallManager/banish.htm");
			}
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("manage_vault"))
		{
			if (!validatePrivileges(player, PrivilegeType.SP_WAREHOUSE_SEARCH))
				return;
			
			final boolean isSCH = (getClanHall() instanceof SiegableHall);
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/clanHallManager/vault" + (isSCH ? "-sch" : "") + ".htm");
			html.replace("%rent%", getClanHall().getLease());
			html.replace("%date%", new SimpleDateFormat("dd-MM-yyyy HH:mm").format(getClanHall().getPaidUntil()));
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("door"))
		{
			if (!validatePrivileges(player, PrivilegeType.CHP_ENTRY_EXIT_RIGHTS))
				return;
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			if (val.equalsIgnoreCase("open"))
			{
				getClanHall().openDoors();
				html.setFile("data/html/clanHallManager/door-open.htm");
			}
			else if (val.equalsIgnoreCase("close"))
			{
				getClanHall().closeDoors();
				html.setFile("data/html/clanHallManager/door-close.htm");
			}
			else
				html.setFile("data/html/clanHallManager/door.htm");
			
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("functions"))
		{
			if (!validatePrivileges(player, PrivilegeType.CHP_USE_FUNCTIONS))
				return;
			
			if (val.equalsIgnoreCase("tele"))
			{
				final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_TELEPORT);
				if (chf == null)
				{
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/clanHallManager/functions-disabled.htm");
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
					return;
				}
				
				showTeleportWindow(player, (chf.getLvl() == 2) ? TeleportType.CHF_LEVEL_2 : TeleportType.CHF_LEVEL_1);
			}
			else if (val.equalsIgnoreCase("item_creation"))
			{
				if (!st.hasMoreTokens())
					return;
				
				final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_CREATE_ITEM);
				if (chf == null)
				{
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/clanHallManager/functions-disabled.htm");
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
					return;
				}
				
				showBuyWindow(player, Integer.parseInt(st.nextToken()) + (chf.getLvl() * 100000));
			}
			else if (val.equalsIgnoreCase("support"))
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				
				final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_SUPPORT_MAGIC);
				if (chf == null)
					html.setFile("data/html/clanHallManager/functions-disabled.htm");
				else
				{
					html.setFile("data/html/clanHallManager/support" + chf.getLvl() + ".htm");
					html.replace("%mp%", (int) getStatus().getMp());
				}
				html.replace("%objectId%", getObjectId());
				player.sendPacket(html);
			}
			else
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile("data/html/clanHallManager/functions.htm");
				html.replace("%npcId%", getNpcId());
				html.replace("%objectId%", getObjectId());
				html.replace("%hp_regen%", getClanHall().getFunctionLevel(ClanHall.FUNC_RESTORE_HP));
				html.replace("%mp_regen%", getClanHall().getFunctionLevel(ClanHall.FUNC_RESTORE_MP));
				html.replace("%xp_regen%", getClanHall().getFunctionLevel(ClanHall.FUNC_RESTORE_EXP));
				player.sendPacket(html);
			}
		}
		else if (actualCommand.equalsIgnoreCase("manage"))
		{
			if (!validatePrivileges(player, PrivilegeType.CHP_SET_FUNCTIONS))
				return;
			
			if (val.equalsIgnoreCase("recovery"))
			{
				if (st.hasMoreTokens())
				{
					if (getClanHall().getOwnerId() == 0)
						return;
					
					val = st.nextToken();
					
					if (val.equalsIgnoreCase("hp_cancel"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-cancel.htm");
						html.replace("%apply%", "recovery hp 0");
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("mp_cancel"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-cancel.htm");
						html.replace("%apply%", "recovery mp 0");
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("exp_cancel"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-cancel.htm");
						html.replace("%apply%", "recovery exp 0");
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("edit_hp"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-apply.htm");
						html.replace("%name%", "Fireplace (HP Recovery Device)");
						
						int level = Integer.parseInt(st.nextToken());
						final int funcLvl = level;
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_RESTORE_HP, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_RESTORE_HP, level);
						if (level > 20)
							level -= 10;
						final int percent = level * 20;
						
						html.replace("%cost%", cost + "</font> Adena / " + days + " day(s)</font>)");
						html.replace("%use%", "Provides additional HP recovery for clan members in the clan hall.<font color=\"00FFFF\">" + percent + "%</font>");
						html.replace("%apply%", "recovery hp " + funcLvl);
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("edit_mp"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-apply.htm");
						html.replace("%name%", "Carpet (MP Recovery)");
						
						int level = Integer.parseInt(st.nextToken());
						final int funcLvl = level;
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_RESTORE_MP, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_RESTORE_MP, level);
						if (level > 10)
							level -= 10;
						final int percent = level * 5;
						
						html.replace("%cost%", cost + "</font> Adena / " + days + " day(s)</font>)");
						html.replace("%use%", "Provides additional MP recovery for clan members in the clan hall.<font color=\"00FFFF\">" + percent + "%</font>");
						html.replace("%apply%", "recovery mp " + funcLvl);
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("edit_exp"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-apply.htm");
						html.replace("%name%", "Chandelier (EXP Recovery Device)");
						
						int level = Integer.parseInt(st.nextToken());
						final int funcLvl = level;
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_RESTORE_EXP, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_RESTORE_EXP, level);
						if (level > 10)
							level -= 10;
						final int percent = level * 5;
						
						html.replace("%cost%", cost + "</font> Adena / " + days + " day(s)</font>)");
						html.replace("%use%", "Restores the Exp of any clan member who is resurrected in the clan hall.<font color=\"00FFFF\">" + percent + "%</font>");
						html.replace("%apply%", "recovery exp " + funcLvl);
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("hp"))
					{
						int level = Integer.parseInt(st.nextToken());
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_RESTORE_HP, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_RESTORE_HP, level);
						if (level > 20)
							level -= 10;
						final int percent = level * 20;
						
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						
						final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_RESTORE_HP);
						if (chf != null && chf.getLvl() == percent)
						{
							html.setFile("data/html/clanHallManager/functions-used.htm");
							html.replace("%val%", level + "%");
							html.replace("%objectId%", getObjectId());
							player.sendPacket(html);
							return;
						}
						
						html.setFile("data/html/clanHallManager/functions-apply_confirmed.htm");
						
						int fee = cost;
						if (percent == 0)
						{
							fee = 0;
							html.setFile("data/html/clanHallManager/functions-cancel_confirmed.htm");
						}
						
						if (!getClanHall().updateFunction(player, ClanHall.FUNC_RESTORE_HP, percent, fee, TimeUnit.DAYS.toMillis(days)))
							html.setFile("data/html/clanHallManager/low_adena.htm");
						else
							revalidateDeco(player);
						
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("mp"))
					{
						int level = Integer.parseInt(st.nextToken());
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_RESTORE_MP, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_RESTORE_MP, level);
						if (level > 10)
							level -= 10;
						final int percent = level * 5;
						
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						
						final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_RESTORE_MP);
						if (chf != null && chf.getLvl() == percent)
						{
							html.setFile("data/html/clanHallManager/functions-used.htm");
							html.replace("%val%", level + "%");
							html.replace("%objectId%", getObjectId());
							player.sendPacket(html);
							return;
						}
						
						html.setFile("data/html/clanHallManager/functions-apply_confirmed.htm");
						
						int fee = cost;
						if (percent == 0)
						{
							fee = 0;
							html.setFile("data/html/clanHallManager/functions-cancel_confirmed.htm");
						}
						
						if (!getClanHall().updateFunction(player, ClanHall.FUNC_RESTORE_MP, percent, fee, TimeUnit.DAYS.toMillis(days)))
							html.setFile("data/html/clanHallManager/low_adena.htm");
						else
							revalidateDeco(player);
						
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("exp"))
					{
						int level = Integer.parseInt(st.nextToken());
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_RESTORE_EXP, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_RESTORE_EXP, level);
						if (level > 20)
							level -= 10;
						final int percent = level * 5;
						
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						
						final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_RESTORE_EXP);
						if (chf != null && chf.getLvl() == percent)
						{
							html.setFile("data/html/clanHallManager/functions-used.htm");
							html.replace("%val%", level + "%");
							html.replace("%objectId%", getObjectId());
							player.sendPacket(html);
							return;
						}
						
						html.setFile("data/html/clanHallManager/functions-apply_confirmed.htm");
						
						int fee = cost;
						if (percent == 0)
						{
							fee = 0;
							html.setFile("data/html/clanHallManager/functions-cancel_confirmed.htm");
						}
						
						if (!getClanHall().updateFunction(player, ClanHall.FUNC_RESTORE_EXP, percent, fee, TimeUnit.DAYS.toMillis(days)))
							html.setFile("data/html/clanHallManager/low_adena.htm");
						else
							revalidateDeco(player);
						
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
				}
				else
				{
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/clanHallManager/edit_recovery.htm");
					
					final int grade = getClanHall().getGrade();
					final boolean isSCH = (getClanHall() instanceof SiegableHall);
					
					// Restore HP function.
					ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_RESTORE_HP);
					if (chf != null)
					{
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_RESTORE_HP, chf.getFuncLvl());
						html.replace("%hp_recovery%", chf.getLvl() + "%</font> (<font color=\"FFAABB\">" + chf.getLease() + "</font> Adena / " + days + " day(s))");
						html.replace("%hp_period%", "Next fee at " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(chf.getEndTime()));
						
						switch (grade)
						{
							case 1:
								html.replace("%change_hp%", REMOVE_HP + HP_GRADE_1);
								break;
							
							case 2:
								html.replace("%change_hp%", REMOVE_HP + (isSCH ? HP_GRADE_2_SCH : HP_GRADE_2));
								break;
							
							case 3:
								html.replace("%change_hp%", REMOVE_HP + (isSCH ? HP_GRADE_3_SCH : HP_GRADE_3));
								break;
						}
					}
					else
					{
						html.replace("%hp_recovery%", NONE);
						html.replace("%hp_period%", NONE);
						
						switch (grade)
						{
							case 1:
								html.replace("%change_hp%", HP_GRADE_1);
								break;
							
							case 2:
								html.replace("%change_hp%", (isSCH ? HP_GRADE_2_SCH : HP_GRADE_2));
								break;
							
							case 3:
								html.replace("%change_hp%", (isSCH ? HP_GRADE_3_SCH : HP_GRADE_3));
								break;
						}
					}
					
					// Restore exp function.
					chf = getClanHall().getFunction(ClanHall.FUNC_RESTORE_EXP);
					if (chf != null)
					{
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_RESTORE_EXP, chf.getFuncLvl());
						html.replace("%exp_recovery%", chf.getLvl() + "%</font> (<font color=\"FFAABB\">" + chf.getLease() + "</font> Adena / " + days + " day(s))");
						html.replace("%exp_period%", "Next fee at " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(chf.getEndTime()));
						
						switch (grade)
						{
							case 1:
								html.replace("%change_exp%", REMOVE_EXP + EXP_GRADE_1);
								break;
							
							case 2:
								html.replace("%change_exp%", REMOVE_EXP + (isSCH ? EXP_GRADE_2_SCH : EXP_GRADE_2));
								break;
							
							case 3:
								html.replace("%change_exp%", REMOVE_EXP + (isSCH ? EXP_GRADE_3_SCH : EXP_GRADE_3));
								break;
						}
					}
					else
					{
						html.replace("%exp_recovery%", NONE);
						html.replace("%exp_period%", NONE);
						
						switch (grade)
						{
							case 1:
								html.replace("%change_exp%", EXP_GRADE_1);
								break;
							
							case 2:
								html.replace("%change_exp%", (isSCH ? EXP_GRADE_2_SCH : EXP_GRADE_2));
								break;
							
							case 3:
								html.replace("%change_exp%", (isSCH ? EXP_GRADE_3_SCH : EXP_GRADE_3));
								break;
						}
					}
					
					// Restore MP function.
					chf = getClanHall().getFunction(ClanHall.FUNC_RESTORE_MP);
					if (chf != null)
					{
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_RESTORE_MP, chf.getFuncLvl());
						html.replace("%mp_recovery%", chf.getLvl() + "%</font> (<font color=\"FFAABB\">" + chf.getLease() + "</font> Adena / " + days + " day(s))");
						html.replace("%mp_period%", "Next fee at " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(chf.getEndTime()));
						
						switch (grade)
						{
							case 1:
								html.replace("%change_mp%", REMOVE_MP + MP_GRADE_1);
								break;
							
							case 2:
								html.replace("%change_mp%", REMOVE_MP + (isSCH ? MP_GRADE_2_SCH : MP_GRADE_2));
								break;
							
							case 3:
								html.replace("%change_mp%", REMOVE_MP + (isSCH ? MP_GRADE_3_SCH : MP_GRADE_3));
								break;
						}
					}
					else
					{
						html.replace("%mp_recovery%", NONE);
						html.replace("%mp_period%", NONE);
						
						switch (grade)
						{
							case 1:
								html.replace("%change_mp%", MP_GRADE_1);
								break;
							
							case 2:
								html.replace("%change_mp%", (isSCH ? MP_GRADE_2_SCH : MP_GRADE_2));
								break;
							
							case 3:
								html.replace("%change_mp%", (isSCH ? MP_GRADE_3_SCH : MP_GRADE_3));
								break;
						}
					}
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
				}
			}
			else if (val.equalsIgnoreCase("other"))
			{
				if (st.hasMoreTokens())
				{
					if (getClanHall().getOwnerId() == 0)
						return;
					
					val = st.nextToken();
					
					if (val.equalsIgnoreCase("item_cancel"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-cancel.htm");
						html.replace("%apply%", "other item 0");
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("tele_cancel"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-cancel.htm");
						html.replace("%apply%", "other tele 0");
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("support_cancel"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-cancel.htm");
						html.replace("%apply%", "other support 0");
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("edit_item"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-apply.htm");
						html.replace("%name%", "Magic Equipment (Item Production Facilities)");
						
						int level = Integer.parseInt(st.nextToken());
						final int funcLvl = level;
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_CREATE_ITEM, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_CREATE_ITEM, level);
						if (level > 10)
							level -= 10;
						
						html.replace("%cost%", cost + "</font> Adena / " + days + " day(s)</font>)");
						html.replace("%use%", "Allow the purchase of special items at fixed intervals.");
						html.replace("%apply%", "other item " + funcLvl);
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("edit_support"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-apply.htm");
						html.replace("%name%", "Insignia (Supplementary Magic)");
						
						int level = Integer.parseInt(st.nextToken());
						final int funcLvl = level;
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_SUPPORT_MAGIC, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_SUPPORT_MAGIC, level);
						if (level > 10)
							level -= 10;
						
						html.replace("%cost%", cost + "</font> Adena / " + days + " day(s)</font>)");
						html.replace("%use%", "Enables the use of supplementary magic.");
						html.replace("%apply%", "other support " + funcLvl);
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("edit_tele"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-apply.htm");
						html.replace("%name%", "Mirror (Teleportation Device)");
						
						int level = Integer.parseInt(st.nextToken());
						final int funcLvl = level;
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_TELEPORT, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_TELEPORT, level);
						if (level > 10)
							level -= 10;
						
						html.replace("%cost%", cost + "</font> Adena / " + days + " day(s)</font>)");
						html.replace("%use%", "Teleports clan members in a clan hall to the target <font color=\"00FFFF\">Stage " + level + "</font> staging area");
						html.replace("%apply%", "other tele " + funcLvl);
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("item"))
					{
						if (getClanHall().getOwnerId() == 0)
							return;
						
						int level = Integer.parseInt(st.nextToken());
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_CREATE_ITEM, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_CREATE_ITEM, level);
						if (level > 10)
							level -= 10;
						
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						
						final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_CREATE_ITEM);
						if (chf != null && chf.getLvl() == level)
						{
							html.setFile("data/html/clanHallManager/functions-used.htm");
							html.replace("%val%", "Stage " + val);
							html.replace("%objectId%", getObjectId());
							player.sendPacket(html);
							return;
						}
						
						html.setFile("data/html/clanHallManager/functions-apply_confirmed.htm");
						
						int fee = cost;
						if (level == 0)
						{
							fee = 0;
							html.setFile("data/html/clanHallManager/functions-cancel_confirmed.htm");
						}
						
						if (!getClanHall().updateFunction(player, ClanHall.FUNC_CREATE_ITEM, level, fee, TimeUnit.DAYS.toMillis(days)))
							html.setFile("data/html/clanHallManager/low_adena.htm");
						else
							revalidateDeco(player);
						
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("tele"))
					{
						int level = Integer.parseInt(st.nextToken());
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_TELEPORT, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_TELEPORT, level);
						if (level > 10)
							level -= 10;
						
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						
						final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_TELEPORT);
						if (chf != null && chf.getLvl() == level)
						{
							html.setFile("data/html/clanHallManager/functions-used.htm");
							html.replace("%val%", "Stage " + level);
							html.replace("%objectId%", getObjectId());
							player.sendPacket(html);
							return;
						}
						
						html.setFile("data/html/clanHallManager/functions-apply_confirmed.htm");
						
						int fee = cost;
						if (level == 0)
						{
							fee = 0;
							html.setFile("data/html/clanHallManager/functions-cancel_confirmed.htm");
						}
						
						if (!getClanHall().updateFunction(player, ClanHall.FUNC_TELEPORT, level, fee, TimeUnit.DAYS.toMillis(days)))
							html.setFile("data/html/clanHallManager/low_adena.htm");
						else
							revalidateDeco(player);
						
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("support"))
					{
						int level = Integer.parseInt(st.nextToken());
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_SUPPORT_MAGIC, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_SUPPORT_MAGIC, level);
						if (level > 10)
							level -= 10;
						
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						
						final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_SUPPORT_MAGIC);
						if (chf != null && chf.getLvl() == level)
						{
							html.setFile("data/html/clanHallManager/functions-used.htm");
							html.replace("%val%", "Stage " + val);
							html.replace("%objectId%", getObjectId());
							player.sendPacket(html);
							return;
						}
						
						html.setFile("data/html/clanHallManager/functions-apply_confirmed.htm");
						
						int fee = cost;
						if (level == 0)
						{
							fee = 0;
							html.setFile("data/html/clanHallManager/functions-cancel_confirmed.htm");
						}
						
						if (!getClanHall().updateFunction(player, ClanHall.FUNC_SUPPORT_MAGIC, level, fee, TimeUnit.DAYS.toMillis(days)))
						{
							html.setFile("data/html/clanHallManager/low_adena.htm");
						}
						else
						{
							getAI().resetBuffCheckTime();
							revalidateDeco(player);
						}
						
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
				}
				else
				{
					final boolean isSCH = (getClanHall() instanceof SiegableHall);
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/clanHallManager/edit_other.htm");
					
					ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_TELEPORT);
					if (chf != null)
					{
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_TELEPORT, chf.getFuncLvl());
						html.replace("%tele%", "Stage " + chf.getLvl() + "</font> (<font color=\"FFAABB\">" + chf.getLease() + "</font> Adena / " + days + " day(s))");
						html.replace("%tele_period%", "Next fee at " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(chf.getEndTime()));
						html.replace("%change_tele%", REMOVE_TELE + (isSCH ? TELE_SCH : TELE));
					}
					else
					{
						html.replace("%tele%", NONE);
						html.replace("%tele_period%", NONE);
						html.replace("%change_tele%", (isSCH ? TELE_SCH : TELE));
					}
					
					final int grade = getClanHall().getGrade();
					
					chf = getClanHall().getFunction(ClanHall.FUNC_SUPPORT_MAGIC);
					if (chf != null)
					{
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_SUPPORT_MAGIC, chf.getFuncLvl());
						html.replace("%support%", "Stage " + chf.getLvl() + "</font> (<font color=\"FFAABB\">" + chf.getLease() + "</font> Adena / " + days + " day(s))");
						html.replace("%support_period%", "Next fee at " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(chf.getEndTime()));
						
						switch (grade)
						{
							case 1:
								html.replace("%change_support%", REMOVE_SUPPORT + SUPPORT_GRADE_1);
								break;
							
							case 2:
								html.replace("%change_support%", REMOVE_SUPPORT + (isSCH ? SUPPORT_GRADE_2_SCH : SUPPORT_GRADE_2));
								break;
							
							case 3:
								html.replace("%change_support%", REMOVE_SUPPORT + (isSCH ? SUPPORT_GRADE_3_SCH : SUPPORT_GRADE_3));
								break;
						}
					}
					else
					{
						html.replace("%support%", NONE);
						html.replace("%support_period%", NONE);
						
						switch (grade)
						{
							case 1:
								html.replace("%change_support%", SUPPORT_GRADE_1);
								break;
							
							case 2:
								html.replace("%change_support%", (isSCH ? SUPPORT_GRADE_2_SCH : SUPPORT_GRADE_2));
								break;
							
							case 3:
								html.replace("%change_support%", (isSCH ? SUPPORT_GRADE_3_SCH : SUPPORT_GRADE_3));
								break;
						}
					}
					
					chf = getClanHall().getFunction(ClanHall.FUNC_CREATE_ITEM);
					if (chf != null)
					{
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_CREATE_ITEM, chf.getFuncLvl());
						html.replace("%item%", "Stage " + chf.getLvl() + "</font> (<font color=\"FFAABB\">" + chf.getLease() + "</font> Adena / " + days + " day(s))");
						html.replace("%item_period%", "Next fee at " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(chf.getEndTime()));
						html.replace("%change_item%", REMOVE_ITEM + (isSCH ? ITEM_SCH : ITEM));
					}
					else
					{
						html.replace("%item%", NONE);
						html.replace("%item_period%", NONE);
						html.replace("%change_item%", (isSCH ? ITEM_SCH : ITEM));
					}
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
				}
			}
			else if (val.equalsIgnoreCase("deco"))
			{
				if (st.hasMoreTokens())
				{
					if (getClanHall().getOwnerId() == 0)
						return;
					
					val = st.nextToken();
					if (val.equalsIgnoreCase("curtains_cancel"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-cancel.htm");
						html.replace("%apply%", "deco curtains 0");
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("fixtures_cancel"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-cancel.htm");
						html.replace("%apply%", "deco fixtures 0");
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("edit_curtains"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-apply.htm");
						html.replace("%name%", "Curtains (Decoration)");
						
						int level = Integer.parseInt(st.nextToken());
						final int funcLvl = level;
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_DECO_CURTAINS, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_DECO_CURTAINS, level);
						if (level > 10)
							level -= 10;
						
						html.replace("%cost%", cost + "</font> Adena / " + days + " day(s)</font>)");
						html.replace("%use%", "These curtains can be used to decorate the clan hall.");
						html.replace("%apply%", "deco curtains " + funcLvl);
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("edit_fixtures"))
					{
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						html.setFile("data/html/clanHallManager/functions-apply.htm");
						html.replace("%name%", "Front Platform (Decoration)");
						
						int level = Integer.parseInt(st.nextToken());
						final int funcLvl = level;
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_DECO_FIXTURES, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_DECO_FIXTURES, level);
						if (level > 10)
							level -= 10;
						
						html.replace("%cost%", cost + "</font> Adena / " + days + " day(s)</font>)");
						html.replace("%use%", "Used to decorate the clan hall.");
						html.replace("%apply%", "deco fixtures " + funcLvl);
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("curtains"))
					{
						int level = Integer.parseInt(st.nextToken());
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_DECO_CURTAINS, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_DECO_CURTAINS, level);
						if (level > 10)
							level -= 10;
						
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						
						final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_DECO_CURTAINS);
						if (chf != null && chf.getLvl() == level)
						{
							html.setFile("data/html/clanHallManager/functions-used.htm");
							html.replace("%val%", "Stage " + val);
							html.replace("%objectId%", getObjectId());
							player.sendPacket(html);
							return;
						}
						
						html.setFile("data/html/clanHallManager/functions-apply_confirmed.htm");
						
						int fee = cost;
						if (level == 0)
						{
							fee = 0;
							html.setFile("data/html/clanHallManager/functions-cancel_confirmed.htm");
						}
						
						if (!getClanHall().updateFunction(player, ClanHall.FUNC_DECO_CURTAINS, level, fee, TimeUnit.DAYS.toMillis(days)))
							html.setFile("data/html/clanHallManager/low_adena.htm");
						else
							revalidateDeco(player);
						
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
					else if (val.equalsIgnoreCase("fixtures"))
					{
						int level = Integer.parseInt(st.nextToken());
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_DECO_FIXTURES, level);
						final int cost = ClanHallDecoData.getInstance().getDecoFee(ClanHall.FUNC_DECO_FIXTURES, level);
						if (level > 10)
							level -= 10;
						
						final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
						
						final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_DECO_FIXTURES);
						if (chf != null && chf.getLvl() == level)
						{
							html.setFile("data/html/clanHallManager/functions-used.htm");
							html.replace("%val%", "Stage " + val);
							html.replace("%objectId%", getObjectId());
							player.sendPacket(html);
							return;
						}
						
						html.setFile("data/html/clanHallManager/functions-apply_confirmed.htm");
						
						int fee = cost;
						if (level == 0)
						{
							fee = 0;
							html.setFile("data/html/clanHallManager/functions-cancel_confirmed.htm");
						}
						
						if (!getClanHall().updateFunction(player, ClanHall.FUNC_DECO_FIXTURES, level, fee, TimeUnit.DAYS.toMillis(days)))
							html.setFile("data/html/clanHallManager/low_adena.htm");
						else
							revalidateDeco(player);
						
						html.replace("%objectId%", getObjectId());
						player.sendPacket(html);
					}
				}
				else
				{
					final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
					html.setFile("data/html/clanHallManager/deco.htm");
					
					ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_DECO_CURTAINS);
					if (chf != null)
					{
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_DECO_CURTAINS, chf.getFuncLvl());
						html.replace("%curtain%", "Stage " + chf.getLvl() + "</font>&nbsp;(<font color=\"FFAABB\">" + chf.getLease() + "</font> Adena / " + days + " day(s))");
						html.replace("%curtain_period%", "Next fee at " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(chf.getEndTime()));
						html.replace("%change_curtain%", REMOVE_CURTAINS + CURTAINS);
					}
					else
					{
						html.replace("%curtain%", NONE);
						html.replace("%curtain_period%", NONE);
						html.replace("%change_curtain%", CURTAINS);
					}
					
					chf = getClanHall().getFunction(ClanHall.FUNC_DECO_FIXTURES);
					if (chf != null)
					{
						final int days = ClanHallDecoData.getInstance().getDecoDays(ClanHall.FUNC_DECO_FIXTURES, chf.getFuncLvl());
						html.replace("%fixture%", "Stage " + chf.getLvl() + "</font>&nbsp;(<font color=\"FFAABB\">" + chf.getLease() + "</font> Adena / " + days + " day(s))");
						html.replace("%fixture_period%", "Next fee at " + new SimpleDateFormat("dd-MM-yyyy HH:mm").format(chf.getEndTime()));
						html.replace("%change_fixture%", REMOVE_FIXTURES + FIXTURES);
					}
					else
					{
						html.replace("%fixture%", NONE);
						html.replace("%fixture_period%", NONE);
						html.replace("%change_fixture%", FIXTURES);
					}
					html.replace("%objectId%", getObjectId());
					player.sendPacket(html);
				}
			}
			else if (val.equalsIgnoreCase("back"))
				showChatWindow(player);
			else
			{
				final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
				html.setFile((getClanHall() instanceof SiegableHall) ? "data/html/clanHallManager/manage_sch.htm" : "data/html/clanHallManager/manage.htm");
				html.replace("%objectId%", getObjectId());
				player.sendPacket(html);
			}
		}
		else if (actualCommand.equalsIgnoreCase("support"))
		{
			if (!validatePrivileges(player, PrivilegeType.CHP_USE_FUNCTIONS))
				return;
			
			final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_SUPPORT_MAGIC);
			if (chf == null || chf.getLvl() == 0)
				return;
			
			if (player.isCursedWeaponEquipped())
			{
				// Custom system message
				player.sendMessage("The wielder of a cursed weapon cannot receive outside heals or buffs");
				return;
			}
			
			setTarget(player);
			
			try
			{
				final int id = Integer.parseInt(val);
				final int lvl = (st.hasMoreTokens()) ? Integer.parseInt(st.nextToken()) : 0;
				
				getAI().addCastDesire(player, id, lvl, 1000000);
			}
			catch (Exception e)
			{
				player.sendMessage("Invalid skill, contact your server support.");
			}
		}
		else if (actualCommand.equalsIgnoreCase("list_back"))
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/clanHallManager/chamberlain.htm");
			html.replace("%npcname%", getName());
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("support_back"))
		{
			if (!validatePrivileges(player, PrivilegeType.CHP_USE_FUNCTIONS))
				return;
			
			final ClanHallFunction chf = getClanHall().getFunction(ClanHall.FUNC_SUPPORT_MAGIC);
			if (chf == null || chf.getLvl() == 0)
				return;
			
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/clanHallManager/support" + chf.getLvl() + ".htm");
			html.replace("%mp%", (int) getStatus().getMp());
			html.replace("%objectId%", getObjectId());
			player.sendPacket(html);
		}
		else if (actualCommand.equalsIgnoreCase("WithdrawC"))
		{
			if (!validatePrivileges(player, PrivilegeType.SP_WAREHOUSE_SEARCH))
			{
				player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_CLAN_WAREHOUSE);
				return;
			}
			
			final Clan clan = player.getClan();
			if (clan == null || clan.getLevel() == 0)
			{
				player.sendPacket(SystemMessageId.ONLY_LEVEL_1_CLAN_OR_HIGHER_CAN_USE_WAREHOUSE);
				return;
			}
			
			player.setActiveWarehouse(clan.getWarehouse());
			player.sendPacket(new WarehouseWithdrawList(player, WarehouseWithdrawList.CLAN));
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
		else if (actualCommand.equalsIgnoreCase("DepositC"))
		{
			final Clan clan = player.getClan();
			if (clan == null || clan.getLevel() == 0)
			{
				player.sendPacket(SystemMessageId.ONLY_LEVEL_1_CLAN_OR_HIGHER_CAN_USE_WAREHOUSE);
				return;
			}
			
			player.setActiveWarehouse(clan.getWarehouse());
			player.tempInventoryDisable();
			player.sendPacket(new WarehouseDepositList(player, WarehouseDepositList.CLAN));
			player.sendPacket(ActionFailed.STATIC_PACKET);
		}
		else
			super.onBypassFeedback(player, command);
	}
	
	@Override
	public void showChatWindow(Player player)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
		html.setFile("data/html/clanHallManager/chamberlain" + ((getNpcTalkCond(player) == NpcTalkCond.OWNER) ? ".htm" : "-no.htm"));
		html.replace("%objectId%", getObjectId());
		player.sendPacket(html);
	}
	
	@Override
	protected boolean isTeleportAllowed(Player player)
	{
		return validatePrivileges(player, PrivilegeType.CHP_USE_FUNCTIONS);
	}
	
	@Override
	protected NpcTalkCond getNpcTalkCond(Player player)
	{
		if (getClanHall() != null && player.getClan() != null && getClanHall().getOwnerId() == player.getClanId())
			return NpcTalkCond.OWNER;
		
		return NpcTalkCond.NONE;
	}
	
	private void revalidateDeco(Player player)
	{
		getClanHall().getZone().broadcastPacket(new ClanHallDecoration(getClanHall()));
	}
	
	private boolean validatePrivileges(Player player, PrivilegeType privilege)
	{
		if (!player.hasClanPrivileges(privilege))
		{
			final NpcHtmlMessage html = new NpcHtmlMessage(getObjectId());
			html.setFile("data/html/clanHallManager/not_authorized.htm");
			player.sendPacket(html);
			return false;
		}
		return true;
	}
}