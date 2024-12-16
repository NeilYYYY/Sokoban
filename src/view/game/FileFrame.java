package view.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import model.Level;
import model.MapMatrix;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import view.FileSHAUtil;
import view.login.User;
import view.music.Sound;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    private final JButton backBtn;
    private final JButton loadBtn;
    private final JButton saveBtn;
    private final JLabel statusLabel;
    private final JLabel avatar;
    JList<String> savingList;
    private int step;
    private GameFrame gameFrame;
    private int id = 0;

    public FileFrame(int width, int height, @NotNull User user, GameFrame gameFrame, int lv, Sound sound) {
        Font f = new Font("Comic Sans MS", Font.BOLD, 18);
        this.gameFrame = gameFrame;
        this.filePath = String.format("saves/%d-%d.json", lv, user.getId());
        this.lv = lv;
        this.user = user;
        this.sound = sound;
        this.setTitle("Savings");
        this.setAlwaysOnTop(false);
        this.setLayout(null);
        this.setSize(width, height);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.gamePanel = new GamePanel(this.gameFrame.getGameController().getModel(), gameFrame, user, gameFrame.getGamePanel().getSteps());
        this.gamePanel.setFocusable(false);
        this.gamePanel.setLocation(130, height / 2 - this.gamePanel.getHeight() / 2);
        this.getContentPane().add(this.gamePanel);

        this.backBtn = new JButton("Back");
        this.backBtn.setLocation(new Point(125 + this.gamePanel.getWidth() + 30, 300));
        this.backBtn.setSize(100, 50);
        this.backBtn.setFont(f);
        this.backBtn.setForeground(Color.BLACK);
        this.backBtn.setMargin(new Insets(0, 0, 0, 0));
        this.backBtn.setBorderPainted(false);
        this.backBtn.setBorder(null);
        this.backBtn.setFocusPainted(false);
        this.backBtn.setContentAreaFilled(false);
        this.backBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backBtn.setForeground(Color.BLACK);
            }
        });
        this.getContentPane().add(this.backBtn);

        this.loadBtn = new JButton("Load");
        this.loadBtn.setFont(f);
        this.loadBtn.setForeground(Color.BLACK);
        this.loadBtn.setMargin(new Insets(0, 0, 0, 0));
        this.loadBtn.setBorderPainted(false);
        this.loadBtn.setBorder(null);
        this.loadBtn.setFocusPainted(false);
        this.loadBtn.setContentAreaFilled(false);
        this.loadBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                loadBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                loadBtn.setForeground(Color.BLACK);
            }
        });
        ImageIcon avatarIcon = new ImageIcon("resources/images/defaultAvatar.png");
        avatarIcon.setImage(avatarIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH));
        this.avatar = new JLabel(avatarIcon);
        this.avatar.setBounds(670, 10, 100, 100);
        this.getContentPane().add(avatar);

        JLabel userName = new JLabel(user.getUsername());
        userName.setFont(f);
        userName.setForeground(Color.BLUE);
        userName.setBounds(600, 35, 50, 50);
        userName.setHorizontalAlignment(SwingConstants.RIGHT);
        this.getContentPane().add(userName);

        this.saveBtn = new JButton("Save");
        this.saveBtn.setFont(f);
        this.saveBtn.setForeground(Color.BLACK);
        this.saveBtn.setMargin(new Insets(0, 0, 0, 0));
        this.saveBtn.setBorderPainted(false);
        this.saveBtn.setBorder(null);
        this.saveBtn.setFocusPainted(false);
        this.saveBtn.setContentAreaFilled(false);
        this.saveBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                saveBtn.setForeground(Color.YELLOW);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                saveBtn.setForeground(Color.BLACK);
            }
        });

        this.loadBtn.setBounds(125 + this.gamePanel.getWidth() + 30, 200, 100, 50);
        this.saveBtn.setBounds(125 + this.gamePanel.getWidth() + 30, 100, 100, 50);

        this.getContentPane().add(loadBtn);
        this.getContentPane().add(saveBtn);

        this.loadBtn.addActionListener(_ -> {
            try {
                Load(this.id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        this.saveBtn.addActionListener(_ -> {
            try {
                Save(this.id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                Show(this.id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        this.statusLabel = new JLabel("");
        this.statusLabel.setFont(f);
        this.statusLabel.setForeground(Color.RED);
        this.statusLabel.setBounds(100 + this.gamePanel.getWidth() / 2, 10, 80, 60);
        this.getContentPane().add(this.statusLabel);

        this.savingList = getSavingList();
        this.savingList.setCellRenderer((_, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value);
            label.setFont(new Font("Serif", Font.BOLD, 12));
            if (isSelected || cellHasFocus || index == this.id) {
                label.setOpaque(false);
                label.setForeground(Color.YELLOW);
            } else {
                label.setOpaque(false);
                label.setForeground(Color.BLACK);
            }
            return label;
        });
        this.savingList.setOpaque(false);
        this.savingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.savingList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                this.id = savingList.getSelectedIndex();
                try {
                    Show(this.id);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        this.savingList.setOpaque(false);
        JScrollPane scrollPane = new JScrollPane(this.savingList);
        scrollPane.setBounds(30, 125, 75, 115);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getViewport().setBackground(new Color(0, 0, 0, 0));
        scrollPane.setBorder(null);
        this.getContentPane().add(scrollPane);

        ImageIcon back = new ImageIcon("resources/images/Zako.png");
        back.setImage(back.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH));
        JLabel bg = new JLabel(back);
        bg.setBounds(0, 0, this.getWidth(), this.getHeight());
        this.getContentPane().add(bg, Integer.valueOf(-1));

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(null);
        this.copyModel = this.gameFrame.getGameController().getModel();
        this.step = this.gameFrame.getGamePanel().getSteps();

        backBtn.addActionListener(_ -> {
            this.gameFrame.getController().getTimer().start();
            this.backBtn.setForeground(Color.BLACK);
            this.dispose();
            this.gameFrame.setVisible(true);
            this.gameFrame.getGamePanel().requestFocusInWindow();
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> {
                        int previousIndex = id - 1;
                        if (previousIndex >= 0) {
                            id = previousIndex;
                            savingList.setSelectedIndex(id);
                        }
                        repaint();
                    }
                    case KeyEvent.VK_DOWN -> {
                        int nextIndex = id + 1;
                        if (nextIndex < 6) {
                            id = nextIndex;
                            savingList.setSelectedIndex(id);
                        }
                        repaint();
                    }
                    case KeyEvent.VK_S -> saveBtn.doClick();
                    case KeyEvent.VK_L -> loadBtn.doClick();
                    case KeyEvent.VK_ESCAPE -> backBtn.doClick();
                }
            }
        });
        SwingUtilities.invokeLater(this::requestFocusInWindow);
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

    public static boolean updateMapById(String filePath, int id, MapMatrix map, int step, int timeUsed, int[] moveHero, int[] moveBox) throws IOException {
        Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
        MapInfo mapToUpdate = maps.get(id);
        if (mapToUpdate != null) {
            mapToUpdate.setModel(map);
            mapToUpdate.setStep(step);
            mapToUpdate.setTimeUsed(timeUsed);
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
            throw new IOException("将Map写入JSON时错误: " + e.getMessage(), e);
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
                            gamePanel.getGrids()[i][j].setBoxInGrid(new Box(gamePanel.getGRID_SIZE() - 10, gamePanel.getGRID_SIZE() - 10, gamePanel.getFrame().getUser(), map.getModel().getId(i, j)));
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

    public JLabel getAvatar() {
        return avatar;
    }

    public int getId() {
        return id;
    }

    public void Load(int id) {
        if (checkFileFailed()) {
            log.warning("存档文件损坏喵！");
            fixFile();
            JOptionPane.showMessageDialog(this, "存档文件损坏，已重置存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            reopenGameFrame();
            return;
        }
        try {
            Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
            MapInfo map = maps.get(id);
            if (map.getModel() != null) {
                System.out.printf("读入存档%d喵~\n", id);
                gameFrame.getGamePanel().setFlag(true);
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
                                    gameFrame.getGamePanel().getGrids()[i][j].setBoxInGrid(new Box(gameFrame.getGamePanel().getGRID_SIZE() - 10, gameFrame.getGamePanel().getGRID_SIZE() - 10, gameFrame.getUser(), map.getModel().getId(i, j)));
                            case 2 -> {
                                gameFrame.getGamePanel().getGrids()[i][j].setHeroInGrid(gameFrame.getGamePanel().getHero());
                                gameFrame.getGamePanel().getHero().setCol(j);
                                gameFrame.getGamePanel().getHero().setRow(i);
                            }
                        }
                    }
                }
                gameFrame.getGamePanel().setSteps(map.getStep());
                gameFrame.getGamePanel().getStepLabel().setText(String.format("Step: %d", map.getStep()));
                gameFrame.getGamePanel().setMoveHero(map.getMoveHero());
                gameFrame.getGamePanel().setMoveBox(map.getMoveBox());
                gameFrame.getGamePanel().setTime(map.getTimeUsed());
                gameFrame.getTimeLabel().setText(String.format("Time: %d", map.getTimeUsed()));
                gameFrame.getController().getTimer().stop();
                gameFrame.getController().setTimer(new Timer(1000, _ -> {
                    gameFrame.getGamePanel().setTime(gameFrame.getGamePanel().getTime() + 1);
                    gameFrame.getGamePanel().getFrame().getTimeLabel().setText(String.format("Time: %d", gameFrame.getGamePanel().getTime()));
                }));
                this.loadBtn.setForeground(Color.BLACK);
                this.dispose();
                this.gameFrame.setVisible(true);
                gameFrame.getGamePanel().repaint();
                gameFrame.getGamePanel().requestFocusInWindow();
            } else {
                log.warning("地图不存在喵");
                JOptionPane.showMessageDialog(this, "这是个空存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    private void reopenGameFrame() {
        gameFrame.dispose();
        MapMatrix mapMatrix = new MapMatrix(Level.values()[this.lv - 1].getMap());
        gameFrame = new GameFrame(800, 450, gameFrame.getLevelFrame(), mapMatrix, this.user, this.sound, this.lv, 0, false, 1);
        this.dispose();
        gameFrame.setVisible(true);
    }

    public void Show(int id) {
        this.requestFocusInWindow();
        if (checkFileFailed()) {
            log.warning("存档文件损坏喵！");
            fixFile();
            JOptionPane.showMessageDialog(this, "存档文件损坏，已重置存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
            MapInfo map = maps.get(id);
            if (map.getModel() != null) {
                this.statusLabel.setText("");
                reloadPanel(map, gamePanel);
            } else {
                map = new MapInfo(Level.values()[this.lv - 1].getMap());
                this.statusLabel.setText("EMPTY");
                reloadPanel(map, gamePanel);
            }
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    public void Save(int id) {
        //读取文件
        this.requestFocusInWindow();
        if (checkFileFailed()) {
            log.warning("存档文件损坏喵！");
            fixFile();
            JOptionPane.showMessageDialog(this, "存档文件损坏喵~已重置存档喵~", "Error", JOptionPane.INFORMATION_MESSAGE);
            reopenGameFrame();
            return;
        }
        if (id == 0) {
            JOptionPane.showMessageDialog(this, "这是Auto_Save喵~", "Tips", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try {
            this.step = this.gameFrame.getGamePanel().getSteps();
            boolean result = updateMapById(this.filePath, id, copyModel, this.step, gameFrame.getGamePanel().getTime(), gameFrame.getGamePanel().getMoveHero(), gameFrame.getGamePanel().getMoveBox());
            if (result) {
                System.out.println("保存成功喵~");
            } else {
                log.warning("保存失败喵");
            }
            FileSHAUtil.saveSHAToFile(FileSHAUtil.calculateSHA(new File(this.filePath)), new File(filePath + ".sha"));
        } catch (IOException e) {
            log.info(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkFileFailed() {
        try {
            return !new File(filePath).exists() || !new File(filePath + ".sha").exists() || !FileSHAUtil.compareSHA(FileSHAUtil.loadSHAFromFile(new File(this.filePath + ".sha")), FileSHAUtil.calculateSHA(new File(this.filePath)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void fixFile() {
        if (new File(filePath).delete() || new File(filePath + ".sha").delete()) {
            log.warning("存档文件已清除喵");
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
            System.out.println("创建新文件并保存喵");
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        try {
            createFile(filePath + ".sha");
            FileSHAUtil.saveSHAToFile(FileSHAUtil.calculateSHA(new File(this.filePath)), new File(filePath + ".sha"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.requestFocusInWindow();
    }
}
