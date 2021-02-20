package Models.CalendarModel.Tiles;

import Controllers.CalendarController;
import Models.CalendarModel.Tiles.TilesLogic.CalendarTileLogic;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.time.LocalDate;

public class CalendarTile implements ITileModifier {

    private final LocalDate checkedDay;
    CalendarTileLogic tileController = CalendarTileLogic.getInstance();


    public CalendarTile(int day) {
        int selectedYear = CalendarController.getInstance().getCurrentYearNum();
        int selectedMonth = CalendarController.getInstance().getCurrentMonthNum();
        this.checkedDay  = LocalDate.of(selectedYear, selectedMonth, day);
    }

    public Rectangle modifyLayout(int xPixels, int yPixels) {

        Rectangle border = new Rectangle(xPixels, yPixels);
        border.setStroke(Color.BLACK);

        LocalDate today = LocalDate.now();
        if(checkedDay.isBefore(today)){
            border.setFill(Color.LIGHTGRAY);
        } else if (checkedDay.isEqual(today)){
            border.setFill(Color.DARKCYAN);
        } else {
            border.setFill(Color.DARKGREEN);
        }
        return border;
    }
    public Text modifyText( String display) {

        Text text = new Text(display);
        text.setFont(Font.font("Calibri", 30));
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }

    public void handleClick() {
        tileController.calendarTileClick(checkedDay);
    }
}
