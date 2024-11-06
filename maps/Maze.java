package maps;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Maze {

    private final int[][] maze;
    public int rows;
    public int cols;
    public int startX, startY;
    private int exitX, exitY;
    private int itemX, itemY;
    private int monsterX, monsterY;
    private boolean itemCollected;
    private final Random random = new Random();
    private static final double MONSTER_MIN_DISTANCE_FACTOR = 0.25;

    private final int[] dRow = {-1, 0, 1, 0};
    private final int[] dCol = {0, 1, 0, -1};
    private static final int WALL = 1;
    private static final int PATH = 0;
    private static final double MIN_DISTANCE_FACTOR = 0.33;

    public Maze(int rows, int cols) {
        if (rows < 5 || cols < 5) {
            throw new IllegalArgumentException("Maze dimensions must be at least 5x5");
        }

        this.rows = rows;
        this.cols = cols;
        this.maze = new int[rows][cols];
        initializeMaze();
        generateInitialMaze();
    }

    private void initializeMaze() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maze[i][j] = 1;
            }
        }
    }

    private void generateInitialMaze() {
        generateMaze(1, 1);
        setRandomStartAndExit();
        setRandomItem();
        setRandomMonster();
        itemCollected = false;
    }

    private void generateMaze(int row, int col) {
        maze[row][col] = 0;

        Integer[] directions = {0, 1, 2, 3};
        Collections.shuffle(Arrays.asList(directions));

        for (int i = 0; i < 4; i++) {
            int direction = directions[i];
            int newRow = row + dRow[direction] * 2;
            int newCol = col + dCol[direction] * 2;

            if (isValid(newRow, newCol) && maze[newRow][newCol] == 1) {
                maze[row + dRow[direction]][col + dCol[direction]] = 0;
                generateMaze(newRow, newCol);
            }
        }
    }

    public void reset() {
        int oldStartX = startX;
        int oldStartY = startY;
        int oldExitX = exitX;
        int oldExitY = exitY;
        int oldMonsterX = monsterX;
        int oldMonsterY = monsterY;

        initializeMaze();
        generateInitialMaze();

        // Set new positions
        while (startX == oldStartX && startY == oldStartY) {
            setRandomStart();
        }

        while (exitX == oldExitX && exitY == oldExitY) {
            setRandomExit();
        }
        // Reset monster position
        setRandomMonster();
        while (monsterX == oldMonsterX && monsterY == oldMonsterY) {
            setRandomMonster();
        }
        if (!validateMaze()) {
            generateInitialMaze();
        }
        // Log reset information
        logResetInfo(oldStartX, oldStartY, oldExitX, oldExitY, oldMonsterX, oldMonsterY);
    }

    private boolean isValid(int row, int col) {
        return row > 0 && row < rows - 1 && col > 0 && col < cols - 1;
    }

    public void setRandomStartAndExit() {
        // Randomize the starting position
        do {
            startX = random.nextInt(cols - 2) + 1;
            startY = random.nextInt(rows - 2) + 1;
        } while (maze[startY][startX] != 0);

        System.out.println("Start Position: (" + startX + ", " + startY + ")");

        // Randomize the exit position with a minimum distance from the start
        do {
            exitX = random.nextInt(cols - 2) + 1;
            exitY = random.nextInt(rows - 2) + 1;
        } while (maze[exitY][exitX] != 0 || (Math.abs(exitX - startX) + Math.abs(exitY - startY) < Math.min(rows, cols) / 3));

        System.out.println("Exit Position: (" + exitX + ", " + exitY + ")");
    }

    public void setRandomItem() {
        do {
            itemX = random.nextInt(cols - 2) + 1;
            itemY = random.nextInt(rows - 2) + 1;
        } while (maze[itemY][itemX] != 0 || (itemX == startX && itemY == startY) || (itemX == exitX && itemY == exitY));

        System.out.println("Item Position: (" + itemX + ", " + itemY + ")");
    }

    private void setRandomMonster() {
        int attempts = 0;
        int maxAttempts = 100;

        do {
            monsterX = random.nextInt(cols - 2) + 1;
            monsterY = random.nextInt(rows - 2) + 1;
            attempts++;

            if (attempts >= maxAttempts) {
                System.out.println("Warning: Relaxing monster spawn conditions after " + maxAttempts + " attempts");
                break;
            }
        } while (maze[monsterY][monsterX] != 0
                || (monsterX == startX && monsterY == startY)
                || (monsterX == itemX && monsterY == itemY)
                || (monsterX == exitX && monsterY == exitY)
                || !isMinimumDistanceFromStart(monsterX, monsterY) // ต้องห่างจากจุดเริ่มพอสมควร
                );

        System.out.println("Monster Position: (" + monsterX + ", " + monsterY + ")");
    }

    private boolean isMinimumDistanceFromStart(int x, int y) {
        int minSize = Math.min(rows, cols);
        int minDistance = (int) (minSize * MONSTER_MIN_DISTANCE_FACTOR);
        return Math.abs(x - startX) + Math.abs(y - startY) >= minDistance;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getExitX() {
        return exitX;
    }

    public int getExitY() {
        return exitY;
    }

    public int[][] getMaze() {
        return maze;
    }

    public int getItemX() {
        return itemX;
    }

    public int getItemY() {
        return itemY;
    }

    public int getCol() {
        return cols;
    }

    public int getRow() {
        return rows;
    }

    public int getMonsterX() {
        return monsterX;
    }

    public int getMonsterY() {
        return monsterY;
    }

    private boolean validateMaze() {

        return hasPath(startX, startY, exitX, exitY);
    }

    //  BFS 
    private boolean hasPath(int startX, int startY, int targetX, int targetY) {
        boolean[][] visited = new boolean[rows][cols];
        List<Point> queue = new ArrayList<>();
        queue.add(new Point(startX, startY));
        visited[startY][startX] = true;

        while (!queue.isEmpty()) {
            Point current = queue.remove(0);
            if (current.x == targetX && current.y == targetY) {
                return true;
            }

            for (int i = 0; i < 4; i++) {
                int newX = current.x + dCol[i];
                int newY = current.y + dRow[i];

                if (isValidPosition(newX, newY) && !visited[newY][newX] && maze[newY][newX] == PATH) {
                    queue.add(new Point(newX, newY));
                    visited[newY][newX] = true;
                }
            }
        }
        return false;
    }

    private void setRandomStart() {
        do {
            startX = random.nextInt(cols - 2) + 1;
            startY = random.nextInt(rows - 2) + 1;
        } while (maze[startY][startX] != PATH);
    }

    private void setRandomExit() {
        do {
            exitX = random.nextInt(cols - 2) + 1;
            exitY = random.nextInt(rows - 2) + 1;
        } while (maze[exitY][exitX] != PATH
                || !isMinimumDistance(startX, startY, exitX, exitY));
    }

    private boolean isMinimumDistance(int x1, int y1, int x2, int y2) {
        int minSize = Math.min(rows, cols);
        int minDistance = (int) (minSize * MIN_DISTANCE_FACTOR);
        return Math.abs(x1 - x2) + Math.abs(y1 - y2) >= minDistance;
    }

    public void resetItems() {
        setRandomItem();
        itemCollected = false;
    }

    private void logResetInfo(int oldStartX, int oldStartY, int oldExitX, int oldExitY,
            int oldMonsterX, int oldMonsterY) {
        System.out.println("Maze Reset Complete:");
        System.out.println("Old Start: (" + oldStartX + ", " + oldStartY + ") -> New Start: ("
                + startX + ", " + startY + ")");
        System.out.println("Old Exit: (" + oldExitX + ", " + oldExitY + ") -> New Exit: ("
                + exitX + ", " + exitY + ")");
        System.out.println("Old Monster: (" + oldMonsterX + ", " + oldMonsterY + ") -> New Monster: ("
                + monsterX + ", " + monsterY + ")");
        System.out.println("New Item Position: (" + itemX + ", " + itemY + ")");
    }

    // Helper class for BFS
    private static class Point {

        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private boolean isValidPosition(int x, int y) {
        return x >= 0 && x < cols && y >= 0 && y < rows;
    }

    public boolean isItemCollected() {
        return itemCollected;
    }

    public void collectItem() {
        itemCollected = true;
    }

}
