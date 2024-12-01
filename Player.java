import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player implements KeyListener {
    // Importing grid
    public MazeGenerator maze;

    // This is to keep track of what doors are closed
    public int door1closed = 0;
    public int door2closed = 0;
    public int door3closed = 0;
    public int door4closed = 0;

    public char currentLetter = '.';
    public boolean isPressed = false;

    // This keeps track of the power, if it is zero, normal gameplay stops, and you will enter a power out scene
    public double actPower = 100.0;

    // This is the power that is displayed to the player. It should be rounded down to the nearest ones place
    // so the player will usually have more power than they think they have
    public double disPower = 100;

    // this is related to the power usage that will be used to decrease the power
    public void powerDeduction() {
        // this is the function that controls the power you have left
        actPower -= (door1closed + door2closed + door3closed + door4closed) / 4.0 / 4.0;
        disPower = (actPower / 1);
    }
// Handles key presses
    @Override
    public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode(); // reminds me of Lua

    currentLetter = e.getKeyChar();
    isPressed = true;
        //the key being pressed
    switch (keyCode) {
        case KeyEvent.VK_W:
        if (this.door1closed == 0) {
            this.door1closed = 1;
            
        } else {
            this.door1closed = 0;
            
        }
        break;
        case KeyEvent.VK_S:
        if (this.door3closed == 0) {
            this.door3closed = 1;
            
        } else {
            this.door3closed = 0;
        }
        break;
        case KeyEvent.VK_A:
        if (this.door2closed == 0) {
            this.door2closed = 1;
            maze.closeDoor(1, 1, this, this.door1closed + this.door2closed + this.door3closed + this.door4closed);
        } else {
            this.door2closed = 0;
            maze.closeDoor(1, 0, this, this.door1closed + this.door2closed + this.door3closed + this.door4closed);
        }
        break;
        case KeyEvent.VK_D:
        if (this.door4closed == 0) {
            this.door4closed = 1;
            maze.closeDoor(2, 1, this, this.door1closed + this.door2closed + this.door3closed + this.door4closed);
        } else {
            this.door4closed = 0;
            maze.closeDoor(2, 0, this, this.door1closed + this.door2closed + this.door3closed + this.door4closed);
        }
        break;
        case KeyEvent.VK_SPACE:
            break;
    }
}

// Not used (for now)
public void keyReleased(KeyEvent e) {
    // Can I have $5
    isPressed = false;
}

// Not used for now
public void keyTyped(KeyEvent e) {
// im sure we could specify what functions we want to import, but the way I
// did it really wants me to override this function so.
}

    public Player(MazeGenerator importmaze) {
        this.maze = importmaze;

        // The player will be able to press buttons to toggle the door closing
        // Every second the usage will be subtracting from your actual power, visually rounded down

        // Cams should have an effect on your power if they are implemented, adding 1 extra usage

        // Power drainage should be kept at a pretty average level, not "hard" but not "basically closed all the doors for most of the night"
        
        System.out.println("key listening");

    }
}
