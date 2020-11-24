package Task;

import Datasource.TaskDatasource;
import Day.Day;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ConcurrentModificationException;
import java.util.Objects;


/**
 * Object <code>CustomTask</code> represents things to do.
 */
public class WorkTask extends Task implements Comparable<WorkTask> {

    public static final Logger log = LoggerFactory.getLogger(WorkTask.class);

    //Visible properties
    private LocalDate deadline;
    private ObservableList<Subtask> subtasks;

    //Hidden properties

    private int deadlineChangeCounter;

    /**
     * Creates new Task.Task Object in the program. Only a few of fields are possible for user to specify, others are internally specified
     */
    public WorkTask(Day day, String taskName, String details, LocalDate deadline) {
        super(day, taskName, details);
        this.deadline = deadline;
        this.subtasks = FXCollections.observableArrayList();
        this.setPointValue(1 + (int) (Math.random() * 10));
        this.deadlineChangeCounter = 0;
        day.addTask(this);

    }

    /**
     * Constructor with all fields being specified is necessary for loading the data from the file and recreating previous list.
     */
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

    /**
     * Allows to change the deadline of a given WorkTask, but a limited number of times
     * @param date provides a new date for deadline
     * deadlineChangeCounter is a private counter that limits number of changes to value hard-coded in this method.
     */
    public void editTaskDeadline(LocalDate date) {
        if (this.deadlineChangeCounter <= 2) {
            this.deadlineChangeCounter++;
            this.setDeadline(date);
            System.out.println("Data deadline została zmieniona na " + date.toString() + ". Ilość możliwych do przeprowadzenia zmian: " + (3 - deadlineChangeCounter));

        } else {
            System.out.println("Przekroczono maksymalną liczbę zmian deadline. Operacja odrzucona.");
        }
    }

    /**
     *
     */
    public void addSubtask(Subtask added) {
        try {
            for (Subtask sub : this.subtasks) {
                if (sub.equals(added)) {
                    System.out.println("Zadanie " + this.getTaskName() + " już zawiera " + added.getTaskName() +
                            "\nOperacja przerwana.");
                    return;
                }
            }
            this.subtasks.add(added);
            TaskDatasource.getInstance().addSubtaskToDB(this, added);

        } catch (NullPointerException e) {
            System.out.println("Problem przy dodawaniu podzadania");
        }
    }

    /**
     * Method allows to remove the subtask from list of subtasks of this Task.Task.
     */
    public void cancelSubtask(Subtask toDelete) {
        try {
            int initialSubtasksNumber = this.getSubtasks().size();
            if (this.subtasks.isEmpty()) {
                System.out.println("List of subtasks for this task is empty. Operation rejected");
            } else {
                for (Subtask verification : this.subtasks) {
                    if (toDelete.equals(verification)) {
                        this.subtasks.remove(verification);
                        System.out.println("Subtask " + toDelete.getTaskName() + " was deleted from " + this.getTaskName());
                        return;
                    }
                }
                if (this.getSubtasks().size() == initialSubtasksNumber) {
                    System.out.println(this.getTaskName() + " does not have subtask " + toDelete.getTaskName() + ". Operation rejected.");
                }
            }
        } catch (ConcurrentModificationException e) {
            System.out.println();
        } catch (Exception e) {
            System.out.println("Error during deletion of subtask: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *
     */
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

    /**
     * Following method allows to compare Tasks to one another.
     * At the beginning only factors taken into account are closeness to deadline, but it will be extended by addition of
     * possibility of stating a status of task, which will describe its importance.
     */
    @Override
    public int compareTo(WorkTask o) {
        int timeForThis = LocalDate.now().compareTo(this.getDeadline());
        int timeForO = LocalDate.now().compareTo(o.getDeadline());
        return Integer.compare(timeForO, timeForThis);
    }
}
