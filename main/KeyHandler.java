package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import entity.Player;
import tile.TileManager;

/**
 *
 * @author User
 */
public class KeyHandler implements KeyListener {

    Player player;
    TileManager TM;
    MazeGame mg;

    public boolean upPressed, downPressed, leftPressed, rightPressed, enterPressed;
    //for debug
    public boolean godMode = false;
    public boolean skipF = false;
    public boolean showDebugText = false;

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setTileManager(TileManager TM) {
        this.TM = TM;
    }

    public KeyHandler(MazeGame mg) {
        this.mg = mg;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        int k = e.getKeyCode();

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        //TITLE STATE
        if (mg.gameState == mg.titleState) {
            titleState(k);
        } //PLAY STATE
        else if (mg.gameState == mg.playState) {
            playState(k);
        } //PAUSE STATE
        else if (mg.gameState == mg.pauseState) {
            pauseState(k);
        } //CHARACTERS STATE
        else if (mg.gameState == mg.characterState) {
            characterState(k);
        } //OPTION STATE
        else if (mg.gameState == mg.optionState) {
            optionState(k);
        } //GAMEOVER STATE
        else if (mg.gameState == mg.gameOverStage) {
            gameOverStage(k);
        } //GAMECOMPLETE STATE
        else if (mg.gameState == mg.gameCompleteStage) {
            gameCompleteStage(k);
        }
    }

    //TITLE STATE//////////////////////////////////////////////////////
    public void titleState(int k) {

        if (mg.ui.titleScreenState == 0) {
            if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP) {
                mg.ui.commandNum--;
                if (mg.ui.commandNum < 0) {
                    mg.ui.commandNum = 1;
                }
            }
            if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN) {
                mg.ui.commandNum++;
                if (mg.ui.commandNum > 1) {
                    mg.ui.commandNum = 0;
                }
            }
            if (k == KeyEvent.VK_ENTER) {
                if (mg.ui.commandNum == 0) {
                    mg.ui.titleScreenState = 1;

                }
                if (mg.ui.commandNum == 2) {
                    //LOAD || IN PROCESS 10 YEAR T-T  Moodeng cry
                }
                if (mg.ui.commandNum == 1) {
                    System.exit(0);
                }
            }
        } //SELECT DIFFICULT LEVEL 
        else if (mg.ui.titleScreenState == 1) {
            if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP) {

                mg.ui.subCommandNum--;
                if (mg.ui.subCommandNum < 0) {
                    mg.ui.subCommandNum = 3;
                }
            }
            if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN) {
                mg.ui.subCommandNum++;
                if (mg.ui.subCommandNum > 3) {
                    mg.ui.subCommandNum = 0;
                }
            }
            if (k == KeyEvent.VK_ENTER) {
                if (mg.ui.subCommandNum == 0) {
                    // Easy mode
                    
                    mg.setDifficulty("easy");
                    mg.ui.commandNum = 0;
                    mg.ui.titleScreenState = 0;
                    player.setDefaultValues();
                    mg.gameState = mg.playState;

                }
                if (mg.ui.subCommandNum == 1) {
                    // Normal mode
                    mg.setDifficulty("normal");
                    mg.ui.commandNum = 0;
                    mg.ui.titleScreenState = 0;
                    player.setDefaultValues();
                    mg.gameState = mg.playState;

                }
                if (mg.ui.subCommandNum == 2) {
                    // Hard mode
                    mg.setDifficulty("hard");
                    mg.ui.commandNum = 0;
                    mg.ui.titleScreenState = 0;
                    player.setDefaultValues();
                    mg.gameState = mg.playState;

                }
                if (mg.ui.subCommandNum == 3) {
                    mg.ui.commandNum = 0;
                    mg.ui.titleScreenState = 0;

                }
            }
        }
    }

    //PLAY STATE////////////////////////////////////PLAY STATE///////////
    public void playState(int k) {
        if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP) {
            upPressed = true;
        }
        if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN) {
            downPressed = true;
        }
        if (k == KeyEvent.VK_A || k == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }
        if (k == KeyEvent.VK_D || k == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }
        if (k == KeyEvent.VK_I) {
            player.increaseVision();
            System.out.println("Vision radius increased to: " + player.getVisionRadius());
        }
        if (k == KeyEvent.VK_O) {
            player.decreaseVision();
            System.out.println("Vision radius decreased to: " + player.getVisionRadius());
        }
        if (k == KeyEvent.VK_P) {
            mg.gameState = mg.pauseState;
        }
        
        if (k == KeyEvent.VK_L) {
            if (godMode == false) {
                godMode = true;

            } else if (godMode == true) {
                godMode = false;
            }
        }
         if (k == KeyEvent.VK_N) {
            if (skipF == false) {
                skipF = true;

            } else if (skipF == true) {
                skipF = false;
            }
        }

        //OPTION
        if (k == KeyEvent.VK_ESCAPE) {
            mg.gameState = mg.optionState;
        }

        //Debug
        if (k == KeyEvent.VK_T) {
            if (showDebugText == false) {
                showDebugText = true;
            } else if (showDebugText == true) {
                showDebugText = false;
            }
        }
    }

    //PAUSE STATE////////////////////////////////////PAUSE STATE///////////
    public void pauseState(int k) {
        if (k == KeyEvent.VK_P) {
            mg.gameState = mg.playState;
        }
    }

    //CHARACTER STATE////////////////////////////////////OPTION STATE///////////
    public void characterState(int k) {
//        if(k == KeyEvent.VK_C){;
//            mg.gameState = mg.playState;
//        }
    }

    //OPTION STATE////////////////////////////////////OPTION STATE///////////
    public void optionState(int k) {

        if (k == KeyEvent.VK_ESCAPE) {
            mg.gameState = mg.playState;
        }
        if (k == KeyEvent.VK_ENTER) {
            enterPressed = true;
        }

        int maxCommandNum = 0;
        switch (mg.ui.subState) {
            case 0:
                maxCommandNum = 3;
                break;
            case 2:
                maxCommandNum = 1;
                break;
        }

        if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP) {
            mg.ui.commandNum--;
            if (mg.ui.commandNum < 0) {
                mg.ui.commandNum = maxCommandNum;
            }
        }
        if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN) {
            mg.ui.commandNum++;
            if (mg.ui.commandNum > maxCommandNum) {
                mg.ui.commandNum = 0;
            }
        }
    }

    public void gameOverStage(int k) {

        if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP) {
            mg.ui.commandNum--;
            if (mg.ui.commandNum < 0) {
                mg.ui.commandNum = 1;
            }
        }
        if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN) {
            mg.ui.commandNum++;
            if (mg.ui.commandNum > 1) {
                mg.ui.commandNum = 0;
            }
        }
        if (k == KeyEvent.VK_ENTER) {
            if (mg.ui.commandNum == 0) {
                mg.gameState = mg.playState;
                mg.retry();
            } else if (mg.ui.commandNum == 1) {
                mg.gameState = mg.titleState;
            }

        }
    }

    public void gameCompleteStage(int k) {
        if (k == KeyEvent.VK_W || k == KeyEvent.VK_UP) {
            mg.ui.commandNum--;
            if (mg.ui.commandNum < 0) {
                mg.ui.commandNum = 0;
            }
        }
        if (k == KeyEvent.VK_S || k == KeyEvent.VK_DOWN) {
            mg.ui.commandNum++;
            if (mg.ui.commandNum > 0) {
                mg.ui.commandNum = 0;
            }
        }
        if (k == KeyEvent.VK_ENTER) {
            if (mg.ui.commandNum == 0) {
                mg.gameState = mg.titleState;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e
    ) {
        int k = e.getKeyCode();

        if (k == KeyEvent.VK_W) {
            upPressed = false;

        }
        if (k == KeyEvent.VK_S) {
            downPressed = false;
        }
        if (k == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (k == KeyEvent.VK_D) {
            rightPressed = false;
        }
//        if (k == KeyEvent.VK_L && player != null) {
//            TM.tile[1].collision = true;
//            System.out.println("FLY MODE OFF!");
//        }
    }
}
