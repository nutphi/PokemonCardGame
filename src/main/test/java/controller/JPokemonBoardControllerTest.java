package controller;

import model.*;
import org.junit.Before;
import view.JPokemonBoard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import static org.junit.Assert.*;

/**
 * Created by nutph on 5/8/2017.
 */
public class JPokemonBoardControllerTest {
    private HumanPlayerController humanPlayerController = null;
    private ComputerPlayerController opponentController = null;
    private JPokemonBoard board = null;
    private JPokemonBoardController controller = null;
    private PokemonCard playerBasicPokemon = null;
    private PokemonCard opponentBasicPokemon = null;
    private PokemonCard basicPokemon1 = null;
    private PokemonCard basicPokemon2 = null;

    private Player player = null;
    private Player opponent = null;

    @Before
    public void beforeEachTest() throws Exception {
        humanPlayerController = new HumanPlayerController(7,6,5);
        opponentController = new ComputerPlayerController(7,6,5);

        basicPokemon1= new PokemonCard();
        basicPokemon1.setStatus(new HashSet());
        basicPokemon2 = new PokemonCard();
        basicPokemon2.setStatus(new HashSet());
        basicPokemon1.setTools(new ArrayList());
        basicPokemon2.setTools(new ArrayList());

        player = new Player();
        player.setPlayerHandCards(new ArrayList<Card>());
        player.setBenchPokemonCards(new ArrayList<PokemonCard>());
        player.setRewardCards(new ArrayList<Card>());
        playerBasicPokemon = new PokemonCard();

        opponent = new Player();
        opponent.setPlayerHandCards(new ArrayList<Card>());
        opponent.setBenchPokemonCards(new ArrayList<PokemonCard>());
        opponent.setRewardCards(new ArrayList<Card>());
        opponentBasicPokemon= new PokemonCard();



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

        humanPlayerController.setPlayer(player);
        opponentController.setPlayer(opponent);
        controller = new JPokemonBoardController(humanPlayerController,opponentController);
    }

    @org.junit.Test
    public void boardSetUpSets7CardsOnPlayerHandAnd7CardsOnOpponentHand() throws Exception {
        controller.getPlayerController().putNCardFromDeckToHand(7,true);
        controller.getOpponentController().putNCardFromDeckToHand(7,true);
        assertEquals("number of cards on the deck",7,controller.getPlayerController().getStartGameNumberHandCards());
        assertEquals("number of cards in opponent hand", 7, controller.getOpponentController().getStartGameNumberHandCards());
    }

    @org.junit.Test
    public void doMulliganTest() throws Exception{
        controller.getPlayerController().putNCardFromDeckToHand(7,true);
        controller.getOpponentController().putNCardFromDeckToHand(7,true);
        controller.doSetupAndMulliganPhase();
        assertNotNull("player hand cards list is not null",controller.getPlayerController().getPlayer().getBasicPokemonCardsOnHand());
        assertEquals("after mulligan, player has a basic pokemon card",CardType.POKEMON,controller.getPlayerController().getPlayer().getBasicPokemonCardsOnHand().get(0).getCardType());
        assertTrue("the pokemon card on player hand is not stage one pokemon",!(controller.getPlayerController().getPlayer().getBasicPokemonCardsOnHand().get(0) instanceof EvolvedPokemonCard));

        assertNotNull("opponent hand cards list is not null",controller.getOpponentController().getPlayer().getBasicPokemonCardsOnHand());
        assertEquals("after mulligan, opponent has a pokemon card",CardType.POKEMON,controller.getOpponentController().getPlayer().getBasicPokemonCardsOnHand().get(0).getCardType());
        assertTrue("the pokemon card on opponent hand is not stage one pokemon",!(controller.getOpponentController().getPlayer().getBasicPokemonCardsOnHand().get(0) instanceof EvolvedPokemonCard));
    }
}