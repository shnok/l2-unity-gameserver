package com.shnok.javaserver.model.stats.functions.fomulas;

import com.shnok.javaserver.model.object.entity.Entity;
import com.shnok.javaserver.model.skills.Skill;
import com.shnok.javaserver.model.stats.BaseStats;
import com.shnok.javaserver.model.stats.Stats;
import com.shnok.javaserver.model.stats.functions.AbstractFunction;

public class FuncMAtkCritical extends AbstractFunction {
    private static final FuncMAtkCritical _fac_instance = new FuncMAtkCritical();

    public static AbstractFunction getInstance() {
        return _fac_instance;
    }

    private FuncMAtkCritical() {
        super(Stats.MCRITICAL_RATE, 1, null, 0, null);
    }

    @Override
    public double calc(Entity effector, Entity effected, Skill skill, double initVal) {
        // CT2: The magic critical rate has been increased to 10 times.
        if (!effector.isPlayer() || (effector.getActiveWeaponInstance() != null)) {
            return initVal * BaseStats.WIT.calcBonus(effector) * 10;
        }
        return initVal;
    }
}