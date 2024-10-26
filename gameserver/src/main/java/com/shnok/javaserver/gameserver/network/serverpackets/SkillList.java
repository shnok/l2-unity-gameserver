package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.records.SkillInfo;
import net.sf.l2j.gameserver.skills.L2Skill;

public final class SkillList extends L2GameServerPacket
{
	private final List<SkillInfo> _skills = new ArrayList<>();
	
	public SkillList(Player player)
	{
		final boolean isWearingFormalWear = player.isWearingFormalWear();
		final boolean isClanDisabled = player.getClan() != null && player.getClan().getReputationScore() < 0;
		
		for (final L2Skill skill : player.getSkills().values())
			_skills.add(new SkillInfo(skill.getId(), skill.getLevel(), skill.isPassive(), isWearingFormalWear || (skill.isClanSkill() && isClanDisabled)));
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x58);
		writeD(_skills.size());
		
		for (SkillInfo temp : _skills)
		{
			writeD(temp.isPassive() ? 1 : 0);
			writeD(temp.level());
			writeD(temp.id());
			writeC(temp.isDisabled() ? 1 : 0);
		}
	}
}