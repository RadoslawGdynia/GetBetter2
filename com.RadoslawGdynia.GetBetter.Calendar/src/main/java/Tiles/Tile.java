package Tiles;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class Tile extends StackPane  {
    private final Pane root;
    private final String ID;




    public Tile(Pane tPane, String id, String display, int xPixels, int yPixels, ITileModifier modifier) {

        this.ID = id;
        this.root = tPane;
        tPane.getChildren().add(this);
        Rectangle shape = modifier.modifyLayout(xPixels, yPixels);
        Text text = modifier.modifyText(display);

        getChildren().addAll(shape, text);

        this.setOnMouseClicked(event -> modifier.handleClick());



    }


}
