package game;

public class Player {
    private Duel duel;

    public void makeGesture(Gesture g) {
        duel.handleGesture(this, g);
    }

    public void enterDuel(Duel duel){
        System.out.println("You've entered duel");
        this.duel = duel;
    }

    public void leaveDuel(){
        duel = null;
    }

    public boolean isDuelling(){
        if (duel != null){
            return true;
        }
        else {
            return false;
        }
    }


}
