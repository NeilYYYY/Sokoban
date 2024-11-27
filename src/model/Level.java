package model;

import org.jetbrains.annotations.NotNull;

public enum Level {
    LEVEL_1(new int[][]{
            {1, 1, 1, 1, 1, 1},
            {1, 20, 0, 0, 0, 1},
            {1, 0, 0, 10, 2, 1},
            {1, 0, 2, 10, 0, 1},
            {1, 1, 1, 1, 1, 1}
    }),
    LEVEL_2(new int[][]{
            {1, 1, 1, 1, 1, 1, 0},
            {1, 20, 0, 0, 0, 1, 1},
            {1, 0, 10, 10, 0, 0, 1},
            {1, 0, 1, 2, 0, 2, 1},
            {1, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1}
    }),
    LEVEL_3(new int[][]{
            {0, 0, 1, 1, 1, 1, 0},
            {1, 1, 1, 0, 0, 1, 0},
            {1, 20, 0, 2, 10, 1, 1},
            {1, 0, 0, 0, 10, 0, 1},
            {1, 0, 1, 2, 0, 0, 1},
            {1, 0, 0, 0, 0, 0, 1},
            {1, 1, 1, 1, 1, 1, 1}
    }),
    LEVEL_4(new int[][]{
            {0, 1, 1, 1, 1, 1, 0},
            {1, 1, 20, 0, 0, 1, 1},
            {1, 0, 0, 1, 0, 0, 1},
            {1, 0, 10, 12, 10, 0, 1},
            {1, 0, 0, 2, 0, 0, 1},
            {1, 1, 0, 2, 0, 1, 1},
            {0, 1, 1, 1, 1, 1, 0}
    }),
    LEVEL_5(new int[][]{
            {1, 1, 1, 1, 1, 1, 0, 0},
            {1, 0, 0, 0, 0, 1, 1, 1},
            {1, 0, 0, 0, 2, 2, 0, 1},
            {1, 0, 10, 10, 10, 20, 0, 1},
            {1, 0, 0, 1, 0, 2, 0, 1},
            {1, 1, 1, 1, 1, 1, 1, 1}
    }),
    LEVEL_6(new int[][]{
            {1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 0, 0, 0, 0, 0, 1, 1, 1},
            {1, 20, 10, 3, 1, 0, 0, 0, 1},//3是假门，4是真门
            {1, 0, 1, 2, 1, 0, 0, 0, 1},
            {1, 0, 1, 1, 1, 1, 15, 5, 1},//易碎格5
            {1, 4, 2, 100, 10, 0, 100, 0, 1},//按钮100
            {1, 0, 1, 1, 1, 0, 0, 3, 1},
            {1, 0, 0, 0, 0, 0, 1, 2, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1}
    });
    private final int[][] map;

    Level(int @NotNull [] @NotNull [] map) {
        this.map = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            System.arraycopy(map[i], 0, this.map[i], 0, map[i].length);
        }
    }

    public int[][] getMap() {
        return map;
    }
}
