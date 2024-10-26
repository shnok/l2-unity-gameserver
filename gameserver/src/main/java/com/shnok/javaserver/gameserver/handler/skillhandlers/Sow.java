package com.shnok.javaserver.gameserver.handler.skillhandlers;

import net.sf.l2j.gameserver.data.manager.CastleManorManager;
import net.sf.l2j.gameserver.enums.skills.SkillType;
import net.sf.l2j.gameserver.handler.ISkillHandler;
import net.sf.l2j.gameserver.model.WorldObject;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Monster;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.manor.Seed;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.Formulas;
import net.sf.l2j.gameserver.skills.L2Skill;

public class Sow implements ISkillHandler
{
	private static final SkillType[] SKILL_IDS =
	{
		SkillType.SOW
	};
	
	@Override
	public void useSkill(Creature creature, L2Skill skill, WorldObject[] targets, ItemInstance item)
	{
		// Must be used from an item, must be called by a Player.
		if (item == null || !(creature instanceof Player player))
			return;
		
		// Target must be a Monster.
		if (!(targets[0] instanceof Monster targetMonster))
			return;
		
		// Target must be dead.
		if (targetMonster.isDead())
			return;
		
		// Target mustn't be already in seeded state.
		if (targetMonster.getSeedState().isSeeded())
		{
			player.sendPacket(SystemMessageId.THE_SEED_HAS_BEEN_SOWN);
			return;
		}
		
		// Seed must exist.
		final Seed seed = CastleManorManager.getInstance().getSeed(item.getItemId());
		if (seed == null)
			return;
		
		// Calculate the sow success rate.
		if (!Formulas.calcSowSuccess(player, targetMonster, seed))
		{
			player.sendPacket(SystemMessageId.THE_SEED_WAS_NOT_SOWN);
			return;
		}
		
		targetMonster.getSeedState().setSeeded(player, seed);
		
		player.sendPacket(SystemMessageId.THE_SEED_WAS_SUCCESSFULLY_SOWN);
	}
	
	@Override
	public SkillType[] getSkillIds()
	{
		return SKILL_IDS;
	}
}