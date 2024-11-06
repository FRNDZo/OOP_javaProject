package main;

import entity.Entity;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import maps.Maze;
import entity.Player;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import monster.MON_Goi;
import object.OBJ_ExitDoor;

import tile.TileManager;

public class MazeGame extends JPanel implements ActionListener, Runnable {

    //MAKE 60 FPS
    int FPS = 60;

    Maze maze;
    StartMaze sm;
    Timer gameTimer;
    LocalTime startTime;
    public TileManager tileM;
    public KeyHandler keyH = new KeyHandler(this);
    public Player player;
    public UI ui;  // เปลี่ยนเป็นแค่ประกาศตัวแปร
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Entity obj[] = new Entity[10];
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<>();

    private int tileSize;
    public final int maxWorldCol = 62;
    public final int maxWorldRow = 62;
    public int worldWidth;
    public int worldHeight;
    public int numItems;

    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int characterState = 3;
    public final int optionState = 4;
    public final int gameOverStage = 5;
    public final int gameCompleteStage = 6;

    public int mazeRows;
    public int mazeCols;
    public int subNumItems = 0;
    public int currentMap = 0;

    public MazeGame(int rows, int cols, int numItems, int tileSize) {

        this.numItems = numItems;
        this.tileSize = tileSize;

        this.worldWidth = tileSize * maxWorldCol;
        this.worldHeight = tileSize * maxWorldRow;

        maze = new Maze(rows, cols);

        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        gameState = titleState;
        ui = new UI(this);
        aSetter = new AssetSetter(this);
        tileM = new TileManager(this);

    }

    public void setupGame() {

        player = new Player(maze.getStartX(), maze.getStartY(), this, keyH);
        player.setMaze(maze);
        ui.setPlayer(player);
        tileM.setMaze(maze);
        tileM.setPlayer(player);
        keyH.setPlayer(player);
        keyH.setTileManager(tileM);
        aSetter.setPlayer(player);
        aSetter.setMaze(maze);
        aSetter.setObject(numItems);
        aSetter.setMonster();

        startGameTread();
    }

    public void setDifficulty(String difficulty) {
        switch (difficulty) {
            case "easy":
                mazeRows = 21;
                mazeCols = 21;
                numItems = 1;
                break;
            case "normal":
                mazeRows = 41;
                mazeCols = 41;
                numItems = 3;
                break;
            case "hard":
                mazeRows = 61;
                mazeCols = 61;
                numItems = 5;
                break;
        }

        // Create new maze with updated settings
        maze = new Maze(mazeRows, mazeCols);

        // Reset player position
        player.setX(maze.getStartX());
        player.setY(maze.getStartY());
        player.setMaze(maze);

        // Reset objects and tiles
        tileM.setMaze(maze);
        aSetter.setMaze(maze);
        aSetter.setObject(numItems);
        subNumItems = numItems;
        aSetter.setMonster();

        // Reset UI and game state
        ui.resetTimer();
        gameState = playState;

        // Spawn new monster for the new maze
    }

    public void retry() {

        player.setDefaultValues();
        maze.setRandomStartAndExit();
        player.setX(maze.getStartX());
        player.setY(maze.getStartY());
        player.setMaze(maze);

        // Reset objects and monsters
        tileM.setMaze(maze);
        aSetter.setMaze(maze);
        aSetter.setObject(numItems);
        subNumItems = numItems;
        aSetter.setMonster();;

        ui.resetTimer();
    }

    public void NextFloor() {

        if (player.currentLevel == mazeRows - 1) {
            player.score += 1000;
            gameState = gameCompleteStage;

        } else {
            // Reset maze and entities
            maze = new Maze(mazeRows, mazeCols);
            player.setX(maze.getStartX());
            player.setY(maze.getStartY());
            player.setMaze(maze);

            // Reset objects and tiles
            tileM.setMaze(maze);
            aSetter.setMaze(maze);
            aSetter.setObject(numItems);
            subNumItems = numItems;
            System.out.println("NUmm " + subNumItems);
            aSetter.setMonster();
            player.score += 1000;
            player.currentLevel++;
        }

//        // Debug log
//        System.out.println("Objects after NextFloor:");
//        for (int i = 0; i < obj.length; i++) {
//            if (obj[i] != null) {
//                System.out.println("obj[" + i + "]: " + obj[i].getClass().getSimpleName()
//                        + " at (" + obj[i].worldX / getTileSize() + "," + obj[i].worldY / getTileSize() + ")");
//            }
//        }
    }

    //Thread========================================================================================v
    public void startGameTread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

        }

    }

    public void update() {

        if (gameState == playState) {
            //PLAYER
            player.update();
            ui.startTimer();

            //MONSTER
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    monster[i].update();
                }
            }
        }
        if (gameState == pauseState || gameState == optionState) {
            ui.stopTimer();
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getWorldWidth() {
        return worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public Maze getMaze() {
        return maze;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
//        this.setBackground(Color.BLACK);

        //Debug
        long drawStart = 0;
        if (keyH.showDebugText == true) {
            drawStart = System.nanoTime();
        }

        //Title Screen
        if (gameState == titleState) {
            ui.draw(g2);

        } //Other
        else {

            //Maze
            tileM.draw(g2);

            //ADD ENTITIES TO THE LIST
            // PLAYER
            entityList.add(player);

            //EXIT     
            if (obj[0] != null) {
//                ((OBJ_ExitDoor) obj[0]).drawDebugCollisionBox(g2);
                entityList.add(obj[0]);
            }

            // vision+ ITEM
            int itemCount = 0;
            for (int i = 1; i <= numItems; i++) {
                if (obj[i] != null) {
                    itemCount++;
                    entityList.add(obj[i]);
                }
            }

            //MONSTER
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    entityList.add(monster[i]);
//                    ((MON_Goi) monster[i]).drawDebugCollisionBox(g2);
                }
            }
            //SORT
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {

                    if (e1 instanceof Player) {
                        return 1;
                    }

                    if (e2 instanceof Player) {
                        return -1;
                    }

                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            //DRAW ENTITIES
            for (int i = 0; i < entityList.size(); i++) {
                entityList.get(i).draw(g2);
            }
            //EMPTY ENTITY LIST
            for (int i = 0; i < entityList.size(); i++) {
                entityList.clear();
            }

//                    player.drawDebugCollisionBox(g2);
            //UI
            ui.draw(g2);

            //GOD MODE
            if (keyH.godMode == true) {
                tileM.tile[1].collision = false;
                player.invincible = true;
                player.speed = 7;
                g2.setColor(Color.GREEN);
                g2.drawString("GOD MODE ON!", (getTileSize() / 2), (getTileSize() / 2));

            }
            if (keyH.godMode == false) {
                player.speed = 3;
                tileM.tile[1].collision = true;
                player.invincible = false;

            }
            //SKIP FLOOR
            //GOD MODE
            if (keyH.skipF == true) {
                NextFloor();
                keyH.skipF = false;
//                g2.setColor(Color.GREEN);
//                g2.drawString("GOD MODE ON!", (getTileSize() / 2), (getTileSize() / 2));
            }

        }

        //debug
        if (keyH.showDebugText == true) {
            long drawEnd = System.nanoTime();
            long passed = drawEnd - drawStart;

            g2.setFont(new Font("Arial", Font.PLAIN, 20));
            g2.setColor(Color.white);
            int x = 10;
            int y = 400;
            int lineHeight = 20;

            g2.drawString("WorldX: " + player.worldX, x, y);
            y += lineHeight;
            g2.drawString("WorldY: " + player.worldY, x, y);
            y += lineHeight;
            g2.drawString("Cols: " + (player.worldX + player.solidArea.x) / tileSize, x, y);
            y += lineHeight;
            g2.drawString("Rows: " + (player.worldY + player.solidArea.y) / tileSize, x, y);
            y += lineHeight;
            g2.drawString("DRAW TIME: " + passed, x, y);

        }

        g.dispose();
    }

//    
    @Override
    public void actionPerformed(ActionEvent e) {

        repaint();
    }

}
