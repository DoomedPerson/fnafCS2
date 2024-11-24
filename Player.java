import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Player implements KeyListener {
    // Importing grid
    public MazeGenerator maze;

    // This is to keep track of what doors are closed
    public static int door1closed = 0;
    public static int door2closed = 0;
    public static int door3closed = 0;
    public static int door4closed = 0;

    // This keeps track of the power, if it is zero, normal gameplay stops, and you will enter a power out scene
    public static double actPower = 100.0;

    // This is the power that is displayed to the player. It should be rounded down to the nearest ones place
    // so the player will usually have more power than they think they have
    public static double disPower = 100;

    // this is related to the power usage that will be used to decrease the power
    public static void powerDeduction() {
        // this is the function that controls the power you have left
        actPower -= (door1closed + door2closed + door3closed + door4closed) / 4;
        disPower = (actPower / 1);
    }
// Handles key presses
    @Override
    public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode(); // reminds me of Lua
        //the key being pressed
    System.out.println("hey");
    switch (keyCode) {
        case KeyEvent.VK_W:
        if (door1closed == 0) {
            door1closed = 1;
            
        } else {
            door1closed = 0;
            
        }
        break;
        case KeyEvent.VK_S:
        if (door3closed == 0) {
            door3closed = 1;
            
        } else {
            door3closed = 0;
        }
        break;
        case KeyEvent.VK_A:
        if (door2closed == 0) {
            door2closed = 1;
            maze.closeDoor(1, 1);
        } else {
            door2closed = 0;
            maze.closeDoor(1, 0);
        }
        break;
        case KeyEvent.VK_D:
        if (door4closed == 0) {
            door4closed = 1;
            maze.closeDoor(2, 1);
        } else {
            door4closed = 0;
            maze.closeDoor(2, 0);
        }
        break;
        case KeyEvent.VK_SPACE:
            break;
    }
}

// Not used (for now)
public void keyReleased(KeyEvent e) {
    // Can I have $5
}

// Not used for now
public void keyTyped(KeyEvent e) {
// im sure we could specify what functions we want to import, but the way I
// did it really wants me to override this function so.
}

    public Player(MazeGenerator importmaze) {
        maze = importmaze;

        // The player will be able to press buttons to toggle the door closing
        // Every second the usage will be subtracting from your actual power, visually rounded down

        // Cams should have an effect on your power if they are implemented, adding 1 extra usage

        // Power drainage should be kept at a pretty average level, not "hard" but not "basically closed all the doors for most of the night"
        
        System.out.println("key listening");
        int il = 0;
        for (int i = 0; il < 0; il++) {
            keyPressed(null);
// Idk what to use the for loop dor yet

            // Only the player should affect power usage unless a mechanic that drains power that is not controlled by the
            // Player is implemented (Which is highly unlikely given time constraints)

        }
        powerDeduction();
    }
}
