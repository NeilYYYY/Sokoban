package model;

import org.jetbrains.annotations.NotNull;

/**
 * This class is to record the map of one game. For example:
 * matrix =
 * {1, 1, 1, 1, 1, 1, 1, 0, 0},
 * {1, 0, 0, 0, 0, 0, 1, 1, 1},
 * {1, 20, 10, 3, 1, 0, 0, 0, 1},
 * {1, 0, 1, 2, 1, 0, 0, 0, 1},
 * {1, 0, 1, 1, 1, 1, 15, 5, 1},
 * {1, 4, 2, 100, 10, 0, 100, 0, 1},
 * {1, 0, 1, 1, 1, 0, 0, 3, 1},
 * {1, 0, 0, 0, 0, 0, 1, 2, 1},
 * {1, 1, 1, 1, 1, 1, 1, 1, 1}
 * The Unit digit number cannot be changed during one game.
 * 1 represents the wall
 * 0 represents the free space
 * 2 represents the target location
 * 3 represents the false door
 * 4 represents the true door
 * 5 represents the fragile grid
 * The Tens digit number can be changed during one game.
 * Ten digit 1 represents the box
 * Ten digit 2 represents the hero/player
 * So that 12 represents a box on the target location and 22 represents the player on the target location.
 * The Hundreds digit number's status can be changed during one game.
 * 100 represents the button
 */
public class MapMatrix {
    int[][] matrix;

    public MapMatrix(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getWidth() {
        return this.matrix[0].length;
    }

    public int getHeight() {
        return this.matrix.length;
    }

    public int getId(int row, int col) {
        return this.matrix[row][col];
    }

    public int[][] getMatrix() {
        return this.matrix;
    }

    public void copyMatrix(int @NotNull [] @NotNull [] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(matrix[i], 0, this.matrix[i], 0, matrix[i].length);
        }
    }
}
