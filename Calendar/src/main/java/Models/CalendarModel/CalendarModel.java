package Models.CalendarModel;

import Models.CalendarModel.CalendarAreasModels.DayPlanModel;
import Models.CalendarModel.Days.Day;
import Models.CalendarModel.Tasks.Task;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Slf4j
public class CalendarModel {

    @Getter
    private static final CalendarModel instance = new CalendarModel();
    private static Day selectedDay;
    @Getter
    private int currentMonthNum, currentYearNum, currentDayNum;

    private final TasksManager tasksManager = TasksManager.getInstance();
    private final CalendarDaysManager calendarDaysManager = CalendarDaysManager.getInstance();
        private final DayPlanModel dayPlanner;


    public void updateTaskManagingArea() {
        dayPlanner.setTaskList(this.provideTasksOfSelectedDay());
      //  dayPlanner.loadDayPlans(selectedDay);
    }

    public void setSelectedDay(Day dayClickedByUser) {
        selectedDay = dayClickedByUser;
        log.info("Date of selected day changed to: {}", selectedDay.getDate());
    }

    public void initializeCalendar() {
        selectedDay = calendarDaysManager.selectDayFromCalendarByDate(LocalDate.now());
        currentMonthNum = selectedDay.getDate().getMonthValue();
        currentYearNum = selectedDay.getDate().getYear();
        currentDayNum = selectedDay.getDate().getDayOfMonth();


    }

    //===== Day-connected methods =====
    public LocalDate provideDateOfSelectedDay() {
        return selectedDay.getDate();
    }

    public String provideCurrentlySelectedMonthName() {
        return LocalDate.of(currentYearNum,currentMonthNum, currentDayNum).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

    }

    public String provideCurrentlySelectedYearValue() {
        return String.valueOf(currentYearNum);
    }

    public void decreaseCurrentMonthBy1() {
        currentMonthNum--;
    }

    public void increaseCurrentMonthBy1() {
        currentMonthNum++;
    }

    public void changeCurrentMonthFromJanuaryToDecember() {
        currentMonthNum = 12;
        currentYearNum--;
    }

    public void changeCurrentMonthFromDecemberToJanuary() {
        currentMonthNum = 1;
        currentYearNum++;
    }

    //===== Task-connected methods =====

    public ObservableList<Task> provideTasksOfSelectedDay() {
        return calendarDaysManager.provideTasksForDay(selectedDay);
    }

    public void removeTaskFromSelectedDay(Task taskToDelete) {
        selectedDay.removeTask(taskToDelete);
    }

    public void createTaskForSelectedDay(String className, String taskName, String details, LocalDate deadline) {
        tasksManager.createNewTaskInCalendarAndDB(selectedDay, className, taskName, details, deadline);
    }


    //===== Other methods =====

    private CalendarModel() {
        calendarDaysManager.loadDaysFromDatabaseToCalendar();
        initializeCalendar();
        dayPlanner = new DayPlanModel(selectedDay.getTodayTasks());
    }



}
