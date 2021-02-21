package Models.CalendarModel.Tasks;

import Models.CalendarModel.Datasources.TaskDatasource;
import Models.CalendarModel.Days.Day;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ConcurrentModificationException;
import java.util.Objects;


public class WorkTask extends Task implements Comparable<WorkTask> {

    public static final Logger log = LoggerFactory.getLogger(WorkTask.class);

    //Visible properties
    private LocalDate deadline;
    private ObservableList<Subtask> subtasks;

    //Hidden properties

    private int deadlineChangeCounter;

    public WorkTask(Day day, String taskName, String details, LocalDate deadline) {
        super(day, taskName, details);
        this.deadline = deadline;
        this.subtasks = FXCollections.observableArrayList();
        this.setPointValue(1 + (int) (Math.random() * 10));
        this.deadlineChangeCounter = 0;
        day.addTask(this);

    }

    public WorkTask(Day day, String taskName, String details, int pointValue, LocalDate deadline, boolean finalised, int deadlineChangeCounter) {
        super(day, taskName, details);
        this.deadline = deadline;
        this.setFinalised(finalised);
        this.subtasks = FXCollections.observableArrayList();

        this.setPointValue(pointValue);
        this.deadlineChangeCounter = deadlineChangeCounter;
        day.addTask(this);
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public ObservableList<Subtask> getSubtasks() {
        return subtasks;
    }

    public int getDeadlineChangeCounter() {
        return deadlineChangeCounter;
    }

    @Override
    public boolean setFinalised(boolean done) {
        if (done) {
            for (Task sub : subtasks) {
                if (!sub.getFinalised()) {
                    log.info("One or more subtasks are not fulfilled. Finish all subtasks in order to close this task.");
                    return false;
                }
            }
            super.setFinalised(true);
            return true;
        }
        return false;
    }

    private void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }


    public void editTaskDeadlineInApplicationAndDB(LocalDate newDeadline) {
        if (this.deadlineChangeCounter <= 2) {
            this.deadlineChangeCounter++;
            this.setDeadline(newDeadline);
            TaskDatasource.getInstance().editTaskDEADLINEInDB(this, newDeadline);
            log.info("Deadline date was changed to: {}. Number of possible deadline changes left: {}", newDeadline, (3 - deadlineChangeCounter));

        } else {
            log.info("Maximum number of deadline changes has been reached. You have to do it now!");
        }
    }

    public void addSubtask(Subtask added) {
        try {
            for (Subtask sub : this.subtasks) {
                if (sub.equals(added)) {
                    log.info("Task {} already has subtask {}\nOperation rejected.", this.getTaskName(), added.getTaskName());
                    return;
                }
            }
            this.subtasks.add(added);
            if (TaskDatasource.getInstance().subtaskNotInDB(this, added.getTaskName()))
                TaskDatasource.getInstance().addSubtaskToDB(this, added);

        } catch (NullPointerException e) {
            log.error("Problem with subtask addition");
        }
    }

    public void cancelSubtask(Subtask toDelete) {
        try {
            int initialSubtasksNumber = this.getSubtasks().size();
            if (this.subtasks.isEmpty()) {
                log.info("List of subtasks for this task is empty. Operation rejected");
            } else {
                for (Subtask verification : this.subtasks) {
                    if (toDelete.equals(verification)) {
                        this.subtasks.remove(verification);
                        log.info("Subtask " + toDelete.getTaskName() + " was deleted from " + this.getTaskName());
                        return;
                    }
                }
                if (this.getSubtasks().size() == initialSubtasksNumber) {
                    log.info("{} does not have subtask {}. Operation rejected.", this.getTaskName(), toDelete.getTaskName());
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println();
        } catch (Exception e) {
            log.error("Error during deletion of subtask: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkTask)) return false;
        WorkTask task = (WorkTask) o;
        return this.deadlineChangeCounter == task.deadlineChangeCounter &&
                this.getTaskName().equals(task.getTaskName()) &&
                Objects.equals(this.getDetails(), task.getDetails()) &&
                this.deadline.equals(task.deadline);
    }

    @Override
    public int compareTo(WorkTask o) {
        int timeForThis = LocalDate.now().compareTo(this.getDeadline());
        int timeForO = LocalDate.now().compareTo(o.getDeadline());
        return Integer.compare(timeForO, timeForThis);
    }
}
