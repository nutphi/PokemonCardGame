package model.ability.filter;

import model.AbilityTarget;
import model.PokemonCard;

/**
 * Created by nutph on 6/8/2017.
 */
public class EvolvesFromFilter extends Filter{
    //target on pokemon that evolves from
    private AbilityTarget abilityTarget;

    public EvolvesFromFilter(AbilityTarget abilityTarget, FilterType filterType) {
        super(filterType);
        this.abilityTarget = abilityTarget;
    }

    public AbilityTarget getAbilityTarget() {
        return abilityTarget;
    }

    public void setAbilityTarget(AbilityTarget abilityTarget) {
        this.abilityTarget = abilityTarget;
    }
}
