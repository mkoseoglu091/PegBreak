import bagel.Window;
import bagel.util.Vector2;


/**
 * The PowerUp class is responsible for the construction, movement and rendering of power up objects.
 * These objects turn the ball into a fireball if the ball ever touches them, at which point they disappear
 * The PowerUp class extends Sprite which is the abstract class for all visible objects in the game
 */
public class PowerUp extends Sprite {

    //sets the constants for the power up
    private static final String IMAGE_PATH = "res/powerup.png";
    private static final int POWERUP_SPEED = 3;
    private static final int MIN_DIST = 5;

    public double targetX;
    public double targetY;

    /**
     * constructor for the power up
     */
    public PowerUp(double x, double y) {
        super(IMAGE_PATH, x, y);
    }

    /**
     * the update function constantly moves the power up, if it is ever MIN_DIST pixels far from the target,
     * changes the target coordinate to a new random point
     */
    public void update() {
        Vector2 vectorToTarget = new Vector2(targetX, targetY);
        Vector2 vectorToPowerUp = new Vector2(-getPosition().x, -getPosition().y);
        Vector2 finalVector = vectorToPowerUp.add(vectorToTarget);

        // this is the final vector with magnitude 1 in the direction of the mouse
        finalVector = finalVector.div(finalVector.length());

        double dx = finalVector.x * POWERUP_SPEED;
        double dy = finalVector.y * POWERUP_SPEED;

        setDx(dx);
        setDy(dy);

        move();
    }

    /**
     * move function for the powerup, checks if current position of the powerup is MIN_DIST far from its target
     * if so changes the target to a new random position
     */
    public void move() {
        super.move();
        double distance = Math.hypot(getPosition().x - targetX, getPosition().y - targetY);
        if (distance <= MIN_DIST) {
            targetX = Math.random() * Window.getWidth();
            targetY = Math.random() * Window.getHeight();
        }
    }
}
