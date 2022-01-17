import java.util.ArrayList;

/**
 * This is an interface that slightly changes the behaviour of OnCollide which is normally a
 * method found in the abstract class Peg. Since green pegs act a bit differently on collision the
 * GreenPegCollidable interface is used
 */
public interface GreenPegCollidable {
    // an interface of how green pegs are supposed to act upon being struck

    void onGreenPegCollide(Ball ball, ArrayList<Peg> pegs, ArrayList<Ball> balls);
}
