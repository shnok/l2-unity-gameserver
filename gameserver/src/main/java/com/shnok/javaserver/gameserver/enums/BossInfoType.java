package com.shnok.javaserver.gameserver.enums;

import com.shnok.javaserver.gameserver.model.group.CommandChannel;
import com.shnok.javaserver.gameserver.network.NpcStringId;

/**
 * Related informations regarding boss.
 */
public enum BossInfoType
{
	AQ(29001, NpcStringId.ID_1800001, NpcStringId.ID_1800005, 36),
	CORE(29006, NpcStringId.ID_1800002, NpcStringId.ID_1800006, 36),
	ORFEN(29014, NpcStringId.ID_1800003, NpcStringId.ID_1800007, 36),
	ZAKEN(29022, NpcStringId.ID_1800004, NpcStringId.ID_1800008, 36),
	REGULAR(0, NpcStringId.ID_1800009, NpcStringId.ID_1800010, 18),
	BAIUM(29020, NpcStringId.ID_1800009, NpcStringId.ID_1800010, 36),
	ANTHARAS(29019, NpcStringId.ID_1800009, NpcStringId.ID_1800010, 225),
	VALAKAS(29028, NpcStringId.ID_1800009, NpcStringId.ID_1800010, 36);
	
	public static final BossInfoType[] VALUES = values();
	
	private final int _npcId;
	private final NpcStringId _ccRightsMsg;
	private final NpcStringId _ccNoRightsMsg;
	private final int _requiredMembersAmount;
	
	private BossInfoType(int npcId, NpcStringId ccRightsMsg, NpcStringId ccNoRightsMsg, int requiredMembersAmount)
	{
		_npcId = npcId;
		_ccRightsMsg = ccRightsMsg;
		_ccNoRightsMsg = ccNoRightsMsg;
		_requiredMembersAmount = requiredMembersAmount;
	}
	
	public int getNpcId()
	{
		return _npcId;
	}
	
	public NpcStringId getCcRightsMsg()
	{
		return _ccRightsMsg;
	}
	
	public NpcStringId getCcNoRightsMsg()
	{
		return _ccNoRightsMsg;
	}
	
	public int getRequiredMembersAmount()
	{
		return _requiredMembersAmount;
	}
	
	public static BossInfoType getBossInfo(int npcId)
	{
		for (BossInfoType bit : VALUES)
			if (bit.getNpcId() == npcId)
				return bit;
			
		return REGULAR;
	}
	
	public static boolean isCcMeetCondition(CommandChannel cc, int npcId)
	{
		return cc != null && cc.getMembersCount() > getBossInfo(npcId).getRequiredMembersAmount();
	}
}