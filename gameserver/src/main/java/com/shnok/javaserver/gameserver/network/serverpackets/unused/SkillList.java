package com.shnok.javaserver.gameserver.network.serverpackets.unused;

import java.util.ArrayList;
import java.util.List;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.records.SkillInfo;
import com.shnok.javaserver.gameserver.network.serverpackets.L2GameServerPacket;
import com.shnok.javaserver.gameserver.skills.L2Skill;

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