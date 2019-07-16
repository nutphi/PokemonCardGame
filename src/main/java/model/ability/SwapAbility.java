package model.ability;

import model.Ability;
import model.AbilityTarget;
import model.AbilityType;

/**
 * Created by nutph on 6/2/2017.
 */
public class SwapAbility extends Ability{
    //swap with active
    private Ability subAbility;//when the ability has more than one effect
    private AbilityTarget target;
    private AbilityType type;//DAM,HEAL, etc.

    public SwapAbility(AbilityTarget target, AbilityType type) {
        this.target = target;
        this.type = type;
    }

    //injection dependency
    public SwapAbility(AbilityTarget target, AbilityType type, Ability subAbility) {
        this.target = target;
        this.type = type;
        this.subAbility = subAbility;
    }

    public AbilityTarget getTarget() {
        return target;
    }

    public void setTarget(AbilityTarget target) {
        this.target = target;
    }

    public AbilityType getType() {
        return type;
    }

    public void setType(AbilityType type) {
        this.type = type;
    }

    @Override
    public Ability getSubAbility() {
        return subAbility;
    }

    public void setSubAbility(Ability subAbility) {
        this.subAbility = subAbility;
    }

    @Override
    public String toString() {
        return getType().name()+" "+getTarget().name();
    }

}
