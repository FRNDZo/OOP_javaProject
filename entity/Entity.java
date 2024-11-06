/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.MazeGame;
import main.StartMaze;
import main.UtilityTool;
import object.OBJ_ExitDoor;

/**
 *
 * @author User
 */
public class Entity {
//    public int x, y;

    public MazeGame mg;
    public Player player;
    public int worldX, worldY;
    public int speed;

    public BufferedImage up1, up2, up3,
            down1, down2, down3,
            left1, left2, left3,
            right1, right2, right3;

    public String direction = "down";

    public int spriteCounter = 0;
    public int spriteNum = 1;

    public Rectangle solidArea;
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collisionOn = false;

    public BufferedImage image, Door1, Door2;
    public String name;
    public boolean collision = false;
    public int actionLockCounter = 0;
    public boolean invincible = false;
    public int invincibleCounter = 0;
    public int type;//0 is player, 1 is NPC, 2 is MONSTER

    public Entity(MazeGame mg) {
        this.mg = mg;
        this.solidArea = new Rectangle(0, 0, mg.getTileSize(), mg.getTileSize());

    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setAction() {

    }

    public void update() {
        setAction();

        collision = false;
        mg.cChecker.checkTile(this);
        mg.cChecker.checkObject(this, false);
        mg.cChecker.checkEntity(this, mg.monster);
        boolean contactPlayer = mg.cChecker.checkPlayer(this);
        //monster att player
        if (this.type == 2 && contactPlayer == true) {
            if (mg.player.invincible == false) {
                mg.gameState = mg.gameOverStage;
            }
        }

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
    }

    public void draw(Graphics2D g2) 
    {
        BufferedImage image = null;

        int screenX = StartMaze.screenWidth / 2 - (mg.getTileSize() / 2);
        int screenY = StartMaze.screenHeight / 2 - (mg.getTileSize() / 2);

        int screenDrawX = worldX - player.getX() + screenX;
        int screenDrawY = worldY - player.getY() + screenY;

        // Calculate player's center position in pixels
        int playerCenterX = player.getX() + player.solidArea.x + (player.solidArea.width / 2);
        int playerCenterY = player.getY() + player.solidArea.y + (player.solidArea.height / 2);

        // Convert to tile coordinates
        int playerTileCenterX = playerCenterX / mg.getTileSize();
        int playerTileCenterY = playerCenterY / mg.getTileSize();

        // Convert entity position to tile coordinates
        // Add offset to get center of entity
        int entityCenterX = (worldX + mg.getTileSize() / 2) / mg.getTileSize();
        int entityCenterY = (worldY + mg.getTileSize() / 2) / mg.getTileSize();

        // Calculate distance in tiles
        int distanceX = Math.abs(entityCenterX - playerTileCenterX);
        int distanceY = Math.abs(entityCenterY - playerTileCenterY);

        if (distanceX <= player.getVisionRadius() && distanceY <= player.getVisionRadius()) {
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

            g2.drawImage(image, screenDrawX, screenDrawY, mg.getTileSize(), mg.getTileSize(), mg);
        }
    }

    public BufferedImage setup(String imagePath) {
        UtilityTool uTool = new UtilityTool();
        BufferedImage image = null;

        try {
            image = ImageIO.read(getClass().getResourceAsStream(imagePath + ".png"));
            image = uTool.scaleImage(image, mg.getTileSize(), mg.getTileSize());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

}
