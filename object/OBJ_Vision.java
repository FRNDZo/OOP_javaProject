/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import entity.Entity;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.MazeGame;

/**
 *
 * @author User
 */
public class OBJ_Vision extends Entity{

    public OBJ_Vision(MazeGame mg) {
        super(mg);
          
        name = "Vision";
        down1 = setup("/img/object/Vision");
        image = down1;
//          try {
//            image = ImageIO.read(getClass().getResourceAsStream("/img/object/Vision.png"));
//            uTool.scaleImage(image, mg.getTileSize(), mg.getTileSize());
//            if (image == null) {
//                System.err.println("Warning: Could not load vision image");
//            }
//        } catch (IOException e) {
//            System.err.println("Error loading vision image: " + e.getMessage());
//        }
    }
 
}
