package model.ability;

import model.Ability;
import model.AbilityTarget;
import model.AbilityType;
import model.ability.extra.Calculation;

/**
 * Created by nutph on 6/3/2017.
 */
public class DeckAbility extends Ability {
    public enum Destination{
        TOP_DECK, BOTTOM_DECK, DISCARD;
    }
    public enum Choice{
        RANDOM, YOU_CHOOSE, OPPONENT_CHOOSE;
    }//with count=all, no choose
    private Destination destination; //deck or discard
    private Choice choice;
    private Calculation calculation;
    private int amount;//Integer.MAX_VALUE = all
    private AbilityTarget target;//e.g. Opponent-active
    private AbilityType type;//DAM,HEAL, etc.
    private Ability subAbility;

    //constructor for damage, heal
    public DeckAbility(int amount, Destination destination, Choice choice, AbilityTarget target, AbilityType type) {
        this.calculation = new Calculation();
        this.calculation.setTarget(Calculation.CountTarget.NULL);
        this.calculation.setOperator(Calculation.Operator.NULL);
        this.amount = amount;
        this.destination = destination;
        this.choice = choice;
        this.target = target;
        this.type = type;
    }

    //constructor for damage, heal
    public DeckAbility(int amount, Destination destination, Choice choice, AbilityTarget target, AbilityType type, Ability subAbility) {
        this.calculation = new Calculation();
        this.calculation.setTarget(Calculation.CountTarget.NULL);
        this.calculation.setOperator(Calculation.Operator.NULL);
        this.amount = amount;
        this.destination = destination;
        this.choice = choice;
        this.target = target;
        this.type = type;
        this.subAbility = subAbility;
    }

    //constructor for damage, heal
    public DeckAbility(Calculation calculation, Destination destination, AbilityTarget target, AbilityType type) {
        this.amount = 0;
        this.calculation = calculation;
        this.destination = destination;
        this.target = target;
        this.type = type;
    }

    //constructor for damage, heal
    public DeckAbility(Calculation calculation, Destination destination, AbilityTarget target, AbilityType type, Ability subAbility) {
        this.amount = 0;
        this.calculation = calculation;
        this.destination = destination;
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

    public Destination getDestination() {
        return destination;
    }

    public void setDestination(Destination destination) {
        this.destination = destination;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public Calculation getCalculation() {
        return calculation;
    }

    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
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
        return getType().name()+" "+getAmount();
    }

}
