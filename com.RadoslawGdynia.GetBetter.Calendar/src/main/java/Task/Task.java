package Task;

import Day.Day;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public abstract class Task {

    public static final Logger log = LoggerFactory.getLogger(Task.class);
    private Day assignedToDay;
    private SimpleStringProperty taskName = new SimpleStringProperty("");
    private SimpleStringProperty details = new SimpleStringProperty("");
    private SimpleBooleanProperty finalised = new SimpleBooleanProperty();

    private int pointValue;

    public Task(Day day, String name, String details){
        this.taskName.set(name);
        this.details.set(details);
        this.assignedToDay = day;
        this.finalised.set(false);
        this.pointValue = 1;

    }

    protected Task() {
    }

    public Day getAssignedToDay() {
        return assignedToDay;
    }

    public String getTaskName() {
        return taskName.getValue();
    }

    public SimpleStringProperty getVisibleTaskName() {
        return taskName;
    }

    public String getDetails() {
        return details.getValue();
    }

    public SimpleStringProperty getVisibleDetails() {
        return details;
    }

    public boolean getFinalised() {
        return finalised.get();
    }

    public SimpleBooleanProperty getVisibleFinalised() {
        return finalised;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setTaskName(String taskName) {
        this.taskName.set(taskName);
    }

    public void setDetails(String details) {
        this.details.set(details);
    }

    public void setAssignedToDay(Day assignedToDay) {
        this.assignedToDay = assignedToDay;
    }

    public boolean setFinalised(boolean finalised) {
        this.finalised.set(finalised);
        return true;
    }

    public void setPointValue(int value) {
        this.pointValue = value;
    }

    public void editTaskName(String newName){
        this.taskName.set(newName);
    }
    public void editTaskDetails(String newDetails){
        this.details.set(newDetails);
    }
    public void moveTask(Day day){

        if(!(day.getDate().isBefore(LocalDate.now()))){
            assignedToDay.removeTask(this);
            day.addTask(this);
            assignedToDay = day;
        }
        else {
            System.out.println("Tasks cannot be moved to the past");
        }
    }

    public boolean equals(Task obj) {
        if(obj==this) return true;
        return (this.getTaskName().equals(obj.getTaskName())) && (this.assignedToDay.getDate().equals(obj.assignedToDay.getDate()));
    }

    @Override
    public String toString() {
        StringBuilder textTask = new StringBuilder();

        textTask.append(this.getClass().getName());
        textTask.append(";");
        textTask.append(assignedToDay.toString().trim());
        textTask.append(";");
        textTask.append(taskName.getValue());
        textTask.append(";");
        textTask.append(details.getValue());
        textTask.append(";");
        textTask.append(finalised.getValue());
        textTask.append(";");

        return textTask.toString();
    }
}
