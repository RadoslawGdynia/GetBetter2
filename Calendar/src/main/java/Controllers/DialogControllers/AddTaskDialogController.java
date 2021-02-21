package Controllers.DialogControllers;

import Models.CalendarModel.CalendarModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class AddTaskDialogController {

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
        deadlineDate.setValue(CalendarModel.getInstance().provideDateOfSelectedDay());
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

            CalendarModel.getInstance().createTaskForSelectedDay(nameOfButton, taskName.getText().trim(), details.getText().trim(), deadlineDate.getValue());

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        }

    }
}
