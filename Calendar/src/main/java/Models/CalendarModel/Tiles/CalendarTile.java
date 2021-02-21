package Models.CalendarModel.Tiles;

import Controllers.CalendarController;
import Models.CalendarModel.CalendarDaysManager;
import Models.CalendarModel.CalendarModel;
import Models.CalendarModel.Datasources.TaskDatasource;
import Models.CalendarModel.Days.Day;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class CalendarTile implements ITileModifier {

    private final LocalDate dayAssignedToTile;

    public CalendarTile(int day) {
        int selectedYear = CalendarModel.getInstance().getCurrentYearNum();
        int selectedMonth = CalendarModel.getInstance().getCurrentMonthNum();
        this.dayAssignedToTile = LocalDate.of(selectedYear, selectedMonth, day);
    }

    public Rectangle modifyLayout(int xPixels, int yPixels) {

        Rectangle border = new Rectangle(xPixels, yPixels);
        border.setStroke(Color.BLACK);

        LocalDate today = LocalDate.now();
        if (dayAssignedToTile.isBefore(today)) {
            border.setFill(Color.LIGHTGRAY);
        } else if (dayAssignedToTile.isEqual(today)) {
            border.setFill(Color.DARKCYAN);
        } else {
            border.setFill(Color.DARKGREEN);
        }
        return border;
    }

    public Text modifyText(String display) {

        Text text = new Text(display);
        text.setFont(Font.font("Calibri", 30));
        text.setTextAlignment(TextAlignment.CENTER);
        return text;
    }

    public void handleClick() {
        Day clickedDay = CalendarDaysManager.getInstance().selectDayFromCalendarByDate(dayAssignedToTile);
        log.info("Day assigned to this tile: {}", dayAssignedToTile);

        if (clickedDay.getTodayTasks().size() < clickedDay.getTaskNumber())
            TaskDatasource.getInstance().loadTasksOfDay(clickedDay);

        CalendarModel.getInstance().setSelectedDay(clickedDay);

        CalendarController.getInstance().handleCalendarTileClick("Plans for: " + clickedDay.getDate());
    }
}
