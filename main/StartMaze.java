package main;

import java.awt.*;
import javax.swing.*;

/**
 * Combined class for maze game handling both menu and game initialization
 *
 * @author User
 */
public class StartMaze {

    // Global static variables for screen dimensions
    public static int originalTileSize = 16;
    public static int scale = 3;

    public static int tileSize = originalTileSize * scale;
    public static int maxScreenCol = 16;
    public static int maxScreenRow = 14;
    public static int screenWidth = tileSize * maxScreenCol;
    public static int screenHeight = tileSize * maxScreenRow;
    
    public static void main(String[] args) {


        JFrame gameFrame = new JFrame();
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setPreferredSize(new Dimension(screenWidth, screenHeight));
        gameFrame.setResizable(false);
        gameFrame.setTitle("Fazo The Maze Runner");

        MazeGame gamePanel = new MazeGame(21, 21, 1, tileSize);
        gamePanel.setupGame();
        gameFrame.add(gamePanel);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }

    // Getter methods for screen dimensions
    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public int getTileSize() {
        return tileSize;
    }

}
