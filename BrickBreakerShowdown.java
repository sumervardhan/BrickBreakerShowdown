package brickbreaker;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.text.*;

import javax.swing.*;
import java.io.*;
import java.sql.Time;
import java.util.Random;
import java.util.Scanner;
import java.util.HashSet;
import java.lang.String.*;
import java.util.concurrent.TimeUnit;

//Code adapted from Duke CS230 Fall 2019 Lab 1.

public class BrickBreakerShowdown extends Application {
    private static final String TITLE = "BrickBreakerShowdown\u2122";
    private static final String GAME_OVER = "game_over1.jpg";
    private static final String[] LEVELS = {"Resources/map1.txt", "Resources/map2.txt", "Resources/map3.txt"};
    private static final String START_SCREEN = "start_screen.png";
    private static final int SIZE = 350;
    private static final int WINDOW_SIZE = 400;
    private static final int FRAMES_PER_SECOND = 120;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final Paint BACKGROUND = Color.WHITE;
    private static final String BOUNCER_IMAGE = "ball.gif";
    private static final String BOUNCER_IMAGE2 = "laserpower.gif";
    private static final String PADDLE_IMAGE1 = "paddle.gif";
    private static final String PADDLE_IMAGE2 = "paddle.gif";
    private static final int BOUNCER_SPEED = 180;
    private static final int PADDLE_SPEED = 30;

    // some things we need to remember during our game
    private Scene myScene;
    private Bouncer bouncer1;
    private Bouncer bouncer2;
    private Paddle paddle1;
    private Paddle paddle2;
    private Text score1;
    private Text score2;
    private Group root;
    private HashSet<Brick> bricks;
    private HashSet<PowerUps> powerUps;
    private int game_level;
    private int map_level;
    private Random random;
    private boolean skip_level_cheat = false;

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) {

        root = new Group();
        // attach scene to the stage and display it
        Scene starting_scene = new Scene(root, WINDOW_SIZE, WINDOW_SIZE, BACKGROUND);
        stage.show();
        Image startImage = new Image(this.getClass().getClassLoader().getResourceAsStream(START_SCREEN));
        ImageView starting_screen = new ImageView(startImage);
        starting_screen.setFitWidth(WINDOW_SIZE);
        starting_screen.setFitHeight(WINDOW_SIZE);
        root.getChildren().add(starting_screen);
        starting_scene.setOnMouseClicked(e -> handleMouseInput(stage, e.getX(), e.getY()));
        stage.setScene(starting_scene);
    }

    private void startGame(Stage stage) {
        root = new Group();
        myScene = setupGame(root, WINDOW_SIZE, WINDOW_SIZE, BACKGROUND);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();
        // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> {
            try {
                step(SECOND_DELAY);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    // Create the game's "scene": what shapes will be in the game and their starting properties
    private Scene setupGame (Group root, int width, int height, Paint background) {

            // create a place to see the shapes
            Scene scene = new Scene(root, width, height, background);

            bouncer1 = new Bouncer(root, BOUNCER_IMAGE, BOUNCER_SPEED, (int) (BOUNCER_SPEED / 1.5), SIZE);
            bouncer2 = new Bouncer(root, BOUNCER_IMAGE2, BOUNCER_SPEED * (-1), (int) (BOUNCER_SPEED / 1.5), SIZE);

            bouncer2.setFit(bouncer1);

            paddle1 = new Paddle(PADDLE_IMAGE1, root, bouncer1, SIZE, PADDLE_SPEED);
            paddle2 = new Paddle(PADDLE_IMAGE2, root, bouncer2, SIZE, PADDLE_SPEED);

            score1 = new Text();
            score2 = new Text();

            Text red_player = new Text("R E D");
            Text blue_player = new Text("B L U E");

            score1.setX(WINDOW_SIZE / 4 * 3);
            score1.setY(WINDOW_SIZE - score1.getBoundsInLocal().getHeight()/2);

            score2.setX(WINDOW_SIZE / 8);
            score2.setY(WINDOW_SIZE - score2.getBoundsInLocal().getHeight()/2);

            red_player.setX(WINDOW_SIZE / 4 * 3);
            red_player.setY(WINDOW_SIZE - score1.getBoundsInLocal().getHeight() - red_player.getBoundsInLocal().getHeight()/2);
            red_player.setFill(Color.RED);

            blue_player.setX(WINDOW_SIZE / 8);
            blue_player.setY(WINDOW_SIZE - score2.getBoundsInLocal().getHeight() - blue_player.getBoundsInLocal().getHeight()/2);
            blue_player.setFill(Color.BLUE);

            root.getChildren().add(score1);
            root.getChildren().add(score2);
            root.getChildren().add(red_player);
            root.getChildren().add(blue_player);

            setUpLevel(1, LEVELS[0]);

            // respond to input
            scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
            return scene;
    }

    // Change properties of shapes in small ways to animate them over time
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start
    private void step (double elapsedTime) throws InterruptedException {

        // Update bouncer positions
        bouncer1.updatePosition(elapsedTime);
        bouncer2.updatePosition(elapsedTime);

        //Ensure that both bouncers are in bounds.
        bouncer1.checkInBounds(SIZE, WINDOW_SIZE);
        bouncer2.checkInBounds(SIZE, WINDOW_SIZE);

        //Check for an intersection between respective bouncer and paddle
        bouncer1.checkPaddleIntersection(paddle1);
        bouncer2.checkPaddleIntersection(paddle2);

        //Check for intersections with bricks. Bricks and score are updated
        bricks = bouncer1.checkBrickIntersection(root, bricks, paddle1);
        bricks = bouncer2.checkBrickIntersection(root, bricks, paddle2);

        //Update Score
        score1.setText(paddle1.getMyScore());
        score2.setText(paddle2.getMyScore());

        if((new Brick(1).isGameOver(bricks)) || skip_level_cheat){
            skip_level_cheat = false;
            root.getChildren().removeAll(bricks);
            bricks.clear();
            for(int i = 0; i < 300000; i++){

            };
            bouncer1.resetPosition(SIZE);
            bouncer2.resetPosition(SIZE);

            if(game_level == 3) {
                int winner = (paddle1.finalScore() > paddle2.finalScore()) ? paddle1.getMyID() : paddle2.getMyID();
                endGame(winner);
            }
            else if(game_level == 1){
                setUpLevel(2, LEVELS[1]);
            }
            else if(game_level == 2){
                setUpLevel(3, LEVELS[2]);
            }
        }

        int randomNum = random.nextInt(500);
        if(randomNum == 42){
            //Create a power-up and attach it to a random brick.
            System.out.println("POWER");
            PowerUps power = new PowerUps(random.nextInt(4) + 1);
            powerUps = power.spawn_powerup(root, bricks, powerUps);
        }

        HashSet<PowerUps> toRetain = null;

        for(PowerUps power : powerUps){
            power.updateLocation(root, SIZE, elapsedTime, bricks);
            toRetain = power.checkInterSectionPaddle(root, bouncer1, paddle1, powerUps);
            toRetain = power.checkInterSectionPaddle(root, bouncer1, paddle2, powerUps);
        }
        if (toRetain != null) {
            powerUps.retainAll(toRetain);
        }

        if(bouncer1.getMyBouncer().getY() > SIZE && bouncer2.getMyBouncer().getY() > SIZE){
            int winner = (paddle1.finalScore() > paddle2.finalScore()) ? paddle1.getMyID() : paddle2.getMyID();
            endGame(winner);
        }
    }

    private void endGame(int winner){
        ImageView game_over = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(GAME_OVER)));
        game_over.setFitWidth(WINDOW_SIZE/2);
        game_over.setFitHeight(WINDOW_SIZE/6);
        game_over.setX(WINDOW_SIZE/2 - game_over.getBoundsInLocal().getWidth()/2);
        game_over.setY(WINDOW_SIZE/6);
        root.getChildren().clear();
        root.getChildren().add(game_over);
        Text winner_message_red = new Text("R E D  W I N S\n\n\n" + paddle1.getMyScore() + " POINTS");
        Text winner_message_blue = new Text("B L U E  W I N S\n\n\n" + paddle2.getMyScore() + " POINTS");

        if(winner == 1){
            winner_message_red.setFill(Color.RED);
            winner_message_red.setX(SIZE/3);
            winner_message_red.setY(SIZE/2);
            root.getChildren().add(winner_message_red);
        }
        else{
            winner_message_blue.setFill(Color.BLUE);
            root.getChildren().add(winner_message_blue);
            winner_message_blue.setX(SIZE/3);
            winner_message_blue.setY(SIZE/2);
        }
    }
    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) {
            paddle1.moveRight(WINDOW_SIZE);
        } else if (code == KeyCode.LEFT) {
            paddle1.moveLeft(WINDOW_SIZE);
        } else if (code == KeyCode.D) {
            paddle2.moveRight(WINDOW_SIZE);
        } else if (code == KeyCode.A) {
            paddle2.moveLeft(WINDOW_SIZE);
        } else if (code == KeyCode.X){
            skip_level_cheat = true;
            removeBricksFromRoot();
        } else if (code == KeyCode.R){
            bouncer1.resetPosition(SIZE);
            bouncer2.resetPosition(SIZE);
            removeBricksFromRoot();
            setUpLevel(game_level, LEVELS[game_level-1]);
        } else if (code == KeyCode.DIGIT1){
            bouncer1.resetPosition(SIZE);
            bouncer2.resetPosition(SIZE);
            removeBricksFromRoot();
            setUpLevel(1, LEVELS[0]);
        } else if (code == KeyCode.DIGIT2) {
            bouncer1.resetPosition(SIZE);
            bouncer2.resetPosition(SIZE);
            removeBricksFromRoot();
            setUpLevel(2, LEVELS[1]);
        } else if (code == KeyCode.DIGIT3) {
            bouncer1.resetPosition(SIZE);
            bouncer2.resetPosition(SIZE);
            removeBricksFromRoot();
            setUpLevel(3, LEVELS[2]);
        } else if(code == KeyCode.I){
            bouncer1.setImmunityPowerup(true);
            bouncer2.setImmunityPowerup(true);
        }

    }

    private void removeBricksFromRoot() {
        for(Brick brick: bricks) {
            root.getChildren().remove(brick.myBrick);
        }
    }


    public void setUpLevel(int level, String map_file) {

        File file = new File(map_file);
        Scanner sc = null;
        random = new Random();
        bricks = new HashSet<Brick>();
        powerUps = new HashSet<PowerUps>();
        ImageView myBrick;

        game_level = level;
        map_level = 1;

        try {
            sc = new Scanner(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (sc.hasNextInt()) {
            int type = sc.nextInt();
            Brick temp = new Brick(type);
            bricks.add(temp);
            if (temp.getNumBricks(bricks) != 0 && temp.getNumBricks(bricks) % 7 == 0) {
                map_level += temp.getBrick_image().getHeight() + 20;
            }
            myBrick = temp.myBrick;
            myBrick.setX(myBrick.getBoundsInLocal().getWidth() * (temp.getNumBricks(bricks) % 7) - myBrick.getBoundsInLocal().getWidth());
            myBrick.setY(map_level);
            if(type != 0) {
                root.getChildren().add(myBrick);
            }
        }
    }


    // What to do each time a key is pressed
    private void handleMouseInput(Stage stage, double x, double y) {
        startGame(stage);
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
