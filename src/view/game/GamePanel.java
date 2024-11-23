package view.game;

import controller.GameController;
import model.Direction;
import model.MapMatrix;
import view.FileMD5Util;
import view.login.User;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * It is the subclass of ListenerPanel, so that it should implement those four methods: do move left, up, down ,right.
 * The class contains a grids, which is the corresponding GUI view of the matrix variable in MapMatrix.
 */
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
    private int[] moveHero = new int[GRID_SIZE];
    private int[] moveBox = new int[GRID_SIZE];
    private int[] moveFragile = new int[GRID_SIZE];
    private int time;

    public GamePanel(MapMatrix model, GameFrame frame, User user, int step) {
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
                //Units digit maps to id attribute in GridComponent. (The no change value)
                grids[i][j] = new GridComponent(i, j, model.getId(i, j) % 10, this.GRID_SIZE);
                grids[i][j].setLocation(j * GRID_SIZE + 2, i * GRID_SIZE + 2);
                //Ten digit maps to Box or Hero in corresponding location in the GridComponent. (Changed value)
                switch (model.getId(i, j) / 10) {
                    case 1:
                        grids[i][j].setBoxInGrid(new Box(GRID_SIZE - 10, GRID_SIZE - 10));
                        break;
                    case 2:
                        this.hero = new Hero(GRID_SIZE - 16, GRID_SIZE - 16, i, j);
                        grids[i][j].setHeroInGrid(hero);
                        break;
                }
                if (model.getId (i,j) == 100) {
                    grids[i][j].setButtonInGrid(new Button(GRID_SIZE - 10, GRID_SIZE - 10));
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

    public JLabel getStepLabel() {
        return stepLabel;
    }

    public void setStepLabel(JLabel stepLabel) {
        this.stepLabel = stepLabel;
    }

    public void afterMove() {
        if (steps == 0 && frame.isMode()) {
            controller.getTimer().start();
        }
        this.steps++;
        if (getFrame().getLv() != 6){
            this.stepLabel.setText(String.format("Step: %d", this.steps));
        }
        if (!file.exists()) {
            MapInfo mapInfo = new MapInfo();
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
        if (controller.doWin(this.frame)) {
            return;
        }
        if (controller.doLose(this.frame)) {
            return;
        }
        if (!frame.isMode()) {
            autoSave();
        }
    }

    public void undoMove() {
        for (int i = 0; i <= this.steps; i++) {
            System.out.println(moveFragile[i]);
        }
        this.steps--;
        if (getFrame().getLv() != 6){
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
                if (moveFragile[this.steps + 1] == 1) {
                    controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] += 4;
                }
                targetGrid = getGridComponent(hero.getRow(), hero.getCol() + 1);
                targetGrid.setHeroInGrid(h);
                h.setRow(hero.getRow());
                h.setCol(hero.getCol() + 1);
            }
            case 2 -> {//撤回英雄右移
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] -= 20;
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 1] += 20;
                if (moveFragile[this.steps + 1] == 1) {
                    controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] += 4;
                }
                targetGrid = getGridComponent(hero.getRow(), hero.getCol() - 1);
                targetGrid.setHeroInGrid(h);
                h.setRow(hero.getRow());
                h.setCol(hero.getCol() - 1);
            }
            case 3 -> {//撤回英雄上移
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] -= 20;
                controller.getModel().getMatrix()[hero.getRow() + 1][hero.getCol()] += 20;
                if (moveFragile[this.steps + 1] == 1) {
                    controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] += 4;
                }
                targetGrid = getGridComponent(hero.getRow() + 1, hero.getCol());
                targetGrid.setHeroInGrid(h);
                h.setRow(hero.getRow() + 1);
                h.setCol(hero.getCol());
            }
            case 4 -> {//撤回英雄下移
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] -= 20;
                controller.getModel().getMatrix()[hero.getRow() - 1][hero.getCol()] += 20;
                if (moveFragile[this.steps + 1] == 1) {
                    controller.getModel().getMatrix()[hero.getRow()][hero.getCol()] += 4;
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
                doorCheck(hero.getRow(), hero.getCol() - 2);
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 2] -= 10;
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() - 1] += 10;
                doorCheck(hero.getRow(), hero.getCol() - 1);
                currentGrid.setBoxInGrid(b);
            }
            case 2 -> {//撤回箱子右移
                ttGrid = getGridComponent(hero.getRow(), hero.getCol() + 2);
                b = ttGrid.removeBoxFromGrid();
                doorCheck(hero.getRow(), hero.getCol() + 2);
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() + 2] -= 10;
                controller.getModel().getMatrix()[hero.getRow()][hero.getCol() + 1] += 10;
                doorCheck(hero.getRow(), hero.getCol() + 1);
                currentGrid.setBoxInGrid(b);
            }
            case 3 -> {//撤回箱子上移
                ttGrid = getGridComponent(hero.getRow() - 2, hero.getCol());
                b = ttGrid.removeBoxFromGrid();
                doorCheck(hero.getRow() - 2, hero.getCol());
                controller.getModel().getMatrix()[hero.getRow() - 2][hero.getCol()] -= 10;
                controller.getModel().getMatrix()[hero.getRow() - 1][hero.getCol()] += 10;
                doorCheck(hero.getRow() - 1, hero.getCol());
                currentGrid.setBoxInGrid(b);
            }
            case 4 -> {//撤回箱子下移
                ttGrid = getGridComponent(hero.getRow() + 2, hero.getCol());
                b = ttGrid.removeBoxFromGrid();
                doorCheck(hero.getRow() + 2, hero.getCol());
                controller.getModel().getMatrix()[hero.getRow() + 2][hero.getCol()] -= 10;
                controller.getModel().getMatrix()[hero.getRow() + 1][hero.getCol()] += 10;
                doorCheck(hero.getRow() + 1, hero.getCol());
                currentGrid.setBoxInGrid(b);
            }
        }
        moveBox[this.steps] = 0;
        autoSave();
    }

    private void autoSave() {
        try {
            if (FileMD5Util.compareMD5failed(FileMD5Util.loadMD5FromFile(new File(this.filepath + ".md5")), FileMD5Util.calculateMD5(new File(this.filepath)))) {
                System.out.println("存档文件损坏喵！");
                frame.getFileFrame().fixFile();
                JOptionPane.showMessageDialog(this, "存档文件损坏喵~已重置存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
            boolean result = FileFrame.updateMapById(0, controller.getModel(), this.steps, moveHero, moveBox, this.filepath);
            if (result) {
                System.out.println("更新成功");
            } else {
                System.out.println("更新失败");
            }
            FileMD5Util.saveMD5ToFile(FileMD5Util.calculateMD5(new File(this.filepath)), new File(filepath + ".md5"));
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

    public int[] getMoveFragile() {
        return moveFragile;
    }

    public void setMoveFragile(int[] moveFragile) {
        this.moveFragile = moveFragile;
    }


    private void doorCheck(int tRow, int tCol) {
        if (controller.getModel().getMatrix()[tRow][tCol] / 10 == 11){
            for (int i = 0; i < controller.getModel().getMatrix().length; i++) {
                for (int j = 0; j < controller.getModel().getMatrix()[0].length; j++) {
                    if (controller.getModel().getMatrix()[i][j] == 3) {
                        controller.getModel().getMatrix()[i][j]++;
                    } else if (controller.getModel().getMatrix()[i][j] == 4) {
                        controller.getModel().getMatrix()[i][j]--;
                    }
                }
            }
        }
    }

}
