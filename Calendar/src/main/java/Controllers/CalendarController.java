package Controllers;

import Controllers.DialogControllers.AddSubtaskController;
import Models.CalendarModel.AbstractFactories.TileFactory;
import Models.CalendarModel.CalendarModel;
import Models.CalendarModel.Tasks.Subtask;
import Models.CalendarModel.Tasks.Task;
import Models.CalendarModel.Tasks.WorkTask;
import Models.EditTaskModel.EditTaskModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;

@Slf4j
public class CalendarController {


    // ============== GENERAL: ==============
    @Getter private static CalendarController instance;
    private static final CalendarModel calendarModel = CalendarModel.getInstance();

    //============== CALENDAR ==============

    @FXML
    private Label monthName;
    @FXML
    private Label yearNumber;
    @FXML
    private Pane daysTilePane;


    // ============== DAY DETAILS ==============
    @FXML @Getter
    private TabPane detailsTabPane;

    // ============== Day Plan ==============
    @FXML
    private Label showDay;
    @FXML
    @Getter
    private ComboBox<Task> taskSelectionCombo;
    @FXML
    @Getter
    private Pane timePane;
    @FXML
    @Getter
    private Pane planningPane;

    // ============== Today Tasks ==============

    @FXML
    private TreeTableView<Task> subtasksTreeTable;
    @FXML
    private TreeTableColumn<Task, String> subtaskNameColumn;
    @FXML
    private TreeTableColumn<Task, Boolean> subtaskFinalisedColumn;


    @FXML
    private TableView<Task> tasksTable;
    @FXML
    private TableColumn<Task, String> taskNameColumn;
    @FXML
    private TableColumn<Task, String> taskDetailsColumn;
    @FXML
    private TableColumn<Task, LocalDate> taskDeadlineColumn;

    @FXML
    private Button addTaskButton;
    @FXML
    private Button editTaskButton;
    @FXML
    private Button deleteTaskButton;
    @FXML
    private Button addSubtaskButton;


    //============== GENERAL METHODS: ==============

    public CalendarController() {
        instance = this;
    }

    public void initialize() {
        calendarModel.initializeCalendar();
        configurePlanTiles();
        configureTimeTiles();
        configureCalendarPane();
        populateDayAndTaskData();
    }

    // ============== CALENDAR AREA METHODS: ==============

    private void configureCalendarPane() {

        monthName.setText(calendarModel.provideCurrentlySelectedMonthName());
        yearNumber.setText(calendarModel.provideCurrentlySelectedYearValue());
        TileFactory.createSetOfTiles("CalendarTile", daysTilePane, 42);
    }

    public void handleMonthBack() {
        daysTilePane.getChildren().clear();
        if (calendarModel.getCurrentMonthNum() == 1) {
            calendarModel.changeCurrentMonthFromJanuaryToDecember();
        } else {
            calendarModel.decreaseCurrentMonthBy1();
        }
        configureCalendarPane();
        log.info("Currently chosen month is: {}", calendarModel.provideCurrentlySelectedMonthName());
    }

    public void handleMonthForward() {
        daysTilePane.getChildren().clear();
        if (calendarModel.getCurrentMonthNum() == 12) {
            calendarModel.changeCurrentMonthFromDecemberToJanuary();
        } else {
            calendarModel.increaseCurrentMonthBy1();
        }
        configureCalendarPane();
        log.info("Currently chosen month is: {}", calendarModel.provideCurrentlySelectedMonthName());
    }

    //============== DAY DETAILS METHODS: ==============

    private void noTaskSelected() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Missing information!");
        alert.setHeaderText("No task was chosen. Chosen action requires a selected task to be performed.");
        alert.setContentText("Please select one of the tasks from the list and proceed.");
        alert.showAndWait();
    }


    //============== A. DAY PLAN METHODS: ==============
    public void configureTimeTiles() {
        final int TILES_NUMBER = 18;
        TileFactory.createSetOfTiles("TimeTile", timePane, TILES_NUMBER);

    }

    public void configurePlanTiles() {
        final int TILES_NUMBER = 72;
        TileFactory.createSetOfTiles("PlanTile", planningPane, TILES_NUMBER);
    }
    public void populateComboBox() {
        //taskSelectionCombo.setItems(selectedDay.getTodayTasks());
    }
    public void handleAssignTaskButton(){

    }
    public void handleClearTileButton(){

    }


    //============ B. TODAY TASK METHODS: ================

    public void populateDayAndTaskData() {

        tasksTable.setItems(calendarModel.provideTasksOfSelectedDay());
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("taskName"));
        taskDetailsColumn.setCellValueFactory(new PropertyValueFactory<>("details"));
        //taskDeadlineColumn.setCellValueFactory(new PropertyValueFactory<>("deadline"));

        taskNameColumn.setCellValueFactory(taskStringCellDataFeatures -> taskStringCellDataFeatures.getValue().getObservableTaskName());
        taskDetailsColumn.setCellValueFactory(taskStringCellDataFeatures -> taskStringCellDataFeatures.getValue().getObservableDetails());

        tasksTable.getSelectionModel().selectedItemProperty().addListener((observableValue, task, t1) -> {
            if (t1 != null) {
                Task observedTask = tasksTable.getSelectionModel().getSelectedItem();
                if (t1.getClass().getSimpleName().equals("WorkTask"))

                    configureSubtasksTable((WorkTask) observedTask);
            }
        });

        tasksTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

    }

    public void handleTaskSelection() {
        Task cTask = tasksTable.getSelectionModel().getSelectedItem();
        if (cTask == null) {
            return;
        }
        log.info("Currently selected task: {}", cTask.getTaskName());
        subtasksTreeTable.setDisable(false);
        addTaskButton.setDisable(false);
        editTaskButton.setDisable(false);
        deleteTaskButton.setDisable(false);
        if (cTask.getClass().getSimpleName().equals("TrivialTask")) {
            addTaskButton.setDisable(true);
            subtasksTreeTable.setDisable(true);
        }

    }


    public void configureSubtasksTable(WorkTask task) {
        subtasksTreeTable.refresh();
        TreeItem<Task> root = new TreeItem<>();
        root.setValue(task);
        for (Subtask subtask : task.getSubtasks()) {
            TreeItem<Task> listableSubtask = new TreeItem<>(subtask);
            root.getChildren().add(listableSubtask);
        }
        subtasksTreeTable.setRoot(root);
        subtaskNameColumn.setCellValueFactory(parameter -> parameter.getValue().getValue().getObservableTaskName());


        subtaskFinalisedColumn.setCellValueFactory(parameter -> parameter.getValue().getValue().getObservableFinalised());
    }

    public void handleAddTaskClick() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Addition of task to the day: " + calendarModel.provideDateOfSelectedDay());


        try {
            URL url = new File("Calendar/src/main/resources/AddTaskDialog.fxml").toURI().toURL();
            dialog.getDialogPane().setContent(FXMLLoader.load(url));
            dialog.showAndWait();

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lack of information");
            alert.setHeaderText("You are trying to create task without necessary information.");
            alert.setContentText("Task name is required.");

        } catch (IOException e) {
            log.error("Could not load the dialog");
            e.printStackTrace();
        }

    }

    public void handleAddSubtaskClick() {
        Task task = tasksTable.getSelectionModel().getSelectedItem();
        if (task == null) {
            noTaskSelected();
        } else {
            WorkTask wt = (WorkTask) task;
            AddSubtaskController.setSelectedWorkTask(wt);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Addition of subtask to task: " + task.getTaskName());
            try {
                URL url = new File("Calendar/src/main/resources/AddSubtaskDialog.fxml").toURI().toURL();
                dialog.getDialogPane().setContent(FXMLLoader.load(url));
                dialog.showAndWait();

            } catch (IOException e) {
                log.error("Could not load the dialog");
                e.printStackTrace();
            }
        }
    }

    public void handleEditTaskClick() {
        Task task = tasksTable.getSelectionModel().getSelectedItem();
        if (task == null) {
            noTaskSelected();
        } else {
            EditTaskModel.setSelectedTask(task);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Editing task: " + task.getTaskName());


            try {
                URL url = new File("Calendar/src/main/resources/EditTaskDialog.fxml").toURI().toURL();
                dialog.getDialogPane().setContent(FXMLLoader.load(url));
                dialog.showAndWait();
            } catch (IOException e) {
                log.error("Could not load the dialog");
                e.printStackTrace();
            }
        }
    }

    public void handleDeleteTaskClick() {
        Task task = tasksTable.getSelectionModel().getSelectedItem();
        deleteTask(task);
    }

    public void deleteTask(Task taskToDelete) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Task deletion");
        a.setHeaderText("You intend to delete task: " + taskToDelete.getTaskName());
        a.setContentText("Are you sure you want to proceed? This operation is irreversible and you have put this task in the calendar for a good reason");
        Optional<ButtonType> result = a.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            calendarModel.removeTaskFromSelectedDay(taskToDelete);
        }
    }
    public void handleCalendarTileClick(String text){
        try {
            showDay.setText(text);
            log.info("Text for the label: {}", calendarModel.provideDateOfSelectedDay().toString());
            showDay.setText(calendarModel.provideDateOfSelectedDay().toString());
            timePane.getChildren().remove(0, timePane.getChildren().size());
            planningPane.getChildren().remove(0, planningPane.getChildren().size());
            log.info("Starting to populate the area");
            populateDayAndTaskData();
            calendarModel.updateTaskManagingArea();
        } catch (NullPointerException e) {
            log.error("Program is unable to change FXML fields");
        }

    }


    public void handleAssignTaskButton(ActionEvent actionEvent) {
    }

    public void handleClearTileButton(ActionEvent actionEvent) {
    }
}


