package view;

import java.awt.*;

/**
 * Created by nutph on 5/7/2017.
 */
public enum BoardPosition {
    HAND, ACTIVE_POKEMON, BENCH_POKEMON, DECK, TRASH, ITEM, STADIUM, REWARD;

    static final Point playerDeckPosition = new Point(625,(int)(536 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY);
    static final Point playerTrashPosition = new Point(625,(int)(645 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY);
    static final Point playerActivePokemonPosition = new Point(325,(int)(505 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY);

    static final int playerHandY = (int)(850 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY;
    static final int playerBenchY = (int)(700 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY;

    static final Point opponentDeckPosition = new Point(26,(int)(317 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY);
    static final Point opponentTrashPosition = new Point(26,(int)(206 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY);
    static final Point opponentActivePokemonPosition = new Point(325,(int)(346 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY);

    static final int opponentHandY = 0;
    static final int opponentBenchY = (int)(150 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY;

    static final Point[] playerReward = {new Point(27,(int)(512 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY),new Point(27,(int)(617 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY),new Point(27,(int)(722 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY),
            new Point(53,(int)(520 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY),new Point(53,(int)(625 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY),new Point(53,(int)(730 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY)};

    static final Point[] opponentReward = {new Point(624,(int)(134 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY),new Point(624,(int)(239 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY),new Point(624,(int)(344 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY),
            new Point(599,(int)(124 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY),new Point(599,(int)(229 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY),new Point(599,(int)(334 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY)};

    static final Point retreatPosition = new Point(500,(int)(550 * JPokemonBoard.ratioHeight) - JPokemonBoard.screenOffsetY);

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
