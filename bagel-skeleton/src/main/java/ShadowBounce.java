import bagel.AbstractGame;
import bagel.Input;
import bagel.MouseButtons;
import bagel.Window;
import bagel.util.Point;
import bagel.util.Vector2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * ShadowBounce is the main game, and it extends AbstractGame. It is where some of the game physics can be found
 * Most of the game physics are inside the objects that contain them, or the abstract classes extend.
 * Only collision checking is done in the game. ShadowBounce also has methods for clearing and loading stages.
 */
public class ShadowBounce extends AbstractGame {
    private static final int MAX_LEVEL = 5;
    private static final int MAX_SHOTS = 20;
    private static final int POWERUP_PROBABILITY = 10;
    private static final int BALL_INIT_SPEED = 10;

    // This part initializes most of the items like an ArrayList of balls, pegs, power ups, lives and stage
    private int currentLevel = 0;
    private ArrayList<Ball> balls = new ArrayList<>();
    private ArrayList<Peg> pegs = new ArrayList<>();
    private ArrayList<PowerUp> powerups = new ArrayList<>();
    private int numShots;
    private boolean newTurn = false;
    private Bucket bucket = new Bucket();


    /**
     * The game is initialized by calling startStage, which reads the csv file and sets up the board
     * Also the number of lives the player has is set at this stage
     */
    public ShadowBounce() throws FileNotFoundException {
        startStage(currentLevel);
        numShots = MAX_SHOTS;
    }

    /**
     * The main function that runs the game
     */
    public static void main(String[] args) {
        try {
            System.out.println(System.getProperty("user.dir"));
            ShadowBounce game = new ShadowBounce();
            game.run();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Uses the render() function of the ball, peg, power up and bucket classes
     */
    private void renderAll() {
        for (Ball ball : balls) {
            ball.render();
        }

        for (Peg peg : pegs) {
            peg.render();
        }
        for (PowerUp powerUp : powerups) {
            powerUp.render();
        }
        bucket.render();
    }

    /**
     * gets the bounding boxes of the balls and compares it with other objects in the game (pegs and power ups),
     * and uses the intersects function in the Rectangle class to check for a collision
     * If there is a collision, the onCollide function of the peg class is called (this function is slightly different)
     * for green pegs and grey pegs. If the collision was with a power up the ball is turned into a fireball
     * Afterwards the items that should be removed (blue, red and green pegs, and power ups) are removed.
     * If the peg was a green one, new balls are added to the balls ArrayList
     */
    private void checkForCollision() {
        boolean greenPegCollided = false;
        ArrayList<Ball> newBalls = new ArrayList<>();
        for (Ball ball : balls) {
            for (Peg peg : pegs) {
                if (ball.getImage().getBoundingBoxAt(ball.getPosition()).intersects(peg.getImage().getBoundingBoxAt(peg.getPosition()))) {
                    if (peg.getColour() != Peg.Colour.GREEN) {
                        peg.onCollide(ball, pegs);
                    } else {
                        GreenPeg greenPeg = (GreenPeg) peg;
                        greenPeg.onGreenPegCollide(ball, pegs, newBalls);
                        greenPegCollided = true;
                    }
                }
            }
            for (PowerUp powerup : powerups) {
                if (ball.getImage().getBoundingBoxAt(ball.getPosition()).intersects(powerup.getImage().getBoundingBoxAt(powerup.getPosition()))) {
                    powerup.setShouldBeRemoved(true);
                    ball.setFireball(true);
                }
            }
        }
        // this part checks if a green peg was destroyed not due to a collision but due to an explosion from another peg
        // if that is the case the green peg still acts as it should
        if (!greenPegCollided) {
            if (pegs.stream().filter(peg -> peg.getColour() == Peg.Colour.GREEN && peg.isShouldBeRemoved()).count() == 1) {
                for (Ball ball : balls) {
                    GreenPeg greenpeg = (GreenPeg) pegs.stream().filter(peg -> peg.getColour() == Peg.Colour.GREEN).collect(Collectors.toList()).get(0);
                    greenpeg.onGreenPegCollide(ball, pegs, newBalls);
                }
            }
        }
        balls.addAll(newBalls);
        pegs.removeIf(peg -> peg.isShouldBeRemoved());
        powerups.removeIf(powerup -> powerup.isShouldBeRemoved());
    }

    /**
     * Starts a new stage by first checking if current stage is bigger than MAX_LEVEL, if not reads the appropriate
     * csv file. Afterwards, since the file names never contain if the peg is blue or if it is a normal (circular)
     * peg, the lines read are decoded so the correct type of peg is created using the correct constructor (grey or blue)
     *
     * Once pegs are created 1/5th of the pegs are randomly selected and are removed with a red peg created in their place
     * After these, the makeGreenPeg() function is called (which creates a green peg at the start of each level as well as
     * after each complete turn)
     *
     * And finally createPowerUp() is called, which creates a fireball power up with 1 in 10 chance
     *
     * @param currentLevel initially 0, can go up to 4
     * @throws FileNotFoundException if the csv file with the current levels number is not found
     */
    private void startStage(int currentLevel) throws FileNotFoundException {
        if (currentLevel == MAX_LEVEL) {
            Window.close();
        } else {
            Scanner scanner = new Scanner(new File("./res/" + currentLevel + ".csv"));
            scanner.useDelimiter(",|\\n");
            while (scanner.hasNext()) {
                String type = scanner.next();

                // Split type to colour and shape, this helps figure out if a grey or peg should be created
                // and to make sure the correct shaped peg is created
                Peg.Colour colour = Peg.Colour.BLUE;
                for (Peg.Colour enumColour : Peg.Colour.values()) {
                    if (type.contains(enumColour.toString().toLowerCase())) {
                        colour = enumColour;
                        break;
                    }
                }

                Peg.Shape shape = Peg.Shape.CIRCULAR;
                for (Peg.Shape enumShape : Peg.Shape.values()) {
                    if (type.contains(enumShape.toString().toLowerCase())) {
                        shape = enumShape;
                        break;
                    }
                }

                // x and y are the coordinates of the peg that is being read from the csv file
                double x = scanner.nextDouble();
                double y = scanner.nextDouble();
                if (colour == Peg.Colour.BLUE) {
                    pegs.add(new BluePeg(shape, x, y));
                } else if (colour == Peg.Colour.GREY) {
                    pegs.add(new GreyPeg(shape, x, y));
                }
            }
            // Create Red Pegs
            int numRedPegsToAdd = (int) pegs.stream().filter(peg -> peg.getColour() == Peg.Colour.BLUE).count() / 5;
            while (numRedPegsToAdd > 0) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, pegs.size());
                Peg randomPeg = pegs.get(randomNum);
                if (randomPeg.getColour() == Peg.Colour.BLUE) {
                    pegs.remove(randomNum);
                    pegs.add(new RedPeg(randomPeg.getShape(), randomPeg.getPosition().x, randomPeg.getPosition().y));
                    numRedPegsToAdd--;
                }
            }

            // create an initial green peg
            makeGreenPeg();

            // create powerup
            createPowerUp();
        }
    }

    /**
     * Selects a random blue peg, removes it and creates a green peg in its place, unless there already is a green peg
     * on the board. In which case if it is not destroyed its location is changed at the end of the turn
     */
    private void makeGreenPeg() {
        // make a random blue peg green
        int numGreenPegs = (int) pegs.stream().filter(peg -> peg.getColour() == Peg.Colour.GREEN).count();
        if (numGreenPegs < 1) {
            while (true) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, pegs.size());
                Peg randomPeg = pegs.get(randomNum);
                if (randomPeg.getColour() == Peg.Colour.BLUE) {
                    pegs.remove(randomNum);
                    pegs.add(new GreenPeg(randomPeg.getShape(), randomPeg.getPosition().x, randomPeg.getPosition().y));
                    break;
                }
            }
        }
    }

    /**
     * clears all the pegs from the board and resets the position of the bucket
     */
    private void clearStage(){
        pegs.clear();
        powerups.clear();
        bucket.setPosition(new Point(bucket.INIT_X,bucket.INIT_Y ));
        bucket.setDx(bucket.INIT_SPEED);
    }

    /**
     * With 1 in 10 chance, creates a power up in a random position goin in a random position.
     */
    private void createPowerUp() {
        int randomNum = ThreadLocalRandom.current().nextInt(0, POWERUP_PROBABILITY);
        if (randomNum == 0) {
            double powerUpX = Math.random() * Window.getWidth();
            double powerUpY = Math.random() * Window.getHeight();
            PowerUp powerup = new PowerUp(powerUpX, powerUpY);
            powerup.targetX = Math.random() * Window.getWidth();
            powerup.targetY = Math.random() * Window.getHeight();
            powerups.add(powerup);
        }
    }

    @Override
    public void update(Input input) {
        double targetX;
        double targetY;
        double dx, dy;
        // these two vectors are used to calculate the vector of the balls movement
        // vectorToBall is a vector from (0,0 to the position of the mouse)
        // vectorToMouse is a vector from (0,0 to the position of the ball)
        Vector2 vectorToBall;
        Vector2 vectorToMouse;

        // the vector of the balls movement
        Vector2 vectorBall;

        // Check if bucket caught ball
        for (Ball ball : balls) {
            if (ball.getPosition().y > Window.getHeight() && ball.getImage().getBoundingBoxAt(ball.getPosition()).intersects(bucket.getImage().getBoundingBoxAt(bucket.getPosition()))) {
                numShots++;
            }
        }

        // If all the balls are below the screen this means the turn is over
        if (balls.stream().allMatch(ball -> ball.getPosition().y > Window.getHeight())) {

            // Check shots, if the player has no shots left, the game is over and the wondow closes
            if (numShots == 0) {
                Window.close();
            }

            // clear balls before starting next turn
            balls.clear();

            // check red pegs, if there are no more red pegs left the game moves on to the next stage
            int numRedPegs = (int) pegs.stream().filter(peg -> peg.getColour() == Peg.Colour.RED).count();
            if (numRedPegs == 0) {
                clearStage();
                try {
                    currentLevel++;
                    startStage(currentLevel);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

            // every new turn move the green peg, by removing the previous green peg and making a new one,
            // also call createPowerUp so there is a 1 in 10 chance of a new power up occuring
            if (newTurn) {
                if (pegs.stream().filter(peg -> peg.getColour() == Peg.Colour.GREEN).count() == 1) {
                    Peg greenPeg = pegs.stream().filter(peg -> peg.getColour() == Peg.Colour.GREEN).collect(Collectors.toList()).get(0);
                    pegs.remove(greenPeg);
                    pegs.add(new BluePeg(greenPeg.getShape(), greenPeg.getPosition().x, greenPeg.getPosition().y));
                    newTurn = false;
                }
                createPowerUp();
            }

            makeGreenPeg();


            // creates a new ball at the start point when the mouse is clicked
            if (input.isDown(MouseButtons.LEFT)) {
                numShots--;

                // Vector calculations to find Dy and Dx
                targetX = input.getMouseX();
                targetY = input.getMouseY();
                vectorToMouse = new Vector2(targetX, targetY);
                vectorToBall = new Vector2(-Ball.INIT_X, -Ball.INIT_Y);
                vectorBall = vectorToBall.add(vectorToMouse);

                // this is the final vector with magnitude 1 in the direction of the mouse
                vectorBall = vectorBall.div(vectorBall.length());
                dx = vectorBall.x * BALL_INIT_SPEED;
                dy = vectorBall.y * BALL_INIT_SPEED;

                Ball ball = new Ball(dx, dy);
                balls.add(ball);
                newTurn = true;
            }
        }

        // various update functions are called here. Update functions in each class basically make those items move
        for (Ball ball : balls) {
            ball.update();
        }

        for (PowerUp powerup : powerups) {
            powerup.update();
        }

        bucket.update();

        // Checks if bounding boxes of ball and pegs or ball and power ups collide and remove them if necessary
        checkForCollision();


        // Renders all images of pegs, balls, power ups and bucket
        renderAll();
    }
}