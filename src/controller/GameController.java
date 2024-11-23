package controller;

import model.Direction;
import model.Level;
import model.MapMatrix;
import view.game.*;
import view.game.Box;
import view.level.LevelFrame;
import view.login.User;
import view.music.Sound;

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
    private final Sound sound;
    private final int[] moveHero = new int[101];
    private final int[] moveBox = new int[101];

    public GameController(GamePanel view, MapMatrix model, User user, int lv, Sound sound) {
        this.view = view;
        this.model = new MapMatrix(new int[model.getHeight()][model.getWidth()]);
        this.model.copyMatrix(model.getMatrix());
        this.user = user;
        this.lv = lv;
        this.sound = sound;
        view.setController(this);
        System.out.println(user);
        if (view.getFrame().isMode()) {
            Timer timer = new Timer(1000, e -> {
                view.setTime(view.getTime() - 1);
                view.getFrame().getTimeLabel().setText(String.format("Left time: %d", view.getTime()));
                if (view.getTime() == 0) {
                    // 倒计时结束，执行相应操作
                    ((Timer) e.getSource()).stop();
                    doLose(view.getFrame());
                }
            });
            timer.start();
        }
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
        view.setTime(view.getFrame().getTime());
        view.getFrame().getTimeLabel().setText(String.format("Left time: %d", view.getTime()));
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
        if (view.getFrame().isMode()) {
            Timer timer = new Timer(1000, e -> {
                view.setTime(view.getTime() - 1);
                view.getFrame().getTimeLabel().setText(String.format("Left time: %d", view.getTime()));
                if (view.getTime() == 0) {
                    // 倒计时结束，执行相应操作
                    ((Timer) e.getSource()).stop();
                    doLose(view.getFrame());
                }
            });
            timer.start();
        }
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
            Sound s = new Sound("src/misc/NV_Korogu_Man_Young_Normal00_HiddenKorok_Appear00.wav");
            s.setVolume(1.0);
            s.play();
            if (gameFrame.getLv() == 5) {//最后一关则退出
                JOptionPane.showMessageDialog(null, "You Win!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                LevelFrame levelFrame = new LevelFrame(user, this.sound, gameFrame.isMode());
                levelFrame.setVisible(true);
                gameFrame.dispose();
                return true;
            }
            int option = JOptionPane.showOptionDialog(null, "You Win!", "SUCCESS", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Back", "Next"}, "Next");
            // 根据用户选择打开不同的 JFrame
            if (option == 1) {
                MapMatrix mapMatrix = new MapMatrix(Level.values()[gameFrame.getLv()].getMap());
                GameFrame gameFrame1 = new GameFrame(800, 450, mapMatrix, this.user, this.lv + 1, 0, this.sound, gameFrame.isMode(), 0);
                gameFrame1.setVisible(true);
                gameFrame.dispose();
                return true;
            } else if (option == 0) {
                LevelFrame levelFrame = new LevelFrame(user, this.sound, gameFrame.isMode());
                levelFrame.setVisible(true);
                gameFrame.dispose();
                return true;
            }
        }
        return false;
    }

    public boolean checkLose() {
        if (view.getSteps() >= 100) {
            return true;
        }
        if (view.getTime() == 0) {
            return true;
        }
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
            Sound s = new Sound("src/misc/NV_Korogu_Man_Young_Bad00_Think00.wav");
            s.setVolume(1.0);
            s.play();
            if (this.view.getSteps() >= 100) {
                System.out.println("Too many steps! 雑魚～");
                JOptionPane.showMessageDialog(gameFrame, "Too many steps! 雑魚～", "FAILED", JOptionPane.INFORMATION_MESSAGE);
                gameFrame.getController().restartGame();
                return true;
            }
            System.out.println("You lose!");
            int option = JOptionPane.showOptionDialog(null, "Game Over!", "FAILED", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Back", "Restart"}, "Restart");
            // 根据用户选择打开不同的 JFrame
            if (option == 1) {
                gameFrame.getController().restartGame();
                return true;
            } else if (option == 0) {
                LevelFrame levelFrame = new LevelFrame(user, this.sound, gameFrame.isMode());
                levelFrame.setVisible(true);
                gameFrame.dispose();
                return true;
            }
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
            moveHeroBack(direction, tRow, tCol, h);
            moveBox[view.getSteps()] = 0;
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
            moveHeroBack(direction, tRow, tCol, h);
            switch (direction) {
                case UP:
                    moveBox[view.getSteps()] = 3;
                    view.setMoveBox(moveBox);
                    break;
                case DOWN:
                    moveBox[view.getSteps()] = 4;
                    view.setMoveBox(moveBox);
                    break;
                case LEFT:
                    moveBox[view.getSteps()] = 1;
                    view.setMoveBox(moveBox);
                    break;
                case RIGHT:
                    moveBox[view.getSteps()] = 2;
                    view.setMoveBox(moveBox);
                    break;
            }
            return true;
        }
        return false;
    }

    private void moveHeroBack(Direction direction, int tRow, int tCol, Hero h) {
        h.setRow(tRow);
        h.setCol(tCol);
        switch (direction) {
            case UP:
                moveHero[view.getSteps()] = 3;
                view.setMoveHero(moveHero);
                break;
            case DOWN:
                moveHero[view.getSteps()] = 4;
                view.setMoveHero(moveHero);
                break;
            case LEFT:
                moveHero[view.getSteps()] = 1;
                view.setMoveHero(moveHero);
                break;
            case RIGHT:
                moveHero[view.getSteps()] = 2;
                view.setMoveHero(moveHero);
                break;
        }
    }

    public boolean checkVertical(int x, int y) {
        int[][] map = model.getMatrix();
        return map[x][y - 1] != 1 && map[x][y - 1] / 10 != 1 && map[x][y + 1] != 1 && map[x][y + 1] / 10 != 1;
    }

    public boolean checkHorizontal(int x, int y) {
        int[][] map = model.getMatrix();
        return map[x - 1][y] != 1 && map[x - 1][y] / 10 != 1 && map[x + 1][y] != 1 && map[x + 1][y] / 10 != 1;
    }
}
