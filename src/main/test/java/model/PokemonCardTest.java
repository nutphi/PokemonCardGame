package model;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PokemonCardTest {

    @Test
    public void createPokemonCardSetWaterLightningEnergyGetEnergyTypes_Return2() throws Exception {

        ArrayList<EnergyType> fightingAbility = new ArrayList<EnergyType>();
        fightingAbility.add(EnergyType.FIGHT);

        ArrayList<PokemonAbility> abilities = new ArrayList<PokemonAbility>();
        PokemonAbility testAbility = new PokemonAbility("Test Fight",30 , fightingAbility, AbilityTarget.OPPONENT_ACTIVE, AbilityType.DAM, EnergyType.FIGHT);

        ArrayList<EnergyCard> energyTypes = new ArrayList<EnergyCard>();
        EnergyCard lightningCard = new EnergyCard(EnergyType.LIGHTNING, CardType.ENERGY, "Lightning card","Lightning", false);
        EnergyCard waterCard = new EnergyCard(EnergyType.WATER, CardType.ENERGY, "Water card","Water", false);
        energyTypes.add(lightningCard);
        energyTypes.add(waterCard);

        PokemonCard gameow = new PokemonCard("Gameow", "Test Gameow", null, null,
                null, true, 30, 20,
                abilities, null, false, EnergyType.COLORLESS);
        gameow.setCardType(CardType.POKEMON);
        gameow.setEnergies(energyTypes);
        ArrayList<EnergyType> pokemonEnergyTypes = gameow.getPokemonEnergyTypes();
        assertEquals(2, pokemonEnergyTypes.size());
    }

    @Test
    public void createPokemonCardWithWaterLightningRemoveWater_ReturnsWaterEnergyCard() throws Exception {
        ArrayList<EnergyCard> energyTypes = new ArrayList<EnergyCard>();
        EnergyCard lightningCard = new EnergyCard(EnergyType.LIGHTNING, CardType.ENERGY, "Lightning card","Lightning", false);
        EnergyCard waterCard = new EnergyCard(EnergyType.WATER, CardType.ENERGY, "Water card","Water", false);
        energyTypes.add(lightningCard);
        energyTypes.add(waterCard);

        PokemonCard gameow = new PokemonCard("Gameow", "Test Gameow", null, null,
                null, true, 30, 20,
                null, null, false, EnergyType.COLORLESS);
        gameow.setCardType(CardType.POKEMON);
        gameow.setEnergies(energyTypes);

        EnergyCard waterEnergyCard = gameow.removeEnergyCardFromEnergyType(EnergyType.WATER);
        assertEquals(EnergyType.WATER, waterEnergyCard.getEnergyType());
    }

}