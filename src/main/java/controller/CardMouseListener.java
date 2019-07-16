package controller;

import model.Card;
import model.CardType;
import model.EvolvedPokemonCard;
import model.PokemonCard;
import model.ability.DeckAbility;
import view.BoardPosition;
import view.JPokemonBoard;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

/**
 * Created by nutph on 5/7/2017.
 */
public class CardMouseListener implements MouseInputListener {
    private Card c;
    private JPokemonBoardController boardController;
    private boolean isShowTrash;
    /**
     * Constructor
     * @param c any cards on the game (Pokemon, Trainer and Energy Cards)
     * @param boardController
     */
    public CardMouseListener(Card c,JPokemonBoardController boardController){
        this.c=c;
        this.boardController=boardController;
        isShowTrash=false;
    }

    public boolean isShowTrash() {
        return isShowTrash;
    }

    public void setShowTrash(boolean showTrash) {
        isShowTrash = showTrash;
    }

    public Card getCard(){
        return c;
    }

    /**
     * When the mouse is right clicked, show card's information.
     * When the mouse is left clicked, it will move according to a state of the game.
     * @param e MouseEvent
     */
    public void mouseClicked(MouseEvent e){
        if(SwingUtilities.isRightMouseButton(e))//&&c.isVisibleCard()
        {
            JOptionPane.showMessageDialog(null, new JLabel(),c.getName()+ " detail",
                    JOptionPane.PLAIN_MESSAGE, c.getBigVisibleCardImageIcon());
        }
        //search ability
        else if(c.getAbilityAction().equals(Card.AbilityAction.SEARCH_DECK_ACTIVE)){
            JPokemonBoard.getBoard().getJLabelBoard().add(c);
            boardController.moveCardToHand(c,false);
            c.setAbilityAction(Card.AbilityAction.NON_ACTIVE);
        }else if(c.getAbilityAction().equals(Card.AbilityAction.SEARCH_TRASH_ACTIVE)){
            JPokemonBoard.getBoard().getJLabelBoard().add(c);
            boardController.moveCardToHand(c,false);
            c.setAbilityAction(Card.AbilityAction.NON_ACTIVE);
        }
        else if(c.getAbilityAction().equals(Card.AbilityAction.DISCARD)){
            JPokemonBoard.getBoard().getJLabelBoard().add(c);
            try {
                ArrayList<Card> cards = new ArrayList();
                cards.add(c);
                boardController.moveCardFromHandToDest(cards, DeckAbility.Destination.DISCARD, false);
                c.setAbilityAction(Card.AbilityAction.NON_ACTIVE);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }else if(c.getAbilityAction().equals(Card.AbilityAction.BOTTOM_DECK)) {
            JPokemonBoard.getBoard().getJLabelBoard().add(c);
            try {
                ArrayList<Card> cards = new ArrayList();
                cards.add(c);
                boardController.moveCardFromHandToDest(cards, DeckAbility.Destination.BOTTOM_DECK, false);
                c.setAbilityAction(Card.AbilityAction.NON_ACTIVE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else if(c.getAbilityAction().equals(Card.AbilityAction.TOP_DECK)) {
            JPokemonBoard.getBoard().getJLabelBoard().add(c);
            try {
                ArrayList<Card> cards = new ArrayList();
                cards.add(c);
                boardController.moveCardFromHandToDest(cards, DeckAbility.Destination.TOP_DECK, false);
                c.setAbilityAction(Card.AbilityAction.NON_ACTIVE);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else if(c.getAbilityAction().equals(Card.AbilityAction.SEARCH_DECK_EVOLVE_ACTIVE)){
            JPokemonBoard.getBoard().getJLabelBoard().add(c);
            if(c instanceof EvolvedPokemonCard) {
                try {
                    boardController.getAbilityController().moveCardOnDeckToEvolve((EvolvedPokemonCard) c);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
            c.setAbilityAction(Card.AbilityAction.NON_ACTIVE);
        }



        else if(boardController.getPlayerController().getPlayer().getTrash().contains(c)){
            try {
                boardController.showCardsOnTrash(false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }else if(boardController.getOpponentController().getPlayer().getTrash().contains(c)){
            try {
                boardController.showCardsOnTrash(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }else if(boardController.getPlayerController().getPlayer().getDeck().contains(c)){
            //click on player's deck (nothing)
            try {
                boardController.showCardsOnDeck(false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }else if(boardController.getOpponentController().getPlayer().getDeck().contains(c)){
            //click on opponent's deck (nothing)
            try {
                boardController.showCardsOnDeck(true);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }else if(boardController.getPlayerController().getPlayer().getBenchPokemonCards().contains(c)){
            //click on player's bench (nothing)
            System.out.println("player's reward cards");

        }else if(boardController.getOpponentController().getPlayer().getBenchPokemonCards().contains(c)){
            //click on opponent's bench (nothing)
        }else if(boardController.getPlayerController().getPlayer().getRewardCards().contains(c)){
            //click on player's reward cards (nothing)
            System.out.println("player's reward cards");
        }else if(boardController.getOpponentController().getPlayer().getRewardCards().contains(c)){
            //click on opponent's reward cards (nothing)
            System.out.println("opponent's reward cards");
        }else if(c.getCardType().equals(CardType.ENERGY)&&
                !boardController.getPlayerController().getPlayer().getPlayerHandCards().contains(c)){
            //click on energy cards on board (nothing)
            System.out.println("energy cards on board");
        }else if(c.getCardType().equals(CardType.TRAINER)&&
                !boardController.getPlayerController().getPlayer().getPlayerHandCards().contains(c)){
            //click on trainer cards on board (nothing)
            System.out.println("trainer cards on board");
        }else {
            boardController.mouseAction(c);
        }
    }

    /**
     * Not used yet
     * @param e MouseEvent
     */
    public void mousePressed(MouseEvent e) {
        //System.out.println("mouse pressed");
    }

    /**
     * Not used yet
     * @param e MouseEvent
     */
    public void mouseReleased(MouseEvent e) {
        //System.out.println("mouse released");
    }

    /**
     * Hand Card will be moved up while the mouse moves over.
     * @param e MouseEvent
     */
    public void mouseEntered(MouseEvent e) {
        try {
            int y = BoardPosition.getGroupPosition(BoardPosition.HAND, false);
            if (y == c.getY()) {
                c.setLocation(c.getLocation().x, c.getLocation().y - 30);
            }
            if (c.getLocation().equals(BoardPosition.getOnePosition(BoardPosition.TRASH, false)) ||
                    c.getLocation().equals(BoardPosition.getOnePosition(BoardPosition.TRASH, true)))
                c.setToolTipText("<html>click left to see all cards on discard pile<br>click right to see the pokemon</html>");
            else if (c.getLocation().y==BoardPosition.getGroupPosition(BoardPosition.HAND, false)-30){
                c.setToolTipText("<html>I have " + boardController.getPlayerController().getPlayer().getPlayerHandCards().size() + " cards on hand</html>");
            }else if(c.getLocation().y==BoardPosition.getGroupPosition(BoardPosition.HAND, true)) {
                c.setToolTipText("<html>Computer has " + boardController.getOpponentController().getPlayer().getPlayerHandCards().size() + " cards on hand</html>");
            }else if(c.getLocation().equals(BoardPosition.getOnePosition(BoardPosition.DECK, false))){
                c.setToolTipText("<html>There are " + boardController.getPlayerController().getPlayer().getDeck().size() + " cards on my deck</html>");
            }else if(c.getLocation().equals(BoardPosition.getOnePosition(BoardPosition.DECK, true))){
                c.setToolTipText("<html>There are " + boardController.getOpponentController().getPlayer().getDeck().size() + " cards on opponent's deck</html>");
            }else if(Arrays.asList(BoardPosition.getRewardPosition(false)).contains(c.getLocation())){
                c.setToolTipText("<html>There are " + boardController.getPlayerController().getPlayer().getRewardCards().size() + " prize cards left</html>");
            }else if(Arrays.asList(BoardPosition.getRewardPosition(true)).contains(c.getLocation())){
                c.setToolTipText("<html>There are " + boardController.getOpponentController().getPlayer().getRewardCards().size() + " prize cards left</html>");
            }else if(c.getLocation().equals(BoardPosition.getOnePosition(BoardPosition.ACTIVE_POKEMON, true))){
                PokemonCard pokemonCard = (PokemonCard)c;
                c.setToolTipText("<html>"+pokemonCard.getName()+" "+(pokemonCard.getHitpoint()-pokemonCard.getDamage())+"/"+pokemonCard.getHitpoint()+"</html>");
            }else if(c.getLocation().equals(BoardPosition.getOnePosition(BoardPosition.ACTIVE_POKEMON, false))){
                PokemonCard pokemonCard = (PokemonCard)c;
                c.setToolTipText("<html>"+pokemonCard.getName()+" "+(pokemonCard.getHitpoint()-pokemonCard.getDamage())+"/"+pokemonCard.getHitpoint()+"</html>");
            }else if(c.getLocation().y==BoardPosition.getGroupPosition(BoardPosition.BENCH_POKEMON, true)){
                PokemonCard pokemonCard = (PokemonCard)c;
                c.setToolTipText("<html>"+pokemonCard.getName()+" "+(pokemonCard.getHitpoint()-pokemonCard.getDamage())+"/"+pokemonCard.getHitpoint()+"</html>");
            }else if(c.getLocation().y==BoardPosition.getGroupPosition(BoardPosition.BENCH_POKEMON, false)){
                PokemonCard pokemonCard = (PokemonCard)c;
                c.setToolTipText("<html>"+pokemonCard.getName()+" "+(pokemonCard.getHitpoint()-pokemonCard.getDamage())+"/"+pokemonCard.getHitpoint()+"</html>");
            }else{
                c.setToolTipText("");
            }

        }catch(Exception ex){


        }
        //System.out.println("mouse entered");
    }

    /**
     * Hand Card will be moved down while the mouse moves out.
     * @param e MouseEvent
     */
    public void mouseExited(MouseEvent e) {
        try {
            int y = BoardPosition.getGroupPosition(BoardPosition.HAND, false);
            if (y-30==c.getY()) {
                c.setLocation(c.getLocation().x,c.getLocation().y+30);
            }
        }catch(Exception ex){
        }
        //System.out.println("mouse exited");
    }

    /**
     * Not used yet
     * @param e MouseEvent
     */
    public void mouseDragged(MouseEvent e) {
        //System.out.println("mouse dragged");
    }

    /**
     * Not used yet
     * @param e MouseEvent
     */
    public void mouseMoved(MouseEvent e) {
        //System.out.println("mouse moved");
        //c.setLocation(e.getX(),e.getY());
    }
}