package pl.RadoslawGdynia.Calendar.Factories;

import pl.RadoslawGdynia.Calendar.Calendar.GetBetterCalendar;
import pl.RadoslawGdynia.Calendar.Datasources.TaskDatasource;
import pl.RadoslawGdynia.Calendar.Day.Day;
import pl.RadoslawGdynia.Calendar.Task.Subtask;
import pl.RadoslawGdynia.Calendar.Task.Task;
import pl.RadoslawGdynia.Calendar.Task.TrivialTask;
import pl.RadoslawGdynia.Calendar.Task.WorkTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;

public class TaskFactory {

    public static final Logger log = LoggerFactory.getLogger(TaskFactory.class);

    public static void reloadTask(int dayID, String className, String name, String details, boolean finalised, int pointValue, LocalDate deadline, int deadlineCounter) throws NullPointerException {
        Day day = GetBetterCalendar.getDays().get(dayID - 1);
        try {
            if (className.equals("TrivialTask")) {
                new TrivialTask(day, name, details, finalised);

            } else if (className.equals("WorkTask")) {
                WorkTask task = new WorkTask(day, name, details, pointValue,deadline, finalised, deadlineCounter);
                TaskDatasource.getInstance().querySubtasks(task);

            } else {
                throw new IOException("Unknown Task class:" + className + ", impossible to add");
            }
        }catch( IOException e) {
            log.error("Error during task creation. \nMessage: " + e.getMessage());

        }
    }
    public static void createNewTask(Day day, String className, String taskName, String details, LocalDate deadline){
        if (className.equals("Trivial Task")){
            Task task = new TrivialTask(day, taskName, details);

        } else if (className.equals("Work Task")) {
            Task task = new WorkTask(day, taskName, details, deadline);
        } else {
            log.error("Unsupported form of Task cannot be created. Name of class user intended to create: " + className);
        }
    }
    public static Subtask reloadSubtask(int dayID, String name, String details, boolean finalised, WorkTask parent){
        return new Subtask(GetBetterCalendar.getDays().get(dayID-1),name,details,parent);
    }
    public static void createNewSubtask(Day day, WorkTask parent, String name, String details){
        Subtask subtask = new Subtask(day, name, details, parent);
    }
}
