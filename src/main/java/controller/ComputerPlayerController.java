package controller;

import model.*;
import model.ability.AddAbility;
import model.ability.ConditionAbility;
import model.ability.SearchAbility;
import model.ability.filter.Filter;
import model.ability.filter.TrainerFilter;
import view.BoardPosition;
import view.JPokemonBoard;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;

/**
 * Computer Player will be controlled by this class
 * Created by nutph on 5/9/2017.
 */
public class ComputerPlayerController extends PlayerController {

    /**
     * Default constructor
     */
    public ComputerPlayerController(){}

    public ComputerPlayerController(int numberHandCards, int numberRewardCards, int numberBenchCards, Player player){
        super(numberHandCards,numberRewardCards,numberBenchCards,player);
    }

    /**
     * Constructor
     * @param numberHandCards the number of cards on the computer hand that can be drawn
     *                       at the beginning of the game
     * @param numberRewardCards the number of reward cards from the computer side
     * @param numberBenchCards the number of pokemon bench cards that can put
     *                         at the beginning of the game
     */
    public ComputerPlayerController(int numberHandCards, int numberRewardCards, int numberBenchCards) {
        super(numberHandCards, numberRewardCards, numberBenchCards);
    }

    /**
     * Show pokemonCard on the bench and active at the beginning of the game
     */
    public void showCardOnActiveAndBench(){
        getPlayer().getActivePokemonCard().setVisibleCard(true);
        ArrayList<PokemonCard> benchCards = getPlayer().getBenchPokemonCards();
        for(int i=0;i<benchCards.size();++i){
            benchCards.get(i).setVisibleCard(true);
        }
    }

    public PokemonCard getNewActivePokemon(){
        ArrayList<PokemonCard> benchCards = getPlayer().getBenchPokemonCards();
        if(benchCards.size()>0) {
            PokemonCard c = benchCards.remove(0);
            getPlayer().setActivePokemonCard(c);
            return c;
        }else
            getPlayer().setActivePokemonCard(null);
            return null;
    }

    public void evolvePokemon(EvolvedPokemonCard c) {
        ArrayList<String> evolveNames = getEvolvingPokemonListNamesOnBoard(c);
        if(evolveNames.size()>0){
            Player player = getPlayer();
            boolean isPokemonEvolve = evolveNames.get(0).endsWith("active");
            player.getPlayerHandCards().remove(c);
            PokemonCard p = addBasicPokemonCardToStageOnePokemon(c,0,isPokemonEvolve);
            JPokemonBoard.getBoard().putEvolvedPokemonOnPokemon(c,p);
            player.getBenchPokemonCards().remove(p);
            c.setVisibleCard(true);
        }
    }

    /**
     * put the first basic pokemon to the active area
     */
    public void actionForStartActive() {
        JPokemonBoard.getBoard().showMessageDialog("opponent active","Change turn!!!");
        ArrayList<PokemonCard> pokemonCards = getPlayer().getBasicPokemonCardsOnHand();
        PokemonCard pokemonCard =pokemonCards.get(0);
        getPlayer().getPlayerHandCards().remove(pokemonCard);
        getPlayer().setActivePokemonCard(pokemonCard);
        try {
            JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.ACTIVE_POKEMON,pokemonCard,true,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * put all basic pokemons to the bench area
     */
    public void actionForStartBench(){
        JPokemonBoard.getBoard().showMessageDialog("opponent bench","Change turn!!!");
        ArrayList<PokemonCard> pokemonCards = getPlayer().getBasicPokemonCardsOnHand();
        try {
            int number;
            if(pokemonCards.size()>getMaxNumberBenchCards()){
                number = getMaxNumberBenchCards();
            }else{
                number = pokemonCards.size();
            }
            for(int i=0;i<number;++i) {
                PokemonCard pokemonCard =pokemonCards.get(i);
                getPlayer().getBenchPokemonCards().add(pokemonCard);
                JPokemonBoard.getBoard().setGroupPositionBoard(BoardPosition.BENCH_POKEMON, getPlayer().getBenchPokemonCards(), true,false);
                getPlayer().getPlayerHandCards().remove(pokemonCard);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * put the first energy to active pokemon, and maybe use trainer card to do something..
     */
    public void actionForStartEnergyTrainer(JPokemonBoardController controller){
        JPokemonBoard.getBoard().showMessageDialog("opponent energy trainer","Information");
    }

    public void actionForPokemonEnergyTrainer(JPokemonBoardController controller) throws Exception {
        //JPokemonBoard.getBoard().showMessageDialog("opponent pokemon energy trainer","Information");

        ArrayList<EvolvedPokemonCard> evolvedPokemonCards = getPlayer().getEvolvedPokemonCard();
        for(EvolvedPokemonCard evolvedPokemonCard:evolvedPokemonCards){
            evolvePokemon(evolvedPokemonCard);
        }

        ArrayList<PokemonCard> cards = getPlayer().getBasicPokemonCardsOnHand();
        Collections.shuffle(cards);

        for(int i=0;i<cards.size()&&getPlayer().getBenchPokemonCards().size()<getMaxNumberBenchCards();i++){
            JPokemonBoard.getBoard().showMessageDialog("opponent place a pokemon on bench","opponent!!");
            putACardFromHandToBench(cards.get(i),true);
            JPokemonBoard.getBoard().showMessageDialog("bench size = "+getPlayer().getBenchPokemonCards().size(),"opponent!!");
        }

        //put energy on pokemon on board
        ArrayList<EnergyCard> energies = getPlayer().getEnergyCardsOnHand();
        if(energies.size()>0){
            EnergyCard e;
            ArrayList<PokemonCard> pokemonOnBoard = new ArrayList();
            pokemonOnBoard.add(getPlayer().getActivePokemonCard());
            pokemonOnBoard.addAll(getPlayer().getBenchPokemonCards());
            for(int i=0;i<pokemonOnBoard.size();++i){
                e = findPokemonEnergy(energies,pokemonOnBoard.get(i));
                if(e!=null){
                    String pokemonNo = i==0?"active":""+i;
                    JPokemonBoard.getBoard().showMessageDialog("opponent put "+e.getName()+ " on "
                                    +pokemonOnBoard.get(i).getName()+" "+pokemonNo,"add Energy");
                    getPlayer().addEnergyCardOnHandToPokemon(pokemonOnBoard.get(i),e);
                    JPokemonBoard.getBoard().setEnergyCardOnPokemon(e, pokemonOnBoard.get(i), true);
                    break;
                }
            }
        }

        ArrayList<TrainerCard> trainerCardsList = new ArrayList<TrainerCard>();
        for(Card card:getPlayer().getPlayerHandCards()){
            if(card.getCardType().equals(CardType.TRAINER)){
                trainerCardsList.add((TrainerCard)card);
            }
        }
        if(trainerCardsList.size()>0) {
            Collections.shuffle(trainerCardsList);
            controller.useTrainerCard(trainerCardsList.get(0), true);
        }

        //attack
        Set<PokemonStatus> status = getPlayer().getActivePokemonCard().getStatus();
        if(!status.contains(PokemonStatus.PARALYZED)&&
                !status.contains(PokemonStatus.STUCK)&&
                !status.contains(PokemonStatus.ASLEEP)) {
            controller.attack(getPlayer().getActivePokemonCard(), true);
        }
        //status effect
        takeActivePokemonStatusEffect();
        //check damage pokemon that dead or not
        controller.lookupKilledPokemonAndGetReward(getDamagePokemonListFromActiveBench());
        getPlayer().changeNewToOldPokemon();


        //isOpponent = false mean player
        controller.doTrigger(false, AddAbility.Trigger.OPPONENT_TURN_END);
    }

    public int chooseActivePokemonAbility() throws Exception {
        PokemonCard activePokemon = getPlayer().getActivePokemonCard();
        ArrayList<PokemonAbility> pokemonAbilities = activePokemon.getAbilities();
        ArrayList<EnergyType> pokemonEnergyTypes = new ArrayList();
        for(EnergyCard type:getPlayer().getActivePokemonCard().getEnergies())
            pokemonEnergyTypes.add(type.getEnergyType());
        for(int i=pokemonAbilities.size()-1;i>=0;--i){
            if(pokemonAbilities.get(i).isAbleToAttack(getPlayer().getActivePokemonCard().getEnergies())){
                Ability ability = pokemonAbilities.get(i).getAbility();
                Set<AbilityType> abilityTypeSet = ability.getTypeSet();

                if(ability.getType().equals(AbilityType.COND)){
                    ConditionAbility conditionAbility = (ConditionAbility)ability;
                    abilityTypeSet.add(conditionAbility.getSubAbilityHead().getType());
                    abilityTypeSet.add(conditionAbility.getSubAbilityTail().getType());
                }
                if(abilityTypeSet.contains(AbilityType.DAM)) {
                    return i;
                }else if(abilityTypeSet.contains(AbilityType.HEAL)){
                    if(isHealYourActive(ability)){
                        if(activePokemon.getDamage()>0){
                            return i;
                        }
                    }
                } else if(abilityTypeSet.contains(AbilityType.SEARCH)){
                    Filter filter = getSearchFilter(ability);
                    if(getSearchFilter(ability)!=null){
                        if(filter.getFilterType().equals(Filter.FilterType.ITEM)){
                            if(ability.getType().equals(AbilityType.SEARCH)) {
                                SearchAbility searchAbility = (SearchAbility) ability;
                                if (searchAbility.getSource().equals(BoardPosition.TRASH)) {
                                    if (isItemCardOnPile(BoardPosition.TRASH)) {
                                        return i;
                                    }
                                }
                            }
                        }
                    }
                }else if(abilityTypeSet.contains(AbilityType.APPLYSTAT)){
                    return i;
                }
            }
        }
        return JOptionPane.CLOSED_OPTION;
    }

    public Filter getSearchFilter(Ability ability){
        if(ability.getType().equals(AbilityType.SEARCH)&&ability.getTarget().equals(AbilityTarget.YOU)){
            SearchAbility searchAbility = (SearchAbility)ability;
            return searchAbility.getSourceFilter();
        }
        if(ability.getType().equals(AbilityType.COND)){
            ConditionAbility conditionAbility = (ConditionAbility)ability;
            if(conditionAbility.getSubAbilityHead()!=null){
                return getSearchFilter(((ConditionAbility) ability).getSubAbilityHead());
            }
        }
        if(ability.getSubAbility()!=null){
            return getSearchFilter(ability.getSubAbility());
        }else{
            return null;
        }
    }

    public boolean isHealYourActive(Ability ability){
        if(ability.getType().equals(AbilityType.HEAL)&&ability.getTarget().equals(AbilityTarget.YOUR_ACTIVE)){
            return true;
        }
        if(ability.getType().equals(AbilityType.COND)){
            ConditionAbility conditionAbility = (ConditionAbility)ability;
            if(conditionAbility.getSubAbilityHead()!=null){
                return isHealYourActive(((ConditionAbility) ability).getSubAbilityHead());
            }
        }else if(ability.getSubAbility()!=null){
            return isHealYourActive(ability.getSubAbility());
        }
        return false;
    }

    public EnergyCard findPokemonEnergy(ArrayList<EnergyCard> energyOnHand, PokemonCard pokemonCard){
        ArrayList<EnergyType> energyCardTypesOnPokemon = pokemonCard.getPokemonEnergyTypes();
        ArrayList<PokemonAbility> abilities = pokemonCard.getAbilities();
        //find energies that put on pokemon and energies of pokemon abilities.. which energy they need.
        for(int i=0;i<abilities.size();++i){
            ArrayList<EnergyType> abilityEnergyTypes = new ArrayList(abilities.get(i).getEnergyAmount());
            //to find an energy to put
            if(abilityEnergyTypes.size()>energyCardTypesOnPokemon.size()){
                for(int j=0;energyCardTypesOnPokemon.size()>0&&j<energyCardTypesOnPokemon.size();++j){
                    for(int k=0;k<energyCardTypesOnPokemon.size();++k)
                    if(abilityEnergyTypes.size()>0&&
                            abilityEnergyTypes.get(0).equals(energyCardTypesOnPokemon.get(k))){
                        abilityEnergyTypes.remove(0);
                        break;
                    }
                }
                for(int j=0;j<abilityEnergyTypes.size();++j){
                    for(int k=0;k<energyOnHand.size();++k){
                        if(abilityEnergyTypes.get(j)==energyOnHand.get(k).getEnergyType()
                                ||(abilityEnergyTypes.size()>0&&
                                abilityEnergyTypes.get(abilityEnergyTypes.size()-1).equals(EnergyType.COLORLESS))){
                            return energyOnHand.get(k);
                        }
                    }
                }
            }
        }
        return null;
    }

    public int chooseRewardCardNumber(){
        if(getPlayer().getRewardCards().size()<1)return -1;
        ArrayList<Integer> random = new ArrayList<Integer>();
        for(int i=0;i<getPlayer().getRewardCards().size();i++){
            random.add(i);
        }
        Collections.shuffle(random);
        return random.get(0);
    }

    /**
     * attack (for human player) and heal (for opponent player)
     * @param playerController
     * @return
     */
    public PokemonCard choosePokemonActiveBench(PlayerController playerController,Ability ability){
        if(ability.getType().equals(AbilityType.REENERGIZE)){
            return getPokemonListWithEnergies().get(0);
        }else{
        //if(ability.getType().equals(AbilityType.DAM)||
        //        ability.getType().equals(AbilityType.HEAL)) {
            ArrayList<PokemonCard> pokemonCards = playerController.getDamagePokemonListFromActiveBench();
            if (pokemonCards.size() == 0) {
                return playerController.getPlayer().getActivePokemonCard();
            }
            PokemonCard c = pokemonCards.get(0);
            int max_dam = c.getDamage();

            for (PokemonCard pokemonCard :
                    pokemonCards) {
                if (pokemonCard.getDamage() > max_dam) {
                    max_dam = pokemonCard.getDamage();
                    c = pokemonCard;
                }
            }
            return c;
        }
    }

    /**
     * attack (for humanplayer) and heal (for opponentplayer)
     * @param playerController
     * @return
     */
    public PokemonCard choosePokemonBench(PlayerController playerController, Ability ability) {
        ArrayList<PokemonCard> benchPokemonCards = playerController.getPlayer().getBenchPokemonCards();
        if (benchPokemonCards.size() == 0) {
            return null;
        }
        PokemonCard c = benchPokemonCards.get(0);
        int max_dam = c.getDamage();

        for (PokemonCard pokemonCard :
                benchPokemonCards) {
            if (pokemonCard.getDamage() > max_dam) {
                max_dam = pokemonCard.getDamage();
                c = pokemonCard;
            }
        }
        return c;
    }

    public PokemonCard getChoosePokemonCardFromAbility(Ability ability, PlayerController playerController) throws Exception{
        PokemonCard pokemonCard;
        AbilityTarget target = ability.getTarget();
        if(ability.getType().equals(AbilityType.SWAP)){
            return null;
        }
        switch(target){
            case CHOICE_YOUR://choose pokemon that has highest damage. if not, choose the active pokemon
                pokemonCard=this.choosePokemonActiveBench(this, ability);
                if(pokemonCard==null){
                    pokemonCard = this.getPokemonFromActiveBenchOption(0);
                }
                break;
            case CHOICE_OPPONENT://choose pokemon that has highest damage. if not, choose the active pokemon
                pokemonCard=this.choosePokemonActiveBench(playerController,ability);
                if(pokemonCard==null){
                    pokemonCard = playerController.getPokemonFromActiveBenchOption(0);
                }
                break;
            case CHOICE_YOUR_BENCH:
                pokemonCard=this.choosePokemonBench(this,ability);
                break;
            case CHOICE_OPPONENT_BENCH:
                pokemonCard=this.choosePokemonBench(playerController, ability);
                break;
            default:
                throw new Exception("this is not opponent choice ("+target+")");
        }
        return pokemonCard;
    }

    public EnergyCard getChooseEnergyCardFromPokemon(PokemonCard pokemonCard){
        return pokemonCard.getEnergies().get(0);
    }


    /**
     *
     * @param sourcePokemonCard
     * @param isOpponentTarget true:opponent has target to himself, false:the opponent has target to the other player
     * @param playerController
     * @return
     */
    public PokemonCard getChoosePokemonFromActiveBenchWithoutSourcePokemon(PokemonCard sourcePokemonCard, boolean isOpponentTarget, PlayerController playerController){
        ArrayList<PokemonCard> pokemonCardArrayList = new ArrayList(getPlayer().getBenchPokemonCards());
        pokemonCardArrayList.add(sourcePokemonCard);
        pokemonCardArrayList.remove(sourcePokemonCard);
        int index = (int)(Math.random()*pokemonCardArrayList.size());
        return pokemonCardArrayList.get(index);
    }

    public int getChooseDamageFromPokemon(PokemonCard sourcePokemonCard){
        int damage = sourcePokemonCard.getDamage();
        return (int)(Math.random()*(damage/10))*10;
    }
}
