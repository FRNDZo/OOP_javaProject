package main;

import entity.Entity;
import entity.Player;
import maps.Maze;
import monster.MON_Goi;
import object.OBJ_ExitDoor;
//import object.OBJ_ExitDoor;
import object.OBJ_RandomChest;

/**
 *
 * @author User
 */
public class AssetSetter {

    MazeGame mg;
    Maze maze;

    public AssetSetter(MazeGame mg) {
        this.mg = mg;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public void setPlayer(Player player) {
        for (Entity entity : mg.obj) {
            if (entity != null) {
                entity.setPlayer(player);
            }
        }
        for (Entity entity : mg.monster) {
            if (entity != null) {
                entity.setPlayer(player);
            }
        }
    }

    public void setObject(int numItems) {

        mg.obj[0] = new OBJ_ExitDoor(mg);
        mg.obj[0].setPlayer(mg.player);
        mg.obj[0].worldX = maze.getExitX() * mg.getTileSize();
        mg.obj[0].worldY = maze.getExitY() * mg.getTileSize();

        System.out.println("Exit door placed at: (" + maze.getExitX() + "," + maze.getExitY() + ")");

        for (int i = 1; i <= numItems; i++) {
            mg.obj[i] = new OBJ_RandomChest(mg);
            mg.obj[i].setPlayer(mg.player);

            int gridX = maze.getItemX();
            int gridY = maze.getItemY();

            mg.obj[i].worldX = gridX * mg.getTileSize();
            mg.obj[i].worldY = gridY * mg.getTileSize();

            System.out.println("Created chest " + i + " at grid(" + gridX + "," + gridY
                    + ") pixel(" + mg.obj[i].worldX + "," + mg.obj[i].worldY + ")");

            maze.setRandomItem(); 
            if (i < numItems) {
                maze.setRandomItem(); 
            }
        }
    }

    public void setMonster() {

        mg.monster[0] = new MON_Goi(mg);
        mg.monster[0].setPlayer(mg.player);
        mg.monster[0].worldX = maze.getMonsterX() * mg.getTileSize();
        mg.monster[0].worldY = maze.getMonsterY() * mg.getTileSize();

    }
}
