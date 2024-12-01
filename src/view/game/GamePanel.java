package view.game;

import controller.GameController;
import model.Direction;
import model.MapMatrix;
import org.jetbrains.annotations.NotNull;
import view.FileMD5Util;
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
        this.filepath = String.format("src/saves/%d-%d.json", this.frame.getLv(), user.id());
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
                    case 1 -> grids[i][j].setBoxInGrid(new Box(GRID_SIZE - 10, GRID_SIZE - 10, frame.getUser()));
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
        System.out.println("Click VK_RIGHT");
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.RIGHT)) {
            this.afterMove();
        }
    }

    @Override
    public void doMoveLeft() {
        System.out.println("Click VK_LEFT");
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.LEFT)) {
            this.afterMove();
        }
    }

    @Override
    public void doMoveUp() {
        System.out.println("Click VK_Up");
        if (controller.doMove(hero.getRow(), hero.getCol(), Direction.UP)) {
            this.afterMove();
        }
    }

    @Override
    public void doMoveDown() {
        System.out.println("Click VK_DOWN");
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
        if (!file.exists()) {
            MapInfo mapInfo = new MapInfo();
            System.out.println(mapInfo.getId());
            mapInfo.setModel(model);
            try {
                FileFrame.createFile(filepath);
                for (int i = 0; i < 6; i++) {
                    MapInfo mapInfo2 = new MapInfo();
                    mapInfo2.setModel(null);
                    mapInfo2.setId(i);
                    mapInfo2.setStep(0);
                    FileFrame.addNewMap(mapInfo2, filepath);
                }
                System.out.println("创建新文件并保存");
            } catch (Exception e) {
                System.out.println("保存失败");
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
        GridComponent targetGrid;
        GridComponent ttGrid;
        Hero h = currentGrid.removeHeroFromGrid();
        switch (moveHero[this.steps]) {
            case 1 -> {//撤回英雄左移
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] -= 20;
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() + 1] += 20;
                if (moveFragile[this.steps] == 1) {
                    controller.getModel().getMatrix()[hero.getRow()][hero.getCol() + 1] += 4;
                    getGrids()[hero.getRow()][hero.getCol() + 1].removeFragileFromGrid();
                    System.out.println("Fragile back");
                    moveFragile[this.steps] = 0;
                }
                targetGrid = getGridComponent(hero.getRow(), hero.getCol() + 1);
                targetGrid.setHeroInGrid(h);
                h.setRow(hero.getRow());
                h.setCol(hero.getCol() + 1);
            }
            case 2 -> {//撤回英雄右移
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] -= 20;
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 1] += 20;
                if (moveFragile[this.steps] == 1) {
                    controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 1] += 4;
                    getGrids()[hero.getRow()][hero.getCol() - 1].removeFragileFromGrid();
                    System.out.println("Fragile back");
                    moveFragile[this.steps] = 0;
                }
                targetGrid = getGridComponent(hero.getRow(), hero.getCol() - 1);
                targetGrid.setHeroInGrid(h);
                h.setRow(hero.getRow());
                h.setCol(hero.getCol() - 1);
            }
            case 3 -> {//撤回英雄上移
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] -= 20;
                controller.getModel().getMatrix()[hero.getRow() + 1][hero.getCol()] += 20;
                if (moveFragile[this.steps] == 1) {
                    controller.getModel().getMatrix()[hero.getRow() + 1][hero.getCol()] += 4;
                    getGrids()[hero.getRow() + 1][hero.getCol()].removeFragileFromGrid();
                    System.out.println("Fragile back");
                    moveFragile[this.steps] = 0;
                }
                targetGrid = getGridComponent(hero.getRow() + 1, hero.getCol());
                targetGrid.setHeroInGrid(h);
                h.setRow(hero.getRow() + 1);
                h.setCol(hero.getCol());
            }
            case 4 -> {//撤回英雄下移
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] -= 20;
                controller.getModel().getMatrix()[hero.getRow() - 1][hero.getCol()] += 20;
                if (moveFragile[this.steps] == 1) {
                    controller.getModel().getMatrix()[hero.getRow() - 1][hero.getCol()] += 4;
                    getGrids()[hero.getRow() - 1][hero.getCol()].removeFragileFromGrid();
                    System.out.println("Fragile back");
                    moveFragile[this.steps] = 0;
                }
                targetGrid = getGridComponent(hero.getRow() - 1, hero.getCol());
                targetGrid.setHeroInGrid(h);
                h.setRow(hero.getRow() - 1);
                h.setCol(hero.getCol());
            }
        }
        moveHero[this.steps] = 0;
        Box b;
        switch (moveBox[this.steps]) {
            case 1 -> {//撤回箱子左移
                ttGrid = getGridComponent(hero.getRow(), hero.getCol() - 2);
                b = ttGrid.removeBoxFromGrid();
                controller.doorCheck(hero.getRow(), hero.getCol() - 2);
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 2] -= 10;
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 1] += 10;
                controller.doorCheck(hero.getRow(), hero.getCol() - 1);
                currentGrid.setBoxInGrid(b);
            }
            case 2 -> {//撤回箱子右移
                ttGrid = getGridComponent(hero.getRow(), hero.getCol() + 2);
                b = ttGrid.removeBoxFromGrid();
                controller.doorCheck(hero.getRow(), hero.getCol() + 2);
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() + 2] -= 10;
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() + 1] += 10;
                controller.doorCheck(hero.getRow(), hero.getCol() + 1);
                currentGrid.setBoxInGrid(b);
            }
            case 3 -> {//撤回箱子上移
                ttGrid = getGridComponent(hero.getRow() - 2, hero.getCol());
                b = ttGrid.removeBoxFromGrid();
                controller.doorCheck(hero.getRow() - 2, hero.getCol());
                controller.getModel().getMatrix()[hero.getRow() - 2][hero.getCol()] -= 10;
                controller.getModel().getMatrix()[hero.getRow() - 1][hero.getCol()] += 10;
                controller.doorCheck(hero.getRow() - 1, hero.getCol());
                currentGrid.setBoxInGrid(b);
            }
            case 4 -> {//撤回箱子下移
                ttGrid = getGridComponent(hero.getRow() + 2, hero.getCol());
                b = ttGrid.removeBoxFromGrid();
                controller.doorCheck(hero.getRow() + 2, hero.getCol());
                controller.getModel().getMatrix()[hero.getRow() + 2][hero.getCol()] -= 10;
                controller.getModel().getMatrix()[hero.getRow() + 1][hero.getCol()] += 10;
                controller.doorCheck(hero.getRow() + 1, hero.getCol());
                currentGrid.setBoxInGrid(b);
            }
        }
        moveBox[this.steps] = 0;
        if (!frame.isMode() || frame.getLv() != 6) {
            autoSave();
        }
    }

    private void autoSave() {
        try {
            if (FileMD5Util.compareMD5failed(FileMD5Util.loadMD5FromFile(new File(this.filepath + ".sha")), FileMD5Util.calculateMD5(new File(this.filepath)))) {
                System.out.println("存档文件损坏喵！");
                frame.getFileFrame().fixFile();
                JOptionPane.showMessageDialog(this, "存档文件损坏喵~已重置存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            boolean result = FileFrame.updateMapById(0, controller.getModel(), this.steps, moveHero, moveBox, this.time, this.filepath);
            if (result) {
                System.out.println("更新成功");
            } else {
                System.out.println("更新失败");
            }
            FileMD5Util.saveMD5ToFile(FileMD5Util.calculateMD5(new File(this.filepath)), new File(filepath + ".sha"));
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
