package model;

/**
 * Created by nutph on 5/6/2017.
 */
public enum EnergyType {
    WATER(0),LIGHTNING(1),PSYCHIC(2),FIGHT(3),COLORLESS(4);
    EnergyType(int n){
        value=n;
    }

    public static EnergyType valueOf2(String name){
        return valueOf(name.toUpperCase());
    }

    public static boolean hasMore(String name){
        if(name.equals("cat")){//category
            return true;
        }
        return false;
    }

    public final int value;
}
