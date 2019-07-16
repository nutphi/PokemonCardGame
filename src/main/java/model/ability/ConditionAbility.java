package model.ability;

import model.Ability;
import model.AbilityTarget;
import model.AbilityType;
import model.ability.NullAbility;
import model.ability.extra.Calculation;

/**
 * Created by nutph on 6/3/2017.
 */
public class ConditionAbility extends Ability {
    public enum Condition{FILP,HEALED_YOUR_ACTIVE,ABILITY,CHOICE,COUNT_IF;}
    private AbilityType type;//DAM,HEAL, etc.
    private Condition condition;
    private Ability subAbilityHead;
    private Ability subAbilityTail;
    private Ability subAbility;



    //for flip/ability/choice
    public ConditionAbility(AbilityType type, Condition condition, Ability subAbilityHead, Ability subAbility) {
        this.type = type;
        this.condition = condition;
        this.subAbilityHead = subAbilityHead;
        this.subAbilityTail = new NullAbility();
        this.subAbility = subAbility;
    }

    //for else
    public ConditionAbility(AbilityType type, Condition condition, Ability subAbilityHead, Ability subAbilityTail, Ability subAbility) {
        this.type = type;
        this.condition = condition;
        this.subAbilityHead = subAbilityHead; //ex. cond:HEAD:else:TAIL :subAbility
        this.subAbilityTail = subAbilityTail;
        this.subAbility = subAbility;
    }

    @Override
    public AbilityType getType() {
        return type;
    }

    public void setType(AbilityType type) {
        this.type = type;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public Ability getSubAbility() {
        return subAbility;
    }

    public Ability getSubAbilityHead() {
        return subAbilityHead;
    }

    public void setSubAbilityHead(Ability subAbilityHead) {
        this.subAbilityHead = subAbilityHead;
    }

    public Ability getSubAbilityTail() {
        return subAbilityTail;
    }

    public void setSubAbilityTail(Ability subAbilityTail) {
        this.subAbilityTail = subAbilityTail;
    }

    @Override
    public AbilityTarget getTarget() {
        return AbilityTarget.NONE;
    }

    @Override
    public String toString() {
        String name = "";
        if(!(subAbilityHead instanceof NullAbility)){
            name += " H:"+subAbilityHead.toString();
        }
        if(!(subAbilityTail instanceof NullAbility)){
            name += " T:"+subAbilityTail.toString();
        }
        return ""+getCondition().name()+name;
    }
}
