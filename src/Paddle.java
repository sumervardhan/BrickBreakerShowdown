package brickbreaker;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.HashSet;

public class Paddle {
    private int myID;
    private String myImage;
    private int mySpeed;
    private ImageView myPaddle;
    private Group root;
    private int myScore;
    private boolean wrapAroundPowerUp;
    private Bouncer myBouncer;


    public Paddle(String myImage, Group root, Bouncer bouncer, int size, int speed){
        myImage = myImage;
        myBouncer = bouncer;
        myScore = 0;
        wrapAroundPowerUp = false;
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(myImage));
        myPaddle = new ImageView(image);
        root = root;
        root.getChildren().add(myPaddle);
        myPaddle.setX(size/2);
        myPaddle.setY(size - myPaddle.getBoundsInLocal().getHeight());
        mySpeed = speed;
    }

    public void updateScore(int pointsAwarded){
        myScore += pointsAwarded;
    }

    public Bounds getLayoutBounds(){
        return myPaddle.getLayoutBounds();
    }

    public void moveLeft(int size){
        if(myPaddle.getX() > myPaddle.getLayoutX()/2) {
            myPaddle.setX(myPaddle.getX() - mySpeed);
        }
        else if (wrapAroundPowerUp){
            myPaddle.setX(size - myPaddle.getBoundsInLocal().getWidth() / 2);
        }
    }

    public void moveRight(int size){
        if(myPaddle.getX() < (size - 60)) {
            myPaddle.setX(myPaddle.getX() + mySpeed);
        }
        else if (wrapAroundPowerUp){
            myPaddle.setX(mySpeed);
        }
    }

    public int getMyID(){
        return myID;
    }

    public void setMyID(int ID){
        myID = ID;
    }

    public String getMyScore(){
        return Integer.toString(myScore);
    }

    public ImageView getMyPaddle() {
        return myPaddle;
    }

    public int finalScore()
    {
        return myScore;
    }

    public void setWrapAroundPowerUp(boolean value){
        wrapAroundPowerUp = value;
    }

}
