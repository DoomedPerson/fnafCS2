


// TO PROPERLY PLAY WITH HIGH FPS CHANGE LINE 301 private static final int DELAY IN DRAWING PANEL
// if you want to play in low fps you dont have to though, but some of our timers are frame dependent so the game may be harder or easier.
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;






class main implements KeyListener {

    static int n = 32;

    static MazeGenerator maze = new MazeGenerator(n, n);
    static Player player;
    


    boolean gameStarted = false;
    boolean gameStarting = false; // used for displaying the transition to the main screen.
    boolean gameLost = false;
    long gameStartingMS = 0;

    boolean gameClose = false;
    boolean inMenu = true;


    DrawingPanel panel;
    Graphics g;


    public static void main(String[] args) {
        main x = new main();
        
        x.panel = new DrawingPanel(896, 896);
        x.g = x.panel.getGraphics();

        x.blockSize = x.panel.HEIGHT/n;
        

        

       try {
           maze.reference = x;
           x.drawMenu(x.panel, x.g);
           Player p = new Player(maze);
           player = p;
           x.panel.addKeyListener(p);
           x.draw(x.panel, x.g);
           x.FinalScreen(x.panel, x.g);
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

    String playerInitials = "";
    boolean grabbingInitials = false;

    int frames = 0; // General time telling device, however, System.currentTimeMilliseconds() would be the most succinct.
    boolean start_search = true;
    Map.Entry<Integer, Integer> start, end, current;

    boolean pathneeded = true;

    

    int enemyx = 0;
    int enemyy = 0;

    int blockSize;

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

    public void FinalScreen(DrawingPanel panel, Graphics g) throws InterruptedException {
        
        String text = "Game Over";
        int textY = 400; 
        float opacity = 0.0f; 
        for (int i = 0; i < 100; i++)
        {

            g.setColor(Color.BLACK);
            g.fillRect(0,0,896,896);

            g.setFont(new Font("Arial", Font.PLAIN, 50));
            g.setColor(new Color(255,255,255, (int)(i*255.0/100.0)));
            
            // Center the text horizontally, adjust vertical position (textY)
            FontMetrics metrics = g.getFontMetrics();
            int x = (panel.getWidth() - metrics.stringWidth(text)) / 2;
            g.drawString(text, x, textY-i);

            Thread.sleep(20);
        }

        if (gameLost == false)
        {
            // you won!! you had this much energy. enter your initials.
            text = "You won!";
            textY = 400;  
            int delay = 60;

            for (int i = 0; i <= text.length(); i++) {
                
                g.setFont(new Font("Arial", Font.PLAIN, 50));
                g.setColor(new Color(20, 220, 20)); 

                FontMetrics metrics = g.getFontMetrics();
                int x = (panel.getWidth() - metrics.stringWidth(text)) / 2;
        
                // Draw the substring of the text up to the current index
                g.drawString(text.substring(0, i), x, textY);
        
                // Pause to simulate typing speed
                try {
                    Thread.sleep(delay);  // Adjust the delay for faster/slower typing
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        
            }

            text = "You had " + Double.toString(player.disPower) + "% power remaining.";
            textY = 500;  

            for (int i = 0; i <= text.length(); i++) {
                
                g.setFont(new Font("Arial", Font.PLAIN, 50));
                g.setColor(new Color(255, 255, 255)); 

                FontMetrics metrics = g.getFontMetrics();
                int x = (panel.getWidth() - metrics.stringWidth(text)) / 2;
        
                // Draw the substring of the text up to the current index
                g.drawString(text.substring(0, i), x, textY);
        
                // Pause to simulate typing speed
                try {
                    Thread.sleep(delay);  // Adjust the delay for faster/slower typing
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        
            }

            text = "Enter your initials:";
            textY = 600;  

            for (int i = 0; i <= text.length(); i++) {
                
                g.setFont(new Font("Arial", Font.PLAIN, 50));
                g.setColor(new Color(255, 255, 255)); 

                FontMetrics metrics = g.getFontMetrics();
                int x = (panel.getWidth() - metrics.stringWidth(text)) / 2;
        
                // Draw the substring of the text up to the current index
                g.drawString(text.substring(0, i), x, textY);
        
                // Pause to simulate typing speed
                try {
                    Thread.sleep(delay);  // Adjust the delay for faster/slower typing
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        
            }


            grabbingInitials = true;

            

        }
        else
        {
            // you lost! try again..

            
            text = "You lost...";
            textY = 400;  
            int delay = 60;
        
            
            for (int i = 0; i <= text.length(); i++) {
                
                g.setFont(new Font("Arial", Font.PLAIN, 50));
                g.setColor(new Color(255, 20, 20)); 

                FontMetrics metrics = g.getFontMetrics();
                int x = (panel.getWidth() - metrics.stringWidth(text)) / 2;
        
                // Draw the substring of the text up to the current index
                g.drawString(text.substring(0, i), x, textY);
        
                // Pause to simulate typing speed
                try {
                    Thread.sleep(delay);  // Adjust the delay for faster/slower typing
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        
            }

            
            text = "try again next time...";
            textY = 500;
            delay = 80;  
        
            
            for (int i = 0; i <= text.length(); i++) {
                
                g.setFont(new Font("Arial", Font.PLAIN, 50));
                g.setColor(new Color(255, 20, 20)); 

                FontMetrics metrics = g.getFontMetrics();
                int x = (panel.getWidth() - metrics.stringWidth(text)) / 2;
        
                
                g.drawString(text.substring(0, i), x, textY);
        
                
                try {
                    Thread.sleep(delay);  
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        
            }

            g.setColor(Color.WHITE);
            g.drawRect(360, 700, 200, 80);  // Button border
            g.drawString("Exit", 396, 760);

            panel.onClick( (x, y) -> checkCoordinatesForMenu(x, y));
        }


        boolean grab = true;


        while (gameClose == false)
        {
            if (grabbingInitials == true)
            {


                if (player.isPressed && Character.isLetter(player.currentLetter) && grab == true)
                {
                    grab = false;
                    playerInitials = playerInitials + player.currentLetter;

                }
                else if (player.isPressed == false || !Character.isLetter(player.currentLetter))
                {
                    grab = true;
                }
                g.setColor(Color.BLACK);
                g.fillRect(250, 640, 600, 120);
                g.setFont(new Font("Arial", Font.PLAIN, 50));
                g.setColor(new Color(255, 255, 255)); 


                FontMetrics metrics = g.getFontMetrics();
                int x = (panel.getWidth() - metrics.stringWidth(playerInitials.toUpperCase())) / 2;
        
                
                g.drawString(playerInitials.toUpperCase(), x, 680);
                if (playerInitials.length() >= 3)
                {
                    break;
                }
            }

            

            Thread.sleep(1);
        }

        for (double i = 0; i < 1; i+=0.01)
        {
            g.setColor(new Color(0,0,0, (int)(70*i)));
            g.fillRect(0, 0, panel.getWidth(), panel.getHeight()); // Left border
            Thread.sleep(20);
        }

        if (playerInitials.length() > 1)
        {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("Scores.txt", true))) {
                // Write some data to the file
                writer.write(playerInitials + " " + player.disPower);
                writer.newLine(); // Add a newline character
                } catch (IOException e) {
                e.printStackTrace();
                }
                // honestly i feel there are one hundred better ways to do this perhaps a hashmap.
                ArrayList<String> names = new ArrayList<String>(); 

                ArrayList<Double> scores = new ArrayList<Double>();

                try {
                    Scanner sc = new Scanner(new File("Scores.txt"));
                    while (sc.hasNextLine())
                    {
                        String newLine = sc.nextLine();
                        String[] words = newLine.split(" ");
                        String Name = words[0];
                        double Score = Double.parseDouble(words[1]);
                        names.add(Name);
                        scores.add(Score);
                    }
                    sc.close();

                    for (int i = 0; i < scores.size(); i++)
                    {
                        for (int j = 0; j < scores.size()-1; j++)
                        {
                            if (scores.get(j) < scores.get(j + 1))
                            {
                                double scoreHolder = scores.get(j+1);
                                String nameHolder = names.get(j+1);
                                scores.set(j+1, scores.get(j));
                                names.set(j+1, names.get(j));
                                scores.set(j, scoreHolder);
                                names.set(j, nameHolder);
                            }
                        }
                    }
                    g.setColor(Color.BLACK); 
                    g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
                    g.setColor(new Color(255,255,255));
                    FontMetrics metrics = g.getFontMetrics();
                    int x = (panel.getWidth() - metrics.stringWidth("Top Scores")) / 2;
                    g.drawString("Top Scores", x, 100);
                    for (int i = 0; i < Math.min(scores.size(), 5); i++)
                    {
                        g.setColor(new Color(220-i*10, 50-i*10, 50-i*10));
                        g.setFont(new Font("SanSerif", Font.BOLD, 30));
                        metrics = g.getFontMetrics();
                        x = (panel.getWidth() - metrics.stringWidth(names.get(i).toUpperCase() + ": " + scores.get(i))) / 2;
                        g.drawString(names.get(i).toUpperCase() + ": " + scores.get(i), x, 160+50*(i));
                        Thread.sleep(80); 
                    }
                    g.setColor(new Color(160, 160, 80));
                    g.setFont(new Font("SanSerif", Font.BOLD, 30)); 

                    metrics = g.getFontMetrics();
                    x = (panel.getWidth() - metrics.stringWidth("Press space to close the game.")) / 2;
                    g.drawString("Press space to close the game.", x, 500);
                    gameClose = true;
                    while (gameClose == true)
                    {
                        
                        Thread.sleep(20);
                    }
                    return; // game has finished lets go back to main.
                }
                catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    public void spawnEnemy()
    {

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
        GameTimer timer = new GameTimer(10);
        timer.start();

        while (gameClose == false) {

            if (pathneeded == true)
            {
                finishedPath = maze.getPath(enemyx,enemyy,maze.width/2,maze.height/2);
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
            else if (finishedPath.size() == 0)
            {
                ArrayList<Map.Entry<Integer, Integer>> neighbors = maze.unvisited_neighborsDumb(enemyx,enemyy);
        
    
                if (!neighbors.isEmpty()) {
                    Map.Entry<Integer, Integer> next = neighbors.get(random.nextInt(neighbors.size()));
                    maze.grid[enemyy][enemyx] &= ~maze.aiPresent;
                
                    enemyx = next.getKey();
                    enemyy = next.getValue();
    
                    maze.grid[enemyy][enemyx] |= maze.aiPresent;
        
                    Thread.sleep(100);
                }
    
            
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

                    if ((cell & maze.LIGHT) != 0)
                    {
                        g2d.setColor(new Color(255, 255, 150, 140));
                        g2d.fillRect(xPos, yPos, blockSize, blockSize);
                    }

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
            g2d.setColor(Color.WHITE);
            g2d.drawString(Double.toString(player.disPower), width-200, height-40);
            g2d.drawString(Integer.toString(((12 + (90 - timer.getSecondsRemaining()) / 15) % 12) == 0 ? 12 : ((12 + (90 - timer.getSecondsRemaining()) / 15) % 12)) + "am", 20, height-40);

            // Draw boundary rectangle around the maze
            g2d.setColor(new Color(31, 32, 43));
            g2d.fillRect(0, 0, blockSize / 4, height + 200); // Left border
            g2d.fillRect(width - blockSize / 4, 0, blockSize / 4, height + 200); // Right border
            g2d.fillRect(0, height + 200 - blockSize / 4, width, blockSize / 4); // Bottom border

            if (enemyx == maze.width/2 && enemyy == maze.height/2 && timer.getSecondsRemaining() < 5)
            {
                // we could jumpscare
                
                gameLost = true;
                for (double i = 0; i < 1; i+=0.01)
                {
                    g2d.setColor(new Color(0,0,0, (int)(70*i)));
                    g2d.fillRect(0, 0, width, height); // Left border
                    Thread.sleep(20);
                }
                return;
            }
            if (timer.getSecondsRemaining() <= 0)
            {
                for (double i = 0; i < 1; i+=0.01)
                {
                    g2d.setColor(new Color(0,0,0, (int)(70*i)));
                    g2d.fillRect(0, 0, width, height); // Left border
                    Thread.sleep(10);
                }
                gameLost = false;
                return;
            }

            player.powerDeduction();

            Thread.sleep(16);
        }    


    }


}
