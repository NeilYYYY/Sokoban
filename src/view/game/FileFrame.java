package view.game;

import com.google.gson.Gson;
import model.MapMatrix;
import view.FrameUtil;
import view.login.User;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class FileFrame extends JFrame /*implements ActionListener */ {
    private User user;
    private GameFrame gameFrame;
    private int lv;
    private String filePath;
    private MapMatrix model;
    int[][] map;

    public FileFrame(int width, int height, User user, GameFrame gameframe, int lv) {
        this.user = user;
        this.gameFrame = gameframe;
        this.lv = lv;
        this.filePath = String.format("src\\saves\\%d-%d.json", this.lv, this.user.getId());
        File file = new File(filePath);
        this.setTitle("Savings");
        this.setAlwaysOnTop(false);
        this.setLayout(null);//关闭默认布局类型 自己手动设置布局
        this.setSize(width, height);
        this.setLocationRelativeTo(null);//设置GUI显示居中
        JButton[][] loads = new JButton[3][2];
        JButton[][] saves = new JButton[3][2];


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
        this.model = this.gameFrame.getGamePanel().getModel();
        //若json文件不存在，创建
        if (!file.exists()) {
            //判断不为游客模式
            if (this.user.getId() != 0) {
                System.out.println("非游客模式");
                Map<Integer, MapInfo> data = new HashMap<>();
                MapInfo mapInfo = new MapInfo();
                mapInfo.setModel(this.model);
                data.put(0, mapInfo);
                data.put(1, null);
                data.put(2, null);
                data.put(3, null);
                data.put(4, null);
                data.put(5, null);
                Gson gson = new Gson();
                //文件不存在，创建新文件并写入数据
                try (FileWriter writer = new FileWriter(filePath)) {
                    gson.toJson(data, writer);
                    System.out.println(filePath + "文件不存在，创建并保存");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("游客模式无法存档");
            }
        }
        loads[0][0].addActionListener(_ -> {
            int[][] array = new int[this.model.getHeight()][this.model.getWidth()];
            //读取地图
            try{
                Map<Integer, MapInfo> maps = loadMapsFromJson(filePath);
                MapInfo map = maps.get(0);
                if (map != null) {
                    System.out.println("读入存档0");
                    for (int i = 0; i < this.model.getHeight(); i++) {
                        for (int j = 0; j < this.model.getWidth(); j++) {
                            array[i][j] = map.getModel().getMatrix()[i][j];
                        }
                    }
                }else {
                    System.out.println("地图不存在");
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            //打开LevelFrame用读取内容涂
            //关闭此窗口
        });
        saves[0][1].addActionListener(_ -> {
            //读取文件
            //改变id文件
            //存储文件
        });
    }
    public static Map<Integer, MapInfo> loadMapsFromJson(String jsonFilePath) throws IOException{
        Gson gson = new Gson();
        FileReader reader = new FileReader(jsonFilePath);
        MapsResponse response = gson.fromJson(reader, MapsResponse.class);

        Map<Integer, MapInfo> maps = new HashMap<>();
        for (MapInfo map: response.getMaps()){
            maps.put(map.getId(), map);
        }

        return maps;
    }

}
