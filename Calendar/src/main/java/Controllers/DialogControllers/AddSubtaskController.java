package Controllers.DialogControllers;

import Controllers.CalendarController;
import Models.CalendarModel.AbstractFactories.TaskFactory;
import Models.CalendarModel.Tasks.WorkTask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddSubtaskController {

    private static WorkTask selectedWorkTask;

    @FXML
    Button okButton;
    @FXML
    Button cancelButton;
    @FXML
    Label parentLabel;
    @FXML
    TextField nameField;
    @FXML
    TextArea detailsArea;
    @FXML
    Label warningLabel;

    public static void setSelectedWorkTask(WorkTask task){
        selectedWorkTask = task;
    }


    public void initialize() {
        parentLabel.setText("Addition of subtask to the Work Task: " + selectedWorkTask.getTaskName());
    }

    public void handleOkButton(){
        String subtaskName = nameField.getText().trim();
        String subtaskDetails = detailsArea.getText().trim();
        if(subtaskName.isEmpty() || subtaskDetails.isEmpty()) {
            warningLabel.setText("Please fill information in both fields to create new subtask.");
            return;
        }
        TaskFactory.createNewSubtask(selectedWorkTask.getAssignedToDay(),selectedWorkTask, subtaskName, subtaskDetails);
        CalendarController.getInstance().configureSubtasksTable(selectedWorkTask);
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();

    }
    public void handleCancelButton(){
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

}
