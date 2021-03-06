package Models.CalendarModel.AbstractFactories;

import Models.CalendarModel.CalendarDaysManager;
import Models.CalendarModel.Datasources.TaskDatasource;
import Models.CalendarModel.Days.Day;
import Models.CalendarModel.Tasks.Subtask;
import Models.CalendarModel.Tasks.Task;
import Models.CalendarModel.Tasks.TrivialTask;
import Models.CalendarModel.Tasks.WorkTask;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
public class TaskFactory {


    public void reloadTask(int dayID, String className, String name, String details, boolean finalised, int pointValue, LocalDate deadline, int deadlineCounter) throws NullPointerException {
        Day day = CalendarDaysManager.getInstance().selectDayFromCalendarByIndex(dayID - 1);
        try {
            if (className.equals("TrivialTask")) {
                new TrivialTask(day, name, details, finalised);

            } else if (className.equals("WorkTask")) {
                WorkTask task = new WorkTask(day, name, details, pointValue, deadline, finalised, deadlineCounter);
                TaskDatasource.getInstance().querySubtasks(task);

            } else {
                throw new IOException("Unknown Task class:" + className + ", impossible to add");
            }
        } catch (IOException e) {
            log.error("Error during task creation. \nMessage: {}", e.getMessage());

        }
    }

    public void createNewTask(Day day, String className, String taskName, String details, LocalDate deadline) {
        if (className.equals("Trivial Task")) {
            Task task = new TrivialTask(day, taskName, details);

        } else if (className.equals("Work Task")) {
            Task task = new WorkTask(day, taskName, details, deadline);
        } else {
            log.error("Unsupported form of Task cannot be created. Name of class user intended to create: {}", className);
        }
    }

    public Subtask reloadSubtask(int dayID, String name, String details, boolean finalised, WorkTask parent) {
        Day dayOfSubtask = CalendarDaysManager.getInstance().selectDayFromCalendarByIndex(dayID - 1);
        return new Subtask(dayOfSubtask, name, details, parent);
    }

    public static void createNewSubtask(Day day, WorkTask parent, String name, String details) {
        Subtask subtask = new Subtask(day, name, details, parent);
    }
}
