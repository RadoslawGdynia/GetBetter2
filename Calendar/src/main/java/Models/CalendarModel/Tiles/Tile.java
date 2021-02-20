package Models.CalendarModel.Tiles;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tile extends StackPane  {

    public static final Logger log = LoggerFactory.getLogger(Tile.class);
    private final Pane root;
    private final int ID;

    public Tile(Pane tPane, int id, String display, int xPixels, int yPixels, ITileModifier modifier) {

        this.ID = id;
        this.root = tPane;
        tPane.getChildren().add(this);
        Rectangle shape = modifier.modifyLayout(xPixels, yPixels);
        Text text = modifier.modifyText(display);

        getChildren().addAll(shape, text);

        this.setOnMouseClicked(event -> modifier.handleClick());



    }


}
