package model;

import controller.AbilityController;
import controller.JPokemonBoardController;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class PokemonAbilityTest {
    @Test
    public void setEnergyAmountAndValidateGet_ReturnsSameNumberOfEnergyAmt() throws Exception {
        PokemonAbility testAbility = new PokemonAbility();
        ArrayList<EnergyType> energyAmount = new ArrayList<EnergyType>();
        energyAmount.add(EnergyType.COLORLESS);
        energyAmount.add(EnergyType.WATER);
        energyAmount.add(EnergyType.LIGHTNING);

        testAbility.setEnergyAmount(energyAmount);
        assertEquals(3, testAbility.getEnergyAmount().size());

    }

    @Test
    public void validateIfEnergyTypeIsSameAsAbilityAndEnergyAmtMoreThanAbility() throws Exception {
        ArrayList<EnergyCard> pokemonEnergyTest = new ArrayList<EnergyCard>();
        EnergyCard energyCard1 = new EnergyCard(EnergyType.COLORLESS, CardType.ENERGY,  "Test Energy", "Water", false);
        EnergyCard energyCard2 = new EnergyCard(EnergyType.LIGHTNING, CardType.ENERGY,  "Test Energy", "Lightning",  false);
        pokemonEnergyTest.add(energyCard1);
        pokemonEnergyTest.add(energyCard2);

        PokemonAbility testAbility = new PokemonAbility();
        ArrayList<EnergyType> energyAmount = new ArrayList<EnergyType>();
        energyAmount.add(EnergyType.COLORLESS);
        energyAmount.add(EnergyType.LIGHTNING);
       // energyAmount.add(EnergyType.LIGHTNING);

        testAbility.setEnergyAmount(energyAmount);
        Boolean ableToAttack = testAbility.isAbleToAttack(pokemonEnergyTest);
        assertEquals(true, ableToAttack);

    }

    @Test
    public void create2PokemonCardCheckDamageAbilityEffect_ReturnDamageOnCard() throws Exception {
        ArrayList<EnergyType> energyAbility = new ArrayList<EnergyType>();
        energyAbility.add(EnergyType.FIGHT);

        PokemonAbility testAbility = new PokemonAbility("Test Fight",30 , energyAbility, AbilityTarget.OPPONENT_ACTIVE, AbilityType.DAM, EnergyType.FIGHT);
        Set<Card> targetCards = new HashSet<Card>();

        Card pokemonCard = new PokemonCard("Gameow", "Test Gameow", null, null,
                null, true, 30, 20,
                null, null, false, null);
        pokemonCard.setCardType(CardType.POKEMON);
        Card purglyCard = new PokemonCard("Purugly", "Test Purgly", null, null,
                null, true, 40, 20,
                null, null, false, null);
        purglyCard.setCardType(CardType.POKEMON);

        targetCards.add(pokemonCard);
        targetCards.add(purglyCard);
        AbilityController abilityController = new AbilityController(new JPokemonBoardController());
        abilityController.takeAbilityEffect(testAbility.getAbility(),targetCards,true);

        ArrayList<PokemonCard> affectedCards = new ArrayList(targetCards);

        assertEquals(2, affectedCards.size());
        int damageOfFirstCard = affectedCards.get(0).getDamage();
        int damageOf2Card = affectedCards.get(1).getDamage();
        assertEquals(50, damageOfFirstCard);
        assertEquals(50, damageOf2Card);


    }

}