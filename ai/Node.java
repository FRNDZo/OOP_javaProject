/////*
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
//// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
//// */
////package ai;
////
/////**
//// *
//// * @author User
//// */
////public class Node {
////    
////    Node parent;
////    public int col;
////    public int row;
////    int gCost;
////    int hCost;
////    int fCost;
////    boolean solid;
////    boolean open;
////    boolean checked;
////
////    public Node(int col, int row) {
////        this.col = col;
////        this.row = row;
////        
////    }
////    
////    
////}
//
//package ai;
//
//public class Node {
//    public Node parent;
//    public int col;
//    public int row;
//    public int gCost;  // Distance from starting node
//    public int hCost;  // Distance from goal node
//    public int fCost;  // Total cost (gCost + hCost)
//    public boolean solid;
//    public boolean open;
//    public boolean checked;
//    
//    public Node(int col, int row) {
//        this.col = col;
//        this.row = row;
//    }
//    
//    public void calculateFCost() {
//        fCost = gCost + hCost;
//    }
//}
package ai;

public class Node {

    public Node parent;
    public int col;
    public int row;
    public int gCost;  // Distance from starting node
    public int hCost;  // Distance from goal node
    public int fCost;  // Total cost (gCost + hCost)
    public boolean solid;
    public boolean open;
    public boolean checked;

    public Node(int col, int row) {
        this.col = col;
        this.row = row;
    }

    public void calculateFCost() {
        fCost = gCost + hCost;
    }
}
