package model.ability;

import model.*;

/**
 * Created by nutph on 6/2/2017.
 */
public class ReenergyAbility extends Ability {
    private enum ReenergyType{
        BASIC;
    }

    private int amount;//depends on the ability (e.g. damage amount, heal amount)
    private Ability subAbility;//when the ability has more than one effect
    private AbilityTarget sourceTarget;
    private AbilityTarget destinationTarget;
    private ReenergyType reenergyType;
    private AbilityType type;//DAM,HEAL, etc.

    public ReenergyAbility(AbilityTarget sourceTarget, AbilityTarget destinationTarget, AbilityType type) {
        this.amount = 1;
        this.reenergyType = ReenergyType.BASIC;
        this.sourceTarget = sourceTarget;
        this.destinationTarget = destinationTarget;
        this.type = type;
    }

    public ReenergyAbility(AbilityTarget sourceTarget, AbilityTarget destinationTarget, AbilityType type, Ability subAbility) {
        this.amount = 1;
        this.reenergyType = ReenergyType.BASIC;
        this.sourceTarget = sourceTarget;
        this.destinationTarget = destinationTarget;
        this.type = type;
        this.subAbility = subAbility;
    }


    //injection dependency
    public ReenergyAbility(int amount, AbilityTarget sourceTarget, AbilityTarget destinationTarget, AbilityType type, Ability subAbility) {
        this.amount = amount;
        this.sourceTarget = sourceTarget;
        this.destinationTarget = destinationTarget;
        this.type = type;
        this.subAbility = subAbility;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public AbilityTarget getTarget() {
        return sourceTarget;
    }

    public void setSourceTarget(AbilityTarget sourceTarget) {
        this.sourceTarget = sourceTarget;
    }

    public AbilityTarget getDestinationTarget() {
        return destinationTarget;
    }

    public void setDestinationTarget(AbilityTarget destinationTarget) {
        this.destinationTarget = destinationTarget;
    }

    public AbilityType getType() {
        return type;
    }

    public void setType(AbilityType type) {
        this.type = type;
    }

    public ReenergyType getReenergyType() {
        return reenergyType;
    }

    public void setReenergyType(ReenergyType reenergyType) {
        this.reenergyType = reenergyType;
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
        return getType()+" "+getAmount();
    }
}
