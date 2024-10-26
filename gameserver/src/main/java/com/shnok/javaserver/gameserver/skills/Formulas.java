package com.shnok.javaserver.gameserver.skills;

import com.shnok.javaserver.commons.lang.StringUtil;
import com.shnok.javaserver.commons.logging.CLogger;
import com.shnok.javaserver.commons.random.Rnd;

import com.shnok.javaserver.Config;
import com.shnok.javaserver.gameserver.data.xml.HealSpsData;
import com.shnok.javaserver.gameserver.data.xml.PlayerLevelData;
import com.shnok.javaserver.gameserver.enums.actors.NpcRace;
import com.shnok.javaserver.gameserver.enums.items.WeaponType;
import com.shnok.javaserver.gameserver.enums.skills.ElementType;
import com.shnok.javaserver.gameserver.enums.skills.ShieldDefense;
import com.shnok.javaserver.gameserver.enums.skills.SkillType;
import com.shnok.javaserver.gameserver.enums.skills.Stats;
import com.shnok.javaserver.gameserver.model.actor.Creature;
import com.shnok.javaserver.gameserver.model.actor.Npc;
import com.shnok.javaserver.gameserver.model.actor.Playable;
import com.shnok.javaserver.gameserver.model.actor.Player;
import com.shnok.javaserver.gameserver.model.actor.Summon;
import com.shnok.javaserver.gameserver.model.actor.instance.Cubic;
import com.shnok.javaserver.gameserver.model.actor.instance.Door;
import com.shnok.javaserver.gameserver.model.item.kind.Armor;
import com.shnok.javaserver.gameserver.model.item.kind.Item;
import com.shnok.javaserver.gameserver.model.item.kind.Weapon;
import com.shnok.javaserver.gameserver.model.manor.Seed;
import com.shnok.javaserver.gameserver.network.SystemMessageId;
import com.shnok.javaserver.gameserver.network.serverpackets.SystemMessage;
import com.shnok.javaserver.gameserver.skills.effects.EffectTemplate;
import com.shnok.javaserver.gameserver.taskmanager.GameTimeTaskManager;

public final class Formulas
{
	private Formulas()
	{
		throw new IllegalStateException("Utility class");
	}
	
	private static final CLogger LOGGER = new CLogger(Formulas.class.getName());
	
	private static final int HP_REGENERATE_PERIOD = 3000; // 3 secs
	
	public static final byte SKILL_REFLECT_FAILED = 0; // Reflect failed.
	public static final byte SKILL_REFLECT_SUCCEED = 1; // normal reflect, some damage reflected some other not
	public static final byte SKILL_COUNTER = 2; // Damages are reflected, partially or completely, to the attacker.
	
	private static final byte MELEE_ATTACK_RANGE = 40;
	
	public static final int MAX_STAT_VALUE = 100;
	
	private static final double[] STR_COMPUTE = new double[]
	{
		1.036,
		34.845
	};
	private static final double[] INT_COMPUTE = new double[]
	{
		1.020,
		31.375
	};
	private static final double[] DEX_COMPUTE = new double[]
	{
		1.009,
		19.360
	};
	private static final double[] WIT_COMPUTE = new double[]
	{
		1.050,
		20.000
	};
	private static final double[] CON_COMPUTE = new double[]
	{
		1.030,
		27.632
	};
	private static final double[] MEN_COMPUTE = new double[]
	{
		1.010,
		-0.060
	};
	
	public static final double[] WIT_BONUS = new double[MAX_STAT_VALUE];
	public static final double[] MEN_BONUS = new double[MAX_STAT_VALUE];
	public static final double[] INT_BONUS = new double[MAX_STAT_VALUE];
	public static final double[] STR_BONUS = new double[MAX_STAT_VALUE];
	public static final double[] DEX_BONUS = new double[MAX_STAT_VALUE];
	public static final double[] CON_BONUS = new double[MAX_STAT_VALUE];
	
	public static final double[] BASE_EVASION_ACCURACY = new double[MAX_STAT_VALUE];
	
	protected static final double[] SQRT_MEN_BONUS = new double[MAX_STAT_VALUE];
	protected static final double[] SQRT_CON_BONUS = new double[MAX_STAT_VALUE];
	
	static
	{
		for (int i = 0; i < STR_BONUS.length; i++)
			STR_BONUS[i] = Math.floor(Math.pow(STR_COMPUTE[0], i - STR_COMPUTE[1]) * 100 + .5d) / 100;
		for (int i = 0; i < INT_BONUS.length; i++)
			INT_BONUS[i] = Math.floor(Math.pow(INT_COMPUTE[0], i - INT_COMPUTE[1]) * 100 + .5d) / 100;
		for (int i = 0; i < DEX_BONUS.length; i++)
			DEX_BONUS[i] = Math.floor(Math.pow(DEX_COMPUTE[0], i - DEX_COMPUTE[1]) * 100 + .5d) / 100;
		for (int i = 0; i < WIT_BONUS.length; i++)
			WIT_BONUS[i] = Math.floor(Math.pow(WIT_COMPUTE[0], i - WIT_COMPUTE[1]) * 100 + .5d) / 100;
		for (int i = 0; i < CON_BONUS.length; i++)
			CON_BONUS[i] = Math.floor(Math.pow(CON_COMPUTE[0], i - CON_COMPUTE[1]) * 100 + .5d) / 100;
		for (int i = 0; i < MEN_BONUS.length; i++)
			MEN_BONUS[i] = Math.floor(Math.pow(MEN_COMPUTE[0], i - MEN_COMPUTE[1]) * 100 + .5d) / 100;
		
		for (int i = 0; i < BASE_EVASION_ACCURACY.length; i++)
			BASE_EVASION_ACCURACY[i] = Math.sqrt(i) * 6;
		
		// Precompute square root values
		for (int i = 0; i < SQRT_CON_BONUS.length; i++)
			SQRT_CON_BONUS[i] = Math.sqrt(CON_BONUS[i]);
		for (int i = 0; i < SQRT_MEN_BONUS.length; i++)
			SQRT_MEN_BONUS[i] = Math.sqrt(MEN_BONUS[i]);
	}
	
	/**
	 * @param creature : The {@link Creature} to test.
	 * @return The period between 2 regeneration ticks.
	 */
	public static int getRegeneratePeriod(Creature creature)
	{
		if (creature instanceof Door)
			return HP_REGENERATE_PERIOD * 100; // 5 mins
			
		return HP_REGENERATE_PERIOD; // 3s
	}
	
	/**
	 * Calculate the blow success rate, based on {@link Creature} attacker, {@link Creature} target and {@link L2Skill}.
	 * @param attacker : The {@link Creature} attacker.
	 * @param target : The {@link Creature} target.
	 * @param skill : The {@link L2Skill} to use.
	 * @return True if successful, false otherwise.
	 */
	public static final boolean calcBlowRate(Creature attacker, Creature target, L2Skill skill)
	{
		double baseRate = skill.getBaseLandRate();
		if (baseRate == 0.0)
			return false;
		
		final boolean isAttackerInFrontofTarget = attacker.isInFrontOf(target);
		final boolean isBackstab = skill.getId() == 30;
		
		if (isAttackerInFrontofTarget && isBackstab)
		{
			if (Config.DEVELOPER)
			{
				StringUtil.printSection("Blow Rate");
				LOGGER.info("Final blow rate overriden by backstab under front target: 30 / 1000");
			}
			return 30 > Rnd.get(1000);
		}
		
		final double dexMul = DEX_BONUS[attacker.getStatus().getDEX()];
		final double blowMul = attacker.getStatus().calcStat(Stats.BLOW_RATE, 1, target, null);
		final double posMul = (attacker.isBehind(target)) ? 1.15 : ((isAttackerInFrontofTarget) ? 1. : 1.1);
		
		final double blowRate = baseRate * 2 * dexMul * blowMul * posMul;
		
		if (Config.DEVELOPER)
		{
			StringUtil.printSection("Blow Rate");
			LOGGER.info("Basic values: baseRate: {}", baseRate);
			LOGGER.info("Multipliers: dex: {}, blow: {}, pos: {}", dexMul, blowMul, posMul);
			LOGGER.info("Final blow rate: {} / 1000", blowRate);
		}
		
		return Math.min(blowRate, (isBackstab) ? 1000 : 800) > Rnd.get(1000);
	}
	
	/**
	 * Calculate the lethal success rate, based on {@link Creature} attacker, {@link Creature} target and few {@link L2Skill} parameters.
	 * @param attacker : The {@link Creature} attacker.
	 * @param target : The {@link Creature} target.
	 * @param baseRate : The base lethal chance of the {@link L2Skill}.
	 * @param magiclvl : The associated magic {@link L2Skill} level.
	 * @return True if successful, false otherwise.
	 */
	private static final boolean calcLethalRate(Creature attacker, Creature target, double baseRate, int magiclvl)
	{
		final double lethalMul = attacker.getStatus().calcStat(Stats.LETHAL_RATE, 1, target, null);
		
		final int attackerLevel = attacker.getStatus().getLevel();
		final int targetLevel = target.getStatus().getLevel();
		
		double editedRate;
		if (magiclvl > 0)
		{
			final int delta = ((magiclvl + attackerLevel) / 2) - 1 - targetLevel;
			if (delta >= -3)
				editedRate = baseRate * attackerLevel / targetLevel;
			else if (delta < -3 && delta >= -9)
				editedRate = baseRate / delta * -3;
			else
				editedRate = baseRate / 15;
		}
		else
			editedRate = baseRate * attackerLevel / targetLevel;
		
		final double lethalRate = editedRate * 10 * lethalMul;
		
		if (Config.DEVELOPER)
		{
			StringUtil.printSection("Lethal Rate");
			LOGGER.info("Basic values: baseRate: {}, editedRate: {}", baseRate, editedRate);
			LOGGER.info("Multipliers: lethal: {}", lethalMul);
			LOGGER.info("Final lethal rate: {} / 1000", lethalRate);
		}
		
		return lethalRate > Rnd.get(1000);
	}
	
	/**
	 * Calculate and process the effects of a lethal hit, based on {@link Creature} attacker, {@link Creature} target and {@link L2Skill} parameters.
	 * @param attacker : The {@link Creature} attacker.
	 * @param target : The {@link Creature} target.
	 * @param skill : The {@link L2Skill} to test.
	 */
	public static final void calcLethalHit(Creature attacker, Creature target, L2Skill skill)
	{
		// If the attacker can't attack, return.
		final Player attackerPlayer = attacker.getActingPlayer();
		if (attackerPlayer != null && !attackerPlayer.getAccessLevel().canGiveDamage())
			return;
		
		// If the target is invulnerable, related to RaidBoss or not lethalable, return.
		if (target.isInvul() || target.isRaidRelated() || !target.isLethalable())
			return;
		
		// Second lethal effect (hp to 1 for npc, cp/hp to 1 for player).
		if (skill.getLethalChance2() > 0 && calcLethalRate(attacker, target, skill.getLethalChance2(), skill.getMagicLevel()))
		{
			if (target instanceof Npc)
				target.reduceCurrentHp(target.getStatus().getHp() - 1, attacker, skill);
			else if (target instanceof Player targetPlayer)
			{
				targetPlayer.getStatus().setHp(1, false);
				targetPlayer.getStatus().setCp(1);
				targetPlayer.sendPacket(SystemMessageId.LETHAL_STRIKE);
			}
			attacker.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.LETHAL_STRIKE_SUCCESSFUL));
		}
		// First lethal effect (hp/2 for npc, cp to 1 for player).
		else if (skill.getLethalChance1() > 0 && calcLethalRate(attacker, target, skill.getLethalChance1(), skill.getMagicLevel()))
		{
			if (target instanceof Npc)
				target.reduceCurrentHp(target.getStatus().getHp() / 2, attacker, skill);
			else if (target instanceof Player targetPlayer)
			{
				targetPlayer.getStatus().setCp(1);
				targetPlayer.sendPacket(SystemMessageId.LETHAL_STRIKE);
			}
			attacker.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.LETHAL_STRIKE_SUCCESSFUL));
		}
	}
	
	/**
	 * @param attacker : The {@link Creature} launching the blow.
	 * @param target : The {@link Creature} victim of the blow.
	 * @param skill : The {@link L2Skill} to test.
	 * @param sDef : The {@link ShieldDefense} of the target.
	 * @param ss : True if ss are activated, false otherwise.
	 * @return The calculated blow damage.
	 */
	public static double calcBlowDamage(Creature attacker, Creature target, L2Skill skill, ShieldDefense sDef, boolean ss)
	{
		double defence = target.getStatus().getPDef(attacker);
		switch (sDef)
		{
			case SUCCESS:
				defence += target.getStatus().getShldDef();
				break;
			
			case PERFECT:
				return 1.;
		}
		
		final boolean isPvP = attacker instanceof Playable && target instanceof Playable;
		
		double attackPower = attacker.getStatus().getPAtk(target);
		double skillPower = skill.getPower();
		final double addCritPower = attacker.getStatus().calcStat(Stats.CRITICAL_DAMAGE_ADD, 0, target, skill) * 6;
		
		if (ss)
		{
			attackPower *= 2.;
			
			if (skill.getSSBoost() > 0)
				skillPower *= skill.getSSBoost();
		}
		
		final double critDamMul = attacker.getStatus().calcStat(Stats.CRITICAL_DAMAGE, 1, target, skill);
		final double rndMul = Rnd.get(95, 105) / 100.;
		final double critDamPosMul = ((attacker.getStatus().calcStat(Stats.CRITICAL_DAMAGE_POS, 1, target, skill) - 1) / 2 + 1); // Divided by 2 for blow types.
		final double posMul = getPosMul(attacker, target, true); // Divided by 2 for blow types.
		final double pvpMul = (isPvP) ? attacker.getStatus().calcStat(Stats.PVP_PHYS_SKILL_DMG, 1, null, null) : 1.;
		
		final double critVuln = target.getStatus().calcStat(Stats.CRIT_VULN, 1, target, skill);
		final double daggerVuln = target.getStatus().calcStat(Stats.DAGGER_WPN_VULN, 1, target, null);
		
		final double damage = ((attackPower + skillPower) * critDamMul * rndMul * critDamPosMul * posMul * pvpMul * critVuln * daggerVuln + addCritPower) * ((isPvP) ? 70. : 77.) / defence;
		
		if (Config.DEVELOPER)
		{
			StringUtil.printSection("Blow damage");
			LOGGER.info("ss:{}, shield:{}, isPvp:{}, defence:{}", ss, sDef, isPvP, defence);
			LOGGER.info("Basic powers: attack: {}, skill: {}, addCrit: {}", attackPower, skillPower, addCritPower);
			LOGGER.info("Multipliers: critDam: {}, rnd: {}, critPos: {}, pos: {}, pvp: {}", critDamMul, rndMul, critDamPosMul, posMul, pvpMul);
			LOGGER.info("Vulnerabilities: criticalVuln: {}, daggerVuln: {}", critVuln, daggerVuln);
			LOGGER.info("Final blow damage: {}", damage);
		}
		return Math.max(1, damage);
	}
	
	/**
	 * @param player : The {@link Player} to test.
	 * @return The calculated amount of HPs to remove, per second, when drowing.
	 */
	public static double calcDrownDamage(Player player)
	{
		final double amount = player.getStatus().getMaxHp() * player.getRace().getBreathMultiplier() * ((player.isMageClass()) ? 0.002698 : 0.001724);
		
		if (Config.DEVELOPER)
			LOGGER.info("Drown damage: {}.", amount);
		
		return amount;
	}
	
	/**
	 * @param caster : The {@link Creature} launching the heal.
	 * @param skill : The {@link L2Skill} to test.
	 * @param sps : True if sps are activated, false otherwise.
	 * @param bsps : True if bsps are activated, false otherwise.
	 * @return The calculated heal amount of a {@link L2Skill}.
	 */
	public static double calcHealAmount(Creature caster, L2Skill skill, boolean sps, boolean bsps)
	{
		final double skillPower = skill.getPower();
		final double addHealPower = caster.getStatus().calcStat(Stats.HEAL_PROFICIENCY, 0, null, null);
		
		double amount = skillPower + addHealPower;
		if (skill.getSkillType() == SkillType.HEAL_STATIC)
			return amount;
		
		final int mAtkPower = caster.getStatus().getMAtk(caster, null);
		
		double spsPower = 0.;
		double mAtkMul = 1.;
		
		if (sps || bsps)
		{
			spsPower = HealSpsData.getInstance().calculateHealSps(skill, mAtkPower);
			if (sps)
				spsPower *= 0.41;
			
			if ((caster instanceof Player player && player.isMageClass()) || caster instanceof Summon)
				mAtkMul = (bsps) ? 4. : 2.;
			else if (caster instanceof Npc)
				mAtkMul = 4.;
		}
		
		amount += spsPower + Math.sqrt(mAtkMul * mAtkPower);
		
		if (Config.DEVELOPER)
		{
			StringUtil.printSection("Heal amount");
			LOGGER.info("magicLevel: {}, sps:{}, bsps:{}", skill.getMagicLevel(), sps, bsps);
			LOGGER.info("Basic powers: mAtk: {}, skill: {}, addHeal: {}, sps: {}", mAtkPower, skillPower, addHealPower, spsPower);
			LOGGER.info("Multipliers: mAtk: {}", mAtkMul);
			LOGGER.info("Final heal amount: {}", amount);
		}
		return amount;
	}
	
	/**
	 * @param attacker : The {@link Creature} launching the physical attack.
	 * @param target : The {@link Creature} victim of the physical attack.
	 * @param sDef : The {@link ShieldDefense} of the target.
	 * @param crit : True if the attack was a critical success, false otherwise.
	 * @param ss : True if ss are activated, false otherwise.
	 * @return The calculated damage of a physical attack.
	 */
	public static final double calcPhysicalAttackDamage(Creature attacker, Creature target, ShieldDefense sDef, boolean crit, boolean ss)
	{
		// If the attacker can't attack, return.
		final Player attackerPlayer = attacker.getActingPlayer();
		if (attackerPlayer != null && !attackerPlayer.getAccessLevel().canGiveDamage())
			return 0.;
		
		double defence = target.getStatus().getPDef(attacker);
		switch (sDef)
		{
			case SUCCESS:
				defence += target.getStatus().getShldDef();
				break;
			
			case PERFECT:
				return 1.;
		}
		
		final boolean isPvP = attacker instanceof Playable && target instanceof Playable;
		
		final double attackPower = attacker.getStatus().getPAtk(target);
		
		double addCritPower = 0.;
		
		final double pvpMul = (isPvP) ? attacker.getStatus().calcStat(Stats.PVP_PHYSICAL_DMG, 1, null, null) : 1.;
		final double posMul = getPosMul(attacker, target, crit);
		final double elemMul = calcElementalPhysicalAttackModifier(attacker, target);
		final double rndMul = attacker.getRandomDamageMultiplier();
		
		double raceMul = 1.;
		double weaponMul = 1.;
		double critDamMul = 1.;
		double critDamPosMul = 1.;
		
		double critVuln = 1.;
		
		// Critic multiplier.
		if (crit)
		{
			critDamMul = attacker.getStatus().calcStat(Stats.CRITICAL_DAMAGE, 1, target, null);
			critDamPosMul = attacker.getStatus().calcStat(Stats.CRITICAL_DAMAGE_POS, 1, target, null);
			
			critVuln = target.getStatus().calcStat(Stats.CRIT_VULN, 1, target, null);
			
			addCritPower = attacker.getStatus().calcStat(Stats.CRITICAL_DAMAGE_ADD, 0, target, null) * 77. / defence;
		}
		
		// Weapon multiplier.
		final Weapon weapon = attacker.getActiveWeaponItem();
		if (weapon != null)
		{
			final Stats stat = weapon.getItemType().getVulnStat();
			if (stat != null)
				weaponMul = target.getStatus().calcStat(stat, 1, target, null);
		}
		
		// Race multiplier.
		if (target instanceof Npc targetNpc)
		{
			final NpcRace race = targetNpc.getTemplate().getRace();
			if (race.getAtkStat() != null && race.getResStat() != null)
				raceMul = 1 + ((attacker.getStatus().calcStat(race.getAtkStat(), 1, targetNpc, null) - targetNpc.getStatus().calcStat(race.getResStat(), 1, targetNpc, null)) / 100);
		}
		
		// End calculation.
		double damage = 0;
		if (crit)
			damage = ((attackPower * 2. * critDamMul * critDamPosMul * critVuln * posMul * rndMul * raceMul * pvpMul * elemMul * weaponMul) + addCritPower) * 77. / defence;
		else
			damage = (attackPower * posMul * rndMul * raceMul * pvpMul * elemMul * weaponMul) * 77. / defence;
		
		// If using ss, the damages are multiplied by 2.
		if (ss)
			damage *= 2.;
		
		if (Config.DEVELOPER)
		{
			StringUtil.printSection("Physical attack damage");
			LOGGER.info("crit:{}, ss:{}, shield:{}, isPvp:{}, defence:{}", crit, ss, sDef, isPvP, defence);
			LOGGER.info("Basic powers: attack: {}, addCrit: {}", attackPower, addCritPower);
			LOGGER.info("Multipliers: critDam: {}, critPos: {}, pos: {}, rnd: {}, race: {}, pvp: {}, elem: {}, weapon: {}", critDamMul, critDamPosMul, posMul, rndMul, raceMul, pvpMul, elemMul, weaponMul);
			LOGGER.info("Vulnerabilities: criticalVuln: {}", critVuln);
			LOGGER.info("Final damage: {}", damage);
		}
		
		if (damage < 0)
			damage = 0;
		else if (damage < 1)
			damage = 1;
		
		return damage;
	}
	
	/**
	 * @param attacker : The {@link Creature} launching the {@link L2Skill}.
	 * @param target : The {@link Creature} victim of the {@link L2Skill}.
	 * @param skill : The {@link L2Skill} to test.
	 * @param sDef : The {@link ShieldDefense} of the target.
	 * @param crit : True if the attack was a critical success, false otherwise.
	 * @param ss : True if ss are activated, false otherwise.
	 * @return The calculated damage of a physical {@link L2Skill} attack.
	 */
	public static final double calcPhysicalSkillDamage(Creature attacker, Creature target, L2Skill skill, ShieldDefense sDef, boolean crit, boolean ss)
	{
		// If the attacker can't attack, return.
		final Player attackerPlayer = attacker.getActingPlayer();
		if (attackerPlayer != null && !attackerPlayer.getAccessLevel().canGiveDamage())
			return 0.;
		
		double defence = target.getStatus().getPDef(attacker);
		switch (sDef)
		{
			case SUCCESS:
				defence += target.getStatus().getShldDef();
				break;
			
			case PERFECT:
				return 1.;
		}
		
		final boolean isPvP = attacker instanceof Playable && target instanceof Playable;
		
		final double attackPower = attacker.getStatus().getPAtk(target);
		
		double skillPower = skill.getPower(attacker);
		
		final double pvpMul = (isPvP) ? attacker.getStatus().calcStat(Stats.PVP_PHYS_SKILL_DMG, 1, null, null) : 1.;
		final double elemMul = calcElementalSkillModifier(target, skill);
		
		double ssMul = 1.;
		double rndMul = 1.;
		double raceMul = 1.;
		double weaponMul = 1.;
		
		if (ss)
		{
			ssMul = 2.04;
			
			if (skill.getSSBoost() > 0)
				skillPower *= skill.getSSBoost();
		}
		
		// Weapon multiplier.
		final Weapon weapon = attacker.getActiveWeaponItem();
		if (weapon != null)
		{
			final Stats stat = weapon.getItemType().getVulnStat();
			if (stat != null)
				weaponMul = target.getStatus().calcStat(stat, 1, target, null);
		}
		
		// Weapon random damage ; invalid for CHARGEDAM skills.
		if (skill.getEffectType() != SkillType.CHARGEDAM)
			rndMul = attacker.getRandomDamageMultiplier();
		
		// Race multiplier.
		if (target instanceof Npc targetNpc)
		{
			final NpcRace race = targetNpc.getTemplate().getRace();
			if (race.getAtkStat() != null && race.getResStat() != null)
				raceMul = 1 + ((attacker.getStatus().calcStat(race.getAtkStat(), 1, targetNpc, null) - targetNpc.getStatus().calcStat(race.getResStat(), 1, targetNpc, null)) / 100);
		}
		
		// End calculation.
		double damage = ((attackPower * ssMul + skillPower) * rndMul * raceMul * pvpMul * elemMul * weaponMul) * 77. / defence;
		if (crit)
			damage *= 2.;
		
		if (Config.DEVELOPER)
		{
			StringUtil.printSection("Physical skill damage");
			LOGGER.info("crit:{}, ss:{}, shield:{}, isPvp:{}, defence:{}", crit, ss, sDef, isPvP, defence);
			LOGGER.info("Basic powers: attack: {}, skill: {}", attackPower, skillPower);
			LOGGER.info("Multipliers: ss: {}, rnd: {}, race: {}, pvp: {}, elem: {}, weapon: {}", ssMul, rndMul, raceMul, pvpMul, elemMul, weaponMul);
			LOGGER.info("Final damage: {}", damage);
		}
		
		if (damage < 0)
			damage = 0;
		else if (damage < 1)
			damage = 1;
		
		return damage;
	}
	
	public static final double calcMagicDam(Creature attacker, Creature target, L2Skill skill, ShieldDefense sDef, boolean ss, boolean bss, boolean mcrit)
	{
		// If the attacker can't attack, return.
		final Player attackerPlayer = attacker.getActingPlayer();
		if (attackerPlayer != null && !attackerPlayer.getAccessLevel().canGiveDamage())
			return 0.;
		
		double mDef = target.getStatus().getMDef(attacker, skill);
		switch (sDef)
		{
			case SUCCESS:
				mDef += target.getStatus().getShldDef();
				break;
			
			case PERFECT:
				return 1.;
		}
		
		double mAtk = attacker.getStatus().getMAtk(target, skill);
		
		if (bss)
			mAtk *= 4;
		else if (ss)
			mAtk *= 2;
		
		double damage = 91 * Math.sqrt(mAtk) / mDef * skill.getPower(attacker);
		
		// Failure calculation
		if (Config.MAGIC_FAILURES && !calcMagicSuccess(attacker, target, skill))
		{
			if (attacker instanceof Player)
			{
				if (calcMagicSuccess(attacker, target, skill) && (target.getStatus().getLevel() - attacker.getStatus().getLevel()) <= 9)
				{
					if (skill.getSkillType() == SkillType.DRAIN)
						attacker.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.DRAIN_HALF_SUCCESFUL));
					else
						attacker.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ATTACK_FAILED));
					
					damage /= 2;
				}
				else
				{
					attacker.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill));
					damage = 1;
				}
			}
			
			if (target instanceof Player targetPlayer)
			{
				if (skill.getSkillType() == SkillType.DRAIN)
					targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.RESISTED_S1_DRAIN).addCharName(attacker));
				else
					targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.RESISTED_S1_MAGIC).addCharName(attacker));
			}
		}
		else if (mcrit)
			damage *= 4;
		
		// Pvp bonuses for dmg
		if (attacker instanceof Playable && target instanceof Playable)
		{
			if (skill.isMagic())
				damage *= attacker.getStatus().calcStat(Stats.PVP_MAGICAL_DMG, 1, null, null);
			else
				damage *= attacker.getStatus().calcStat(Stats.PVP_PHYS_SKILL_DMG, 1, null, null);
		}
		
		damage *= calcElementalSkillModifier(target, skill);
		
		return damage;
	}
	
	public static final double calcMagicDam(Cubic attacker, Creature target, L2Skill skill, boolean mcrit, ShieldDefense sDef)
	{
		double mDef = target.getStatus().getMDef(attacker.getOwner(), skill);
		switch (sDef)
		{
			case SUCCESS:
				mDef += target.getStatus().getShldDef();
				break;
			
			case PERFECT:
				return 1.;
		}
		
		double damage = 91 / mDef * skill.getPower();
		Player owner = attacker.getOwner();
		
		// Failure calculation
		if (Config.MAGIC_FAILURES && !calcMagicSuccess(owner, target, skill))
		{
			if (calcMagicSuccess(owner, target, skill) && (target.getStatus().getLevel() - skill.getMagicLevel()) <= 9)
			{
				if (skill.getSkillType() == SkillType.DRAIN)
					owner.sendPacket(SystemMessageId.DRAIN_HALF_SUCCESFUL);
				else
					owner.sendPacket(SystemMessageId.ATTACK_FAILED);
				
				damage /= 2;
			}
			else
			{
				owner.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.S1_RESISTED_YOUR_S2).addCharName(target).addSkillName(skill));
				damage = 1;
			}
			
			if (target instanceof Player targetPlayer)
			{
				if (skill.getSkillType() == SkillType.DRAIN)
					targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.RESISTED_S1_DRAIN).addCharName(owner));
				else
					targetPlayer.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.RESISTED_S1_MAGIC).addCharName(owner));
			}
		}
		else if (mcrit)
			damage *= 4;
		
		damage *= calcElementalSkillModifier(target, skill);
		
		return damage;
	}
	
	/**
	 * @param actor
	 * @param target
	 * @param skill
	 * @return true in case of critical hit
	 */
	public static final boolean calcCrit(Creature actor, Creature target, L2Skill skill)
	{
		return calcCrit(actor.getStatus().getCriticalHit(target, skill));
	}
	
	public static final boolean calcCrit(double rate)
	{
		return rate > Rnd.get(1000);
	}
	
	public static final boolean calcMCrit(Creature actor, Creature target, L2Skill skill)
	{
		final int mRate = actor.getStatus().getMCriticalHit(target, skill);
		
		if (Config.DEVELOPER)
			LOGGER.info("Current mCritRate: {} / 1000.", mRate);
		
		return mRate > Rnd.get(1000);
	}
	
	/**
	 * Check if casting process is canceled due to hit.
	 * @param target The target to make checks on.
	 * @param dmg The amount of dealt damages.
	 */
	public static final void calcCastBreak(Creature target, double dmg)
	{
		// Don't go further for invul characters or raid bosses.
		if (target.isRaidRelated() || target.isInvul())
			return;
		
		// Break automatically the skill cast if under attack.
		if (target.getFusionSkill() != null)
		{
			target.getCast().interrupt();
			return;
		}
		
		// Don't go further for ppl casting a physical skill.
		if (target.getCast().getCurrentSkill() != null && !target.getCast().getCurrentSkill().isMagic())
			return;
		
		// Calculate all modifiers for ATTACK_CANCEL ; chance to break is higher with higher dmg, and is affected by target MEN.
		double rate = target.getStatus().calcStat(Stats.ATTACK_CANCEL, 15 + Math.sqrt(13 * dmg) - (MEN_BONUS[target.getStatus().getMEN()] * 100 - 100), null, null);
		
		if (Config.DEVELOPER)
		{
			StringUtil.printSection("Cast break rate");
			LOGGER.info("Final cast break rate: {}%.", rate);
		}
		
		if (Math.clamp(rate, 1, 99) > Rnd.get(100))
			target.getCast().interrupt();
	}
	
	/**
	 * @param attacker : The {@link Creature} who attacks.
	 * @return The delay, in ms, before the next attack.
	 */
	public static final int calculateTimeBetweenAttacks(Creature attacker)
	{
		return Math.max(100, 500000 / attacker.getStatus().getPAtkSpd());
	}
	
	/**
	 * Calculate delay (in milliseconds) for skills cast.
	 * @param attacker
	 * @param skill used to know if skill is magic or no.
	 * @param skillTime
	 * @return delay in ms.
	 */
	public static final int calcAtkSpd(Creature attacker, L2Skill skill, double skillTime)
	{
		if (skill.isMagic())
			return (int) (skillTime * 333 / attacker.getStatus().getMAtkSpd());
		
		return (int) (skillTime * 333 / attacker.getStatus().getPAtkSpd());
	}
	
	/**
	 * Calculate the hit/miss chance.
	 * @param attacker : The {@link Creature} attacker to test.
	 * @param target : The {@link Creature} target to test.
	 * @return True if the hit missed or false if it evaded.
	 */
	public static boolean calcHitMiss(Creature attacker, Creature target)
	{
		int diff = attacker.getStatus().getAccuracy() - target.getStatus().getEvasionRate(attacker);
		
		// Get high or low Z bonus.
		final int diffZ = attacker.getZ() - target.getZ();
		if (diffZ > 50)
			diff += 3;
		else if (diffZ < -50)
			diff -= 3;
		
		// Get weather bonus.
		if (GameTimeTaskManager.getInstance().isNight())
			diff -= 10;
		
		// Get position bonus.
		if (attacker.isBehind(target))
			diff += 10;
		else if (!attacker.isInFrontOf(target))
			diff += 5;
		
		int chance = (90 + (2 * (diff))) * 10;
		
		if (Config.DEVELOPER)
			LOGGER.info("calcHitMiss diff: {}, rate: {}%.", diff, chance / 10);
		
		return Math.clamp(chance, 300, 980) < Rnd.get(1000);
	}
	
	/**
	 * @param attacker : The {@link Creature} used as attacker.
	 * @param target : The {@link Creature} used as target ; his shield is checked.
	 * @param skill : The {@link L2Skill} used by the attacker, if any.
	 * @param isCrit : If true, we apply a x3 block rate.
	 * @return A {@link ShieldDefense} based on calculated shield rate.
	 */
	public static ShieldDefense calcShldUse(Creature attacker, Creature target, L2Skill skill, boolean isCrit)
	{
		// "Ignore shield" skills types bypass the shield use.
		if (skill != null && skill.ignoreShield())
			return ShieldDefense.FAILED;
		
		// No shield is actually equipped, return as FAILED.
		final Item item = target.getSecondaryWeaponItem();
		if (!(item instanceof Armor))
			return ShieldDefense.FAILED;
		
		// The calculated shield rate is 0, return as FAILED.
		final double baseRate = target.getStatus().calcStat(Stats.SHIELD_RATE, 0, attacker, null);
		if (baseRate == 0.0)
			return ShieldDefense.FAILED;
		
		// The degree doesn't match, return as FAILED.
		final int degreeSide = (int) target.getStatus().calcStat(Stats.SHIELD_DEFENCE_ANGLE, 120, null, null);
		if (degreeSide < 360 && !target.isFacing(attacker, degreeSide))
			return ShieldDefense.FAILED;
		
		final double dexMul = DEX_BONUS[target.getStatus().getDEX()];
		
		double shieldRate = baseRate * dexMul;
		
		// Shield block rate is multiplied by 3 if the attacker uses a bow.
		final boolean isBow = attacker.getAttackType() == WeaponType.BOW;
		if (isBow)
			shieldRate *= 3;
		
		// Shield block rate is multiplied by 3 during critical hits.
		if (isCrit)
			shieldRate *= 3;
		
		final int chance = Rnd.get(100);
		
		// Calculate the ShieldDefense.
		ShieldDefense sDef;
		if (chance < Config.PERFECT_SHIELD_BLOCK_RATE)
			sDef = ShieldDefense.PERFECT;
		else if (chance < shieldRate)
			sDef = ShieldDefense.SUCCESS;
		else
			sDef = ShieldDefense.FAILED;
		
		// Send message to Player target.
		if (target instanceof Player targetPlayer)
		{
			switch (sDef)
			{
				case SUCCESS:
					targetPlayer.sendPacket(SystemMessageId.SHIELD_DEFENCE_SUCCESSFULL);
					break;
				
				case PERFECT:
					targetPlayer.sendPacket(SystemMessageId.YOUR_EXCELLENT_SHIELD_DEFENSE_WAS_A_SUCCESS);
					break;
			}
		}
		
		if (Config.DEVELOPER)
		{
			StringUtil.printSection("Shield use");
			LOGGER.info("Basic values: baseRate: {}, degreeSide: {}", baseRate, degreeSide);
			LOGGER.info("Multipliers: dex: {}, isBow: {}, isCrit: {}", dexMul, (isBow) ? "3.0" : "0.", (isCrit) ? "3.0" : "0.");
			LOGGER.info("ShieldDefense: {}, rate: {} / 100, chance: {}", sDef, shieldRate, chance);
		}
		
		return sDef;
	}
	
	public static boolean calcMagicAffected(Creature actor, Creature target, L2Skill skill)
	{
		double defence = 0;
		
		if (skill.isActive() && skill.isOffensive())
			defence = target.getStatus().getMDef(actor, skill);
		
		double attack = 2 * actor.getStatus().getMAtk(target, skill) * calcSkillVulnerability(actor, target, skill, skill.getSkillType());
		double d = (attack - defence) / (attack + defence);
		
		d += 0.5 * Rnd.nextGaussian();
		return d > 0;
	}
	
	public static double calcSkillVulnerability(Creature attacker, Creature target, L2Skill skill, SkillType type)
	{
		double multiplier = 1;
		
		// Get the elemental damages.
		if (skill.getElement() != ElementType.NONE)
			multiplier *= Math.sqrt(calcElementalSkillModifier(target, skill));
		
		// Get the skillType to calculate its effect in function of base stats of the target.
		switch (type)
		{
			case BLEED:
				multiplier = target.getStatus().calcStat(Stats.BLEED_VULN, multiplier, target, null);
				break;
			
			case POISON:
				multiplier = target.getStatus().calcStat(Stats.POISON_VULN, multiplier, target, null);
				break;
			
			case STUN:
				multiplier = target.getStatus().calcStat(Stats.STUN_VULN, multiplier, target, null);
				break;
			
			case PARALYZE:
				multiplier = target.getStatus().calcStat(Stats.PARALYZE_VULN, multiplier, target, null);
				break;
			
			case ROOT:
				multiplier = target.getStatus().calcStat(Stats.ROOT_VULN, multiplier, target, null);
				break;
			
			case SLEEP:
				multiplier = target.getStatus().calcStat(Stats.SLEEP_VULN, multiplier, target, null);
				break;
			
			case MUTE, FEAR, BETRAY, AGGDEBUFF, AGGREDUCE_CHAR, ERASE, CONFUSION:
				multiplier = target.getStatus().calcStat(Stats.DERANGEMENT_VULN, multiplier, target, null);
				break;
			
			case DEBUFF, WEAKNESS:
				multiplier = target.getStatus().calcStat(Stats.DEBUFF_VULN, multiplier, target, null);
				break;
			
			case CANCEL:
				multiplier = target.getStatus().calcStat(Stats.CANCEL_VULN, multiplier, target, null);
				break;
		}
		
		// Return a multiplier (exemple with resist shock : 1 + (-0,4 stun vuln) = 0,6%
		return multiplier;
	}
	
	private static double calcSkillStatModifier(SkillType type, Creature target, boolean isMagic)
	{
		double multiplier = 1;
		
		switch (type)
		{
			case STUN, BLEED, POISON:
				multiplier = 2 - SQRT_CON_BONUS[target.getStatus().getCON()];
				break;
			
			case SLEEP, DEBUFF, WEAKNESS, ERASE, ROOT, MUTE, FEAR, BETRAY, CONFUSION, AGGREDUCE_CHAR, PARALYZE:
				if (isMagic)
					multiplier = 2 - SQRT_MEN_BONUS[target.getStatus().getMEN()];
				break;
		}
		
		return Math.max(0, multiplier);
	}
	
	public static double getSTRBonus(Creature activeChar)
	{
		return STR_BONUS[activeChar.getStatus().getSTR()];
	}
	
	private static double getLevelModifier(Creature attacker, Creature target, L2Skill skill)
	{
		if (skill.getLevelDepend() == 0)
			return 1;
		
		int delta = (skill.getMagicLevel() > 0 ? skill.getMagicLevel() : attacker.getStatus().getLevel()) + skill.getLevelDepend() - target.getStatus().getLevel();
		return 1 + ((delta < 0 ? 0.01 : 0.005) * delta);
	}
	
	private static double getMatkModifier(Creature attacker, Creature target, L2Skill skill, boolean bss)
	{
		double mAtkModifier = 1;
		
		if (skill.isMagic())
		{
			final double mAtk = attacker.getStatus().getMAtk(target, skill);
			double val = mAtk;
			if (bss)
				val = mAtk * 4.0;
			
			mAtkModifier = (Math.sqrt(val) / target.getStatus().getMDef(attacker, skill)) * 11.0;
		}
		return mAtkModifier;
	}
	
	public static boolean calcEffectSuccess(Creature attacker, Creature target, EffectTemplate effect, L2Skill skill, boolean bss)
	{
		final SkillType type = effect.getEffectType();
		final double baseChance = effect.getEffectPower();
		
		if (type == null)
			return Rnd.get(100) < baseChance;
		
		if (type.equals(SkillType.CANCEL)) // CANCEL type lands always
			return true;
		
		final double statModifier = calcSkillStatModifier(type, target, skill.isMagic());
		final double skillModifier = calcSkillVulnerability(attacker, target, skill, type);
		final double mAtkModifier = getMatkModifier(attacker, target, skill, bss);
		final double lvlModifier = getLevelModifier(attacker, target, skill);
		final double rate = Math.clamp(baseChance * statModifier * skillModifier * mAtkModifier * lvlModifier, 1, 99);
		
		if (Config.DEVELOPER)
			LOGGER.info("calcEffectSuccess(): name:{} eff.type:{} power:{} statMod:{} skillMod:{} mAtkMod:{} lvlMod:{} total:{}%.", skill.getName(), type.toString(), baseChance, String.format("%1.2f", statModifier), String.format("%1.2f", skillModifier), String.format("%1.2f", mAtkModifier), String.format("%1.2f", lvlModifier), String.format("%1.2f", rate));
		
		return (Rnd.get(100) < rate);
	}
	
	public static boolean calcSkillSuccess(Creature attacker, Creature target, L2Skill skill, ShieldDefense sDef, boolean bss)
	{
		if (sDef == ShieldDefense.PERFECT)
			return false;
		
		final double baseChance = skill.getEffectPower();
		if (skill.ignoreResists())
			return (Rnd.get(100) < baseChance);
		
		final SkillType type = skill.getEffectType();
		final double statModifier = calcSkillStatModifier(type, target, skill.isMagic());
		final double skillModifier = calcSkillVulnerability(attacker, target, skill, type);
		final double mAtkModifier = getMatkModifier(attacker, target, skill, bss);
		final double lvlModifier = getLevelModifier(attacker, target, skill);
		final double rate = Math.clamp(baseChance * statModifier * skillModifier * mAtkModifier * lvlModifier, 1, 99);
		
		if (Config.DEVELOPER)
			LOGGER.info("calcSkillSuccess(): name:{} type:{} power:{} statMod:{} skillMod:{} mAtkMod:{} lvlMod:{} total:{}%.", skill.getName(), skill.getSkillType().toString(), baseChance, String.format("%1.2f", statModifier), String.format("%1.2f", skillModifier), String.format("%1.2f", mAtkModifier), String.format("%1.2f", lvlModifier), String.format("%1.2f", rate));
		
		return (Rnd.get(100) < rate);
	}
	
	public static boolean calcCubicSkillSuccess(Cubic attacker, Creature target, L2Skill skill, ShieldDefense sDef, boolean bss)
	{
		// if target reflect this skill then the effect will fail
		if (calcSkillReflect(target, skill) != SKILL_REFLECT_FAILED)
			return false;
		
		if (sDef == ShieldDefense.PERFECT)
			return false;
		
		final double baseChance = skill.getEffectPower();
		
		if (skill.ignoreResists())
			return Rnd.get(100) < baseChance;
		
		double mAtkModifier = 1;
		
		// Add Matk/Mdef Bonus
		if (skill.isMagic())
		{
			final double mAtk = attacker.getMAtk();
			double val = mAtk;
			if (bss)
				val = mAtk * 4.0;
			
			mAtkModifier = (Math.sqrt(val) / target.getStatus().getMDef(null, null)) * 11.0;
		}
		
		final SkillType type = skill.getEffectType();
		final double statModifier = calcSkillStatModifier(type, target, skill.isMagic());
		final double skillModifier = calcSkillVulnerability(attacker.getOwner(), target, skill, type);
		final double lvlModifier = getLevelModifier(attacker.getOwner(), target, skill);
		final double rate = Math.clamp(baseChance * statModifier * skillModifier * mAtkModifier * lvlModifier, 1, 99);
		
		if (Config.DEVELOPER)
			LOGGER.info("calcCubicSkillSuccess(): name:{} type:{} power:{} statMod:{} skillMod:{} mAtkMod:{} lvlMod:{} total:{}%.", skill.getName(), skill.getSkillType().toString(), baseChance, String.format("%1.2f", statModifier), String.format("%1.2f", skillModifier), String.format("%1.2f", mAtkModifier), String.format("%1.2f", lvlModifier), String.format("%1.2f", rate));
		
		return (Rnd.get(100) < rate);
	}
	
	public static boolean calcMagicSuccess(Creature attacker, Creature target, L2Skill skill)
	{
		int lvlDifference = target.getStatus().getLevel() - ((skill.getMagicLevel() > 0 ? skill.getMagicLevel() : attacker.getStatus().getLevel()) + skill.getLevelDepend());
		double rate = 100;
		
		if (lvlDifference > 0)
			rate = (Math.pow(1.166, lvlDifference)) * 100;
		
		if (attacker instanceof Player attackerPlayer && attackerPlayer.getWeaponGradePenalty())
			rate += 6000;
		
		if (Config.DEVELOPER)
			LOGGER.info("calcMagicSuccess(): name:{} lvlDiff:{} fail:{}%.", skill.getName(), lvlDifference, String.format("%1.2f", rate / 100));
		
		rate = Math.min(rate, 9900);
		
		return (Rnd.get(10000) > rate);
	}
	
	public static double calcManaDam(Creature attacker, Creature target, L2Skill skill, boolean ss, boolean bss)
	{
		double mAtk = attacker.getStatus().getMAtk(target, skill);
		double mDef = target.getStatus().getMDef(attacker, skill);
		double mp = target.getStatus().getMaxMp();
		
		if (bss)
			mAtk *= 4;
		else if (ss)
			mAtk *= 2;
		
		double damage = (Math.sqrt(mAtk) * skill.getPower(attacker) * (mp / 97)) / mDef;
		damage *= calcSkillVulnerability(attacker, target, skill, skill.getSkillType());
		return damage;
	}
	
	public static double calcRevivePower(Creature caster, double skillPower)
	{
		if (skillPower == 0. || skillPower == 100.)
			return skillPower;
		
		double revivePower = skillPower * WIT_BONUS[caster.getStatus().getWIT()];
		if (revivePower - skillPower > 20.)
			revivePower += 20.;
		
		revivePower = Math.max(revivePower, skillPower);
		
		return Math.min(revivePower, 90.);
	}
	
	public static boolean calcPhysicalSkillEvasion(Creature target, L2Skill skill)
	{
		return !skill.isMagic() && Rnd.get(100) < target.getStatus().calcStat(Stats.P_SKILL_EVASION, 0, null, skill);
	}
	
	/**
	 * @param creature : The {@link Creature} to test.
	 * @param skill : The {@link L2Skill} to test.
	 * @return True if the Skill Mastery skill activates, false otherwise.
	 */
	public static boolean calcSkillMastery(Creature creature, L2Skill skill)
	{
		if (!(creature instanceof Player player) || skill.getSkillType() == SkillType.FISHING)
			return false;
		
		final double skillPower = player.getStatus().calcStat(Stats.SKILL_MASTERY, 0, null, null);
		final double statMul = (player.isMageClass()) ? INT_BONUS[player.getStatus().getINT()] : STR_BONUS[player.getStatus().getSTR()];
		
		final double toReach = skillPower * statMul;
		final double toTest = Rnd.get(100.);
		
		if (Config.DEVELOPER)
		{
			StringUtil.printSection("Skill mastery");
			LOGGER.info("Basic powers: skill: {}", skillPower);
			LOGGER.info("Multipliers: stat: {}", statMul);
			LOGGER.info("Value to reach: {}. Did {} / 100", toReach, toTest);
		}
		
		return toTest < toReach;
	}
	
	/**
	 * Calculate the elemental modifier, based on one {@link Creature} attacker and one {@link Creature} target.
	 * @param attacker : The {@link Creature} used to retrieve elemental attacks.
	 * @param target : The {@link Creature} used to retrieve elemental protections.
	 * @return A multiplier based on attacker {@link ElementType} attack traits, or 1.
	 */
	private static double calcElementalPhysicalAttackModifier(Creature attacker, Creature target)
	{
		double elemMod = 1.;
		for (ElementType element : ElementType.VALUES)
		{
			if (element == ElementType.NONE)
				continue;
			
			final int traitAmount = attacker.getStatus().getAttackElementValue(element);
			if (traitAmount > 0)
				elemMod *= target.getStatus().getDefenseElementValue(element);
		}
		return elemMod;
	}
	
	/**
	 * Calculate the elemental modifier, based on a {@link Creature} target and a {@link L2Skill}.
	 * @param target : The {@link Creature} used to retrieve elemental protections.
	 * @param skill : If different of null, it will be considered as a {@link L2Skill} resist check.
	 * @return A multiplier based on the {@link ElementType} of the {@link L2Skill}, or 1.
	 */
	private static double calcElementalSkillModifier(Creature target, L2Skill skill)
	{
		if (skill == null || skill.getElement() == ElementType.NONE)
			return 1.;
		
		return target.getStatus().getDefenseElementValue(skill.getElement());
	}
	
	/**
	 * Calculate skill reflection according to these three possibilities:
	 * <ul>
	 * <li>Reflect failed</li>
	 * <li>Normal reflect (just effects).</li>
	 * <li>Vengeance reflect (100% damage reflected but damage is also dealt to actor).</li>
	 * </ul>
	 * @param target : The skill's target.
	 * @param skill : The skill to test.
	 * @return SKILL_REFLECTED_FAILED, SKILL_REFLECT_SUCCEED or SKILL_REFLECT_VENGEANCE
	 */
	public static byte calcSkillReflect(Creature target, L2Skill skill)
	{
		// Some special skills (like hero debuffs...) or ignoring resistances skills can't be reflected.
		if (skill.ignoreResists() || !skill.canBeReflected())
			return SKILL_REFLECT_FAILED;
		
		// Only magic and melee skills can be reflected.
		if (!skill.isMagic() && (skill.getCastRange() == -1 || skill.getCastRange() > MELEE_ATTACK_RANGE))
			return SKILL_REFLECT_FAILED;
		
		byte reflect = SKILL_REFLECT_FAILED;
		
		// Check for skilltypes.
		switch (skill.getSkillType())
		{
			case BUFF, REFLECT, HEAL_PERCENT, MANAHEAL_PERCENT, HOT, MPHOT, AGGDEBUFF, CONT:
				return SKILL_REFLECT_FAILED;
			
			case PDAM, BLOW, CHARGEDAM:
				if (target.getStatus().calcStat(Stats.COUNTER_SKILL_PHYSICAL, 0, target, null) > 0)
					reflect |= SKILL_COUNTER;
				break;
		}
		
		final double reflectChance = target.getStatus().calcStat((skill.isMagic()) ? Stats.REFLECT_SKILL_MAGIC : Stats.REFLECT_SKILL_PHYSIC, 0, null, skill);
		if (Rnd.get(100) < reflectChance)
			reflect |= SKILL_REFLECT_SUCCEED;
		
		return reflect;
	}
	
	/**
	 * @param actor : The character affected.
	 * @param fallHeight : The height the NPC fallen.
	 * @return the damage, based on max HPs and falling height.
	 */
	public static double calcFallDam(Creature actor, int fallHeight)
	{
		if (!Config.ENABLE_FALLING_DAMAGE || fallHeight < 0)
			return 0;
		
		return actor.getStatus().calcStat(Stats.FALL, fallHeight * actor.getStatus().getMaxHp() / 1000., null, null);
	}
	
	/**
	 * @param level : The level of the PKer.
	 * @param exp : The amount of xp earned.
	 * @return The amount of karma player has lost.
	 */
	public static int calculateKarmaLost(int level, long exp)
	{
		return (int) (exp / PlayerLevelData.getInstance().getPlayerLevel(level).karmaModifier() / 15);
	}
	
	/**
	 * @param pkCount : The current number of PK kills.
	 * @param isSummon : Does the victim is a summon or no (lesser karma gain if true).
	 * @return karma points that will be added to the player.
	 */
	public static int calculateKarmaGain(int pkCount, boolean isSummon)
	{
		int result = 14400;
		if (pkCount < 100)
			result = (int) (((((pkCount - 1) * 0.5) + 1) * 60) * 4);
		else if (pkCount < 180)
			result = (int) (((((pkCount + 1) * 0.125) + 37.5) * 60) * 4);
		
		if (isSummon)
			result = ((pkCount & 3) + result) >> 2;
		
		return result;
	}
	
	/**
	 * @param player : The {@link Player} launching the skill.
	 * @param target : The {@link Creature} used as target.
	 * @param seed : The {@link Seed} used as object.
	 * @return True if the sow was successful, or false otherwise.
	 */
	public static boolean calcSowSuccess(Player player, Creature target, Seed seed)
	{
		final int minLevelSeed = seed.getLevel() - 5;
		final int maxLevelSeed = seed.getLevel() + 5;
		
		final int targetLevel = target.getStatus().getLevel();
		
		int rate = (seed.isAlternative()) ? 20 : 90;
		
		// Apply a 5% penalty for each level difference, above 5, between target and seed levels.
		if (targetLevel < minLevelSeed)
			rate -= 5 * (minLevelSeed - targetLevel);
		else if (targetLevel > maxLevelSeed)
			rate -= 5 * (targetLevel - maxLevelSeed);
		
		// Apply a 5% penalty for each level difference, above 5, between player and target levels.
		final int diff = Math.abs(player.getStatus().getLevel() - targetLevel);
		if (diff > 5)
			rate -= (diff - 5) * 5;
		
		// Success rate can't be lesser than 1%.
		return Rnd.get(100) < Math.max(1, rate);
	}
	
	public static final boolean doorUnlock(L2Skill skill)
	{
		if (skill.getSkillType() == SkillType.UNLOCK_SPECIAL)
			return Rnd.get(100) < skill.getPower();
		
		switch (skill.getLevel())
		{
			case 0:
				return false;
			case 1:
				return Rnd.get(120) < 30;
			case 2:
				return Rnd.get(120) < 50;
			case 3:
				return Rnd.get(120) < 75;
			default:
				return Rnd.get(120) < 100;
		}
	}
	
	public static final boolean chestUnlock(L2Skill skill, int level)
	{
		int chance = 0;
		
		if (skill.getSkillType() == SkillType.DELUXE_KEY_UNLOCK)
		{
			// check the chance to open the box.
			int keyLevelNeeded = (level / 10) - skill.getLevel();
			if (keyLevelNeeded < 0)
				keyLevelNeeded *= -1;
			
			// Regular keys got 60% to succeed.
			chance = ((skill.getId() == 2065) ? 60 : 100) - keyLevelNeeded * 40;
		}
		else
		{
			if (level > 60)
			{
				if (skill.getLevel() < 10)
					return false;
				
				chance = (skill.getLevel() - 10) * 5 + 30;
			}
			else if (level > 40)
			{
				if (skill.getLevel() < 6)
					return false;
				
				chance = (skill.getLevel() - 6) * 5 + 10;
			}
			else if (level > 30)
			{
				if (skill.getLevel() < 3)
					return false;
				
				if (skill.getLevel() > 12)
					return true;
				
				chance = (skill.getLevel() - 3) * 5 + 30;
			}
			else
			{
				if (skill.getLevel() > 10)
					return true;
				
				chance = skill.getLevel() * 5 + 35;
			}
			
			chance = Math.min(chance, 50);
		}
		
		return Rnd.get(100) < chance;
	}
	
	/**
	 * @param attacker : The {@link Creature} launching the attack.
	 * @param target : The {@link Creature} victim of the attack.
	 * @param crit : If true, we divide by 2 the multiplier.
	 * @return The position multiplier for physical hit / skills and blows.
	 */
	private static double getPosMul(Creature attacker, Creature target, boolean crit)
	{
		if (attacker.isBehind(target))
			return (crit) ? 1.1 : 1.2;
		
		if (!attacker.isInFrontOf(target))
			return (crit) ? 1.025 : 1.05;
		
		return 1.;
	}
}