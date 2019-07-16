package model.ability.extra;

import model.Ability;
import model.AbilityType;
import model.ability.ConditionAbility;
import model.ability.NullAbility;

/**
 * Created by nutph on 6/21/2017.
 */
public class AbilityConditionAbility extends ConditionAbility{
    private Ability conditionAbility;
    public AbilityConditionAbility(AbilityType abilityType,Condition condition, Ability conditionAbility, Ability subAbilityHead){
        super(abilityType,condition,subAbilityHead,new NullAbility());
        this.conditionAbility = conditionAbility;
    }

    public Ability getConditionAbility() {
        return conditionAbility;
    }

    public void setConditionAbility(Ability conditionAbility) {
        this.conditionAbility = conditionAbility;
    }
}
