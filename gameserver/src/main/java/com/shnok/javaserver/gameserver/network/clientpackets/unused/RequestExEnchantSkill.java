package com.shnok.javaserver.gameserver.network.clientpackets.unused;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.SkillTable;
import com.shnok.javaserver.gameserver.data.xml.SkillTreeData;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.Folk;
import com.shnok.javaserver.gameserver.model.holder.skillnode.EnchantSkillNode;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.SkillList;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.network.serverpackets.actor.UserInfo;
import com.shnok.javaserver.gameserver.skills.L2Skill;

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