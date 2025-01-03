package view.game;

import controller.GameController;
import model.Direction;
import model.MapMatrix;
import org.jetbrains.annotations.NotNull;
import view.FileSHAUtil;
import view.login.User;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class GamePanel extends ListenerPanel {
    private final int GRID_SIZE = 50;
    private final GridComponent[][] grids;
    private final MapMatrix model;
    private final GameFrame frame;
    private final String filepath;
    private final File file;
    private final Logger log = Logger.getLogger(GamePanel.class.getName());
    private GameController controller;
    private JLabel stepLabel;
    private int steps;
    private Hero hero;
    private int[] moveHero = new int[150];
    private int[] moveBox = new int[150];
    private int[] moveFragile = new int[150];
    private int time;
    private boolean flag = false;

    public GamePanel(@NotNull MapMatrix model, @NotNull GameFrame frame, @NotNull User user, int step) {
        this.setVisible(true);
        this.setFocusable(true);
        this.setLayout(null);
        this.setSize(model.getWidth() * GRID_SIZE + 4, model.getHeight() * GRID_SIZE + 4);
        this.model = new MapMatrix(new int[model.getHeight()][model.getWidth()]);
        this.model.copyMatrix(model.getMatrix());
        this.frame = frame;
        this.grids = new GridComponent[model.getHeight()][model.getWidth()];
        this.filepath = String.format("saves/%d-%d.json", this.frame.getLv(), user.getId());
        this.file = new File(filepath);
        this.time = frame.getTime();
        initialGame(step);
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public GameFrame getFrame() {
        return frame;
    }

    public GridComponent[][] getGrids() {
        return grids;
    }

    public MapMatrix getModel() {
        return model;
    }

    public Hero getHero() {
        return hero;
    }

    public int getGRID_SIZE() {
        return GRID_SIZE;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void initialGame(int step) {
        this.steps = step;
        for (int i = 0; i < grids.length; i++) {
            for (int j = 0; j < grids[i].length; j++) {
                grids[i][j] = new GridComponent(model.getId(i, j), this.GRID_SIZE);
                grids[i][j].setLocation(j * GRID_SIZE + 2, i * GRID_SIZE + 2);
                switch (model.getId(i, j) / 10) {
                    case 1 -> grids[i][j].setBoxInGrid(new Box(GRID_SIZE - 10, GRID_SIZE - 10, frame.getUser(), model.getId(i, j)));
                    case 2 -> {
                        this.hero = new Hero(GRID_SIZE - 16, GRID_SIZE - 16, i, j, this.frame.getUser());
                        grids[i][j].setHeroInGrid(hero);
                    }
                }
                if (model.getId(i, j) % 10 == 3) {
                    grids[i][j].setClosedDoorInGrid(new ClosedDoor(GRID_SIZE - 10, GRID_SIZE - 10));
                }
                if (model.getId(i, j) % 10 == 4) {
                    grids[i][j].setOpenDoorInGrid(new OpenDoor(GRID_SIZE - 10, GRID_SIZE - 10));
                }
                this.add(grids[i][j]);
            }
        }
        this.repaint();
    }

    @Override
    public void doMoveRight() {
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.RIGHT)) {
            this.afterMove();
        }
    }

    @Override
    public void doMoveLeft() {
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.LEFT)) {
            this.afterMove();
        }
    }

    @Override
    public void doMoveUp() {
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.UP)) {
            this.afterMove();
        }
    }

    @Override
    public void doMoveDown() {
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.DOWN)) {
            this.afterMove();
        }
    }

    @Override
    public void doWin() {
        controller.doWin(this.frame, true);
    }

    @Override
    public void back() {
        frame.getBackBtn().doClick();
    }

    public JLabel getStepLabel() {
        return stepLabel;
    }

    public void setStepLabel(JLabel stepLabel) {
        this.stepLabel = stepLabel;
    }

    public void afterMove() {
        if (steps == 0 && frame.isMode()) {
            controller.getTimer().start();
        } else if (flag || steps == 0) {
            controller.getTimer().start();
            flag = false;
        }
        this.steps++;
        if (getFrame().getLv() != 6) {
            this.stepLabel.setText(String.format("Step: %d", this.steps));
        }
        if (!file.exists() && frame.getUser().getId() != 0 && frame.getLv() != 6) {
            try {
                FileFrame.createFile(filepath);
                for (int i = 0; i < 6; i++) {
                    MapInfo mapInfo = new MapInfo();
                    mapInfo.setModel(null);
                    mapInfo.setId(i);
                    mapInfo.setStep(0);
                    FileFrame.addNewMap(mapInfo, filepath);
                }
                System.out.println("创建新文件并保存喵");
            } catch (Exception e) {
                System.out.println("保存失败喵");
                log.info(e.getMessage());
            }
        }
        if (controller.doWin(this.frame, false)) {
            return;
        }
        if (controller.doLose(this.frame)) {
            return;
        }
        if (!frame.isMode() && frame.getLv() != 6) {
            autoSave();
        }
    }

    public void undoMove() {
        this.steps--;
        if (getFrame().getLv() != 6) {
            this.stepLabel.setText(String.format("Step: %d", this.steps));
        }
        GridComponent currentGrid = getGridComponent(hero.getRow(), hero.getCol());
        GridComponent originGrid;
        GridComponent boxGrid;
        Hero h = currentGrid.removeHeroFromGrid();
        switch (moveHero[this.steps]) {
            case 1 -> {//撤回英雄左移
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] -= 20;
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() + 1] += 20;
                if (moveFragile[this.steps] == 1) {
                    controller.getModel().getMatrix()[hero.getRow()][hero.getCol() + 1] += 4;
                    getGrids()[hero.getRow()][hero.getCol() + 1].removeFragileFromGrid();
                    moveFragile[this.steps] = 0;
                }
                originGrid = getGridComponent(hero.getRow(), hero.getCol() + 1);
                originGrid.setHeroInGrid(h);
                h.setRow(hero.getRow());
                h.setCol(hero.getCol() + 1);
                currentGrid.repaint();
                originGrid.repaint();
            }
            case 2 -> {//撤回英雄右移
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] -= 20;
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 1] += 20;
                if (moveFragile[this.steps] == 1) {
                    controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 1] += 4;
                    getGrids()[hero.getRow()][hero.getCol() - 1].removeFragileFromGrid();
                    moveFragile[this.steps] = 0;
                }
                originGrid = getGridComponent(hero.getRow(), hero.getCol() - 1);
                originGrid.setHeroInGrid(h);
                h.setRow(hero.getRow());
                h.setCol(hero.getCol() - 1);
                currentGrid.repaint();
                originGrid.repaint();
            }
            case 3 -> {//撤回英雄上移
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] -= 20;
                controller.getModel().getMatrix()[hero.getRow() + 1][hero.getCol()] += 20;
                if (moveFragile[this.steps] == 1) {
                    controller.getModel().getMatrix()[hero.getRow() + 1][hero.getCol()] += 4;
                    getGrids()[hero.getRow() + 1][hero.getCol()].removeFragileFromGrid();
                    moveFragile[this.steps] = 0;
                }
                originGrid = getGridComponent(hero.getRow() + 1, hero.getCol());
                originGrid.setHeroInGrid(h);
                h.setRow(hero.getRow() + 1);
                h.setCol(hero.getCol());
                currentGrid.repaint();
                originGrid.repaint();
            }
            case 4 -> {//撤回英雄下移
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] -= 20;
                controller.getModel().getMatrix()[hero.getRow() - 1][hero.getCol()] += 20;
                if (moveFragile[this.steps] == 1) {
                    controller.getModel().getMatrix()[hero.getRow() - 1][hero.getCol()] += 4;
                    getGrids()[hero.getRow() - 1][hero.getCol()].removeFragileFromGrid();
                    moveFragile[this.steps] = 0;
                }
                originGrid = getGridComponent(hero.getRow() - 1, hero.getCol());
                originGrid.setHeroInGrid(h);
                h.setRow(hero.getRow() - 1);
                h.setCol(hero.getCol());
                currentGrid.repaint();
                originGrid.repaint();
            }
        }
        moveHero[this.steps] = 0;
        Box b;
        switch (moveBox[this.steps]) {
            case 1 -> {//撤回箱子左移
                boxGrid = getGridComponent(hero.getRow(), hero.getCol() - 2);
                boxGrid.removeBoxFromGrid();
                controller.doorCheck(hero.getRow(), hero.getCol() - 2);
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 2] -= 10;
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 1] += 10;
                controller.doorCheck(hero.getRow(), hero.getCol() - 1);
                b = new Box(getGRID_SIZE() - 10, getGRID_SIZE() - 10, frame.getUser(), controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 1]);
                currentGrid.setBoxInGrid(b);
                boxGrid.repaint();
                currentGrid.repaint();
            }
            case 2 -> {//撤回箱子右移
                boxGrid = getGridComponent(hero.getRow(), hero.getCol() + 2);
                boxGrid.removeBoxFromGrid();
                controller.doorCheck(hero.getRow(), hero.getCol() + 2);
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() + 2] -= 10;
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() + 1] += 10;
                controller.doorCheck(hero.getRow(), hero.getCol() + 1);
                b = new Box(getGRID_SIZE() - 10, getGRID_SIZE() - 10, frame.getUser(), controller.getModel().getMatrix()[hero.getRow()][hero.getCol() + 1]);
                currentGrid.setBoxInGrid(b);
                boxGrid.repaint();
                currentGrid.repaint();
            }
            case 3 -> {//撤回箱子上移
                boxGrid = getGridComponent(hero.getRow() - 2, hero.getCol());
                boxGrid.removeBoxFromGrid();
                controller.doorCheck(hero.getRow() - 2, hero.getCol());
                controller.getModel().getMatrix()[hero.getRow() - 2][hero.getCol()] -= 10;
                controller.getModel().getMatrix()[hero.getRow() - 1][hero.getCol()] += 10;
                controller.doorCheck(hero.getRow() - 1, hero.getCol());
                b = new Box(getGRID_SIZE() - 10, getGRID_SIZE() - 10, frame.getUser(), controller.getModel().getMatrix()[hero.getRow() - 1][hero.getCol()]);
                currentGrid.setBoxInGrid(b);
                boxGrid.repaint();
                currentGrid.repaint();
            }
            case 4 -> {//撤回箱子下移
                boxGrid = getGridComponent(hero.getRow() + 2, hero.getCol());
                boxGrid.removeBoxFromGrid();
                controller.doorCheck(hero.getRow() + 2, hero.getCol());
                controller.getModel().getMatrix()[hero.getRow() + 2][hero.getCol()] -= 10;
                controller.getModel().getMatrix()[hero.getRow() + 1][hero.getCol()] += 10;
                controller.doorCheck(hero.getRow() + 1, hero.getCol());
                b = new Box(getGRID_SIZE() - 10, getGRID_SIZE() - 10, frame.getUser(), controller.getModel().getMatrix()[hero.getRow() + 1][hero.getCol()]);
                currentGrid.setBoxInGrid(b);
                boxGrid.repaint();
                currentGrid.repaint();
            }
        }
        moveBox[this.steps] = 0;
        if (!frame.isMode() && frame.getLv() != 6) {
            autoSave();
        }
    }

    private void autoSave() {
        if (frame.getUser().getId() == 0) {
            return;
        }
        try {
            if (frame.getFileFrame().checkFileFailed()) {
                System.out.println("存档文件损坏喵！");
                frame.getFileFrame().fixFile();
                JOptionPane.showMessageDialog(this.frame, "存档文件损坏，已重置存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            boolean result = FileFrame.updateMapById(this.filepath, 0, controller.getModel(), this.steps, this.time, moveHero, moveBox);
            if (!result) {
                System.out.println("自动保存失败喵。。。");
            }
            FileSHAUtil.saveSHAToFile(FileSHAUtil.calculateSHA(new File(this.filepath)), new File(filepath + ".sha"));
        } catch (IOException e) {
            log.info(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setController(GameController controller) {
        this.controller = controller;
    }

    public GridComponent getGridComponent(int row, int col) {
        return grids[row][col];
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int[] getMoveHero() {
        return moveHero;
    }

    public void setMoveHero(int[] moveHero) {
        this.moveHero = moveHero;
    }

    public int[] getMoveBox() {
        return moveBox;
    }

    public void setMoveBox(int[] moveBox) {
        this.moveBox = moveBox;
    }

    public void setMoveFragile(int[] moveFragile) {
        this.moveFragile = moveFragile;
    }
}
