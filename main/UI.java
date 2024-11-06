package main;

import entity.Player;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Duration;
import java.time.LocalTime;
import javax.swing.Timer;
import object.OBJ_RandomChest;
import object.OBJ_Vision;

public class UI {

    MazeGame mg;
    Player player;
    Font arial_15, arial_40, arialBOLD_15,
            arialBOLD_20, arialBOLD_58;
    Graphics2D g2;
    private Timer gameTimer;
    private LocalTime startTime;
    BufferedImage ChestImage, VisionImage;
    public boolean messageOn = false;
    public String message = "";
    int messageCounter = 0;
    private Duration elapsedTime;
    public int commandNum = 0;
    public int subCommandNum = 0;
    public int titleScreenState = 0;//0 is MAIN MENU, 1 is Select difficulty level MENU 
    int subState = 0;
    public String currentDialogue = "";

    public UI(MazeGame mg) {
        this.mg = mg;

//        if (mg.getTileSize() <= 0) {
//            throw new IllegalArgumentException("TileSize must be greater than 0 when creating UI");
//        }
        arial_15 = new Font("Times New Roman", Font.PLAIN, 15);
        arial_40 = new Font("Times New Roman", Font.PLAIN, 40);
        arialBOLD_15 = new Font("Times New Roman", Font.BOLD, 15);
        arialBOLD_20 = new Font("Times New Roman", Font.BOLD, 20);
        arialBOLD_58 = new Font("Times New Roman", Font.BOLD, 58);
        OBJ_RandomChest rChest = new OBJ_RandomChest(mg);
        OBJ_Vision vision = new OBJ_Vision(mg);
        ChestImage = rChest.image;
        VisionImage = vision.image;

        startTime = LocalTime.now();
        elapsedTime = Duration.ZERO;
        gameTimer = new Timer(1000, e -> {
            LocalTime now = LocalTime.now();
            elapsedTime = Duration.between(startTime, now);
        });
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void startTimer() {
        if (!gameTimer.isRunning()) {
            if (elapsedTime != null) {
                startTime = LocalTime.now().minus(elapsedTime);
            } else {
                startTime = LocalTime.now();
            }
            gameTimer.start();
        }
    }

    public void stopTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
    }

    public void resetTimer() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        startTime = LocalTime.now();
        elapsedTime = Duration.ZERO;
        gameTimer.start();
    }

    public void showMessage(String text) {

        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2) {

        this.g2 = g2;

        g2.setFont(arial_40);
        g2.setColor(Color.white);

        //TITLE STATE
        if (mg.gameState == mg.titleState) {
            drawTitleScreen();
        }

        //PlAY STATE
        if (mg.gameState == mg.playState) {
            drawMessage();
        }

        //PAUSE STATE
        if (mg.gameState == mg.pauseState) {
            drawPauseScreen();
        }

        //OPTION STATE
        if (mg.gameState == mg.optionState) {
            drawOptionScreen();
        }

        //GAMEOVER STATE
        if (mg.gameState == mg.gameOverStage) {
            drawGameOverScreen();
        }

        if (mg.gameState == mg.gameCompleteStage) {
            drawGameCompleteScreen();
        }

        g2.setFont(arialBOLD_15);
    }

    public void drawTitleScreen() {

        g2.setColor(new Color(49, 54, 63));
        g2.fillRect(0, 0, StartMaze.screenWidth, StartMaze.screenHeight);

        if (titleScreenState == 0) {

            //TITLE NAME
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 64F));
            String text = "Fazo The Maze Runner";
            int x = getXforCenteredText(text);
            int y = mg.getTileSize() * 3;

            //SHADOW
            g2.setColor(Color.black);
            g2.drawString(text, x + 4, y + 4);
            //MAIN Color
            g2.setColor(Color.white);
            g2.drawString(text, x, y);

            //Image ON Menu Screen if want
//        x = StartMaze.screenWidth / 2 - ( mg.getTileSize() * 2 ) - 300;
//        y = mg.getTileSize() ;
//        g2.drawImage( mg.player.right3 , x , y ,mg.getTileSize() * 2 ,mg.getTileSize() * 2, null );
//        
            //MENU
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 42F));
            text = "NEW GAME";
            x = getXforCenteredText(text);
            y += mg.getTileSize() * 4;
            g2.drawString(text, x, y);
            if (commandNum == 0) {
                g2.drawString(">", x - mg.getTileSize(), y);
                g2.setColor(new Color(236, 223, 204));
                g2.drawString(text, x, y);

            }

            g2.setColor(Color.WHITE);
            text = "LOAD GAME";
            x = getXforCenteredText(text);
            y += mg.getTileSize() + 25;
            g2.drawString(text, x, y);
//            if (commandNum == 1) {
//                g2.drawString(">", x - mg.getTileSize(), y);
//                g2.setColor(new Color(236, 223, 204));
//                g2.drawString(text, x, y);
//
//            }

            g2.setColor(Color.WHITE);
            text = "QUIT";
            x = getXforCenteredText(text);
            y += mg.getTileSize() + 25;
            g2.drawString(text, x, y);
            if (commandNum == 1) {
                g2.drawString(">", x - mg.getTileSize(), y);
                g2.setColor(new Color(214, 189, 152));
                g2.drawString(text, x, y);

            }

        } else if (titleScreenState == 1) {

            //Select difficulty level SCREEN
            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(42F));

            String text = "Select Difficulty";
            int x = getXforCenteredText(text);
            int y = mg.getTileSize() * 3;
            g2.drawString(text, x, y);

            text = "Easy";
            x = getXforCenteredText(text);
            y += mg.getTileSize() * 3;
            g2.drawString(text, x, y);
            if (subCommandNum == 0) {
                g2.drawString(">", x - mg.getTileSize(), y);
                g2.setColor(new Color(236, 223, 204));
                g2.drawString(text, x, y);

            }

            g2.setColor(Color.WHITE);
            text = "Normal";
            x = getXforCenteredText(text);
            y += mg.getTileSize() + 25;
            g2.drawString(text, x, y);
            if (subCommandNum == 1) {
                g2.drawString(">", x - mg.getTileSize(), y);
                g2.setColor(new Color(236, 223, 204));
                g2.drawString(text, x, y);
            }

            g2.setColor(Color.WHITE);
            text = "Hard";
            x = getXforCenteredText(text);
            y += mg.getTileSize() + 25;
            g2.drawString(text, x, y);
            if (subCommandNum == 2) {
                g2.drawString(">", x - mg.getTileSize(), y);
                g2.setColor(new Color(236, 223, 204));
                g2.drawString(text, x, y);
            }

            g2.setColor(Color.WHITE);
            text = "Back";
            x = getXforCenteredText(text);
            y += mg.getTileSize() + 50;
            g2.drawString(text, x, y);
            if (subCommandNum == 3) {
                g2.drawString(">", x - mg.getTileSize(), y);
                g2.setColor(new Color(214, 189, 152));
                g2.drawString(text, x, y);
            }

        }

    }

    public void drawMessage() {
        g2.setFont(arialBOLD_15);
        g2.setColor(Color.WHITE);
        g2.drawImage(ChestImage, (mg.getTileSize() / 2) + 10, (mg.getTileSize() / 2), 40, 40, null);
        g2.drawString("x " + mg.subNumItems, (mg.getTileSize() / 2) + 65, (mg.getTileSize() / 2) + 30);

        g2.drawImage(VisionImage, (mg.getTileSize() / 2), (mg.getTileSize() / 2) + 40, 60, 60, null);
        g2.drawString(" " + player.getVisionRadius() + " Blocks", (mg.getTileSize() / 2) + 65, (mg.getTileSize() / 2) + 75);

        g2.drawString("Scores :  " + player.score, (mg.getTileSize() / 2), (mg.getTileSize() / 2) + 125);
        g2.drawString("Floor: " + player.currentLevel, (mg.getTileSize() / 2), (mg.getTileSize() / 2) + 165);
        //Time
        String timeString = String.format("%02d:%02d:%02d",
                elapsedTime.toHours(),
                elapsedTime.toMinutesPart(),
                elapsedTime.toSecondsPart());
        g2.setColor(Color.GREEN);
        g2.setFont(arialBOLD_20);
        g2.drawString("Time: " + timeString, getXforCenteredText("Time: 00:00:00"), 20);

        //message
        if (messageOn == true) {

            g2.setColor(Color.WHITE);
            g2.setFont(g2.getFont().deriveFont(20F));
            g2.drawString(message, (mg.getTileSize() / 2), mg.getTileSize() * 10);

            messageCounter++;

            if (messageCounter > 120) {
                messageCounter = 0;
                messageOn = false;
            }
        }
    }

    public void drawPauseScreen() {

        String text = "PAUSED";
        int x = getXforCenteredText(text);

        int y = StartMaze.screenHeight / 2;

        g2.drawString(text, x, y);
    }

    public void drawGameOverScreen() {

        g2.setColor(new Color(0, 0, 0, 150));
        g2.fillRect(0, 0, StartMaze.screenWidth, StartMaze.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 110f));

        text = "Game Over";
        //shadow
        g2.setColor(Color.black);
        x = getXforCenteredText(text);
        y = mg.getTileSize() * 4;
        g2.drawString(text, x, y);
        //main
        g2.setColor(Color.white);
        g2.drawString(text, x - 4, y - 4);

        //Retry
        g2.setFont(g2.getFont().deriveFont(50f));
        text = "Retry";
        x = getXforCenteredText(text);
        y += mg.getTileSize() * 4;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - 40, y);
        }

        //back to Main Menu
        text = "Main menu";
        x = getXforCenteredText(text);
        y += 60;
        g2.drawString(text, x, y);
        if (commandNum == 1) {
            g2.drawString(">", x - 40, y);
        }

    }

    public void drawGameCompleteScreen() {

        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRect(0, 0, StartMaze.screenWidth, StartMaze.screenHeight);

        int x;
        int y;
        String text;
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 90f));

        text = "Congratulations!!";
        //shadow
        g2.setColor(Color.gray);
        x = getXforCenteredText(text);
        y = mg.getTileSize() * 4;
        g2.drawString(text, x, y);
        //main
        g2.setColor(Color.yellow);
        g2.drawString(text, x - 3, y - 3);

        //Show final score
        //shadow
        g2.setColor(Color.black);
        g2.setFont(g2.getFont().deriveFont(30f));
        text = "Your Score : " + player.score;
        x = getXforCenteredText(text);
        y += mg.getTileSize() * 3;
        g2.drawString(text, x, y);
        //main
        g2.setColor(Color.white);
        g2.drawString(text, x - 2, y - 2);

        //back to Main Menu
        g2.setFont(g2.getFont().deriveFont(Font.BOLD, 40f));
        text = "Main menu";
        x = getXforCenteredText(text);
        y += mg.getTileSize() * 3;
        g2.drawString(text, x, y);
        if (commandNum == 0) {
            g2.drawString(">", x - 40, y);
        }

    }

    public void drawOptionScreen() {
        g2.setColor(Color.white);
        g2.setFont(g2.getFont().deriveFont(40F));

        //SUB WINDOW
        int frameX = mg.getTileSize() * 4;
        int frameY = mg.getTileSize();
        int frameWidth = mg.getTileSize() * 8;
        int frameHeight = mg.getTileSize() * 10;

        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        switch (subState) {
            case 0:
                option_top(frameX, frameY);
                break;
            case 1:
                option_control(frameX, frameY);
                break;
            case 2:
                option_backToMainConfirmation(frameX, frameY);
                break;
            case 3:
                option_resetConfirmation(frameX, frameY);
                break;
        }

        mg.keyH.enterPressed = false;
    }

    public void option_top(int frameX, int frameY) {

        int textX;
        int textY;

        //title
        String text = "OPTIONS";
        textX = getXforCenteredText(text);
        textY = frameY + mg.getTileSize();
        g2.drawString(text, textX, textY);

        g2.setFont(g2.getFont().deriveFont(25F));
        //CONTROL
        text = "CONTROL";
        textX = getXforCenteredText(text);
        textY += mg.getTileSize() * 2;
        g2.drawString(text, textX, textY);
        if (commandNum == 0) {

            g2.drawString(">", textX - 25, textY);

            if (mg.keyH.enterPressed == true) {
                subState = 1;
                commandNum = 0;
            }
        }

        //RESET GAME
        text = "RESET";
        textX = getXforCenteredText(text);
        textY += mg.getTileSize() + 15;
        g2.drawString(text, textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
            
            if (mg.keyH.enterPressed == true) {
                subState = 3;
                commandNum = 0;
            }
        }

        //BACK TO MAIN MENU    
        text = "MAIN MENU";
        textX = getXforCenteredText(text);
        textY += mg.getTileSize() + 15;
        g2.drawString(text, textX, textY);
        if (commandNum == 2) {

            g2.drawString(">", textX - 25, textY);

            if (mg.keyH.enterPressed == true) {
                subState = 2;
                commandNum = 0;
            }
        }

        g2.setFont(g2.getFont().deriveFont(25F));
        //BACK   
        text = "BACK";
        textX = getXforCenteredText(text);
        textY += mg.getTileSize() * 3;
        g2.drawString(text, textX, textY + 15);
        if (commandNum == 3) {
            g2.drawString(">", textX - 25, textY + 15);
            

            if (mg.keyH.enterPressed == true) {
                mg.gameState = mg.playState;
                commandNum = 0;
            }
        }
        
    }

    public void option_control(int frameX, int frameY) {

        int textX;
        int textY;

        //TITLE
        String text = "CONTROL";
        textX = getXforCenteredText(text);
        textY = frameY + mg.getTileSize();
        g2.drawString(text, textX, textY);

        g2.setFont(g2.getFont().deriveFont(25F));
        textX = frameX + mg.getTileSize();
        textY += mg.getTileSize();
        g2.drawString("Move", textX, textY);
        textY += mg.getTileSize();
        g2.drawString("Pause", textX, textY);
        textY += mg.getTileSize();
        g2.drawString("Option", textX, textY);

        textX = frameX + mg.getTileSize() * 6;
        textY = frameY + mg.getTileSize() * 2;
        g2.drawString("WASD", textX, textY);
        textY += mg.getTileSize();
        g2.drawString("P", textX, textY);
        textY += mg.getTileSize();
        g2.drawString("ESC", textX, textY);

        text = "BACK";
        textX = getXforCenteredText(text);
        textY = (frameY + mg.getTileSize() * 9);
        g2.drawString(text, textX, textY - 3);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (mg.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 0;
            }
        }

    }

    public void option_backToMainConfirmation(int frameX, int frameY) {

        int textX = frameX + mg.getTileSize();
        int textY = frameY + mg.getTileSize() * 2;

        currentDialogue = "Return to Main Menu?";

        g2.setFont(g2.getFont().deriveFont(25F));
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        //YES
        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += mg.getTileSize();
        g2.drawString(text, textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (mg.keyH.enterPressed == true) {
                subState = 0;
                mg.gameState = mg.titleState;
            }
        }
        //NO
        text = "No";
        textX = getXforCenteredText(text);
        textY += mg.getTileSize();
        g2.drawString(text, textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
            if (mg.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 2;
            }
        }

    }

     public void option_resetConfirmation(int frameX, int frameY) {

        int textX = frameX + mg.getTileSize();
        int textY = frameY + mg.getTileSize() * 2;

        currentDialogue = "Want to Reset?";

        g2.setFont(g2.getFont().deriveFont(25F));
        for (String line : currentDialogue.split("\n")) {
            g2.drawString(line, textX, textY);
            textY += 40;
        }

        //YES
        String text = "Yes";
        textX = getXforCenteredText(text);
        textY += mg.getTileSize();
        g2.drawString(text, textX, textY);
        if (commandNum == 0) {
            g2.drawString(">", textX - 25, textY);
            if (mg.keyH.enterPressed == true) {
                subState = 0;
                mg.gameState = mg.playState;
                mg.retry();
                
                
                
            }
        }
        //NO
        text = "No";
        textX = getXforCenteredText(text);
        textY += mg.getTileSize();
        g2.drawString(text, textX, textY);
        if (commandNum == 1) {
            g2.drawString(">", textX - 25, textY);
            if (mg.keyH.enterPressed == true) {
                subState = 0;
                commandNum = 2;
            }
        }

    }
    
    public void drawSubWindow(int x, int y, int width, int height) {

        Color c = new Color(0, 0, 0, 210);
        g2.setColor(c);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        c = new Color(255, 255, 255);
        g2.setColor(c);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);

    }

    public int getXforCenteredText(String text) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();
        int x = (StartMaze.screenWidth / 2) - (length / 2);

        return x;
    }

}
