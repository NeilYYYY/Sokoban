package view.game;

import java.util.List;

public class MapsResponse {
    private List<MapInfo> maps;
    public List<MapInfo> getMaps() {
        return maps;
    }

    public void setMaps(List<MapInfo> maps) {
        this.maps = maps;
    }
}
