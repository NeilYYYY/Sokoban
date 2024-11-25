package view.game;

import model.MapMatrix;

public class MapInfo {
    private int id;
    private MapMatrix model;
    private int step;
    private int[] moveHero;
    private int[] moveBox;
    private int[] moveFragile;

    public MapInfo() {}

    public MapInfo(int[][] model) {
        int[][] a = new int[model.length][model[0].length];
        this.model = new MapMatrix(a);
        this.model.copyMatrix(model);
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

    public int[] getMoveFragile() {
        return moveFragile;
    }

    public void setMoveFragile(int[] moveFragile) {
        this.moveFragile = moveFragile;
    }
}
