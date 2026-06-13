package com.shnok.javaserver.gameserver.skills.effects;

import com.shnok.javaserver.gameserver.enums.skills.EffectFlag;
import com.shnok.javaserver.gameserver.enums.skills.EffectType;
import com.shnok.javaserver.gameserver.enums.skills.FlyType;
import com.shnok.javaserver.gameserver.geoengine.GeoEngine;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.location.Location;
import com.shnok.javaserver.gameserver.network.serverpackets.unused.FlyToLocation;
import com.shnok.javaserver.gameserver.network.serverpackets.movement.ValidateLocation;
import com.shnok.javaserver.gameserver.skills.AbstractEffect;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class EffectThrowUp extends AbstractEffect
{
	private int _x;
	private int _y;
	private int _z;
	
	public EffectThrowUp(EffectTemplate template, L2Skill skill, Creature effected, Creature effector)
	{
		super(template, skill, effected, effector);
	}
	
	@Override
	public EffectType getEffectType()
	{
		return EffectType.THROW_UP;
	}
	
	@Override
	public boolean onStart()
	{
		// Abort attack, cast and move.
		getEffected().abortAll(false);
		
		// Get current position of the Creature
		final int curX = getEffected().getX();
		final int curY = getEffected().getY();
		final int curZ = getEffected().getZ();
		
		// Get the difference between effector and effected positions
		final double dx = getEffector().getX() - curX;
		final double dy = getEffector().getY() - curY;
		final double dz = getEffector().getZ() - curZ;
		
		// Calculate distance between effector and effected current position
		final double distance = Math.sqrt(dx * dx + dy * dy);
		if (distance < 1 || distance > 2000)
			return false;
		
		int offset = Math.min((int) distance + getSkill().getFlyRadius(), 1400);
		
		// approximation for moving futher when z coordinates are different
		// TODO: handle Z axis movement better
		offset += Math.abs(dz);
		if (offset < 5)
			offset = 5;
		
		// Calculate movement angles needed
		double sin = dy / distance;
		double cos = dx / distance;
		
		// Calculate the new destination with offset included
		_x = getEffector().getX() - (int) (offset * cos);
		_y = getEffector().getY() - (int) (offset * sin);
		_z = getEffected().getZ();
		
		final Location loc = GeoEngine.getInstance().getValidLocation(getEffected(), _x, _y, _z);
		_x = loc.getX();
		_y = loc.getY();
		
		// Refresh abnormal effects.
		getEffected().updateAbnormalEffect();
		
		getEffected().broadcastPacket(new FlyToLocation(getEffected(), _x, _y, _z, FlyType.THROW_UP));
		return true;
	}
	
	@Override
	public boolean onActionTime()
	{
		return false;
	}
	
	@Override
	public void onExit()
	{
		// Refresh abnormal effects.
		getEffected().updateAbnormalEffect();
		
		getEffected().setXYZ(_x, _y, _z);
		getEffected().broadcastPacket(new ValidateLocation(getEffected()));
	}
	
	@Override
	public int getEffectFlags()
	{
		return EffectFlag.STUNNED.getMask();
	}
}