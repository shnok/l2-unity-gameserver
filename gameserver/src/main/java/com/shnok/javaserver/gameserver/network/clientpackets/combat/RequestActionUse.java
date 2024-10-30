package com.shnok.javaserver.gameserver.network.clientpackets.combat;

import com.shnok.javaserver.commons.random.Rnd;
import com.shnok.javaserver.commons.util.ArraysUtil;

import com.shnok.javaserver.gameserver.enums.SayType;
import com.shnok.javaserver.gameserver.model.WorldObject;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.actor.instance.Pet;
import com.shnok.javaserver.gameserver.model.actor.instance.Servitor;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.shnok.javaserver.gameserver.network.serverpackets.combat.ActionFailed;
import com.shnok.javaserver.gameserver.network.serverpackets.NpcSay;
import com.shnok.javaserver.gameserver.skills.L2Skill;

public final class RequestActionUse extends L2GameClientPacket
{
	private static final int[] PASSIVE_SUMMONS =
	{
		12564,
		12621,
		14702,
		14703,
		14704,
		14705,
		14706,
		14707,
		14708,
		14709,
		14710,
		14711,
		14712,
		14713,
		14714,
		14715,
		14716,
		14717,
		14718,
		14719,
		14720,
		14721,
		14722,
		14723,
		14724,
		14725,
		14726,
		14727,
		14728,
		14729,
		14730,
		14731,
		14732,
		14733,
		14734,
		14735,
		14736
	};
	
	private static final int SIN_EATER_ID = 12564;
	private static final String[] SIN_EATER_ACTIONS_STRINGS =
	{
		"special skill? Abuses in this kind of place, can turn blood Knots...!",
		"Hey! Brother! What do you anticipate to me?",
		"shouts ha! Flap! Flap! Response?",
		", has not hit...!"
	};
	
	private int _actionId;
	private boolean _isCtrlPressed;
	private boolean _isShiftPressed;
	
	@Override
	protected void readImpl()
	{
		_actionId = readD();
		_isCtrlPressed = (readD() == 1);
		_isShiftPressed = (readC() == 1);
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return;
			
		// Dont do anything if player is dead, or use fakedeath using another action than sit.
		// You can stopFakeDEeath with sit/stand. On adv, you cannot.
		if ((player.isFakeDeath() && _actionId != 0) || player.isDead() || player.isOutOfControl())
		{
			player.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (player.isInObserverMode())
		{
			player.sendPacket(SystemMessageId.OBSERVERS_CANNOT_PARTICIPATE);
			return;
		}
		
		final Summon summon = player.getSummon();
		final WorldObject target = player.getTarget();
		
		switch (_actionId)
		{
			case 0: // Sit/Stand
				if (player.isSitting() || player.isSittingNow())
					player.getAI().tryToStand();
				else
					player.getAI().tryToSit(target);
				break;
			
			case 1: // Walk/Run
				if (player.isMounted())
					return;
				
				if (player.isRunning())
					player.forceWalkStance();
				else
					player.forceRunStance();
				break;
			
			case 10: // Private Store - Sell
				player.tryOpenPrivateSellStore(false);
				break;
			
			case 28: // Private Store - Buy
				player.tryOpenPrivateBuyStore();
				break;
			
			case 15, 21: // Change Movement Mode (pet follow/stop)
				if (summon == null)
					return;
				
				// You can't order anymore your pet to stop if distance is superior to 2000.
				if (summon.getAI().getFollowStatus() && !player.isIn3DRadius(summon, 2000))
					return;
				
				if (summon.isOutOfControl())
				{
					player.sendPacket(SystemMessageId.PET_REFUSING_ORDER);
					return;
				}
				
				summon.getAI().switchFollowStatus();
				break;
			
			case 16, 22: // Attack (pet attack)
				if (target == null || summon == null || summon == target || player == target)
					return;
				
				// If target is trully dead, then do nothing at all. Fake Death is handled elsewhere (attack task).
				if (target instanceof Creature targetCreature && targetCreature.isDead())
					return;
				
				// Sin eater, Big Boom, Wyvern can't attack with attack button.
				if (ArraysUtil.contains(PASSIVE_SUMMONS, summon.getNpcId()))
					return;
				
				if (summon.isOutOfControl())
				{
					player.sendPacket(SystemMessageId.PET_REFUSING_ORDER);
					return;
				}
				
				if (summon instanceof Pet && (summon.getStatus().getLevel() - player.getStatus().getLevel() > 20))
				{
					player.sendPacket(SystemMessageId.PET_TOO_HIGH_TO_CONTROL);
					return;
				}
				
				summon.setTarget(target);
				
				if (target instanceof Creature targetCreature)
				{
					if (targetCreature.isAttackableWithoutForceBy(player) || (_isCtrlPressed && targetCreature.isAttackableBy(player)))
						summon.getAI().tryToAttack(targetCreature, _isCtrlPressed, _isShiftPressed);
					else
						summon.getAI().tryToFollow(targetCreature, _isShiftPressed);
				}
				else
					summon.getAI().tryToInteract(target, _isCtrlPressed, _isShiftPressed);
				break;
			
			case 17, 23: // Stop (pet - cancel action)
				if (summon == null)
					return;
				
				if (summon.isOutOfControl())
				{
					player.sendPacket(SystemMessageId.PET_REFUSING_ORDER);
					return;
				}
				
				summon.getAI().tryToIdle();
				break;
			
			case 19: // Returns pet to control item
				if (!(summon instanceof Pet pet))
					return;
				
				if (pet.isDead())
					player.sendPacket(SystemMessageId.DEAD_PET_CANNOT_BE_RETURNED);
				else if (pet.isOutOfControl())
					player.sendPacket(SystemMessageId.PET_REFUSING_ORDER);
				else if (pet.getAttack().isAttackingNow() || pet.isInCombat())
					player.sendPacket(SystemMessageId.PET_CANNOT_SENT_BACK_DURING_BATTLE);
				else if (pet.checkUnsummonState())
					player.sendPacket(SystemMessageId.YOU_CANNOT_RESTORE_HUNGRY_PETS);
				else
					pet.unSummon(player);
				break;
			
			case 38: // pet mount/dismount
				player.mountPlayer(summon);
				break;
			
			case 32: // Wild Hog Cannon - Mode Change
				// useSkill(4230);
				break;
			
			case 36: // Soulless - Toxic Smoke
				useSkill(4259, target);
				break;
			
			case 37: // Dwarven Manufacture
				player.tryOpenWorkshop(true);
				break;
			
			case 39: // Soulless - Parasite Burst
				useSkill(4138, target);
				break;
			
			case 41: // Wild Hog Cannon - Attack
				if (!(target instanceof Door))
				{
					player.sendPacket(SystemMessageId.INVALID_TARGET);
					return;
				}
				
				useSkill(4230, target);
				break;
			
			case 42: // Kai the Cat - Self Damage Shield
				useSkill(4378, player);
				break;
			
			case 43: // Unicorn Merrow - Hydro Screw
				useSkill(4137, target);
				break;
			
			case 44: // Big Boom - Boom Attack
				useSkill(4139, target);
				break;
			
			case 45: // Unicorn Boxer - Master Recharge
				useSkill(4025, player);
				break;
			
			case 46: // Mew the Cat - Mega Storm Strike
				useSkill(4261, target);
				break;
			
			case 47: // Silhouette - Steal Blood
				useSkill(4260, target);
				break;
			
			case 48: // Mechanic Golem - Mech. Cannon
				useSkill(4068, target);
				break;
			
			case 51: // General Manufacture
				player.tryOpenWorkshop(false);
				break;
			
			case 52: // Unsummon a servitor
				if (!(summon instanceof Servitor servitor))
					return;
				
				if (servitor.isDead())
					player.sendPacket(SystemMessageId.DEAD_PET_CANNOT_BE_RETURNED);
				else if (servitor.isOutOfControl())
					player.sendPacket(SystemMessageId.PET_REFUSING_ORDER);
				else if (servitor.getAttack().isAttackingNow() || servitor.isInCombat())
					player.sendPacket(SystemMessageId.PET_CANNOT_SENT_BACK_DURING_BATTLE);
				else
					servitor.unSummon(player);
				break;
			
			case 53, 54: // move to target, move to target hatch/strider
				if (target == null || summon == null || summon == target)
					return;
				
				if (summon.isOutOfControl())
				{
					player.sendPacket(SystemMessageId.PET_REFUSING_ORDER);
					return;
				}
				
				summon.getAI().setFollowStatus(false);
				
				if (target instanceof Creature targetCreature)
					summon.getAI().tryToFollow(targetCreature, _isShiftPressed);
				else
					summon.getAI().tryToInteract(target, _isCtrlPressed, _isShiftPressed);
				break;
			
			case 61: // Private Store Package Sell
				player.tryOpenPrivateSellStore(true);
				break;
			
			case 1000: // Siege Golem - Siege Hammer
				if (!(target instanceof Door))
				{
					player.sendPacket(SystemMessageId.INVALID_TARGET);
					return;
				}
				
				useSkill(4079, target);
				break;
			
			case 1001: // Sin Eater - Ultimate Bombastic Buster
				if (useSkill(4139, summon) && summon.getNpcId() == SIN_EATER_ID && Rnd.get(100) < 10)
					summon.broadcastPacket(new NpcSay(summon, SayType.ALL, Rnd.get(SIN_EATER_ACTIONS_STRINGS)));
				break;
			
			case 1003: // Wind Hatchling/Strider - Wild Stun
				useSkill(4710, target);
				break;
			
			case 1004: // Wind Hatchling/Strider - Wild Defense
				useSkill(4711, player);
				break;
			
			case 1005: // Star Hatchling/Strider - Bright Burst
				useSkill(4712, target);
				break;
			
			case 1006: // Star Hatchling/Strider - Bright Heal
				useSkill(4713, player);
				break;
			
			case 1007: // Cat Queen - Blessing of Queen
				useSkill(4699, player);
				break;
			
			case 1008: // Cat Queen - Gift of Queen
				useSkill(4700, player);
				break;
			
			case 1009: // Cat Queen - Cure of Queen
				useSkill(4701, target);
				break;
			
			case 1010: // Unicorn Seraphim - Blessing of Seraphim
				useSkill(4702, player);
				break;
			
			case 1011: // Unicorn Seraphim - Gift of Seraphim
				useSkill(4703, player);
				break;
			
			case 1012: // Unicorn Seraphim - Cure of Seraphim
				useSkill(4704, target);
				break;
			
			case 1013: // Nightshade - Curse of Shade
				useSkill(4705, target);
				break;
			
			case 1014: // Nightshade - Mass Curse of Shade
				useSkill(4706, player);
				break;
			
			case 1015: // Nightshade - Shade Sacrifice
				useSkill(4707, target);
				break;
			
			case 1016: // Cursed Man - Cursed Blow
				useSkill(4709, target);
				break;
			
			case 1017: // Cursed Man - Cursed Strike/Stun
				useSkill(4708, target);
				break;
			
			case 1031: // Feline King - Slash
				useSkill(5135, target);
				break;
			
			case 1032: // Feline King - Spinning Slash
				useSkill(5136, target);
				break;
			
			case 1033: // Feline King - Grip of the Cat
				useSkill(5137, target);
				break;
			
			case 1034: // Magnus the Unicorn - Whiplash
				useSkill(5138, target);
				break;
			
			case 1035: // Magnus the Unicorn - Tridal Wave
				useSkill(5139, target);
				break;
			
			case 1036: // Spectral Lord - Corpse Kaboom
				useSkill(5142, target);
				break;
			
			case 1037: // Spectral Lord - Dicing Death
				useSkill(5141, target);
				break;
			
			case 1038: // Spectral Lord - Force Curse
				useSkill(5140, target);
				break;
			
			case 1039: // Swoop Cannon - Cannon Fodder
				if (target instanceof Door)
				{
					player.sendPacket(SystemMessageId.INVALID_TARGET);
					return;
				}
				
				useSkill(5110, target);
				break;
			
			case 1040: // Swoop Cannon - Big Bang
				if (target instanceof Door)
				{
					player.sendPacket(SystemMessageId.INVALID_TARGET);
					return;
				}
				
				useSkill(5111, target);
				break;
			
			default:
				LOGGER.warn("Unhandled action type {} detected for {}.", _actionId, player.getName());
		}
	}
	
	/**
	 * Cast a skill for active pet/servitor.
	 * @param skillId The id of the skill to launch.
	 * @param target The target is specified as a parameter but can be overwrited or ignored depending on skill type.
	 * @return true if you can use the skill, false otherwise.
	 */
	private boolean useSkill(int skillId, WorldObject target)
	{
		final Player player = getClient().getPlayer();
		if (player == null)
			return false;
		
		final Summon summon = player.getSummon();
		if (summon == null)
			return false;
		
		final L2Skill skill = summon.getSkill(skillId);
		if (skill == null)
			return false;
		
		if (summon instanceof Pet pet && pet.getStatus().getLevel() - player.getStatus().getLevel() > 20)
			return false;
		
		summon.getAI().tryToCast((target instanceof Creature targetCreature) ? targetCreature : null, skill, _isCtrlPressed, _isShiftPressed, 0);
		return true;
	}
}