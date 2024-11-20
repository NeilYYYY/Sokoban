package view.game;

import controller.GameController;
import model.Direction;
import model.MapMatrix;
import view.login.User;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

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
    private GameController controller;
    private JLabel stepLabel;
    private int steps;
    private Hero hero;

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
        initialGame(step);
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
        this.steps++;
        this.stepLabel.setText(String.format("Step: %d", this.steps));
        if (controller.doWin(this.frame)) {
            return;
        }
        if (controller.doLose(this.frame)) {
            return;
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
                e.printStackTrace();
            }
        }
        try {
            boolean result = FileFrame.updateMapById(0, controller.getModel(), this.steps, this.filepath);
            if (result) {
                System.out.println("更新成功");
            } else {
                System.out.println("更新失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
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
}
