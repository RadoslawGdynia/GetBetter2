package Controllers;

import Day.Day;
import Task.TrivialTask;
import Task.WorkTask;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class AddTaskDialogController {
    public static final Logger log = LoggerFactory.getLogger(AddTaskDialogController.class);

    Day day = GetBetterCalendarController.getInstance().getSelectedDay();
    @FXML
    TextField taskName;
    @FXML
    TextArea details;
    @FXML
    DatePicker deadlineDate;
    @FXML
    Label deadlineLabel;
    @FXML
    Button OKButton;
    @FXML
    Button CancelButton;



    public void initialize() {
        deactivateWorkMode();
    }


    public TrivialTask createTask(){

            String name = taskName.getText().trim();
            String description = details.getText().trim();
            return new TrivialTask(day, name, description);

    }
    public WorkTask createWorkTask(){
        String name = taskName.getText().trim();
        String description = details.getText().trim();
        LocalDate deadline = deadlineDate.getValue();
        return new WorkTask(day, name, description, deadline);
    }
    public void handleCancelButton() {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
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
}
