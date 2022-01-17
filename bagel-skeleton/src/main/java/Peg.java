import bagel.util.Point;
import bagel.util.Side;
import bagel.util.Vector2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Peg is an abstract class that extends Sprite. Grey, Blue, Green and Red pegs all extend the Peg class
 * the most important function of Peg is the OnCollide function that specifies the outcomes of collisions with
 * a ball
 */
public abstract class Peg extends Sprite  {

    // a ball can be one of these colours
    enum Colour {
        BLUE,
        GREY,
        RED,
        GREEN
    }

    // the ball can be one of these shapes
    enum Shape {
        CIRCULAR,
        HORIZONTAL,
        VERTICAL
    }

    // the blast radius is used if the ball striking the pegs is a fireball, in which case all pegs in the
    // blast radius are destroyed
    private static final double BLAST_RADIUS = 70;

    private Colour colour;
    private Shape shape;

    /**
     * Getter function for colour
     */
    public Colour getColour() {
        return colour;
    }

    /**
     * Getter function for shape
     */
    public Shape getShape() {
        return shape;
    }

    /**
     * constructor of pegs, takes in as arguments the colour, shape and position of the peg and formats the information
     * into what is going to be used as the image path by the constructors of realized peg classes
     */
    public Peg(Colour colour, Shape shape, double x, double y) {
        super(String.format("./res/%s%speg.png", colour == Colour.BLUE ? "" : colour.toString().toLowerCase() + "-", shape == Shape.CIRCULAR ? "" : shape.toString().toLowerCase() + "-"), x, y);
        this.colour = colour;
        this.shape = shape;
    }

    /**
     * On collision, the side of the peg where the ball has struck is calculated so that it can bounce off
     * of other pegs. If the ball is a fireball, this also destroys pegs that are in the blast radius
     */
    void onCollide(Ball ball, ArrayList<Peg> pegs) {

        // find the corners of the ball's boundingbox
        Point bottomRight = ball.getImage().getBoundingBoxAt(ball.getPosition()).bottomRight();
        Point bottomLeft = ball.getImage().getBoundingBoxAt(ball.getPosition()).bottomLeft();
        Point topRight = ball.getImage().getBoundingBoxAt(ball.getPosition()).topRight();
        Point topLeft = ball.getImage().getBoundingBoxAt(ball.getPosition()).topLeft();
        Vector2 velocity = new Vector2(ball.getDx(), ball.getDy());

        ArrayList<Point> corners = new ArrayList<>();
        corners.add(bottomRight);
        corners.add(bottomLeft);
        corners.add(topLeft);
        corners.add(topRight);

        List<Point> intersectingPoints = corners
                .stream()
                .filter(corner -> getImage().getBoundingBoxAt(getPosition()).intersects(corner))
                .collect(Collectors.toList());

        double averageX = intersectingPoints.stream().mapToDouble(o -> o.x).sum() / intersectingPoints.size();
        double averageY = intersectingPoints.stream().mapToDouble(o -> o.y).sum() / intersectingPoints.size();

        // find the mid point of the corners that happened to intersect with the target peg
        Point midPoint = new Point(averageX, averageY);

        // find the side that is estimated to be the side the ball struck
        Side hitSide = getImage().getBoundingBoxAt(getPosition()).intersectedAt(midPoint, velocity);

        // depending on the side, change the velocity of the ball to give it a bouncing effect
        if (hitSide == Side.BOTTOM || hitSide == Side.TOP) {
            ball.setDy(-ball.getDy());
        } else if (hitSide == Side.RIGHT || hitSide == Side.LEFT) {
            ball.setDx(-ball.getDx());
        } else {
            ball.setDy(-ball.getDy());
            ball.setDx(-ball.getDx());
        }

        // Fireball
        if (ball.isFireball()) {
            for (Peg peg : pegs) {
                if (Math.hypot(getPosition().x - peg.getPosition().x, getPosition().y - peg.getPosition().y) <= BLAST_RADIUS) {
                    peg.setShouldBeRemoved(true);
                }
            }
        }
    }
}
