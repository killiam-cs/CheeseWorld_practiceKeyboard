//Import Section
//Add Java libraries needed for the game

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.*;

public class CheeseWorld implements Runnable, KeyListener {

    //Variable Definition Section

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;
    public BufferStrategy bufferStrategy;

    //Declare the character objects
    public Mouse jerry;
    public Cheese cheese;
    // STEP 1 FOR CREATING AN ARRAY
    public Cheese[] manyCheese;
    public Cat tom;

    public String winner = "";
    public boolean gameStart = false;
    public boolean gameOver = false;

    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        CheeseWorld myApp = new CheeseWorld();   //creates a new instance of the game
        new Thread(myApp).start();               //creates a threads & starts up the code in the run( ) method
    }

    // Constructor Method - setup portion of the program
    // Initialize your variables and construct your program objects here.
    public CheeseWorld() {

        setUpGraphics();

        canvas.addKeyListener(this);

        //create (construct) the objects needed for the game
        tom = new Cat(650, 250, 0, 0);
        jerry = new Mouse(200, 300, 0, 0);
        cheese = new Cheese(400, 300, 3, -4);
        // STEP 2 FOR CREATING AN ARRAY
        // construct array
        manyCheese = new Cheese[5];
        // fill array
        for (int i = 0; i < manyCheese.length; i=i+1) {
            manyCheese[i] = new Cheese((int)(Math.random()*900), i*100,
                    (int)(Math.random()*5-2), (int)(Math.random()*5-2));
            while (manyCheese[i].dx == 0) {
                manyCheese[i].dx = (int)(Math.random()*5-2);
            }
            while (manyCheese[i].dy == 0) {
                manyCheese[i].dy = (int)(Math.random()*5-2);
            }
            //re-locate cheese if it is within 50 of tom/jerry
            //condition: (cheese x within 50 of tom x AND
            // cheese y within 50 of tom y) OR
            // (cheese x within 50 of jerry x AND
            // cheese y within 50 of jerry y)
            while ((Math.abs(manyCheese[i].xpos - tom.xpos) <= 50 &&
                    Math.abs(manyCheese[i].ypos - tom.ypos) <= 50) ||
                    (Math.abs(manyCheese[i].xpos - jerry.xpos) <= 50 &&
                    Math.abs(manyCheese[i].ypos - jerry.ypos) <= 50)) {
                System.out.println("relocating cheese " + i);
                manyCheese[i].xpos = (int)(Math.random()*900);
            }
        }


        //load images
        cheese.pic = Toolkit.getDefaultToolkit().getImage("cheese.gif");
        for (int i = 0; i < manyCheese.length; i=i+1) {
            manyCheese[i].pic = Toolkit.getDefaultToolkit().getImage("cheese.gif");
        }
        jerry.pic = Toolkit.getDefaultToolkit().getImage("jerry.gif");
        tom.pic = Toolkit.getDefaultToolkit().getImage("tomCat.png");

    } // CheeseWorld()


//*******************************************************************************

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        while (true) {
            if (gameStart == true && gameOver == false) {
                checkKeys();
                moveThings();           //move all the game objects
                checkIntersections();   // check character crashes
            }
            render();               // paint the graphics
            pause(20);         // sleep for 20 ms
        }
    }

    public void checkKeys() {
        if (tom.left == true) {
            tom.dx = -3;
        }
        else if (tom.right == true) {
            tom.dx = 3;
        }
        else {
            tom.dx = 0;
        }

//        if (tom.up == true) {
//            tom.dy = -3;
//        }
//        else if (tom.down == true) {
//            tom.dy = 3;
//        }
//        else {
//            tom.dy = 0;
//        }

//        if (tom.jumping == true) {
//            tom.dy = tom.dy + 1;
//        }
//        if (tom.ypos > 250) {
//            tom.ypos = 250;
//        }

    }

    public void moveThings() {
        jerry.move();
        cheese.move();
        for (int i = 0; i < manyCheese.length; i++) {
            manyCheese[i].move();
        }
        tom.move();
    }

    public void checkIntersections() {
        if (tom.rec.intersects(jerry.rec)) {
            jerry.isAlive = false;
            winner = "Tom";
            gameOver = true;
        }
        if (jerry.rec.intersects(cheese.rec)) {
            cheese.isAlive = false;
            winner = "Jerry";
            gameOver = true;
        }
        if (tom.rec.intersects(cheese.rec)) {
            tom.isAlive = false;
            winner = "cheese";
            gameOver = true;
        }
        for (int i = 0; i < manyCheese.length; i++) {
            if (tom.rec.intersects(manyCheese[i].rec)) {
                tom.isAlive = false;
                winner = "cheese";
                gameOver = true;
            }
        }
    }

    //paints things on the screen using bufferStrategy
    public void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        if (gameStart == false) {
            g.setColor(Color.BLUE);
            g.fillRect(0,0,1000,800);
            g.setColor(Color.YELLOW);
            g.drawString("Press enter to start", 350, 300);
            int textX = 50;
            int textY = 50;
            g.setFont(new Font("TimesRoman", Font.BOLD, 20));
            g.drawString("Tom tries to get Jerry and avoid cheese", textX, textY);
            g.drawString("Jerry tries to eat the cheese and avoid Tom", textX, textY + 30);
            g.drawString("First one to succeed wins!",textX, textY + 60);

        } // gameStart is false (game has not started yet)
        else if (gameStart == true && gameOver == false) {

            // draw characters to the screen (only draw if they are alive)
            if (jerry.isAlive == true) {
                g.drawImage(jerry.pic, jerry.xpos, jerry.ypos, jerry.width, jerry.height, null);
            }
            if (cheese.isAlive == true) {
                g.drawImage(cheese.pic, cheese.xpos, cheese.ypos, cheese.width, cheese.height, null);
            }
            for (int i = 0; i < manyCheese.length; i=i+1) {
                g.drawImage(manyCheese[i].pic, manyCheese[i].xpos, manyCheese[i].ypos,
                        manyCheese[i].width, manyCheese[i].height, null);
            }
            if (tom.isAlive == true) {
                g.drawImage(tom.pic, tom.xpos, tom.ypos, tom.width, tom.height, null);
            }
        } // gameStart is tru (game has started)
        else {
            g.drawString(winner + " wins!!!", 450, 350);
        }

        // HW: in the holiday game, for the character that is NOT keyboard-controlled,
        // make an array of that character (e.g. an array of loraxes)
        // that behave in the same way as the original character

        g.dispose();
        bufferStrategy.show();
    }

    //Graphics setup method
    public void setUpGraphics() {
        frame = new JFrame("CheeseWorld");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
        System.out.println("DONE graphic setup");

    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        //sleep
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        System.out.println(keyCode);
        if (keyCode == 10) {
            gameStart = true;
        }

        if (keyCode == 37) {
            tom.left = true;
        }
        if (keyCode == 39) {
            tom.right = true;
        }
        if (keyCode == 32) {
            tom.dy = -15;
            tom.jumping = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        if (keyCode == 37) {
            tom.left = false;
        }
        if (keyCode == 39) {
            tom.right = false;
        }
    }
} // end of class
