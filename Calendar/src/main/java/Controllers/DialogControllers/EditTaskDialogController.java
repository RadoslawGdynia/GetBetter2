package Controllers.DialogControllers;

import Models.EditTaskModel.EditTaskModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class EditTaskDialogController {

    public static final Logger log = LoggerFactory.getLogger(EditTaskDialogController.class);
    EditTaskModel model = new EditTaskModel(this);

    @FXML
    TextField nameField;
    @FXML
    TextField detailsText;
    @FXML
    Label deadlineLabel;
    @FXML
    DatePicker datePicker;
    @FXML
    Button cancelButton;
    @FXML
    Button applyButton;


    public void initialize() {
        nameField.setText(model.provideName());
        detailsText.appendText(model.provideDetails());
        deadlineLabel.setVisible(false);
        datePicker.setValue(LocalDate.now());
        datePicker.setVisible(false);
        datePicker.setDisable(true);
        try {
            if (model.taskHasDeadline()) {
                deadlineLabel.setVisible(true);
                datePicker.setVisible(true);
                datePicker.setDisable(false);
                datePicker.setValue(model.provideDeadline());
                deadlineLabel.setText("Current deadline for " + nameField.getText() + " is: " + datePicker.getValue() +
                        ". If you would like to change it, choose a date from DatePicker below.");
            }
        } catch (IllegalArgumentException ignored) {
        }
    }

    public void handleApplyButton() {
        String newName = nameField.getText().trim();
        String newDetails = detailsText.getText().trim();
        LocalDate newDeadline = datePicker.getValue();

        model.implementChanges(newName, newDetails, newDeadline);
        closeWindow();
    }

    public void handleCancelButton() {
        closeWindow();
    }

    private void closeWindow(){
        Stage stage = (Stage) applyButton.getScene().getWindow();
        stage.close();
    }
}
