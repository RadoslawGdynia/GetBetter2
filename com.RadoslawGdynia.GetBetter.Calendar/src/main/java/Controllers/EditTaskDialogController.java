package Controllers;

import Task.Task;
import Task.WorkTask;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    TextField taskDetailsText;
    @FXML
    Label taskDeadlineLabel;
    @FXML
    DatePicker ETDatePicker;
    @FXML
    Button cancelButton;


    public static void setSelectedTask(Task sTask) {
        selectedTask = sTask;
    }

    public void initialize() {
        TaskDetailsLabel.setText("By changing content of the text and date below you will implement changes in task " + selectedTask.getTaskName());
        taskDetailsText.appendText(selectedTask.getDetails());

        ETTaskNameLabel.setText("Name of the task: ");
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

     public void handleApplyButton(){
        String previousDetails = selectedTask.getDetails();
       //  LocalDate previousDeadline = selectedTask.getDeadline();

         if(!selectedTask.getTaskName().equals(ETTaskNameField.getText().trim())) {
             selectedTask.editTaskName(ETTaskNameField.getText().trim());
         }
         else if((!previousDetails.equals(taskDetailsText.getText().trim()))){
             selectedTask.editTaskDetails(taskDetailsText.getText().trim());
         }
//         else if (previousDeadline != ETDatePicker.getValue()) {
//             selectedTask.editTaskDeadline(ETDatePicker.getValue());
//         }
     }
     public void handleCancelButton() {
         Stage stage = (Stage) cancelButton.getScene().getWindow();
         stage.close();
     }
}
