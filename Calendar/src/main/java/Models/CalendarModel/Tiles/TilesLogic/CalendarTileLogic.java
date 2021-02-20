package Models.CalendarModel.Tiles.TilesLogic;

import Controllers.CalendarController;
import Models.CalendarModel.CalendarDaysManager;
import Models.CalendarModel.CalendarModel;
import Models.CalendarModel.Datasources.TaskDatasource;
import Models.CalendarModel.Days.Day;
import javafx.scene.layout.Pane;

import java.time.LocalDate;

public class CalendarTileLogic {

    public void calendarTileClick(LocalDate checkedDay){

        Day clickedDay = CalendarDaysManager.getInstance().selectDayFromCalendar(checkedDay);
        CalendarModel.getInstance().setSelectedDay(clickedDay);
        CalendarController.getInstance().getShowDay().setText("Plans for: " + clickedDay.getDate());

        if(clickedDay.getTodayTasks().size()<clickedDay.getTaskNumber())  TaskDatasource.getInstance().loadTasksOfDay(clickedDay);

        Pane TimePane = CalendarController.getInstance().getTimePane();
        Pane PlanningPane = CalendarController.getInstance().getPlanningPane();

        CalendarController.getInstance().getDetailsTabPane().setDisable(false);

        TimePane.getChildren().remove(0, TimePane.getChildren().size());
        PlanningPane.getChildren().remove(0, PlanningPane.getChildren().size());
        CalendarController.getInstance().populateDayAndTaskData();
    }

     private CalendarTileLogic(){

     }


}
