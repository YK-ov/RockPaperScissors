import game.Duel;
import game.Gesture;
import game.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    @Test
    void playerTest() {
        Player playerOne = new Player();
        Player playerTwo = new Player();

        Duel duel = new Duel(playerOne, playerTwo);
        boolean isDuellingTest = true;
        boolean valueOne;
        boolean valueTwo;
        valueOne = playerOne.isDuelling();
        valueTwo = playerTwo.isDuelling();

        assertEquals(isDuellingTest, valueOne);
        assertEquals(isDuellingTest, valueTwo);
    }

    @Test
    void evaluateWinnerTest() {
    Player playerOne = new Player();
    Player playerTwo = new Player();

    Duel duel = new Duel(playerOne, playerTwo);



    Duel.Result firstPlayerWon = new Duel.Result(playerOne, playerTwo);

    playerOne.makeGesture(Gesture.ROCK);
    playerTwo.makeGesture(Gesture.SCISSORS); //without duel.handleGesture (should be written in makeGesture method already)

    Duel.Result actualTest = duel.evaluate();

    assertEquals(firstPlayerWon, actualTest);
    }

    @Test
    void evaluateTieTest() {
    Player playerOne = new Player();
    Player playerTwo = new Player();

    Duel duel = new Duel(playerOne, playerTwo);

    playerOne.makeGesture(Gesture.ROCK);
    playerTwo.makeGesture(Gesture.ROCK);


    Duel.Result expectedTie = null;

    Duel.Result actualTest = duel.evaluate();

    assertEquals(expectedTie, actualTest);
    }
}
