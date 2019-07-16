package controller;

import model.*;
import model.Ability;
import model.AbilityTarget;
import model.AbilityType;
import model.ability.AddAbility;
import model.ability.DeckAbility;
import model.ability.extra.CoinToss;
import view.BoardPosition;
import view.JPokemonBoard;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This controller class used to controller the whole game
 * Created by nutph on 5/7/2017.
 */
public class JPokemonBoardController extends Thread{

    static final String DECK_ONE = "deck/deck2.txt";
    static final String DECK_TWO = "deck/deck1.txt";
    static final String ALL_CARDS = "deck/cards.txt";
    static final String ALL_ABILITIES = "deck/abilities.txt";

    static final int DEFAULT_HAND_CARDS = 7;
    static final int DEFAULT_REWARD_CARDS = 6;
    static final int DEFAULT_MAX_BENCH_CARDS = 5;

    private HumanPlayerController playerController;
    private ComputerPlayerController opponentController;
    private AbilityController abilityController;
    private DeckCreator deckCreator;

    public JPokemonBoardController(){
        abilityController = new AbilityController(this);
    }

    /**
     * For independence injection(testing)
     * @param playerController HumanPlayerController to control state of human
     * @param opponentController ComputerPlayerController to control computer
     */
    public JPokemonBoardController(HumanPlayerController playerController, ComputerPlayerController opponentController){
        this.playerController = playerController;
        this.opponentController = opponentController;
        abilityController = new AbilityController(this);
    }

    /**
     * get HumanPlayerController
     * @return object of HumanPlayerController
     */
    public HumanPlayerController getPlayerController() {
        return playerController;
    }

    public AbilityController getAbilityController() {
        return abilityController;
    }

    public void setAbilityController(AbilityController abilityController) {
        this.abilityController = abilityController;
    }

    public void setPlayerController(HumanPlayerController playerController) {
        this.playerController = playerController;
    }

    /**
     * get ComputerPlayerController
     * @return object of ComputerPlayerController (opponent)
     */
    public ComputerPlayerController getOpponentController() {
        return opponentController;
    }

    /**
     * set ComputerPlayerController
     * @param opponentController object of ComputerPlayerController (opponent)
     */
    public void setOpponentController(ComputerPlayerController opponentController) {
        this.opponentController = opponentController;
    }

    /**
     * set default of the beginning of the game for both player and computer
     * (e.g. import deck, number of cards on hands, rewards, maximum pokemon bench cards)
     * @throws Exception
     */
    public void setUp() throws Exception{
        deckCreator = new DeckCreator(DECK_ONE,DECK_TWO,ALL_CARDS,ALL_ABILITIES);
        setUpCard();
    }


    /**
     *
     * set default of the beginning of the game for both player and computer
     * (e.g. import deck files, number of cards on hands, rewards, maximum pokemon bench cards)
     * @param deck1FileName
     * @param deck2FileName
     * @throws Exception
     */
    public void setUp(String deck1FileName,String deck2FileName) throws Exception {
        deckCreator = new DeckCreator(deck1FileName, deck2FileName, ALL_CARDS, ALL_ABILITIES);
        setUpCard();
    }


    public void setUpCard() throws Exception{
        initializeHumanController();
        initializeComputerController();

        //put card on hand
        playerController.putNCardFromDeckToHand(playerController.getStartGameNumberHandCards(),true);
        opponentController.putNCardFromDeckToHand(opponentController.getStartGameNumberHandCards(),false);
        //still doing this part
        start();
    }



    private void initializeComputerController() throws Exception {
        opponentController = new ComputerPlayerController(DEFAULT_HAND_CARDS,DEFAULT_REWARD_CARDS, DEFAULT_MAX_BENCH_CARDS);
        opponentController.initializeDeckHands();
        //put card on deck
        ArrayList<Card> deckForComputerPlayer = deckCreator.createDeck(true);
        opponentController.setUpDeck(deckForComputerPlayer);
        addCardListener(deckForComputerPlayer);
    }

    private void initializeHumanController() throws Exception {
        playerController = new HumanPlayerController(DEFAULT_HAND_CARDS,DEFAULT_REWARD_CARDS, DEFAULT_MAX_BENCH_CARDS);
        playerController.initializeDeckHands();
        //put cards on deck
        ArrayList<Card> deckForHumanPlayer = deckCreator.createDeck(false);
        playerController.setUpDeck(deckForHumanPlayer);
        addCardListener(deckForHumanPlayer);
    }


    //Mulligan is when player does not have basic pokemon card

    /**
     * at the beginning of the game, both player and computer draw cards into hand.
     * if there is no basic pokemon cards on either player or computer hand, we do mulligan.
     * if both have basic pokemon cards, start the game.
     * @throws Exception throw exception when we use a wrong method (programmer problem)
     */
    public void doSetupAndMulliganPhase() throws Exception {
            while(playerController.isMulligan()&&opponentController.isMulligan()){
                //both do not have basic pokemon card, just return their hand cards and draw again
                opponentController.showAllHandCards();
                showMessageDialog("Both have no basic pokemon, so both take mulligans","Mulligan Phase");
                playerController.takeAMulligan();
                opponentController.takeAMulligan();
                playerController.putNCardFromDeckToHand(playerController.getStartGameNumberHandCards(),true);
                opponentController.putNCardFromDeckToHand(opponentController.getStartGameNumberHandCards(),false);
            }
            if(playerController.isMulligan()){
                //player does not have basic pokemon card on hand
                showMessageDialog("Player has no basic pokemon, take mulligans","Mulligan Phase");
                //
                opponentController.setUpReward();

                opponentController.actionForStartActive();
                opponentController.actionForStartBench();
                //
                do {
                    playerController.takeAMulligan();
                    playerController.putNCardFromDeckToHand(playerController.getStartGameNumberHandCards(), true);
                    showMessageDialog("opponent draw a card for player mulligan","Mulligan Phase");
                    opponentController.putACardFromDeckToHand(false);
                    if(playerController.isMulligan())
                        showMessageDialog("player has no basic pokemon, take mulligans","Mulligan Phase");
                }while(playerController.isMulligan());
                playerController.setUpReward();

                showMessageDialog("choose pokemon as active pokemon","Mulligan Phase");
                playerController.waitState();
                showMessageDialog("choose pokemon as bench pokemon","Mulligan Phase");

                JPokemonBoard.getBoard().setUpDoneBtn(this);
                playerController.waitState();

            }else if(opponentController.isMulligan()){
                showMessageDialog("Opponent has no basic pokemon, so take mulligans","Mulligan Phase");
                playerController.setUpReward();
                showMessageDialog("choose pokemon as active pokemon","Mulligan Phase");
                playerController.waitState();

                showMessageDialog("choose pokemon as bench pokemon","Mulligan Phase");
                JPokemonBoard.getBoard().setUpDoneBtn(this);
                playerController.waitState();

                do {
                    showMessageDialog("opponent show hand cards for mulligans","Mulligan Phase");
                    opponentController.showAllHandCards();

                    showMessageDialog("opponent take mulligans","Mulligan Phase");
                    opponentController.takeAMulligan();
                    opponentController.putNCardFromDeckToHand(opponentController.getStartGameNumberHandCards(),false);
                    boolean isDraw=JOptionPane.showConfirmDialog(JPokemonBoard.getBoard().getJLabelBoard(),
                            "do you want to draw a card for opponent mulligan?")==JOptionPane.YES_OPTION;
                    if(isDraw)
                        playerController.putACardFromDeckToHand(true);

                    if(opponentController.isMulligan())
                        showMessageDialog("opponent has no basic pokemon, so take mulligans again","Mulligan Phase");
                    else{
                        opponentController.actionForStartActive();
                        opponentController.actionForStartBench();
                        break;
                    }
                }while(opponentController.isMulligan());
                opponentController.setUpReward();
                opponentController.showCardOnActiveAndBench();
            }else{
                //if both have basic pokemon card, then set up reward cards on both side
                playerController.setUpReward();
                opponentController.setUpReward();
            }
    }


    /**
     * This thread is the main flow of the game.
     */
    public void run(){
        //need to change a lot from here
        try {
            this.doSetupAndMulliganPhase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        showMessageDialog("choose pokemon as active pokemon","Start Phase");
        playerController.waitState();

        opponentController.actionForStartActive();

        showMessageDialog("choose pokemon as bench pokemon","Start Phase");

        JPokemonBoard.getBoard().setUpDoneBtn(this);
        playerController.waitState();

        opponentController.actionForStartBench();
        //open card of opponent
        showMessageDialog("show cards","Start Phase");
        opponentController.showCardOnActiveAndBench();

        ArrayList<Integer> t = new ArrayList();
        t.add(1);
        t.add(2);
        //Collections.shuffle(t);
        if(t.get(0)==1){
            //player can add energy and use trainer card
            showMessageDialog("Player first","En/Tr Phase(No PKM Card/Ab.)");
            try {
                drawACardFromPlayerDeckToHand();
            } catch (Exception e) {
                e.printStackTrace();
            }

            playerController.getPlayer().setState(PlayerState.ENERGY_SUPPORTER_ITEM);

            playerController.waitState();

            showMessageDialog("opponent turn","Change Turn!!");
            try {
                drawACardFromOpponentDeckToHand();
                opponentController.actionForPokemonEnergyTrainer(this);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            //opponent can add energy and use trainer card
            showMessageDialog("opponent first","Energy Trainer Phase");
            //need to write the code for AI
            try {
                drawACardFromOpponentDeckToHand();
            } catch (Exception e) {
                e.printStackTrace();
            }
            opponentController.actionForStartEnergyTrainer(this);
        }
        RetreatBtnListener retreatBtnListener = new RetreatBtnListener(playerController);
        JPokemonBoard.getBoard().setUpRetreatBtn(retreatBtnListener);
        while(!playerController.isWinner()&&!opponentController.isWinner()
                &&!playerController.isLose()&&!opponentController.isLose()){
            //run until we don't have rewards card, or no card on deck?
            showMessageDialog("player turn","Change Turn!!");
            try {
                drawACardFromPlayerDeckToHand();
            } catch (Exception e) {
                e.printStackTrace();
            }

            playerController.getPlayer().setState(PlayerState.POKEMON_ENERGY_SUPPORTER_ITEM);
            JPokemonBoard.getBoard().showRetreatBtn();

            playerController.waitState();
            //check active pokemon status at the end of turn



            if(playerController.isWinner()||opponentController.isWinner()
                    ||playerController.isLose()||opponentController.isLose())
            {
                break;
            }

            JPokemonBoard.getBoard().hideRetreatBtn();
            showMessageDialog("opponent turn","Change turn!!");
            //check opponent's active pokemon status

            try{
                drawACardFromOpponentDeckToHand();
                opponentController.actionForPokemonEnergyTrainer(this);
            }catch (Exception e){
                e.printStackTrace();
            }
            //check opponent's active pokemon status at the end of turn

        }
        if(playerController.isLose()){
            showMessageDialog("You are the loser","Game Over");
        }else if(opponentController.isLose()) {
            showMessageDialog("You are the winner","Game Over");
        }else if(playerController.isWinner()){
            showMessageDialog("You are the winner","Game Over");
        }else{
            showMessageDialog("You are the loser","Game Over");
        }
        JPokemonBoard.getBoard().turnOff();
    }

    public void drawACardFromPlayerDeckToHand() throws Exception{
        if(playerController.putACardFromDeckToHand(true)){
            showMessageDialog("You are the loser","Game Over");
            JPokemonBoard.getBoard().turnOff();
        }
    }

    public void drawACardFromOpponentDeckToHand() throws Exception{
        if(opponentController.putACardFromDeckToHand(false)){
            showMessageDialog("You are the winner","Game Over");
            JPokemonBoard.getBoard().turnOff();
        }
    }

    /**
     * Action of player click from each player state
     * @param c card that player clicked
     */
    public void mouseAction(Card c){
        //System.out.println("mouseClicked");
        try {
            //we have game state in order (look at PlayerState class)
            switch(playerController.getPlayer().getState()){
                case START_ACTIVE:
                    //pokemon on hand to active pokemon (when we start game)
                    if (c.isBasicPokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)) {
                        playerController.putACardFromHandToActive((PokemonCard) c, false);
                        playerController.notifyState();
                        playerController.getPlayer().setState(PlayerState.START_BENCH);
                    }
                    break;
                case START_BENCH:
                    //pokemon on hand to pokemon bench(when we start game)
                    if(c.isBasicPokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        if(!playerController.putACardFromHandToBench((PokemonCard)c,true)){
                            showMessageDialog("the number of bench pokemon can't more than 5","Alert!!");
                        }
                    }
                    break;
                case ENERGY_SUPPORTER_ITEM:
                    //when the player plays first (random who is the first player)
                    //add pokemon from hand to bench
                    if(c.isBasicPokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        if(!playerController.putACardFromHandToBench((PokemonCard)c,true)){
                            showMessageDialog("the number of bench pokemon can't more than 5","Alert!!");
                        }
                    }//add energy card
                    else if(c.getCardType().equals(CardType.ENERGY)) {
                        addEnergy((EnergyCard)c,false);
                    }else if(c.getCardType().equals(CardType.TRAINER)){
                        useTrainerCard((TrainerCard)c,false);
                    }
                    break;
                case ENERGY_ITEM:
                    //when the player plays first (random who is the first player)
                    //add pokemon from hand to bench
                    if(c.isBasicPokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        if(!playerController.putACardFromHandToBench((PokemonCard)c,true)){
                            showMessageDialog("the number of bench pokemon can't more than 5","Alert!!");
                        }
                    }//add energy card
                    else if(c.getCardType().equals(CardType.ENERGY)) {
                        addEnergy((EnergyCard)c,false);
                    }
                    else if(c.getCardType().equals(CardType.TRAINER)&&((TrainerCard)c).getTrainerType().equals(TrainerType.ITEM)){
                        useTrainerCard((TrainerCard)c,false);
                    }
                    break;
                case SUPPORTER_ITEM:
                    //in case we use item card and put pokemon on bench
                    /**
                     * need to play the game to check...
                     */
                    if(c.isBasicPokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        if(!playerController.putACardFromHandToBench((PokemonCard)c,true)){
                            showMessageDialog("the number of bench pokemon can't more than 5","Alert!!");
                        }
                    }else if(c.getCardType().equals(CardType.TRAINER)){
                        useTrainerCard((TrainerCard)c,false);
                    }
                    break;
                case ITEM:
                    //in case we use item card and put pokemon on bench
                    /**
                     * need to play the game to check...
                     */
                    if(c.isBasicPokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        if(!playerController.putACardFromHandToBench((PokemonCard)c,true)){
                            showMessageDialog("the number of bench pokemon can't more than 5","Alert!!");
                        }
                    }else if(c.getCardType().equals(CardType.TRAINER)&&((TrainerCard)c).getTrainerType().equals(TrainerType.ITEM)){
                        useTrainerCard((TrainerCard)c,false);
                    }
                    break;
                case POKEMON_ENERGY_SUPPORTER_ITEM:
                    //in case active pokemon attacks
                    if(c.isStageOnePokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        playerController.evolvePokemon((EvolvedPokemonCard)c);
                    }else if(c.isBasicPokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        if(!playerController.putACardFromHandToBench((PokemonCard)c,true)){
                            showMessageDialog("the number of bench pokemon can't more than 5","Alert!!");
                        }
                    }//add energy card
                    else if(c.getCardType().equals(CardType.ENERGY)) {
                        addEnergy((EnergyCard)c,false);
                    }//pokemon use skill
                    else if(c.equals(playerController.getPlayer().getActivePokemonCard())) {
                        attack(c,false);
                    }
                    else if(c.getCardType().equals(CardType.TRAINER)){
                        useTrainerCard((TrainerCard)c,false);
                    }
                    break;
                case POKEMON_ENERGY_ITEM:
                    //in case active pokemon attacks
                    if(c.isStageOnePokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        playerController.evolvePokemon((EvolvedPokemonCard)c);
                    }else if(c.isBasicPokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        if(!playerController.putACardFromHandToBench((PokemonCard)c,true)){
                            showMessageDialog("the number of bench pokemon can't more than 5","Alert!!");
                        }
                    }//add energy card
                    else if(c.getCardType().equals(CardType.ENERGY)) {
                        addEnergy((EnergyCard)c,false);
                    }//pokemon use skill
                    else if(c.equals(playerController.getPlayer().getActivePokemonCard())) {
                        attack(c,false);
                    }
                    else if(c.getCardType().equals(CardType.TRAINER)&&((TrainerCard)c).getTrainerType().equals(TrainerType.ITEM)){
                        useTrainerCard((TrainerCard)c,false);
                    }
                    break;
                case POKEMON_SUPPORTER_ITEM:
                    //in case active pokemon attacks
                    if(c.isStageOnePokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        playerController.evolvePokemon((EvolvedPokemonCard)c);
                    }else if(c.isBasicPokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        if(!playerController.putACardFromHandToBench((PokemonCard)c,true)){
                            showMessageDialog("the number of bench pokemon can't more than 5","Alert!!");
                        }
                    }//active pokemon use skill
                    else if(c.equals(playerController.getPlayer().getActivePokemonCard())) {
                        attack(c,false);
                    }
                    else if(c.getCardType().equals(CardType.TRAINER)){
                        useTrainerCard((TrainerCard)c,false);
                    }
                    break;
                case POKEMON_ITEM:
                    //in case active pokemon attacks
                    if(c.isStageOnePokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        playerController.evolvePokemon((EvolvedPokemonCard)c);
                    }else if(c.isBasicPokemonCard()&&playerController.getPlayer().isThisCardOnHand(c)){
                        if(!playerController.putACardFromHandToBench((PokemonCard)c,true)){
                            showMessageDialog("the number of bench pokemon can't more than 5","Alert!!");
                        }
                    }//active pokemon use skill
                    else if(c.equals(playerController.getPlayer().getActivePokemonCard())) {
                        attack(c,false);
                    }
                    else if(c.getCardType().equals(CardType.TRAINER)&&((TrainerCard)c).getTrainerType().equals(TrainerType.ITEM)){
                        useTrainerCard((TrainerCard)c,false);
                    }
                    break;
                case DONE:

                    done();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void done() throws Exception{
        //check condition when active pokemon was killed during finishing the game
        if(playerController.getPlayer().getActivePokemonCard()!=null){
            //status effect
            playerController.takeActivePokemonStatusEffect();
            //check damage pokemon that dead or not
            lookupKilledPokemonAndGetReward(playerController.getDamagePokemonListFromActiveBench());
        }


        playerController.getPlayer().setState(PlayerState.DONE);
        playerController.getPlayer().changeNewToOldPokemon();
        doTrigger(true, AddAbility.Trigger.OPPONENT_TURN_END);
        playerController.notifyState();
    }



    public void doTrigger(boolean isOpponent, AddAbility.Trigger trigger) throws Exception{
        PlayerController pController;
        //opponent end of turn
        String text;
        if(isOpponent){
            //opponent of player is opponent
            text="opponent";
            pController = opponentController;
        }else{
            //opponent of opponent is player
            text="player";
            pController = playerController;
        }
        ArrayList<PokemonCard> activeBenchPokemonCards = pController.getPokemonListFromActiveBench();
        if(activeBenchPokemonCards==null){
            return;
        }
        for(PokemonCard pokemonCard:activeBenchPokemonCards){
            if(pokemonCard!=null&&pokemonCard.getTools()!=null&&
                    pokemonCard.getTools().size()>0){
                for(TrainerCard trainerCard:pokemonCard.getTools()){
                    Ability ability = trainerCard.getAbilities().get(0).getAbility();
                    if(ability.getType().equals(AbilityType.ADD)
                            &&((AddAbility)ability).getTrigger().equals(trigger)){
                        Set<Card> cardSet = new HashSet();
                        //add pokemon card for self
                        cardSet.add(pokemonCard);
                        Ability subAbility = trainerCard.getAbilities().get(0).getAbility().getSubAbility();
                        showMessageDialog(text+" "+pokemonCard.getName()+" get "+subAbility,"ADD WITH TRIGGER "+trigger.name());
                        abilityController.useAbility(subAbility,!isOpponent,cardSet);
                    }
                }
            }
        }
    }

    /**
     * when player clicked active pokemon, use pokemon abilities
     * @param c
     */
    public void attack(Card c, boolean isOpponent) throws Exception {
        PokemonCard pokemonCard = (PokemonCard) c;
        ArrayList<PokemonAbility> abilities = pokemonCard.getAbilities();
        if(pokemonCard.getStatus().contains(PokemonStatus.PARALYZED)){
            showMessageDialog("YOU CAN'T ATTACK BECAUSE OF PARALYZED","WARNING !!");
            return;
        }else if(pokemonCard.getStatus().contains(PokemonStatus.ASLEEP)){
            showMessageDialog("YOU CAN'T ATTACK BECAUSE OF ASLEEP","WARNING !!");
            return;
        }
        if(abilities.size()>0) {
            int out;
            if(isOpponent){
                out = opponentController.chooseActivePokemonAbility();
            }else {
                out = showOptionDialog("choose pokemon abilities",
                        c.getName() + " abilities", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        abilities.toArray(), abilities.get(0));
            }
            if(out!=JOptionPane.CLOSED_OPTION) {
                //need to work on this
                PokemonAbility pokemonAbility = abilities.get(out);
                if(pokemonAbility.isAbleToAttack(((PokemonCard)c).getEnergies())) {
                    Ability ability = pokemonAbility.getAbility();
                    Set<Card> cardSet = abilityController.useAbility(ability,isOpponent,null);
                    ArrayList<Card> cards;
                    if(cardSet==null) {
                        return;
                    }else {
                        cards = new ArrayList(cardSet);
                    }
                    AbilityTarget target = ability.getTarget();

                    ArrayList<PokemonCard> pokemonCards = new ArrayList(cards);
                    showMessageDialog("use " + abilities.get(out).getName() + " !!!", "Use ability!!");

                    if(target.equals(AbilityTarget.OPPONENT_ACTIVE)||
                            target.equals(AbilityTarget.CHOICE_OPPONENT_BENCH)||
                            target.equals(AbilityTarget.CHOICE_OPPONENT)) {
                        //check all pokemons if the damage is higher than hit point then go to trash
                        lookupKilledPokemonAndGetReward(pokemonCards);
                    }

                    if(!isOpponent) {
                        playerController.notifyState();
                        done();
                    }
                }else{
                    showMessageDialog("can't use " + abilities.get(out).getName() + " !!!","Don't do stupid click, it wastes time");
                }
            }
        }
        else
            showMessageDialog("this pokemon has no ability","this pokemon has no abilities");
    }


    /**
     * Add mouse listener to card
     * @param cards All cards at the beginning of the game
     */
    public void addCardListener(ArrayList<Card> cards){
        for (Card card:cards) {
            card.addMouseListener(new CardMouseListener(card,this));
        }
    }

    public void moveCardToHand(Card c, boolean isOpponent){
        try {
            PlayerController pController;
            if(isOpponent){
                pController = getOpponentController();
            }else{
                pController = getPlayerController();
            }
            if(pController.getPlayer().getDeck().contains(c)) {
                pController.getPlayer().getDeck().remove(c);
            }else if(pController.getPlayer().getTrash().contains(c)){
                pController.getPlayer().getTrash().remove(c);
            }else{
                throw new Exception("Only Deck or Trash to move to hand now");
            }

            pController.getPlayer().getPlayerHandCards().add(c);
            JPokemonBoard.getBoard().setGroupPositionBoard(BoardPosition.HAND,
                    pController.getPlayer().getPlayerHandCards(),
                    isOpponent, !isOpponent);
            JPokemonBoard.getBoard().getJLabelBoard().repaint();
            if(!isOpponent) {
                JPokemonBoard.getBoard().closeDialog(BoardPosition.DECK, false);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void moveCardFromHandToDest(ArrayList<Card> handCards, DeckAbility.Destination destination, boolean isOpponent) throws Exception {
        PlayerController pController;
        if (isOpponent){
            pController = opponentController;
        }else {
            pController = playerController;
        }

        if(destination.equals(DeckAbility.Destination.DISCARD)){
            for (Card hand:
                    handCards) {
                int index = pController.getPlayer().getPlayerHandCards().indexOf(hand);
                Card c = pController.getPlayer().getPlayerHandCards().remove(index);
                pController.getPlayer().getTrash().push(c);
                JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.TRASH, c, isOpponent, false);
            }
        }else if(destination.equals(DeckAbility.Destination.BOTTOM_DECK)||
                destination.equals(DeckAbility.Destination.TOP_DECK)){
            if(destination.equals(DeckAbility.Destination.BOTTOM_DECK)){
                for (Card hand:handCards) {
                    int index = pController.getPlayer().getPlayerHandCards().indexOf(hand);
                    Card c = pController.getPlayer().getPlayerHandCards().remove(index);
                    pController.getPlayer().getDeck().add(0, c);
                    JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.DECK, c, isOpponent, false);
                }
            }else{
                Iterator<Card> iteratorHandCards = handCards.iterator();
                while(iteratorHandCards.hasNext()) {
                    Card hand = iteratorHandCards.next();
                    int index = pController.getPlayer().getPlayerHandCards().indexOf(hand);
                    Card c = pController.getPlayer().getPlayerHandCards().remove(index);
                    pController.getPlayer().getDeck().push(c);
                    JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.DECK, c, isOpponent, false);
                }
            }
        }
        JPokemonBoard.getBoard().closeDialog(BoardPosition.HAND, isOpponent);
    }

    public void showCardsOnTrash(boolean isOpponent) throws Exception {
        if(isOpponent){
            JPokemonBoard.getBoard().showPileInformation(getOpponentController().getPlayer().getTrash(),"Trash",BoardPosition.TRASH,isOpponent);
        }else{
            JPokemonBoard.getBoard().showPileInformation(getPlayerController().getPlayer().getTrash(),"Trash",BoardPosition.TRASH,isOpponent);
        }
    }

    public void showCardsOnDeck(boolean isOpponent) throws Exception {
        if(isOpponent){
            JPokemonBoard.getBoard().showPileInformation(getOpponentController().getPlayer().getDeck(),"Deck",BoardPosition.DECK,isOpponent);
        }else{
            JPokemonBoard.getBoard().showPileInformation(getPlayerController().getPlayer().getDeck(),"Deck",BoardPosition.DECK,isOpponent);
        }
    }

    /**
     * Trainer Card has ability that can effect to both player
     * @param trainerCard
     * @param isOpponent
     * @throws Exception
     */
    public void useTrainerCard(TrainerCard trainerCard, boolean isOpponent) throws Exception {
        Ability ability;
        if(trainerCard.getAbilities().size()<1){
            throw new Exception("you forget to add ability on "+trainerCard.getName());
        }else if (trainerCard.getAbilities().size() > 1) {
            //you can choose ability
            ability = null;
        }else{
            ability = trainerCard.getAbilities().get(0).getAbility();
        }
        PlayerController pController;
        if (isOpponent) {
            pController=opponentController;
        } else {
            pController=playerController;
        }
        if (ability.getType().equals(AbilityType.ADD)) {
            Set<Card> cardSet = new HashSet<>();
            cardSet.add(trainerCard);
            abilityController.useAbility(ability, isOpponent, cardSet);
            pController.getPlayer().useTrainerCardOnHand(trainerCard,false);
            showMessageDialog("use "+trainerCard.getName(),""+trainerCard.getAbilities().get(0));
        } else {
            Set<Card> cardSet = abilityController.useAbility(ability, isOpponent, null);
            if (cardSet != null) {
                ArrayList<Card> cards = new ArrayList<>(cardSet);
                for (Card card : cards) {
                    if (card.getCardType().equals(CardType.POKEMON)) {
                        JPokemonBoard.getBoard().reorderPokemonOnBoard((PokemonCard) card);
                    }
                }
                //for(Card)
                pController.getPlayer().useTrainerCardOnHand(trainerCard,true);
                JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.TRASH, trainerCard, isOpponent, false);
                //to reorder label on the board
                ArrayList<Card> trash = new ArrayList(pController.getPlayer().getTrash());
                JPokemonBoard.getBoard().reorderCardLabel(trash,BoardPosition.TRASH);
                showMessageDialog("use "+trainerCard.getName(),""+trainerCard.getAbilities().get(0));
            }
        }
    }

    public void addEnergy(EnergyCard energyCard, boolean isOpponent){
        //put energy card on active or bench pokemon
        String[] pokemonCardsList = playerController.getPokemonNameArrayOnBenchActive();
        int out = showOptionDialog("choose Pokemon",
                energyCard.getName() + " abilities", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                pokemonCardsList, null);
        if (out != JOptionPane.CLOSED_OPTION) {
            PokemonCard pokemon = playerController.getPokemonFromActiveBenchOption(out);
            //add energy to pokemon and change state of player
            playerController.getPlayer().addEnergyCardOnHandToPokemon(pokemon,energyCard);
            JPokemonBoard.getBoard().setEnergyCardOnPokemon(energyCard, pokemon, false);
        }
    }


    /**
     *
     * @param pokemonCards pokemon that got damage
     * @throws Exception
     */
    public void lookupKilledPokemonAndGetReward(ArrayList<PokemonCard> pokemonCards) throws Exception {
        for (PokemonCard pokemonCard : pokemonCards) {
            if (pokemonCard.getDamage() >= pokemonCard.getHitpoint()) {
                showMessageDialog(pokemonCard.getName() + " is dead"
                        , "Good Bye " + pokemonCard.getName());
                int rewardNo;
                Player player;
                pokemonCard.setDamage(0);
                pokemonCard.setText("");
                boolean isOpponent;

                if(playerController.getPlayer().isPokemonCardOnActiveBench(pokemonCard)){

                    isOpponent = true;//opponent will get reward because player pokemon is dead
                    //player need to change bench pokemon to active pokemon if active pokemon is dead
                    playerController.moveDeadPokemonToTrash(pokemonCard,BoardPosition.ACTIVE_POKEMON,!isOpponent);
                    ArrayList<Card> pile = new ArrayList(playerController.getPlayer().getTrash());
                    JPokemonBoard.getBoard().reorderCardLabel(pile,BoardPosition.TRASH);

                    player = opponentController.getPlayer();
                    rewardNo = opponentController.chooseRewardCardNumber();

                }else {
                    isOpponent = false;//player will get reward
                    opponentController.moveDeadPokemonToTrash(pokemonCard,BoardPosition.BENCH_POKEMON,!isOpponent);
                    ArrayList<Card> pile = new ArrayList(opponentController.getPlayer().getTrash());
                    JPokemonBoard.getBoard().reorderCardLabel(pile,BoardPosition.TRASH);

                    player = playerController.getPlayer();
                    rewardNo = showOptionDialog("choose reward cards", pokemonCard.getName() + " is dead",
                            JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, playerController.getRewardList(),
                            null);

                }
                Card rewardCard = player.getRewardCards().remove(rewardNo);
                player.getPlayerHandCards().add(rewardCard);
                //if opponent get rewards, his hand cards will be hid
                JPokemonBoard.getBoard().setGroupPositionBoard(BoardPosition.HAND, player.getPlayerHandCards(), isOpponent,!isOpponent);
                PokemonCard newActivePokemon=null;

                JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.TRASH, pokemonCard, !isOpponent, false);
                if(pokemonCard instanceof EvolvedPokemonCard){
                    JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.TRASH, ((EvolvedPokemonCard)pokemonCard).getBasePokemon(),
                            !isOpponent, false);
                }

                for (EnergyCard e:pokemonCard.getEnergies()) {
                    JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.TRASH, e,
                            !isOpponent, false);
                }

                for (TrainerCard t:pokemonCard.getTools()) {
                    JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.TRASH, t,
                            !isOpponent, false);
                }

                if(!isOpponent) {
                    //to reorder label on the board
                    if(opponentController.getPlayer().getActivePokemonCard()==null) {
                        newActivePokemon = opponentController.getNewActivePokemon();
                    }
                    if(newActivePokemon!=null) {
                        showMessageDialog("opponent put " + newActivePokemon.getName()
                                + " on the active area", "put pokemon on board");
                    }
                }else{
                    //to reorder label on the board
                    if(playerController.getPlayer().getActivePokemonCard()==null){
                        newActivePokemon = playerController.getNewActivePokemon();
                    }

                }

                if (newActivePokemon != null) {
                    JPokemonBoard.getBoard().movePokemonOnOnePositionBoard(BoardPosition.ACTIVE_POKEMON, newActivePokemon, !isOpponent);
                }else{
                    done();
                }
            }
        }
    }

    protected void showMessageDialog(String message, String title){
        JPokemonBoard.getBoard().showMessageDialog(message,title);
    }

    protected int showOptionDialog(String message,String title,int optionType, int messageType, Object[] option, Object initialValue){
        return JOptionPane.showOptionDialog(JPokemonBoard.getBoard().getJLabelBoard(), message, title, optionType, messageType, null, option, initialValue);
    }

    protected int showConfirmDialog(String message,String title,int optionType){
        return JOptionPane.showConfirmDialog(JPokemonBoard.getBoard().getJLabelBoard(),message,title,optionType);
    }
}
