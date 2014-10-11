//Java Implementation of Atari Breakout

//Filename: BreakOut.java
//Author: John Hartinger (hartinj@bu.edu)
//Much thanks to Andrew Tarrh for invaluable help during the final debugging process.
//Date: June 2014
//Prog Description: Class creates game-play window and facilitates game-play. Note that game has not been desgined for
//                  play on windows with a width less than 250 and a height less than 200.
//---------------------------------------------------------------------------------------------------------------------------//

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import javax.swing.JOptionPane;


public class BreakOut extends javax.swing.JFrame
{
  //constants used to define the game field and bounds
  public static final int GAME_WINDOW_HEIGHT = 512;
  public static final int GAME_WINDOW_WIDTH = 512;
  public static final int GAME_HEIGHT = 440;
  public static final int GAME_WIDTH = 440;
  public static final int X_OFFSET = 8; //Horizontal offset required so blocks don't appear jammed in upper-left corner of screen
  public static final int Y_OFFSET = 30; //Vertical offset required for same reason as horizontal offset
  public static final int TIME = 10;
  
  // member variables
  private KeyHandler kh;//handle keyboard events
  private Block[][] blockArray;//array of Block objects
  private Ball ball;
  private Paddle paddle;
  private int blockArrayWidth = 5;
  private int blockArrayHeight = 9;
  private Graphics g;
  private Color[] randColorArray = {Color.blue, Color.red, Color.orange, Color.cyan, Color.green, Color.yellow};
  private Color defaultBackground;//Stores the color of the background of the graphics window
  
  ////Constructor////
  
  public BreakOut()
  {
    initComponents();
    
    //subscribe for events from keyboard
    kh = new KeyHandler();
    addKeyListener(kh);
  }
  
  ////Initializer method////
  
  public void initComponents()
  {
    setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    setSize(GAME_WINDOW_WIDTH, GAME_WINDOW_HEIGHT);
    setTitle("BreakOut");
    this.defaultBackground = getBackground();
    
    //First initialize Block object array
    this.blockArray = new Block[this.blockArrayWidth][this.blockArrayHeight];
    for (int i = 0; i < this.blockArrayWidth; i++)// width
    {
      for (int j = 0; j < this.blockArrayHeight; j++)// height
      {
        int randIndex = (int)(Math.random() * this.randColorArray.length);
        this.blockArray[i][j] = new Block(X_OFFSET + (i*Block.blockWidth), Y_OFFSET + (j*Block.blockHeight), 
                                          this.randColorArray[randIndex], this.defaultBackground);
      }
    }
    
    //Next, initialize paddle object
    this.paddle = new Paddle((GAME_WINDOW_WIDTH / 2) - Paddle.paddleWidth / 2, GAME_WINDOW_HEIGHT - Y_OFFSET);
    
    //Finally, initialize ball object
    this.ball = new Ball((GAME_WINDOW_WIDTH / 2), GAME_WINDOW_HEIGHT - 2*Y_OFFSET, Color.red);
    
  } 
  
  ////Paint method////
  
  public void paint(Graphics graph)
  { //Method handles all graphical rendering. From any other method, repaint() is called to redraw the screen 
    //First paint Block objects:
    for (int i = 0; i < this.blockArrayWidth; i++)
    {
      for (int j = 0; j < this.blockArrayHeight; j++)
      {
        this.blockArray[i][j].drawBlock(graph); 
      } 
    }
    
    //Paint paddle:
    this.paddle.drawPaddle(graph);
    
    //Paint ball:
    this.ball.drawBall(graph);
  }
  
  ////Wait method: Enables time delay between repainting of graphics window////
  
  public static void wait(int milliseconds)
  {
    try{Thread.sleep(milliseconds);}
    catch(InterruptedException e){}
  }
  
  ////Play method (i.e. main game loop)////
  
  public void play()
  { 
    this.g = getGraphics();
    boolean gameover = false;
    boolean win = false;
    while (!gameover && !win)
    { 
      //Check to see if ball has hit the bottom border of the game-play window. If so: GAME OVER
      if (this.ball.getYPosition() > GAME_WINDOW_HEIGHT)
      {
        gameover = true;
      }
      
      //Check to see if user has won the game (i.e. if there are no blocks left)
      int counter = 0;//this.blockArrayWidth * this.blockArrayHeight;
      for (int i = 0; i < this.blockArrayWidth; i++)
      {
        for (int j = 0; j < this.blockArrayHeight; j++)
        {
          if (this.blockArray[i][j].getColor().equals(this.defaultBackground)){ counter++; } 
        }
      }
      if (counter == this.blockArrayWidth * this.blockArrayHeight){ win = true; } 
      
      //If arrow keys have been pressed, move paddle AND ball
      if(kh.getKeyCode() == KeyCodes.LEFT_ARROW_KEY || kh.getKeyCode() == KeyCodes.RIGHT_ARROW_KEY)
      {
        this.paddle.movePaddle(kh.getKeyCode());
        kh.setKeyCode(100);
        this.ball.moveBall(this.paddle, this.blockArray);
        repaint();
        wait(TIME);
      }
      //If user presses 'p' or 'P', pause game
      else if(kh.getKeyCode() == KeyCodes.UPPER_CASE_P || kh.getKeyCode() == KeyCodes.LOWER_CASE_P)
      {
        this.g.drawString("Paused (press any key other than 'p' to unpause)", GAME_WINDOW_WIDTH/3, (GAME_WINDOW_HEIGHT/2) + 20);
        int key = kh.getKeyCode();
        while (key == KeyCodes.UPPER_CASE_P)
        {
          wait(TIME);
          key = kh.getKeyCode();
        }
        this.g.clearRect((GAME_WINDOW_WIDTH/3), (GAME_WINDOW_HEIGHT/2) + 10, 300, 20);// clear the pause message
      }
      //If user has not moved the paddle or paused the game, move JUST the ball and repaint the screen
      else
      {
        this.ball.moveBall(this.paddle, this.blockArray);
        repaint();
        wait(TIME);
      }
      //System.out.println("X: " + this.ball.getXPosition() + ", Y: " + this.ball.getYPosition());
    }
    //Present message to user if they have lost the game
    if (gameover)
    {
      String msg = "Better luck next time loser!\n Try again?";
      setTitle("GAME OVER");
      int choice = JOptionPane.showConfirmDialog(getParent(), msg, "GAME OVER", JOptionPane.YES_NO_OPTION);
      if (choice == JOptionPane.YES_OPTION)
      {
        this.g.clearRect(0, GAME_HEIGHT/2, GAME_WIDTH, GAME_HEIGHT);
        initComponents();
        play();
      }
      else
      {
        System.exit(0);
      }
    }
    if (win)
    {
      String msg = "Congrats! You won!";
      setTitle("WIN WIN WIN WIN WIN WIN WIN WIN WIN");
      JOptionPane.showMessageDialog(getParent(), msg, "SUCCESS", JOptionPane.OK_OPTION);
    }
    
  }
  
  ////KeyHandler sub-class////
  
  public class KeyHandler extends KeyAdapter
  { // This is an inner class that enables the game loop to receive keyboard events.
    
    ////Fields////
    private int keyCode;
    
    ////Methods////
    public void keyPressed(KeyEvent ke)
    { //Method returns ASCII value of key pressed           
      int key = ke.getKeyCode();
      this.keyCode = key;
    }
    
    public int getKeyCode(){return this.keyCode;}
    
    public void setKeyCode(int code){this.keyCode = code;}
    
  }
  
  ////Main method////
  
  public static void main(String[] args)
  { //Main method starts the game; delegates all work to the play method
    BreakOut game = new BreakOut();
    game.setVisible(true);
    wait(TIME);
    game.play();                
  } 
  
}
