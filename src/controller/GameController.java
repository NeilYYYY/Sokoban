package controller;

import model.Direction;
import model.MapMatrix;
import view.game.*;
import view.game.Box;
import view.level.LevelFrame;
import view.login.User;

import javax.swing.*;
import java.awt.*;

/**
 * It is a bridge to combine GamePanel(view) and MapMatrix(model) in one game.
 * You can design several methods about the game logic in this class.
 */
public class GameController {
    private final GamePanel view;
    private final MapMatrix model;
//    private SoundPlayerUtil soundPlayer;
    int[][] origin_map = new int[10001][10001];
    private User user;
    private int lv;

    public GameController(GamePanel view, MapMatrix model, User user, int lv) {
        this.view = view;
        this.model = model;
        this.user = user;
        this.lv = lv;
        for (int x = 0; x < model.getHeight(); x++) {
            for (int y = 0; y < model.getWidth(); y++) {
                origin_map[x][y] = model.getMatrix()[x][y];
            }
        }
        view.setController(this);
        System.out.println(user);
    }

    //TODO RESTART
    public void restartGame() {
        view.getFrame().setVisible(false);
        System.out.println("Do restart game here");
        for (int x = 0; x < model.getHeight(); x++) {
            for (int y = 0; y < model.getWidth(); y++) {
                model.getMatrix()[x][y] = origin_map[x][y];
            }
        }
        GameFrame gameFrame = new GameFrame(800, 450, model, user, lv);
        gameFrame.setVisible(true);
    }

    public boolean checkWin() {
        int[][] map = model.getMatrix();
        for (int[] ints : map) {
            for (int anInt : ints) {
                if (anInt == 10) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean checkLose() {
        int[][] map = model.getMatrix();
        boolean flag = true;
        for (int i = 0;i < map.length; i++) {
            for (int j = 0;j < map[i].length; j++) {
                if (map[i][j] / 10 == 1) {
                    if (!(((map[i - 1][j] == 1 || map[i - 1][j] / 10 == 1) && (map[i][j - 1] == 1 || map[i][j - 1] / 10 == 1)) || ((map[i - 1][j] == 1 || map[i - 1][j] / 10 == 1) && (map[i][j + 1] == 1 || map[i][j + 1] / 10 == 1)) || ((map[i + 1][j] == 1 || map[i + 1][j] / 10 == 1) && (map[i][j - 1] == 1 || map[i][j - 1] / 10 == 1)) || ((map[i + 1][j] == 1 || map[i + 1][j] / 10 == 1) && (map[i][j + 1] == 1 || map[i][j + 1] / 10 == 1)))) {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }

    public boolean doMove(int row, int col, Direction direction) {
        GridComponent currentGrid = view.getGridComponent(row, col);
        //target row can column.
        int tRow = row + direction.getRow();
        int tCol = col + direction.getCol();
        int ttRow = tRow + direction.getRow();
        int ttCol = tCol + direction.getCol();
        GridComponent targetGrid = view.getGridComponent(tRow, tCol);
        int[][] map = model.getMatrix();
        if (map[tRow][tCol] == 0 || map[tRow][tCol] == 2) {
            //update hero in MapMatrix
            model.getMatrix()[row][col] -= 20;
            model.getMatrix()[tRow][tCol] += 20;
            //Update hero in GamePanel
            Hero h = currentGrid.removeHeroFromGrid();
            targetGrid.setHeroInGrid(h);
            //Update the row and column attribute in hero
            h.setRow(tRow);
            h.setCol(tCol);
            return true;
        }
        if (map[tRow][tCol] == 10 || map[tRow][tCol] == 12) {
            GridComponent ttGrid = view.getGridComponent(ttRow, ttCol);
            if (map[ttRow][ttCol] / 10 == 1 || map[ttRow][ttCol] == 1) {
                return false;
            }
            model.getMatrix()[row][col] -= 20;
            model.getMatrix()[tRow][tCol] += 10;
            model.getMatrix()[ttRow][ttCol] += 10;
            Box b = targetGrid.removeBoxFromGrid();
            Hero h = currentGrid.removeHeroFromGrid();
            targetGrid.setHeroInGrid(h);
            ttGrid.setBoxInGrid(b);
            h.setRow(tRow);
            h.setCol(tCol);
            return true;
        }
        return false;
    }

//    public void playSound(){
//        SoundPlayerUtil.flag = true;
//        soundPlayer = new SoundPlayerUtil("src\\misc\\music1.wav");
//        soundPlayer.playSound();
//    }
    /*
    public void stopSound(){
        soundPlayer.stopSound();
    }

     */


    //todo: add other methods such as loadGame, saveGame...

}
