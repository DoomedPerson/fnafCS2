import java.util.*;
import java.util.Map.Entry;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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


public class MazeGenerator {
    main reference;

    int N = 0x1; // 0001
    int S = 0x2; // 0010
    int E = 0x4; // 0100
    int W = 0x8; // 1000
    
    int IN = 0x10; // 10000
    int FRONTIER = 0x20; // 100000
    int VISITED = 0x40; // 1000000
    
    int START = 0x80; // 10000000
    int END = 0x100; // 100000000
    
    int PATH = 0x200; // 1000000000
    int DEAD = 0x400; // 10000000000
    int PATHM = 0x800; // 1000000000
    int DEADM = 0x1000; // 1000000000
    int PATHD = 0x2000; // 1000000000
    int DEADD = 0x4000; // 1000000000
    int aiPresent = 0x8000; // 1000000000
    int DOOR = 0x20000; // 1000000000
    int LIGHT = 0x40000; // 10000000000

    int tClosedDoors = 0;

    int door1x = 14;
    int door1y = 16;
    int door2x = 18;
    int door2y = 16;
    int door3x = 16;
    int door3y = 18;
    int door4x = 16;
    int door4y = 14;

    int last = 1;
    
    int width, height;
    boolean mazeComplete = false;
    boolean startFound = false;
    boolean backtrack = false;
    int startEndDistance = 0;
    int creationSteps = 0;

    int pathstepsD = 0;

    Map<Integer, Integer> opposite = Map.of(E, W, W, E, N, S, S, N);
    List<Map.Entry<Integer, Integer>> frontier = new ArrayList<>();
    List<Integer> lastMove = new ArrayList<>();
    List<Map.Entry<Integer, Integer>> lastPath = new ArrayList<>();

    List<Map.Entry<Integer, Integer>> lastPathD = new ArrayList<>();

    Random rand = new Random();
    Map.Entry<Integer, Integer> start, end, current;

    int[][] grid = new int[height][width];

    ArrayList<Map.Entry<Integer, Integer>> finishedPath = new ArrayList<>(); // Completed path from start to finish



    public ArrayList<Map.Entry<Integer, Integer>> unvisited_neighborsDumb(int x, int y) {
        List<Map.Entry<Integer, Integer>> result = new ArrayList<>();

        if (x > 0 && (grid[y][x - 1] & PATHD) == 0 && (grid[y][x] & W) == 0 && (grid[y][x-1] & E) == 0 && (grid[y][x] & DOOR) == 0 ) {
            result.add(new AbstractMap.SimpleEntry<>(x - 1, y)); // Left
        }
        if (x + 1 < width && (grid[y][x + 1] & PATHD) == 0 && (grid[y][x] & E) == 0 && (grid[y][x+1] & W) == 0 && (grid[y][x] & DOOR) == 0) {
            result.add(new AbstractMap.SimpleEntry<>(x + 1, y)); // Right
        }
        if (y > 0 && (grid[y - 1][x] & PATHD) == 0 && (grid[y][x] & N) == 0 && (grid[y-1][x] & S) == 0 && (grid[y][x] & DOOR) == 0) {
            result.add(new AbstractMap.SimpleEntry<>(x, y - 1)); // Up
        }
        if (y + 1 < height && (grid[y + 1][x] & PATHD) == 0 && (grid[y][x] & S) == 0 && (grid[y+1][x] & N ) == 0 && (grid[y][x] & DOOR) == 0) {
            result.add(new AbstractMap.SimpleEntry<>(x, y + 1)); // Down
        }

        return (ArrayList<Entry<Integer, Integer>>) result;
    }
    public Map.Entry<Integer, Integer> depthFirstSearch(int x, int y, int targetx, int targety) {
        // Start DFS from the starting point
        lastPathD.add(Map.entry(x, y));

        while (!lastPathD.isEmpty()) {
            Map.Entry<Integer, Integer> current = lastPathD.get(lastPathD.size() - 1);
            x = current.getKey();
            y = current.getValue();

            // Get neighbors of the current position
            List<Map.Entry<Integer, Integer>> neighbors = unvisited_neighborsDumb(x, y);
            
            int b = 0; // manhattan distance implemenation
            int dist = 32134; // integer greatest than max distance
            int i = 0;
            for (Map.Entry<Integer, Integer> n : neighbors) {
                int tempdist = Math.abs(n.getKey() - targetx) + Math.abs(n.getValue() - targety);
                if (tempdist < dist)
                {
                    dist = tempdist;
                    b = i;
                }
                else if (tempdist == dist)
                {
                    int xx = rand.nextInt(1);
                    if (xx == 1)
                    {
                        b = i;
                    }
                }

                
                i++;

            }

            if (!neighbors.isEmpty()) {
                // Continue DFS: pick a random neighbor and move there
                Map.Entry<Integer, Integer> next = neighbors.get(b);//rand.nextInt(neighbors.size()));
                int nx = next.getKey();
                int ny = next.getValue();

                // Mark the path
                grid[ny][nx] |= PATHD;

                // Add the new position to the path stack
                lastPathD.add(next);

                // If we reached the finish, store the path and return the finish
                if (next.equals(Map.entry(targetx,targety))) {
                    // Copy the current path into finishedPath
                    finishedPath = new ArrayList<>(lastPathD);
                    return next; // Finish found, return the last position
                }


            } else if (lastPathD.size() > 1) {
                // No unvisited neighbors, backtrack
                lastPathD.remove(lastPathD.size() - 1); // Pop the stack
                grid[y][x] |= DEAD;  // Mark the current cell as dead
            }
        }


        // find a path to nowhere,


        return null; // If no path found
    }

    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    
        // Initialize the grid with the given dimensions
        this.grid = new int[height][width];

        // Create a Random object
        Random random = new Random();

        // Generate random x and y within bounds
        int x = width/2;
        int y = height/2;

        mark(x, y, grid, 0);

        if (!startFound) {
            startFound = true;
            start = Map.entry(rand.nextInt(width - 1), rand.nextInt(height - 1));

            current = Map.entry(x, y);
            lastPathD.add(current);
            lastPath.add(current);
            grid[y][x] |= START;
        }
    
        // Call the generateMaze method with the initialized grid
        generateMaze(this.grid);
    }

    public void generateMaze(int[][] grid) {

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                grid[j][i] |= VISITED;
            }
        }

        for (int i = 0; i < width; i++)
        {
            grid[0][i] |= N;
            grid[height-1][i] |= S;

            grid[i][0] |= W;
            grid[i][width-1] |= E;
        }


        grid[1][1] |= N;
        grid[1][1] |= W;
        grid[2][1] |= W;

        grid[1][2] |= N;
        grid[1][3] |= N;
        grid[1][4] |= N;
        grid[1][1] |= N;
        grid[1][1] |= W;
        grid[2][1] |= W;

        grid[width/2-2][width/2-1] |= N;
        grid[width/2-2][width/2-2] |= W;
        grid[width/2-1][width/2-2] |= W;
        grid[width/2-2][width/2-2] |= N;
        grid[width/2-3][width/2] |= N;
        grid[width/2-2][width/2+1] |= N;
        grid[width/2-2][width/2+2] |= N;
        grid[width/2-1][width/2+2] |= E;
        grid[width/2-2][width/2+2] |= E;

        //grid[width/2-2][width/2+2] |= LIGHT;
        //grid[30][5] |= LIGHT;
        
        grid[width/2+2][width/2-1] |= S;
        grid[width/2+2][width/2-2] |= W;
        grid[width/2+1][width/2-2] |= W;
        grid[width/2+2][width/2-2] |= S;
        grid[width/2+3][width/2] |= S;
        grid[width/2+2][width/2+1] |= S;
        grid[width/2+2][width/2+2] |= S;
        grid[width/2+1][width/2+2] |= E;
        grid[width/2+2][width/2+2] |= E;


        grid[width/2-2][width/2-3] |= S;
        grid[width/2+2][width/2-3] |= N;
        grid[width/2-2][width/2+3] |= S;
        grid[width/2+2][width/2+3] |= N;

        grid[width/2-1][width/2+4] |= E;
        grid[width/2+1][width/2+4] |= E;
        grid[width/2-2][width/2+4] |= E;
        grid[width/2+2][width/2+4] |= E;
        grid[width/2][width/2+4] |= E;

        grid[width/2-4][width/2+4] |= N;
        grid[width/2-4][width/2+3] |= N;
        grid[width/2-4][width/2+2] |= N;
        grid[width/2-4][width/2+1] |= N;
        grid[width/2-4][width/2] |= N;
        grid[width/2-4][width/2-4] |= N;
        grid[width/2-4][width/2-3] |= N;
        grid[width/2-4][width/2-2] |= N;
        grid[width/2-4][width/2-1] |= N;

        grid[width/2-3][width/2] |= E;
        grid[width/2-4][width/2] |= W;

        grid[width/2+3][width/2] |= W;
        grid[width/2+4][width/2] |= E;

        grid[width/2+4][width/2+4] |= S;
        grid[width/2+4][width/2+3] |= S;
        grid[width/2+4][width/2+2] |= S;
        grid[width/2+4][width/2+1] |= S;
        grid[width/2+4][width/2] |= S;
        grid[width/2+4][width/2-4] |= S;
        grid[width/2+4][width/2-3] |= S;
        grid[width/2+4][width/2-2] |= S;
        grid[width/2+4][width/2-1] |= S;

        grid[width/2-1][width/2-4] |= W;
        grid[width/2+1][width/2-4] |= W;
        grid[width/2-2][width/2-4] |= W;
        grid[width/2+2][width/2-4] |= W;
        grid[width/2][width/2-4] |= W;


        grid[width/2+1][width/2-1] |= W;
        grid[width/2+1][width/2-1] |= S;

        grid[width/2-1][width/2] |= N;
        grid[width/2+1][width/2] |= S;


        grid[width/2-1][width/2-1] |= N;
        grid[width/2][width/2-1] |= W;
        grid[width/2][width/2+1] |= E;
        grid[width/2+1][width/2+1] |= S;
        grid[width/2-1][width/2+1] |= E;
        grid[width/2-1][width/2+1] |= N;

        grid[width/2-5][width/2-1] |= N;
        grid[width/2-5][width/2+1] |= N;
        grid[width/2-6][width/2-1] |= E;
        grid[width/2-7][width/2-1] |= E;
        grid[width/2-6][width/2+1] |= W;

        grid[width/2+5][width/2+1] |= S;
        grid[width/2+5][width/2-1] |= S;
        grid[width/2+6][width/2+1] |= W;
        grid[width/2+7][width/2+1] |= W;
        grid[width/2+6][width/2-1] |= E;

        grid[width/2-1][width/2-5] |= W;
        grid[width/2+1][width/2-5] |= W;
        grid[width/2-1][width/2-6] |= S;
        grid[width/2-1][width/2-7] |= S;
        grid[width/2+1][width/2-6] |= N;

        grid[width/2+1][width/2+5] |= E;
        grid[width/2-1][width/2+5] |= E;
        grid[width/2+1][width/2+6] |= N;
        grid[width/2+1][width/2+7] |= N;
        grid[width/2-1][width/2+6] |= S;

        grid[width/2+5][width/2+5] |= E;
        grid[width/2+6][width/2+5] |= E;
        grid[width/2+7][width/2+6] |= N;
        grid[width/2+8][width/2+7] |= N;
        grid[width/2+8][width/2+6] |= S;
        grid[width/2+7][width/2+6] |= E;
        grid[width/2+8][width/2+6] |= E;

        grid[width/2+5][width/2+5] |= E;
        grid[width/2+7][width/2+6] |= N;
        grid[width/2+8][width/2+7] |= N;
        grid[width/2+8][width/2+6] |= S;
        grid[width/2+7][width/2+6] |= E;
        grid[width/2+8][width/2+6] |= E;
        
        grid[width/2+3][width/2+5] |= E;
        grid[width/2+4][width/2+5] |= E;
        grid[width/2+4][width/2+6] |= N;
        grid[width/2+4][width/2+7] |= N;
        // TOP RIGHT
        grid[width/2-5][width/2+5] |= E;
        grid[width/2-7][width/2+6] |= N;
        grid[width/2-8][width/2+7] |= N;
        grid[width/2-8][width/2+6] |= S;
        grid[width/2-7][width/2+6] |= E;
        grid[width/2-8][width/2+6] |= E;
        
        grid[width/2-3][width/2+5] |= E;
        grid[width/2-4][width/2+5] |= E;
        grid[width/2-4][width/2+6] |= N;
        grid[width/2-4][width/2+7] |= N;

        grid[width/2-5][width/2-5] |= W;
        grid[width/2-7][width/2-6] |= S;
        grid[width/2-8][width/2-7] |= S;
        grid[width/2-8][width/2-6] |= N;
        grid[width/2-7][width/2-6] |= W;
        grid[width/2-8][width/2-6] |= W;
        
        grid[width/2-3][width/2-5] |= W;
        grid[width/2-4][width/2-5] |= W;
        grid[width/2-4][width/2-6] |= S;
        grid[width/2-4][width/2-7] |= S;

        grid[width/2+5][width/2-5] |= W;
        grid[width/2+7][width/2-6] |= S;
        grid[width/2+8][width/2-7] |= S;
        grid[width/2+8][width/2-6] |= N;
        grid[width/2+7][width/2-6] |= W;
        grid[width/2+8][width/2-6] |= W;
        
        grid[width/2+3][width/2-5] |= W;
        grid[width/2+4][width/2-5] |= W;
        grid[width/2+4][width/2-6] |= S;
        grid[width/2+4][width/2-7] |= S;


        grid[width/2][width/2-1] |= W;
        grid[width/2][width/2+1] |= E;
        grid[width/2+1][width/2+1] |= S;
        grid[width/2-1][width/2+1] |= E;
        grid[width/2-1][width/2+1] |= N;

        grid[width/2+1][width/2-1] |= W;
        grid[width/2+1][width/2-1] |= S;

        grid[width/2-1][width/2] |= N;
        grid[width/2+1][width/2] |= S;

        /*
        while (!mazeComplete) {
            creationSteps++;

            Map.Entry<Integer, Integer> indexF = frontier.get(frontier.size() - 1);
            int x = indexF.getKey();
            int y = indexF.getValue();

            if (x == width || x == 0 || y == height || y == 0) {
                if (x == width) grid[y][x] |= E;
                else if (x == 0) grid[y][x] |= W;
                else if (y == height) grid[y][x] |= S;
                else if (y == 0) grid[y][x] |= N;

                if (!startFound) {
                    startFound = true;
                    start = Map.entry(rand.nextInt(width - 1), rand.nextInt(height - 1));
                    current = Map.entry(x, y);
                    lastPath.add(current);
                    grid[y][x] |= START;
                }

                int distance = Math.abs(x - start.getKey() / 2) + Math.abs(y - start.getValue() / 2);

                if (!backtrack && startFound && distance > startEndDistance) {
                    startEndDistance = distance;
                    end = Map.entry(rand.nextInt(width), rand.nextInt(height));
                }
            }

            List<Map.Entry<Integer, Integer>> neighbors = neighbors(x, y, grid);
            if (neighbors.isEmpty()) {
                backtrack = true;
                grid[y][x] &= ~FRONTIER;
                grid[y][x] |= VISITED;
                frontier.remove(frontier.size() - 1);
                lastMove.remove(lastMove.size() - 1);
            } else {
                int index = rand.nextInt(neighbors.size());
                int nx = neighbors.get(index).getKey();
                int ny = neighbors.get(index).getValue();

                int dir = wallDir(x, y, nx, ny);
                if (backtrack) {
                    //grid[y][x] &= ~direction(x, y, nx, ny);
                    backtrack = false;
                } else {

                    //grid[y][x] |= dir;
                    //grid[y][x] |= opposite.get(dir);
                    //grid[y][x] |= opposite.get(direction(x, y, nx, ny));

                    //grid[y][x] &= ~opposite.get(last);

                    //grid[y][x] &= ~direction(x, y, nx, ny);

                    //grid[ny][nx] &= ~opposite.get(dir);

                }

                last = direction(x, y, nx, ny);
                
                mark(nx, ny, grid, last);
            }


            if (frontier.isEmpty()) {
                grid[width/2][height/2] |= END;
                mazeComplete = true;
            }
        } */
    }


    public void closeDoor(int door, int c, Player p, int closedDoorsTotal)
    {
        tClosedDoors = closedDoorsTotal;

        frontier = new ArrayList<>();
        lastMove = new ArrayList<>();
        lastPath = new ArrayList<>();
        lastPathD = new ArrayList<>();

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                grid[i][j] &= ~PATHD;
            }
        }

        if (door == 1)
        {
            grid[door1y][door1x] ^= (DOOR);
        }
        if (door == 2)
        {
            grid[door2y][door2x] ^= (DOOR);
        }
        if (door == 3)
        {
            grid[door3y][door3x] ^= (DOOR);
        }
        if (door == 4)
        {
            grid[door4y][door4x] ^= (DOOR);
        }
        reference.pathneeded = true;



    }

    public ArrayList<Map.Entry<Integer, Integer>> getPath(int x, int y, int targetx, int targety)
    {
        frontier = new ArrayList<>();
        lastMove = new ArrayList<>();
        lastPath = new ArrayList<>();
        lastPathD = new ArrayList<>();

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < height; j++)
            {
                grid[i][j] &= ~PATHD;
            }
        }

        if (tClosedDoors == 4)
        {
            targetx = rand.nextInt(width);
            targety = rand.nextInt(height);

            while (targetx > width/2-4 && targetx < width/2 + 4)
            {
                targetx = rand.nextInt(width);
            }

            while (targety > height/2-4 && targety < height/2 + 4)
            {
                targety = rand.nextInt(height);
            }

            if (x > height/2-4 && x < height/2 + 4 && y > height/2-4 && y < height/2 + 4)
            {
                targetx = x;
                targety = y;
            }

        }
        boolean searching = true;
        while (searching == true) {
            current = depthFirstSearch(x, y, targetx, targety);



        
                    // Check if we reached the end
            if (current != null && checkEnd(current, targetx, targety)) {
                searching = false;  // Stop the search
            } else if (current == null)
            {
                // meandering around....
                //return getPath(x, y, rand.nextInt(width), rand.nextInt(height)); thought this was faulty but it was just some other part of my code, anyway im using a different, less efficient method now but whatever its got better logic checks!


            }
        }

        return finishedPath;

    }

    public void blowOut() throws InterruptedException
    {
        grid[width/2][width/2] |= LIGHT;
        grid[width/2][width/2+1] |= LIGHT;
        grid[width/2][width/2+2] |= LIGHT;
        grid[width/2][width/2-2] |= LIGHT;
        grid[width/2][width/2-1] |= LIGHT;
        grid[width/2+1][width/2] |= LIGHT;
        grid[width/2+1][width/2+1] |= LIGHT;
        grid[width/2+1][width/2-1] |= LIGHT;
        grid[width/2+1][width/2+2] |= LIGHT;
        grid[width/2+1][width/2-2] |= LIGHT;
        grid[width/2-1][width/2+1] |= LIGHT;
        grid[width/2-1][width/2-1] |= LIGHT;
        grid[width/2-1][width/2+2] |= LIGHT;
        grid[width/2-1][width/2-2] |= LIGHT;
        grid[width/2-1][width/2-3] |= LIGHT;
        grid[width/2-1][width/2+3] |= LIGHT;
        grid[width/2+1][width/2-3] |= LIGHT;
        grid[width/2+1][width/2+3] |= LIGHT;
        grid[width/2][width/2-3] |= LIGHT;
        grid[width/2][width/2+3] |= LIGHT;
        grid[width/2-1][width/2] |= LIGHT;


        grid[width/2+2][width/2] |= LIGHT;
        grid[width/2+2][width/2+1] |= LIGHT;
        grid[width/2+2][width/2+2] |= LIGHT;
        grid[width/2+2][width/2-2] |= LIGHT;
        grid[width/2+2][width/2-1] |= LIGHT;
        grid[width/2+2][width/2+3] |= LIGHT;
        grid[width/2+2][width/2-3] |= LIGHT;

        grid[width/2+3][width/2] |= LIGHT;
        grid[width/2+3][width/2+1] |= LIGHT;
        grid[width/2+3][width/2+2] |= LIGHT;
        grid[width/2+3][width/2-2] |= LIGHT;
        grid[width/2+3][width/2-1] |= LIGHT;
        grid[width/2+3][width/2+3] |= LIGHT;
        grid[width/2+3][width/2-3] |= LIGHT;

        grid[width/2-3][width/2] |= LIGHT;
        grid[width/2-3][width/2+1] |= LIGHT;
        grid[width/2-3][width/2+2] |= LIGHT;
        grid[width/2-3][width/2-2] |= LIGHT;
        grid[width/2-3][width/2-1] |= LIGHT;
        grid[width/2-3][width/2+3] |= LIGHT;
        grid[width/2-3][width/2-3] |= LIGHT;




        grid[width/2-2][width/2] |= LIGHT;
        grid[width/2-2][width/2+1] |= LIGHT;
        grid[width/2-2][width/2+2] |= LIGHT;
        grid[width/2-2][width/2-2] |= LIGHT;
        grid[width/2-2][width/2-1] |= LIGHT;
        grid[width/2-2][width/2+3] |= LIGHT;
        grid[width/2-2][width/2-3] |= LIGHT;

        grid[width/2+1][width/2-1] |= LIGHT;
        grid[width/2+1][width/2+2] |= LIGHT;
        grid[width/2+1][width/2-2] |= LIGHT;
        grid[width/2-1][width/2+1] |= LIGHT;
        grid[width/2-1][width/2-1] |= LIGHT;
        grid[width/2-1][width/2+2] |= LIGHT;
        grid[width/2-1][width/2-2] |= LIGHT;
        grid[width/2-1][width/2] |= LIGHT;

    }

    public boolean checkEnd(Map.Entry<Integer,Integer> curr, int targetx, int targety)
    {
        return (curr.getKey() == targetx && curr.getValue() == targety);
    }

    private void mark(int x, int y, int[][] grid, int last) {
        addFrontier(x, y, grid);
        lastMove.add(last);
    }

    private void addFrontier(int x, int y, int[][] grid) {
        if (x >= 0 && y >= 0 && y < height && x < width && grid[y][x] == 0) {
            grid[y][x] |= FRONTIER;
            frontier.add(Map.entry(x, y));
        }
    }

    private List<Map.Entry<Integer, Integer>> neighbors(int x, int y, int[][] grid) {
        List<Map.Entry<Integer, Integer>> result = new ArrayList<>();
        if (x > 0 && grid[y][x - 1] == 0) result.add(Map.entry(x - 1, y));
        if (x + 1 < width && grid[y][x + 1] == 0) result.add(Map.entry(x + 1, y));
        if (y > 0 && grid[y - 1][x] == 0) result.add(Map.entry(x, y - 1));
        if (y + 1 < height && grid[y + 1][x] == 0) result.add(Map.entry(x, y + 1));
        return result;
    }

    private int direction(int fx, int fy, int tx, int ty) {
            if (fx < tx) return E;
            else if (fx > tx) return W;
            else if (fy < ty) return S;
            else return N;
    }

    private int wallDir(int fx, int fy, int tx, int ty) {
        if (fx < tx) return S;
        else if (fx > tx) return N;
        else if (fy < ty) return E;
        else return W;
    }



}
