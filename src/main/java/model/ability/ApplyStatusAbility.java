package model.ability;

import model.Ability;
import model.AbilityTarget;
import model.AbilityType;
import model.PokemonStatus;

/**
 * Created by nutph on 6/2/2017.
 */
public class ApplyStatusAbility extends Ability {
    private Ability subAbility;//when the ability has more than one effect
    private PokemonStatus status;//when pokemon on bench or active get status
    private AbilityTarget target;
    //if cond, if random=true then use subAbility
    private AbilityType type;//DAM,HEAL, etc.

    //constructor for damage, heal
    public ApplyStatusAbility(PokemonStatus status, AbilityTarget target, AbilityType type) {
        this.status = status;
        this.target = target;
        this.type = type;
    }

    public ApplyStatusAbility(PokemonStatus status, AbilityTarget target, AbilityType type, Ability subAbility) {
        this.status = status;
        this.target = target;
        this.type = type;
        this.subAbility = subAbility;
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

    public PokemonStatus getStatus() {
        return status;
    }

    public void setStatus(PokemonStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getType().name()+" "+getStatus().name();
    }

}
