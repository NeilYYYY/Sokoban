package controller;

import model.Direction;
import model.Level;
import model.MapMatrix;
import view.game.*;
import view.game.Box;
import view.level.LevelFrame;
import view.login.User;

import javax.swing.*;

/**
 * It is a bridge to combine GamePanel(view) and MapMatrix(model) in one game.
 * You can design several methods about the game logic in this class.
 */
public class GameController {
    private final GamePanel view;
    private final MapMatrix model;
    private final User user;
    private final int lv;

    public GameController(GamePanel view, MapMatrix model, User user, int lv) {
        this.view = view;
        this.model = new MapMatrix(new int[model.getHeight()][model.getWidth()]);
        this.model.copyMatrix(model.getMatrix());
        this.user = user;
        this.lv = lv;
        view.setController(this);
        System.out.println(user);
    }

    public MapMatrix getModel() {
        return model;
    }

    public void restartGame() {
        System.out.println("Do restart game here");
        for (int i = 0; i < view.getGrids().length; i++) {
            for (int j = 0; j < view.getGrids()[i].length; j++) {
                switch (model.getId(i, j) / 10) {
                    case 1 -> view.getGrids()[i][j].removeBoxFromGrid();
                    case 2 -> view.getGrids()[i][j].removeHeroFromGrid();
                }
            }
        }
        switch (this.lv) {
            case 1 -> model.copyMatrix(Level.LEVEL_1.getMap());
            case 2 -> model.copyMatrix(Level.LEVEL_2.getMap());
            case 3 -> model.copyMatrix(Level.LEVEL_3.getMap());
            case 4 -> model.copyMatrix(Level.LEVEL_4.getMap());
            case 5 -> model.copyMatrix(Level.LEVEL_5.getMap());
        }
        for (int i = 0; i < view.getGrids().length; i++) {
            for (int j = 0; j < view.getGrids()[i].length; j++) {
                switch (model.getId(i, j) / 10) {
                    case 1 ->
                            view.getGrids()[i][j].setBoxInGrid(new Box(view.getGRID_SIZE() - 10, view.getGRID_SIZE() - 10));
                    case 2 -> {
                        view.getGrids()[i][j].setHeroInGrid(view.getHero());
                        view.getHero().setRow(i);
                        view.getHero().setCol(j);
                    }
                }
            }
        }
        view.setSteps(0);
        view.getStepLabel().setText(String.format("Step: %d", view.getSteps()));
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

    public boolean doWin(GameFrame gameFrame) {
        if (checkWin()) {
            System.out.println("You win!");
            JOptionPane.showMessageDialog(gameFrame, "You Win!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
            gameFrame.dispose();
            LevelFrame levelFrame = new LevelFrame(user);
            levelFrame.setVisible(true);
            levelFrame.getSound().start(true);
            gameFrame.getSound().stop();
            return true;
        }
        return false;
    }

    public boolean checkLose() {
        int[][] map = model.getMatrix();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] / 10 == 1) {
                    if (checkVertical(i, j) || checkHorizontal(i, j)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean doLose(GameFrame gameFrame) {
        if (checkLose()) {
            System.out.println("You lose!");
            JOptionPane.showMessageDialog(gameFrame, "Game Over !", "FAILED", JOptionPane.INFORMATION_MESSAGE);
            gameFrame.getController().restartGame();
            return true;
        }
        return false;
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

    public boolean checkVertical(int x, int y) {
        int[][] map = model.getMatrix();
        return map[x][y - 1] != 1 && map[x][y - 1] / 10 != 1 && map[x][y + 1] != 1 && map[x][y + 1] / 10 != 1;
    }

    public boolean checkHorizontal(int x, int y) {
        int[][] map = model.getMatrix();
        return map[x - 1][y] != 1 && map[x - 1][y] / 10 != 1 && map[x + 1][y] != 1 && map[x + 1][y] / 10 != 1;
    }
    //todo: add other methods such as loadGame, saveGame...
}
