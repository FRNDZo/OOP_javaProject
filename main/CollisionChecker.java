/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

import entity.Entity;

/**
 *
 * @author User
 */
public class CollisionChecker {

    MazeGame mg;

    public CollisionChecker(MazeGame mg) {
        this.mg = mg;

    }

    public void checkTile(Entity entity) {

        int entityLeftWorldX = entity.worldX + entity.solidArea.x;
        int entityRightWorldX = entity.worldX + entity.solidArea.x + entity.solidArea.width;
        int entityTopWorldY = entity.worldY + entity.solidArea.y;
        int entityBottomWorldY = entity.worldY + entity.solidArea.y + entity.solidArea.height;

        int entityLeftCol = entityLeftWorldX / mg.getTileSize();
        int entityRightCol = entityRightWorldX / mg.getTileSize();
        int entityTopRow = entityTopWorldY / mg.getTileSize();
        int entityBottomRow = entityBottomWorldY / mg.getTileSize();

        int tileNum1, tileNum2;

        switch (entity.direction) {
            case "up":
                entityTopRow = (entityTopWorldY - entity.speed) / mg.getTileSize();

                tileNum1 = mg.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = mg.tileM.mapTileNum[entityRightCol][entityTopRow];
                if (mg.tileM.tile[tileNum1].collision || mg.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;

            case "down":
                entityBottomRow = (entityBottomWorldY + entity.speed) / mg.getTileSize();

                tileNum1 = mg.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                tileNum2 = mg.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (mg.tileM.tile[tileNum1].collision || mg.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;

            case "left":
                entityLeftCol = (entityLeftWorldX - entity.speed) / mg.getTileSize();
                tileNum1 = mg.tileM.mapTileNum[entityLeftCol][entityTopRow];
                tileNum2 = mg.tileM.mapTileNum[entityLeftCol][entityBottomRow];
                if (mg.tileM.tile[tileNum1].collision || mg.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;

            case "right":
                entityRightCol = (entityRightWorldX + entity.speed) / mg.getTileSize();
                tileNum1 = mg.tileM.mapTileNum[entityRightCol][entityTopRow];
                tileNum2 = mg.tileM.mapTileNum[entityRightCol][entityBottomRow];
                if (mg.tileM.tile[tileNum1].collision || mg.tileM.tile[tileNum2].collision) {
                    entity.collisionOn = true;
                }
                break;
        }
    }

    public int checkObject(Entity entity, boolean player) {

        int index = 999;

        for (int i = 0; i < mg.obj.length; i++) {

            if (mg.obj[i] != null) {

                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                mg.obj[i].solidArea.x = mg.obj[i].worldX + mg.obj[i].solidArea.x;
                mg.obj[i].solidArea.y = mg.obj[i].worldY + mg.obj[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                if (entity.solidArea.intersects(mg.obj[i].solidArea)) {
                    if (mg.obj[i].collision == true) {
                        entity.collisionOn = true;
                    }
                    if (player == true) {
                        index = i;
                    }
                }

                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                mg.obj[i].solidArea.x = mg.obj[i].solidAreaDefaultX;
                mg.obj[i].solidArea.y = mg.obj[i].solidAreaDefaultY;
            }

        }

        return index;
    }

    //MONSTER
    public int checkEntity(Entity entity, Entity[] target) {
        int index = 999;

        for (int i = 0; i < target.length; i++) {

            if (target[i] != null) {

                entity.solidArea.x = entity.worldX + entity.solidArea.x;
                entity.solidArea.y = entity.worldY + entity.solidArea.y;

                target[i].solidArea.x = target[i].worldX + target[i].solidArea.x;
                target[i].solidArea.y = target[i].worldY + target[i].solidArea.y;

                switch (entity.direction) {
                    case "up":
                        entity.solidArea.y -= entity.speed;
                        break;
                    case "down":
                        entity.solidArea.y += entity.speed;
                        break;
                    case "left":
                        entity.solidArea.x -= entity.speed;
                        break;
                    case "right":
                        entity.solidArea.x += entity.speed;
                        break;
                }

                if (entity.solidArea.intersects(target[i].solidArea)) {
                    if (target[i] != entity) {
                        entity.collisionOn = true;
                        index = i;
                    }

                }
                entity.solidArea.x = entity.solidAreaDefaultX;
                entity.solidArea.y = entity.solidAreaDefaultY;
                target[i].solidArea.x = target[i].solidAreaDefaultX;
                target[i].solidArea.y = target[i].solidAreaDefaultY;
            }

        }

        return index;
    }

    public boolean checkPlayer(Entity entity) {

        boolean contactPlayer = false;

        entity.solidArea.x = entity.worldX + entity.solidArea.x;
        entity.solidArea.y = entity.worldY + entity.solidArea.y;

        mg.player.solidArea.x = mg.player.worldX + mg.player.solidArea.x;
        mg.player.solidArea.y = mg.player.worldY + mg.player.solidArea.y;

        switch (entity.direction) {
            case "up":
                entity.solidArea.y -= entity.speed;
                break;
            case "down":
                entity.solidArea.y += entity.speed;
                break;
            case "left":
                entity.solidArea.x -= entity.speed;
                break;
            case "right":
                entity.solidArea.x += entity.speed;
                break;
        }
        
        if (entity.solidArea.intersects(mg.player.solidArea)) {
            entity.collisionOn = true;
            contactPlayer = true;
        }
        
        entity.solidArea.x = entity.solidAreaDefaultX;
        entity.solidArea.y = entity.solidAreaDefaultY;
        mg.player.solidArea.x = mg.player.solidAreaDefaultX;
        mg.player.solidArea.y = mg.player.solidAreaDefaultY;
        
        return contactPlayer;
    }

}
