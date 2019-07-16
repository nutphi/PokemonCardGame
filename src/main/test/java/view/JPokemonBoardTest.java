package view;

import controller.JPokemonBoardController;
import model.Card;
import java.awt.*;
import java.awt.event.InputEvent;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by nutph on 6/7/2017.
 */
public class JPokemonBoardTest{
    public JPokemonBoardTest(){

    }

    /**
     * auto mouse; it will fail if you move your cursor
     */
    private Robot robot = null;

    @Override
    public void setUp(){

    }

    @Override
    public void tearDown(){

    }

    @org.junit.Test
    public void testPutACardFromHandToActiveBench() throws Exception {
        JPokemonBoardController controller = new JPokemonBoardController();
        controller.setUp("deck/deck2.txt", "deck/deck1.txt");

        ArrayList<Card> handCards = controller.getPlayerController().getPlayer().getPlayerHandCards();

        assertNull("before put a card",controller.getPlayerController().getPlayer().getActivePokemonCard());

        //wait
        Thread.sleep(2000);
        robot = new Robot();
        robot.createScreenCapture(new Rectangle(0,40,720,950));

        clickOk();

        //move to basic pokemon

        //pokemon no.7 count from right to left
        int cardPositionX = handCards.get(6).getLocationOnScreen().x;
        int cardPositionY = handCards.get(6).getLocationOnScreen().y;
        robot.mouseMove(cardPositionX,cardPositionY);


        //click left to skip info
        clickLeft();

        assertNotNull("has active",controller.getPlayerController().getPlayer().getActivePokemonCard());

        clickOk();

        clickOk();

        //card no.6 count from right to left
        cardPositionX = handCards.get(5).getLocationOnScreen().x;
        cardPositionY = handCards.get(5).getLocationOnScreen().y;
        robot.mouseMove(cardPositionX ,cardPositionY);

        clickLeft();

        //card no.5 count from right to left
        cardPositionX = handCards.get(4).getLocationOnScreen().x;
        cardPositionY = handCards.get(4).getLocationOnScreen().y;
        robot.mouseMove(cardPositionX ,cardPositionY);

        clickLeft();


        //done button index 0 (because i added it on board first)
        int x = JPokemonBoard.getBoard().getJLabelBoard().getComponent(0).getX();
        int y = JPokemonBoard.getBoard().getJLabelBoard().getComponent(0).getY();
        robot.mouseMove(x+15,y+40);

        //retreat button index 1(because i added it on board second)

        clickLeft();


        clickOk();
        Thread.sleep(2000);
        clickOk();
        Thread.sleep(2000);
        clickOk();
        Thread.sleep(3000);


        assertTrue("has 2 pokemon cards on bench",controller.getPlayerController().getPlayer().getBenchPokemonCards().size()==2);
    }


    public void clickOk(){
        robot.mouseMove(360,550);
        //click left
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(300);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(300);

    }

    public void clickLeft(){
        //click left
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(300);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(300);

    }
}
