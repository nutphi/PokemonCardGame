package controller;

import model.*;
import model.ability.DamageAbility;
import model.ability.DeckAbility;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;


public class DeckCreatorTest {
    @Test
    public void createGlameowCardTest() throws Exception {
        ArrayList<Integer> indexDeck1List = new ArrayList(Arrays.asList(1));
        ArrayList<Integer> indexDeck2List = new ArrayList(Arrays.asList(1));
        ArrayList<String> cardList = new ArrayList(Arrays.asList(
                "Glameow:pokemon:cat:basic:cat:colorless:60:retreat:cat:colorless:2:attacks:cat:colorless:1:1,cat:colorless:2:2"));
        ArrayList<String> abilityList = new ArrayList(Arrays.asList(
                "Act Cute:deck:target:them:destination:deck:bottom:choice:target:1",
                "Scratch:dam:target:opponent-active:20"));

        DeckCreator deckCreator = new DeckCreator(indexDeck1List, indexDeck2List, cardList, abilityList);

        ArrayList<Card> cards = deckCreator.createDeck(true);
        Card card = cards.get(0);

        assertEquals("Card Name","Glameow",card.getName());
        assertEquals("Card Type", CardType.POKEMON,card.getCardType());

        PokemonCard glameow = (PokemonCard)card;
        assertEquals("Glameow Hit Point",60,glameow.getHitpoint());

        ArrayList<EnergyType> retreatExpect = new ArrayList(Arrays.asList(EnergyType.COLORLESS,EnergyType.COLORLESS));
        assertEquals("Glameow Retreat",retreatExpect,glameow.getRetreat());

        PokemonAbility firstPokemonAbility = glameow.getAbilities().get(0);
        assertEquals("First Ability Name Act Cute","Act Cute",firstPokemonAbility.getName());

        ArrayList<EnergyType> ability1EnergyTypesExpect = new ArrayList(Arrays.asList(EnergyType.COLORLESS));
        assertEquals("Act Cute Energy Type", ability1EnergyTypesExpect, firstPokemonAbility.getEnergyAmount());

        Ability actCuteAbility = firstPokemonAbility.getAbility();
        assertEquals("Act Cute Ability Type",AbilityType.DECK,actCuteAbility.getType());
        assertEquals("Act Cute Ability Target",AbilityTarget.OPPONENT,actCuteAbility.getTarget());

        DeckAbility actCuteDeckAbility = (DeckAbility)actCuteAbility;
        assertEquals("Act Cute Deck Destination", DeckAbility.Destination.BOTTOM_DECK,actCuteDeckAbility.getDestination());
        assertEquals("Act Cute Deck Choice",DeckAbility.Choice.OPPONENT_CHOOSE,actCuteDeckAbility.getChoice());
        assertEquals("Act Cute Deck Amount",1,actCuteDeckAbility.getAmount());


        PokemonAbility secondPokemonAbility = glameow.getAbilities().get(1);
        assertEquals("Second Ability Name Act Cute","Scratch",secondPokemonAbility.getName());

        ArrayList<EnergyType> ability2EnergyTypes = new ArrayList(Arrays.asList(EnergyType.COLORLESS,EnergyType.COLORLESS));
        assertEquals("Scratch Energy Types",ability2EnergyTypes,secondPokemonAbility.getEnergyAmount());

        Ability scratchAbility  = secondPokemonAbility.getAbility();
        assertEquals("Scratch Ability Type",AbilityType.DAM,scratchAbility.getType());
        assertEquals("Scratch Ability Target",AbilityTarget.OPPONENT_ACTIVE,scratchAbility.getTarget());

        DamageAbility scratchDamageAbility = (DamageAbility)secondPokemonAbility.getAbility();
        assertEquals("Scratch Damage",20,scratchDamageAbility.getAmount());
    }

    @Test
    public void createPikachuuCardTest() throws Exception {
        ArrayList<Integer> indexDeck1List = new ArrayList(Arrays.asList(1));
        ArrayList<Integer> indexDeck2List = new ArrayList(Arrays.asList(1));
        ArrayList<String> cardList = new ArrayList(Arrays.asList(
                "Pikachu:pokemon:cat:basic:cat:lightning:60:retreat:cat:colorless:1:attacks:cat:colorless:1:1,cat:colorless:2:2"));
        ArrayList<String> abilityList = new ArrayList(Arrays.asList(
                "Nuzzle:cond:flip:applystat:status:paralyzed:opponent-active",
                        "Quick Attack:dam:target:opponent-active:20,cond:flip:dam:target:opponent-active:10"));

        DeckCreator deckCreator = new DeckCreator(indexDeck1List, indexDeck2List, cardList, abilityList);
        ArrayList<Card> cards = deckCreator.createDeck(true);
        Card card = cards.get(0);
        assertEquals("Card Name","Pikachu",card.getName());
        assertEquals("Card Type", CardType.POKEMON,card.getCardType());

        PokemonCard pikachu = (PokemonCard)card;
        assertEquals("Glameow Hit Point",60,pikachu.getHitpoint());
    }

    //Fury Attack:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40

    @Test
    public void createFuryAttackCardTest() throws Exception {
        ArrayList<Integer> indexDeck1List = new ArrayList(Arrays.asList(1));
        ArrayList<Integer> indexDeck2List = new ArrayList(Arrays.asList(1));
        ArrayList<String> cardList = new ArrayList(Arrays.asList("Dodrio:pokemon:cat:stage-one:Doduo:cat:colorless:90:retreat:cat:colorless:1:attacks:cat:colorless:3:1"));
        ArrayList<String> abilityList = new ArrayList(Arrays.asList(
                "Fury Attack:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40"));


        DeckCreator deckCreator = new DeckCreator(indexDeck1List, indexDeck2List, cardList, abilityList);
        ArrayList<Card> cards = deckCreator.createDeck(true);
        Card card = cards.get(0);
        assertEquals("Card Name","Dodrio",card.getName());
        assertEquals("Card Type", CardType.POKEMON,card.getCardType());

        PokemonCard pikachu = (PokemonCard)card;
        assertEquals("Glameow Hit Point",90,pikachu.getHitpoint());
    }


    @Test
    public void createMistyTrainerCardTest() throws Exception {
        ArrayList<Integer> indexDeck1List = new ArrayList(Arrays.asList(1));
        ArrayList<Integer> indexDeck2List = new ArrayList(Arrays.asList(1));
        ArrayList<String> cardList = new ArrayList(Arrays.asList(
                "Misty's Determination:trainer:cat:supporter:1"));
        ArrayList<String> abilityList = new ArrayList(Arrays.asList(
                "Potion:heal:target:your:30"));

        DeckCreator deckCreator = new DeckCreator(indexDeck1List, indexDeck2List, cardList, abilityList);
        ArrayList<Card> cards = deckCreator.createDeck(true);
        Card card = cards.get(0);
        assertEquals("Card Name","Misty's Determination",card.getName());
        assertEquals("Card Type", CardType.TRAINER,card.getCardType());

    }

    //Floral Crown:trainer:cat:item:67
    @Test
    public void createFloralCrownTrainerCardTest() throws Exception {
        ArrayList<Integer> indexDeck1List = new ArrayList(Arrays.asList(1));
        ArrayList<Integer> indexDeck2List = new ArrayList(Arrays.asList(1));
        ArrayList<String> cardList = new ArrayList(Arrays.asList(
                "Floral Crown:trainer:cat:item:1"));
        ArrayList<String> abilityList = new ArrayList(Arrays.asList(
                "Floral Crown:add:target:your:trigger:opponent:turn-end:(heal:target:self:20)"));

        DeckCreator deckCreator = new DeckCreator(indexDeck1List, indexDeck2List, cardList, abilityList);
        ArrayList<Card> cards = deckCreator.createDeck(true);
        Card card = cards.get(0);
        assertEquals("Card Name","Floral Crown",card.getName());
        assertEquals("Card Type", CardType.TRAINER,card.getCardType());

    }

    //Water:energy:cat:water
    @Test
    public void createWaterEnergyCardTest() throws Exception {
        ArrayList<Integer> indexDeck1List = new ArrayList(Arrays.asList(1));
        ArrayList<Integer> indexDeck2List = new ArrayList(Arrays.asList(1));
        ArrayList<String> cardList = new ArrayList(Arrays.asList(
                "Water:energy:cat:water"));
        ArrayList<String> abilityList = new ArrayList(Arrays.asList(
                "Potion:heal:target:your:30"));

        DeckCreator deckCreator = new DeckCreator(indexDeck1List, indexDeck2List, cardList, abilityList);
        ArrayList<Card> cards = deckCreator.createDeck(true);
        Card card = cards.get(0);
        assertEquals("Card Name","Water",card.getName());
        assertEquals("Card Type", CardType.ENERGY,card.getCardType());

    }

    //Persian:pokemon:cat:stage-one:Meowth:cat:colorless:90:retreat:cat:colorless:1:attacks:cat:colorless:1:65,cat:colorless:2:66

    @Test
    public void createPersianCardTest() throws Exception {
        ArrayList<Integer> indexDeck1List = new ArrayList(Arrays.asList(1));
        ArrayList<Integer> indexDeck2List = new ArrayList(Arrays.asList(1));
        ArrayList<String> cardList = new ArrayList(Arrays.asList(
                "Persian:pokemon:cat:stage-one:Meowth:cat:colorless:90:retreat:cat:colorless:1:attacks:cat:colorless:1:1,cat:colorless:2:2",
                "Meowth:pokemon:cat:basic:cat:colorless:60:retreat:cat:colorless:1:attacks:cat:colorless:1:41"));
        ArrayList<String> abilityList = new ArrayList(Arrays.asList(
                "Fake Out:dam:target:opponent-active:30,cond:flip:applystat:status:paralyzed:opponent-active" ,
                        "Ambush:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:30"));

        DeckCreator deckCreator = new DeckCreator(indexDeck1List, indexDeck2List, cardList, abilityList);
        ArrayList<Card> cards = deckCreator.createDeck(true);
        Card card = cards.get(0);
        assertEquals("Card Name","Persian",card.getName());
        assertEquals("Card Type", CardType.POKEMON,card.getCardType());

        PokemonCard slowpoke = (PokemonCard)card;
        assertEquals(" Hit Point",90,slowpoke.getHitpoint());
    }


    //Slowpoke:pokemon:cat:basic:cat:psychic:50:retreat:cat:colorless:1:attacks:cat:colorless:1:57,cat:psychic:2:58
    @Test
    public void createSlowpokeCardTest() throws Exception {
        ArrayList<Integer> indexDeck1List = new ArrayList(Arrays.asList(1));
        ArrayList<Integer> indexDeck2List = new ArrayList(Arrays.asList(1));
        ArrayList<String> cardList = new ArrayList(Arrays.asList(
                "Slowpoke:pokemon:cat:basic:cat:psychic:50:retreat:cat:colorless:1:attacks:cat:colorless:1:1,cat:psychic:2:2"));
        ArrayList<String> abilityList = new ArrayList(Arrays.asList(
                "Spacing Out:cond:flip:heal:target:your-active:10",
                "Scavenge:cond:ability:deenergize:target:your-active:1:(search:target:you:source:discard:filter:cat:item:1)"));

        DeckCreator deckCreator = new DeckCreator(indexDeck1List, indexDeck2List, cardList, abilityList);
        ArrayList<Card> cards = deckCreator.createDeck(true);
        Card card = cards.get(0);
        assertEquals("Card Name","Slowpoke",card.getName());
        assertEquals("Card Type", CardType.POKEMON,card.getCardType());

        PokemonCard slowpoke = (PokemonCard)card;
        assertEquals(" Hit Point",50,slowpoke.getHitpoint());
    }

    //Meowstic:pokemon:cat:stage-one:Espurr:cat:psychic:90:retreat:cat:colorless:1:attacks:cat:psychic:1:36,cat:psychic:3:37
    @Test
    public void createMeowsticCardTest() throws Exception {
        ArrayList<Integer> indexDeck1List = new ArrayList(Arrays.asList(1));
        ArrayList<Integer> indexDeck2List = new ArrayList(Arrays.asList(1));
        ArrayList<String> cardList = new ArrayList(Arrays.asList(
                "Meowstic:pokemon:cat:stage-one:Espurr:cat:psychic:90:retreat:cat:colorless:1:attacks:cat:psychic:1:1,cat:psychic:3:2"));
        ArrayList<String> abilityList = new ArrayList(Arrays.asList(
                "Ear Influence:redamage:target:opponent:target:opponent:count(target:last:source:damage)" ,
                        "Psychic:dam:target:opponent-active:60,dam:target:opponent-active:count(target:opponent-active:energy)*10"));

        DeckCreator deckCreator = new DeckCreator(indexDeck1List, indexDeck2List, cardList, abilityList);
        ArrayList<Card> cards = deckCreator.createDeck(true);
        Card card = cards.get(0);
        assertEquals("Card Name","Meowstic",card.getName());
        assertEquals("Card Type", CardType.POKEMON,card.getCardType());

        PokemonCard slowpoke = (PokemonCard)card;
        assertEquals(" Hit Point",90,slowpoke.getHitpoint());
    }

}
