package controller;

import model.Direction;
import model.MapMatrix;
import view.game.*;
import view.game.Box;
import view.level.LevelFrame;

import javax.swing.*;
import java.awt.*;

/**
 * It is a bridge to combine GamePanel(view) and MapMatrix(model) in one game.
 * You can design several methods about the game logic in this class.
 */
public class GameController {
    private final GamePanel view;
    private final MapMatrix model;

    public GameController(GamePanel view, MapMatrix model) {
        this.view = view;
        this.model = model;
        view.setController(this);
    }
    //TODO RESTART
    public void restartGame() {
        System.out.println("Do restart game here");
        int temp = 0;
        if (model.getMatrix()[0].length == 6){
            MapMatrix mapMatrix1 = new MapMatrix(new int[][]{
                    {1, 1, 1, 1, 1, 1},
                    {1, 20, 0, 0, 0, 1},
                    {1, 0, 0, 10, 2, 1},
                    {1, 0, 2, 10, 0, 1},
                    {1, 1, 1, 1, 1, 1},
            });
            GameFrame gameFrame1 = new GameFrame(600, 450, mapMatrix1);
            gameFrame1.setVisible(true);
        }//6格的房间检测代表level1
        else if (model.getMatrix()[0].length == 8){
            MapMatrix mapMatrix5 = new MapMatrix(new int[][]{
                    {1, 1, 1, 1, 1, 1, 0, 0},
                    {1, 0, 0, 0, 0, 1, 1, 1},
                    {1, 0, 0, 0, 2, 2, 0, 1},
                    {1, 0, 10, 10, 10, 20, 0, 1},
                    {1, 0, 0, 1, 0, 2, 0, 1},
                    {1, 1, 1, 1, 1, 1, 1, 1},
            });
            GameFrame gameFrame5 = new GameFrame(600, 450, mapMatrix5);
            gameFrame5.setVisible(true);
        }//8格的房间检测代表level5
        else {
            for (int i = 0; i < model.getMatrix().length; i++) {
                for (int j = 0; j < model.getMatrix()[i].length; j++) {
                    if (model.getMatrix()[i][j] == 1){
                        temp ++;
                    }
                }
            }//计算墙体数量
            switch (temp){
                case 24://检测代表level2
                    MapMatrix mapMatrix2 = new MapMatrix(new int[][]{
                            {1, 1, 1, 1, 1, 1, 0},
                            {1, 20, 0, 0, 0, 1, 1},
                            {1, 0, 10, 10, 0, 1, 1},
                            {1, 0, 1, 2, 0, 2, 1},
                            {1, 0, 0, 0, 0, 0, 1},
                            {1, 1, 1, 1, 1, 1, 1},
                    });
                    GameFrame gameFrame2 = new GameFrame(600, 450, mapMatrix2);
                    gameFrame2.setVisible(true);
                    break;
                case 25://检测代表level3
                    MapMatrix mapMatrix3 = new MapMatrix(new int[][]{
                            {0, 0, 1, 1, 1, 1, 0},
                            {1, 1, 1, 0, 0, 1, 0},
                            {1, 20, 0, 2, 10, 1, 1},
                            {1, 0, 0, 0, 10, 0, 1},
                            {1, 0, 1, 2, 0, 0, 1},
                            {1, 0, 0, 0, 0, 0, 1},
                            {1, 1, 1, 1, 1, 1, 1},
                    });
                    GameFrame gameFrame3 = new GameFrame(600, 450, mapMatrix3);
                    gameFrame3.setVisible(true);
                    break;
                case 26://检测代表level4
                    MapMatrix mapMatrix4 = new MapMatrix(new int[][]{
                            {0, 1, 1, 1, 1, 1, 0},
                            {1, 1, 20, 0, 0, 1, 1},
                            {1, 0, 0, 1, 0, 0, 1},
                            {1, 0, 10, 12, 10, 0, 1},
                            {1, 0, 1, 2, 0, 0, 1},
                            {1, 1, 0, 2, 0, 1, 1},
                            {0, 1, 1, 1, 1, 1, 0},
                    });
                    GameFrame gameFrame4 = new GameFrame(600, 450, mapMatrix4);
                    gameFrame4.setVisible(true);
                    break;
            }
        }

        //new RestartFrame();

    }
    public class RestartFrame extends JFrame {
        public RestartFrame() {
            Container container = getContentPane();
            container.setLayout(null);
            this.setLocationRelativeTo(null);

            JLabel label = new JLabel("Restart Or Not");
            label.setHorizontalAlignment(SwingConstants.CENTER);
            container.add(label);//对话框内容放在中间

            JButton RestartButton = new JButton("Yes, I'm vegetable");
            RestartButton.setBounds(30,200,150,20);//定义重开按钮
            JButton ExitButton = new JButton("No, I will try again");
            ExitButton.setBounds(170,200,150,20);//定义返回按钮


            RestartButton.addActionListener(e -> {

                RestartFrame.this.setVisible(false);
            });
            ExitButton.addActionListener(e -> {
                RestartFrame.this.setVisible(false);
            });

            container.add(RestartButton);
            container.add(ExitButton);
            container.setBackground(Color.white);

            setSize(400,300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setVisible(true);
        }
    }

    public boolean checkWin() {
        int[][] map = model.getMatrix();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 10) {
                    return false;
                }
            }
        }
        return true;
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
        }
        return false;
    }

    //todo: add other methods such as loadGame, saveGame...

}
