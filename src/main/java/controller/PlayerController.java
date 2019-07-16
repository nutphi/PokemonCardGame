package controller;

import model.*;
import model.ability.extra.CoinToss;
import view.BoardPosition;
import view.JPokemonBoard;

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * an abstract class of player and computer controller.
 * we don't use this one as the main object.
 * Created by nutph on 5/7/2017.
 */
public abstract class PlayerController{
    private int numberHandCards;
    private int numberRewardCards;
    private int maxNumberBenchCards;
    private Player player;

    public PlayerController(){}

    public PlayerController(int numberHandCards, int numberRewardCards, int maxNumberBenchCards, Player player){
        this.numberHandCards = numberHandCards;
        this.numberRewardCards = numberRewardCards;
        this.maxNumberBenchCards = maxNumberBenchCards;
        this.player = player;
    }

    /**
     * Constructor for dependency injection (testing)
     * @param numberHandCards
     * @param numberRewardCards
     * @param maxNumberBenchCards
     */
    public PlayerController(int numberHandCards, int numberRewardCards, int maxNumberBenchCards) {
        this.numberHandCards = numberHandCards;
        this.numberRewardCards = numberRewardCards;
        this.maxNumberBenchCards = maxNumberBenchCards;
    }

    /**
     * set Player
     * @param player set object of Player into this controller class
     */
    public void setPlayer(Player player){
        this.player= player;
    }

    /**
     * get Player
     * @return player object of Player
     */
    public Player getPlayer(){
        return player;
    }

    /**
     * get number of cards on hand at the beginning
     * @return number of hand cards
     */
    public int getStartGameNumberHandCards() {
        return numberHandCards;
    }

    /**
     * set number of cards on hand at the beginning
     * @param numberHandCards set Integer of the number of hand cards
     */
    public void setStartGameNumberHandCards(int numberHandCards) {
        this.numberHandCards = numberHandCards;
    }

    /**
     * get number of reward cards
     * @return integer of the number of reward cards
     */
    public int getNumberRewardCards() {
        return numberRewardCards;
    }

    /**
     * set number of rewards cards
     * @param numberRewardCards number of reward cards
     */
    public void setNumberRewardCards(int numberRewardCards) {
        this.numberRewardCards = numberRewardCards;
    }

    /**
     * get maximum number of bench cards
     * @return integer of the maximum number of bench cards
     */
    public int getMaxNumberBenchCards() {
        return maxNumberBenchCards;
    }

    /**
     * set maximum number of bench cards
     * @param maxNumberBenchCards maximum number of bench cards
     */
    public void setMaxNumberBenchCards(int maxNumberBenchCards) {
        this.maxNumberBenchCards = maxNumberBenchCards;
    }

    /**
     * set initial value on board for one player
     */
    public void initializeDeckHands(){
        Stack<Card> playerDeck = new Stack();
        Stack<Card> playerTrash = new Stack();
        ArrayList<Card> playerHandCards = new ArrayList();
        ArrayList<PokemonCard> playerBenchPokemonCards = new ArrayList();
        ArrayList<Card> rewardCards = new ArrayList();
        player = new Player(null, playerBenchPokemonCards, playerDeck, playerTrash,
                playerHandCards, 0, PlayerState.START_ACTIVE,rewardCards);
    }

    /**
     * set up all cards into the deck from both the player object and GUI
     * @param cards cards that will be added in the deck
     */
    public void setUpDeck(ArrayList<Card> cards) throws Exception{
        boolean isOpponent = this instanceof ComputerPlayerController;
        for (int i = cards.size()-1;i>=0;--i) {
            JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.DECK,cards.get(i),isOpponent,true);
            player.getDeck().push(cards.get(i));
        }
    }

    /**
     * set up reward cards when player kill pokemon on the board area
     */
    public void setUpReward(){
        boolean isOpponent = this instanceof ComputerPlayerController;
        for(int i=0;i<numberRewardCards;i++) {
            Card c= getPlayer().getDeck().pop();
            JPokemonBoard.getBoard().putRewardOnBoard(c, i, numberRewardCards, isOpponent);
            this.getPlayer().getRewardCards().add(c);
        }
    }

    /**
     *
     *
     * put number of cards from the deck to hand
     * @param n number of cards that will be added on hand.
     * @param isVisible if it is true, we can see all of those cards information
     * @return isDeckEmpty false if deck is not empty; otherwise true.
     * @throws Exception
     */
    public boolean putNCardFromDeckToHand(int n, boolean isVisible) throws Exception {
        boolean isDeckEmpty;
        for(int i=0;i<n;i++){
            isDeckEmpty = this.putACardFromDeckToHand(isVisible);
            if(isDeckEmpty){
                return true;
            }
        }
        return false;
    }

    /**
     *
     *
     * put a card from the deck to hand
     * @param isVisible if it is true, we can see all of those cards information@return
     * @throws Exception
     * @return
     * */
    public boolean putACardFromDeckToHand(boolean isVisible) throws Exception {
        boolean isDeckEmpty;
        boolean isOpponent = this instanceof ComputerPlayerController;
        if(player.getDeck().size()>0) {
            Card card = player.getDeck().pop();
            player.getPlayerHandCards().add(card);
            JPokemonBoard.getBoard().setGroupPositionBoard(BoardPosition.HAND,
                    player.getPlayerHandCards(), isOpponent, isVisible);
            isDeckEmpty=false;
        }else{
            JPokemonBoard.getBoard().showMessageDialog("No card on the deck","Deck information");
            isDeckEmpty=true;
        }
        return isDeckEmpty;
    }

    /**
     *
     * @param c Pokemon Card that put on active area
     * @param isOppenent if opponent, set opponent card on GUI; otherwise, set player card on GUI
     * @throws Exception throw exception when we use a wrong method (programmer problem)
     */
    public void putACardFromHandToActive(PokemonCard c,boolean isOppenent) throws Exception{
        int i = player.getPlayerHandCards().indexOf(c);
        PokemonCard card = (PokemonCard)player.getPlayerHandCards().get(i);
        player.getPlayerHandCards().remove(card);
        player.setActivePokemonCard(card);
        JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.ACTIVE_POKEMON, card,
                isOppenent,false);
    }

    /**
     * put a pokemon card from hand to bench
     * @param c Pokemon Card that will be put on bench
     * @param isVisible if it is true, we can see all of those cards information
     */
    public boolean putACardFromHandToBench(PokemonCard c,boolean isVisible) throws Exception {
        boolean isOpponent = this instanceof ComputerPlayerController;
        int i = player.getPlayerHandCards().indexOf(c);
        PokemonCard card = (PokemonCard)player.getPlayerHandCards().get(i);
        if(player.getBenchPokemonCards().size()<getMaxNumberBenchCards()) {
            player.getPlayerHandCards().remove(card);
            player.getBenchPokemonCards().add(c);
            JPokemonBoard.getBoard().setGroupPositionBoard(BoardPosition.BENCH_POKEMON,
                    player.getBenchPokemonCards(), isOpponent, isVisible);
            return true;
        }else{
            return false;
        }
    }

    /**
     * check if there is no pokemon on active area or not
     * @return true if there is no active pokemon on active area; otherwise, false.
     */
    public boolean hasNoActivePokemonCard(){
        //System.out.println(player.getActivePokemonCard());
        return player.getActivePokemonCard()==null;
    }

    /**
     * check if player is winner or not
     * @return true if player is the winner; otherwise, false.
     */
    public boolean isWinner(){
        return player.getRewardCards().size()==0;
    }

    /**
     * check if player does not have active pokemon
     * @return true if player does not have; otherwise, false.
     */
    public boolean isLose(){
        return player.getActivePokemonCard()==null;
    }

    /**
     * check if mulligan will be occur from the player or not.
     * @return true if this player has no basic pokemon; otherwise false.
     */
    public boolean isMulligan(){
        return !getPlayer().isBasicPokemonCardOnHand();
    }

    /**
     * do the mulligan. by return cards from the hand the deck.
     * Then shuffle the deck, and draw the default number of hand cards.
     * @throws Exception
     */
    public void takeAMulligan() throws Exception {
        boolean isOpponent = this instanceof ComputerPlayerController;
        for(Card c:player.getPlayerHandCards()){
            c.setVisibleCard(false);
            player.getDeck().add(c);
            JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.DECK,c,isOpponent,true);
        }
        player.getPlayerHandCards().removeAll(player.getPlayerHandCards());
        player.shuffleDeck();
    }

    /**
     * show cards on hand (for mulligan and trainer card)
     */
    public void showAllHandCards() {
        for (Card c : player.getPlayerHandCards()) {
            c.setVisibleCard(true);
        }
    }

    /**
     * get array of number from 1 to i+1 of reward to choose by i+1 is number of reward cards
     * @return array of integer
     */
    public Integer[] getRewardList(){
        Integer[] values = new Integer[player.getRewardCards().size()];
        for(int i=0;i<player.getRewardCards().size();i++){
            values[i] = new Integer(i+1);
        }
        return values;
    }

    /**
     * To get the list of pokemons that can evolve by Pokemon c
     * @param c EvolvedPokemon
     * @return string of pokemons that can evolve by Pokemon c
     */
    public ArrayList<String> getEvolvingPokemonListNamesOnBoard(PokemonCard c){
        ArrayList<String> list = new ArrayList<String>();
        PokemonCard active = getPlayer().getActivePokemonCard();
        if(c.getDescription().endsWith(active.getName())&&!active.isNewPokemon()){
            list.add(active.getName()+" active");
        }
        int j=1;
        for(int i =0;i<getPlayer().getBenchPokemonCards().size();++i){
            PokemonCard bench =getPlayer().getBenchPokemonCards().get(i);
            if(c.getDescription().endsWith(bench.getName())&&!bench.isNewPokemon()){
                list.add(bench.getName()+j);
                j++;
            }
        }
        return list;
    }

    /**
     * Put basic pokemon card to stage one pokemon
     * @param e EvolvedPkemonCard
     * @param pokemonNo index of number of evolving pokemon
     * @param isActivePokemonEvolve if there is active pokemon in evolve
     * @return evolving pokemon
     */
    public PokemonCard addBasicPokemonCardToStageOnePokemon(EvolvedPokemonCard e, int pokemonNo, boolean isActivePokemonEvolve){
        PokemonCard base;
        ArrayList<PokemonCard> list = getEvolvingPokemonListOnBoard(e);
        if(pokemonNo==0&&isActivePokemonEvolve){
            base = getPlayer().getActivePokemonCard();
            e.setBasePokemon(base);
            getPlayer().setActivePokemonCard(e);
        }else {
            base = list.get(pokemonNo);
            e.setBasePokemon(base);
            int i=getPlayer().getBenchPokemonCards().indexOf(base);
            getPlayer().getBenchPokemonCards().set(i,e);
        }

        base.setText("");
        base.getStatus().clear();
        e.setDamage(base.getDamage());
        e.setHorizontalTextPosition(JLabel.CENTER);
        e.setFont(new Font("Courier", Font.PLAIN, 24));
        String show = ""+e.getDamage();
        if(e.getDamage()==0){
            show = "";
        }
        e.setText("<html><br>" + show + "<html>");
        for(EnergyCard energyCard:base.getEnergies())
        {
            e.getEnergies().add(energyCard);
        }

        return base;
    }

    /**
     * this method use only the one above
     * @param c EvolvedPokemonCard that want to put on pokemon on active/bench area
     * @return list of pokemons on active and bench (active=0,bench=0-n)
     */
    public ArrayList<PokemonCard> getEvolvingPokemonListOnBoard(EvolvedPokemonCard c){
        ArrayList<PokemonCard> list = new ArrayList<PokemonCard>();
        PokemonCard active = getPlayer().getActivePokemonCard();
        if(c.getDescription().endsWith(active.getName())&&!active.isNewPokemon()){
            list.add(active);
        }
        for(int i =0;i<getPlayer().getBenchPokemonCards().size();++i){
            PokemonCard bench =getPlayer().getBenchPokemonCards().get(i);
            if(c.getDescription().endsWith(bench.getName())&&!bench.isNewPokemon()){
                list.add(bench);
            }
        }
        return list;
    }

    public PokemonCard getPokemonFromActiveBenchOption(int out){
        if(out==0){
            return getPlayer().getActivePokemonCard();
        }else{
            return getPlayer().getBenchPokemonCards().get(out-1);
        }
    }

    public PokemonCard getPokemonFromActiveBenchWithoutSourcePokemonOption(PokemonCard sourcePokemonCard,int out,
                                                                           boolean opponentTarget, PlayerController opponentController){
        return getPokemonListFromActiveBenchWithoutSourcePokemon(sourcePokemonCard,opponentTarget,opponentController).get(out);
    }

    public PokemonCard getPokemonFromBenchOption(int out){
        return getPlayer().getBenchPokemonCards().get(out);
    }

    public ArrayList<PokemonCard> getDamageStatusPokemonListFromActiveBench(){
        Set<PokemonCard> pokemonCards = new HashSet<PokemonCard>();
        pokemonCards.addAll(getActiveStatusPokemonList());
        pokemonCards.addAll(getDamagePokemonListFromActiveBench());
        return new ArrayList(pokemonCards);
    }


    public ArrayList<PokemonCard> getActiveStatusPokemonList(){
        Set<PokemonCard> pokemonCards = new HashSet<PokemonCard>();
        Set<PokemonStatus> status = getPlayer().getActivePokemonCard().getStatus();
        if(!status.isEmpty()){
            pokemonCards.add(getPlayer().getActivePokemonCard());
        }
        return new ArrayList(pokemonCards);
    }

    public ArrayList<PokemonCard> getPokemonListFromActiveBench(){
        ArrayList<PokemonCard> pokemonCards = new ArrayList<>();
        PokemonCard active =getPlayer().getActivePokemonCard();
        pokemonCards.add(active);
        pokemonCards.addAll(getPlayer().getBenchPokemonCards());
        return pokemonCards;
    }

    private boolean isEvolvedPokemonOnDeck(PokemonCard pokemonCard){
        Stack<Card> deck = getPlayer().getDeck();

        boolean isEvolved = false;
        for (int j = 0; j < deck.size(); ++j) {
            Card card = deck.get(j);
            if (card instanceof EvolvedPokemonCard) {
                if(card.getDescription().endsWith(pokemonCard.getName())){
                    isEvolved = true;
                    break;
                }
            }
        }
        return isEvolved;
    }

    public ArrayList<PokemonCard> getDamagePokemonListFromActiveBench(){
        ArrayList<PokemonCard> pokemonCards = getPokemonListFromActiveBench();
        Iterator<PokemonCard> dmgPokemonCards = pokemonCards.iterator();
        while(dmgPokemonCards.hasNext()){
            PokemonCard pokemonCard = dmgPokemonCards.next();
            if(pokemonCard.getDamage()==0){
                dmgPokemonCards.remove();
            }
        }
        return pokemonCards;
    }

    public ArrayList<PokemonCard> getActiveBenchPokemonListToEvolve(){
        ArrayList<PokemonCard> pokemonCardsToEvolve = new ArrayList();
        PokemonCard active =getPlayer().getActivePokemonCard();
        if(isEvolvedPokemonOnDeck(active)){
            pokemonCardsToEvolve.add(active);
        }
        ArrayList<PokemonCard> benchPokemonCards = getPlayer().getBenchPokemonCards();
        for(PokemonCard benchPokemonCard:benchPokemonCards){
            if(isEvolvedPokemonOnDeck(benchPokemonCard)){
                pokemonCardsToEvolve.add(benchPokemonCard);
            }
        }
        return pokemonCardsToEvolve;
    }

    public PokemonCard getActiveBenchPokemonListToEvolveOption(int out){
        return getActiveBenchPokemonListToEvolve().get(out);
    }

    public PokemonCard getActiveStatusPokemonListOption(int out){
        return getActiveStatusPokemonList().get(out);
    }

    public PokemonCard getDamageStatusPokemonFromActiveBenchOption(int out){
        return getDamageStatusPokemonListFromActiveBench().get(out);
    }

    public PokemonCard getDamagePokemonFromActiveBenchOption(int out){
        return getDamagePokemonListFromActiveBench().get(out);
    }

    public void moveDeadPokemonToTrash(PokemonCard pokemonCard, BoardPosition position, boolean isOpponent) throws Exception{
        if(!position.equals(BoardPosition.ACTIVE_POKEMON)&&!position.equals(BoardPosition.BENCH_POKEMON)){
            throw new Exception("only pokemon position(active/bench) on board ("+position+")");
        }
        if(getPlayer().getBenchPokemonCards().contains(pokemonCard)){
            getPlayer().getBenchPokemonCards().remove(pokemonCard);
        }else if(getPlayer().getActivePokemonCard().equals(pokemonCard)){
            getPlayer().setActivePokemonCard(null);
        }

        Iterator<EnergyCard> energyCardIterator = pokemonCard.getEnergies().iterator();
        while(energyCardIterator.hasNext()){
            EnergyCard e = energyCardIterator.next();
            getPlayer().getTrash().push(e);
            energyCardIterator.remove();
        }
        if(pokemonCard instanceof EvolvedPokemonCard){
            PokemonCard e = ((EvolvedPokemonCard)pokemonCard).getBasePokemon();
            getPlayer().getTrash().push(e);
        }
        getPlayer().getTrash().push(pokemonCard);
        JPokemonBoard.getBoard().moveCardOnDeckTrash(BoardPosition.TRASH,new ArrayList(getPlayer().getTrash()),isOpponent);
    }

    public ArrayList<PokemonCard> getPokemonListFromActiveBenchWithoutSourcePokemon(PokemonCard sourcePokemonCard, boolean isOpponentTarget, PlayerController opponentController){
        ArrayList<PokemonCard> pokemonCards;
        if(isOpponentTarget) {
            pokemonCards = opponentController.getPokemonListFromActiveBench();
        }else {
            pokemonCards = getPokemonListFromActiveBench();
        }
        pokemonCards.remove(sourcePokemonCard);
        return pokemonCards;
    }

    public String[] getPokemonOptionFromActiveBenchWithoutSourcePokemon(PokemonCard sourcePokemonCard,boolean isOpponentTarget, PlayerController opponentController){
        ArrayList<PokemonCard> pokemonCards = getPokemonListFromActiveBenchWithoutSourcePokemon(sourcePokemonCard,isOpponentTarget, opponentController);
        return getPokemonNameArrayFromList(pokemonCards);
    }

    public Card getCardOnHandRandomly(){
        int i = (int)(Math.random()*(getPlayer().getPlayerHandCards().size()-1));
        if(getPlayer().getPlayerHandCards().size()>=0){
            return getPlayer().getPlayerHandCards().get(i);
        }
        return null;
    }


    /**
     * status:
     *
     * 1 paralyzed:
     *          a paralyzed pokemon may neither attack nor retreat.
     *      -At the end of a turn-, a player removes status:paralyzed from their active
     *      pokemon
     *
     * 2 asleep:
     *          a sleeping pokemon may neither attack nor retreat. -At the end of a turn-,
     *      a player has a 50% chance that sleep will be removed from their pokemon.
     *
     * 3 stuck:
     *          a stuck pokemon may not retreat. -At the end of a turn-, a
     *      player removes status:stuck from their active pokemon
     *
     * 4 poisoned:
     *          a poisoned pokemon takes one damage -at the end of their turn.-
     *
     */
    public void takeActivePokemonStatusEffect(){
        PokemonCard activePokemonCard = getPlayer().getActivePokemonCard();
        Iterator<PokemonStatus> iterStatus = activePokemonCard.getStatus().iterator();
        CoinToss coinToss;
        while(iterStatus.hasNext()) {
            PokemonStatus status = iterStatus.next();
            String text;

            switch (status) {
                case PARALYZED:
                    text = activePokemonCard.getName()+" RECOVER FROM PARALYZED";
                    activePokemonCard.getStatus().remove(PokemonStatus.PARALYZED);
                    JPokemonBoard.getBoard().showMessageDialog("GOT "+text,"PARALYZED EFFECT");
                    activePokemonCard.setDamageStatusText();
                    break;
                case ASLEEP:
                    coinToss = CoinToss.random();
                    if(coinToss.equals(CoinToss.HEAD)){
                        text = "HEAD, "+activePokemonCard.getName()+" AWAKE";
                        activePokemonCard.getStatus().remove(PokemonStatus.ASLEEP);
                        activePokemonCard.setDamageStatusText();
                    }else{
                        text = "TAIL, "+activePokemonCard.getName()+" STILL SLEEP";
                    }
                    JPokemonBoard.getBoard().showMessageDialog("GOT "+text,"ASLEEP EFFECT");
                    break;
                case STUCK:
                    text = activePokemonCard.getName()+" RECOVER FROM STUCK";
                    activePokemonCard.getStatus().remove(PokemonStatus.STUCK);
                    activePokemonCard.setDamageStatusText();
                    JPokemonBoard.getBoard().showMessageDialog("GOT "+text,"STUCK EFFECT");
                    break;
                case POISONED:
                    JPokemonBoard.getBoard().showMessageDialog("GOT "+activePokemonCard.getName()+" 10 DAMAGE FROM POISON",
                            "POISON EFFECT");
                    activePokemonCard.setDamage(activePokemonCard.getDamage()+10);
                    activePokemonCard.setDamageStatusText();
                    break;
            }
        }
    }

    /**
     * To get a list of active and bench pokemon
     * @return array of Active and Bench pokemon names
     */
    public String[] getPokemonNameArrayOnBenchActive(){
        int size = getPlayer().getBenchPokemonCards().size()+1;
        String[] pCards= new String[size];
        pCards[0] = getPlayer().getActivePokemonCard().getName()+" active";
        for(int i=1;i<size;i++)
        {
            pCards[i] = getPlayer().getBenchPokemonCards().get(i-1).getName()+i;
        }
        return pCards;
    }

    /**
     * To get a list of active and bench pokemon
     * @return array of Active and Bench pokemon names
     */
    public String[] getPokemonNameArrayOnBench(){
        int size = getPlayer().getBenchPokemonCards().size();
        String[] pCards= new String[size];
        for(int i=0;i<size;i++)
        {
            pCards[i] = getPlayer().getBenchPokemonCards().get(i).getName()+(i+1);
        }
        return pCards;
    }

    public String[] getPokemonNameArrayFromList(ArrayList<PokemonCard> pokemonCards){
        String[] pokemonCardsString = new String[pokemonCards.size()];
        for(int i=0;i<pokemonCards.size();++i){
            pokemonCardsString[i]=pokemonCards.get(i).getName();
        }
        return pokemonCardsString;
    }

    //for reenergy
    public ArrayList<PokemonCard> getPokemonListWithEnergies(){
        ArrayList<PokemonCard> pokemonCards = getPokemonListFromActiveBench();
        Iterator<PokemonCard> pokemonCardIterator = pokemonCards.iterator();
        while(pokemonCardIterator.hasNext()){
            PokemonCard pokemonCard = pokemonCardIterator.next();
            if(!(pokemonCard.getEnergies()!=null&&pokemonCard.getEnergies().size()>0)){
                pokemonCardIterator.remove();
            }
        }
        return pokemonCards;
    }

    public String[] getEnergyNameArrayFromPokemon(PokemonCard pokemonCard){
        ArrayList<EnergyCard> energyCards = getEnergyListFromPokemon(pokemonCard);
        String[] energyCardsString = new String[energyCards.size()];
        for(int i=0;i<energyCards.size();++i){
            energyCardsString[i]=energyCards.get(i).getName();
        }
        return energyCardsString;
    }

    public Integer[] getDamageArrayFromPokemon(PokemonCard sourcePokemon){
        int damageCounter = sourcePokemon.getDamage()/10;
        Integer[] damageArray = new Integer[damageCounter];
        for (int i = 0; i < damageCounter; i++) {
            damageArray[i] = (i+1)*10;
        }
        return damageArray;
    }

    public ArrayList<EnergyCard> getEnergyListFromPokemon(PokemonCard pokemonCard){
        return pokemonCard.getEnergies();
    }

    public boolean isItemCardOnPile(BoardPosition boardPosition) throws Exception{
        Stack<Card> pile;
        if(boardPosition.equals(BoardPosition.TRASH)){
            pile = getPlayer().getTrash();
        }else if(boardPosition.equals(BoardPosition.DECK)){
            pile = getPlayer().getDeck();
        }else{
            throw new Exception("only deck and trash");
        }
        for(Card card:pile){
            if(card.getCardType().equals(CardType.TRAINER)
                    &&((TrainerCard)card).getTrainerType().equals(TrainerType.ITEM)){
                return true;
            }
        }
        return false;
    }

    public abstract PokemonCard getNewActivePokemon();

    public abstract void evolvePokemon(EvolvedPokemonCard c);
}
