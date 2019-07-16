package controller;

import model.*;
import model.ability.*;
import model.ability.ConditionAbility;
import model.ability.extra.AbilityConditionAbility;
import model.ability.extra.Calculation;
import model.ability.extra.CoinToss;
import model.ability.extra.CountConditionAbility;
import model.ability.filter.Filter;
import model.ability.filter.PokemonFilter;
import model.ability.filter.TopFilter;
import view.BoardPosition;
import view.JPokemonBoard;

import javax.swing.*;
import java.util.*;

/**
 * Created by nutph on 6/2/2017.
 */
public class AbilityController {
    private JPokemonBoardController controller;
    private int out;//for wally card

    public AbilityController(JPokemonBoardController controller){
        this.controller=controller;
    }

    /**
     * take effect then add all cards that have effect
     * 1. choose target cards that have effect from the ability first
     * 2. take the ability affect from take effect method
     *      true: if the ability effect is working
     *      false: not working
     * 3. do sub ability recursively
     * @param ability ability from pokemon or trainer ability
     * @param isOpponent true: opponent use ability, false: player use ability
     * @param lastCard if target is LAST, use the one before, it also use for card effect as add ability.
     * @return affected cards
     */
    public Set<Card> useAbility(Ability ability, boolean isOpponent, Set<Card> lastCard) throws Exception{

        lastCard = doConditionAbility(ability, isOpponent, lastCard);

        if(ability.getTarget().equals(AbilityTarget.YOU)||ability.getTarget().equals(AbilityTarget.OPPONENT)){
            //not choose any cards from active/bench but hand and trash instead
            boolean isOpponentTarget = ability.getTarget().equals(AbilityTarget.OPPONENT);
            if(!takeAbilityEffect(ability, isOpponent, isOpponentTarget)){
                return null;
            }
        }else{
            if(!takeAbilityEffect(ability, lastCard, isOpponent)){
                return null;
            }
        }

        if(ability.getType().equals(AbilityType.ADD)){
            //it will take sub-ability effect only end of opponent turn
            return lastCard;
        }

        if(ability.getSubAbility()!=null) {
            if(ability.getSubAbility().getTarget().equals(AbilityTarget.LAST)){
                lastCard.addAll(useAbility(ability.getSubAbility(), isOpponent, lastCard));
            }else{
                lastCard.addAll(useAbility(ability.getSubAbility(), isOpponent,null));
            }
        }
        return lastCard;
    }



    private Set<Card> doConditionAbility(Ability ability, boolean isOpponent, Set<Card> lastCard) throws Exception{
        HumanPlayerController playerController = controller.getPlayerController();
        ComputerPlayerController opponentController = controller.getOpponentController();
        Set<Card> cards = new HashSet<>();
        if(ability.getType().equals(AbilityType.NULL)){
            return cards;
        }
        PokemonCard pokemonCard;
        switch(ability.getTarget()){
            case SELF:
            case LAST:
                cards.addAll(lastCard);
                break;
            case OPPONENT:
            case YOU:
                break;
            case YOUR_ACTIVE:
                //opponent active pokemon
                if (isOpponent) {
                    pokemonCard = opponentController.getPlayer().getActivePokemonCard();
                }
                //player active pokemon
                else {
                    pokemonCard = playerController.getPlayer().getActivePokemonCard();
                }
                cards.add(pokemonCard);
                break;
            case OPPONENT_ACTIVE:
                //player active pokemon
                if (isOpponent) {
                    pokemonCard = playerController.getPlayer().getActivePokemonCard();
                }
                //opponent active pokemon
                else {
                    pokemonCard = opponentController.getPlayer().getActivePokemonCard();
                }
                cards.add(pokemonCard);
                break;
            case CHOICE_OPPONENT:
            case CHOICE_YOUR:
            case CHOICE_OPPONENT_BENCH:
            case CHOICE_YOUR_BENCH:
                if(ability.getType().equals(AbilityType.ADD)){
                    //add trainer card to set as the first card
                    cards.addAll(lastCard);
                }
                if(pickPokemonFromBoard(ability,cards,isOpponent)==null){
                    return null;
                }
                break;
            case YOUR_BENCH:
                if (isOpponent) {
                    cards.addAll(opponentController.getPlayer().getBenchPokemonCards());
                }else{
                    cards.addAll(playerController.getPlayer().getBenchPokemonCards());
                }
        }
        return cards;
    }

    public PokemonCard pickPokemonFromBoard(PokemonCard sourcePokemonCard, boolean isOpponent, boolean isOpponentTarget){
        HumanPlayerController playerController = controller.getPlayerController();
        ComputerPlayerController opponentController = controller.getOpponentController();
        PokemonCard pokemonCard;

        if (isOpponent) {
            pokemonCard = opponentController.getChoosePokemonFromActiveBenchWithoutSourcePokemon(sourcePokemonCard,isOpponentTarget,playerController);
        } else {
            //for player
            //isOpponentTarget true:player has target to opponent, false
            String[] pokemonCardsList = playerController.getPokemonOptionFromActiveBenchWithoutSourcePokemon(sourcePokemonCard,isOpponentTarget,opponentController);
            if (pokemonCardsList.length < 1) {
                //unable to choose
                return null;
            }
            out = controller.showOptionDialog("choose Pokemon to get the effect", "Choose destination pokemon",
                    JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, pokemonCardsList, null);
            if (out != JOptionPane.CLOSED_OPTION) {
                //use playerController but opponentController to test heal
                pokemonCard = playerController.getPokemonFromActiveBenchWithoutSourcePokemonOption(sourcePokemonCard,out,isOpponentTarget,opponentController);
            } else {
                return null;
            }
        }
        return pokemonCard;
    }


    /**
     *
     * @param ability
     * @param cards
     * @param isOpponent
     * @return
     * @throws Exception
     */
    public PokemonCard pickPokemonFromBoard(Ability ability,Set<Card> cards,boolean isOpponent) throws Exception{
        HumanPlayerController playerController = controller.getPlayerController();
        ComputerPlayerController opponentController = controller.getOpponentController();
        PokemonCard pokemonCard = null;
        if (isOpponent) {
            pokemonCard = opponentController.getChoosePokemonCardFromAbility(ability, playerController);
            if(pokemonCard==null){
                return null;
            }
            cards.add(pokemonCard);
        } else {
            //for player
            String[] pokemonCardsList = playerController.getPokemonOptionFromAbility(ability, opponentController);
            if (pokemonCardsList.length < 1) {
                //unable to choose
                return null;
            }
            out = controller.showOptionDialog("choose Pokemon to " + ability.getType(), ability.getType().name(),
                    JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, pokemonCardsList, null);
            if (out != JOptionPane.CLOSED_OPTION) {
                //use playerController but opponentController to test heal
                pokemonCard = playerController.getPokemonCardFromOptionAbilityTarget(ability, out, opponentController);
                cards.add(pokemonCard);
            } else {
                return null;
            }
        }
        return pokemonCard;
    }



    public int pickDamageListFromPokemonOnBoard(Ability ability, PokemonCard pokemonCard, boolean isOpponent) throws Exception{
        HumanPlayerController playerController = controller.getPlayerController();
        ComputerPlayerController opponentController = controller.getOpponentController();
        int damage = 0;
        if (isOpponent) {
            //redamage
            damage = opponentController.getChooseDamageFromPokemon(pokemonCard);
        } else {
            //for player
            Integer[] energyCardsList = playerController.getDamageArrayFromPokemon(pokemonCard);
            if (energyCardsList.length < 1) {
                //unable to choose
                return -1;
            }
            out = controller.showOptionDialog("choose Pokemon to " + ability.getType(), ability.getType().name(),
                    JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,energyCardsList, null);
            if (out != JOptionPane.CLOSED_OPTION) {
                //use playerController but opponentController to test heal
                damage = playerController.getDamageArrayFromPokemon(pokemonCard)[out];
            } else {
                return -1;
            }
        }
        return damage;
    }

    public EnergyCard pickEnergyCardsListFromPokemonOnBoard(Ability ability,PokemonCard pokemonCard,boolean isOpponent) throws Exception{
        HumanPlayerController playerController = controller.getPlayerController();
        ComputerPlayerController opponentController = controller.getOpponentController();
        EnergyCard energyCard = null;
        if (isOpponent) {
            energyCard = opponentController.getChooseEnergyCardFromPokemon(pokemonCard);
        } else {
            //for player
            String[] energyCardsList = playerController.getEnergyNameArrayFromPokemon(pokemonCard);
            if (energyCardsList.length < 1) {
                //unable to choose
                return null;
            }
            out = controller.showOptionDialog("choose Pokemon to " + ability.getType(), ability.getType().name(),
                    JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,energyCardsList, null);
            if (out != JOptionPane.CLOSED_OPTION) {
                //use playerController but opponentController to test heal
                energyCard = playerController.getEnergyListFromPokemon(pokemonCard).get(out);
            } else {
                return null;
            }
        }
        return energyCard;
    }


    public boolean takeAbilityEffect(Ability ability, boolean isOpponent, boolean isOpponentTarget) throws Exception{
        boolean isEffect = true;
        if(ability.getType().equals(AbilityType.DRAW)){
            DrawAbility drawAbility = ((DrawAbility)ability);
            try {
                if((isOpponent&&!isOpponentTarget) || !isOpponent&&isOpponentTarget){
                    controller.getOpponentController().putNCardFromDeckToHand(drawAbility.getAmount(), false);
                }else{//isOpponent&&isOpponentTarget || !isOpponent&&!isOpponentTarget
                    controller.getPlayerController().putNCardFromDeckToHand(drawAbility.getAmount(),true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(ability.getType().equals(AbilityType.SEARCH)){
            SearchAbility searchAbility = (SearchAbility)ability;
            Stack<Card> pile;
            Card.AbilityAction abilityAction;
            if(searchAbility.getSource().equals(BoardPosition.DECK)){
                abilityAction = Card.AbilityAction.SEARCH_DECK_ACTIVE;
                if((isOpponent&&!isOpponentTarget) || !isOpponent&&isOpponentTarget){
                    pile = controller.getOpponentController().getPlayer().getDeck();
                }else{//isOpponent&&isOpponentTarget || !isOpponent&&!isOpponentTarget
                    pile = controller.getPlayerController().getPlayer().getDeck();
                }
            }else if(searchAbility.getSource().equals(BoardPosition.TRASH)){
                abilityAction = Card.AbilityAction.SEARCH_TRASH_ACTIVE;
                if(isOpponent){
                    pile = controller.getOpponentController().getPlayer().getTrash();
                }else{
                    pile = controller.getPlayerController().getPlayer().getTrash();
                }
            }else{
                throw new Exception("this is only for deck/trash board position");
            }
            if(searchAbility.getSourceFilter()!=null&&searchAbility.getSourceFilter().getFilterType().equals(Filter.FilterType.TOP)){
                TopFilter topFilter = (TopFilter)searchAbility.getSourceFilter();
                Stack<Card> topCards = new Stack<>();
                for(int j=0;j<topFilter.getShowedNum();j++){
                    Card card = pile.pop();
                    topCards.add(card);
                    JPokemonBoard.getBoard().getJLabelBoard().remove(card);
                }
                boolean isOpponent1 = isOpponent^isOpponentTarget;//XOR
                JPokemonBoard.getBoard().showPileInformation(topCards,"show top card",BoardPosition.DECK,isOpponent1);
                Iterator<Card> cardIterator = topCards.iterator();
                while(cardIterator.hasNext()){
                    Card c = cardIterator.next();
                    JPokemonBoard.getBoard().getJLabelBoard().add(c);
                    pile.push(c);
                    cardIterator.remove();
                }
            }else {
                for (int i = 0; i < searchAbility.getPickedAmount(); i++) {
                    ArrayList<Card> filteredList = new ArrayList();
                    if (searchAbility.getSourceFilter() != null) {
                        if (searchAbility.getSourceFilter().getFilterType().equals(Filter.FilterType.ENERGY)) {
                            for (Card card : pile) {
                                if (card.getCardType().equals(CardType.ENERGY)) {
                                    filteredList.add(card);
                                    JPokemonBoard.getBoard().getJLabelBoard().remove(card);
                                }
                            }
                        } else if (searchAbility.getSourceFilter().getFilterType().equals(Filter.FilterType.ITEM)) {
                            for (Card card : pile) {
                                if (card.getCardType().equals(CardType.TRAINER) &&
                                        ((TrainerCard) card).getTrainerType().equals(TrainerType.ITEM)) {
                                    filteredList.add(card);
                                    JPokemonBoard.getBoard().getJLabelBoard().remove(card);
                                }
                            }
                        } else if (searchAbility.getSourceFilter().getFilterType().equals(Filter.FilterType.POKEMON)) {
                            PokemonFilter pokemonFilter = (PokemonFilter)searchAbility.getSourceFilter();
                            if(pokemonFilter.getPokemonType().equals(PokemonFilter.PokemonType.ANY)) {
                                for (Card card : pile) {
                                    if (card.getCardType().equals(CardType.POKEMON)) {
                                        filteredList.add(card);
                                        JPokemonBoard.getBoard().getJLabelBoard().remove(card);
                                    }
                                }
                            } else if(pokemonFilter.getPokemonType().equals(PokemonFilter.PokemonType.BASIC)){
                                for (Card card : pile) {
                                    if (card.getCardType().equals(CardType.POKEMON)&&
                                            !(card instanceof EvolvedPokemonCard)) {
                                        filteredList.add(card);
                                        JPokemonBoard.getBoard().getJLabelBoard().remove(card);
                                    }
                                }
                            } else{
                                throw new Exception("we have only ANY, TRAINER, ENERGY, ANY POKEMON, AND BASIC POKEMON FILTER now");
                            }
                        }
                    }else{
                        //ability wish
                        for (Card card : pile) {
                            filteredList.add(card);
                            JPokemonBoard.getBoard().getJLabelBoard().remove(card);
                        }
                    }
                    if (!isOpponent) {
                        for (Card card : filteredList) {
                            card.setVisibleCard(true);
                            card.setAbilityAction(abilityAction);
                        }
                        String text ="";
                        if(searchAbility.getSourceFilter()!=null){
                            text = searchAbility.getSourceFilter().getFilterType().toString();
                        }
                        JPokemonBoard.getBoard().showCardListInformation(filteredList, "Search " + text, "Search Ability !!", isOpponent);
                        for (Card card : pile) {
                            if (searchAbility.getSource().equals(BoardPosition.DECK)) {
                                card.setVisibleCard(false);
                            }
                            card.setAbilityAction(Card.AbilityAction.NON_ACTIVE);
                        }
                    } else {
                        //do ai
                        Card c = filteredList.get((int)(Math.random()*filteredList.size()));
                        controller.moveCardToHand(c,true);
                    }
                    filteredList.clear();
                }
            }
            //do here
        }else if(ability.getType().equals(AbilityType.DECK)){
            DeckAbility deckAbility = (DeckAbility)ability;
            int amount = 0;
            if(deckAbility.getCalculation() !=null){
                if(isOpponent) {
                    if(isOpponentTarget) {
                        amount = deckAbility.getCalculation().calculateAmount(controller, deckAbility.getAmount(), !isOpponent);
                    }else{
                        amount = deckAbility.getCalculation().calculateAmount(controller, deckAbility.getAmount(), isOpponent);
                    }
                }else{
                    if(isOpponentTarget) {
                        amount = deckAbility.getCalculation().calculateAmount(controller, deckAbility.getAmount(), isOpponent);
                    }else{
                        amount = deckAbility.getCalculation().calculateAmount(controller, deckAbility.getAmount(), !isOpponent);
                    }
                }
            }
            ArrayList<Card> hand = new ArrayList();
            if(deckAbility.getCalculation().getTarget().equals(Calculation.CountTarget.OPPONENT_HAND)){
                if(isOpponent){
                    hand = new ArrayList(controller.getPlayerController().getPlayer().getPlayerHandCards());
                }else{
                    hand = new ArrayList(controller.getOpponentController().getPlayer().getPlayerHandCards());
                }
                controller.moveCardFromHandToDest(hand,deckAbility.getDestination(),!isOpponent);
            }else if(deckAbility.getCalculation().getTarget().equals(Calculation.CountTarget.YOUR_HAND)){
                if(isOpponent){
                    hand = new ArrayList(controller.getOpponentController().getPlayer().getPlayerHandCards());
                }else{
                    hand = new ArrayList(controller.getPlayerController().getPlayer().getPlayerHandCards());
                }
                controller.moveCardFromHandToDest(hand,deckAbility.getDestination(),isOpponent);
            }else if(deckAbility.getCalculation().getTarget().equals(Calculation.CountTarget.NULL)){
                if((!isOpponentTarget&&isOpponent)||(isOpponentTarget&&!isOpponent)){
                    //move opponent hand cards
                    for(int i =0;i<amount;i++) {
                        Card c = controller.getOpponentController().getCardOnHandRandomly();
                        if (c != null) {
                            hand.add(c);
                        }
                    }
                    controller.moveCardFromHandToDest(hand,deckAbility.getDestination(),true);
                }else{
                    //move player hand cards
                    Card.AbilityAction abilityAction = null;
                    if(deckAbility.getDestination().equals(DeckAbility.Destination.BOTTOM_DECK)) {
                         abilityAction= Card.AbilityAction.BOTTOM_DECK;
                    }else if(deckAbility.getDestination().equals(DeckAbility.Destination.TOP_DECK)) {
                        abilityAction = Card.AbilityAction.TOP_DECK;
                    }else if(deckAbility.getDestination().equals(DeckAbility.Destination.DISCARD)) {
                        abilityAction = Card.AbilityAction.DISCARD;
                    }
                    hand = controller.getPlayerController().getPlayer().getPlayerHandCards();
                    for (Card c: hand) {
                        c.setAbilityAction(abilityAction);
                    }
                    if(deckAbility.getChoice().equals(DeckAbility.Choice.YOU_CHOOSE)) {
                        if(!isOpponent) {
                            JPokemonBoard.getBoard().showCardListInformation(hand, "Choose card", "Deck Ability", isOpponent);
                            for (Card c : hand) {
                                c.setAbilityAction(Card.AbilityAction.NON_ACTIVE);
                            }
                        }else{
                            //opponent
                            return false;
                        }
                    }else if(deckAbility.getChoice().equals(DeckAbility.Choice.OPPONENT_CHOOSE)){
                        if(isOpponent){
                            JPokemonBoard.getBoard().showCardListInformation(hand, "Choose card", "Deck Ability", !isOpponent);
                            for (Card c : hand) {
                                c.setAbilityAction(Card.AbilityAction.NON_ACTIVE);
                            }
                        }else {
                            for (int i = 0; i < amount; i++) {
                                Card c = controller.getOpponentController().getCardOnHandRandomly();
                                if (c != null) {
                                    hand.add(c);
                                }
                            }
                        }
                        controller.moveCardFromHandToDest(hand,deckAbility.getDestination(),isOpponent);
                    }else if(deckAbility.getChoice().equals(DeckAbility.Choice.RANDOM)){

                    }
                }
            }else{
                throw new Exception("deck ability calculation target error");
            }
        }else if(ability.getType().equals(AbilityType.SHUFFLE)){
            if(isOpponent^isOpponentTarget){
                controller.getOpponentController().getPlayer().shuffleDeck();
            }else{
                controller.getPlayerController().getPlayer().shuffleDeck();
            }
        }
        return isEffect;
    }


    /**
     *
     * @param targetCards
     * @return
     */
    public boolean takeAbilityEffect(Ability ability,Set<Card> targetCards, boolean isOpponent) throws Exception{
        boolean isEffect = true;
        if(ability.getType().equals(AbilityType.DAM)){
            DamageAbility damageAbility = (DamageAbility)ability;
            if(targetCards==null){
                return false;
            }
            ArrayList<PokemonCard> targetPokemonCards= new ArrayList(targetCards);
            int calAmount = 0;
            if(damageAbility.getCalculation()!=null){
                Calculation cal = damageAbility.getCalculation();
                calAmount = cal.calculateAmount(controller,damageAbility.getAmount(),isOpponent);
            }
            for(PokemonCard targetPokemonCard:targetPokemonCards) {
                int damage = targetPokemonCard.getDamage();
                if(damageAbility.getCalculation()!=null) {
                    damage += calAmount;
                }else{
                    damage += damageAbility.getAmount();
                }
                targetPokemonCard.setDamage(damage);
                targetPokemonCard.setDamageStatusText();
            }
        }else if(ability.getType().equals(AbilityType.HEAL)){
            HealAbility healAbility = (HealAbility)ability;
            if(targetCards==null){
                return false;
            }
            ArrayList<PokemonCard> targetPokemonCards= new ArrayList(targetCards);
            for(PokemonCard targetPokemonCard:targetPokemonCards) {
                int damage = targetPokemonCard.getDamage();
                damage -= healAbility.getAmount();
                damage = damage<0?0:damage;
                targetPokemonCard.setHealed(true);
                targetPokemonCard.setDamage(damage);
                targetPokemonCard.setDamageStatusText();
            }
        }else if(ability.getType().equals(AbilityType.DEENERGIZE)){
            DeenergyAbility deenergyAbility = ((DeenergyAbility)ability);
            if(targetCards==null){
                return false;
            }
            ArrayList<PokemonCard> targetPokemonCards= new ArrayList(targetCards);
            for(PokemonCard targetPokemonCard:targetPokemonCards) {
                int calAmount;
                if(deenergyAbility.getCalculation()!=null){
                    Calculation cal = deenergyAbility.getCalculation();
                    calAmount = cal.calculateAmount(controller,deenergyAbility.getAmount(),isOpponent);
                    deenergyAbility.setAmount(calAmount);
                }

                if(targetPokemonCard.getEnergies().size()<deenergyAbility.getAmount()){
                    //remove all
                    isEffect = removeEnergyCard(targetPokemonCard, targetPokemonCard.getEnergies().size(), isOpponent);
                }else{
                    isEffect = removeEnergyCard(targetPokemonCard, deenergyAbility.getAmount(), isOpponent);
                }
            }
        }else if(ability.getType().equals(AbilityType.REENERGIZE)){
            ReenergyAbility reenergyAbility = ((ReenergyAbility)ability);
            boolean isOpponentTarget = true;
            if(reenergyAbility.getDestinationTarget().equals(AbilityTarget.CHOICE_YOUR)){
                isOpponentTarget = false;
                Iterator<Card> sourceCardIterator =  targetCards.iterator();
                PokemonCard sourcePokemonCard =null;
                if(sourceCardIterator.hasNext()){
                    Card card = sourceCardIterator.next();
                    if(card.getCardType().equals(CardType.POKEMON))
                        sourcePokemonCard = (PokemonCard)card;
                }
                PokemonCard destinationPokemonCard = pickPokemonFromBoard(sourcePokemonCard, isOpponent, isOpponentTarget);
                EnergyCard energyCard = pickEnergyCardsListFromPokemonOnBoard(ability,sourcePokemonCard,isOpponent);
                sourcePokemonCard.getEnergies().remove(energyCard);
                destinationPokemonCard.getEnergies().add(energyCard);
                JPokemonBoard.getBoard().setEnergyCardOnPokemon(energyCard,destinationPokemonCard,isOpponent);
                targetCards.add(destinationPokemonCard);
            }else{
                throw new Exception("only 'choice:your' ability destination target");
            }
        }else if(ability.getType().equals(AbilityType.REDAMAGE)){
            RedamangeAbility redamangeAbility = ((RedamangeAbility)ability);
            boolean isOpponentTarget = false;
            if(redamangeAbility.getDestinationTarget().equals(AbilityTarget.CHOICE_OPPONENT)){
                isOpponentTarget = true;
                Iterator<Card> sourceCardIterator =  targetCards.iterator();
                PokemonCard sourcePokemonCard =null;
                if(sourceCardIterator.hasNext()){
                    Card card = sourceCardIterator.next();
                    if(card.getCardType().equals(CardType.POKEMON))
                        sourcePokemonCard = (PokemonCard)card;
                }
                PokemonCard destinationPokemonCard = pickPokemonFromBoard(sourcePokemonCard, isOpponent,isOpponentTarget);
                int damage = pickDamageListFromPokemonOnBoard(ability,sourcePokemonCard,isOpponent);
                sourcePokemonCard.setDamage(sourcePokemonCard.getDamage()-damage);
                sourcePokemonCard.setDamageStatusText();

                destinationPokemonCard.setDamage(destinationPokemonCard.getDamage()+damage);
                destinationPokemonCard.setDamageStatusText();

                targetCards.add(destinationPokemonCard);
            }else{
                throw new Exception("only 'choice:opponent' as ability destination target");
            }
        }else if(ability.getType().equals(AbilityType.SWAP)){
            if(targetCards==null){
                return false;
            }
            Card card = targetCards.iterator().next();
            if(card instanceof PokemonCard){
                PokemonCard swapPokemonCard = (PokemonCard)card;
                Player player;
                PokemonCard active;
                if(isOpponent){
                    player = controller.getOpponentController().getPlayer();
                }else{
                    player = controller.getPlayerController().getPlayer();
                }
                active = player.getActivePokemonCard();

                try {
                    player.getBenchPokemonCards().remove(swapPokemonCard);
                    player.getBenchPokemonCards().add(active);
                    active.getStatus().clear();
                    player.setActivePokemonCard(swapPokemonCard);
                    JPokemonBoard.getBoard().movePokemonOnGroupPositionBoard(BoardPosition.BENCH_POKEMON,active
                            ,player.getBenchPokemonCards(),isOpponent,true);
                    JPokemonBoard.getBoard().movePokemonOnOnePositionBoard(BoardPosition.ACTIVE_POKEMON,swapPokemonCard
                            ,isOpponent);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }else if(ability.getType().equals(AbilityType.DESTAT)){
            DestatusAbility destatusAbility = ((DestatusAbility)ability);
            if(targetCards==null){
                return false;
            }
            ArrayList<PokemonCard> targetPokemonCards= new ArrayList(targetCards);
            for(PokemonCard targetPokemonCard:targetPokemonCards) {
                targetPokemonCard.getStatus().clear();
                targetPokemonCard.setDamageStatusText();
            }
        }else if(ability.getType().equals(AbilityType.APPLYSTAT)){
            ApplyStatusAbility applyStatusAbility = ((ApplyStatusAbility)ability);
            if(targetCards==null){
                return false;
            }
            ArrayList<PokemonCard> targetPokemonCards= new ArrayList(targetCards);
            for(PokemonCard targetPokemonCard:targetPokemonCards) {
                targetPokemonCard.getStatus().add(applyStatusAbility.getStatus());
                targetPokemonCard.setDamageStatusText();
            }
        }else if(ability.getType().equals(AbilityType.SEARCH)){
            //for wally card (trainer card)
            SearchAbility searchAbility = (SearchAbility)ability;
            Stack<Card> deck;
            Card.AbilityAction abilityAction;
            if(searchAbility.getSource().equals(BoardPosition.DECK)){
                abilityAction = Card.AbilityAction.SEARCH_DECK_EVOLVE_ACTIVE;
                if(isOpponent){
                    return false;
                    //deck = controller.getOpponentController().getPlayer().getDeck();
                }else{//isOpponent&&isOpponentTarget || !isOpponent&&!isOpponentTarget
                    deck = controller.getPlayerController().getPlayer().getDeck();
                }
            }else{
                throw new Exception("this is only for deck board position (wally)");
            }
            PokemonCard basicPokemon = null;
            Iterator<Card> cardIterator = targetCards.iterator();
            if(cardIterator.hasNext()){
                Card card = cardIterator.next();
                if(card instanceof PokemonCard)
                    basicPokemon = (PokemonCard)card;
            }else{

            }
            for(int i =0;i<searchAbility.getPickedAmount();i++) {
                ArrayList<Card> filteredList = new ArrayList();
                if(searchAbility.getSourceFilter()!=null){
                    if(searchAbility.getSourceFilter().getFilterType().equals(Filter.FilterType.EVOLVES_FROM)){
                        for(Card card:deck){
                            if(card instanceof EvolvedPokemonCard&&card.getDescription().endsWith(basicPokemon.getName())){
                                filteredList.add(card);
                                JPokemonBoard.getBoard().getJLabelBoard().remove(card);
                            }
                        }
                    }
                }

                if(!isOpponent) {
                    for (Card card : filteredList) {
                        card.setVisibleCard(true);
                        card.setAbilityAction(abilityAction);
                    }
                    JPokemonBoard.getBoard().showCardListInformation(filteredList,"Search "+ searchAbility.getSourceFilter().getFilterType(),"Search Ability !!",isOpponent);
                    for (Card card : deck) {
                        if(searchAbility.getSource().equals(BoardPosition.DECK)) {
                            card.setVisibleCard(false);
                        }
                        card.setAbilityAction(Card.AbilityAction.NON_ACTIVE);
                    }
                }else{
                    //do ai
                }
                filteredList.clear();
            }
            //do here
        }else if(ability.getType().equals(AbilityType.COND)){
            ConditionAbility conditionAbility = (ConditionAbility)ability;
            if(conditionAbility.getCondition().equals(ConditionAbility.Condition.FILP)){
                CoinToss coinToss = CoinToss.random();
                controller.showMessageDialog("GOT "+coinToss.name(),"THROW COIN");
                if(coinToss.equals(CoinToss.HEAD)){
                    Ability head =((ConditionAbility) ability).getSubAbilityHead();
                    useAbility(head,isOpponent,targetCards);
                }else{
                    Ability tail =((ConditionAbility) ability).getSubAbilityTail();
                    useAbility(tail,isOpponent,targetCards);
                }
            }else if(conditionAbility.getCondition().equals(ConditionAbility.Condition.HEALED_YOUR_ACTIVE)){
                Iterator<Card> iterCards= targetCards.iterator();
                if(iterCards.hasNext()){
                    Card c = iterCards.next();
                    if(c instanceof PokemonCard){
                        PokemonCard pokemonCard = (PokemonCard)c;
                        if(pokemonCard.isHealed()){
                            Ability head = ((ConditionAbility) ability).getSubAbilityHead();
                            useAbility(head,isOpponent,targetCards);
                        }else{
                            Ability tail = ((ConditionAbility) ability).getSubAbilityTail();
                            if(tail.getType().equals(AbilityType.NULL)){
                                return !isEffect;
                            }
                            useAbility(tail,isOpponent,targetCards);
                        }
                    }
                }
            }else if(conditionAbility.getCondition().equals(ConditionAbility.Condition.ABILITY)){
                AbilityConditionAbility abilityConditionAbility = (AbilityConditionAbility)conditionAbility;
                Ability head  = abilityConditionAbility.getSubAbilityHead();
                Ability tail = abilityConditionAbility.getSubAbilityTail();
                Set<Card> cardSet = useAbility(abilityConditionAbility.getConditionAbility(),isOpponent,targetCards);
                if(cardSet!=null) {
                    useAbility(head,isOpponent,targetCards);
                }else{
                    useAbility(tail,isOpponent,targetCards);
                }
                //do here
            }else if(conditionAbility.getCondition().equals(ConditionAbility.Condition.CHOICE)){
                //do here
                Ability head  = ((ConditionAbility) ability).getSubAbilityHead();
                Ability tail =((ConditionAbility) ability).getSubAbilityTail();
                int out = controller.showConfirmDialog("Do you want to Y:"+ head+", N:"+tail,
                        "CONDITION CHOICE",JOptionPane.OK_CANCEL_OPTION);
                if(out == JOptionPane.OK_OPTION){
                    useAbility(head,isOpponent,targetCards);
                }else{
                    useAbility(tail,isOpponent,targetCards);
                }
            }else{
                //extra condition
                if(conditionAbility instanceof CountConditionAbility){
                    CountConditionAbility countConditionAbility=(CountConditionAbility)conditionAbility;
                    if(countConditionAbility.isGreater(controller,isOpponent)){
                        Ability subAbilityHead = countConditionAbility.getSubAbilityHead();
                        useAbility(subAbilityHead,isOpponent,targetCards);
                    }
                }
            }
        }else if(ability.getType().equals(AbilityType.ADD)) {
            AddAbility addAbility = (AddAbility)ability;
            Iterator<Card>  cardIterator= targetCards.iterator();
            TrainerCard trainerCard = null;
            PokemonCard attachedPokemonCard = null;
            while(cardIterator.hasNext()){
                Card card = cardIterator.next();
                if(card.getCardType().equals(CardType.TRAINER)){
                    trainerCard = (TrainerCard)card;
                }else if(card.getCardType().equals(CardType.POKEMON)){
                    attachedPokemonCard = (PokemonCard)card;
                }
                if(trainerCard!=null&&attachedPokemonCard!=null){
                    break;
                }
            }
            if(trainerCard ==null){
                throw new Exception("add should have trainer card here");
            }
            attachedPokemonCard.getTools().add(trainerCard);
            JPokemonBoard.getBoard().setItemCardOnPokemon(trainerCard,attachedPokemonCard,isOpponent);
        }else {
            //null ability always works for nothing
            //ability.getType().equals(AbilityType.NULL);
        }
        return isEffect;
    }


    public boolean removeEnergyCard(PokemonCard targetPokemon, int amount, boolean isOpponent){
        int energy_choice;
        boolean isCancel = false;
        ArrayList<EnergyCard> removedEnergyCards = new ArrayList<EnergyCard>();
        for (int i = amount; i > 0; --i) {
            ArrayList<EnergyType> activeEnergyTypes = targetPokemon.getPokemonEnergyTypes();
            energy_choice = controller.showOptionDialog("choose " + i + " energies to remove", "Deenergy", JOptionPane.OK_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE, activeEnergyTypes.toArray(), null);
            //when you choose pokemonEnergy
            if (energy_choice != -1) {
                EnergyType type = activeEnergyTypes.get(energy_choice);
                removedEnergyCards.add(targetPokemon.removeEnergyCardFromEnergyType(type));
            } else {
                //when you click cancel on the top right, all will be rolled back
                for (int j = 0; j < removedEnergyCards.size(); ++j) {
                    targetPokemon.getEnergies().add(removedEnergyCards.get(j));
                }
                isCancel = true;
                break;
            }
        }
        if(!isCancel){
            for (int i = 0; i < removedEnergyCards.size(); i++) {
                try {
                    JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.TRASH, removedEnergyCards.get(i), isOpponent, false);
                    if(!isOpponent) {
                        controller.getPlayerController().getPlayer().getTrash().push(removedEnergyCards.get(i));
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    /**
     * for wally card
     * @param evolvedPokemonCard
     */
    public void moveCardOnDeckToEvolve(EvolvedPokemonCard evolvedPokemonCard) throws Exception{
        evolvedPokemonCard.setVisibleCard(true);
        PlayerController playerController = controller.getPlayerController();
        PokemonCard pokemonCard = playerController.getActiveBenchPokemonListToEvolveOption(out);
        out = playerController.getEvolvingPokemonListOnBoard(evolvedPokemonCard).indexOf(pokemonCard);
        playerController.addBasicPokemonCardToStageOnePokemon(evolvedPokemonCard,out,true);
        JPokemonBoard.getBoard().putEvolvedPokemonOnPokemon(evolvedPokemonCard,pokemonCard);
        playerController.getPlayer().getDeck().remove(evolvedPokemonCard);
        JPokemonBoard.getBoard().closeDialog(BoardPosition.DECK,false);
    }

}
