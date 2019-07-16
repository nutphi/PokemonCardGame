package controller;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Stack;

import org.junit.Before;

import model.Card;
import model.EnergyCard;
import model.Player;
import model.PokemonCard;
import model.TrainerCard;
import view.JPokemonBoard;

/**
 * Created by nutph on 5/8/2017.
 */
public class PlayerControllerTest {
    private PlayerController controller = null;
    private Player player = null;
    private JPokemonBoard board = null;
    /**
     * 3 cards on deck, 3 cards on hand, no card on bench, active
     */
    @Before
    public void beforeEachTest(){
        controller = new HumanPlayerController();
        Card trainer1 = new TrainerCard();
        Card trainer2 = new TrainerCard();
        Card energy1 = new EnergyCard();
        Card energy2 = new EnergyCard();
        Card pokemon1 = new PokemonCard();
        Card pokemon2 = new PokemonCard();
        Stack<Card> deck = new Stack<Card>();
        deck.push(trainer1);
        deck.push(energy1);
        deck.push(pokemon1);
        board = JPokemonBoard.getBoard();
        ArrayList<Card> hand = new ArrayList();
        hand.add(pokemon2);
        hand.add(trainer2);
        hand.add(energy2);

        ArrayList<PokemonCard> bench = new ArrayList();
        player = new Player(null,bench,deck,null,hand,0,
                null,null);
        controller.setPlayer(player);
    }

    @org.junit.Test
    public void testSetUpDeck() throws Exception {
        player.setDeck(new Stack<Card>());
        ArrayList<Card> deck = new ArrayList();
        PokemonCard p = new PokemonCard();
        TrainerCard t = new TrainerCard();
        EnergyCard e = new EnergyCard();
        deck.add(p);
        deck.add(t);
        deck.add(e);

        controller.setUpDeck(deck);
        assertEquals("number of cards on the deck",3,player.getDeck().size());
        assertEquals("has pokemon card",true,player.getDeck().contains(p));
        assertEquals("has trainer card",true,player.getDeck().contains(t));
        assertEquals("has energy card",true,player.getDeck().contains(e));

    }

    @org.junit.Test
    public void testPutACardFromDeckToHand() throws Exception {
        assertEquals("hand size before put a card from deck to hand",3,player.getPlayerHandCards().size());
        assertEquals("deck size before put a card from deck to hand",3,player.getDeck().size());
        controller.putACardFromDeckToHand(false);
        assertEquals("hand size after put a card from deck to hand",4,player.getPlayerHandCards().size());
        assertEquals("deck size after put a card from deck to hand",2,player.getDeck().size());
    }

    @org.junit.Test
    public void testPutACardFromHandToActive() throws Exception {
        PokemonCard newCard = new PokemonCard();
        player.getPlayerHandCards().add(newCard);
        assertEquals("no active card",null,player.getActivePokemonCard());
        assertEquals("player has 4 cards on hand",4,player.getPlayerHandCards().size());

        controller.putACardFromHandToActive(newCard,false);
        assertEquals("has active card",newCard,player.getActivePokemonCard());
        assertEquals("player has 3 cards on hand",3,player.getPlayerHandCards().size());
    }

    @org.junit.Test
    public void testHasNoActivePokemonCard() throws Exception {
        assertEquals("no active pokemon card",true,controller.hasNoActivePokemonCard());
        player.setActivePokemonCard(new PokemonCard());
        assertEquals("has active pokemon card",false,controller.hasNoActivePokemonCard());

    }

}