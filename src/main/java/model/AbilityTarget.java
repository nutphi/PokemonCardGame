package model;

/**
 * Created by nutph on 5/6/2017.
 */
public enum AbilityTarget {
    YOUR_ACTIVE,OPPONENT_ACTIVE,//only active
    CHOICE_OPPONENT,CHOICE_YOUR,//choose active or one on bench
    CHOICE_OPPONENT_BENCH,CHOICE_YOUR_BENCH,//choice one on bench
    OPPONENT_BENCH, YOUR_BENCH,//all on bench
    YOU, OPPONENT,//hand, deck or discard pile
    SELF,//for the item
    LAST,//damTarget like the former one
    NONE;
}
