package view.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import model.Level;
import model.MapMatrix;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import view.FileMD5Util;
import view.login.User;
import view.music.Sound;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.logging.Logger;


public class FileFrame extends JFrame /*implements ActionListener */ {
    private final String filePath;
    private final MapMatrix copyModel;
    private final GamePanel gamePanel;
    private final int lv;
    private final User user;
    private final Sound sound;
    private final Logger log = Logger.getLogger(FileFrame.class.getName());
    JList<String> savingList;
    private int step;
    private GameFrame gameFrame;
    private int id = 0;

    public FileFrame(int width, int height, User user, GameFrame gameFrame, int lv, Sound sound) {
        try {
            String lookAndFeel = UIManager.getCrossPlatformLookAndFeelClassName();
            UIManager.setLookAndFeel(lookAndFeel);
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        Font f = new Font("Comic Sans MS", Font.BOLD, 18);
        this.gameFrame = gameFrame;
        this.filePath = String.format("src/saves/%d-%d.json", lv, user.id());
        this.lv = lv;
        this.user = user;
        this.sound = sound;
        this.setTitle("Savings");
        this.setAlwaysOnTop(false);
        this.setLayout(null);//关闭默认布局类型 自己手动设置布局
        this.setSize(width, height);
        this.setLocationRelativeTo(null);//设置GUI显示居中
        this.setResizable(false);
        this.gamePanel = new GamePanel(this.gameFrame.getGameController().getModel(), gameFrame, user, gameFrame.getGamePanel().getSteps());
        this.gamePanel.setFocusable(false);
        this.gamePanel.setLocation(130, height / 2 - this.gamePanel.getHeight() / 2);
        this.getContentPane().add(this.gamePanel);

        JButton backBtn = new JButton("Back");
        backBtn.setLocation(new Point(125 + this.gamePanel.getWidth() + 30, 300));
        backBtn.setSize(100, 50);
        backBtn.setFont(f);
        backBtn.setForeground(Color.BLACK);
        backBtn.setMargin(new Insets(0, 0, 0, 0));
        backBtn.setBorderPainted(false);
        backBtn.setBorder(null);
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(false);
        this.getContentPane().add(backBtn);

        JButton loadBtn = new JButton("Load");
        loadBtn.setFont(f);
        loadBtn.setForeground(Color.BLACK);
        loadBtn.setMargin(new Insets(0, 0, 0, 0));
        loadBtn.setBorderPainted(false);
        loadBtn.setBorder(null);
        loadBtn.setFocusPainted(false);
        loadBtn.setContentAreaFilled(false);

        JButton saveBtn = new JButton("Save");
        saveBtn.setFont(f);
        saveBtn.setForeground(Color.BLACK);
        saveBtn.setMargin(new Insets(0, 0, 0, 0));
        saveBtn.setBorderPainted(false);
        saveBtn.setBorder(null);
        saveBtn.setFocusPainted(false);
        saveBtn.setContentAreaFilled(false);

        loadBtn.setBounds(125 + this.gamePanel.getWidth() + 30, 200, 100, 50);
        saveBtn.setBounds(125 + this.gamePanel.getWidth() + 30, 100, 100, 50);

        this.getContentPane().add(loadBtn);
        this.getContentPane().add(saveBtn);

        loadBtn.addActionListener(_ -> {
            try {
                Load(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        saveBtn.addActionListener(_ -> {
            try {
                Save(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                Show(id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        savingList = getSavingList();
        savingList.setCellRenderer((_, value, _, _, _) -> {
            JLabel label = new JLabel(value);
            label.setOpaque(false); // 让每个项的背景透明
            label.setFont(new Font("Serif", Font.BOLD, 12));
            label.setForeground(Color.BLACK); // 设置文本颜色为黑色
            return label;
        });
        savingList.setOpaque(false);
        savingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        savingList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                id = savingList.getSelectedIndex();
                System.out.println(id);
                try {
                    Show(id);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        savingList.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(savingList);
        scrollPane.setBounds(30, 125, 75, 115);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        scrollPane.setBorder(null);
        this.getContentPane().add(scrollPane);

        ImageIcon back = new ImageIcon("src/images/FileFrameBackground.png");
        back.setImage(back.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        JLabel bg = new JLabel(back);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1)); // 背景图置于最底层

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置关闭模式
        this.getContentPane().setLayout(null);
        copyModel = this.gameFrame.getGameController().getModel();
        this.step = this.gameFrame.getGamePanel().getSteps();
        backBtn.addActionListener(_ -> {
            this.dispose();
            this.gameFrame.setVisible(true);
        });
    }

    @Contract(" -> new")
    private static @NotNull JList<String> getSavingList() {
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

    public static @NotNull Map<Integer, MapInfo> loadMapsFromJson(String jsonFilePath) throws IOException {
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

    public static void saveMapsToJson(@NotNull Map<Integer, MapInfo> maps, String filePath) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        MapsResponse response = new MapsResponse();
        response.setMaps(List.copyOf(maps.values()));
        // 使用 FileWriter 创建文件
        try (Writer writer = new FileWriter(filePath)) {
            // 创建 JsonWriter 并设置缩进为 4 个空格
            JsonWriter jsonWriter = new JsonWriter(writer);
            jsonWriter.setIndent("    "); // 4 个空格的缩进
            // 将 response 对象写入文件
            gson.toJson(response, MapsResponse.class, jsonWriter);
            jsonWriter.flush(); // 确保所有内容都已写入
        } catch (IOException e) {
            throw new IOException("Error saving maps to JSON: " + e.getMessage(), e);
        }
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

    private static void reloadPanel(MapInfo map, @NotNull GamePanel gamePanel) {
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
                            gamePanel.getGrids()[i][j].setBoxInGrid(new Box(gamePanel.getGRID_SIZE() - 10, gamePanel.getGRID_SIZE() - 10, gamePanel.getFrame().getUser()));
                    case 2 -> {
                        gamePanel.getGrids()[i][j].setHeroInGrid(gamePanel.getHero());
                        gamePanel.getHero().setCol(j);
                        gamePanel.getHero().setRow(i);
                    }
                }
            }
        }
        gamePanel.repaint();
    }

    public void Load(int id) {
        //读取地图
        if (checkFile()) {
            System.err.println("存档文件损坏喵！");
            fixFile();
            JOptionPane.showMessageDialog(this, "存档文件损坏喵~已重置存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            reopenGameFrame();
            return;
        }
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
                                    gameFrame.getGamePanel().getGrids()[i][j].setBoxInGrid(new Box(gameFrame.getGamePanel().getGRID_SIZE() - 10, gameFrame.getGamePanel().getGRID_SIZE() - 10, gameFrame.getUser()));
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
                gameFrame.getGamePanel().setMoveHero(maps.get(map.getId()).getMoveHero());
                gameFrame.getGamePanel().setMoveBox(maps.get(map.getId()).getMoveBox());
                this.dispose();
                this.gameFrame.setVisible(true);
                gameFrame.getGamePanel().repaint();
                gameFrame.getGamePanel().requestFocusInWindow();
            } else {
                System.err.println("地图不存在");
                JOptionPane.showMessageDialog(this, "这是个空存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    private void reopenGameFrame() {
        gameFrame.dispose();
        MapMatrix mapMatrix = new MapMatrix(Level.values()[this.lv - 1].getMap());
        gameFrame = new GameFrame(800, 450, mapMatrix, this.user, this.lv, 0, this.sound, false, 1, gameFrame.getLevelFrame());
        this.dispose();
        gameFrame.setVisible(true);
    }

    public void Show(int id) {
        //读取地图
        if (checkFile()) {
            System.err.println("存档文件损坏喵！");
            fixFile();
            JOptionPane.showMessageDialog(this, "存档文件损坏喵~已重置存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
            MapInfo map = maps.get(id);
            if (map.getModel() != null) {
                System.out.printf("读入存档%d\n", id);
                reloadPanel(map, gamePanel);
            } else {
                map = new MapInfo(Level.values()[this.lv - 1].getMap());
                System.err.println("地图不存在");
                reloadPanel(map, gamePanel);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    public void Save(int id) {
        //读取文件
        if (checkFile()) {
            System.err.println("存档文件损坏喵！");
            fixFile();
            JOptionPane.showMessageDialog(this, "存档文件损坏喵~已重置存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            reopenGameFrame();
        }
        if (id == 0) {
            JOptionPane.showMessageDialog(this, "这是Auto_Save喵~", "Tips", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            this.step = this.gameFrame.getGamePanel().getSteps();
            boolean result = updateMapById(id, copyModel, this.step, gameFrame.getGamePanel().getMoveHero(), gameFrame.getGamePanel().getMoveBox(), this.filePath);
            if (result) {
                System.out.println("更新成功");
            } else {
                System.err.println("更新失败");
            }
            FileMD5Util.saveMD5ToFile(FileMD5Util.calculateMD5(new File(this.filePath)), new File(filePath + ".md5"));
        } catch (IOException e) {
            log.info(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkFile() {
        try {
            return FileMD5Util.compareMD5failed(FileMD5Util.loadMD5FromFile(new File(this.filePath + ".md5")), FileMD5Util.calculateMD5(new File(this.filePath)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void fixFile() {
        File file = new File(filePath);
        File file1 = new File(filePath + ".md5");
        if (file.delete() && file1.delete()) {
            System.err.println("文件已删除");
        } else {
            System.err.println("删除文件失败");
        }

        MapMatrix originalMap = new MapMatrix(Level.values()[gameFrame.getLv() - 1].getMap());
        try {
            createFile(filePath);
            for (int i = 0; i < 6; i++) {
                MapInfo mapInfo = new MapInfo();
                mapInfo.setModel(originalMap);
                mapInfo.setId(i);
                mapInfo.setStep(0);
                addNewMap(mapInfo, filePath);
            }
            System.out.println("创建新文件并保存");
        } catch (Exception e) {
            System.err.println("保存失败");
            log.info(e.getMessage());
        }
        try {
            createFile(filePath + ".md5");
            FileMD5Util.saveMD5ToFile(FileMD5Util.calculateMD5(new File(this.filePath)), new File(filePath + ".md5"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
