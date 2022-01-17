import bagel.Image;
import bagel.util.Point;

/**
 * Sprite is an abstract class that all other classes extend. It has the most basic functionality that all
 * other objects share such as moving, rendering, and various getters and setters.
 */
public abstract class Sprite {
    private Image image;
    private Point position;
    private double dx;
    private double dy;
    private boolean shouldBeRemoved = false;

    /**
     * constructor for the abstract class Sprite
     * all other objects extend this class
     *
     * @param imagePath a String where the objects image is found
     * @param x the x coordinate of where the object is to be placed
     * @param y the y coordinate of where the object is to be placed
     */
    public Sprite(String imagePath, double x, double y) {
        image = new Image(imagePath);
        position = new Point(x, y);
    }

    /**
     *These are various getters and setters for attributes that are shared across all other objects such as
     * having a position, a speed, an image etc.
     */

    /**
     * Getter for if the object is supposed to be removed or not
     */
    public boolean isShouldBeRemoved() {
        return shouldBeRemoved;
    }

    /**
     * Setter for if the object should be removed or not
     */
    public void setShouldBeRemoved(boolean shouldBeRemoved) {
        this.shouldBeRemoved = shouldBeRemoved;
    }

    /**
     * Getter for the dx value of the object. The dx value is the change in its position on the x-axis
     */
    public double getDx() {
        return dx;
    }

    /**
     * Setter for the dx value of the object
     */
    public void setDx(double dx) {
        this.dx = dx;
    }

    /**
     * Getter for the dy value of the object
     */
    public double getDy() {
        return dy;
    }

    /**
     * Setter for the dy value of the object
     */
    public void setDy(double dy) {
        this.dy = dy;
    }

    /**
     * Setter for the image of the object
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Getter for the image of the object
     */
    public Image getImage() {
        return image;
    }

    /**
     * Setter for the position of the object. Requires an object of Point class with an x and a y value
     * @param pixel an object of the class Point with an x and a y value
     */
    public void setPosition(Point pixel) {
        position = pixel;
    }

    /**
     * Getter for the position of the object given as a Point with an x and a y value
     */
    public Point getPosition() {
        return position;
    }


    /**
     * render function common to all objects that extend the Sprite class, draws the object at the given position
     */
    public void render() {
        image.draw(position.x, position.y);
    }

    /**
     * somewhat modified in some of the objects that inherit from this class, but commonly makes the object move
     */
    public void update() {
        move();
    }

    /**
     * moves the object depending on its Dx and Dy values
     */
    public void move() {
        double newX = position.x + dx;
        double newY = position.y + dy;
        position = new Point(newX, newY);
    }
}
