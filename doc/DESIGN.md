### **This section describes the design of the code for BrickBreaker Showdown**



#### **What are the project's design goals, specifically what kinds of new features did you want to make easy to add?**

My main goal when I was designing my project was to separate the functionality of the ball, paddle and bricks from the basic rules 
and structure of this variant of the game. The idea was that adding additional functionality to the ball, paddle or bricks i.e new power ups
or potential twists based on the same basic underlying game of Brick Breaker, would be easy to add. I believe I accomplished
this goal.



#### **Describe the high-level design of your project, focusing on the purpose and interaction of the core classes**


I separated my code into 5 distinct classes:

1. BrickBreakerShowdown - Main class. This class 'runs' the game by configuring the levels and the game loop,
                          and calling all the relevant functions each pass of the game loop
2. Bouncer              - Class that defines a bouncer. It contains methods to check for bouncer interactions with all
                          objects the bouncer can interact with.
3. Paddle               - Serves as a class that represents each player and their paddle. Contains functions
                          to check for paddle interactions with power ups and to keep track of player score
4. Brick                - Class used to create new bricks. New bricks are created ready to be placed on the map
                          and so this class must be used in conjunction with the setuplevel method in BrickBreakerShowdown
                          and a corresponding map config file in resources.
5. PowerUps             - Class used to create new power-ups. New power ups can be defined here


BrickBreakerShowdown initialises the Scene, adds two paddle objects and two bouncer objects to the scene, sets up the first
level from a configuration file, and then initiates the game loop. It also uses HashSets to keep track of all the Brick and 
PowerUp objects being added to the Scene.


**The game loop continuously calls the following functions for both bouncer objects:**


 **bouncer1.updatePosition(elapsedTime)** 
 - to move the bouncers (elapsedTime is an argument of the game loop function)
 
 **bouncer1.checkInBounds(SIZE, WINDOW_SIZE)** 
 - checks if the bouncer has hit any of the edges of the screen. Bounce if yes
 
 **bouncer1.checkPaddleIntersection(paddle1)** 
 - checks if the bouncer has hit its paddle. Bounce if yes
 
 **bricks = bouncer1.checkBrickIntersection(root, bricks, paddle1)**
  - checks if bouncer hit a brick. Brick is removed from HashMap. If it needs to be replaced, it is. The updated HashMap 
  is returned to BrickBreakerShowdown to update its version of HashSet<Brick> bricks. Paddle score is updated.
  
 **score1.setText(paddle1.getMyScore())**     
 - Score set on the Scene is updated for each player so that values dynamically change as bricks are destroyed.



#### **What assumptions or decisions were made to simplify your project's design, especially those that affected adding required features**

I assumed that the requirement in terms of additions to the game would be in the form of variations to the classic game of Brick Breaker that
involves breaking bricks using a ball that bounces off objects, and a paddle that can be controlled with keys or a mouse. I made it easy to make changes 
to the functionality of the paddles, balls, bricks and powerups. Changing the premise of the game i.e if the game were to not use a paddle but use click
input to save the ball from falling off the screen, this would render a lot of my organisation useless. 



#### **Describe, in detail, how to add new features to your project, especially ones you were not able to complete by the deadline**

**The following are the features I believe would be most straightforward to add to my design:**

1. Changing the abilities of the paddle 
- the way its position is updated based on the key input, adding special abilities based on power ups or changing
how score is calculated

**How to do it:** 

Altering the moveLeft(int size) and moveRight(int size) functions can easily change how much and how the paddle moves. To 
add a special ability based on a power up, add a method corresponding to the functionality of the power up to the Paddle class
and call it from the power up class when the power up is collected by the paddle through the checkIntersectionWithPaddle method in the 
PowerUp class.

2. Changing the abilities of the ball
- Altering the objects the ball interacts with, what it does when it interacts with them

**How to do it:**

Altering the checkPaddleIntersection and checkBrickIntersection methods in the Bouncer class can be used to change the behavior of the ball
when it interacts with these objects. To add more objects for the ball to interact with, add corresponding checkIntersectionWithObject methods
in the Bouncer class and call them for each bouncer from BrickBreakerShowdown within the game loop.

3. Changing the abilities and properties of the bricks
- Adding new types of bricks, changing the behaviour of existing bricks (i.e changing their health, points or designs)

**How to do it:**

The Brick constructor creates bricks based on the brick_type its given. Adding new brick types involves adding new if (brick_type == this.brick_type)
clauses to the Brick constructor. To change the functionality of the bricks, their initial health and point values can be changed within the same constructor.
To make them do different things, like move around for example, a method accepting a HashSet of Brick objects will have to be created in the Brick class.
This method will be called in the game loop with the current HashSet<Brick> bricks HashSet passed as the parameter.

4. Adding PowerUps
- Adding new power ups

**How to do it:**

Update PowerUp constructor to accept more power up codes. Add functionality by adding methods in the class affected by the power up to carry out the
power up's function i.e a function in the bouncer class to make an additional bouncer for a given paddle. Call this function from the checkIntersectionWithPaddle
method in the PowerUp class.



                                      