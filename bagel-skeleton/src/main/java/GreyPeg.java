import java.util.ArrayList;

/**
 * GreyPeg is a concrete object that extends the abstract class Peg.
 * GreyPegs cannot be destroyed when a ball collides with them.
 */
public class GreyPeg extends Peg {

    /**
     * constructor for the Grey Peg class with specified shape, and position
     */
    public GreyPeg(Shape shape, double x, double y) {
        super(Colour.GREY, shape, x, y);
    }

    /**
     * onCollide method found in Pegs abstract class that implements various behaviours on collision with a ball
     * @param ball the ball that has collided with this peg
     * @param pegs the list of pegs needed if the ball is a fireball
     */
    @Override
    void onCollide(Ball ball, ArrayList<Peg> pegs) {
        super.onCollide(ball, pegs);
    }

    /**
     * unlike other pegs, no matter what shouldBeRemoved is, it is set to false since
     * grey pegs can never be destroyed
     */
    @Override
    public void setShouldBeRemoved(boolean shouldBeRemoved) {
        super.setShouldBeRemoved(false);
    }
}
