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
public class ApplyStatAbilityTest {
    private MockUpJPokemonBoardController jPokemonBoardController;
    private AbilityController abilityController;
    private ArrayList<Card> humanCards;
    private ArrayList<Card> computerCards;

    @Before
    public void beforeEachTest() throws Exception{
        int numberOfHandCard = 7;
        int numberOfRewardCards = 6;
        int numberOfBenchCard = 5;

        //index 0 jynx then 1-12 water energy , 13 lightning energy
        ArrayList<Integer> indexDeck1List = new ArrayList(Arrays.asList(1,2,2,2,2,2,2,2,2,2,2,2,2,3));
        ArrayList<Integer> indexDeck2List = new ArrayList(Arrays.asList(1,2,2,2,2,2,2,2,2,2,2,2,2,3));
        ArrayList<String> cardList = new ArrayList(Arrays.asList(
                "Jynx:pokemon:cat:basic:cat:psychic:70:retreat:cat:colorless:1:attacks:cat:colorless:2,cat:psychic:1:1",
                "Lightning:energy:cat:lightning","Water:energy:cat:water"));
        ArrayList<String> abilityList = new ArrayList(Arrays.asList(
                "Hug:dam:target:opponent-active:30,applystat:status:stuck:opponent-active"));
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
        humanDeck.push(humanCards.get(4));
        humanDeck.push(humanCards.get(5));
        humanDeck.push(humanCards.get(6));

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
    public void useApplyStatTest() throws Exception{
        PokemonCard humanActivePokemonCard = jPokemonBoardController.getPlayerController().getPlayer().getActivePokemonCard();
        ArrayList<PokemonAbility> humanActivePokemonAbilities = humanActivePokemonCard.getAbilities();

        //applystat
        Ability activePokemonUsingAbility = humanActivePokemonAbilities.get(0).getAbility();
        boolean isOpponent = false;
        Set<Card> lastTargetCards = new HashSet<Card>();
        PokemonCard computerActivePokemonCard = jPokemonBoardController.getOpponentController().getPlayer().getActivePokemonCard();
        assertTrue("active pokemon no status",!computerActivePokemonCard.getStatus().contains(PokemonStatus.STUCK));
        assertEquals("active pokemon damage",0,computerActivePokemonCard.getDamage());

        abilityController.useAbility(activePokemonUsingAbility,isOpponent,lastTargetCards);

        assertTrue("active pokemon stuck status",computerActivePokemonCard.getStatus().contains(PokemonStatus.STUCK));
        assertEquals("active pokemon damage",30,computerActivePokemonCard.getDamage());
    }
}