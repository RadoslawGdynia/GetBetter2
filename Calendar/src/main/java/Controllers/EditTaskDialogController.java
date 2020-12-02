package Controllers;

import Datasources.TaskDatasource;
import Day.Day;
import Task.Task;
import Task.WorkTask;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class EditTaskDialogController {

    public static final Logger log = LoggerFactory.getLogger(EditTaskDialogController.class);

    private static Task selectedTask;

    @FXML
    Label ETTaskNameLabel;
    @FXML
    TextField ETTaskNameField;
    @FXML
    Label TaskDetailsLabel;
    @FXML
    TextField ETDetailsText;
    @FXML
    Label taskDeadlineLabel;
    @FXML
    DatePicker ETDatePicker;
    @FXML
    Button cancelButton;
    @FXML
    Button applyButton;


    public static void setSelectedTask(Task sTask) {
        selectedTask = sTask;
    }

    public void initialize() {
        ETDetailsText.appendText(selectedTask.getDetails());
        ETTaskNameField.setText(selectedTask.getTaskName());
        taskDeadlineLabel.setVisible(false);
        ETDatePicker.setVisible(false);
        ETDatePicker.setDisable(true);
        if(selectedTask.getClass().getSimpleName().equals("WorkTask")){
            WorkTask wt = (WorkTask) selectedTask;
            taskDeadlineLabel.setVisible(true);
            ETDatePicker.setVisible(true);
            ETDatePicker.setDisable(false);
            taskDeadlineLabel.setText("Current deadline for " + wt.getTaskName() + " is: " + wt.getDeadline() +
                ". If you would like to change it, choose a date from DatePicker below.");
        ETDatePicker.setValue(wt.getDeadline());
        }

     }

     public void handleApplyButton() {
        String newName = ETTaskNameField.getText().trim();
        String newDetails = ETDetailsText.getText().trim();
        LocalDate newDeadline = ETDatePicker.getValue();
        Day dayOfSelectedTask = selectedTask.getAssignedToDay();
        if((!selectedTask.getTaskName().equals(newName)) && dayOfSelectedTask.taskNameUsed(newName)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Illegal name");
            alert.setHeaderText("Name of the task you have chosen is already used in day: " + dayOfSelectedTask.getDate());
            alert.setContentText("Task name has to be unique for a given day. Please insert one that is not being used");
            alert.showAndWait();
            return;
        }
        TaskDatasource.getInstance().editTaskNameAndDetailsInDB(selectedTask, newName, newDetails);
        selectedTask.editTaskName(newName);
        selectedTask.editTaskDetails(newDetails);


        if(selectedTask.getClass().getSimpleName().equals("WorkTask")){
            WorkTask wt = (WorkTask) selectedTask;
            if(!newDeadline.isEqual(wt.getDeadline())) {
                wt.editTaskDeadline(newDeadline);
                TaskDatasource.getInstance().editTaskDEADLINEInDB(wt, newDeadline);
            }
        }
         Stage stage = (Stage) applyButton.getScene().getWindow();
         stage.close();

     }

     public void handleCancelButton() {
         Stage stage = (Stage) cancelButton.getScene().getWindow();
         stage.close();
     }

}
