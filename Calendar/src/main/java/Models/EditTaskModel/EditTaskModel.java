package Models.EditTaskModel;

import Controllers.DialogControllers.EditTaskDialogController;
import Models.CalendarModel.Days.Day;
import Models.CalendarModel.Tasks.Task;
import Models.CalendarModel.Tasks.WorkTask;
import javafx.scene.control.Alert;

import java.time.LocalDate;

public class EditTaskModel {

    EditTaskDialogController controller;
    private static Task selectedTask;
    private final Day dayOfSelectedTask = selectedTask.getAssignedToDay();

    public EditTaskModel(EditTaskDialogController controller) {
        this.controller = controller;
    }

    public void implementChanges(String newName, String newDetails, LocalDate newDeadline) {
        if (nameIsTaken(newName)) {
            createAlertOfNameTaken();
            return;
        }
        changeName(newName);
        changeDetails(newDetails);
        if (taskHasDeadline()) changeDeadline(newDeadline);
    }

    private void changeName(String newName) {
        selectedTask.editNameInApplicationAndDB(newName);
    }

    private void changeDetails(String newDetails) {
        selectedTask.editDetailsInApplicationAndDB(newDetails);

    }

    private void changeDeadline(LocalDate newDeadline) {
        ((WorkTask) selectedTask).editTaskDeadlineInApplicationAndDB(newDeadline);
    }

    private boolean nameIsTaken(String newName) {
        return dayOfSelectedTask.taskNameIsUsed(newName);
    }

    private void createAlertOfNameTaken() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Illegal name");
        alert.setHeaderText("Name of the task you have chosen is already used in day: " + dayOfSelectedTask.getDate());
        alert.setContentText("Task name has to be unique for a given day. Please insert one that is not being used");
        alert.showAndWait();
    }

    public LocalDate provideDeadline() {
        if (taskHasDeadline()) {
            WorkTask wt = (WorkTask) selectedTask;
            return wt.getDeadline();
        } else throw new IllegalArgumentException();
    }

    public boolean taskHasDeadline() {
        return selectedTask instanceof WorkTask;
    }

    public String provideDetails() {
        return selectedTask.getTaskName();
    }

    public String provideName() {
        return selectedTask.getTaskName();
    }

    public static void setSelectedTask(Task sTask) {
        selectedTask = sTask;
    }
}
