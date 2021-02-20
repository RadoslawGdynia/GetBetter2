package Models.CalendarModel;

import Models.CalendarModel.CalendarAreasModels.DayPlanModel;
import Models.CalendarModel.CalendarAreasModels.TaskManagementModel;
import Models.CalendarModel.Days.Day;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class CalendarModel {


    private static CalendarModel instance;
    private Day selectedDay;
    private int currentMonthNum, currentYearNum, currentDayNum;

    private final TasksManager tasksManager = TasksManager.getInstance();
    private final CalendarDaysManager calendarDaysManager = CalendarDaysManager.getInstance();
    private final DayPlanModel dayPlanner = DayPlanModel.getInstance();
    private final TaskManagementModel taskManager = TaskManagementModel.getInstance();

    public static CalendarModel getInstance() {
        if(instance == null){
            instance = new CalendarModel();
        }
        return instance;
    }

    public void updateTaskManagingArea() {
        dayPlanner.configureTimeTiles();
        dayPlanner.configurePlanTiles();
        dayPlanner.populateComboBox();
    }

    public void setSelectedDay(Day dayClickedByUser){
        this.selectedDay = dayClickedByUser;
    }

    public void initializeCalendar(){
        selectedDay = calendarDaysManager.selectDayFromCalendar(LocalDate.now());
        currentMonthNum = selectedDay.getDate().getMonthValue();
        currentYearNum = selectedDay.getDate().getYear();
        currentDayNum = selectedDay.getDate().getDayOfMonth();
    }

    private CalendarModel() {
        calendarDaysManager.loadDaysFromDatabaseToCalendar();
        initializeCalendar();
    }
}
