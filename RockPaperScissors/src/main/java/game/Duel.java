package game;


public class Duel {
    private Player playerOne;
    private Player playerTwo;
    private Gesture gestureOne;
    private Gesture gestureTwo;
    public record Result(Player winner, Player loser){

    }
    public End onEnd;

    public void setOnEnd(End onEnd) {
        this.onEnd = onEnd;
    }

    public Duel(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        playerOne.enterDuel(this);
        playerTwo.enterDuel(this);
    }

    public void handleGesture(Player player, Gesture gesture) {
        if (player == playerOne) {
            gestureOne = gesture;
        }
        else if (player == playerTwo) {
            gestureTwo = gesture;
        }
        if (gestureOne != null && gestureTwo != null){
            setOnEnd(() -> {   // lambda statement, can be replaced with anonymous one (new End ... inside the ( ) )
                System.out.println(gestureOne + " first " + gestureTwo + " second");
            });

            onEnd.duelEnd();
            System.out.println("registered");
         }

    }

    public Result evaluate(){ // Record == first for winner, second for loser, therefore if scoreOne > 0 playerOne is first in the record and playerTwo is second!!
        int scoreOne = gestureOne.compareWith(gestureTwo);
        int scoreTwo = gestureTwo.compareWith(gestureOne);

        if (scoreOne > scoreTwo) {
            playerOne.leaveDuel();
            playerTwo.leaveDuel();
            return new Result(playerOne, playerTwo);
        }
        else if (scoreTwo > scoreOne) {
            playerOne.leaveDuel();
            playerTwo.leaveDuel();
            return new Result(playerTwo, playerOne);
        } else {
            playerOne.leaveDuel();
            playerTwo.leaveDuel();
            return null;
        }
    }

    @FunctionalInterface
    public interface End{
        public void duelEnd();
    }


}
