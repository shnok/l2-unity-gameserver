package com.shnok.javaserver.gameserver.handler.skillhandlers;

import com.shnok.javaserver.gameserver.data.manager.CastleManorManager;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.handler.ISkillHandler;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Monster;
import com.shnok.javaserver.gameserver.model.item.instance.ItemInstance;
import com.shnok.javaserver.gameserver.model.manor.Seed;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.skills.Formulas;
import com.shnok.javaserver.gameserver.skills.L2Skill;

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