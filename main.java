


// TO PROPERLY PLAY WITH HIGH FPS CHANGE LINE 301 private static final int DELAY IN DRAWING PANEL
// if you want to play in low fps you dont have to though, but some of our timers are frame dependent so the game may be harder or easier.
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;





class App implements KeyListener {

    static int n = 32;

    static MazeGenerator maze = new MazeGenerator(n, n);
    
    



    boolean gameStarted = false;
    boolean gameStarting = false; // used for displaying the transition to the main screen.
    long gameStartingMS = 0;

    boolean gameClose = false;
    boolean inMenu = true;


    DrawingPanel panel;
    Graphics g;


    public static void main(String[] args) {
        App x = new App();
        
        x.panel = new DrawingPanel(896, 896);
        x.g = x.panel.getGraphics();
        

        

       try {
           maze.reference = x;
           x.drawMenu(x.panel, x.g);
           x.panel.addKeyListener(new Player(maze));
           x.draw(x.panel, x.g);
       } catch (InterruptedException e) { // incase either titleScreen or draw get interrupted when hanging their thread up.
           e.printStackTrace();
       }

       System.exit(0); // lets close the game for the player.
    }

    // I had used something like this before but I used a stack-overflow post and https://www.geeksforgeeks.org/java-keylistener-in-awt/ to double check my work
    public void keyPressed(KeyEvent e) {
        // develop this later
    }


    public void keyReleased(KeyEvent e) {
        // develop this later
    }





    public void keyTyped(KeyEvent e) {
        // im sure we could specify what functions we want to import, but the way I did it really wants me to override this function so.
    }


    int frames = 0; // General time telling device, however, System.currentTimeMilliseconds() would be the most succinct.
    boolean start_search = true;
    Map.Entry<Integer, Integer> start, end, current;

    boolean pathneeded = true;

    int enemyx = 0;
    int enemyy = 0;

    ArrayList<Map.Entry<Integer, Integer>> finishedPath = new ArrayList<>();

    Random random = new Random();

    public void checkCoordinatesForMenu(int x, int y)
    {
        if (x >= 360 && x <= 560 && y >= 600 && y <= 680)
        {
            inMenu = false;
        }

        if (x >= 360 && x <= 560 && y >= 700 && y <= 780)
        {
            inMenu = false;
            gameClose = true;
        }
    }


    public void drawMenu(DrawingPanel panel, Graphics g) {

        panel.onClick( (x, y) -> checkCoordinatesForMenu(x, y));

            //background color
            g.setColor(Color.BLACK);
            g.fillRect(0,0,896,896);


        while (inMenu)
        {

            // Title (probably change this)
            g.setFont(new Font("DialogInput", Font.BOLD, 50));
            g.setColor(Color.WHITE);
            g.drawString("Five Nights Town", 240, 100);

            // start
            g.setColor(Color.WHITE);
            g.setFont(new Font("DialogInput", Font.PLAIN, 50));
            g.setColor(Color.WHITE);
            g.drawRect(360, 600, 200, 80);  // Button border
            g.drawString("Start", 380, 660);

            // exit
            g.setColor(Color.WHITE);
            g.drawRect(360, 700, 200, 80);  // Button border
            g.drawString("Exit", 396, 760);

            // halt the code here!!

        }

    }

    public void draw(DrawingPanel panel, Graphics g) throws InterruptedException { // perhaps the name is misleading but this is our main game loop

        while (gameClose == false) {

            if (pathneeded == true)
            {
                finishedPath = maze.getPath(enemyx,enemyy);
                pathneeded = false;
            }

            boolean gameRunning = true;

            int width  = panel.getWidth();
            int height = panel.getHeight();

            int blockSize = height/n;

            g.setColor(Color.black);
            //g.fillRect(0, 0, width, height);\

            if (start_search && finishedPath.size() > 0) {
                // Call weightedManhattenSearch to get the next position


                Map.Entry<Integer, Integer> point = finishedPath.remove(0);  // Removes and returns the first element

                maze.grid[enemyy][enemyx] &= ~maze.aiPresent;
                
                enemyx = point.getKey();
                enemyy = point.getValue();

                maze.grid[enemyy][enemyx] |= maze.aiPresent;
    
                Thread.sleep(100);
            }
            

            Graphics2D g2d = (Graphics2D) g;
            
            for (int y = 0; y < n; y++) {
                for (int x = 0; x < n; x++) {
                    int cell = maze.grid[y][x];
                    int xPos = x * blockSize;
                    int yPos = y * blockSize;

                    // Draw cell based on state
                    if (cell == 0) {
                        g2d.setColor(new Color(15, 15, 20));
                    } else if ((cell & maze.START) != 0) {
                        g2d.setColor(new Color(158, 206, 106));
                    } else if ((cell & maze.END) != 0) {
                        g2d.setColor(new Color(122, 162, 247));
                    } else if ((cell & maze.VISITED) != 0 && (cell & maze.DEAD) == 0 && (cell & maze.PATHD) == 0) {
                        g2d.setColor(new Color(15, 15, 20));
                    } else if ((cell & maze.PATH) != 0) {
                        g2d.setColor(new Color(169, 177, 214, 200));
                    } else if ((cell & maze.DEAD) != 0) {
                        g2d.setColor(new Color(15, 15, 20));
                    } else if ((cell & maze.PATHM) != 0) {
                        g2d.setColor(new Color(140, 67, 81, 200));
                    } else if ((cell & maze.PATHD) != 0) {
                        g2d.setColor(new Color(15, 15, 20));
                    } else if ((cell & maze.DEADM) != 0 && (cell & maze.START) == 0) {
                        g2d.setColor(new Color(15, 40, 20));
                    } else if ((cell & maze.DEADD) != 0 && (cell & maze.START) == 0) {
                        g2d.setColor(new Color(15, 90, 20));
                    } else if ((cell & maze.FRONTIER) != 0) {
                        g2d.setColor(new Color(247, 118, 142));
                    }

                    // Draw the rectangle for the cell
                    g2d.fillRect(xPos, yPos, blockSize, blockSize);

                    // Draw walls if present
                    g2d.setColor(new Color(31, 32, 43));
                    if ((cell & maze.N) != 0) {
                        g2d.fillRect(xPos, yPos - 2, blockSize, 4);
                    }
                    if ((cell & maze.S) != 0) {
                        g2d.fillRect(xPos, yPos + blockSize * 3 / 4 + 2, blockSize, 4);
                    }
                    if ((cell & maze.E) != 0) {
                        g2d.fillRect(xPos + blockSize * 3 / 4 + 2, yPos, 4, blockSize);
                    }
                    if ((cell & maze.W) != 0) {
                        g2d.fillRect(xPos - 2, yPos, 4, blockSize);
                    }
                    if ((cell & maze.DOOR) != 0) {
                        g2d.setColor(Color.white);
                        g2d.fillRect(xPos, yPos, blockSize, blockSize);
                    }
                    g2d.setColor(new Color(31, 32, 43));
                    
                    // Draw borders for the maze
                    if (x == width - 1) {
                        g2d.fillRect(xPos + blockSize + 2, yPos, 4, blockSize);
                    }
                    if (x == 0) {
                        g2d.fillRect(xPos, yPos, 4, blockSize);
                    }
                    if (y == height - 1) {
                        g2d.fillRect(xPos, yPos + blockSize * 3 / 4 + 2, blockSize, 6);
                    }
                    if (y == 0) {
                        g2d.fillRect(xPos, yPos, blockSize, 6);
                    }


                    if ((cell & maze.aiPresent) != 0)
                    {
                        g2d.setColor(new Color(247, 20, 20));
                        g2d.fillRect(xPos+4, yPos+4, blockSize-8, blockSize-8);
                    }
                }
            }

            // Draw boundary rectangle around the maze
            g2d.setColor(new Color(31, 32, 43));
            g2d.fillRect(0, 0, blockSize / 4, height + 200); // Left border
            g2d.fillRect(width - blockSize / 4, 0, blockSize / 4, height + 200); // Right border
            g2d.fillRect(0, height + 200 - blockSize / 4, width, blockSize / 4); // Bottom border

            Thread.sleep(16);
        }    


    }


}
