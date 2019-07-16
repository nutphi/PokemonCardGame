package model.ability.extra;

import controller.JPokemonBoardController;
import model.EnergyCard;
import model.EnergyType;
import model.Player;
import model.PokemonCard;

import java.util.ArrayList;

/**
 * calculation of count(), greater and multiply operator
 * Created by nutph on 6/6/2017.
 */
public class Calculation {
    public enum CountTarget {
        NULL,YOUR_BENCH, YOUR_ACTIVE_ENERGY, YOUR_ACTIVE_ENERGY_PSYCHIC, LAST_SOURCE_DAMAGE, OPPONENT_ACTIVE_ENERGY,
        YOUR_ACTIVE_DAMAGE,YOUR_HAND,OPPONENT_HAND
    }

    public enum Operator{
        NULL,MULTIPLY,GREATER;
    }

    private CountTarget target;
    private Operator operator;

    public Calculation(){
        this.target = CountTarget.NULL;
        this.operator = Operator.NULL;
    }
    public Calculation(CountTarget target,Operator operator){
        this.target = target;
        this.operator = operator;
    }

    public CountTarget getTarget() {
        return target;
    }

    public void setTarget(CountTarget target) {
        this.target = target;
    }

    public Operator getOperator() {
        return operator;
    }

    public void setOperator(Operator operator) {
        this.operator = operator;
    }

    public boolean isGreater(JPokemonBoardController controller, int amount, boolean isOpponent){
        int count = getCount(controller,isOpponent);
        if(count>amount){
            return true;
        }else{
            return false;
        }
    }

    public int calculateAmount(JPokemonBoardController controller, int amount, boolean isOpponent){
        if(getTarget().equals(CountTarget.NULL)){
            //not count
            return amount;
        }else {
            //count
            int count = getCount(controller, isOpponent);
            if (operator.equals(Operator.MULTIPLY)) {
                amount = amount * count;
            } else {
                amount = count;
            }
            return amount;
        }
    }

    public int getCountLastSourceDamage(PokemonCard pokemonCard){
        return pokemonCard.getDamage()/10;
    }

    private int getCount(JPokemonBoardController controller, boolean isOpponent){
        Player opponent = controller.getOpponentController().getPlayer();
        Player player = controller.getPlayerController().getPlayer();
        int count = 0;
        if(target.equals(CountTarget.YOUR_ACTIVE_DAMAGE)) {
            if (isOpponent) {
                count = opponent.getActivePokemonCard().getDamage()/10;
            } else {
                count = player.getActivePokemonCard().getDamage()/10;
            }
        }else if(target.equals(CountTarget.YOUR_ACTIVE_ENERGY)) {
            if (isOpponent) {
                count = opponent.getActivePokemonCard().getEnergies().size();
            } else {
                count = player.getActivePokemonCard().getEnergies().size();
            }
        }else if(target.equals(CountTarget.OPPONENT_ACTIVE_ENERGY)){
            if (!isOpponent) {
                count = opponent.getActivePokemonCard().getEnergies().size();
            } else {
                count = player.getActivePokemonCard().getEnergies().size();
            }
        }else if(target.equals(CountTarget.YOUR_BENCH)){
            if(isOpponent){
                count = opponent.getBenchPokemonCards().size();
            }else{
                count = player.getBenchPokemonCards().size();
            }
        }else if(target.equals(CountTarget.OPPONENT_HAND)){
            if(!isOpponent){
                count = opponent.getPlayerHandCards().size();
            }else{
                count = player.getPlayerHandCards().size();
            }
        }else if(target.equals(CountTarget.YOUR_HAND)){
            if(isOpponent){
                count = opponent.getPlayerHandCards().size();
            }else{
                count = player.getPlayerHandCards().size();
            }
        }else if(target.equals(Calculation.CountTarget.YOUR_ACTIVE_ENERGY_PSYCHIC)){
            ArrayList<EnergyCard> energyCardArrayList;
            if(isOpponent){
                energyCardArrayList = controller.getOpponentController().getPlayer().getActivePokemonCard().getEnergies();
            }else{
                energyCardArrayList = controller.getPlayerController().getPlayer().getActivePokemonCard().getEnergies();
            }
            for(EnergyCard e:energyCardArrayList){
                if(e.getEnergyType().equals(EnergyType.PSYCHIC)){
                    count++;
                }
            }
        }
        return count;
    }
}