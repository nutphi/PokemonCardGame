package model.ability.filter;

import model.EnergyType;

/**
 * Created by nutph on 6/8/2017.
 */
public class PokemonFilter extends Filter{
    public enum PokemonType{ANY,BASIC,STAGE_ONE};
    private EnergyType energyType;
    private PokemonType pokemonType;

    public PokemonFilter(EnergyType energyType, PokemonType pokemonType, FilterType filterType) {
        super(filterType);
        this.energyType = energyType;
        this.pokemonType = pokemonType;
    }


    public PokemonFilter(EnergyType energyType, FilterType filterType) {
        super(filterType);
        this.energyType = energyType;
    }

    public PokemonFilter(PokemonType pokemonType, FilterType filterType) {
        super(filterType);
        this.pokemonType = pokemonType;
    }

    public EnergyType getEnergyType() {
        return energyType;
    }

    public void setEnergyType(EnergyType energyType) {
        this.energyType = energyType;
    }

    public PokemonType getPokemonType() {
        return pokemonType;
    }

    public void setPokemonType(PokemonType pokemonType) {
        this.pokemonType = pokemonType;
    }
}
