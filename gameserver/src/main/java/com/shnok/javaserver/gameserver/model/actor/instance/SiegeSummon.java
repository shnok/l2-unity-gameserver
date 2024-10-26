package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class SiegeSummon extends Servitor
{
	public static final int SIEGE_GOLEM_ID = 14737;
	public static final int HOG_CANNON_ID = 14768;
	public static final int SWOOP_CANNON_ID = 14839;
	
	public SiegeSummon(int objectId, NpcTemplate template, Player owner, L2Skill skill)
	{
		super(objectId, template, owner, skill);
	}
	
	@Override
	public void onSpawn()
	{
		super.onSpawn();
		
		if (!isInsideZone(ZoneId.SIEGE))
		{
			unSummon(getOwner());
			getOwner().sendPacket(SystemMessageId.YOUR_SERVITOR_HAS_VANISHED);
		}
	}
	
	@Override
	public void onTeleported()
	{
		if (!isInsideZone(ZoneId.SIEGE))
		{
			unSummon(getOwner());
			getOwner().sendPacket(SystemMessageId.YOUR_SERVITOR_HAS_VANISHED);
			return;
		}
		
		super.onTeleported();
	}
}