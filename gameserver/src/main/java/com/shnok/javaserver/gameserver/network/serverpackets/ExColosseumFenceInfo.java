package com.shnok.javaserver.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.model.actor.instance.Fence;

/**
 * Format: (ch)ddddddd d: object id d: type (00 - no fence, 01 - only 4 columns, 02 - columns with fences) d: x coord d: y coord d: z coord d: width d: height
 */
public class ExColosseumFenceInfo extends L2GameServerPacket
{
	private final int _objectId;
	private final Fence _fence;
	
	public ExColosseumFenceInfo(int objectId, Fence fence)
	{
		_objectId = objectId;
		_fence = fence;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x09);
		
		writeD(_objectId);
		writeD(_fence.getType());
		writeD(_fence.getX());
		writeD(_fence.getY());
		writeD(_fence.getZ());
		writeD(_fence.getSizeX());
		writeD(_fence.getSizeY());
	}
}