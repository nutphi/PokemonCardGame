package model;

import java.util.ArrayList;

/**
 * Created by nutph on 5/16/2017.
 */
public class EvolvedPokemonCard extends PokemonCard{
    private PokemonCard basePokemon;
    public EvolvedPokemonCard(){}

    public EvolvedPokemonCard(String name, String filename, String description, boolean isVisibleCard, int hitpoint, int damage,
                       ArrayList<PokemonAbility> abilities, ArrayList<EnergyType> retreat, boolean isNewPokemon,
                       EnergyType pokemonType, PokemonCard basePokemon) {
        super(name, filename, description, isVisibleCard, hitpoint, damage,
        abilities, retreat, isNewPokemon, pokemonType);
        this.basePokemon = basePokemon;
    }

    public PokemonCard getBasePokemon() {
        return basePokemon;
    }

    public void setBasePokemon(PokemonCard basePokemon) {
        this.basePokemon = basePokemon;
    }
}
