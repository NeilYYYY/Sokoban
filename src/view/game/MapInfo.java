package view.game;

import model.MapMatrix;

public class MapInfo {
    private int id;
    private MapMatrix model;
    private int step;

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
}
