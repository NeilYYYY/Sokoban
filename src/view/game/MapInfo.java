package view.game;

import model.MapMatrix;

public class MapInfo {
    private int id;
    private MapMatrix model;

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
}
