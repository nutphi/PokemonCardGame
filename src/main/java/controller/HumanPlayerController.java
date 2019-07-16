package controller;

import model.*;
import model.ability.SearchAbility;
import model.ability.filter.Filter;
import view.JPokemonBoard;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * HumanPlayerController is used to control the state of human player
 * Created by nutph on 5/9/2017.
 */
public class HumanPlayerController extends PlayerController{

    public HumanPlayerController(){}

    public HumanPlayerController(int numberHandCards, int numberRewardCards, int numberBenchCards, Player player){
        super(numberHandCards,numberRewardCards,numberBenchCards,player);
    }

    public HumanPlayerController(int numberHandCards, int numberRewardCards, int numberBenchCards) {
        super(numberHandCards, numberRewardCards, numberBenchCards);
    }
    private PlayerState state;

    public PlayerState getState() {
        return state;
    }

    public void setState(PlayerState state) {
        this.state = state;
    }

    public void waitState(){
        synchronized (getPlayer().getState()){
            try{
                state = getPlayer().getState();
                getPlayer().getState().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyState(){
        if(!state.equals(getPlayer().getState()))
        {
            PlayerState temp = getPlayer().getState();
            getPlayer().setState(state);
            state = temp;
        }
        synchronized (getPlayer().getState()) {

            getPlayer().getState().notify();

        }
        getPlayer().setState(state);
    }

    public PokemonCard getNewActivePokemon(){
        if(getPlayer().getBenchPokemonCards().size()>0) {
            int benchNo = JOptionPane.CLOSED_OPTION;
            while(benchNo==JOptionPane.CLOSED_OPTION) {
                benchNo = JOptionPane.showOptionDialog(JPokemonBoard.getBoard().getJLabelBoard(),
                        "choose bench pokemon card to be on active area",
                        "pokemon is dead",
                        JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, getPokemonNameArrayOnBench(),
                        null);
                if(benchNo==JOptionPane.CLOSED_OPTION){
                    JPokemonBoard.getBoard().showMessageDialog("Need to choose a bench pokemon to be in the active area","Alert!!");
                }
            }
            PokemonCard c = getPlayer().getBenchPokemonCards().remove(benchNo);
            getPlayer().setActivePokemonCard(c);
            return c;
        }else {
            getPlayer().setActivePokemonCard(null);
            return null;
        }
    }


    /**
     * method evolvePokemon is to find basic pokemon options and pick the pokemon to evolve
     * method addBasicPokemonCardToStageOnePokemon handle information model
     * method putEvolvedPokemonOnPokemon to handle view
     * @param c EvolvedPokemonCard
     */
    public void evolvePokemon(EvolvedPokemonCard c){
        ArrayList<String> evolveNames = getEvolvingPokemonListNamesOnBoard(c);

        if(evolveNames.size()>0){
            int out = JPokemonBoard.getBoard().showOptionDialog("choose pokemon to evolve","evolution",evolveNames.toArray(),null);
            if(out!=JOptionPane.CLOSED_OPTION) {
                Player player = getPlayer();
                boolean isPokemonEvolve = evolveNames.get(0).endsWith("active");
                PokemonCard p = addBasicPokemonCardToStageOnePokemon(c,out,isPokemonEvolve);
                JPokemonBoard.getBoard().putEvolvedPokemonOnPokemon(c,p);
                player.getPlayerHandCards().remove(c);
            }
        }else{
            JPokemonBoard.getBoard().showMessageDialog("no pokemon on board to evolve","Problem!!");
        }
    }

    /**
     * for player to get option to choose
     * @param ability
     * @param opponentController
     * @return
     * @throws Exception
     */
    public String[] getPokemonOptionFromAbility(Ability ability, PlayerController opponentController) throws Exception{
        String[] pokemonCardsArray;
        AbilityTarget target = ability.getTarget();
        Set<AbilityType> abilityTypeSet = ability.getTypeSet();
        switch (target){
            case CHOICE_YOUR:
                //choice your (heal)
                ArrayList<PokemonCard> cardList;
                if(ability.getType().equals(AbilityType.ADD)){
                    cardList = getPokemonListFromActiveBench();
                    pokemonCardsArray = this.getPokemonNameArrayFromList(cardList);
                }else if(abilityTypeSet.containsAll(Arrays.asList(
                        AbilityType.HEAL,AbilityType.DESTAT))) {
                    cardList = getDamageStatusPokemonListFromActiveBench();
                    pokemonCardsArray = this.getPokemonNameArrayFromList(cardList);
                }else if(abilityTypeSet.contains(AbilityType.HEAL)){
                    cardList = getDamagePokemonListFromActiveBench();
                    pokemonCardsArray = this.getPokemonNameArrayFromList(cardList);
                }else if(abilityTypeSet.contains(AbilityType.DESTAT)){
                    cardList = getActiveStatusPokemonList();
                    pokemonCardsArray = this.getPokemonNameArrayFromList(cardList);
                }
                else if(ability.getType().equals(AbilityType.SEARCH)) {
                    SearchAbility searchAbility = ((SearchAbility) ability);
                    Filter.FilterType filterType = searchAbility.getSourceFilter().getFilterType();
                    if(filterType.equals(Filter.FilterType.EVOLVES_FROM)){
                        cardList = getActiveBenchPokemonListToEvolve();
                        pokemonCardsArray = this.getPokemonNameArrayFromList(cardList);
                    }else{
                        throw new Exception("this is not player filter for search ("+filterType+")");
                    }
                }else if(abilityTypeSet.contains(AbilityType.REENERGIZE)){
                    cardList = this.getPokemonListWithEnergies();
                    pokemonCardsArray = this.getPokemonNameArrayFromList(cardList);
                }else{
                    throw new Exception("this is not player choice ("+abilityTypeSet+")");
                }
                break;
            case CHOICE_OPPONENT:
                //choice opponent (attack)
                if(abilityTypeSet.contains(AbilityType.REDAMAGE)){
                    cardList = opponentController.getDamagePokemonListFromActiveBench();
                    pokemonCardsArray = opponentController.getPokemonNameArrayFromList(cardList);
                }else {
                    pokemonCardsArray = opponentController.getPokemonNameArrayOnBenchActive();
                }
                break;
            case CHOICE_YOUR_BENCH:
                pokemonCardsArray=this.getPokemonNameArrayOnBench();
                break;
            case CHOICE_OPPONENT_BENCH:
                pokemonCardsArray=opponentController.getPokemonNameArrayOnBench();
                break;
            default:
                throw new Exception("this is not player choice ("+target+")");
        }
        return pokemonCardsArray;
    }


    /**
     * for player to get output from option that player already chose
     * @param ability ability of the card
     * @param out the index of cards that provide on getPokemonOptionFromAbility method
     * @param opponentController opponent (as human player, it is ComputerPlayerController, as computer, it is HumanPlayerController)
     * @return pokemonCard that you chose from the mentioned method.
     * @throws Exception
     */
    public PokemonCard getPokemonCardFromOptionAbilityTarget(Ability ability, int out, PlayerController opponentController) throws Exception{
        AbilityTarget target = ability.getTarget();
        Set<AbilityType> abilityType = ability.getTypeSet();
        PokemonCard card;
        switch(target){
            case CHOICE_YOUR:
                if(ability.getType().equals(AbilityType.ADD)){
                    card = getPokemonFromActiveBenchOption(out);
                }else if(abilityType.contains(AbilityType.HEAL)||abilityType.contains(AbilityType.DESTAT)) {
                    card = getDamageStatusPokemonFromActiveBenchOption(out);
                }else if(abilityType.contains(AbilityType.HEAL)){
                    card = this.getDamagePokemonFromActiveBenchOption(out);
                }else if(abilityType.contains(AbilityType.DESTAT)){
                    card = getActiveStatusPokemonListOption(out);
                }else if(abilityType.contains(AbilityType.SEARCH)){
                    SearchAbility searchAbility = (SearchAbility)ability;
                    Filter.FilterType filterType = searchAbility.getSourceFilter().getFilterType();
                    if(filterType.equals(Filter.FilterType.EVOLVES_FROM)) {
                        card = getActiveBenchPokemonListToEvolveOption(out);
                    }else{
                        throw new Exception("this is not evolves from filter type ("+filterType+")");
                    }
                }else if(abilityType.contains(AbilityType.REENERGIZE)){
                    card = getPokemonListWithEnergies().get(out);
                }else{
                    throw new Exception("this is not player choice ("+abilityType+")");
                }
                break;
            case CHOICE_OPPONENT:
                if(abilityType.contains(AbilityType.REDAMAGE)){
                    card = opponentController.getDamagePokemonListFromActiveBench().get(out);
                }else {
                    card = opponentController.getPokemonFromActiveBenchOption(out);
                }
                break;
            case CHOICE_YOUR_BENCH:
                card = this.getPokemonFromBenchOption(out);
                break;
            case CHOICE_OPPONENT_BENCH:
                card = opponentController.getPokemonFromBenchOption(out);
                break;
            default:
                throw new Exception("this is not choice target for player");
        }
        return card;
    }

}
