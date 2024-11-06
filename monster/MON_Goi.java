package monster;

import ai.Node;
import ai.PathFinder;
import entity.Entity;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Random;
import main.MazeGame;
import main.StartMaze;

public class MON_Goi extends Entity {

    public final int screenX;
    public final int screenY;
    PathFinder pathFinder = new PathFinder(mg,2);
    private int searchCooldown = 0;
    private final int SEARCH_INTERVAL = 60;
    private final int DETECTION_RADIUS = 60;
    private final double PATH_NODE_THRESHOLD = 8;
    private boolean isFollowingPath = false;
    private int currentPathIndex = 0;
    private final int BASE_SPEED = 1;

    private boolean onPath = false;
    private final int TILE_CENTER_OFFSET;

    public MON_Goi(MazeGame mg) {
        super(mg);

        screenX = StartMaze.screenWidth / 2 - (mg.getTileSize() / 2);
        screenY = StartMaze.screenHeight / 2 - (mg.getTileSize() / 2);
        TILE_CENTER_OFFSET = mg.getTileSize() / 2;

        type = 2;
        name = "Goi";
        speed = 2;
        direction = "up";

        solidArea.x = 15;
        solidArea.y = 20;
        solidArea.width = 18;
        solidArea.height = 25;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;

        centerEntityOnTile();

        getImage();
    }

    public void getImage() {

        up1 = setup("/img/monster/Monster1");
        up2 = setup("/img/monster/Monster2");
        up3 = setup("/img/monster/Monster3");

        down1 = setup("/img/monster/Monster1_right1");
        down2 = setup("/img/monster/Monster2_right2");
        down3 = setup("/img/monster/Monster3_right3");

        left1 = setup("/img/monster/Monster1");
        left2 = setup("/img/monster/Monster2");
        left3 = setup("/img/monster/Monster3");

        right1 = setup("/img/monster/Monster1_right1");
        right2 = setup("/img/monster/Monster2_right2");
        right3 = setup("/img/monster/Monster3_right3");

    }

    public void update() {
        super.update();

        // คำนวณระยะห่างจากผู้เล่น
        int xDistance = Math.abs(worldX - mg.player.worldX);
        int yDistance = Math.abs(worldY - mg.player.worldY);
        int tileDistance = (xDistance + yDistance) / mg.getTileSize();

        if (!onPath && tileDistance < DETECTION_RADIUS) {
            int i = new Random().nextInt(100) + 1;
            if (i > 50) {
                onPath = true;
                searchCooldown = 0; // บังคับให้ค้นหาเส้นทางทันที
            }
        }

        if (onPath && tileDistance > DETECTION_RADIUS * 2) {
            onPath = false;
            isFollowingPath = false;
        }

        if (collisionOn) {
            // ถ้าชนกำแพง ให้ค้นหาเส้นทางใหม่ทันที
            searchCooldown = 0;
        }
    }

    public void setAction() {
        if (onPath) {
            int goalCol = (mg.player.worldX + mg.player.solidArea.x) / mg.getTileSize();
            int goalRow = (mg.player.worldY + mg.player.solidArea.y) / mg.getTileSize();
            searchPath(goalCol, goalRow);
        } else {
            // คงการเคลื่อนที่แบบสุ่มเดิมไว้ แต่ปรับให้เดินตรงกลางทางเดิน
            actionLockCounter++;
            if (actionLockCounter == 120) {
                Random random = new Random();
                int i = random.nextInt(100) + 1;

                if (i <= 25) {
                    direction = "up";
                } else if (i <= 50) {
                    direction = "down";
                } else if (i <= 75) {
                    direction = "left";
                } else {
                    direction = "right";
                }

                actionLockCounter = 0;
                speed = BASE_SPEED;
                centerEntityOnTile(); // จัดตำแหน่งให้อยู่กึ่งกลางทางเดินทุกครั้งที่เปลี่ยนทิศทาง
            }
        }
    }

    private void searchPath(int goalCol, int goalRow) {
        int startCol = (worldX + solidArea.x) / mg.getTileSize();
        int startRow = (worldY + solidArea.y) / mg.getTileSize();

        searchCooldown--;

        
        if (!isFollowingPath || (searchCooldown <= 0
                && (Math.abs(goalCol - pathFinder.goalNode.col) > 1
                || Math.abs(goalRow - pathFinder.goalNode.row) > 1))) {

            pathFinder.setNodes(startCol, startRow, goalCol, goalRow);
            if (pathFinder.search()) {
                isFollowingPath = true;
                currentPathIndex = 0;
                searchCooldown = SEARCH_INTERVAL;
            } else {
                // ถ้าหาเส้นทางไม่พบ ให้หยุดไล่
                onPath = false;
                isFollowingPath = false;
                return;
            }
        }

        if (isFollowingPath && !pathFinder.pathList.isEmpty()) {
            followPath();
        }
    }
    
    private void followPath() {
        if (currentPathIndex >= pathFinder.pathList.size()) {
            isFollowingPath = false;
            return;
        }

        Node targetNode = pathFinder.pathList.get(currentPathIndex);
        
        // คำนวณตำแหน่งกึ่งกลางของ tile เป้าหมาย
        int targetX = targetNode.col * mg.getTileSize() + (mg.getTileSize() / 2);
        int targetY = targetNode.row * mg.getTileSize() + (mg.getTileSize() / 2);
        
        // คำนวณระยะห่างจากตำแหน่งปัจจุบันถึงเป้าหมาย
        double xDiff = targetX - (worldX + solidArea.x);
        double yDiff = targetY - (worldY + solidArea.y);
        double distance = Math.sqrt(xDiff * xDiff + yDiff * yDiff);

        // ถ้าถึงจุดหมายแล้ว (ใกล้พอ)
        if (distance < PATH_NODE_THRESHOLD) {
            currentPathIndex++;
            return;
        }

        // ปรับความเร็วตามระยะห่างจากผู้เล่น
        int distanceToPlayer = Math.abs(worldX - mg.player.worldX) + 
                             Math.abs(worldY - mg.player.worldY);
        speed = BASE_SPEED + (distanceToPlayer > mg.getTileSize() * 5 ? 1 : 0);

        // คำนวณเวกเตอร์การเคลื่อนที่
        double moveX = (xDiff / distance) * speed;
        double moveY = (yDiff / distance) * speed;

        // อัพเดตตำแหน่งและทิศทาง
        worldX += Math.round(moveX);
        worldY += Math.round(moveY);

        // อัพเดตทิศทางการเคลื่อนที่
        if (Math.abs(xDiff) > Math.abs(yDiff)) {
            direction = (xDiff > 0) ? "right" : "left";
        } else {
            direction = (yDiff > 0) ? "down" : "up";
        }
    }

    private void centerEntityOnTile() {
        int currentTileX = worldX / mg.getTileSize();
        int currentTileY = worldY / mg.getTileSize();
        worldX = currentTileX * mg.getTileSize() + TILE_CENTER_OFFSET - solidArea.width / 2;
        worldY = currentTileY * mg.getTileSize() + TILE_CENTER_OFFSET - solidArea.height / 2;
    }

    public void drawDebugCollisionBox(Graphics2D g2) {
        // Save current color
        Color oldColor = g2.getColor();

        // Calculate screen position relative to player
        int screenDrawX = worldX - player.worldX + screenX;
        int screenDrawY = worldY - player.worldY + screenY;

        // Draw collision box
        g2.setColor(Color.GREEN);
        g2.drawRect(screenDrawX + solidArea.x,
                screenDrawY + solidArea.y,
                solidArea.width,
                solidArea.height);

        // Restore original color
        g2.setColor(oldColor);
    }

    public void setX(int startX) {
        this.worldX = startX * mg.getTileSize();
    }

    public void setY(int startY) {
        this.worldY = startY * mg.getTileSize();
    }

//    @Override
//    public void draw(Graphics2D g2) {
//        super.draw(g2);
//
//        // Draw path if monster is chasing player
//        int monsterCol = worldX / mg.getTileSize();
//        int monsterRow = worldY / mg.getTileSize();
//        int playerCol = mg.player.worldX / mg.getTileSize();
//        int playerRow = mg.player.worldY / mg.getTileSize();
//
//        int distanceX = Math.abs(monsterCol - playerCol);
//        int distanceY = Math.abs(monsterRow - playerRow);
//
//        if (distanceX <= DETECTION_RADIUS && distanceY <= DETECTION_RADIUS) {
//            drawPath(g2);
//        }
//    }
//
//    private void drawPath(Graphics2D g2) {
//        // Save original stroke and color
//        Color originalColor = g2.getColor();
//        Stroke originalStroke = g2.getStroke();
//
//        // Set path drawing style
//        g2.setColor(new Color(255, 0, 0, 180)); // Semi-transparent red
//        g2.setStroke(new BasicStroke(3)); // Thicker line
//
//        for (int i = 0; i < pathFinder.pathList.size() - 1; i++) {
//            Node currentNode = pathFinder.pathList.get(i);
//            Node nextNode = pathFinder.pathList.get(i + 1);
//
//            // Convert world coordinates to screen coordinates
//            int currentScreenX = currentNode.col * mg.getTileSize() - mg.player.worldX + screenX;
//            int currentScreenY = currentNode.row * mg.getTileSize() - mg.player.worldY + screenY;
//            int nextScreenX = nextNode.col * mg.getTileSize() - mg.player.worldX + screenX;
//            int nextScreenY = nextNode.row * mg.getTileSize() - mg.player.worldY + screenY;
//
//            // Draw line between nodes
//            g2.drawLine(
//                    currentScreenX + mg.getTileSize() / 2,
//                    currentScreenY + mg.getTileSize() / 2,
//                    nextScreenX + mg.getTileSize() / 2,
//                    nextScreenY + mg.getTileSize() / 2
//            );
//
//            // Draw node points
//            int nodeSize = 8;
//            g2.fillOval(
//                    currentScreenX + mg.getTileSize() / 2 - nodeSize / 2,
//                    currentScreenY + mg.getTileSize() / 2 - nodeSize / 2,
//                    nodeSize,
//                    nodeSize
//            );
//        }
//
//        // Draw last node if path exists
//        if (!pathFinder.pathList.isEmpty()) {
//            Node lastNode = pathFinder.pathList.get(pathFinder.pathList.size() - 1);
//            int lastScreenX = lastNode.col * mg.getTileSize() - mg.player.worldX + screenX;
//            int lastScreenY = lastNode.row * mg.getTileSize() - mg.player.worldY + screenY;
//
//            g2.fillOval(
//                    lastScreenX + mg.getTileSize() / 2 - 4,
//                    lastScreenY + mg.getTileSize() / 2 - 4,
//                    8,
//                    8
//            );
//        }
//
//        // Add debug info (optional)
//        if (mg.keyH.showDebugText) {
//            g2.setColor(Color.WHITE);
//            g2.drawString("Path Length: " + pathFinder.pathList.size(), 10, 500);
//        }
//
//        // Restore original graphics settings
//        g2.setColor(originalColor);
//        g2.setStroke(originalStroke);
//    }
}
