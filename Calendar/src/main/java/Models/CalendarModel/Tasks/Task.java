package Models.CalendarModel.Tasks;

import Models.CalendarModel.Datasources.TaskDatasource;
import Models.CalendarModel.Days.Day;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public abstract class Task {

    public static final Logger log = LoggerFactory.getLogger(Task.class);
    private Day assignedToDay;
    private final SimpleStringProperty taskName = new SimpleStringProperty("");
    private final SimpleStringProperty details = new SimpleStringProperty("");
    private final SimpleBooleanProperty finalised = new SimpleBooleanProperty();

    private int pointValue;

    public Task(Day day, String name, String details) {
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

    public SimpleStringProperty getObservableTaskName() {
        return taskName;
    }

    public String getDetails() {
        return details.getValue();
    }

    public SimpleStringProperty getObservableDetails() {
        return details;
    }

    public boolean getFinalised() {
        return finalised.get();
    }

    public SimpleBooleanProperty getObservableFinalised() {
        return finalised;
    }

    public int getPointValue() {
        return pointValue;
    }

    public void setName(String taskName) {
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

    public void moveTask(Day day) {

        if (!(day.getDate().isBefore(LocalDate.now()))) {
            assignedToDay.removeTask(this);
            day.addTask(this);
            assignedToDay = day;
        } else {
            log.info("Models.CalendarModel.Tasks cannot be moved to the past");
        }
    }

    public boolean equals(Task obj) {
        if (obj == this) return true;
        return (this.getTaskName().equals(obj.getTaskName())) && (this.assignedToDay.getDate().equals(obj.assignedToDay.getDate()));
    }

    public void editDetailsInApplicationAndDB(String newDetails) {
        setDetails(newDetails);
        TaskDatasource.getInstance().editTaskDetailsInDB(this, newDetails);
    }

    public void editNameInApplicationAndDB(String newName) {
        setName(newName);
        TaskDatasource.getInstance().editTaskNameInDB(this, newName);
    }

    @Override
    public String toString() {
        return this.getTaskName();
    }
}
