package controller;

import javax.swing.*;

/**
 * Created by nutph on 6/23/2017.
 */
public class MockUpJPokemonBoardController extends JPokemonBoardController{
    public MockUpJPokemonBoardController(HumanPlayerController playerController, ComputerPlayerController opponentController){
        super(playerController,opponentController);
    }

    @Override
    protected void showMessageDialog(String message, String title){

    }

    @Override
    protected int showOptionDialog(String message,String title,int optionType, int messageType, Object[] option, Object initialValue){
        if(option.length>0)
            return 0;
        else
            return JOptionPane.CLOSED_OPTION;
    }

    @Override
    protected int showConfirmDialog(String message,String title,int optionType){
        return JOptionPane.OK_OPTION;
    }
}
