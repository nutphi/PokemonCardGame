package model;

import controller.AbilityController;
import controller.ComputerPlayerController;
import controller.HumanPlayerController;
import controller.JPokemonBoardController;
import model.ability.DamageAbility;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by ssamudhraa on 6/22/2017.
 */
public class AbilityTest {

    @Before
    public void beforeEachTest() throws Exception {

/*
        PokemonCard basicPokemon1= new PokemonCard();
        PokemonCard basicPokemon2 = new PokemonCard();


        Player player = new Player();
        player.setPlayerHandCards(new ArrayList<Card>());
        player.setBenchPokemonCards(new ArrayList<PokemonCard>());
        player.setRewardCards(new ArrayList<Card>());
        PokemonCard playerBasicPokemon = new PokemonCard();

        Player opponent = new Player();
        opponent.setPlayerHandCards(new ArrayList<Card>());
        opponent.setBenchPokemonCards(new ArrayList<PokemonCard>());
        opponent.setRewardCards(new ArrayList<Card>());
        PokemonCard opponentBasicPokemon= new PokemonCard();



        Stack<Card> deck1 = new Stack();
        deck1.push(new EnergyCard());
        deck1.push(new EvolvedPokemonCard());
        deck1.push(new TrainerCard());
        deck1.push(new EnergyCard());
        deck1.push(new EvolvedPokemonCard());
        deck1.push(new TrainerCard());
        deck1.push(new EnergyCard());
        deck1.push(new EvolvedPokemonCard());
        deck1.push(new TrainerCard());
        deck1.push(new EnergyCard());
        deck1.push(new EvolvedPokemonCard());
        deck1.push(new TrainerCard());
        deck1.push(new EnergyCard());
        deck1.push(playerBasicPokemon);
        player.setDeck(deck1);

        Stack<Card> deck2 = new Stack();
        deck2.push(new EnergyCard());
        deck2.push(new EvolvedPokemonCard());
        deck2.push(new TrainerCard());
        deck2.push(new EnergyCard());
        deck2.push(new EvolvedPokemonCard());
        deck2.push(new TrainerCard());
        deck2.push(new EnergyCard());
        deck2.push(new EvolvedPokemonCard());
        deck2.push(new TrainerCard());
        deck2.push(new EnergyCard());
        deck2.push(new EvolvedPokemonCard());
        deck2.push(new TrainerCard());
        deck2.push(new EnergyCard());
        deck2.push(opponentBasicPokemon);
        opponent.setDeck(deck2);

        */
    }

    @Test
    public void testDamageAbilityOnAPokemon() throws Exception {
        int amount = 30;
        Ability subAbility = null;
        AbilityTarget target = AbilityTarget.OPPONENT_ACTIVE;
        AbilityType type = AbilityType.DAM;
        DamageAbility damageAbility =  new DamageAbility(amount, target, type);
        PokemonCard gameow = new PokemonCard("Gameow", "Test Gameow", null, null,
                null, true, 30, 20,
                null, null, false, EnergyType.COLORLESS);
        gameow.setCardType(CardType.POKEMON);
        Set<Card> cards = new HashSet<>();
        cards.add(gameow);
        JPokemonBoardController pokemonController = new JPokemonBoardController();
        AbilityController controller = new AbilityController(pokemonController);
        Boolean isDamaged = controller.takeAbilityEffect(damageAbility, cards, false);
        assertEquals(true,isDamaged);

    }
}
