package model;

import model.ability.HealAbility;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by nutph on 5/6/2017.
 */
public class TrainerAbility {
    private Ability ability;
    public TrainerAbility(){}

    public TrainerAbility(Ability ability){
        this.ability = ability;
    }

    public TrainerAbility(int amount , AbilityTarget target, AbilityType abilityType) {
        if(abilityType.equals(AbilityType.HEAL)){
            ability = new HealAbility(amount,target,abilityType);
        }
    }

    public Ability getAbility() {
        return ability;
    }

    public void setAbility(Ability ability) {
        this.ability = ability;
    }

    @Override
    public String toString() {
        return ability.toString();
    }
}