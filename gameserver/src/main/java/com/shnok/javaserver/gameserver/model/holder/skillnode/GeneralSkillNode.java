package com.shnok.javaserver.gameserver.model.holder.skillnode;

import com.shnok.javaserver.commons.data.StatSet;

/**
 * A datatype used by general skill types. It extends {@link SkillNode}.
 */
public class GeneralSkillNode extends SkillNode
{
	private final int _cost;
	
	public GeneralSkillNode(StatSet set)
	{
		super(set);
		
		_cost = set.getInteger("cost");
	}
	
	public int getCost()
	{
		return _cost;
	}
	
	/**
	 * Method used for Divine Inspiration skill implementation, since it uses -1 as cost (easier management). We couldn't keep 0, otherwise it would be considered as an autoGet and be freely given ; and using a boolean tag would kill me.<br>
	 * <br>
	 * <b>Only used to display the correct value to client, regular uses must be -1.</b>
	 * @return 0 or the initial cost if superior to 0.
	 */
	public int getCorrectedCost()
	{
		return Math.max(0, _cost);
	}
}