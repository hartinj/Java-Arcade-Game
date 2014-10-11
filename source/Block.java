//Java Implementation of Atari Breakout

//Filename: Block.java
//Author: John Hartinger (hartinj@bu.edu)
//Date: June 2014
//Prog Description: Class contains all the pertinent data and methods for the blocks that the user must eliminate in 
//                  the Breakout game. Class stores block location (as X and Y coordinates), dimensions, color, and
//                  a boolean value to indicate whether or not the block is visible. 
//---------------------------------------------------------------------------------------------------------------------------//

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Block
{
  //instance variables:
  private boolean isVisible;
  private Point location;
  private Color color;
  private Color defaultBackground; 
  //constants:
  public static final int blockWidth = 100;
  public static final int blockHeight = 20;
  public static final int blockArcWidth = 15;
  public static final int blockArcHeight = 40;
  
  ////Constructors////
  
  public Block()
  {
    this.location = new Point();
    this.color = Color.red;//Default color of block is red
  }
  
  public Block(int xLocation, int yLocation, Color blockColor, Color backgroundColor)
  { //Constructor takes two integers (indicating location of Block object) and two Color objects as inputs
    this.isVisible = true;
    this.location = new Point(xLocation, yLocation);
    this.color = blockColor;
    this.defaultBackground = backgroundColor;
  }
  
  ////Mutators////
  
  public void setIsVisible(boolean value){this.isVisible = value;}
  
  public void setColor(Color color){this.color = color;}
  
  public void drawBlock(Graphics g)
  { //Method draws a Block object (really a rounded rectangle) onto a Graphics object. Block object has a standard 
    //width, height, arc width, and arc height. 
    
    g.setColor(this.color);
    if (this.isVisible)
    { //If block is visible, draw the following
      g.fillRoundRect((int)this.location.getX(), (int)this.location.getY(), blockWidth, blockHeight, blockArcWidth, blockArcHeight);
      g.setColor(Color.lightGray);
      g.drawRoundRect((int)this.location.getX(), (int)this.location.getY(), blockWidth, blockHeight, blockArcWidth, blockArcHeight);
    }
    else
    { //If block is not visible, draw it such that it is the same color as the background
      this.setColor(this.defaultBackground);
      g.setColor(this.defaultBackground);
      g.fillRoundRect((int)this.location.getX(), (int)this.location.getY(), blockWidth, blockHeight, blockArcWidth, blockArcHeight);
      g.drawRoundRect((int)this.location.getX(), (int)this.location.getY(), blockWidth, blockHeight, blockArcWidth, blockArcHeight);
    }
  }
  
  ////Accessors////
  
  public boolean getIsVisible(){return this.isVisible;}
  
  public Color getColor(){return this.color;}
  
  public double getXLocation(){return this.location.getX();}
  
  public double getYLocation(){return this.location.getY();}
  
}