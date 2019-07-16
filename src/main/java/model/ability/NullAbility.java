package model.ability;

import model.Ability;
import model.AbilityTarget;
import model.AbilityType;

/**
 * Created by nutph on 6/3/2017.
 */
public class NullAbility extends Ability {

    @Override
    public AbilityType getType() {
        return AbilityType.NULL;
    }

    @Override
    public AbilityTarget getTarget() {
        return AbilityTarget.NONE;
    }

    @Override
    public Ability getSubAbility() {
        return null;
    }

    @Override
    public String toString() {
        return "NOTHING";
    }
}
