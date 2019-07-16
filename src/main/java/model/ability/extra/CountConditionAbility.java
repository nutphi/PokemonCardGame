package model.ability.extra;

import controller.JPokemonBoardController;
import model.Ability;
import model.AbilityType;
import model.ability.ConditionAbility;
import model.ability.NullAbility;

/**
 * Created by nutph on 6/8/2017.
 *
 */
public class CountConditionAbility extends ConditionAbility{
    private Calculation calculation;//now only YOUR ACTIVE ENERGY PSYCHIC > 0
    private int amount;

    //count_if
    public CountConditionAbility(int amount, Calculation calculation, Condition condition, Ability subAbilityHead){
        super(AbilityType.COND,condition,subAbilityHead,new NullAbility());
        this.calculation = calculation;
        this.amount = amount;
    }

    public boolean isGreater(JPokemonBoardController controller, boolean isOpponent){
        return calculation.isGreater(controller,amount,isOpponent);
    }
}
