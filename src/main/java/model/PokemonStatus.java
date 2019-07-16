package model;

/**
 * Created by nutph on 5/6/2017.
 */
public enum PokemonStatus {

    PARALYZED("&#x2601;"),ASLEEP("zzZ"),STUCK("&#x2605;"),POISONED("&#x2620;");

    PokemonStatus(String val){
        value = val;
    }
    final String value;
}
