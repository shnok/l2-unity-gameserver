package com.shnok.javaserver.gameserver.skills.conditions;

import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public abstract class Condition implements ConditionListener
{
	private ConditionListener _listener;
	
	private String _msg;
	private int _msgId;
	
	private boolean _addName;
	private boolean _result;
	
	/**
	 * Test impl.
	 * @param effector : The Creature who is the initiator of the Condition.
	 * @param effected : The Creature who is the target of the Condition.
	 * @param skill : The Skill, if any.
	 * @param item : The Item, if any.
	 * @return true, if successful
	 */
	abstract boolean testImpl(Creature effector, Creature effected, L2Skill skill, Item item);
	
	/**
	 * Sets the message.
	 * @param msg the new message
	 */
	public final void setMessage(String msg)
	{
		_msg = msg;
	}
	
	/**
	 * Gets the message.
	 * @return the message
	 */
	public final String getMessage()
	{
		return _msg;
	}
	
	/**
	 * Sets the message id.
	 * @param msgId the new message id
	 */
	public final void setMessageId(int msgId)
	{
		_msgId = msgId;
	}
	
	/**
	 * Gets the message id.
	 * @return the message id
	 */
	public final int getMessageId()
	{
		return _msgId;
	}
	
	/**
	 * Adds the name.
	 */
	public final void addName()
	{
		_addName = true;
	}
	
	/**
	 * Checks if is adds the name.
	 * @return true, if is adds the name
	 */
	public final boolean isAddName()
	{
		return _addName;
	}
	
	/**
	 * Sets the listener.
	 * @param listener the new listener
	 */
	void setListener(ConditionListener listener)
	{
		_listener = listener;
		notifyChanged();
	}
	
	/**
	 * Gets the listener.
	 * @return the listener
	 */
	final ConditionListener getListener()
	{
		return _listener;
	}
	
	public final boolean test(Creature caster, Creature target, L2Skill skill)
	{
		return test(caster, target, skill, null);
	}
	
	public final boolean test(Creature caster, Creature target, Item item)
	{
		return test(caster, target, null, null);
	}
	
	/**
	 * Test this {@link Condition} condition, and update listener.
	 * @param caster : The Creature who is the initiator of the Condition.
	 * @param target : The Creature who is the target of the Condition.
	 * @param skill : The Skill, if any.
	 * @param item : The Item, if any.
	 * @return true if all conditions were met.
	 */
	public final boolean test(Creature caster, Creature target, L2Skill skill, Item item)
	{
		boolean res = testImpl(caster, target, skill, item);
		if (_listener != null && res != _result)
		{
			_result = res;
			notifyChanged();
		}
		return res;
	}
	
	@Override
	public void notifyChanged()
	{
		if (_listener != null)
			_listener.notifyChanged();
	}
}