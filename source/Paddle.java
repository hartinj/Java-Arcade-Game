//Java Implementation of Atari Breakout

//Filename: Paddle.java
//Author: John Hartinger (hartinj@bu.edu)
//Date: June 2014
//Prog Description: Class contains all pertinent data and methods for the paddle object in the Breakout game, including
//                  position (represented as X and Y coordinates), size, speed, and color.
//---------------------------------------------------------------------------------------------------------------------------//

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Paddle
{
  //instance variables:
  private Point location;
  private Color color;
  private double prevPositionX;
  private double prevPositionY;
  //constants:
  public static final int paddleWidth = 100;
  public static final int paddleHeight = 15;
  public static final int paddleArcWidth = 20;
  public static final int paddleArcHeight = 20;
  public static final int paddleSpeed = 20;
  
  ////Constructor////
  
  public Paddle(int xLocation, int yLocation)
  { //Constructor takes an (X,Y) coordinate as input
    this.location = new Point(xLocation, yLocation);
    this.color = new Color(128, 0, 128); //Default paddle color is purple
  }
  
  ////Accessors////
  
  public double getPaddleLocationX()
  { //Method returns paddle's X coordinate
    return this.location.getX();
  }
  
  public double getPaddleLocationY()
  { //Returns paddle's Y coordinate
    return this.location.getY();
  }
  
  ////Draw method////
  
  public void drawPaddle(Graphics g)
  { //Method draws paddle object.
    this.undrawPaddle(g);
    g.setColor(this.color);
    g.fillRoundRect((int)this.location.getX(), (int)this.location.getY(), paddleWidth, paddleHeight, paddleArcWidth, paddleArcHeight);
    g.setColor(Color.lightGray);
    g.drawRoundRect((int)this.location.getX(), (int)this.location.getY(), paddleWidth, paddleHeight, paddleArcWidth, paddleArcHeight);
  }
  
  public void undrawPaddle(Graphics g)
  { //Method clears space around paddle object
    g.clearRect((int)this.prevPositionX, (int)this.prevPositionY, paddleWidth + 5, paddleHeight + 5);
  }
  
  ////Move method: Defines how paddle moves accross screen////
  
  public void movePaddle(int movementCode)
  { //Paddle movement is fairly simple. It can only move in the x direction. 
    this.prevPositionX = this.location.getX();
    this.prevPositionY = this.location.getY();
    if (movementCode == KeyCodes.LEFT_ARROW_KEY)
    {
      if (this.location.getX() - paddleSpeed >= BreakOut.X_OFFSET)
      {
        this.location.translate(-paddleSpeed, 0);
      }
    }
    else if (movementCode == KeyCodes.RIGHT_ARROW_KEY)
    {
      if (this.location.getX() + paddleWidth + paddleSpeed <= BreakOut.GAME_WINDOW_WIDTH)
      {
        this.location.translate(paddleSpeed,0);
      }
    }
  }
  
}