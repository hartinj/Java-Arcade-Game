//Java Implementation of Atari Breakout

//Filename: Ball.java
//Author: John Hartinger (hartinj@bu.edu)
//Date: June 2014
//Prog Description: Class contains all pertinent data and methods for the ball in the Breakout game, including 
//                  position (represented as X and Y coordinates), direction of travel, speed, size, and color. 
//---------------------------------------------------------------------------------------------------------------------------//

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Ball
{
  //instance variables:
  private Point location;// Contains x and y coordinates of ball
  private int locationCode;// Ball can only move in four directions: NE = 1, SE = 2, NW = 3, SW = 4
  private Color color;
  private double prevPositionX;
  private double prevPositionY;
  //constants:
  public static final int ballDiameter = 20;
  public static final int ballSpeed = 4;
  
  ////Constructors////
  
  public Ball()
  {
    this.location = new Point();
    this.color = Color.blue;
    this.locationCode = 1; //Initial direction of travel is north-east
  }
  
  public Ball(int x, int y, Color color)
  {
    this.location = new Point(x,y);
    this.color = color;
    this.locationCode = 1; //Initial direction of travel is north-east
  }
  
  ////Accessors////
  
  public double getXPosition(){return this.location.getX();}
  
  public double getYPosition(){return this.location.getY();}
  
  ////Draw methods////
  
  public void drawBall(Graphics g)
  {
    this.undrawBall(g);
    g.setColor(this.color);
    g.fillOval((int)this.location.getX(), (int)this.location.getY(), ballDiameter, ballDiameter);
    g.drawOval((int)this.location.getX(), (int)this.location.getY(), ballDiameter, ballDiameter);
  }
  
  public void undrawBall(Graphics g)
  {
    g.clearRect((int)this.prevPositionX - 3, (int)this.prevPositionY - 3, ballDiameter + 4, ballDiameter + 4);
  }
  
  ////Movement method: Defines how the ball moves////
  
  public void moveBall(Paddle p, Block[][] b)
  { 
    //First obtain current position of ball (used for the undrawBall() method)
    this.prevPositionX = this.location.getX();
    this.prevPositionY = this.location.getY();
    
    //Check if ball has collided with anything
    if (!this.isCollisionWithWall() && !this.isCollisionWithPaddle(p) && !this.isCollisionWithBlock(b))
    { //If ball has not collided with anything then its position will shift by equal amounts in the x and y direction.
      //The change in position is defined by the ballSpeed instance variable.
      if (this.locationCode == 1)
      { //If ball moves in NE direction, increase X, decrease Y:
        this.location.translate(ballSpeed, -ballSpeed);
      }
      else if (this.locationCode == 2)
      { //If ball moves in SE direction, increase X and Y:
        this.location.translate(ballSpeed, ballSpeed);
      }
      else if (this.locationCode == 3)
      { //If ball moves in NW direction, decrease X and Y:
        this.location.translate(-ballSpeed, -ballSpeed);
      }
      else if (this.locationCode == 4)
      { //If ball moves in SW direction, decrease X, increase Y:
        this.location.translate(-ballSpeed, ballSpeed);
      }
    }
    else
    { //If ball has collided with something, then change its locationCode to indicate how it has changed directions.
      //First check if ball has collided with the top of the game window:
      if (this.location.getY() <= BreakOut.Y_OFFSET)
      {
        if (this.locationCode == 1)
        {
          this.locationCode = 2;
          this.location.translate(ballSpeed, ballSpeed);
        }
        else if (this.locationCode == 3)
        {
          this.locationCode = 4;
          this.location.translate(-ballSpeed, ballSpeed);
        }
      }
      //Then check if ball has hit the paddle:
      if (this.isCollisionWithPaddle(p))
      {
        if (this.locationCode == 4)
        {
          this.locationCode = 3;
          this.location.translate(-ballSpeed, -ballSpeed);
        }
        else if (this.locationCode == 2)
        {
          this.locationCode = 1;
          this.location.translate(ballSpeed, -ballSpeed);
        }
      }
      //Next, if ball has hit a block, change its direction appropriately. Also, make the block invisible.
      if (this.isCollisionWithBlock(b))
      {
        for (int i = 0; i < b.length; i++)
        {
          for (int j = 0; j < b[0].length; j++)
          {
            if (this.location.getX() + ballDiameter >= b[i][j].getXLocation() && 
                this.location.getX() <= b[i][j].getXLocation() + Block.blockWidth &&
                this.location.getY() <= b[i][j].getYLocation() + Block.blockHeight &&
                this.location.getY() + ballDiameter >= b[i][j].getYLocation() &&
                b[i][j].getIsVisible())
            {
              b[i][j].setIsVisible(false);// Make block invisible
            }
          }
        }
        if (this.locationCode == 1)// NE
        {
          this.locationCode = 2;// SE
          this.location.translate(ballSpeed, ballSpeed);
        }
        else if (this.locationCode == 3)// NW
        {
          this.locationCode = 4;// SW
          this.location.translate(-ballSpeed, ballSpeed);
        }
        else if (this.locationCode == 2)// SE
        {
          this.locationCode = 1;// NE
          this.location.translate(ballSpeed, -ballSpeed);
        }
        else if (this.locationCode == 4) //SW
        {
          this.locationCode = 3;// NW
          this.location.translate(-ballSpeed, -ballSpeed);  
        }
      }
      //Finally, check if the ball has hit the side walls of the window:
      if (this.location.getX() <= BreakOut.X_OFFSET)// Left side of window
      { 
        if (this.locationCode == 3)// NW
        {
          this.locationCode = 1;
          this.location.translate(ballSpeed, -ballSpeed);
        }
        else if (this.locationCode == 4)// SW
        {
          this.locationCode = 2;
          this.location.translate(ballSpeed, ballSpeed);
        }
        
      }
      if (this.location.getX() >= BreakOut.GAME_WINDOW_WIDTH - 2*BreakOut.X_OFFSET)// Right side of window
      { 
        if (this.locationCode == 1)// NE
        {
          this.locationCode = 3;
          this.location.translate(-ballSpeed, -ballSpeed);
        }
        else if (this.locationCode == 2)// SE
        {
          this.locationCode = 4;
          this.location.translate(-ballSpeed, ballSpeed);
        }
        else if (this.locationCode == 4)// SW
        {
          this.location.translate(-ballSpeed, ballSpeed);
        }
      }

    }
    
  }
  
  ////Collision methods: Defines what is considered a collision of the ball with another surface////
  
  public boolean isCollisionWithWall()
  { //Method returns true if ball has collided with the extreme boundaries of the game-play window.
    if (this.location.getX() <= BreakOut.X_OFFSET || this.location.getX() >= BreakOut.GAME_WINDOW_WIDTH - 2*BreakOut.X_OFFSET ||
        this.location.getY() <= BreakOut.Y_OFFSET) 
    {return true;}
    return false;
    
  }
  
  public boolean isCollisionWithPaddle(Paddle p)
  { //Method returns true if ball has collided with the paddle. 
    double paddleX = p.getPaddleLocationX();
    if (this.location.getX() >= paddleX && this.location.getX() <= paddleX + Paddle.paddleWidth &&
        this.location.getY() + ballDiameter >= p.getPaddleLocationY() && 
        this.location.getY() < p.getPaddleLocationY() + Paddle.paddleHeight )
    {return true;}
    return false;
  }
  
  public boolean isCollisionWithBlock(Block[][] b)
  { //Methods returns true if ball has collided with block in the block array.
    for (int i = 0; i < b.length; i++)
    {
      for (int j = 0; j < b[0].length; j++)
      {
        
        if (this.location.getX() + ballDiameter >= b[i][j].getXLocation() && 
            this.location.getX() <= b[i][j].getXLocation() + Block.blockWidth &&
            this.location.getY() <= b[i][j].getYLocation() + Block.blockHeight &&
            this.location.getY() + ballDiameter >= b[i][j].getYLocation() &&
            b[i][j].getIsVisible())
        {return true;}
      }
    }
    return false;
  }
  
}