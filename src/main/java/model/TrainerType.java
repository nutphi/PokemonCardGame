package model;

/**
 * Created by nutph on 5/4/2017.
 */
public enum TrainerType {
    STADIUM,SUPPORTER,ITEM;
    public static TrainerType valueOf2(String name){
        return valueOf(name.toUpperCase());
    }
}
