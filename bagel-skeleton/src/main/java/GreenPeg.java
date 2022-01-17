import java.util.ArrayList;

/**
 * GreenPeg is a concrete class that extends Peg. It also implements GreenPegCollidable, which
 * makes the behaviour of onCollide found in Peg a bit different. Mainly by creating two new balls going in
 * two different directions.
 */
public class GreenPeg extends Peg implements GreenPegCollidable {

    /**
     * constructor for the green peg
     */
    public GreenPeg(Shape shape, double x, double y) {
        super(Colour.GREEN, shape, x, y);
    }

    /**
     * differing from the onCollide function the other pegs have, upon being struck, the green peg
     * also creates two new balls to be added to the ball ArrayList
     */
    @Override
    public void onGreenPegCollide(Ball ball, ArrayList<Peg> pegs, ArrayList<Ball> newBalls) {
        super.onCollide(ball, pegs);
        setShouldBeRemoved(true);
        newBalls.add(new Ball(getPosition().x, getPosition().y, ball.isFireball(), true));
        newBalls.add(new Ball(getPosition().x, getPosition().y, ball.isFireball(), false));
    }
}
