//package ai;
//
//import java.util.ArrayList;
//import main.MazeGame;
//
//public class PathFinder {
//    MazeGame mg;
//    Node[][] node;
//    ArrayList<Node> openList = new ArrayList<>();
//    public ArrayList<Node> pathList = new ArrayList<>();
//    Node startNode, goalNode, currentNode;
//    boolean goalReached = false;
//    int step = 0;
//    
//    private final int MAX_SEARCH_STEPS = 200;  // Limit search steps for performance
//
//    public PathFinder(MazeGame mg) {
//        this.mg = mg;
//        instantiateNodes();
//    }
//
//    public void instantiateNodes() {
//        node = new Node[mg.maxWorldCol][mg.maxWorldRow];
//        
//        for (int row = 0; row < mg.maxWorldRow; row++) {
//            for (int col = 0; col < mg.maxWorldCol; col++) {
//                node[col][row] = new Node(col, row);
//            }
//        }
//    }
//
//    public void resetNodes() {
//        // Reset all nodes
//        for (int row = 0; row < mg.maxWorldRow; row++) {
//            for (int col = 0; col < mg.maxWorldCol; col++) {
//                node[col][row].reset();
//            }
//        }
//        
//        openList.clear();
//        pathList.clear();
//        goalReached = false;
//        step = 0;
//    }
//
//    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
//        resetNodes();
//
//        // Validate coordinates
//        startCol = Math.min(Math.max(startCol, 0), mg.maxWorldCol - 1);
//        startRow = Math.min(Math.max(startRow, 0), mg.maxWorldRow - 1);
//        goalCol = Math.min(Math.max(goalCol, 0), mg.maxWorldCol - 1);
//        goalRow = Math.min(Math.max(goalRow, 0), mg.maxWorldRow - 1);
//
//        // Set Start and Goal nodes
//        startNode = node[startCol][startRow];
//        currentNode = startNode;
//        goalNode = node[goalCol][goalRow];
//        openList.add(currentNode);
//
//        // Set solid nodes based on maze walls
//        for (int row = 0; row < mg.maxWorldRow; row++) {
//            for (int col = 0; col < mg.maxWorldCol; col++) {
//                int tileNum = mg.tileM.mapTileNum[col][row];
//                if (mg.tileM.tile[tileNum].collision) {
//                    node[col][row].solid = true;
//                }
//            }
//        }
//    }
//
//    public boolean search() {
//        while (!goalReached && step < MAX_SEARCH_STEPS) {
//            int col = currentNode.col;
//            int row = currentNode.row;
//
//            // Check the current node
//            currentNode.checked = true;
//            openList.remove(currentNode);
//
//            // Check adjacent nodes
//            checkNode(col, row - 1); // Up
//            checkNode(col, row + 1); // Down
//            checkNode(col - 1, row); // Left
//            checkNode(col + 1, row); // Right
//
//            // Find the best node
//            int bestNodeIndex = findBestNode();
//            
//            if (bestNodeIndex == -1) {
//                break; // No path found
//            }
//
//            currentNode = openList.get(bestNodeIndex);
//
//            if (currentNode == goalNode) {
//                goalReached = true;
//                trackThePath();
//            }
//            
//            step++;
//        }
//        
//        return goalReached && !pathList.isEmpty();
//    }
//
//    private void openNode(Node node) {
//        if (!node.open && !node.checked && !node.solid) {
//            node.open = true;
//            node.parent = currentNode;
//            openList.add(node);
//
//            // Calculate costs
//            // G cost - distance from start node
//            int xDistance = Math.abs(node.col - startNode.col);
//            int yDistance = Math.abs(node.row - startNode.row);
//            node.gCost = xDistance + yDistance;
//
//            // H cost - distance from goal node
//            xDistance = Math.abs(node.col - goalNode.col);
//            yDistance = Math.abs(node.row - goalNode.row);
//            node.hCost = xDistance + yDistance;
//
//            // F cost
//            node.calculateFCost();
//        }
//    }
//
//    private void trackThePath() {
//        // Backtrack from goal to start
//        Node current = goalNode;
//
//        while (current != startNode) {
//            pathList.add(0, current);
//            current = current.parent;
//        }
//    }
//    
//     private void checkNode(int col, int row) {
//        // Check if coordinates are within bounds
//        if (col < 0 || col >= mg.maxWorldCol || row < 0 || row >= mg.maxWorldRow) {
//            return;
//        }
//
//        // Get the node at the specified position
//        Node targetNode = node[col][row];
//
//        // Check if the node is already processed or is solid
//        if (targetNode.checked || targetNode.solid) {
//            return;
//        }
//
//        // Open the node for processing
//        openNode(targetNode);
//    }
//
//    private int findBestNode() {
//        if (openList.isEmpty()) {
//            return -1;
//        }
//
//        int bestNodeIndex = 0;
//        int bestFCost = openList.get(0).fCost;
//
//        for (int i = 1; i < openList.size(); i++) {
//            Node currentNode = openList.get(i);
//            
//            // If current node has lower F cost or equal F cost but lower H cost
//            if (currentNode.fCost < bestFCost || 
//                (currentNode.fCost == bestFCost && currentNode.hCost < openList.get(bestNodeIndex).hCost)) {
//                bestNodeIndex = i;
//                bestFCost = currentNode.fCost;
//            }
//        }
//
//        return bestNodeIndex;
//    }
//
//    public void resetSearchData() {
//        openList.clear();
//        pathList.clear();
//        goalReached = false;
//        step = 0;
//        currentNode = startNode;
//    }
//}
package ai;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Comparator;
import main.MazeGame;

public class PathFinder {
    private final MazeGame mg;
    private Node[][] nodes;
    private final PriorityQueue<Node> openList;
    public ArrayList<Node> pathList;
    public Node startNode, goalNode, currentNode;
    private boolean goalReached;
    private int step;
    private final int stepSize; // จำนวนช่องที่สามารถก้าวข้ามได้ในแต่ละครั้ง
    
    private static final int MAX_SEARCH_STEPS = 300;
    private static final int DIAGONAL_COST = 14;
    private static final int STRAIGHT_COST = 10; // Base cost for straight movement

     public PathFinder(MazeGame mg, int stepSize) {
        this.mg = mg;
        this.stepSize = stepSize;
        this.openList = new PriorityQueue<>(new NodeComparator());
        this.pathList = new ArrayList<>();
        instantiateNodes();
    }

    private static class NodeComparator implements Comparator<Node> {

        @Override
        public int compare(Node n1, Node n2) {
            // First compare by fCost
            int fCostComparison = Integer.compare(n1.fCost, n2.fCost);
            if (fCostComparison != 0) {
                return fCostComparison;
            }
            // If fCosts are equal, compare by hCost
            return Integer.compare(n1.hCost, n2.hCost);
        }
    }

    public void instantiateNodes() {
        nodes = new Node[mg.maxWorldCol][mg.maxWorldRow];
        for (int col = 0; col < mg.maxWorldCol; col++) {
            for (int row = 0; row < mg.maxWorldRow; row++) {
                nodes[col][row] = new Node(col, row);
            }
        }
    }

    public void resetNodes() {
        for (int col = 0; col < mg.maxWorldCol; col++) {
            for (int row = 0; row < mg.maxWorldRow; row++) {
                Node node = nodes[col][row];
                node.parent = null;
                node.gCost = 0;
                node.hCost = 0;
                node.fCost = 0;
                node.open = false;
                node.checked = false;
                // Don't reset solid state as it depends on map layout
            }
        }
        openList.clear();
        pathList.clear();
        goalReached = false;
        step = 0;
    }

    public void setNodes(int startCol, int startRow, int goalCol, int goalRow) {
        resetNodes();

        // Validate and clamp coordinates
        startCol = Math.min(Math.max(startCol, 0), mg.maxWorldCol - 1);
        startRow = Math.min(Math.max(startRow, 0), mg.maxWorldRow - 1);
        goalCol = Math.min(Math.max(goalCol, 0), mg.maxWorldCol - 1);
        goalRow = Math.min(Math.max(goalRow, 0), mg.maxWorldRow - 1);

        // Set start and goal nodes
        startNode = nodes[startCol][startRow];
        currentNode = startNode;
        goalNode = nodes[goalCol][goalRow];

        // Initialize starting node
        startNode.gCost = 0;
        startNode.hCost = calculateHeuristic(startNode, goalNode);
        startNode.calculateFCost();
        openList.offer(currentNode);

        // Update solid nodes based on collision map
        updateSolidNodes();
    }

    private void updateSolidNodes() {
        for (int col = 0; col < mg.maxWorldCol; col++) {
            for (int row = 0; row < mg.maxWorldRow; row++) {
                int tileNum = mg.tileM.mapTileNum[col][row];
                nodes[col][row].solid = mg.tileM.tile[tileNum].collision;
            }
        }
    }

    private int calculateHeuristic(Node start, Node target) {
        // Using octile distance for more accurate heuristic
        int dx = Math.abs(start.col - target.col);
        int dy = Math.abs(start.row - target.row);
        return STRAIGHT_COST * Math.max(dx, dy) + (DIAGONAL_COST - STRAIGHT_COST) * Math.min(dx, dy);
    }

    public boolean search() {
        while (!goalReached && step < MAX_SEARCH_STEPS && !openList.isEmpty()) {
            currentNode = openList.poll();
            currentNode.checked = true;

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
                break;
            }

            // ตรวจสอบโหนดรอบๆ ตามระยะ stepSize
            for (int i = 1; i <= stepSize; i++) {
                // แนวตั้ง และ แนวนอน
                checkNode(currentNode.col - i, currentNode.row); // ซ้าย
                checkNode(currentNode.col + i, currentNode.row); // ขวา
                checkNode(currentNode.col, currentNode.row - i); // บน
                checkNode(currentNode.col, currentNode.row + i); // ล่าง
            }

            step++;
        }
        
        return goalReached && !pathList.isEmpty();
    }

    private void checkNode(int col, int row) {
        // ตรวจสอบขอบเขต
        if (col < 0 || col >= mg.maxWorldCol || row < 0 || row >= mg.maxWorldRow) {
            return;
        }

        Node target = nodes[col][row];

        // ตรวจสอบว่ามีสิ่งกีดขวางระหว่างทางหรือไม่
        if (!isPathClear(currentNode.col, currentNode.row, col, row)) {
            return;
        }

        // คำนวณระยะทางจริง
        int distance = Math.abs(currentNode.col - col) + Math.abs(currentNode.row - row);
        int newGCost = currentNode.gCost + (STRAIGHT_COST * distance);
        
        if (!target.open || newGCost < target.gCost) {
            target.gCost = newGCost;
            target.hCost = calculateHeuristic(target, goalNode);
            target.calculateFCost();
            target.parent = currentNode;

            if (!target.open) {
                openList.offer(target);
                target.open = true;
            }
        }
    }

     private boolean isPathClear(int startCol, int startRow, int endCol, int endRow) {
        // ใช้ Bresenham's line algorithm เพื่อตรวจสอบเส้นทาง
        int dx = Math.abs(endCol - startCol);
        int dy = Math.abs(endRow - startRow);
        int x = startCol;
        int y = startRow;
        
        int sx = startCol < endCol ? 1 : -1;
        int sy = startRow < endRow ? 1 : -1;
        int err = dx - dy;

        while (true) {
            if (x == endCol && y == endRow) break;
            
            // ตรวจสอบว่าช่องปัจจุบันเป็น solid หรือไม่
            if (nodes[x][y].solid) {
                return false;
            }

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x += sx;
            }
            if (e2 < dx) {
                err += dx;
                y += sy;
            }
        }
        
        return !nodes[endCol][endRow].solid;
    }
     
    private void trackThePath() {
        pathList.clear();
        Node current = goalNode;

        while (current != startNode) {
            pathList.add(0, current);
            current = current.parent;
        }
    }

    public static PathFinder createWithStepSize(MazeGame mg, int stepSize) {
        return new PathFinder(mg, stepSize);
    }
    
    public void reset() {
        resetNodes();
        currentNode = startNode;
    }
}
