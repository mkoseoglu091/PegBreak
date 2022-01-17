import java.util.ArrayList;

/**
 * RegPeg extends Peg, and is a type of peg that when destroyed, if there is no more red pegs the game
 * moves on to another stage. Otherwise it is very similar to the blue peg.
 */
public class RedPeg extends Peg {
    /**
     * constructor for red pegs
     */
    public RedPeg(Shape shape, double x, double y) {
        super(Colour.RED, shape, x, y);
    }

    /**
     * onCollide function for when red pegs are struck and are removed
     */
    @Override
    void onCollide(Ball ball, ArrayList<Peg> pegs) {
        super.onCollide(ball, pegs);
        setShouldBeRemoved(true);
    }
}
