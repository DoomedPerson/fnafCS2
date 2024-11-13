import java.util.*;

public class MazeGenerator {
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

    int last = 1;
    
    int width, height;
    boolean mazeComplete = false;
    boolean startFound = false;
    boolean backtrack = false;
    int startEndDistance = 0;
    int creationSteps = 0;

    Map<Integer, Integer> opposite = Map.of(E, W, W, E, N, S, S, N);
    List<Map.Entry<Integer, Integer>> frontier = new ArrayList<>();
    List<Integer> lastMove = new ArrayList<>();
    List<Map.Entry<Integer, Integer>> lastPath = new ArrayList<>();

    Random rand = new Random();
    Map.Entry<Integer, Integer> start, end, current;

    int[][] grid = new int[height][width];

    public MazeGenerator(int width, int height) {
        this.width = width;
        this.height = height;
    
        // Initialize the grid with the given dimensions
        this.grid = new int[height][width];

        // Create a Random object
        Random random = new Random();

        // Generate random x and y within bounds
        int x = random.nextInt(width);
        int y = random.nextInt(height);

        mark(x, y, grid, 0);
    
        // Call the generateMaze method with the initialized grid
        generateMaze(this.grid);
    }

    public void generateMaze(int[][] grid) {
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
                    grid[y][x] &= ~direction(x, y, nx, ny);
                    backtrack = false;
                } else {
                    grid[y][x] |= dir;
                    grid[y][x] |= opposite.get(dir);

                    grid[y][x] |= dir;
                    grid[y][x] |= opposite.get(dir);
                    grid[y][x] |= opposite.get(direction(x, y, nx, ny));

                    grid[y][x] &= ~opposite.get(last);

                    grid[y][x] &= ~direction(x, y, nx, ny);
                }

                last = direction(x, y, nx, ny);
                
                mark(nx, ny, grid, last);
            }


            if (frontier.isEmpty()) {
                grid[end.getValue()][end.getKey()] |= END;
                mazeComplete = true;
            }
        }
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
