package controller;

import model.Player;
import model.PlayerState;
import view.JPokemonBoard;

import javax.swing.*;
import java.awt.event.*;

/**
 * This listener is only used with the done button at the left-bottom corner
 * Created by nutph on 5/10/2017.
 */
public class EndStateMouseListener implements MouseListener {

    JPokemonBoardController boardController;

    /**
     * Default contructor
     * @param boardController we put JPokemonBoardController on the listener
     *                       to control the state of the game.
     */
    public EndStateMouseListener (JPokemonBoardController boardController){
        this.boardController = boardController;
    }

    /**
     * When clicked on done button, we change from state of HumanPlayer to ComputerPlayer.
     * @param e MouseEvent
     */
    public void mouseClicked(MouseEvent e) {
        Player player = boardController.getPlayerController().getPlayer();
        if(player.getState().equals(PlayerState.START_BENCH)||
                player.getState().equals(PlayerState.ENERGY_SUPPORTER_ITEM)){
            synchronized (player.getState()){
                player.getState().notify();
            }
            player.changeNewToOldPokemon();
        }else {
            int out = JOptionPane.showConfirmDialog(JPokemonBoard.getBoard().getJLabelBoard(),"are you sure?");
            if(out==JOptionPane.OK_OPTION){
                player.setState(PlayerState.DONE);
                boardController.mouseAction(null);
            }
        }

    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
