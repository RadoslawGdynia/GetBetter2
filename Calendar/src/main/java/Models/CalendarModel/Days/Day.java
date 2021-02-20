package Models.CalendarModel.Days;

import Models.CalendarModel.Datasources.CalendarDatasource;
import Models.CalendarModel.Datasources.TaskDatasource;
import Models.CalendarModel.Tasks.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;

public class Day {

    public static final Logger log = LoggerFactory.getLogger(Day.class);
    private final LocalDate date;
    private final ObservableList<Task> todayTasks;
    private final int dayID;
    private static int GlobalID = 1;
    private int taskNumber;


    public Day(LocalDate date) {
        this.date = date;
        this.dayID = GlobalID;
        GlobalID++;
        todayTasks = FXCollections.observableArrayList();
        try {
            CalendarMain.getInstance().addDay(this);
        } catch (IOException e) {
            log.error("Operation of addition of day {} to calendar have failed",this.getDate());
        }
    }

    public Day(int id, LocalDate date, int taskNumber) {
        this.date = date;
        this.dayID = id;
        this.taskNumber = taskNumber;
        GlobalID = id + 1;
        todayTasks = FXCollections.observableArrayList();
        try {
            CalendarMain.getInstance().addDay(this);
        } catch (IOException e) {
            log.error("Operation of addition of day {} to calendar have failed", this.getDate());
        }

    }


    public LocalDate getDate() {
        return date;
    }

    public int getDayID() {
        return dayID;
    }

    public int getTaskNumber() {
        return taskNumber;
    }

    public ObservableList<Task> getTodayTasks() {
        return todayTasks;
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
