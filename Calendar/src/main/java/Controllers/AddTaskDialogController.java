package Controllers;

import Models.CalendarModel.AbstractFactories.TaskFactory;
import Models.CalendarModel.Days.Day;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AddTaskDialogController {
    public static final Logger log = LoggerFactory.getLogger(AddTaskDialogController.class);

    Day day = CalendarController.getInstance().getSelectedDay();
    @FXML
    TextField taskName;
    @FXML
    TextArea details;
    @FXML
    DatePicker deadlineDate;
    @FXML
    Label deadlineLabel;
    @FXML
    Button okButton;
    @FXML
    Button cancelButton;
    @FXML
    Label warningLabel;
    @FXML
    ToggleGroup taskType;



    public void initialize() {
        deactivateWorkMode();
        warningLabel.setVisible(false);
        deadlineDate.setValue(day.getDate());
    }

    public void handleCancelButton() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    public void handleRadioTrivial(){
        deactivateWorkMode();
    }
    public void handleRadioWork(){
        activateWorkMode();
    }
    private void activateWorkMode(){
        deadlineLabel.setVisible(true);
        deadlineDate.setVisible(true);
        deadlineDate.setDisable(false);
    }
    private void deactivateWorkMode() {
        deadlineLabel.setVisible(false);
        deadlineDate.setVisible(false);
        deadlineDate.setDisable(true);
    }
    public void handleOKButton() {
        if(taskName.getText().isEmpty() || details.getText().isEmpty()){
            warningLabel.setVisible(true);
            warningLabel.setText("Both task name and its details are required");
        } else {
            RadioButton chosenOption = (RadioButton) taskType.getSelectedToggle();
            String nameOfButton = chosenOption.getText();
            TaskFactory.createNewTask(day, nameOfButton, taskName.getText().trim(), details.getText().trim(), deadlineDate.getValue());
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }

    }
}
