package view;

import controller.CardMouseListener;
import controller.EndStateMouseListener;
import controller.JPokemonBoardController;
import controller.RetreatBtnListener;
import model.*;

import javax.swing.*;

import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * This is the class for board GUI
 * We use singleton design pattern
 * Created by nutph on 5/6/2017.
 */
public class JPokemonBoard{
    static final int screenWidth = 720;
    static final int screenHeight = 950;
    static final int cardWidth = 70;
    static final int cardHeight = 100;
    static final int benchWidth = 470;
    static final int benchStartOpponentX = 110;
    static final int benchStartOpponentY = 150;
    static final int benchStartPlayerX = 135;
    static final int benchStartPlayerY = 700;

    /**
     * this class is to get all position of cards on hand or bench
     * @param cardNumber number of cards on hand
     * @param isBench true if card on bench; false if card on hand
     * @param isOpponent true to get opponent position; false to get player position.
     * @return return array of position (x,y)
     */
    public static ArrayList<Point> handBenchPosition(int cardNumber, boolean isBench, boolean isOpponent)
    {
        ArrayList<Point> position = new ArrayList();
        if(isOpponent&&!isBench) {
            //System.out.println("opponent hand cards");
            for (int i = cardNumber; i > 0; i--) {
                position.add(new Point(i * (screenWidth-cardWidth)/(1+cardNumber)-cardWidth/2, 0));
            }
        }else if(isOpponent&&isBench){
            //System.out.println("opponent bench cards");
            for (int i = cardNumber; i > 0; i--) {
                position.add(new Point(((cardNumber-i) * benchWidth/cardNumber)+benchStartOpponentX, benchStartOpponentY));
            }
        }else if(!isOpponent&&!isBench){
            //System.out.println("player hand cards");
            for (int i = cardNumber; i > 0; i--) {
                position.add(new Point(i * (screenWidth-cardWidth)/(1+cardNumber)-cardWidth/2, screenHeight-cardHeight));
            }
        }else{
            //System.out.println("player bench cards");
            for (int i = cardNumber; i > 0; i--) {
                position.add(new Point(((cardNumber-i) * benchWidth/cardNumber)+benchStartPlayerX, benchStartPlayerY));
            }
        }
        return position;
    }


    private static JPokemonBoard instance;
    private JFrame screen;
    private JLabel board;
    private JButton doneBtn;
    private JButton retreatBtn;
    private JDialog dialog;
    private JOptionPane popup;

    /**
     * Get the board
     * @return return object of board's JLabel
     */
    public JLabel getJLabelBoard()
    {
        return board;
    }

    /**
     * Default constructor to set all GUI components on board
     */
    private JPokemonBoard(){
        screen = new JFrame("Pokemon Trading Card Game");
        Image m = (new ImageIcon(getClass().getClassLoader().getResource("images/icon.png"))).getImage();
        screen.setIconImage(m);
        //'40' is the title on the top
        screen.setBounds(0,0,screenWidth,screenHeight+40);
        ImageIcon bgImg = new ImageIcon(getClass().getClassLoader().getResource("images/pokemon-board.jpg"));
        board = new JLabel();
        board.setIcon(bgImg);

        //three steps for non-layout
        board.setLayout(null);
        board.setBounds(0,0,screenWidth,screenHeight);
        board.repaint();
        //

        //set card on board
        screen.setVisible(true);
        screen.setResizable(false);
        screen.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        screen.add(board);
        board.setVisible(true);
        doneBtn = new JButton("done");
        doneBtn.setFont(new Font("Courier", Font.PLAIN,24));

        doneBtn.setBounds(screenWidth-100,screenHeight-195,90,90);
        doneBtn.setLayout(null);
        board.add(doneBtn);
        doneBtn.setVisible(false);

        retreatBtn = new JButton("Retreat");
        retreatBtn.setFont(new Font("Courier", Font.PLAIN,10));
        retreatBtn.setSize(70,30);
        retreatBtn.setLocation(BoardPosition.retreatPosition);
        board.add(retreatBtn);
        retreatBtn.setVisible(false);
    }

    public <T> void reorderCardLabel(ArrayList<T> pile, BoardPosition boardPosition){
        for(T c:pile){
            getJLabelBoard().remove((Card)c);
        }
        if(boardPosition.equals(BoardPosition.DECK)||boardPosition.equals(BoardPosition.TRASH)) {
            for (int i = pile.size() - 1; i >= 0; --i) {
                getJLabelBoard().add((Card) (pile.get(i)));
            }
        }else{
            for (int i = 0; i < pile.size(); ++i) {
                getJLabelBoard().add((Card) (pile.get(i)));
            }
        }
    }

    public void reorderPokemonOnBoard(PokemonCard pokemonCard){
        ArrayList<EnergyCard> energyCards= pokemonCard.getEnergies();
        ArrayList<TrainerCard> trainerCards = pokemonCard.getTools();
        getJLabelBoard().remove(pokemonCard);
        getJLabelBoard().add(pokemonCard);
        if(pokemonCard instanceof EvolvedPokemonCard){
            EvolvedPokemonCard evolvedPokemonCard=(EvolvedPokemonCard)pokemonCard;
            getJLabelBoard().remove(evolvedPokemonCard.getBasePokemon());
            getJLabelBoard().add(evolvedPokemonCard.getBasePokemon());
        }
        for (EnergyCard energyCard:
                energyCards) {
            getJLabelBoard().remove(energyCard);
            getJLabelBoard().add(energyCard);
        }
        for (TrainerCard trainerCard:trainerCards){
            getJLabelBoard().remove(trainerCard);
            getJLabelBoard().add(trainerCard);
        }

        board.repaint();
    }

    /**
     * To add mouse listener from JPokemonBoardController and set visible
     * after player set pokemon on bench (the second state of the game.)
     * @param boardController
     */
    public void setUpDoneBtn(JPokemonBoardController boardController){
        doneBtn.addMouseListener(new EndStateMouseListener(boardController));
        doneBtn.setVisible(true);
    }

    public void setUpRetreatBtn(RetreatBtnListener retreatBtnListener){
        retreatBtn.addMouseListener(retreatBtnListener);
    }

    public void showRetreatBtn(){
        retreatBtn.setVisible(true);
    }

    public void hideRetreatBtn(){
        retreatBtn.setVisible(false);
    }

    /**
     * To get the static class object
     * @return object of JPokemonBoard
     */
    public static JPokemonBoard getBoard(){
        if(instance==null){
            instance=new JPokemonBoard();
        }
        return instance;
    }


    /**
     * put a card to one position board (active area, trash or deck)
     * @param position BoardPosition BoardPosition.DECK
     * @param card card
     * @param isOppenent if opponent, set opponent card GUI
     * @param start if start game, to set each card on the deck
     * @throws Exception does not work for Group BoardPosition position
     * (e.g. BoardPosition.hand, BoardPosition.benchPokemon)
     */
    public void setOnePositionBoard(BoardPosition position, Card card, boolean isOppenent, boolean start)
            throws Exception{
        if(position.equals(position==BoardPosition.BENCH_POKEMON||position==BoardPosition.HAND))
        {
            throw new Exception("Don't use setOnePositionBoard,Use setGroupPositionBoard");
        }else if(start)
        {
            //to set deck
            board.add(card);
            card.setVisible(true);
            Point p = BoardPosition.getOnePosition(position, isOppenent);
            card.setLocation(p);
        }
        else
        {
            Point p = BoardPosition.getOnePosition(position, isOppenent);
            CardMove a = new CardMove(card,p);
            a.run();
            getJLabelBoard().remove(card);
            getJLabelBoard().add(card);
            card.setLocation(p);
        }
        board.repaint();
    }

    //put card to pile (e.g. put a card on active pokemon to hand (with group of cards), put a card on hand to bench)


    /**
     * put a card to group position board (hand, bench)
     * @param position group position (hand,bench) that you want the cards on.
     * @param pile hand or bench cards that already added cards on it (e.g. putACardFromDeckToHand)
     * @param isOppenent if the move is opponent, return true
     * @param <T> Use Card or PokemonCard
     */
    public <T> void setGroupPositionBoard(BoardPosition position, ArrayList<T> pile, boolean isOppenent, boolean isVisible)
            throws Exception {
        ArrayList<Point> groupPosition;
        if(position.equals(BoardPosition.BENCH_POKEMON))
        {
            groupPosition = handBenchPosition(pile.size()+1,true,isOppenent);
            reorderCardLabel(pile,BoardPosition.BENCH_POKEMON);
        }
        else if(position.equals(BoardPosition.HAND))
        {
            groupPosition = handBenchPosition(pile.size()+1,false,isOppenent);
            reorderCardLabel(pile,BoardPosition.HAND);
        }
        else
        {
            throw new Exception("Use putCardOnOnePositionBoard");
        }

        for(int i=0;i<pile.size();i++)
        {
            Card c =(Card)(pile.get(i));
            c.setVisibleCard(isVisible);
            CardMove a = new CardMove(c,groupPosition.get(i));
            a.run();
            c.setLocation(groupPosition.get(i));

            if(position.equals(BoardPosition.BENCH_POKEMON)){
                ArrayList<EnergyCard> energyCards =((PokemonCard)c).getEnergies();
                if(energyCards!=null && energyCards.size()>0)
                    setEnergyCardOnPokemon(energyCards.get(energyCards.size()-1),(PokemonCard)c,isOppenent);
                if(c instanceof EvolvedPokemonCard)
                {
                    ((EvolvedPokemonCard) c).getBasePokemon().setLocation(c.getLocation());
                }
            }
        }
        board.repaint();
    }

    /**
     * put energy card to pokemon on bench or active pokemon
     * @param e Energy card that will be put on pokemon
     * @param p Pokemon card that will put Energy card
     * @param isOpponent if opponent, set opponent GUI
     */
    public void setEnergyCardOnPokemon(EnergyCard e, PokemonCard p, boolean isOpponent){
        CardMove a = new CardMove(e,p.getLocation());
        a.run();
        if(isOpponent)
            e.setVisibleCard(true);
        for(int i=0;i<p.getEnergies().size();i++) {
            getJLabelBoard().remove(p.getEnergies().get(i));
            getJLabelBoard().add(p.getEnergies().get(i));
            int var = isOpponent ? -10 : 10;
            int x = p.getX() + (var * (i+1));
            int y = p.getY() + (var * (i+1));
            p.getEnergies().get(i).setLocation(x, y);
        }
        reorderPokemonOnBoard(p);
        board.repaint();
    }

    /**
     * put add ability as trainer card to pokemon on bench or active pokemon
     * @param t Trainer card that will be put on pokemon
     * @param p Pokemon card that will put Energy card
     * @param isOpponent if opponent, set opponent GUI
     */
    public void setItemCardOnPokemon(TrainerCard t, PokemonCard p, boolean isOpponent){
        CardMove a = new CardMove(t,p.getLocation());
        a.run();
        if(isOpponent)
            t.setVisibleCard(true);
        for(int i=0;i<p.getTools().size();i++) {
            getJLabelBoard().remove(p.getTools().get(i));
            getJLabelBoard().add(p.getTools().get(i));
            int var = !isOpponent ? -10 : 10;
            int x = p.getX() + (var * (i+1));
            int y = p.getY() + (var * (i+1));
            p.getTools().get(i).setLocation(x, y);
        }
        reorderPokemonOnBoard(p);
        board.repaint();
    }


    /**
     * put cards from deck to reward area
     * @param card A Card that will be put on reward area
     * @param times the current number of reward cards
     * @param number the maximum number of reward cards
     * @param isOpponent if opponent, set opponent GUI
     */
    public void putRewardOnBoard(Card card, int times, int number, boolean isOpponent){
        if(!isOpponent) {
            //System.out.println("player reward");
            //change i and change for good alignment
            moveCardFromCurrentPos2RewardArea(card, times, number);
        }else{
            //System.out.println("opponent reward");
            //change i and change for good alignment
            moveOpponentCardFromCurrentPos2RewardArea(card, times, number);
        }
        //board.repaint();
    }

    private void moveOpponentCardFromCurrentPos2RewardArea(Card card, int times, int number) {
        int i = (number>3?0:3)+times;
        CardMove a = new CardMove(card, BoardPosition.opponentReward[i]);
        a.run();
        getJLabelBoard().remove(card);
        getJLabelBoard().add(card);
        card.setLocation(BoardPosition.opponentReward[i]);
        board.repaint();
    }

    private void moveCardFromCurrentPos2RewardArea(Card card, int times, int number) {
        int i = (number>3?0:3)+times;
        CardMove a = new CardMove(card, BoardPosition.playerReward[i]);
        a.run();
        getJLabelBoard().remove(card);
        getJLabelBoard().add(card);
        card.setLocation(BoardPosition.playerReward[i]);
        board.repaint();
    }

    public void putEvolvedPokemonOnPokemon(EvolvedPokemonCard e, PokemonCard p){
        CardMove a = new CardMove(e,p.getLocation());
        a.run();
        e.setLocation(p.getLocation());
        getJLabelBoard().remove(p);
        getJLabelBoard().add(p);
        reorderPokemonOnBoard(p);
        board.repaint();
    }

    /**
     * Class to move card from one to another position
     */
    public class CardMove extends Thread{
        private Point to;
        private Card card;
        public CardMove(Card card, Point to)
        {
            this.to = to;
            this.card = card;
        }

        public void run() {

            int step= 5;
            for(int i=0;i<step;i++)
            {
                int x = Math.abs((card.getLocation().x-to.x)/step);
                int y = Math.abs((card.getLocation().y-to.y)/step);
                if(card.getLocation().x>=to.x){
                    x=card.getLocation().x-x;
                }else {
                    x=card.getLocation().x+x;
                }
                if(card.getLocation().y>=to.y){
                    y=card.getLocation().y-y;
                }else{
                    y=card.getLocation().y+y;
                }
                card.setLocation(x,y);
                try{
                    Thread.sleep(0,1);
                }catch(Exception e){e.printStackTrace();}
            }
        }
    }

    /**
     * show message on the board
     * @param message message that show on message dialog
     */
    public void showMessageDialog(String message, String title){
        JOptionPane popup = new JOptionPane(message,JOptionPane.PLAIN_MESSAGE,JOptionPane.CLOSED_OPTION);
        dialog = popup.createDialog(this.getJLabelBoard(),title);
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);

    }

    public int showOptionDialog(String message, String title, Object[] options, String initialValue){
        popup = new JOptionPane(message, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null,
                options, initialValue);
        dialog = popup.createDialog(this.getJLabelBoard(),title);
        //dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
        return Arrays.asList(popup.getOptions()).indexOf(popup.getValue());
    }

    public void turnOff(){
        screen.setVisible(false);
        screen.dispose();
        System.exit(0);
    }

    public void moveCardOnDeckTrash(BoardPosition position, ArrayList<Card> cards, boolean isOpponent) throws Exception {
        if(!position.equals(BoardPosition.TRASH)&&!position.equals(BoardPosition.DECK)){
            throw new Exception("only deck and trash position");
        }
        for(Card c:cards) {
            setOnePositionBoard(position, c, isOpponent, false);
            if(position.equals(BoardPosition.DECK)){
                c.setVisibleCard(false);
            }else{
                c.setVisibleCard(true);
            }
        }
        board.repaint();
    }

    public void movePokemonOnOnePositionBoard(BoardPosition position, PokemonCard pokemonCard, boolean isOpponent) throws Exception {
        if(position.equals(BoardPosition.ACTIVE_POKEMON)) {
            setOnePositionBoard(position, pokemonCard, isOpponent, false);
            if (pokemonCard instanceof EvolvedPokemonCard) {
                setOnePositionBoard(position, ((EvolvedPokemonCard) pokemonCard).getBasePokemon(), isOpponent, false);
            }
        }
        for (EnergyCard energyCard : pokemonCard.getEnergies()) {
            setEnergyCardOnPokemon(energyCard, pokemonCard, isOpponent);
        }

        for (TrainerCard trainerCard : pokemonCard.getTools()) {
            setItemCardOnPokemon(trainerCard, pokemonCard, isOpponent);
        }
        board.repaint();
    }

    public <T> void movePokemonOnGroupPositionBoard(BoardPosition position, PokemonCard pokemonCard, ArrayList<T> pile, boolean isOpponent, boolean isVisible) throws Exception {
        if(position.equals(BoardPosition.BENCH_POKEMON)) {
            setGroupPositionBoard(position, pile, isOpponent, isVisible);
            if (pokemonCard instanceof EvolvedPokemonCard) {
                putEvolvedPokemonOnPokemon((EvolvedPokemonCard)pokemonCard , ((EvolvedPokemonCard)pokemonCard).getBasePokemon());
            }
        }

        for (EnergyCard energyCard : pokemonCard.getEnergies()) {
            setEnergyCardOnPokemon(energyCard, pokemonCard, isOpponent);
        }

        for (TrainerCard trainerCard : pokemonCard.getTools()) {
            setItemCardOnPokemon(trainerCard, pokemonCard, isOpponent);
        }

        board.repaint();
    }

    public void showPileInformation(Stack<Card> pile, String title, BoardPosition position, boolean isOpponent) throws Exception {
        if(!position.equals(BoardPosition.TRASH)&&!position.equals(BoardPosition.DECK)){
            throw new Exception("use only for trash and deck");
        }

        int size = pile.size();
        JPanel j = new JPanel();
        int row = (size/10)+ size%10>1?0:1;
        GridLayout experimentLayout = new GridLayout(row,10);
        j.setLayout(experimentLayout);
        for (Card card:pile) {
            //used when you show deck card
            //card.setVisibleCard(true);
            ((CardMouseListener)card.getMouseListeners()[0]).setShowTrash(true);
            card.setVisibleCard(true);
            j.add(card);
        }

        JOptionPane.showMessageDialog(JPokemonBoard.getBoard().getJLabelBoard(), j
                , (isOpponent?"Opponent":"Player")+"'s "+title+" ="+size,JOptionPane.PLAIN_MESSAGE);

        for (int i=pile.size()-1;i>=0;--i) {
            Card card = pile.get(i);
            JPokemonBoard.getBoard().setOnePositionBoard(position,card,isOpponent,false);
            //used when you show deck card
            //card.setVisibleCard(false);
            ((CardMouseListener)card.getMouseListeners()[0]).setShowTrash(false);
        }
        board.repaint();
    }

    public void showCardListInformation(ArrayList<Card> pile,String message, String title, boolean isOpponent) throws Exception {
        int size = pile.size();
        JPanel j = new JPanel();
        int row = (size/10)+ size%10>1?0:1;
        GridLayout experimentLayout = new GridLayout(row,10);
        j.setLayout(experimentLayout);
        for (Card card:pile) {
            //used when you show deck card
            card.setVisibleCard(true);
            j.add(card);
        }
        popup = new JOptionPane(j,JOptionPane.PLAIN_MESSAGE,JOptionPane.NO_OPTION,null,null, null);
        dialog = popup.createDialog(JPokemonBoard.getBoard().getJLabelBoard()
                , (isOpponent?"Opponent":"Player")+"'s "+title+" ="+size);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);
    }


    public void closeDialog(BoardPosition boardPosition, boolean isOpponent) throws Exception {
        if(popup== null || popup.getMessage() instanceof String) {
            return;
        }
        Component[] cards = ((JPanel) popup.getMessage()).getComponents();
        if (boardPosition.equals(BoardPosition.DECK) || boardPosition.equals(BoardPosition.TRASH)) {
            for (int i = 0; i < cards.length; ++i) {
                Card card = (Card) cards[i];
                board.add(card);
                if (boardPosition.equals(BoardPosition.DECK)) {
                    card.setVisibleCard(false);
                }
                setOnePositionBoard(boardPosition, card, isOpponent, false);
            }
        }else{
            ArrayList<Card> cardList = new ArrayList<Card>();
            for(int i=0;i<cards.length;i++){
                cardList.add((Card)cards[i]);
            }
            setGroupPositionBoard(boardPosition, cardList, isOpponent, !isOpponent);
        }

        dialog.dispose();
    }
}