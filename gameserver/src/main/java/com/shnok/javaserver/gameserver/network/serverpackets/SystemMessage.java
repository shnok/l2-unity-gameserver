package com.shnok.javaserver.gameserver.network.serverpackets;

import java.util.Arrays;

import net.sf.l2j.gameserver.data.sql.PlayerInfoTable;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.item.kind.Item;
import net.sf.l2j.gameserver.model.location.Location;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.skills.AbstractEffect;
import net.sf.l2j.gameserver.skills.L2Skill;

public final class SystemMessage extends L2GameServerPacket
{
	private static final SMParam[] EMPTY_PARAM_ARRAY = new SMParam[0];
	
	private static final byte TYPE_ZONE_NAME = 7;
	private static final byte TYPE_ITEM_NUMBER = 6;
	private static final byte TYPE_CASTLE_NAME = 5;
	private static final byte TYPE_SKILL_NAME = 4;
	private static final byte TYPE_ITEM_NAME = 3;
	private static final byte TYPE_NPC_NAME = 2;
	private static final byte TYPE_NUMBER = 1;
	private static final byte TYPE_TEXT = 0;
	
	private final SystemMessageId _smId;
	private SMParam[] _params;
	private int _paramIndex;
	
	private SystemMessage(final SystemMessageId smId)
	{
		final int paramCount = smId.getParamCount();
		_smId = smId;
		_params = paramCount != 0 ? new SMParam[paramCount] : EMPTY_PARAM_ARRAY;
	}
	
	public static final SystemMessage sendString(final String text)
	{
		if (text == null)
			throw new NullPointerException();
		
		return SystemMessage.getSystemMessage(SystemMessageId.S1).addString(text);
	}
	
	public static final SystemMessage getSystemMessage(final SystemMessageId smId)
	{
		SystemMessage sm = smId.getStaticSystemMessage();
		if (sm != null)
			return sm;
		
		sm = new SystemMessage(smId);
		if (smId.getParamCount() == 0)
			smId.setStaticSystemMessage(sm);
		
		return sm;
	}
	
	/**
	 * Use {@link #getSystemMessage(SystemMessageId)} where possible instead
	 * @param id
	 * @return the system message associated to the given Id.
	 */
	public static SystemMessage getSystemMessage(int id)
	{
		return getSystemMessage(SystemMessageId.getSystemMessageId(id));
	}
	
	private final void append(final SMParam param)
	{
		if (_paramIndex >= _params.length)
		{
			_params = Arrays.copyOf(_params, _paramIndex + 1);
			_smId.setParamCount(_paramIndex + 1);
			
			LOGGER.warn("Wrong parameter count '{}' for {}.", _paramIndex + 1, _smId);
		}
		
		_params[_paramIndex++] = param;
	}
	
	public final SystemMessage addString(final String text)
	{
		append(new SMParam(TYPE_TEXT, text));
		return this;
	}
	
	/**
	 * Castlename-e.dat<br>
	 * 0-9 Castle names<br>
	 * 21-64 CH names<br>
	 * 81-89 Territory names<br>
	 * 101-121 Fortress names<br>
	 * @param number
	 * @return
	 */
	public final SystemMessage addFortId(final int number)
	{
		append(new SMParam(TYPE_CASTLE_NAME, number));
		return this;
	}
	
	public final SystemMessage addNumber(final int number)
	{
		append(new SMParam(TYPE_NUMBER, number));
		return this;
	}
	
	public final SystemMessage addItemNumber(final int number)
	{
		append(new SMParam(TYPE_ITEM_NUMBER, number));
		return this;
	}
	
	public final SystemMessage addCharName(final int objectId)
	{
		return addString(PlayerInfoTable.getInstance().getPlayerName(objectId));
	}
	
	public final SystemMessage addCharName(final Creature cha)
	{
		return addString(cha.getName());
	}
	
	public final SystemMessage addItemName(final ItemInstance item)
	{
		return addItemName(item.getItem().getItemId());
	}
	
	public final SystemMessage addItemName(final Item item)
	{
		return addItemName(item.getItemId());
	}
	
	public final SystemMessage addItemName(final int id)
	{
		append(new SMParam(TYPE_ITEM_NAME, id));
		return this;
	}
	
	public final SystemMessage addZoneName(final Location loc)
	{
		append(new SMParam(TYPE_ZONE_NAME, loc));
		return this;
	}
	
	public final SystemMessage addSkillName(final AbstractEffect effect)
	{
		return addSkillName(effect.getSkill());
	}
	
	public final SystemMessage addSkillName(final L2Skill skill)
	{
		return addSkillName(skill.getId(), skill.getLevel());
	}
	
	public final SystemMessage addSkillName(final int id)
	{
		return addSkillName(id, 1);
	}
	
	public final SystemMessage addSkillName(final int id, final int lvl)
	{
		append(new SMParam(TYPE_SKILL_NAME, new IntIntHolder(id, lvl)));
		return this;
	}
	
	public final SystemMessageId getSystemMessageId()
	{
		return _smId;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x64);
		
		writeD(_smId.getId());
		writeD(_paramIndex);
		
		SMParam param;
		for (int i = 0; i < _paramIndex; i++)
		{
			param = _params[i];
			writeD(param.getType());
			
			switch (param.getType())
			{
				case TYPE_TEXT:
					writeS((String) param.getObject());
					break;
				
				case TYPE_ITEM_NUMBER, TYPE_ITEM_NAME, TYPE_CASTLE_NAME, TYPE_NUMBER, TYPE_NPC_NAME:
					writeD((Integer) param.getObject());
					break;
				
				case TYPE_SKILL_NAME:
					final IntIntHolder info = (IntIntHolder) param.getObject();
					writeD(info.getId());
					writeD(info.getValue());
					break;
				
				case TYPE_ZONE_NAME:
					writeLoc((Location) param.getObject());
					break;
			}
		}
	}
	
	private static final class SMParam
	{
		private final byte _type;
		private final Object _value;
		
		public SMParam(final byte type, final Object value)
		{
			_type = type;
			_value = value;
		}
		
		public int getType()
		{
			return _type;
		}
		
		public Object getObject()
		{
			return _value;
		}
	}
}