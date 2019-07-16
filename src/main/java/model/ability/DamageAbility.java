package model.ability;

import model.Ability;
import model.AbilityTarget;
import model.AbilityType;
import model.ability.extra.Calculation;

/**
 * Created by nutph on 6/2/2017.
 */
public class DamageAbility extends Ability{

    private int amount;//depends on the ability (e.g. damage amount, heal amount)
    private Ability subAbility;//when the ability has more than one effect
    private AbilityTarget target;
    //if cond, if random=true then use subAbility
    private AbilityType type;//DAM,HEAL, etc.

    private Calculation calculation;

    //constructor for damage, heal
    public DamageAbility(int amount, AbilityTarget target, AbilityType type) {
        this.amount = amount;
        this.target = target;
        this.type = type;
    }

    //injection dependency
    public DamageAbility(int amount, AbilityTarget target, AbilityType type, Ability subAbility) {
        this.amount = amount;
        this.target = target;
        this.type = type;
        this.subAbility = subAbility;
    }

    //injection dependency
    public DamageAbility(int amount,Calculation calculation,AbilityTarget target, AbilityType type) {
        this.amount = amount;
        this.calculation = calculation;
        this.target = target;
        this.type = type;
    }

    //injection dependency
    public DamageAbility(int amount,Calculation calculation,AbilityTarget target, AbilityType type, Ability subAbility) {
        this.amount = amount;
        this.calculation = calculation;
        this.target = target;
        this.type = type;
        this.subAbility = subAbility;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Calculation getCalculation() {
        return calculation;
    }

    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
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
        return ""+getAmount();
    }

}
