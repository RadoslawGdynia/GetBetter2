package Models.CalendarModel.Tile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class PlanTilesManager {
    public static final Logger log = LoggerFactory.getLogger(PlanTilesManager.class);
    public static PlanTilesManager instance;
    private final List<PlanTile> selectedTiles;
    private PlanTilesManager() {
        this.selectedTiles = new ArrayList<>();
    }

    public static PlanTilesManager getInstance(){
        if(instance==null){
            instance = new PlanTilesManager();
        }
        return instance;
    }

    public void addTileToList(PlanTile tile){
        selectedTiles.add(tile);
    }
    public void removeTileFromList(PlanTile tile){
        selectedTiles.remove(tile);
    }
}
