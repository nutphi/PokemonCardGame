package view;

import java.awt.*;

/**
 * Created by nutph on 5/7/2017.
 */
public enum BoardPosition {
    HAND, ACTIVE_POKEMON, BENCH_POKEMON, DECK, TRASH, ITEM, STADIUM, REWARD;

    static final Point playerDeckPosition = new Point(625,536);
    static final Point playerTrashPosition = new Point(625,645);
    static final Point playerActivePokemonPosition = new Point(325,505);

    static final int playerHandY = 850;
    static final int playerBenchY = 700;

    static final Point opponentDeckPosition = new Point(26,317);
    static final Point opponentTrashPosition = new Point(26,206);
    static final Point opponentActivePokemonPosition = new Point(325,346);

    static final int opponentHandY = 0;
    static final int opponentBenchY = 150;

    static final Point[] playerReward = {new Point(27,512),new Point(27,617),new Point(27,722),
            new Point(53,520),new Point(53,625),new Point(53,730)};

    static final Point[] opponentReward = {new Point(624,134),new Point(624,239),new Point(624,344),
            new Point(599,124),new Point(599,229),new Point(599,334)};

    static final Point retreatPosition = new Point(500,550);

    public static Point[] getRewardPosition(boolean isOpponent){
        return isOpponent?opponentReward:playerReward;
    }

    public static int getGroupPosition(BoardPosition p, boolean isOpponent) throws Exception
    {
        if(p.equals(HAND)&&!isOpponent){
            return playerHandY;
        }else if(p.equals(BENCH_POKEMON)&&!isOpponent){
            return playerBenchY;
        }
        if(p.equals(HAND)&&isOpponent){
            return opponentHandY;
        }else if(p.equals(BENCH_POKEMON)&&isOpponent){
            return opponentBenchY;
        }else {
            throw new Exception("only hand and bench");
        }
    }

    public static Point getOnePosition(BoardPosition p, boolean isOpponent) throws Exception
    {
        if(p.equals(ACTIVE_POKEMON)&&!isOpponent){
            return playerActivePokemonPosition;
        }else if(p.equals(DECK)&&!isOpponent){
            return playerDeckPosition;
        }else if(p.equals(TRASH)&&!isOpponent){
            return playerTrashPosition;
        }

        if(p.equals(ACTIVE_POKEMON)&&isOpponent){
            return opponentActivePokemonPosition;
        }else if(p.equals(DECK)&&isOpponent){
            return opponentDeckPosition;
        }else if(p.equals(TRASH)&&isOpponent){
            return opponentTrashPosition;
        }
        else {
            throw new Exception("only active,deck,trash");
        }
    }
}
