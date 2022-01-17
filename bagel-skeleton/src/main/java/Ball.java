import bagel.Image;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;


/**
 * This is the Ball class, and it extends Sprite. The ball class has two possible image paths and
 * collision mechanisms. One for regular ball and another for fireball. If the boolean fireball is set to true,
 * the ball turns into a fireball and destroys destroyable pegs in a set radius.
 * The ball always bounces off of walls and other pegs.
 */
public class Ball extends Sprite {
    public static final double INIT_X = 512;
    public static final double INIT_Y = 32; // The ball is initially created out of bounds of game
    public static final double SPLIT_VELOCITY_X = Math.cos(Math.toRadians(45))*10;
    public static final double SPLIT_VELOCITY_Y = -Math.sin(Math.toRadians(45))*10;
    public static final String IMAGE_PATH_NORMAL = "res/ball.png";
    public static final String IMAGE_PATH_FIREBALL = "res/fireball.png";
    private static final Vector2 gravity = new Vector2(0, 0.15);

    // if fireball is set to true the ball turns into a fireball with different functionality
    private boolean fireball = false;

    /**
     * constructor for the ball class with dx dy calculated in the game through vector calculations
     */
    public Ball(double dx, double dy) {
        super(IMAGE_PATH_NORMAL, INIT_X, INIT_Y);
        setDx(dx);
        setDy(dy);
    }

    /**
     * a second constructor that is used only when the ball strikes a green peg, in which case
     * two new balls are created that are the same type as the original ball.
     */
    public Ball(double x, double y, boolean isFireball, boolean shouldGoRight) {
        super(isFireball ? IMAGE_PATH_FIREBALL : IMAGE_PATH_NORMAL, x, y);
        setDx(shouldGoRight ? SPLIT_VELOCITY_X : -SPLIT_VELOCITY_X);
        setDy(SPLIT_VELOCITY_Y);
    }

    /**
     * Moves the ball
     * @param dx a double of how much the ball moves on the x-axis
     * @param dy a double of how much the ball moves on the y-axis
     */
    public void move(double dx, double dy) {
        double newX = getPosition().x + dx;
        double newY = getPosition().y + dy;
        setPosition(new Point(newX, newY));
    }

    /**
     * getter for the boolean fireball. Returns if the ball is a fireball or not
     */
    public boolean isFireball() {
        return fireball;
    }

    /**
     * Setter for the boolean fireball
     * @param fireball a true or false value that changes the balls behaviour
     */
    public void setFireball(boolean fireball) {
        this.fireball = fireball;
        setImage(new Image(fireball ? IMAGE_PATH_FIREBALL : IMAGE_PATH_NORMAL));
    }

    /**
     * update function that gives the ball its movement commands along with the fact that
     * the ball bounces off walls by changing direction on the x-axis
     */
    public void update() {
        if (getPosition().x < 0 || getPosition().x > Window.getWidth()) {
            setDx(-getDx());
        }

        // Adds the value of gravity each frame
        setDy(getDy() + gravity.y);
        move(getDx(), getDy());
    }
}
