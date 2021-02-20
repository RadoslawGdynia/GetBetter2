package Models.CalendarModel.CalendarAreasModels;

import Controllers.CalendarController;
import Models.CalendarModel.AbstractFactories.TileFactory;
import Models.CalendarModel.Days.Day;
import javafx.scene.layout.Pane;

public class DayPlanModel {
   private static DayPlanModel instance;
   private static CalendarController calendarController = CalendarController.getInstance();

    public static DayPlanModel getInstance(){
        if (instance == null) {
            instance = new DayPlanModel();
        }
        return instance;
    }

    public void configureTimeTiles() {
        Pane timePane = calendarController.getTimePane();
        final int TILES_NUMBER = 18;
        TileFactory.createSetOfTiles("TimeTile", timePane, TILES_NUMBER);

    }
    public void configurePlanTiles() {
        Pane planningPane = calendarController.getPlanningPane();
        final int TILES_NUMBER = 72;
        TileFactory.createSetOfTiles("PlanTile", planningPane, TILES_NUMBER);
    }
    public void populateComboBox() {
       // ComboBox<Task> taskSelectionCombo = calendarController.getTaskComboBox();
        Day selectedDay = calendarController.getSelectedDay();
       // taskSelectionCombo.setItems(selectedDay.getTodayTasks());
    }
    public void handleAssignTaskButton(){

    }
    public void handleClearTileButton(){

    }
    private DayPlanModel() {
    }
}
