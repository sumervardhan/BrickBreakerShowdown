package brickbreaker;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.HashSet;
import java.util.Random;

public class PowerUps {
    public static final int BIG_PADDLE = 130;
    public static final int SMALL_PADDLE = 40;
    private static final String POWERUP1 = "sizepower.gif";
    private static final String POWERUP2 = "laserpower.gif";
    private static final String POWERUP3 = "extraballpower.gif";
    private static final String POWERUP4 = "pointspower.gif";
    private static final int POWERUP_SPEED = 120;
    private int powerUpCode;
    private String powerUpImage;
    private ImageView myPower;
    private Brick myBrick;

    public PowerUps(int code){
        powerUpCode = code;
        if(powerUpCode == 1){
            powerUpImage = POWERUP1;
        } else if (powerUpCode == 2){
            powerUpImage = POWERUP2;
        } else if (powerUpCode == 3){
            powerUpImage = POWERUP3;
        } else if (powerUpCode == 4){
            powerUpImage = POWERUP4;
        }
    }

    public HashSet<PowerUps> spawn_powerup(Group root, HashSet<Brick> bricks, HashSet<PowerUps> powerUps){
        Random random = new Random();
        Image image = new Image(this.getClass().getClassLoader().getResourceAsStream(powerUpImage));
        myPower = new ImageView(image);
        Brick randomBrick = null;
        for(Brick brick : bricks){
            if(random.nextBoolean() && brick.getBrick_type() != 0 && brick.getBrick_type() != 5){
                randomBrick = brick;
                break;
            }
            randomBrick = brick;
        }
        myBrick = randomBrick;
        myPower.setX(randomBrick.myBrick.getX());
        myPower.setY(randomBrick.myBrick.getY());
        root.getChildren().add(myPower);
        powerUps.add(this);
        return powerUps;
    }

    public void updateLocation(Group root, int size, double elapsed_time, HashSet<Brick> bricks){
        if(!bricks.contains(this.myBrick)) {
            myPower.setY(myPower.getY() + POWERUP_SPEED * elapsed_time);
        }
    }

    public HashSet<PowerUps> checkInterSectionPaddle(Group root, Bouncer bouncer, Paddle paddle, HashSet<PowerUps> powerUps){
        if(this.myPower.intersects(paddle.getMyPaddle().getLayoutBounds())){
            root.getChildren().remove(this.myPower);
            if(this.powerUpCode == 1){
                paddle.getMyPaddle().setFitWidth(BIG_PADDLE);
            }
            if(this.powerUpCode == 2){
                paddle.getMyPaddle().setFitWidth(SMALL_PADDLE);
            }
            if(this.powerUpCode == 3){
                paddle.setWrapAroundPowerUp(true);
            }
            if(this.powerUpCode == 4){
                bouncer.setImmunityPowerup(true);
            }
            return powerUps;
        }
        return powerUps;
    }
}
