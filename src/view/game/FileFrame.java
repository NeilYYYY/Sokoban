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
    private User user;
    private GameFrame gameFrame;
    private int lv;
    private int step;
    private String filePath;
    private MapMatrix model;
    int[][] map;
    private MapMatrix copyModel;

    public FileFrame(int width, int height, User user, GameFrame gameframe, int lv) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.user = user;
        this.gameFrame = gameframe;
        this.lv = lv;
        this.filePath = String.format("src/saves/%d-%d.json", this.lv, this.user.getId());
        File file = new File(filePath);
        this.setTitle("Savings");
        this.setAlwaysOnTop(false);
        this.setLayout(null);//关闭默认布局类型 自己手动设置布局
        this.setSize(width, height);
        this.setLocationRelativeTo(null);//设置GUI显示居中
        JButton[][] loads = new JButton[3][2];
        JButton[][] saves = new JButton[3][2];
        JButton back = new JButton("Back");
        back = FrameUtil.createButton(this,"Back",new Point(500,500),100,50);


        for (int i = 0; i < loads.length; i++) {
            for (int j = 0; j < loads[0].length; j++) {
                if (i == 0 && j == 0) {
                    loads[i][j] = FrameUtil.createButton(this, "Load", new Point(150,100), 100, 50);
                }
                else{
                    loads[i][j] = FrameUtil.createButton(this, "Load ", new Point((i + 1) * 100 + 50, j * 100 + 50), 100, 50);
                    saves[i][j] = FrameUtil.createButton(this,"Save", new Point((i + 1) * 100 + 50, j * 100 + 100), 100, 50);
                    this.add(loads[i][j]);
                    this.add(saves[i][j]);
                }
            }
        }
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置关闭模式
        this.getContentPane().setLayout(null);
        this.model = this.gameFrame.getGameController().getModel();
        copyModel = this.model;
        this.step = this.gameFrame.getGamePanel().getSteps();
        //若json文件不存在，创建
        //if (this.user.getId() != 0){
            System.out.println("非游客模式");
            if (!file.exists()) {
                Map<Integer, MapInfo> data = new HashMap<>();
                MapInfo mapInfo = new MapInfo();
                mapInfo.setModel(copyModel);
                try {
                    createFile(filePath);
                    for (int i = 0; i < 6; i++){
                        MapInfo mapInfo2 = new MapInfo();
                        mapInfo2.setModel(null);
                        mapInfo2.setId(i);
                        mapInfo2.setStep(0);
                        addNewMap(mapInfo2, filePath);
                    }
                    System.out.println("创建新文件并保存");
                }catch (Exception e){
                    System.out.println("保存失败");
                    e.printStackTrace();
                }

            }
            back.addActionListener(e -> {
                this.setVisible(false);
                gameFrame.setVisible(true);
            });
            loads[0][0].addActionListener(_ -> {
                //读取地图
                try{
                    Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
                    MapInfo map = maps.get(0);
                    if (map != null) {
                        System.out.println("读入存档0");
                        GameFrame gameFrame = new GameFrame(800, 450, copyModel, user, lv, map.getStep());
                        this.setVisible(false);
                        gameFrame.setVisible(true);
                    }else {
                        System.out.println("地图不存在");
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
        loads[1][0].addActionListener(_ -> {
            //读取地图
            try{
                Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
                MapInfo map = maps.get(1);
                if (map.getModel() != null) {
                    System.out.println("读入存档1");
                    GameFrame gameFrame = new GameFrame(800, 450, map.getModel(), user, lv, map.getStep());
                    this.setVisible(false);
                    gameFrame.setVisible(true);
                }else {
                    System.out.println("地图不存在");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
            saves[1][0].addActionListener(_ -> {
                //读取文件
                try{
                    boolean result = updateMapById(1, copyModel, this.step, filePath);
                    if (result) {
                        System.out.println("更新成功");
                    }else{
                        System.out.println("更新失败");
                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            });
        loads[2][0].addActionListener(_ -> {
            //读取地图
            try{
                Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
                MapInfo map = maps.get(2);
                if (map.getModel() != null) {
                    System.out.println("读入存档2");
                    GameFrame gameFrame = new GameFrame(800, 450, map.getModel(), user, lv, map.getStep());
                    this.setVisible(false);
                    gameFrame.setVisible(true);
                }else {
                    System.out.println("地图不存在");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        saves[2][0].addActionListener(_ -> {
            //读取文件
            try{
                boolean result = updateMapById(2, copyModel, this.step, filePath);
                if (result) {
                    System.out.println("更新成功");
                }else{
                    System.out.println("更新失败");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        loads[0][1].addActionListener(_ -> {
            //读取地图
            try{
                Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
                MapInfo map = maps.get(3);
                if (map.getModel() != null) {
                    System.out.println("读入存档3");
                    GameFrame gameFrame = new GameFrame(800, 450, map.getModel(), user, lv, map.getStep());
                    this.setVisible(false);
                    gameFrame.getGamePanel().setSteps(map.getStep());
                    gameFrame.setVisible(true);
                }else {
                    System.out.println("地图不存在");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        saves[0][1].addActionListener(_ -> {
            //读取文件
            try{
                boolean result = updateMapById(3, copyModel, this.step, filePath);
                if (result) {
                    System.out.println("更新成功");
                }else{
                    System.out.println("更新失败");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        loads[1][1].addActionListener(_ -> {
            //读取地图
            try{
                Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
                MapInfo map = maps.get(4);
                if (map.getModel() != null) {
                    System.out.println("读入存档4");
                    GameFrame gameFrame = new GameFrame(800, 450, map.getModel(), user, lv, map.getStep());
                    this.setVisible(false);
                    gameFrame.getGamePanel().setSteps(map.getStep());
                    gameFrame.setVisible(true);
                }else {
                    System.out.println("地图不存在");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        saves[1][1].addActionListener(_ -> {
            //读取文件
            try{
                boolean result = updateMapById(4, copyModel, this.step, filePath);
                if (result) {
                    System.out.println("更新成功");
                }else{
                    System.out.println("更新失败");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        loads[2][1].addActionListener(_ -> {
            //读取地图
            try{
                Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
                MapInfo map = maps.get(5);
                if (map.getModel() != null) {
                    System.out.println("读入存档5");
                    GameFrame gameFrame = new GameFrame(800, 450, map.getModel(), user, lv, map.getStep());
                    this.setVisible(false);
                    gameFrame.setVisible(true);
                }else {
                    System.out.println("地图不存在");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        saves[2][1].addActionListener(_ -> {
            //读取文件
            try{
                boolean result = updateMapById(5, copyModel, this.step, filePath);
                if (result) {
                    System.out.println("更新成功");
                }else{
                    System.out.println("更新失败");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        });
        //}else {
        //    System.out.println("游客模式无法存档");
        //}


    }
    public static Map<Integer, MapInfo> loadMapsFromJson(String jsonFilePath) throws IOException{
        Gson gson = new Gson();
        FileReader reader = new FileReader(jsonFilePath);
        MapsResponse response = gson.fromJson(reader, MapsResponse.class);

        if (response == null || response.getMaps() == null) {
            response = new MapsResponse();
            response.setMaps(List.of());
        }
        Map<Integer, MapInfo> maps = new HashMap<>();
        for (MapInfo map: response.getMaps()){
            maps.put(map.getId(), map);
        }

        return maps;
    }

    public static boolean updateMapById (int id, MapMatrix map, int step, String filePath)throws IOException{
        Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
        MapInfo mapToUpdate = maps.get(id);
        if (mapToUpdate != null) {
            mapToUpdate.setModel(map);
            mapToUpdate.setStep(step);
            saveMapsToJson(maps,filePath);
            return true;
        }
        return false;
    }


    public static boolean addNewMap (MapInfo newMap, String filePath)throws IOException{
        Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
        maps.put(newMap.getId(), newMap);
        saveMapsToJson(maps,filePath);
        return true;
    }

    public static void saveMapsToJson (Map<Integer, MapInfo> maps, String filePath) throws IOException {
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
            file.createNewFile();
            try(FileWriter writer = new FileWriter(file)){
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                MapsResponse response = new MapsResponse();
                response.setMaps(new ArrayList<MapInfo>());
                gson.toJson(response,writer);
                writer.flush();
            }
        }
    }
}
