package com.shnok.javaserver.gameserver.model.actor.ai.type;

import java.util.List;

import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.gameserver.enums.actors.NpcSkillType;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.instance.TamedBeast;
import com.shnok.javaserver.gameserver.network.serverpackets.SocialAction;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public class TamedBeastAI extends AttackableAI<TamedBeast>
{
	private static final int MAX_DISTANCE_FROM_HOME = 13000;
	
	// Messages used every minute by the tamed beast when he automatically eats food.
	protected static final String[] FOOD_CHAT =
	{
		"Refills! Yeah!",
		"I am such a gluttonous beast, it is embarrassing! Ha ha.",
		"Your cooperative feeling has been getting better and better.",
		"I will help you!",
		"The weather is really good. Wanna go for a picnic?",
		"I really like you! This is tasty...",
		"If you do not have to leave this place, then I can help you.",
		"What can I help you with?",
		"I am not here only for food!",
		"Yam, yam, yam, yam, yam!"
	};
	
	private int _step;
	
	public TamedBeastAI(TamedBeast tamedBeast)
	{
		super(tamedBeast);
	}
	
	@Override
	public void runAI()
	{
		// Call it once per 5 seconds.
		if (++_step % 5 != 0)
			return;
		
		// Check if the owner is no longer around. If so, despawn.
		final Player owner = getOwner();
		if (owner == null || !owner.isOnline())
		{
			_actor.deleteMe();
			return;
		}
		
		// Happens every 60s.
		if (_step > 60)
		{
			// Verify first if the tamed beast is still in the good range. If not, delete it.
			if (!_actor.isIn2DRadius(52335, -83086, MAX_DISTANCE_FROM_HOME))
			{
				_actor.deleteMe();
				return;
			}
			
			// Destroy the food from owner's inventory ; if none is found, delete the pet.
			if (!owner.destroyItemByItemId(_actor.getFoodId(), 1, true))
			{
				_actor.deleteMe();
				return;
			}
			
			_actor.broadcastPacket(new SocialAction(_actor, 2));
			_actor.broadcastNpcSay(Rnd.get(FOOD_CHAT));
			
			_step = 0;
		}
		
		// If the owner is dead or if the tamed beast is currently casting a spell,do nothing.
		if (owner.isDead())
			return;
		
		// Retrieve all possible buffs, and remove buffs which are already set.
		final List<L2Skill> skills = _actor.getTemplate().getSkills(NpcSkillType.BUFF1, NpcSkillType.BUFF2, NpcSkillType.BUFF3, NpcSkillType.BUFF4, NpcSkillType.BUFF5);
		skills.removeIf(s -> owner.getFirstEffect(s) != null);
		
		// Up of 5 possible buffs, we got more than 2 available to cast, meaning the max limit of 3 isn't reached.
		if (skills.size() > 2)
			addCastDesire(owner, Rnd.get(skills), 1000000);
		else
			addFollowDesire(owner, 1000000);
	}
	
	@Override
	protected void onEvtOwnerAttacked(Creature attacker)
	{
		// Check if the owner is no longer around. If so, despawn.
		if (getOwner() == null || !getOwner().isOnline())
		{
			_actor.deleteMe();
			return;
		}
		
		// If the owner is dead or if the tamed beast is currently casting a spell,do nothing.
		if (getOwner().isDead())
			return;
		
		if (Rnd.nextBoolean())
		{
			final L2Skill skill = _actor.getTemplate().getSkill(NpcSkillType.HEAL);
			if (skill != null)
			{
				if (skill.getSkillType() == SkillType.MANARECHARGE || skill.getSkillType() == SkillType.MANAHEAL_PERCENT)
				{
					if (getOwner().getStatus().getMpRatio() < 0.5)
						addCastDesire(getOwner(), skill, 1000000);
				}
				else if (getOwner().getStatus().getHpRatio() < 0.5)
					addCastDesire(getOwner(), skill, 1000000);
			}
		}
		else
		{
			final L2Skill skill = _actor.getTemplate().getSkill(NpcSkillType.DEBUFF);
			if (skill != null && attacker.getFirstEffect(skill) == null)
				addCastDesire(attacker, skill, 1000000);
		}
	}
	
	@Override
	protected void onEvtFinishedCasting()
	{
		if (_nextIntention.isBlank())
			doFollowIntention(getOwner(), false);
		else
			doIntention(_nextIntention);
	}
	
	private Player getOwner()
	{
		return _actor.getOwner();
	}
}