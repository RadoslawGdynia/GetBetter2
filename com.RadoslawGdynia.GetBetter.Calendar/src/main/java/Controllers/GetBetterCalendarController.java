package Controllers;

import CalendarWindow.GetBetterCalendar;
import Day.Day;
import Task.Task;
import Task.WorkTask;
import Tiles.CalendarTile;
import Tiles.PlanTile;
import Tiles.Tile;
import Tiles.TimeTile;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Optional;

public class GetBetterCalendarController {

    // ============== GENERAL: ==============
    public static final Logger log = LoggerFactory.getLogger(GetBetterCalendarController.class);
    private static GetBetterCalendarController instance;
    private int currentMonthNum, currentYearNum, currentDayNum;
    private Day selectedDay = GetBetterCalendar.getDays().get(GetBetterCalendar.getDayIndex(LocalDate.now()));

    //============== CALENDAR ==============

    @FXML
    private Label monthName;
    @FXML
    private Label yearNumber;
    @FXML
    private Pane daysTilePane;


    // ============== DAY DETAILS ==============
    @FXML
    private TabPane detailsTabPane;

    // ============== Day Plan ==============
    @FXML
    private Label showDay;
    @FXML
    private ComboBox<Task> taskSelectionCombo;
    @FXML
    private Pane TimePane;
    @FXML
    private Pane PlanningPane;

    // ============== Today Tasks ==============

    @FXML
    private TreeTableView<Task> subtasksTreeTable;
    @FXML
    private TreeTableColumn<Task, String> subtaskNameColumn;
    @FXML
    private TreeTableColumn<Task, String> subtaskDeadlineColumn;
    @FXML
    private TreeTableColumn<Task, ProgressBar> subtaskProgressColumn;


    @FXML
    private TableView<Task> TVTasksTable;
    @FXML
    private TableColumn<Task, String> TVTaskName;

    @FXML
    private Button addTaskButton;
    @FXML
    private Button editTaskButton;
    @FXML
    private Button deleteTaskButton;


    //============== GENERAL METHODS: ==============
    public GetBetterCalendarController() {
        instance = this;
    }

    public static GetBetterCalendarController getInstance() {
        return instance;
    }

    public Day getSelectedDay() {
        return selectedDay;
    }

    public int getCurrentMonthNum() {
        return currentMonthNum;
    }

    public int getCurrentYearNum() {
        return currentYearNum;
    }

    public Pane getTimePane() {
        return TimePane;
    }

    public Label getShowDay() {
        return showDay;
    }

    public Pane getPlanningPane() {
        return PlanningPane;
    }

    public TabPane getDetailsTabPane() {
        return detailsTabPane;
    }

    public void setSelectedDay(Day selectedDay) {
        this.selectedDay = selectedDay;
    }

    public void initialize() {
        detailsTabPane.setDisable(true);
        currentMonthNum = selectedDay.getDate().getMonthValue();
        currentYearNum = selectedDay.getDate().getYear();
        currentDayNum = selectedDay.getDate().getDayOfMonth();
        configureCalendarPane();
    }
    // ============== CALENDAR AREA METHODS: ==============

    private void configureCalendarPane() {

        monthName.setText(LocalDate.of(currentYearNum, currentMonthNum, currentDayNum).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
        yearNumber.setText(String.valueOf(currentYearNum));

        int firstDayOfMonth = LocalDate.of(currentYearNum, currentMonthNum, currentDayNum - (currentDayNum - 1)).getDayOfWeek().getValue();
        int numberOfDaysInCurrentMonth = LocalDate.of(currentYearNum, currentMonthNum, currentDayNum).getMonth().maxLength();

        int NUM_OF_TILES = 42;
        int size = 90;

        for (int i = 1; i <= NUM_OF_TILES; i++) {

            String idString = "CalendarTile" + i;
            Tile calendarTile;
            int dayNumber = (i - (firstDayOfMonth - 1));
            String display = dayNumber + "";
            if ((i >= firstDayOfMonth) && (i < (numberOfDaysInCurrentMonth + firstDayOfMonth))) {

                calendarTile = new Tile(daysTilePane, idString, display, size, size, new CalendarTile(dayNumber));

            } else {
                calendarTile = new Tile(daysTilePane, idString, display, size, size, new CalendarTile(1));
                calendarTile.setDisable(true);
                calendarTile.setVisible(false);
            }

        }
    }

    public void handleMonthBack() {
        daysTilePane.getChildren().clear();
        if (currentMonthNum == 1) {
            currentMonthNum = 12;
            currentYearNum--;
        } else {
            currentMonthNum--;
        }
        detailsTabPane.setDisable(true);
        configureCalendarPane();
        log.info("Currently chosen month is: " + LocalDate.of(currentYearNum, currentMonthNum, currentDayNum).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
    }

    public void handleMonthForward() {
        daysTilePane.getChildren().clear();
        if (currentMonthNum == 12) {
            currentMonthNum = 1;
            currentYearNum++;
        } else {
            currentMonthNum++;
        }
        detailsTabPane.setDisable(true);
        configureCalendarPane();
        log.info("Currently chosen month is: " + LocalDate.of(currentYearNum, currentMonthNum, currentDayNum).getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH));
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
        int hour1 = 6;
        int hour2 = 7;
        StringBuilder text = new StringBuilder();

        for (int i = 1; i <= TILES_NUMBER; i++) {
            text.append(hour1);
            text.append(":00");
            text.append("-");
            text.append(hour2);
            text.append(":");
            text.append(":00");

            Tile timeTile = new Tile(TimePane, i + "", text.toString(), 100, (int) (TimePane.getHeight() / TILES_NUMBER), new TimeTile());
            hour1++;
            hour2++;

            text.delete(0, text.length());
        }

    }

    public void configurePlanTiles() {
        final int TILES_NUMBER = 72;
        for (int i = 1; i <= TILES_NUMBER; i++) {
            Tile planTile = new Tile(PlanningPane, i + "", "Plan", 200, 9, new PlanTile());
        }

    }


    //============ B. TODAYS TASK METHODS: ================


    public void configureTasksTable() {
        TVTasksTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        TVTaskName.setCellValueFactory(new PropertyValueFactory<Task, String>("taskName"));
        TVTasksTable.setItems(selectedDay.getTodayTasks());
    }

    public void handleTaskSelection(MouseEvent mouseEvent) {
        Task cTask = TVTasksTable.getSelectionModel().getSelectedItem();
        if(cTask==null){
            return;
        }
        log.info("Currently selected task: " + cTask.getTaskName());
        addTaskButton.setDisable(false);
        editTaskButton.setDisable(false);
        deleteTaskButton.setDisable(false);
        if(cTask.getClass().getSimpleName().equals("TrivialTask")){
            addTaskButton.setDisable(true);
        }
        subtasksTreeTable.setDisable(false);
    }


    public void configureSubtasksTable() {
        TreeItem<Task> root = new TreeItem<>();

        subtaskNameColumn.setCellValueFactory((Callback<TreeTableColumn.CellDataFeatures<Task, String>, ObservableValue<String>>)
                taskStringCellDataFeatures -> new SimpleStringProperty(taskStringCellDataFeatures.getValue().getValue().getTaskName()));

//        subtaskDeadlineColumn.setCellValueFactory(taskStringCellDataFeatures -> {
//            if (taskStringCellDataFeatures == null) {
//                return null;
//            } else {
//                return new SimpleStringProperty(taskStringCellDataFeatures.getValue().getValue().getDeadline().toString());
//            }
//        });
//
//        for(Task task : selectedDay.getTodayTasks()) {
//           TreeItem<Task> listableTask = new TreeItem<>(task);
//
//            for (Task subtask : task.getSubtasks()) {
//                TreeItem<Task> listableSubtask = new TreeItem<>(subtask);
//                listableTask.getChildren().add(listableSubtask);
//            }
//            root.getChildren().add(listableTask);
//        }
//        subtasksTreeTable.setRoot(root);


    }

    public void handleAddTaskClick() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Addition of task to the day: " + selectedDay.getDate());


        try {
            URL url = new File("com.RadoslawGdynia.GetBetter.Calendar/src/main/resources/AddTaskDialog.fxml").toURI().toURL();
            dialog.getDialogPane().setContent(FXMLLoader.load(url));
            dialog.showAndWait();

        } catch (NullPointerException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lack of information");
                alert.setHeaderText("You are trying to create task without necessary information.");
                alert.setContentText("Task name is required.");

        } catch(IOException e) {
            log.error("Could not load the dialog");
            e.printStackTrace();
        }

    }

    public void handleAddSubtaskClick(ActionEvent event) {
        Task task = TVTasksTable.getSelectionModel().getSelectedItem();
        if (task == null) {
            noTaskSelected();
        } else {
            WorkTask wt = (WorkTask) task;
            Controllers.AddSubtaskController.setSelectedWorkTask(wt);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Addition of subtask to task: " + task.getTaskName());
            try {
                URL url = new File("com.RadoslawGdynia.GetBetter.Calendar/src/main/resources/AddSubtaskDialog.fxml").toURI().toURL();
                dialog.getDialogPane().setContent(FXMLLoader.load(url));
                dialog.showAndWait();

            } catch (IOException e) {
                log.error("Could not load the dialog");
                e.printStackTrace();
            }
        }
    }

    public void handleEditTaskClick() {
        Task task = TVTasksTable.getSelectionModel().getSelectedItem();
        if(task == null){
            noTaskSelected();
        } else {
            Controllers.EditTaskDialogController.setSelectedTask(task);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Editing task: " + task.getTaskName());


            try {
                URL url = new File("com.RadoslawGdynia.GetBetter.Calendar/src/main/resources/EditTaskDialog.fxml").toURI().toURL();
                dialog.getDialogPane().setContent(FXMLLoader.load(url));
                dialog.showAndWait();
            } catch (IOException e) {
                log.error("Could not load the dialog");
                e.printStackTrace();
            }
        }
    }

    public void handleDeleteTaskClick(ActionEvent event) {

        Task task = TVTasksTable.getSelectionModel().getSelectedItem();
        deleteTask(task);
    }

    public void deleteTask(Task t) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle("Task deletion");
        a.setHeaderText("You intend to delete task: " + t.getTaskName());
        a.setContentText("Are you sure you want to proceed? This operation is irreversible and you have put this task in the calendar for a good reason");
        Optional<ButtonType> result = a.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            selectedDay.removeTask(t);
        }
    }


}


