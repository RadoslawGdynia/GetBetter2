package Tiles;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeTile implements ITileModifier {

    public static final Logger log = LoggerFactory.getLogger(TimeTile.class);

    public TimeTile() {
    }

    @Override
    public Rectangle modifyLayout(int xPixels, int yPixels) {
        Rectangle border = new Rectangle(xPixels, yPixels);
        border.setFill(Color.LIGHTGRAY);
        border.setStroke(Color.BLACK);
        return border;
    }

    @Override
    public Text modifyText(String display) {
        Text text = new Text(display);
        text.setFont(Font.font("Calibri", 10));
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }

    @Override
    public void handleClick() {

    }
}
