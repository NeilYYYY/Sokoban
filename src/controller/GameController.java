package controller;

import model.Direction;
import model.Level;
import model.MapMatrix;
import org.jetbrains.annotations.NotNull;
import view.game.*;
import view.game.Box;
import view.level.LevelFrame;
import view.login.User;
import view.music.Sound;

import javax.swing.*;
import java.util.ArrayList;

/**
 * It is a bridge to combine GamePanel(view) and MapMatrix(model) in one game.
 * You can design several methods about the game logic in this class.
 */
public class GameController {
    private final GamePanel view;
    private final MapMatrix model;
    private final int lv;
    private final Sound sound;
    private final int[] moveHero = new int[151];
    private final int[] moveBox = new int[151];
    private final int[] moveFragile = new int[151];
    private final User user;
    private LevelFrame levelFrame;
    private Timer timer;

    public GameController(@NotNull GamePanel view, @NotNull MapMatrix model, User user, int lv, Sound sound, LevelFrame levelFrame) {
        this.view = view;
        this.model = new MapMatrix(new int[model.getHeight()][model.getWidth()]);
        this.model.copyMatrix(model.getMatrix());
        this.user = user;
        this.lv = lv;
        this.sound = sound;
        this.levelFrame = levelFrame;
        view.setController(this);
        System.out.println(user);
        if (view.getFrame().isMode()) {
            timer = new Timer(1000, e -> {
                view.setTime(view.getTime() - 1);
                view.getFrame().getLeftTimeLabel().setText(String.format("Left time: %d", view.getTime()));
                if (view.getTime() == 0) {
                    ((Timer) e.getSource()).stop();
                    doLose(view.getFrame());
                }
            });
        } else {
            timer = new Timer(1000, _ -> {
                view.setTime(view.getTime() + 1);
                view.getFrame().getTimeLabel().setText(String.format("Time: %d", view.getTime()));
            });
        }
    }

    public MapMatrix getModel() {
        return model;
    }

    public void restartGame() {
        System.out.println("Do restart game here");
        for (int i = 0; i < view.getGrids().length; i++) {
            for (int j = 0; j < view.getGrids()[i].length; j++) {
                switch (model.getId(i, j) % 10) {
                    case 3 -> view.getGrids()[i][j].removeClosedDoorFromGrid();
                    case 4 -> view.getGrids()[i][j].removeOpenDoorFromGrid();
                }
                switch (model.getId(i, j) % 100 / 10) {
                    case 1 -> view.getGrids()[i][j].removeBoxFromGrid();
                    case 2 -> view.getGrids()[i][j].removeHeroFromGrid();
                }
            }
        }
        if (view.getFrame().getLv() == 6) {
            if (model.getId(4, 6) == 1) {
                view.getGrids()[4][6].removeFragileFromGrid();
            }
            if (model.getId(4, 7) == 1) {
                view.getGrids()[4][7].removeFragileFromGrid();
            }
        }
        switch (this.lv) {
            case 1 -> model.copyMatrix(Level.LEVEL_1.getMap());
            case 2 -> model.copyMatrix(Level.LEVEL_2.getMap());
            case 3 -> model.copyMatrix(Level.LEVEL_3.getMap());
            case 4 -> model.copyMatrix(Level.LEVEL_4.getMap());
            case 5 -> model.copyMatrix(Level.LEVEL_5.getMap());
            case 6 -> model.copyMatrix(Level.LEVEL_6.getMap());
        }
        for (int i = 0; i < view.getGrids().length; i++) {
            for (int j = 0; j < view.getGrids()[i].length; j++) {
                switch (model.getId(i, j) % 10) {
                    case 3 ->
                            view.getGrids()[i][j].setClosedDoorInGrid(new ClosedDoor(view.getGRID_SIZE() - 10, view.getGRID_SIZE() - 10));
                    case 4 ->
                            view.getGrids()[i][j].setOpenDoorInGrid(new OpenDoor(view.getGRID_SIZE() - 10, view.getGRID_SIZE() - 10));
                }
                switch (model.getId(i, j) % 100 / 10) {
                    case 1 ->
                            view.getGrids()[i][j].setBoxInGrid(new Box(view.getGRID_SIZE() - 10, view.getGRID_SIZE() - 10, user));
                    case 2 -> {
                        view.getGrids()[i][j].setHeroInGrid(view.getHero());
                        view.getHero().setRow(i);
                        view.getHero().setCol(j);
                    }
                }
            }
        }
        view.setSteps(0);
        if (view.getFrame().getLv() != 6) {
            view.getStepLabel().setText(String.format("Step: %d", view.getSteps()));
        }
        if (view.getFrame().isMode()) {
            view.setTime(view.getFrame().getTime());
            view.getFrame().getLeftTimeLabel().setText(String.format("Left time: %d", view.getTime()));
            timer.stop();
            timer = new Timer(1000, e -> {
                view.setTime(view.getTime() - 1);
                view.getFrame().getLeftTimeLabel().setText(String.format("Left time: %d", view.getTime()));
                if (view.getTime() == 0) {
                    ((Timer) e.getSource()).stop();
                    doLose(view.getFrame());
                }
            });
        } else {
            view.setTime(0);
            view.getFrame().getTimeLabel().setText(String.format("Time: %d", view.getTime()));
            timer.stop();
            timer = new Timer(1000, _ -> {
                view.setTime(view.getTime() + 1);
                view.getFrame().getTimeLabel().setText(String.format("Time: %d", view.getTime()));
            });
        }
        view.repaint();
    }

    public boolean checkWin() {
        int[][] map = model.getMatrix();
        for (int[] ints : map) {
            for (int anInt : ints) {
                if (anInt % 100 / 10 == 1 && anInt % 10 != 2) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean doWin(GameFrame gameFrame) {
        if (checkWin()) {
            ArrayList<User> users = User.getUserList();
            this.user.lv()[0][this.lv - 1] = true;
            if (this.user.id() != 0) {
                users.get(this.user.id()).lv()[0][this.lv - 1] = true;
                User.writeUser(users);
            }
            System.out.println("You win!");
            Sound s = new Sound("src/misc/NV_Korogu_Man_Young_Normal00_HiddenKorok_Appear00.wav");
            s.setVolume(1.0);
            s.play();
            timer.stop();
            if (gameFrame.getLv() == 6) {//最后一关则退出
                JOptionPane.showMessageDialog(null, "Congratulations!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                this.view.getFrame().getSound().changeSource(this.view.getFrame().getMusicPath());
                this.view.getFrame().getSound().play();
                this.levelFrame = new LevelFrame(this.user, this.sound, this.view.getFrame().isMode(), true);
                this.levelFrame.setVisible(true);
                gameFrame.dispose();
                return true;
            } else {
                if (gameFrame.getGamePanel().getSteps() == gameFrame.getLeastStep()[this.lv - 1]) {
                    this.user.lv()[1][this.lv - 1] = true;
                    if (this.user.id() != 0) {
                        users.get(this.user.id()).lv()[1][this.lv - 1] = true;
                        User.writeUser(users);
                    }
                }
                if (gameFrame.isMode()) {
                    this.user.lv()[2][this.lv - 1] = true;
                    if (this.user.id() != 0) {
                        users.get(this.user.id()).lv()[2][this.lv - 1] = true;
                        User.writeUser(users);
                    }
                }
            }
            if (gameFrame.getLv() == 5) {
                JOptionPane.showMessageDialog(null, "Congratulations!", "SUCCESS", JOptionPane.INFORMATION_MESSAGE);
                this.levelFrame = new LevelFrame(this.user, this.sound, this.view.getFrame().isMode(), true);
                this.levelFrame.setVisible(true);
                gameFrame.dispose();
                return true;
            }
            int option = JOptionPane.showOptionDialog(null, "You Win!", "SUCCESS", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{"Back", "Next"}, "Next");
            if (option == 1) {
                GameFrame newGameFrame = getNewGameFrame(gameFrame);
                newGameFrame.setVisible(true);
            } else {
                this.levelFrame = new LevelFrame(this.user, this.sound, this.view.getFrame().isMode(), false);
                this.levelFrame.setVisible(true);
            }
            gameFrame.dispose();
            return true;
        }
        return false;
    }

    private @NotNull GameFrame getNewGameFrame(@NotNull GameFrame gameFrame) {
        MapMatrix mapMatrix = new MapMatrix(Level.values()[gameFrame.getLv()].getMap());
        GameFrame newGameFrame;
        if (gameFrame.getLv() == 6) {
            newGameFrame = new GameFrame(900, 600, mapMatrix, this.user, this.lv + 1, 0, this.sound, gameFrame.isMode(), view.getFrame().getTime(), this.levelFrame);
        } else {
            newGameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv + 1, 0, this.sound, gameFrame.isMode(), view.getFrame().getTime(), this.levelFrame);
        }
        return newGameFrame;
    }

    public boolean checkLose() {
        if (view.getSteps() >= 100) {
            timer.stop();
            return true;
        }
        int[][] map = model.getMatrix();
        for (int[] ints : map) {
            for (int j = 0; j < map[0].length; j++) {
                if (ints[j] == 13) {
                    timer.stop();
                    return true;
                }
            }
        }
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] % 100 / 10 == 1) {
                    if (checkVertical(i, j) || checkHorizontal(i, j)) {
                        return false;
                    }
                }
            }
        }
        timer.stop();
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
            } else {
                if (this.lv == 6) {
                    this.levelFrame = new LevelFrame(this.user, this.sound, this.view.getFrame().isMode(), true);
                } else {
                    this.levelFrame = new LevelFrame(this.user, this.sound, this.view.getFrame().isMode(), false);
                }
                this.levelFrame.setVisible(true);
                gameFrame.dispose();
            }
            return true;
        }
        return false;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public boolean doMove(int row, int col, @NotNull Direction direction) {
        GridComponent currentGrid = view.getGridComponent(row, col);
        //target row can column.
        int tRow = row + direction.getRow();
        int tCol = col + direction.getCol();
        int ttRow = tRow + direction.getRow();
        int ttCol = tCol + direction.getCol();
        GridComponent targetGrid = view.getGridComponent(tRow, tCol);
        int[][] map = model.getMatrix();
        if (map[tRow][tCol] == 0 || map[tRow][tCol] == 2 || map[tRow][tCol] == 4 || map[tRow][tCol] == 100 || map[tRow][tCol] == 5) {
            //update hero in MapMatrix
            if (model.getMatrix()[row][col] == 25) {
                moveFragile[view.getSteps()] = 1;
                System.out.println("Fragile");
            } else {
                moveFragile[view.getSteps()] = 0;
            }
            model.getMatrix()[row][col] -= 20;
            model.getMatrix()[tRow][tCol] += 20;
            //Update hero in GamePanel
            Hero h = currentGrid.removeHeroFromGrid();
            targetGrid.setHeroInGrid(h);
            //Update the row and column attribute in hero
            moveHeroBack(direction, tRow, tCol, h);
            moveBox[view.getSteps()] = 0;
            if (model.getMatrix()[row][col] == 5) {
                model.getMatrix()[row][col] = 1;
                view.setMoveFragile(moveFragile);
                view.getGrids()[row][col].setFragileInGrid(new Fragile(view.getGRID_SIZE() - 10, view.getGRID_SIZE() - 10));
            }
            return true;
        }
        if (map[tRow][tCol] == 10 || map[tRow][tCol] == 12 || map[tRow][tCol] == 14 || map[tRow][tCol] == 110 || map[tRow][tCol] == 15) {
            GridComponent ttGrid = view.getGridComponent(ttRow, ttCol);
            if (map[ttRow][ttCol] % 100 / 10 == 1 || map[ttRow][ttCol] == 1 || map[ttRow][ttCol] == 3) {
                return false;
            }
            doorCheck(tRow, tCol);
            if (model.getMatrix()[row][col] == 25) {
                moveFragile[view.getSteps()] = 1;
                view.setMoveFragile(moveFragile);
                System.out.println("Fragile");
            } else {
                moveFragile[view.getSteps()] = 0;
            }
            model.getMatrix()[row][col] -= 20;
            model.getMatrix()[tRow][tCol] += 10;
            model.getMatrix()[ttRow][ttCol] += 10;
            Box b = targetGrid.removeBoxFromGrid();
            Hero h = currentGrid.removeHeroFromGrid();
            targetGrid.setHeroInGrid(h);
            ttGrid.setBoxInGrid(b);
            moveHeroBack(direction, tRow, tCol, h);
            if (model.getMatrix()[row][col] == 5) {
                model.getMatrix()[row][col] = 1;
                view.setMoveFragile(moveFragile);
                view.getGrids()[row][col].setFragileInGrid(new Fragile(view.getGRID_SIZE() - 10, view.getGRID_SIZE() - 10));
            }
            doorCheck(ttRow, ttCol);
            switch (direction) {
                case UP -> {
                    moveBox[view.getSteps()] = 3;
                    view.setMoveBox(moveBox);
                }
                case DOWN -> {
                    moveBox[view.getSteps()] = 4;
                    view.setMoveBox(moveBox);
                }
                case LEFT -> {
                    moveBox[view.getSteps()] = 1;
                    view.setMoveBox(moveBox);
                }
                case RIGHT -> {
                    moveBox[view.getSteps()] = 2;
                    view.setMoveBox(moveBox);
                }
            }
            return true;
        }
        return false;
    }

    private void doorCheck(int tRow, int tCol) {
        if (model.getMatrix()[tRow][tCol] / 10 == 11) {
            for (int i = 0; i < model.getMatrix().length; i++) {
                for (int j = 0; j < model.getMatrix()[0].length; j++) {
                    if (model.getMatrix()[i][j] % 10 == 3) {
                        model.getMatrix()[i][j]++;
                        view.getGrids()[i][j].removeClosedDoorFromGrid();
                        view.getGrids()[i][j].setOpenDoorInGrid(new OpenDoor(view.getGRID_SIZE() - 10, view.getGRID_SIZE() - 10));
                    } else if (model.getMatrix()[i][j] % 10 == 4) {
                        model.getMatrix()[i][j]--;
                        view.getGrids()[i][j].removeOpenDoorFromGrid();
                        view.getGrids()[i][j].setClosedDoorInGrid(new ClosedDoor(view.getGRID_SIZE() - 10, view.getGRID_SIZE() - 10));
                    }
                }
            }
        }
    }

    private void moveHeroBack(@NotNull Direction direction, int tRow, int tCol, @NotNull Hero h) {
        h.setRow(tRow);
        h.setCol(tCol);
        switch (direction) {
            case UP -> {
                moveHero[view.getSteps()] = 3;
                view.setMoveHero(moveHero);
            }
            case DOWN -> {
                moveHero[view.getSteps()] = 4;
                view.setMoveHero(moveHero);
            }
            case LEFT -> {
                moveHero[view.getSteps()] = 1;
                view.setMoveHero(moveHero);
            }
            case RIGHT -> {
                moveHero[view.getSteps()] = 2;
                view.setMoveHero(moveHero);
            }
        }
    }

    public boolean checkVertical(int x, int y) {
        int[][] map = model.getMatrix();
        return map[x][y - 1] != 1 && map[x][y - 1] / 10 != 1 && map[x][y - 1] != 3 && map[x][y + 1] != 1 && map[x][y + 1] / 10 != 1 && map[x][y + 1] != 3;
    }

    public boolean checkHorizontal(int x, int y) {
        int[][] map = model.getMatrix();
        return map[x - 1][y] != 1 && map[x - 1][y] / 10 != 1 && map[x - 1][y] != 3 && map[x + 1][y] != 1 && map[x + 1][y] / 10 != 1 && map[x + 1][y] != 3;
    }
}
