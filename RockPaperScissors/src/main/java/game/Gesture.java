package game;

public enum Gesture {
    ROCK, PAPER, SCISSORS;

    public static Gesture fromString(String gesture) {
        if (gesture.equals("r")) {   // could be done with switch statements (better)
            return ROCK;
        }
        else if (gesture.equals("p")) {
            return PAPER;
        }
        else if (gesture.equals("s")) {
            return SCISSORS;
        }
        throw new IllegalStateException("Unexpected value" + gesture);
    }
    // switch statements below:
       /* return switch (gesture) {
            case "r" ->
                    ROCK;
            case "p" -> PAPER;
            case "s" -> SCISSORS;

            default -> throw new IllegalStateException("Unexpected value: " + gesture);
        };
*/

    public int compareWith(Gesture g) {
        if (this.equals(g)) {
            return 0;
        }
        else if (this.equals(ROCK) && g == PAPER) {
            return -1;
        }
        else if (this.equals(ROCK) && g == SCISSORS) {
            return 1;
        }
        else if (this.equals(PAPER) && g == ROCK) {
            return 1;
        }
        else if (this.equals(PAPER) && g == SCISSORS) {
            return -1;
        }
        else if (this.equals(SCISSORS) && g == ROCK) {
            return -1;
        }
        else if (this.equals(SCISSORS) && g == PAPER) {
            return 1;
        }

        throw new IllegalStateException("Unexpected value" + g);
    }

    }








