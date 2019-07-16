package model;

/**
 * Created by nutph on 5/6/2017.
 */
public enum CardType {
    POKEMON,ENERGY,TRAINER;
    public static CardType valueOf2(String name){
        return valueOf(name.toUpperCase());
    }
}
