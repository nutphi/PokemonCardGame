package model.ability;

import model.Ability;
import model.AbilityTarget;
import model.AbilityType;

/**
 * Created by nutph on 6/3/2017.
 */
public class AddAbility extends Ability{
    public enum Trigger{
        OPPONENT_TURN_END;
    }
    private Trigger trigger;
    private AbilityTarget target;
    private AbilityType type;
    private Ability subAbility;//only heal self

    public AddAbility(Trigger trigger, AbilityTarget target, AbilityType type, Ability subAbility) {
        this.trigger = trigger;
        this.target = target;
        this.type = type;
        this.subAbility = subAbility;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public void setTrigger(Trigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public AbilityTarget getTarget() {
        return target;
    }

    public void setTarget(AbilityTarget target) {
        this.target = target;
    }

    @Override
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
}
