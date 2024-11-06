package main;

import java.awt.*;

import java.awt.image.BufferedImage;

/**
 *
 * @author User
 */
public class UtilityTool {

    MazeGame mg;

    public void setMazeGame(MazeGame mg) {
        this.mg = mg;
    }

    public BufferedImage scaleImage(BufferedImage original, int width, int height) {
        // เพิ่มการตรวจสอบ
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException(
                    "Width and height must be positive values. Current values: width=" + width + ", height=" + height);
        }

        BufferedImage scaledImage = new BufferedImage(width, height, original.getType());
        Graphics2D g2 = scaledImage.createGraphics();
        g2.drawImage(original, 0, 0, width, height, null);
        g2.dispose();

        return scaledImage;
    }

}
