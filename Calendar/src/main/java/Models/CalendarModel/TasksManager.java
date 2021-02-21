package Models.CalendarModel;

import Models.CalendarModel.AbstractFactories.TaskFactory;
import Models.CalendarModel.Days.Day;

import java.time.LocalDate;

public class TasksManager {

    private static TasksManager instance;
    private static TaskFactory taskFactory = new TaskFactory();

    public static TasksManager getInstance() {
        if (instance == null) {
            instance = new TasksManager();
        }
        return instance;
    }

    public void createNewTaskInCalendarAndDB(Day dayToWhichTaskIsAssigned, String className, String taskName, String details, LocalDate deadline) {
        taskFactory.createNewTask(dayToWhichTaskIsAssigned, className, taskName, details, deadline);
    }

    private TasksManager() {
    }
}
