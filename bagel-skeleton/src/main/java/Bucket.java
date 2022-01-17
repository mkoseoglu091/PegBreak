import bagel.Window;

/**
 * The Bucket extends Sprite, It is an object at the bottom of the screen where if a ball exits the screen
 * while in contact with the bucket the player does not lose a shot.
 */
public class Bucket extends Sprite {

    // sets the constants such as the image path, initial position and initial speed
    public static final String IMAGE_PATH = "res/bucket.png";
    public static final double INIT_X = 512;
    public static final double INIT_Y = 744;
    public static final double INIT_SPEED = -4;

    /**
     * constructor of the bucket with initial position and speed
     */
    public Bucket() {
        super(IMAGE_PATH, INIT_X, INIT_Y);
        this.setDx(INIT_SPEED);
    }

    /**
     * if the bucket touches any of the sides, it changes direction
     */
    public void update() {
        if (getPosition().x < getImage().getWidth()/2 || getPosition().x > Window.getWidth() - getImage().getWidth()/2) {
            setDx(-getDx());
        }
        move();
    }
}
