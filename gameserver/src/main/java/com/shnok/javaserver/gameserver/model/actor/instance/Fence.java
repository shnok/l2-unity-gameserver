package com.shnok.javaserver.gameserver.model.actor.instance;

import com.shnok.javaserver.gameserver.geoengine.geodata.IGeoObject;
import com.shnok.javaserver.gameserver.idfactory.IdFactory;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.network.serverpackets.ExColosseumFenceInfo;

public class Fence extends WorldObject implements IGeoObject
{
	private static final int FENCE_HEIGHT = 24;
	
	// fence description from world point of view
	private final int _type;
	private final int _sizeX;
	private final int _sizeY;
	private final int _height;
	
	// 2 dummy object to spawn fences with 2 and 3 layers easily
	// TODO: I know it is shitcoded, but didn't figure out any better solution
	private final L2DummyFence _object2;
	private final L2DummyFence _object3;
	
	// fence description from geodata point of view
	private final int _geoX;
	private final int _geoY;
	private final int _geoZ;
	private final byte[][] _geoData;
	
	public Fence(int type, int sizeZ, int sizeY, int height, int geoX, int geoY, int geoZ, byte[][] geoData)
	{
		super(IdFactory.getInstance().getNextId());
		
		_type = type;
		_sizeX = sizeZ;
		_sizeY = sizeY;
		_height = height * FENCE_HEIGHT;
		
		_object2 = height > 1 ? new L2DummyFence(this) : null;
		_object3 = height > 2 ? new L2DummyFence(this) : null;
		
		_geoX = geoX;
		_geoY = geoY;
		_geoZ = geoZ;
		_geoData = geoData;
	}
	
	public int getType()
	{
		return _type;
	}
	
	public int getSizeX()
	{
		return _sizeX;
	}
	
	public int getSizeY()
	{
		return _sizeY;
	}
	
	@Override
	public void onSpawn()
	{
		// spawn me
		super.onSpawn();
		
		// spawn dummy fences
		if (_object2 != null)
			_object2.spawnMe(getPosition());
		if (_object3 != null)
			_object3.spawnMe(getPosition());
	}
	
	@Override
	public void decayMe()
	{
		// despawn dummy fences
		if (_object2 != null)
			_object2.decayMe();
		if (_object3 != null)
			_object3.decayMe();
		
		// despawn me
		super.decayMe();
	}
	
	@Override
	public void sendInfo(Player player)
	{
		player.sendPacket(new ExColosseumFenceInfo(getObjectId(), this));
	}
	
	@Override
	public int getGeoX()
	{
		return _geoX;
	}
	
	@Override
	public int getGeoY()
	{
		return _geoY;
	}
	
	@Override
	public int getGeoZ()
	{
		return _geoZ;
	}
	
	@Override
	public int getHeight()
	{
		return _height;
	}
	
	@Override
	public byte[][] getObjectGeoData()
	{
		return _geoData;
	}
	
	/**
	 * Dummy fence class in order to spawn/delete multi-layer fences correctly.
	 */
	protected class L2DummyFence extends WorldObject
	{
		private final Fence _fence;
		
		public L2DummyFence(Fence fence)
		{
			super(IdFactory.getInstance().getNextId());
			
			_fence = fence;
		}
		
		@Override
		public void sendInfo(Player player)
		{
			player.sendPacket(new ExColosseumFenceInfo(getObjectId(), _fence));
		}
	}
}