package view.game;

import model.MapMatrix;
import org.jetbrains.annotations.NotNull;

public class MapInfo {
    private int id;
    private MapMatrix model;
    private int step;
    private int[] moveHero;
    private int[] moveBox;
    private int timeUsed;

    public MapInfo() {
    }

    public MapInfo(int @NotNull [] @NotNull [] model) {
        int[][] a = new int[model.length][model[0].length];
        this.model = new MapMatrix(a);
        this.model.copyMatrix(model);
    }

    public int getTimeUsed() {
        return timeUsed;
    }

    public void setTimeUsed(int timeUsed) {
        this.timeUsed = timeUsed;
    }

    public MapMatrix getModel() {
        return model;
    }

    public void setModel(MapMatrix model) {
        this.model = model;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public int[] getMoveBox() {
        return moveBox;
    }

    public void setMoveBox(int[] moveBox) {
        this.moveBox = moveBox;
    }

    public int[] getMoveHero() {
        return moveHero;
    }

    public void setMoveHero(int[] moveHero) {
        this.moveHero = moveHero;
    }

}
