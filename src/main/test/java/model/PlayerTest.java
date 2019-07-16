package model;

import model.ability.filter.PokemonFilter;
import org.junit.Test;

import javax.swing.*;
import java.util.ArrayList;
//import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class PlayerTest {

   /* member variables
    private PlayerState state;
    private PokemonCard activePokemonCard;
    private ArrayList<PokemonCard> benchPokemonCards;
    private Stack<Card> deck;
    private Stack<Card> trash;
    private ArrayList<Card> playerHandCards;
    private ArrayList<Card> rewardCards;*/



    @Test
    public void shuffleDeck() throws Exception {

    }


    private ArrayList<Card> getBaseCards(){
        ArrayList<Card> handCards = new ArrayList<Card>();
        ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
        ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.jpg"));
        ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.jpg"));

        PokemonCard gameow = new PokemonCard("Gameow", "Test Gameow", visible, invisible,
                bigVisible, true, 30, 20,
        null, null, false, null);
        gameow.setCardType(CardType.POKEMON);

        handCards.add(gameow);
        return handCards;
    }

    private ArrayList<Card> getNonBaseCards(){
        ArrayList<Card> handCards = new ArrayList<Card>();
        ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
        ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.jpg"));
        ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.jpg"));

        PokemonCard purgly = new PokemonCard("Purgly", "Test Purgly", visible, invisible,
                bigVisible, true, 30, 20,
                new ArrayList(), new ArrayList(), false, EnergyType.COLORLESS);
        purgly.setCardType(CardType.ENERGY);

        handCards.add(purgly);
        return handCards;
    }

    private ArrayList<Card> getTwoCards(){
        ArrayList<Card> handCards = new ArrayList<Card>();

        ImageIcon invisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.png"));
        ImageIcon visible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.jpg"));
        ImageIcon bigVisible = new ImageIcon(getClass().getClassLoader().getResource("images/invisible.jpg"));

        Card gameow = new PokemonCard("Gameow", "Test Gameow", visible, invisible,
                bigVisible, true, 30, 20,
                null, null, false, null);
        gameow.setCardType(CardType.POKEMON);

        Card purgly = new EnergyCard( EnergyType.COLORLESS, CardType.ENERGY, "Water Energy","Water", false);

        handCards.add(gameow);
        handCards.add(purgly);
        return handCards;
    }
}