package model.ability;

import model.Ability;
import model.AbilityTarget;
import model.AbilityType;
import model.PokemonCard;
import model.ability.extra.Calculation;

/**
 * Created by nutph on 6/2/2017.
 */
public class RedamangeAbility extends Ability {
    private int amount;//depends on the ability (e.g. damage amount, heal amount)
    private Calculation calculation;
    private Ability subAbility;//when the ability has more than one effect
    private AbilityTarget sourceTarget;
    private AbilityTarget destinationTarget;
    //if cond, if random=true then use subAbility
    private AbilityType type;//DAM,HEAL, etc.

    //constructor for damage, heal
    public RedamangeAbility(Calculation calculation, AbilityTarget sourceTarget, AbilityTarget destinationTarget, AbilityType type) {
        this.calculation = calculation;
        this.sourceTarget = sourceTarget;
        this.destinationTarget = destinationTarget;
        this.type = type;
    }

    //injection dependency
    public RedamangeAbility(Calculation calculation, AbilityTarget sourceTarget, AbilityTarget destinationTarget, AbilityType type, Ability subAbility) {
        this.calculation = calculation;
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

    public Calculation getCalculation() {
        return calculation;
    }

    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
    }

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

    public int getCountLastDamage(PokemonCard lastSourceDamagePokemonCard){
        return calculation.getCountLastSourceDamage(lastSourceDamagePokemonCard);
    }
}
