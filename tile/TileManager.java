package tile;

import entity.Player;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.MazeGame;
import main.StartMaze;
import main.UtilityTool;
import maps.Maze;

/**
 *
 * @author User
 */
public class TileManager {

    MazeGame mg;
    Maze maze;
    Player player;
    public Tile[] tile;
    public int mapTileNum[][];

    public TileManager(MazeGame mg) {

        this.mg = mg;
        tile = new Tile[10];
        mapTileNum = new int[mg.maxWorldCol][mg.maxWorldRow];
        if (mg.getTileSize() <= 0) {
            throw new IllegalArgumentException("TileSize must be greater than 0");
        }

        getTileImage();

    }

    // Add setter method for Player and Maze
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
        loadMap();
    }

    public void getTileImage() {

        setup(0, "WALL", false);
        setup(1, "path", true);
    }

    public void setup(int index, String imageName, boolean collision) {

        UtilityTool uTool = new UtilityTool();

        try {
            tile[index] = new Tile();
            tile[index].image = ImageIO.read(getClass().getResourceAsStream("/img/tile/" + imageName + ".png"));
            tile[index].image = uTool.scaleImage(tile[index].image, mg.getTileSize(), mg.getTileSize());

            tile[index].collision = collision;

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void draw(Graphics2D g2) {
        if (player == null || maze == null) {
            return;
        }

        drawBackground(g2);
        int worldCol = 0;
        int worldRow = 0;

        int playerCenterX = player.getX() + player.solidArea.x + (player.solidArea.width / 2);
        int playerCenterY = player.getY() + player.solidArea.y + (player.solidArea.height / 2);

        int playerTileCenterX = playerCenterX / mg.getTileSize();
        int playerTileCenterY = playerCenterY / mg.getTileSize();

        int screenX = player.screenX;
        int screenY = player.screenY;

        while (worldRow < maze.getMaze().length) {
            while (worldCol < maze.getMaze()[0].length) {
                int worldX = worldCol * mg.getTileSize();
                int worldY = worldRow * mg.getTileSize();

                int screenDrawX = worldX - player.getX() + screenX;
                int screenDrawY = worldY - player.getY() + screenY;

                

                int distanceX = Math.abs(worldCol - playerTileCenterX);
                int distanceY = Math.abs(worldRow - playerTileCenterY);

                if (distanceX <= player.getVisionRadius() && distanceY <= player.getVisionRadius()) {

                    if (maze.getMaze()[worldRow][worldCol] == 1) {
                        g2.drawImage(tile[0].image, screenDrawX, screenDrawY, mg);
                    } else {
                        g2.drawImage(tile[1].image, screenDrawX, screenDrawY, mg);
                    }
                } else {

                    g2.setColor(Color.DARK_GRAY);
                    g2.fillRect(screenDrawX, screenDrawY, mg.getTileSize(), mg.getTileSize());
                }

                worldCol++;
            }
            worldCol = 0;
            worldRow++;
        }
    }

    private void loadMap() {
        if (maze != null) {
            int[][] mazeData = maze.getMaze();
            for (int row = 0; row < mg.maxWorldRow; row++) {
                for (int col = 0; col < mg.maxWorldCol; col++) {
                    // แปลงข้อมูลจาก maze (1 คือกำแพง, 0 คือทางเดิน) เป็น tile index
                    if (row < mazeData.length && col < mazeData[0].length) {
                        mapTileNum[col][row] = mazeData[row][col];
                    } else {
                        mapTileNum[col][row] = 1; // default to wall if outside maze bounds
                    }
                }
            }
        }
    }

    private void drawBackground(Graphics2D g2) {

        g2.setColor(Color.DARK_GRAY);
        g2.fillRect(0, 0, StartMaze.screenWidth, StartMaze.screenHeight);
    }

}
