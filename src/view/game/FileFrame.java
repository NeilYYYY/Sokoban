package view.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.MapMatrix;
import view.FrameUtil;
import view.login.User;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class FileFrame extends JFrame /*implements ActionListener */ {
    private final GameFrame gameFrame;
    private final int step;
    private final String filePath;
    private final MapMatrix copyModel;
    private final GamePanel gamePanel;
    JList<String> levelList;
    private int id = 0;
    private final int[] moveHero;
    private final int[] moveBox;

    public FileFrame(int width, int height, User user, GameFrame gameFrame, int lv) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.gameFrame = gameFrame;
        this.filePath = String.format("src/saves/%d-%d.json", lv, user.id());
        this.moveHero = gameFrame.getGamePanel().getMoveHero();
        this.moveBox = gameFrame.getGamePanel().getMoveBox();
        File file = new File(filePath);
        this.setTitle("Savings");
        this.setAlwaysOnTop(false);
        this.setLayout(null);//关闭默认布局类型 自己手动设置布局
        this.setSize(width, height);
        this.setLocationRelativeTo(null);//设置GUI显示居中
        this.setResizable(false);
        this.gamePanel = new GamePanel(this.gameFrame.getGameController().getModel(), gameFrame, user, gameFrame.getGamePanel().getSteps());
        this.gamePanel.setFocusable(false);
        this.gamePanel.setLocation(130, height / 2 - this.gamePanel.getHeight() / 2);
        this.add(this.gamePanel);
        JButton backBtn = FrameUtil.createButton(this, "Back", new Point(125 + this.gamePanel.getWidth() + 30, 300), 100, 50);
        JButton loadBtn = new JButton("Load");
        JButton saveBtn = new JButton("Save");
        loadBtn.setBounds(125 + this.gamePanel.getWidth() + 30, 200, 100, 50);
        saveBtn.setBounds(125 + this.gamePanel.getWidth() + 30, 100, 100, 50);
        this.add(loadBtn);
        this.add(saveBtn);

        loadBtn.addActionListener(_ -> Load(id));
        saveBtn.addActionListener(_ -> {
            Save(id);
            Show(id);
        });

        levelList = getLevelList();
        levelList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        levelList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                id = levelList.getSelectedIndex();
                System.out.println(id);
                Show(id);
            }
        });
        levelList.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(levelList);
        scrollPane.setBounds(30, 125, 70, 110);
        scrollPane.getViewport().setBackground(Color.WHITE);
        this.add(scrollPane);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置关闭模式
        this.getContentPane().setLayout(null);
        copyModel = this.gameFrame.getGameController().getModel();
        this.step = this.gameFrame.getGamePanel().getSteps();
        //若json文件不存在，创建
        if (!file.exists()) {
            MapInfo mapInfo = new MapInfo();
            mapInfo.setModel(copyModel);
            try {
                createFile(filePath);
                for (int i = 0; i < 6; i++) {
                    MapInfo mapInfo2 = new MapInfo();
                    mapInfo2.setModel(null);
                    mapInfo2.setId(i);
                    mapInfo2.setStep(0);
                    addNewMap(mapInfo2, filePath);
                }
                System.out.println("创建新文件并保存");
            } catch (Exception e) {
                System.out.println("保存失败");
                e.printStackTrace();
            }
        }
        backBtn.addActionListener(_ -> {
            this.dispose();
            this.gameFrame.setVisible(true);
        });
    }

    private static JList<String> getLevelList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("auto_save");
        listModel.addElement("save_01");
        listModel.addElement("save_02");
        listModel.addElement("save_03");
        listModel.addElement("save_04");
        listModel.addElement("save_05");
        // 创建列表，并设置选择监听器
        return new JList<>(listModel);
    }

    public static Map<Integer, MapInfo> loadMapsFromJson(String jsonFilePath) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(jsonFilePath);
        MapsResponse response = gson.fromJson(reader, MapsResponse.class);

        if (response == null || response.getMaps() == null) {
            response = new MapsResponse();
            response.setMaps(List.of());
        }
        Map<Integer, MapInfo> maps = new HashMap<>();
        for (MapInfo map : response.getMaps()) {
            maps.put(map.getId(), map);
        }
        return maps;
    }

    public static boolean updateMapById(int id, MapMatrix map, int step, int[] moveHero, int[] moveBox, String filePath) throws IOException {
        Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
        MapInfo mapToUpdate = maps.get(id);
        if (mapToUpdate != null) {
            mapToUpdate.setModel(map);
            mapToUpdate.setStep(step);
            mapToUpdate.setMoveHero(moveHero);
            mapToUpdate.setMoveBox(moveBox);
            saveMapsToJson(maps, filePath);
            return true;
        }
        return false;
    }

    public static void addNewMap(MapInfo newMap, String filePath) throws IOException {
        Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
        maps.put(newMap.getId(), newMap);
        saveMapsToJson(maps, filePath);
    }

    public static void saveMapsToJson(Map<Integer, MapInfo> maps, String filePath) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        MapsResponse response = new MapsResponse();
        response.setMaps(List.copyOf(maps.values()));
        FileWriter writer = new FileWriter(filePath);
        gson.toJson(response, writer);
        writer.flush();
        writer.close();
    }

    public static void createFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                MapsResponse response = new MapsResponse();
                response.setMaps(new ArrayList<>());
                gson.toJson(response, writer);
                writer.flush();
            }
        }
    }

    private static void reloadPanel(MapInfo map, GamePanel gamePanel) {
        for (int i = 0; i < gamePanel.getGrids().length; i++) {
            for (int j = 0; j < gamePanel.getGrids()[i].length; j++) {
                switch (gamePanel.getModel().getId(i, j) / 10) {
                    case 1 -> gamePanel.getGrids()[i][j].removeBoxFromGrid();
                    case 2 -> gamePanel.getGrids()[i][j].removeHeroFromGrid();
                }
            }
        }
        gamePanel.getModel().copyMatrix(map.getModel().getMatrix());
        for (int i = 0; i < gamePanel.getGrids().length; i++) {
            for (int j = 0; j < gamePanel.getGrids()[i].length; j++) {
                switch (map.getModel().getId(i, j) / 10) {
                    case 1 ->
                            gamePanel.getGrids()[i][j].setBoxInGrid(new Box(gamePanel.getGRID_SIZE() - 10, gamePanel.getGRID_SIZE() - 10));
                    case 2 -> {
                        gamePanel.getGrids()[i][j].setHeroInGrid(gamePanel.getHero());
                        gamePanel.getHero().setCol(j);
                        gamePanel.getHero().setRow(i);
                    }
                }
            }
        }
    }

    public void Load(int id) {
        //读取地图
        try {
            Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
            MapInfo map = maps.get(id);
            if (map.getModel() != null) {
                System.out.printf("读入存档%d\n", id);
                for (int i = 0; i < gameFrame.getGamePanel().getGrids().length; i++) {
                    for (int j = 0; j < gameFrame.getGamePanel().getGrids()[i].length; j++) {
                        switch (copyModel.getId(i, j) / 10) {
                            case 1 -> gameFrame.getGamePanel().getGrids()[i][j].removeBoxFromGrid();
                            case 2 -> gameFrame.getGamePanel().getGrids()[i][j].removeHeroFromGrid();
                        }
                    }
                }
                gameFrame.getGameController().getModel().copyMatrix(map.getModel().getMatrix());
                for (int i = 0; i < gameFrame.getGamePanel().getGrids().length; i++) {
                    for (int j = 0; j < gameFrame.getGamePanel().getGrids()[i].length; j++) {
                        switch (map.getModel().getId(i, j) / 10) {
                            case 1 ->
                                    gameFrame.getGamePanel().getGrids()[i][j].setBoxInGrid(new Box(gameFrame.getGamePanel().getGRID_SIZE() - 10, gameFrame.getGamePanel().getGRID_SIZE() - 10));
                            case 2 -> {
                                gameFrame.getGamePanel().getGrids()[i][j].setHeroInGrid(gameFrame.getGamePanel().getHero());
                                gameFrame.getGamePanel().getHero().setCol(j);
                                gameFrame.getGamePanel().getHero().setRow(i);
                            }
                        }
                    }
                }
                gameFrame.getGamePanel().setSteps(map.getStep());
                gameFrame.getGamePanel().getStepLabel().setText(String.format("Step: %d", gameFrame.getGamePanel().getSteps()));
                this.dispose();
                this.gameFrame.setVisible(true);
                gameFrame.getGamePanel().requestFocusInWindow();
            } else {
                System.out.println("地图不存在");
                JOptionPane.showMessageDialog(this, "这是个空存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Show(int id) {
        //读取地图
        try {
            Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
            MapInfo map = maps.get(id);
            if (map.getModel() != null) {
                System.out.printf("读入存档%d\n", id);
                reloadPanel(map, gamePanel);
            } else {
                map = maps.get(0);
                System.out.println("地图不存在");
                reloadPanel(map, gamePanel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Save(int id) {
        //读取文件
        if (id == 0) {
            JOptionPane.showMessageDialog(this, "这是Auto_Save喵~", "Tips", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            boolean result = updateMapById(id, copyModel, this.step, this.moveHero, this.moveBox, this.filePath);
            if (result) {
                System.out.println("更新成功");
            } else {
                System.out.println("更新失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
