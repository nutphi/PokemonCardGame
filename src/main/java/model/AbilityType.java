package model;

/**
 * Created by nutph on 5/9/2017.
 */
public enum AbilityType{
    NULL(0),DAM(1),HEAL(2),DEENERGIZE(3),REENERGIZE(4),REDAMAGE(5),SWAP(6),DESTAT(7),APPLYSTAT(8),DRAW(9),
    SEARCH(10),DECK(11),SHUFFLE(15),COND(16),ADD(17);

    AbilityType(int n){
        value=n;
    }
    public int value;
    public static AbilityType valueOf2(String name){
        return valueOf(name.toUpperCase());
    }
}
