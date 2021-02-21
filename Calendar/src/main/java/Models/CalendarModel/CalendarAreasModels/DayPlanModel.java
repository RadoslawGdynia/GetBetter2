package Models.CalendarModel.CalendarAreasModels;

import Controllers.CalendarController;
import Models.CalendarModel.AbstractFactories.TileFactory;
import Models.CalendarModel.Days.Day;
import Models.CalendarModel.Tasks.Task;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import lombok.Setter;

public class DayPlanModel {

    private static CalendarController calendarController = CalendarController.getInstance();
    @Setter
    private ObservableList<Task> taskList;

    public DayPlanModel(ObservableList<Task> taskList) {
        this.taskList = taskList;
    }

    public void loadDayPlans(Day selectedDay){
        if(calendarController != null) {
            configureTimeTiles();
            configurePlanTiles();
            populateComboBox();
        }
    }

    private void configureTimeTiles() {
        Pane timePane = calendarController.getTimePane();
        final int TILES_NUMBER = 18;
        TileFactory.createSetOfTiles("TimeTile", timePane, TILES_NUMBER);

    }

    private void configurePlanTiles() {
        Pane planningPane = calendarController.getPlanningPane();
        final int TILES_NUMBER = 72;
        TileFactory.createSetOfTiles("PlanTile", planningPane, TILES_NUMBER);
    }

    private void populateComboBox() {
        ComboBox<Task> taskSelectionCombo = calendarController.getTaskSelectionCombo();
        taskSelectionCombo.setItems(taskList);
    }

    public void handleAssignTaskButton() {

    }

    public void handleClearTileButton() {

    }
}
