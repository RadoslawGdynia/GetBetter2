package Models.CalendarModel.Tile;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlanTile implements ITileModifier {
    private Color color = Color.PLUM;
    private boolean selected = false;

    public static final Logger log = LoggerFactory.getLogger(PlanTile.class);

    public PlanTile() {
    }

    @Override
    public Rectangle modifyLayout(int xPixels, int yPixels) {
        Rectangle border = new Rectangle(xPixels, yPixels);
        border.setFill(color);
        border.setStroke(Color.BLACK);
        return border;
    }

    @Override
    public Text modifyText(String display) {
        Text text = new Text(display);
        text.setFont(Font.font("Calibri", 8));
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }
    public void setColor(String color){
        this.color = Color.valueOf(color);
    }
    public void setTileText(String text){

    }

    @Override
    public void handleClick() {
        this.selected = !selected;
        if(this.selected) {
            PlanTilesManager.getInstance().addTileToList(this);
        } else {
            PlanTilesManager.getInstance().removeTileFromList(this);
        }
    }
}