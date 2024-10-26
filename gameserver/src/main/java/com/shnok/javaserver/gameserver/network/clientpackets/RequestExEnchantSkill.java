package com.shnok.javaserver.gameserver.network.clientpackets;

import net.sf.l2j.commons.random.Rnd;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.xml.SkillTreeData;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.actor.instance.Folk;
import net.sf.l2j.gameserver.model.holder.skillnode.EnchantSkillNode;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.SkillList;
import net.sf.l2j.gameserver.network.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.network.serverpackets.UserInfo;
import net.sf.l2j.gameserver.skills.L2Skill;

public final class RequestExEnchantSkill extends L2GameClientPacket
{
	private int _skillId;
	private int _skillLevel;
	
	@Override
	protected void readImpl()
	{
		_skillId = readD();
		_skillLevel = readD();
	}
	
	@Override
	protected void runImpl()
	{
		if (_skillId <= 0 || _skillLevel <= 0)
			return;
		
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
		
		if (player.getClassId().getLevel() < 3 || player.getStatus().getLevel() < 76)
			return;
		
		final Folk folk = player.getCurrentFolk();
		if (folk == null || !player.getAI().canDoInteract(folk))
			return;
		
		if (player.getSkillLevel(_skillId) >= _skillLevel)
			return;
		
		final L2Skill skill = SkillTable.getInstance().getInfo(_skillId, _skillLevel);
		if (skill == null)
			return;
		
		final EnchantSkillNode esn = SkillTreeData.getInstance().getEnchantSkillFor(player, _skillId, _skillLevel);
		if (esn == null)
			return;
		
		// Check exp and sp neccessary to enchant skill.
		if (player.getStatus().getSp() < esn.getSp())
		{
			player.sendPacket(SystemMessageId.YOU_DONT_HAVE_ENOUGH_SP_TO_ENCHANT_THAT_SKILL);
			return;
		}
		
		if (player.getStatus().getExp() - esn.getExp() < player.getStatus().getExpForLevel(76))
		{
			player.sendPacket(SystemMessageId.YOU_DONT_HAVE_ENOUGH_EXP_TO_ENCHANT_THAT_SKILL);
			return;
		}
		
		// Check item restriction, and try to consume item.
		if (Config.ES_SP_BOOK_NEEDED && esn.getItem() != null && !player.destroyItemByItemId(esn.getItem().getId(), esn.getItem().getValue(), true))
		{
			player.sendPacket(SystemMessageId.YOU_DONT_HAVE_ALL_OF_THE_ITEMS_NEEDED_TO_ENCHANT_THAT_SKILL);
			return;
		}
		
		// All conditions fulfilled, consume exp and sp.
		player.removeExpAndSp(esn.getExp(), esn.getSp());
		
		// Try to enchant skill.
		if (Rnd.get(100) <= esn.getEnchantRate(player.getStatus().getLevel()))
		{
			player.addSkill(skill, true, true);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_SUCCEEDED_IN_ENCHANTING_THE_SKILL_S1).addSkillName(_skillId, _skillLevel));
		}
		else
		{
			player.addSkill(SkillTable.getInstance().getInfo(_skillId, SkillTable.getInstance().getMaxLevel(_skillId)), true, true);
			player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_FAILED_TO_ENCHANT_THE_SKILL_S1).addSkillName(_skillId, _skillLevel));
		}
		
		player.sendPacket(new SkillList(player));
		player.sendPacket(new UserInfo(player));
		
		// Show enchant skill list.
		folk.showEnchantSkillList(player);
	}
}