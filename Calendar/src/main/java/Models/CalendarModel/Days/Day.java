package Models.CalendarModel.Days;

import Models.CalendarModel.CalendarDaysManager;
import Models.CalendarModel.Datasources.CalendarDatasource;
import Models.CalendarModel.Datasources.TaskDatasource;
import Models.CalendarModel.Tasks.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;

@Slf4j
@Getter
public class Day {

    private static int GlobalID = 1;
    private final LocalDate date;
    private final ObservableList<Task> todayTasks;
    private final int dayID;
    private int taskNumber;


    public Day(LocalDate date) {
        this.date = date;
        this.dayID = GlobalID;
        GlobalID++;
        todayTasks = FXCollections.observableArrayList();
        try {
            CalendarDaysManager.getInstance().addDayToCalendar(this);
        } catch (IOException e) {
            log.error("Operation of addition of day {} to calendar have failed", this.getDate());
        }
    }

    public Day(int id, LocalDate date, int taskNumber) {
        this.date = date;
        this.dayID = id;
        this.taskNumber = taskNumber;
        GlobalID = id + 1;
        todayTasks = FXCollections.observableArrayList();
        try {
            CalendarDaysManager.getInstance().addDayToCalendar(this);
        } catch (IOException e) {
            log.error("Operation of addition of day {} to calendar have failed", this.getDate());
        }

    }

    public void addTask(Task newTask) {
        if (findTask(newTask)) {
            log.info("Task {} is already in this day. Operation rejected.", newTask.getTaskName());
        } else
            todayTasks.add(newTask);
        if (TaskDatasource.getInstance().taskNotInDB(this.getDayID(), newTask.getTaskName())) {
            TaskDatasource.getInstance().addTaskToDB(newTask);
            this.taskNumber++;
            CalendarDatasource.getInstance().updateDayTaskNumber(this, taskNumber);
        }

        log.info("Task {} was added to: {}", newTask.getTaskName(), this.getDate());

    }

    public boolean removeTask(Task taskToDelete) {
        if (todayTasks.isEmpty()) {
            log.info("Task list for the day {} is empty, deletion impossible. Rejected.", this.getDate());
            return false;
        } else {
            if (findTask(taskToDelete)) {
                todayTasks.remove(taskToDelete);
                TaskDatasource.getInstance().deleteTaskFromDB(taskToDelete);
                log.info("Task {} was deleted", taskToDelete.getTaskName());
                this.taskNumber--;
                CalendarDatasource.getInstance().updateDayTaskNumber(this, taskNumber);
                return true;
            } else {
                log.info("Task {} is not on the task list. Rejected.", taskToDelete.getTaskName());
                return false;
            }
        }
    }

    private boolean findTask(Task sought) {
        for (Task task : todayTasks) {
            if (sought.equals(task)) {
                return true;
            }
        }
        return false;
    }

    public boolean taskNameIsUsed(String sought) {
        for (Task task : todayTasks) {
            if (sought.equals(task.getTaskName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {

        return this.date +
                " number of tasks: " +
                this.taskNumber;
    }
}
