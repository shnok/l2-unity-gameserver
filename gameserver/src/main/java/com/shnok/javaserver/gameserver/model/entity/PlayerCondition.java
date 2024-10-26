package com.shnok.javaserver.gameserver.model.entity;

import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.location.Location;

/**
 * This class hold important player informations, which will be restored on duel end.
 */
public class PlayerCondition
{
	private Player _player;
	
	private double _hp;
	private double _mp;
	private double _cp;
	
	private Location _loc;
	
	public PlayerCondition(Player player, boolean partyDuel)
	{
		if (player == null)
			return;
		
		_player = player;
		_hp = _player.getStatus().getHp();
		_mp = _player.getStatus().getMp();
		_cp = _player.getStatus().getCp();
		
		if (partyDuel)
			_loc = _player.getPosition().clone();
		
		_player.storeEffect(true);
	}
	
	public void restoreCondition(boolean abnormalEnd)
	{
		if (_loc != null)
			_player.teleportTo(_loc, 0);
		
		if (abnormalEnd)
			return;
		
		_player.getStatus().setCpHpMp(_cp, _hp, _mp);
		
		_player.stopAllEffects();
		_player.restoreEffects();
	}
	
	public Player getPlayer()
	{
		return _player;
	}
}