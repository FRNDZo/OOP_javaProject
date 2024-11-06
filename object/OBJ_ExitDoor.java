package object;

import entity.Entity;
import java.awt.Color;
import java.awt.Graphics2D;
import main.MazeGame;
import main.StartMaze;

public class OBJ_ExitDoor extends Entity {

    public final int screenX;
    public final int screenY;

    public OBJ_ExitDoor(MazeGame mg) {
        super(mg);

        screenX = StartMaze.screenWidth / 2 - (mg.getTileSize() / 2);
        screenY = StartMaze.screenHeight / 2 - (mg.getTileSize() / 2);

        name = "ExitDoor";
        down1 = setup("/img/object/Exit_door");
//        image = down1;
  
        
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

    }

    public void drawDebugCollisionBox(Graphics2D g2) {
        // Save current color
        Color oldColor = g2.getColor();

        // Calculate screen position relative to player
        int screenDrawX = worldX - player.worldX + screenX;
        int screenDrawY = worldY - player.worldY + screenY;

        // Draw collision box
        g2.setColor(Color.RED);
        g2.drawRect(screenDrawX + solidArea.x,
                screenDrawY + solidArea.y,
                solidArea.width,
                solidArea.height);

        // Restore original color
        g2.setColor(oldColor);
    }

}
