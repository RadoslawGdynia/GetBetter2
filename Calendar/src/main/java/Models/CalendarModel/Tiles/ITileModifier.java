package Models.CalendarModel.Tiles;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public interface ITileModifier {

    Rectangle modifyLayout(int xPixels, int yPixels);
    Text modifyText(String display);
    void handleClick();
}
