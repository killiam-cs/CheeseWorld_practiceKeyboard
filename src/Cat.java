
import java.awt.*;

public class Cat {

    public int xpos;                //the x position
    public int ypos;                //the y position
    public int width;
    public int height;
    public boolean isAlive;            //a boolean to denote if the hero is alive or dead.
    public int dx;                    //the speed of the hero in the x direction
    public int dy;                    //the speed of the hero in the y direction
    public Rectangle rec;
    public Image pic;

    // movement booleans
    public boolean right;
    public boolean down;
    public boolean left;
    public boolean up;
    public boolean jumping;

    public Cat(int pXpos, int pYpos, int dxParameter, int dyParameter) {

        xpos = pXpos;
        ypos = pYpos;
        width = 100;
        height = 100;
        dx = dxParameter;
        dy = dyParameter;
        isAlive = true;
        rec = new Rectangle(xpos, ypos, width, height);

    } // constructor

    //move( ) method for a keyboard controlled character
    public void move() {
        xpos = xpos + dx;
        ypos = ypos + dy;

        dy = dy + 1;

        if (ypos > 250) {
            ypos = 250;
        }

        if (xpos > 1000 - width || xpos < 0) {
            dx = -dx;
        } // right/left bounce

        if (ypos < 0 || ypos + height > 700) {
            dy = -dy;
        } // up/down bounce

        rec = new Rectangle(xpos, ypos, width, height);
    }

}
