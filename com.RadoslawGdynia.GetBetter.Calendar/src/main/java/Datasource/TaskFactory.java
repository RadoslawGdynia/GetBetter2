package Datasource;

import CalendarControl.GetBetterCalendar;
import Day.Day;
import Task.Subtask;
import Task.TrivialTask;
import Task.WorkTask;

import java.io.IOException;
import java.time.LocalDate;

public class TaskFactory {

    public static void createTask(int dayID, String className, String name, String details, boolean finalised, int pointValue, LocalDate deadline, int deadlineCounter) throws NullPointerException {
        Day day = GetBetterCalendar.getDays().get(dayID - 1);
        try {
            if (className.equals("TrivialTask")) {
                new TrivialTask(day, name, details, finalised);

            } else if (className.equals("WorkTask")) {
                WorkTask task = new WorkTask(day, name, details, pointValue,deadline, finalised, deadlineCounter);
                CalendarDatasource.getInstance().querySubtasks(task);

            } else {
                throw new IOException("Unknowk Task class:" + className + ", impossible to add");
            }
        }catch( IOException e) {
            System.out.println("Error during task creation. \nMessage: " + e.getMessage());

        }
    }
    public static Subtask createSubtask(int dayID, String name, String details, boolean finalised, WorkTask parent){
        return new Subtask(GetBetterCalendar.getDays().get(dayID-1),name,details,parent);

    }
}
