package controller;

import model.*;
import view.BoardPosition;
import view.JPokemonBoard;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created by nutph on 5/18/2017.
 */
public class RetreatBtnListener implements MouseListener {
    private HumanPlayerController playerController;
    public RetreatBtnListener(HumanPlayerController playerController){
        this.playerController = playerController;
    }

    public HumanPlayerController getPlayerController() {
        return playerController;
    }

    public void setPlayerController(HumanPlayerController playerController) {
        this.playerController = playerController;
    }

    /**
     * when you click pokemonCard
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
        Player player = playerController.getPlayer();
        PokemonCard active = player.getActivePokemonCard();
        if(active.getStatus().contains(PokemonStatus.PARALYZED)){
            JPokemonBoard.getBoard().showMessageDialog("you can't retreat because you are paralyzed","STATUS PARALYZED");
            return;
        }
        else if(active.getStatus().contains(PokemonStatus.ASLEEP)){
            JPokemonBoard.getBoard().showMessageDialog("you can't retreat because you are asleep","STATUS PARALYZED");
            return;
        }
        else if(active.getStatus().contains(PokemonStatus.STUCK)){
            JPokemonBoard.getBoard().showMessageDialog("you can't retreat because you are stuck","STATUS PARALYZED");
            return;
        }
        int out = JOptionPane.showConfirmDialog(JPokemonBoard.getBoard().getJLabelBoard(),"are you sure?");
        if(out==JOptionPane.OK_OPTION) {
            ArrayList<EnergyType> needRetreatEnergies = active.getRetreat();
            if(active.getEnergies().size()>=needRetreatEnergies.size()){
                int energy_choice;
                boolean isCancel=false;
                ArrayList<EnergyCard> retreatEnergyCards = new ArrayList<EnergyCard>();
                for(int i=needRetreatEnergies.size();i>0;--i){
                    ArrayList<EnergyType> activeEnergyTypes = active.getPokemonEnergyTypes();
                    energy_choice=JPokemonBoard.getBoard().showOptionDialog("choose "+i+" energies to retreat","retreat"
                            , activeEnergyTypes.toArray(),null);
                    //when you choose pokemonEnergy
                    if(energy_choice!=-1){
                        EnergyType type = activeEnergyTypes.get(energy_choice);
                        retreatEnergyCards.add(active.removeEnergyCardFromEnergyType(type));
                    }else{
                        //when you click cancel on the top right, all will be rolled back
                        for(int j=0;j<retreatEnergyCards.size();++j){
                            active.getEnergies().add(retreatEnergyCards.get(j));
                        }
                        isCancel=true;
                        break;
                    }
                }
                if(!isCancel) {
                    //swap pokemon
                    int pokemonBenchIndex=JPokemonBoard.getBoard().showOptionDialog("choose a pokemon to change","retreat"
                            , playerController.getPokemonNameArrayOnBench(),null);
                    PokemonCard swapPokemonCard = player.getBenchPokemonCards().get(pokemonBenchIndex);
                    try {
                        player.getBenchPokemonCards().remove(swapPokemonCard);
                        player.getBenchPokemonCards().add(active);
                        player.setActivePokemonCard(swapPokemonCard);
                        JPokemonBoard.getBoard().movePokemonOnGroupPositionBoard(BoardPosition.BENCH_POKEMON,active
                                ,player.getBenchPokemonCards(),false,true);
                        JPokemonBoard.getBoard().movePokemonOnOnePositionBoard(BoardPosition.ACTIVE_POKEMON,swapPokemonCard
                                ,false);

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }

                    for (int i = 0; i < retreatEnergyCards.size(); i++) {
                        try {
                            JPokemonBoard.getBoard().setOnePositionBoard(BoardPosition.TRASH, retreatEnergyCards.get(i), false, false);
                            player.getTrash().push(retreatEnergyCards.get(i));
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }else{
                JOptionPane.showMessageDialog(JPokemonBoard.getBoard().getJLabelBoard(),
                        active.getName()+" does not have enough energies to retreat");
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
