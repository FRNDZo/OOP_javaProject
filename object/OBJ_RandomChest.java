/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package object;

import entity.Entity;

import main.MazeGame;

/**
 *
 * @author User
 */
public class OBJ_RandomChest extends Entity {

    public OBJ_RandomChest(MazeGame mg) {
        super(mg);
        

        name = "RandomChest";
        down1 = setup("/img/object/Random_Chest");
        image = down1;


    }

}


