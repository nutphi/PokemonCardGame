package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

/**
 * Created by nutph on 5/6/2017.
 */
public class Player {
    private PlayerState state;
    private PokemonCard activePokemonCard;
    private ArrayList<PokemonCard> benchPokemonCards;
    private Stack<Card> deck;
    private Stack<Card> trash;
    private ArrayList<Card> playerHandCards;
    private ArrayList<Card> rewardCards;

    public void shuffleDeck(){
        Collections.shuffle(deck);
    }

    public Player(){}

    public Player(PokemonCard activePokemonCard, ArrayList<PokemonCard> benchPokemonCards, Stack<Card> deck, Stack<Card> trash, ArrayList<Card> playerHandCards, int killedPokemonAmount, PlayerState playerState, ArrayList<Card> rewardCards) {
        this.activePokemonCard = activePokemonCard;
        this.benchPokemonCards = benchPokemonCards;
        this.deck = deck;
        this.trash = trash;
        this.playerHandCards = playerHandCards;
        this.state = playerState;
        this.rewardCards = rewardCards;
    }

    public PokemonCard getActivePokemonCard() {
        return activePokemonCard;
    }

    public void setActivePokemonCard(PokemonCard activePokemonCard) {
        this.activePokemonCard = activePokemonCard;
    }

    public ArrayList<PokemonCard> getBenchPokemonCards() {
        return benchPokemonCards;
    }

    public void setBenchPokemonCards(ArrayList<PokemonCard> benchPokemonCards) {
        this.benchPokemonCards = benchPokemonCards;
    }

    public Stack<Card> getDeck() {
        return deck;
    }

    public void setDeck(Stack<Card> deck) {
        this.deck = deck;
    }

    public Stack<Card> getTrash() {
        return trash;
    }

    public void setTrash(Stack<Card> trash) {
        this.trash = trash;
    }

    public ArrayList<Card> getPlayerHandCards() {
        return playerHandCards;
    }

    public void setPlayerHandCards(ArrayList<Card> playerHandCards) {
        this.playerHandCards = playerHandCards;
    }

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public ArrayList<Card> getRewardCards() {
        return rewardCards;
    }

    public void setRewardCards(ArrayList<Card> rewardCards) {
        this.rewardCards = rewardCards;
    }

    public boolean isBasicPokemonCardOnHand(){
        for(Card card:playerHandCards){
            if(card.isBasicPokemonCard())
            return true;
        }
        return false;
    }

    public boolean isThisCardOnHand(Card c){
        for(Card card:playerHandCards){
            if(card.equals(c)){
                return true;
            }
        }
        return false;
    }

    public Card getThisCardOnHand(Card c){
        for(Card card:playerHandCards){
            if(card.equals(c)){
                return c;
            }
        }
        return null;
    }

    public ArrayList<PokemonCard> getBasicPokemonCardsOnHand(){
        ArrayList<PokemonCard> pokemonCards = new ArrayList<PokemonCard>();
        for(Card card:playerHandCards){
            if(card instanceof PokemonCard && !(card instanceof EvolvedPokemonCard)){
                pokemonCards.add((PokemonCard)card);
            }
        }
        return pokemonCards;
    }

    public ArrayList<EnergyCard> getEnergyCardsOnHand(){
        ArrayList<EnergyCard> energyCards = new ArrayList();
        for(Card card:playerHandCards){
            if(card instanceof EnergyCard){
                energyCards.add((EnergyCard)card);
            }
        }
        return energyCards;
    }

    public void addEnergyCardOnHandToPokemon(PokemonCard pokemon, EnergyCard energyCard) {
        pokemon.getEnergies().add(energyCard);
        getPlayerHandCards().remove(energyCard);
        if (getState().equals(PlayerState.ENERGY_SUPPORTER_ITEM)){
            setState(PlayerState.SUPPORTER_ITEM);
        }else if (getState().equals(PlayerState.ENERGY_ITEM)){
            setState(PlayerState.ITEM);
        }else if(getState().equals(PlayerState.POKEMON_ENERGY_SUPPORTER_ITEM)){
            setState(PlayerState.POKEMON_SUPPORTER_ITEM);
        }else {
            //if(getState().equals(PlayerState.POKEMON_ENERGY_ITEM))
            setState(PlayerState.POKEMON_ITEM);
        }
    }

    public void useTrainerCardOnHand(TrainerCard trainerCard, boolean isMovingToTrash){
        getPlayerHandCards().remove(trainerCard);
        if(isMovingToTrash) {
            trainerCard.setVisibleCard(true);
            getTrash().push(trainerCard);
        }
        if(trainerCard.getTrainerType().equals(TrainerType.SUPPORTER)){
            if (getState().equals(PlayerState.ENERGY_SUPPORTER_ITEM)){
                setState(PlayerState.ENERGY_ITEM);
            }else if (getState().equals(PlayerState.SUPPORTER_ITEM)){
                setState(PlayerState.ITEM);
            }else if(getState().equals(PlayerState.POKEMON_ENERGY_SUPPORTER_ITEM)){
                setState(PlayerState.POKEMON_ENERGY_ITEM);
            }else {
                //if(getState().equals(PlayerState.POKEMON_SUPPORTER_ITEM))
                setState(PlayerState.POKEMON_ITEM);
            }
        }
    }

    public ArrayList<EvolvedPokemonCard> getEvolvedPokemonCard(){
        if(getPlayerHandCards()!=null){
            ArrayList<EvolvedPokemonCard> evolvedPokemonCards = new ArrayList<EvolvedPokemonCard>();
            for(Card card:getPlayerHandCards()){
                if(card instanceof EvolvedPokemonCard){
                    evolvedPokemonCards.add((EvolvedPokemonCard)card);
                }
            }
            return evolvedPokemonCards;
        }else{
            return null;
        }
    }

    public boolean isPokemonCardOnActiveBench(PokemonCard pokemonCard){
        if(pokemonCard.equals(activePokemonCard)){
            return true;
        }
        for (PokemonCard benchPokemonCard:benchPokemonCards) {
            if(benchPokemonCard.equals(pokemonCard)){
                return true;
            }
        }
        return false;
    }

    public void changeNewToOldPokemon(){
        if(getActivePokemonCard()!=null)
            getActivePokemonCard().setNewPokemon(false);
        for(PokemonCard c:getBenchPokemonCards()){
            c.setNewPokemon(false);
        }
    }
}