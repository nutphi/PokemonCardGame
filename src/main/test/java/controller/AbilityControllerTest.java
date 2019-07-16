package controller;

import model.*;
import model.ability.HealAbility;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by nutph on 6/22/2017.
 */
public class AbilityControllerTest {
    private MockUpJPokemonBoardController jPokemonBoardController;
    private AbilityController abilityController;
    private ArrayList<Card> humanCards;
    private ArrayList<Card> computerCards;

    @Before
    public void beforeEachTest() throws Exception{
        int numberOfHandCard = 7;
        int numberOfRewardCards = 6;
        int numberOfBenchCard = 5;

        //index 0 glameow then 1-12 water energy , 13 lightning energy, 14 potion
        ArrayList<Integer> indexDeck1List = new ArrayList(Arrays.asList(1,2,2,2,2,2,2,2,2,2,2,2,2,3,4));
        ArrayList<Integer> indexDeck2List = new ArrayList(Arrays.asList(1,2,2,2,2,2,2,2,2,2,2,2,2,3,4));
        ArrayList<String> cardList = new ArrayList(Arrays.asList(
                "Glameow:pokemon:cat:basic:cat:colorless:60:retreat:cat:colorless:2:attacks:cat:colorless:1:1,cat:colorless:2:2",
                "Water:energy:cat:water","Lightning:energy:cat:lightning","Potion:trainer:cat:item:3"));
        ArrayList<String> abilityList = new ArrayList(Arrays.asList(
                "Act Cute:deck:target:them:destination:deck:bottom:choice:target:1",
                "Scratch:dam:target:opponent-active:20","Potion:heal:target:your:30"));
        DeckCreator deckCreator = new DeckCreator(indexDeck1List,indexDeck2List,cardList,abilityList);

        humanCards = deckCreator.createDeck(false);
        computerCards = deckCreator.createDeck(true);

        PokemonCard humanActivePokemonCard = (PokemonCard)humanCards.get(0);
        PokemonCard computerActivePokemonCard = (PokemonCard)computerCards.get(0);

        ArrayList<PokemonCard> humanBenchPokemonCards = new ArrayList<>();
        ArrayList<PokemonCard> computerBenchPokemonCards = new ArrayList<>();

        Stack<Card> humanDeck = new Stack<Card>();
        humanDeck.push(humanCards.get(1));
        humanDeck.push(humanCards.get(2));
        humanDeck.push(humanCards.get(3));

        Stack<Card> humanTrash = new Stack<Card>();
        humanTrash.push(humanCards.get(4));
        humanTrash.push(humanCards.get(5));
        humanTrash.push(humanCards.get(6));

        Stack<Card> computerDeck = new Stack<Card>();
        computerDeck.push(computerCards.get(1));
        computerDeck.push(computerCards.get(2));
        computerDeck.push(computerCards.get(3));

        Stack<Card> computerTrash = new Stack<Card>();
        computerTrash.push(computerCards.get(4));
        computerTrash.push(computerCards.get(5));
        computerTrash.push(computerCards.get(6));

        ArrayList<Card> humanHandCards = new ArrayList<Card>();
        humanHandCards.add(humanCards.get(7));
        humanHandCards.add(humanCards.get(8));
        humanHandCards.add(humanCards.get(9));

        ArrayList<Card> computerHandCards = new ArrayList<Card>();
        computerHandCards.add(computerCards.get(7));
        computerHandCards.add(computerCards.get(8));
        computerHandCards.add(computerCards.get(9));


        int killedPokemonAmount =0;

        PlayerState playerState = PlayerState.POKEMON_ENERGY_SUPPORTER_ITEM;

        ArrayList<Card> rewardCards = new ArrayList<Card>();

        Player humanPlayer = new Player(humanActivePokemonCard,humanBenchPokemonCards,humanDeck,
                humanTrash,humanHandCards,killedPokemonAmount,playerState,rewardCards);
        Player computerPlayer = new Player(computerActivePokemonCard,computerBenchPokemonCards,computerDeck,
                computerTrash,computerHandCards,killedPokemonAmount,playerState,rewardCards);

        HumanPlayerController humanPlayerController = new HumanPlayerController(
                numberOfHandCard, numberOfRewardCards,
                numberOfBenchCard, humanPlayer);

        ComputerPlayerController computerPlayerController = new ComputerPlayerController(
                numberOfHandCard, numberOfRewardCards,
                numberOfBenchCard, computerPlayer);

        jPokemonBoardController = new MockUpJPokemonBoardController(humanPlayerController,computerPlayerController);
        abilityController = new AbilityController(jPokemonBoardController);
    }

    @Test
    public void usePokemonDamAbilitytest() throws Exception{
        PokemonCard humanActivePokemonCard = jPokemonBoardController.getPlayerController().getPlayer().getActivePokemonCard();
        ArrayList<PokemonAbility> humanActivePokemonAbilities = humanActivePokemonCard.getAbilities();

        //dam
        Ability activePokemonUsingAbility = humanActivePokemonAbilities.get(1).getAbility();
        boolean isAttackedOpponent = false;
        Set<Card> lastTargetCards = new HashSet<Card>();

        PokemonCard computerActivePokemonCard = jPokemonBoardController.getOpponentController().getPlayer().getActivePokemonCard();

        assertEquals("active pokemon with 0 dam",0,computerActivePokemonCard.getDamage());

        abilityController.useAbility(activePokemonUsingAbility,isAttackedOpponent,lastTargetCards);

        assertEquals("active pokemon is attacked with 20 dam ability",20,computerActivePokemonCard.getDamage());
    }

    @Test
    public void usePokemonDeckAbilitytest() throws Exception{
        PokemonCard humanActivePokemonCard = jPokemonBoardController.getPlayerController().getPlayer().getActivePokemonCard();
        ArrayList<PokemonAbility> humanActivePokemonAbilities = humanActivePokemonCard.getAbilities();

        //deck
        Ability activePokemonUsingAbility = humanActivePokemonAbilities.get(0).getAbility();
        boolean isAttackedOpponent = false;
        Set<Card> lastTargetCards = new HashSet<Card>();


        Stack<Card> opponentDeck = jPokemonBoardController.getOpponentController().getPlayer().getDeck();
        Card oldBottomCard = opponentDeck.get(0);
        int oldOpponentHandCardsSize = jPokemonBoardController.getOpponentController().getPlayer().getPlayerHandCards().size();
        int oldOpponentDeckSize =jPokemonBoardController.getOpponentController().getPlayer().getDeck().size();


        abilityController.useAbility(activePokemonUsingAbility,isAttackedOpponent,lastTargetCards);
        Card newBottomCard = opponentDeck.get(0);

        int newOpponentHandCardsSize = jPokemonBoardController.getOpponentController().getPlayer().getPlayerHandCards().size();
        int newOpponentDeckSize = jPokemonBoardController.getOpponentController().getPlayer().getDeck().size();


        assertEquals("number of cards on opponent hand reduce 1",oldOpponentHandCardsSize-1,newOpponentHandCardsSize);

        assertEquals("number of cards on opponent deck increase 1",oldOpponentDeckSize+1,newOpponentDeckSize);

        //both of them are water card
        assertTrue("Opponent put card at the bottom of deck",computerCards.get(1).getName().equals(newBottomCard.getName()));
    }

    @Test
    public void useTrainerPotionAbilitytest() throws Exception{

        assertEquals("this is a trainer card",CardType.TRAINER,humanCards.get(14).getCardType());
        TrainerCard trainerCard = (TrainerCard)humanCards.get(14);
        assertEquals("this is item card",TrainerType.ITEM,trainerCard.getTrainerType());
        assertEquals("this is potion","Potion", trainerCard.getName());

        ArrayList<TrainerAbility> trainerAbilities = trainerCard.getAbilities();

        HealAbility potionAbility = (HealAbility)(trainerAbilities.get(0).getAbility());
        assertEquals("this is heal ability",AbilityType.HEAL, potionAbility.getType());


        jPokemonBoardController.getPlayerController().getPlayer().getPlayerHandCards().add(trainerCard);

        PokemonCard activePokemon = jPokemonBoardController.getPlayerController().getPlayer().getActivePokemonCard();
        activePokemon.setDamage(20);

        assertEquals("active pokemon damage",20, activePokemon.getDamage());

        jPokemonBoardController.useTrainerCard(trainerCard,false);

        assertEquals("active pokemon damage",0, activePokemon.getDamage());

    }
}