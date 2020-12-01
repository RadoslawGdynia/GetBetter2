package Tiles;

import Calendar.GetBetterCalendar;
import Controllers.GetBetterCalendarController;
import Datasources.TaskDatasource;
import Day.Day;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class CalendarTile implements ITileModifier {

    public static final Logger log = LoggerFactory.getLogger(CalendarTile.class);
    int selectedYear =  GetBetterCalendarController.getInstance().getCurrentYearNum();
    int selectedMonth =  GetBetterCalendarController.getInstance().getCurrentMonthNum();
    int dayOfMonth;
    LocalDate checkedDay;

    public CalendarTile(int day) {
        this.dayOfMonth = day;
        this.checkedDay  = LocalDate.of(selectedYear, selectedMonth, dayOfMonth);
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
        Day clickedDay = GetBetterCalendar.getDays().get(GetBetterCalendar.getDayIndex(checkedDay));
        GetBetterCalendarController.getInstance().setSelectedDay(clickedDay);
        GetBetterCalendarController.getInstance().getShowDay().setText("Plans for: " + clickedDay.getDate());

        if(clickedDay.getTodayTasks().size()<clickedDay.getTaskNumber())  TaskDatasource.getInstance().loadTasksOfDay(clickedDay);

        Pane TimePane = GetBetterCalendarController.getInstance().getTimePane();
        Pane PlanningPane = GetBetterCalendarController.getInstance().getPlanningPane();

        GetBetterCalendarController.getInstance().getDetailsTabPane().setDisable(false);

        TimePane.getChildren().remove(0, TimePane.getChildren().size());
        PlanningPane.getChildren().remove(0, PlanningPane.getChildren().size());
        GetBetterCalendarController.getInstance().configureTasksTable();
        GetBetterCalendarController.getInstance().configureTimeTiles();
        GetBetterCalendarController.getInstance().configurePlanTiles();


    }
}
