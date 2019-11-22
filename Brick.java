/**
 *
 */

package brickbreaker;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.HashSet;

public class Brick {

    private static final String LIGHT_GREEN_BRICK = "brick2.gif";
    private static final String LIGHT_BLUE_BRICK = "brick1.gif";
    private static final String DARK_GREEN_BRICK = "brick8.gif";
    private static final String DARK_BLUE_BRICK = "brick7.gif";
    private static final String BLACK_BRICK = "brick3.gif";
    private int brick_type;
    private int brick_health;
    private int brick_points;
    private Image brick_image;
    public ImageView myBrick;
    private int powerUp;



    public Brick(int type){
        if(type == 0){
            brick_image = new Image(this.getClass().getClassLoader().getResourceAsStream(LIGHT_BLUE_BRICK));
        }
        else if(type == 1){
            brick_image =  new Image(this.getClass().getClassLoader().getResourceAsStream(LIGHT_GREEN_BRICK));
            brick_health = 1;
            brick_points = 1;
            brick_type = 1;
        }
        else if(type == 2){
            brick_image = new Image(this.getClass().getClassLoader().getResourceAsStream(LIGHT_BLUE_BRICK));
            brick_health = 1;
            brick_points = 2;
            brick_type = 2;
        }
        else if(type == 3){
            brick_image = new Image(this.getClass().getClassLoader().getResourceAsStream(DARK_GREEN_BRICK));
            brick_health = 1;
            brick_points = 1;
            brick_type = 3;
        }
        else if(type == 4){
            brick_image = new Image(this.getClass().getClassLoader().getResourceAsStream(DARK_BLUE_BRICK));
            brick_health = 1;
            brick_points = 2;
            brick_type = 4;
        }
        else if(type == 5){
            brick_image = new Image(this.getClass().getClassLoader().getResourceAsStream(BLACK_BRICK));
            brick_health = 10000;
            brick_type = 5;
        }
        myBrick = new ImageView(brick_image);
    }


    public void replaceBrick(Brick brick){
        brick.myBrick.setX(this.myBrick.getX());
        brick.myBrick.setY(this.myBrick.getY());
    }

    public boolean isGameOver(HashSet<Brick> bricks) {
        int num_bricks = 0;
        for (Brick brick : bricks) {
            if (brick.getBrick_type() != 0 && brick.getBrick_type() != 5) {
                num_bricks++;
            }
        }
        return (num_bricks == 0);
    }

    public HashSet<Brick> updateBrickOnIntersection(Group root, HashSet<Brick> bricks) {
        Brick temp = null;
        this.updateBrick_health();
        if (this.getBrick_health() <= 0) {
            if (this.getPowerUp() == 1) {
                this.setPowerUp(0);
            }
            if (this.getBrick_type() == 3) {
                temp = new Brick(1);
                this.replaceBrick(temp);
                bricks.add(temp);
                root.getChildren().add(temp.myBrick);
            }
            if (this.getBrick_type() == 4) {
                temp = new Brick(2);
                this.replaceBrick(temp);
                bricks.add(temp);
                root.getChildren().add(temp.myBrick);
            }
            root.getChildren().remove(this.myBrick);
            bricks.remove(this);
            return bricks;
        }
        return bricks;
    }

    public int getPowerUp(){
        return powerUp;
    }

    public void setPowerUp(int code){
        powerUp = code;
    }

    public Image getBrick_image(){
        return brick_image;
    }

    public int getBrick_points(){
        return brick_points;
    }

    public void updateBrick_health(){
        brick_health--;
    }

    public int getBrick_health(){
        return brick_health;
    }

    public int getBrick_type(){
        return brick_type;
    }

    public int getNumBricks(HashSet<Brick> bricks) {
        return bricks.size();
    }
}

