package com.shnok.javaserver.gameserver.model.actor.instance;

import java.util.concurrent.ScheduledFuture;

import com.shnok.javaserver.commons.pool.ThreadPool;

import com.shnok.javaserver.gameserver.data.SkillTable.FrequentSkill;
import com.shnok.javaserver.gameserver.enums.ZoneId;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.template.NpcTemplate;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.TargetUnselected;
import com.shnok.javaserver.gameserver.skills.L2Skill;

/**
 * Christmas trees used on events.<br>
 * The special tree (npcId 13007) emits a regen aura, but only when set outside a peace zone.
 */
public class ChristmasTree extends Folk
{
	public static final int SPECIAL_TREE_ID = 13007;
	
	private ScheduledFuture<?> _aiTask;
	
	public ChristmasTree(int objectId, NpcTemplate template)
	{
		super(objectId, template);
		
		if (template.getNpcId() == SPECIAL_TREE_ID && !isInsideZone(ZoneId.TOWN))
		{
			final L2Skill recoveryAura = FrequentSkill.SPECIAL_TREE_RECOVERY_BONUS.getSkill();
			if (recoveryAura == null)
				return;
			
			_aiTask = ThreadPool.scheduleAtFixedRate(() -> forEachKnownTypeInRadius(Player.class, 200, player ->
			{
				if (player.getFirstEffect(recoveryAura) == null)
					recoveryAura.getEffects(player, player);
			}), 3000, 3000);
		}
	}
	
	@Override
	public void deleteMe()
	{
		if (_aiTask != null)
		{
			_aiTask.cancel(true);
			_aiTask = null;
		}
		super.deleteMe();
	}
	
	@Override
	public void onAction(Player player, boolean isCtrlPressed, boolean isShiftPressed)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
	}

	@Override
	public void onTarget(Player player, boolean isShiftPressed)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		player.sendPacket(new TargetUnselected(player));
	}
}