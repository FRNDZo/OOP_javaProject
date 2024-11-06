package entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import main.KeyHandler;
import main.MazeGame;
import main.StartMaze;
import maps.Maze;

public class Player extends Entity {

    KeyHandler keyH;
    StartMaze sm;
    Maze maze;

    public int visionRadius = 1;
    public final int screenX;
    public final int screenY;
    private final Random random = new Random();
    int[] arrRan = {1, 2, 3};
    int standCounter = 1;
    public int score = 0;
    public int currentLevel = 1;

//    URL imgPlayerURL = getClass().getResource("/img/Fazo.png");
//    Image imgPlayer = new ImageIcon(imgPlayerURL).getImage();
    public Player(int startX, int startY, MazeGame mg, KeyHandler keyH) {
        super(mg);

        this.worldX = startX * mg.getTileSize();
        this.worldY = startY * mg.getTileSize();

        this.keyH = keyH;

        screenX = StartMaze.screenWidth / 2 - (mg.getTileSize() / 2);
        screenY = StartMaze.screenHeight / 2 - (mg.getTileSize() / 2);

        solidArea = new Rectangle();
        int padding = mg.getTileSize() / 4;
        solidArea.x = padding;
        solidArea.y = padding * 2;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        solidArea.width = mg.getTileSize() - (padding * 2);
        solidArea.height = mg.getTileSize() - (padding * 2);

        setDefaultValues();
        getPlayerImage();

    }

    public void setDefaultValues() {

        speed = 3;

        visionRadius = 1;
        score = 0;
        currentLevel = 1;
        direction = "down";
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public void getPlayerImage() {

        up1 = setup("/img/player/Fazo_back1");
        up2 = setup("/img/player/Fazo_back2");
        down1 = setup("/img/player/Fazo_front1");
        down2 = setup("/img/player/Fazo_front2");
        left1 = setup("/img/player/Fazo_left1");
        left2 = setup("/img/player/Fazo_left2");
        left3 = setup("/img/player/Fazo_left3");
        right1 = setup("/img/player/Fazo_right1");
        right2 = setup("/img/player/Fazo_right2");
        right3 = setup("/img/player/Fazo_right3");

    }

    public void update() {

        if (keyH.upPressed == true || keyH.downPressed == true
                || keyH.leftPressed == true || keyH.rightPressed == true) {

            if (keyH.upPressed == true) {
                direction = "up";

            } else if (keyH.downPressed == true) {
                direction = "down";

            } else if (keyH.leftPressed == true) {
                direction = "left";

            } else if (keyH.rightPressed == true) {
                direction = "right";

            }

            //check collision
            collisionOn = false;
            mg.cChecker.checkTile(this);

            //check obj collision
            int objIndex = mg.cChecker.checkObject(this, true);
            pickUpObject(objIndex);

            //CHECK MONSTER COLLISION
            int monsterIndex = mg.cChecker.checkEntity(this, mg.monster);
            contactMonter(monsterIndex);

            //IF FALSE , PLAYER CAN MOVE
            if (collisionOn == false) {
                switch (direction) {
                    case "up":
                        worldY -= speed;
                        break;

                    case "down":
                        worldY += speed;
                        break;

                    case "left":
                        worldX -= speed;
                        break;

                    case "right":
                        worldX += speed;
                        break;

                }
            }

            //animation
            spriteCounter++;

            if (spriteCounter >= 12) {
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 3;
                } else if (spriteNum == 3) {
                    spriteNum = 1;
                }

                spriteCounter = 0;
            }
        } else {
            standCounter++;

            if (standCounter == 20) {

                spriteNum = 1;
                standCounter = 0;
            }

        }

    }

    public void pickUpObject(int i) {

        if (i != 999) {

            String objectName = mg.obj[i].name;

            switch (objectName) {
                case "RandomChest":

                    int effect = arrRan[random.nextInt(arrRan.length)];

                    // Apply effect based on the random choice
                    switch (effect) {
                        case 1:
                            increaseVision();
                            mg.ui.showMessage("Vision increased!");
                            mg.subNumItems--;
                            score += 500;
                            mg.obj[i] = null;
                            break;

                        case 2:
                            decreaseVision();
                            mg.ui.showMessage("Vision decreased!");

                            mg.subNumItems--;
                            score += 250;
                            mg.obj[i] = null;
                            break;

                        case 3:
                            teleportToRandomPosition();
                            mg.ui.showMessage("Teleported to a new position!");

                            mg.subNumItems--;
                            score += 700;
                            mg.obj[i] = null;
                            break;

                    }

                    break;

                case "ExitDoor":

                    mg.NextFloor();

                    break;

            }
        }
    }

//    public void interactNPC(int i){
//        if(i != 999){
//            //some text
//        }
//    }
    public void contactMonter(int i) {
        if (i != 999) {

            if (invincible == false) {
                mg.gameState = mg.gameOverStage;

            }
        }
    }

    public void teleportToRandomPosition() {
        if (maze == null) {
            System.err.println("Error: Maze is not initialized!");
            return;
        }

        int newX, newY;

        // Repeat until a valid walkable position is found that is not the exit
        do {
            newX = random.nextInt(maze.getCol() - 2) + 1;
            newY = random.nextInt(maze.getRow() - 2) + 1;
        } while (mg.getMaze().getMaze()[newY][newX] != 0
                || (newX == mg.getMaze().getExitX() && newY == mg.getMaze().getExitY()));

        // Set new position for the player
        this.worldX = newX * mg.getTileSize();
        this.worldY = newY * mg.getTileSize();
    }

    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                if (spriteNum == 3) {
                    image = up1;
                }
                break;
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                if (spriteNum == 3) {
                    image = down1;
                }
                break;
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                if (spriteNum == 3) {
                    image = left3;
                }
                break;

            case "right":

                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                if (spriteNum == 3) {
                    image = right3;
                }

                break;

        }
        g2.drawImage(image, screenX, screenY, mg);

    }

    public void drawDebugCollisionBox(Graphics2D g2) {
        // Save current color
        Color oldColor = g2.getColor();

        // Draw collision box
        g2.setColor(Color.RED);
        g2.drawRect(screenX + solidArea.x,
                screenY + solidArea.y,
                solidArea.width,
                solidArea.height);

        // Restore original color
        g2.setColor(oldColor);
    }

    public int getX() {
        return this.worldX;
    }

    public int getY() {
        return this.worldY;
    }

    public int getVisionRadius() {
        return visionRadius;
    }

    public void setVisionRadius(int radius) {
        this.visionRadius = radius;
    }

    public void increaseVision() {
        visionRadius++;
    }

    public void decreaseVision() {
        if (visionRadius > 1) {
            visionRadius--;
        }
    }

    public void setX(int startX) {
        this.worldX = startX * mg.getTileSize();
    }

    public void setY(int startY) {
        this.worldY = startY * mg.getTileSize();
    }

}
