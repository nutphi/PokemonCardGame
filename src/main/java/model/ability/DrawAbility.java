package model.ability;

import model.Ability;
import model.AbilityTarget;
import model.AbilityType;
import model.PokemonStatus;

/**
 * Created by nutph on 6/2/2017.
 */
public class DrawAbility extends Ability {
    private int amount;//number of cards
    private Ability subAbility;//when the ability has more than one effect
    private AbilityTarget target;
    private AbilityType type;//DAM,HEAL, etc.

    //constructor for damage, heal
    public DrawAbility(int amount, AbilityTarget target, AbilityType type) {
        this.amount = amount;
        this.target = target;
        this.type = type;
    }

    //injection dependency
    public DrawAbility(int amount, AbilityTarget target, AbilityType type, Ability subAbility) {
        this.amount = amount;
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
        return getType()+" "+getAmount();
    }

}
