package Models.CalendarModel.Tile;

import CalendarMain.CalendarMain;
import Controllers.CalendarController;
import Models.CalendarModel.Datasources.TaskDatasource;
import Models.CalendarModel.Days.Day;
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
    private int selectedYear =  CalendarController.getInstance().getCurrentYearNum();
    private int selectedMonth =  CalendarController.getInstance().getCurrentMonthNum();
    private int dayOfMonth;
    private LocalDate checkedDay;

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
        Day clickedDay = CalendarMain.getDays().get(CalendarMain.getDayIndex(checkedDay));
        CalendarController.getInstance().setSelectedDay(clickedDay);
        CalendarController.getInstance().getShowDay().setText("Plans for: " + clickedDay.getDate());

        if(clickedDay.getTodayTasks().size()<clickedDay.getTaskNumber())  TaskDatasource.getInstance().loadTasksOfDay(clickedDay);

        Pane TimePane = CalendarController.getInstance().getTimePane();
        Pane PlanningPane = CalendarController.getInstance().getPlanningPane();

        CalendarController.getInstance().getDetailsTabPane().setDisable(false);

        TimePane.getChildren().remove(0, TimePane.getChildren().size());
        PlanningPane.getChildren().remove(0, PlanningPane.getChildren().size());
        CalendarController.getInstance().configureTasksTable();
        CalendarController.getInstance().configureTimeTiles();
        CalendarController.getInstance().configurePlanTiles();
        CalendarController.getInstance().populateComboBox();


    }
}
