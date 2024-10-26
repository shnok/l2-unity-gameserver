package com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.Warrior;

import com.shnok.javaserver.commons.random.Rnd;
import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.enums.IntentionType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.NpcStringId;
import com.shnok.javaserver.gameserver.scripting.script.ai.individual.Monster.WarriorBase.WarriorBase;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class Warrior extends WarriorBase
{
	public Warrior()
	{
		super("ai/individual/Monster/WarriorBase/Warrior");
	}
	
	public Warrior(String descr)
	{
		super(descr);
	}
	
	protected final int[] _npcIds =
	{
		20001,
		20009,
		20023,
		20028,
		20032,
		20065,
		20069,
		20073,
		20091,
		20095,
		20097,
		20107,
		20116,
		20119,
		20139,
		20142,
		20151,
		20152,
		20163,
		20175,
		20189,
		20255,
		20293,
		20297,
		20298,
		20299,
		20301,
		20303,
		20307,
		20309,
		20318,
		20321,
		20326,
		20328,
		20330,
		20334,
		20336,
		20337,
		20348,
		20358,
		20360,
		20381,
		20382,
		20391,
		20392,
		20393,
		20401,
		20405,
		20406,
		20407,
		20418,
		20440,
		20441,
		20443,
		20444,
		20450,
		20451,
		20465,
		20477,
		20488,
		20492,
		20510,
		20531,
		20533,
		20535,
		20653,
		20742,
		20744,
		21011,
		21094,
		21098,
		21119,
		21124,
		21126,
		21127,
		21132,
		21136,
		21265,
		27001,
		27002,
		27006,
		27007,
		27008,
		27009,
		27018,
		27019,
		27042,
		27046,
		27047,
		27048,
		27049,
		27050,
		27051,
		27052,
		27053,
		27055,
		27063,
		27066,
		27069,
		27078,
		27111,
		27115,
		27199,
		27200,
		18003,
		18231,
		18232,
		18233,
		18234,
		18235,
		18236,
		18237,
		18238,
		18239,
		18240,
		18241,
		18242,
		27154,
		27121,
		20074,
		20077,
		20079,
		27182,
		27183,
		20456,
		20388,
		20373,
		20370,
		27079,
		20317,
		20480,
		20400,
		20389,
		20390,
		20532,
		20775,
		20568,
		27038,
		20349,
		20236,
		20272,
		20316,
		20251,
		20252,
		20203,
		27153,
		27101,
		20013,
		20019,
		20281,
		20536,
		20538,
		20544,
		20539,
		20537,
		20442,
		27155,
		27316,
		21017,
		20008,
		20010,
		20014,
		20433,
		20594,
		20007,
		20651,
		20540,
		27098,
		20121,
		20250,
		20003,
		20322,
		20319,
		20528,
		20325,
		20327,
		20320,
		27179,
		20226,
		20525,
		20335,
		27214,
		27215,
		27216,
		20640,
		20147,
		27058,
		20308,
		20777,
		20286,
		20403,
		20274,
		20004,
		20005,
		27061,
		27067,
		27064,
		27059,
		20468,
		20473,
		20470,
		20206,
		21117,
		20476,
		20474,
		20475,
		20481,
		27021,
		20487,
		20030,
		20027,
		20357,
		27116,
		20577,
		27122,
		27123,
		20580,
		20919,
		20922,
		20363,
		20232,
		27022,
		20369,
		20365,
		27117,
		27016,
		20089,
		20575,
		27130,
		27031,
		20547,
		20031,
		20130,
		27054,
		20096,
		20131,
		20598,
		20106,
		27317,
		20778,
		20779,
		20311,
		20312,
		20359,
		20021,
		20534,
		27086,
		20551,
		20054,
		27171,
		20112,
		20114,
		20039,
		27090,
		20514,
		20168,
		27097,
		27096,
		27095,
		27100,
		20515,
		20104,
		27003,
		27004,
		27005,
		20509,
		20200,
		20040,
		20630,
		20573,
		20570,
		20583,
		20035,
		20560,
		20062,
		27070,
		18342,
		20192,
		27077,
		20446,
		20448,
		27020,
		20132,
		20553,
		20120,
		20529,
		20545,
		20530,
		18339,
		22001,
		22089,
		22090,
		22097,
		22098,
		27319,
		27320,
		27321,
		27322,
		27201
	};
	
	@Override
	public void onNoDesire(Npc npc)
	{
		npc.getAI().addWanderDesire(5, 5);
	}
	
	@Override
	public void onCreated(Npc npc)
	{
		final int shoutMsg1 = getNpcIntAIParam(npc, "ShoutMsg1");
		if (shoutMsg1 > 0)
			npc.broadcastNpcShout(NpcStringId.getNpcMessage(shoutMsg1));
		
		if (getNpcIntAIParam(npc, "MoveAroundSocial") > 0 || getNpcIntAIParam(npc, "ShoutMsg2") > 0 || getNpcIntAIParam(npc, "ShoutMsg3") > 0)
			startQuestTimerAtFixedRate("1001", npc, null, 10000, 10000);
		
		super.onCreated(npc);
	}
	
	@Override
	public String onTimer(String name, Npc npc, Player player)
	{
		if (name.equalsIgnoreCase("1001"))
		{
			switch (npc.getAI().getCurrentIntention().getType())
			{
				case WANDER, IDLE, MOVE_TO:
					if (!npc.isDead() && npc.getStatus().getHpRatio() > 0.4)
					{
						final int moveAroundSocial2 = getNpcIntAIParam(npc, "MoveAroundSocial2");
						final int moveAroundSocial1 = getNpcIntAIParam(npc, "MoveAroundSocial1");
						final int moveAroundSocial = getNpcIntAIParam(npc, "MoveAroundSocial");
						
						if (moveAroundSocial2 > 0 && Rnd.get(100) < 20)
							npc.getAI().addSocialDesire(3, moveAroundSocial2 * 1000 / 30, 50);
						else if (moveAroundSocial1 > 0 && Rnd.get(100) < 20)
							npc.getAI().addSocialDesire(2, moveAroundSocial1 * 1000 / 30, 50);
						else if (moveAroundSocial > 0 && Rnd.get(100) < 20)
							npc.getAI().addSocialDesire(1, moveAroundSocial * 1000 / 30, 50);
						
						final int shoutMsg2 = getNpcIntAIParam(npc, "ShoutMsg2");
						if (shoutMsg2 > 0 && Rnd.get(1000) < 17)
							npc.broadcastNpcShout(NpcStringId.getNpcMessage(shoutMsg2));
					}
					break;
				
				case ATTACK:
					final int shoutMsg3 = getNpcIntAIParam(npc, "ShoutMsg3");
					if (shoutMsg3 > 0 && Rnd.get(100) < 10)
						npc.broadcastNpcShout(NpcStringId.getNpcMessage(shoutMsg3));
					break;
			}
		}
		return super.onTimer(name, npc, player);
	}
	
	@Override
	public void onAttacked(Npc npc, Creature attacker, int damage, L2Skill skill)
	{
		double initialHateRatio = getHateRatio(npc, attacker);
		double hateRatio = initialHateRatio;
		
		if (attacker instanceof Playable)
		{
			hateRatio = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((hateRatio / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
			
			npc.getAI().addAttackDesire(attacker, hateRatio * 100);
		}
		
		final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
		if (mostHated != null)
		{
			if (npc.getMove().getGeoPathFailCount() >= 10 && attacker == mostHated && npc.getStatus().getHpRatio() != 1.0)
			{
				npc.abortAll(false);
				npc.instantTeleportTo(attacker.getX(), attacker.getY(), attacker.getZ(), 0);
			}
			
			final int i0 = getAbnormalLevel(npc, 1201, 1);
			if (i0 >= 0 && npc.distance2D(attacker) > 40)
			{
				if (npc.getAttack().canAttack(mostHated))
				{
					if (attacker instanceof Playable)
					{
						hateRatio = initialHateRatio;
						hateRatio = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((hateRatio / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
						
						npc.getAI().addAttackDesire(attacker, hateRatio * 100);
					}
				}
				else
				{
					npc.getAI().getAggroList().stopHate(mostHated);
					
					if (attacker instanceof Playable)
					{
						hateRatio = initialHateRatio;
						hateRatio = (((1.0 * damage) / (npc.getStatus().getLevel() + 7)) + ((hateRatio / 100) * ((1.0 * damage) / (npc.getStatus().getLevel() + 7))));
						
						npc.getAI().addAttackDesire(attacker, hateRatio * 100);
					}
				}
			}
		}
		super.onAttacked(npc, attacker, damage, skill);
	}
	
	@Override
	public void onClanAttacked(Npc caller, Npc called, Creature attacker, int damage, L2Skill skill)
	{
		if (called.getAI().getLifeTime() > 7)
		{
			final Creature mostHated = called.getAI().getAggroList().getMostHatedCreature();
			if (mostHated != null && attacker == mostHated && called.getStatus().getHpRatio() != 1.0 && called.getMove().getGeoPathFailCount() >= 10)
			{
				called.abortAll(false);
				called.instantTeleportTo(attacker.getX(), attacker.getY(), attacker.getZ(), 0);
			}
			
			if (attacker instanceof Playable)
			{
				double hateRatio = getHateRatio(called, attacker);
				hateRatio = (((1.0 * damage) / (called.getStatus().getLevel() + 7)) + ((hateRatio / 100) * ((1.0 * damage) / (called.getStatus().getLevel() + 7))));
				
				called.getAI().addAttackDesire(attacker, (int) (hateRatio * 100));
			}
		}
	}
	
	@Override
	public void onSeeSpell(Npc npc, Player caster, L2Skill skill, Creature[] targets, boolean isPet)
	{
		final Creature mostHated = npc.getAI().getAggroList().getMostHatedCreature();
		
		if ((skill.getAggroPoints() > 0 || skill.getPower() > 0 || skill.isOffensive()) && npc.getAI().getCurrentIntention().getType() == IntentionType.ATTACK && ArraysUtil.contains(targets, mostHated))
		{
			double i0 = Math.max(Math.max(skill.getAggroPoints(), skill.getPower(targets[0])), 20);
			double hateRatio = getHateRatio(npc, caster);
			hateRatio = (((1.0 * i0) / (npc.getStatus().getLevel() + 7)) + ((hateRatio / 100) * ((1.0 * i0) / (npc.getStatus().getLevel() + 7))));
			
			npc.getAI().addAttackDesire(caster, hateRatio * 150);
		}
		
		if (caster == mostHated && npc.getStatus().getHpRatio() != 1.0 && npc.getMove().getGeoPathFailCount() >= 10)
		{
			npc.abortAll(false);
			npc.instantTeleportTo(caster.getX(), caster.getY(), caster.getZ(), 0);
		}
	}
	
	// EventHandler DESIRE_MANIPULATION(speller,desire)
	// {
	// myself::MakeAttackEvent(speller,desire,0);
	// }
	
	@Override
	public void onMyDying(Npc npc, Creature killer)
	{
		final int shoutMsg4 = getNpcIntAIParam(npc, "ShoutMsg4");
		if (shoutMsg4 > 0 && Rnd.get(100) < 30)
			npc.broadcastNpcShout(NpcStringId.getNpcMessage(shoutMsg4));
	}
}