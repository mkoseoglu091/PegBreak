import java.util.ArrayList;

/**
 * BluePeg is the most basic example of a Peg. I decided to not make Peg a concrete class and make its basic
 * example a blue peg, and decided to make Peg an abstract as well, that BluePeg also extends similar to other
 * types of pegs. I believe this makes everything tidier.
 */
public class BluePeg extends Peg {

    /**
     * constructor for blue pegs
     */
    public BluePeg(Shape shape, double x, double y) {
        super(Colour.BLUE, shape, x, y);
    }

    /**
     * onCollide function that remoes blue pegs upon collision
     */
    void onCollide(Ball ball, ArrayList<Peg> pegs) {
        super.onCollide(ball, pegs);
        setShouldBeRemoved(true);
    }
}
