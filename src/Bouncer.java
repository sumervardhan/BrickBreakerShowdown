/**
 * This class defines Bouncer objects in BrickBreakerShowdown. It is responsible for creating the bouncer and placing it on the screen and has methods for the following behaviors:
 * Update position of the bouncer
 * Check if the bouncer is in bounds. If not, change direction to simulate bounce
 * Check for intersection with a paddle passed as an argument. Simulate bounce if intersection
 * Check for intersections with any bricks. If yes, call updateBrickOnIntersection of brick class through the brick object that has been hit
 * Set size of the bouncer
 * Reset the position of the bouncer
 *
 * I believe this piece of code is well designed because it separates the behaviour of the ball, a key actor in this game with lots of varied behaviour, into one class.
 * Adding new features or changing existing functions of the ball are easy because we just need to change the Bouncer class and call any new methods added in the BrickBreakerShowdown class'
 * game loop as necessary.
 */

package brickbreaker;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.HashSet;

public class Bouncer {

    private String myBouncerImage;
    private ImageView myBouncer;
    private int myXSpeed;
    private int myYSpeed;
    private int myXDirection;
    private int myYDirection;
    private boolean immunityPowerup;
    Group myRoot;

    public Bouncer(Group root, String BouncerImage, int xspeed, int yspeed, int size){
        myBouncerImage = BouncerImage;
        myRoot = root;
        myXSpeed = xspeed;
        myYSpeed = yspeed;
        myXDirection = 1;
        myYDirection = 1;
        immunityPowerup = false;
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(myBouncerImage));
        myBouncer = new ImageView(image);
        myBouncer.setX(size/2);
        myBouncer.setY(size/2);
        root.getChildren().add(myBouncer);
    }

    public void updatePosition(double elapsed_time){
        myBouncer.setX(myBouncer.getX() + myXDirection * myXSpeed * elapsed_time);
        myBouncer.setY(myBouncer.getY() + myYDirection * myYSpeed * elapsed_time);
    }

    public void checkInBounds(int size, int window_size)
    {
        if(myBouncer.getX() > window_size - 5)
        {
            myXDirection = myXDirection * (-1);
        }

        if(myBouncer.getX() < 5)
        {
            myXDirection = myXDirection * (-1);
        }

        if(myBouncer.getY() < 0.5)
        {
            myYDirection = myYDirection * (-1);
        }
        if(myBouncer.getY() > size - 1 && immunityPowerup)
        {
            myYDirection = myYDirection * (-1);
        }
    }

    public void checkPaddleIntersection(Paddle paddle){
        if(myBouncer.intersects(paddle.getLayoutBounds()))
        {
            myYDirection = myYDirection * (-1);
        }
    }

    public HashSet<Brick> checkBrickIntersection(Group root, HashSet<Brick> bricks, Paddle paddle){
        Brick isHit = null;
        int flag = -1;
        for(Brick brick: bricks){
            if(brick.myBrick.intersects(myBouncer.getLayoutBounds()) && brick.getBrick_type() != 0)
            {
                isHit = brick;
                isHit.updateBrickOnIntersection(root, bricks);
                setMyYDirection(getMyYDirection() * (-1));
                paddle.updateScore(brick.getBrick_points());
                break;
            }
        }
        return bricks;
    }

    public void setFit(Bouncer template_bouncer){
        myBouncer.setFitHeight(template_bouncer.myBouncer.getBoundsInLocal().getHeight());
        myBouncer.setFitWidth(template_bouncer.myBouncer.getBoundsInLocal().getWidth());
    }

    public void resetPosition(int size){
        myBouncer.setX(size/2 - myBouncer.getBoundsInLocal().getWidth()/2);
        myBouncer.setY(size/2 - myBouncer.getBoundsInLocal().getHeight()/2);
    }

    public void setMyYDirection(int dir){
        myYDirection = dir;
    }

    public int getMyYDirection(){
        return myYDirection;
    }

    public void setImmunityPowerup(boolean value){
        immunityPowerup = value;
    }

    public ImageView getMyBouncer(){
        return myBouncer;
    }
}
