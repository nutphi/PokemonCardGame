package model;

/**
 * Created by nutph on 5/8/2017.
 */
public enum PlayerState {
    /**
     * START_ACTIVE = beginning of the game used to put pokemon on active
     * START_BENCH = beginning of the game used to put pokemon on bench
     *
     * ENERGY_SUPPORTER_ITEM = beginning of the game  after you put pokemon on bench
     *      and opponent put pokemon on bench, but no attack
     *      if you are the first, you can put one energy to a pokemon or use any trainer cards
     * ENERGY_ITEM = beginning of the game after you put pokemon on bench
     *      and opponent put pokemon on bench, after you put supporter, but no attack
     *      if you are the first, you can put one energy to a pokemon or use item cards
     * SUPPORTER_ITEM = beginning of the game after you put pokemon on bench
     *      and opponent put pokemon on bench, but no attack
     *      if you are the first, you can put one energy to a pokemon or use trainer card
     * ITEM = beginning of the game after you put energy to pokemon,
     *      you can only use item trainer card, but no attack
     *
     * POKEMON_ENERGY_SUPPORTER_ITEM = you can use any cards, and even attack
     * POKEMON_ENERGY_ITEM = you can use an energy card, item cards, and even attack
     * POKEMON_SUPPORTER_ITEM = you can use any item cards, and even attack, but not energy card
     * POKEMON_ITEM = after put energy card and supporter cards, you can use item card or attack.
     * DONE = finish your turn
     */
    START_ACTIVE, START_BENCH,
    ENERGY_SUPPORTER_ITEM, ENERGY_ITEM, SUPPORTER_ITEM, ITEM,
    POKEMON_ENERGY_SUPPORTER_ITEM, POKEMON_ENERGY_ITEM, POKEMON_SUPPORTER_ITEM, POKEMON_ITEM, DONE;
}
