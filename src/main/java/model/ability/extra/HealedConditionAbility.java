package model.ability.extra;

import model.Ability;
import model.AbilityTarget;
import model.AbilityType;
import model.ability.ConditionAbility;
import model.ability.NullAbility;

/**
 * Created by nutph on 6/8/2017.
 */
public class HealedConditionAbility extends ConditionAbility{
    //for healed (SELF)
    private AbilityTarget target;

    //healed
    public HealedConditionAbility(AbilityType type, Condition condition, AbilityTarget target, Ability subAbilityHead){
        super(type, condition, subAbilityHead, new NullAbility());
        this.target = target;
    }

    @Override
    public AbilityTarget getTarget() {
        return target;
    }

    public void setTarget(AbilityTarget target) {
        this.target = target;
    }
}
