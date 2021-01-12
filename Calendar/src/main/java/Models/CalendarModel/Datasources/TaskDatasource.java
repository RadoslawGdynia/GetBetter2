package Models.CalendarModel.Datasources;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import Models.CalendarModel.Days.Day;
import Models.CalendarModel.AbstractFactories.TaskFactory;
import Models.CalendarModel.Tasks.Subtask;
import Models.CalendarModel.Tasks.Task;
import Models.CalendarModel.Tasks.WorkTask;

import java.sql.*;
import java.time.LocalDate;

public class TaskDatasource {

    public static final Logger log = LoggerFactory.getLogger(TaskDatasource.class);

    public static final String DB_NAME = "GetBetterCalendarDB.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:P:\\GitHub repozytoria\\Repozytoria Online\\GetBetter\\Calendar\\src\\main\\resources\\" + DB_NAME;
    private Connection conn;
    private static TaskDatasource instance = new TaskDatasource();


    public final String TABLE_TASKS = "Tasks";
    public final String COLUMN_TASKS_DAYID = "DayID";
    public final String COLUMN_TASKS_CLASS = "Class";
    public final String COLUMN_TASKS_NAME = "Name";
    public final String COLUMN_TASKS_DETAILS = "Details";
    public final String COLUMN_TASKS_FINALISED = "Finalised";
    public final String COLUMN_TASKS_POINTVALUE = "PointValue";
    public final String COLUMN_TASKS_DEADLINE = "Deadline";
    public final String COLUMN_TASKS_DEADLINECOUNTER= "DeadlineCounter";

    public final String TABLE_SUBTASKS = "Subtasks";
    public final String COLUMN_SUBTASKS_DAYID = "DayID";
    public final String COLUMN_SUBTASKS_PARENT = "ParentName";
    public final String COLUMN_SUBTASKS_NAME = "Name";
    public final String COLUMN_SUBTASKS_DETAILS = "Details";
    public final String COLUMN_SUBTASKS_FINALISED = "Finalised";

    public static TaskDatasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;

        } catch (SQLException e) {
            log.error("Could not get connection with database. Message: {}", e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            log.error("Couldn't close connection. WHAT IS HAPPENING?!");

        }
    }
    public void addTaskToDB(Task task) {
        int dayID = task.getAssignedToDay().getDayID();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " +  TABLE_TASKS +" ("+ COLUMN_TASKS_DAYID + ", " + COLUMN_TASKS_CLASS + ", " + COLUMN_TASKS_NAME +
                ", " + COLUMN_TASKS_DETAILS +", " + COLUMN_TASKS_FINALISED +", " + COLUMN_TASKS_POINTVALUE+", " + COLUMN_TASKS_DEADLINE + ", " + COLUMN_TASKS_DEADLINECOUNTER +")  VALUES ( ");
        sb.append(dayID);
        sb.append(", \"");
        sb.append(task.getClass().getSimpleName().trim());
        sb.append("\", \"");
        sb.append(task.getTaskName());
        sb.append("\", \"");
        sb.append(task.getDetails());
        sb.append("\", \"");
        sb.append(task.getFinalised());
        sb.append("\", ");
        sb.append(task.getPointValue());
        sb.append(", \"");
        if (task.getClass().getSimpleName().equals("WorkTask")) {
            WorkTask workTask = (WorkTask) task;
            sb.append(workTask.getDeadline().toString());
            sb.append("\", ");
            sb.append(((WorkTask) task).getDeadlineChangeCounter());
        } else {
            sb.append("null");
            sb.append("\", ");
            sb.append(0);
        }
        sb.append(")");

        try (Statement statement = conn.createStatement()) {
            statement.execute(sb.toString());

        } catch (SQLException e) {
            log.error("Error took place while saving task {} to the DB \n Message: {}", task.getTaskName(), e.getMessage());
        }
    }
    public void addSubtaskToDB(Task parent, Subtask task) {
        int dayID = parent.getAssignedToDay().getDayID();
        String parentName = parent.getTaskName();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Subtasks (DayID, ParentName, Name, Details, Finalised)  VALUES ( ");
        sb.append(dayID);
        sb.append(", \"");
        sb.append(parentName);
        sb.append("\", \"");
        sb.append(task.getTaskName());
        sb.append("\", \"");
        sb.append(task.getDetails());
        sb.append("\", \"");
        sb.append(task.getFinalised());
        sb.append("\")");
        try (Statement statement = conn.createStatement()) {

            statement.execute(sb.toString());

        } catch (SQLException e) {
            log.error("Error during saving subtask {} of WorkTask: {}", task.getTaskName(), parentName);
        }

    }
    public void editTaskDEADLINEInDB(Task task, LocalDate date) {
        int dayID = task.getAssignedToDay().getDayID();
        String DEADLINE_UPDATE = "UPDATE \"" + TABLE_TASKS + "\" SET \"" + COLUMN_TASKS_DEADLINE + "\"=\"" + date.toString() +
                "\" WHERE DayID=\"" + dayID + "\" AND Models.CalendarModel.Tasks.Name=\"" + task.getTaskName() + "\"";

        try (Statement statement = conn.createStatement()) {
            if (task.getClass().getSimpleName().equals("WorkTask")) {
                statement.execute(DEADLINE_UPDATE);
            } else {
                log.info("Only WorkTasks may have their deadlines changed");
            }

        } catch (SQLException e) {
            log.error("Error took place while editing deadline of task {}\n Message: {}", task.getTaskName(), e.getMessage());
        }

    }

    public void editTaskFINALISEDInDB(Task task, boolean update) {
        int dayID = task.getAssignedToDay().getDayID();
        String FINALISED_UPDATE = "UPDATE \"" + TABLE_TASKS + "\" SET \"" + COLUMN_TASKS_FINALISED + "\"=\"" + update +
                "\" WHERE DayID=\"" + dayID + "\" AND Models.CalendarModel.Tasks.Name=\"" + task.getTaskName() + "\"";

        try (Statement statement = conn.createStatement()) {
            statement.execute(FINALISED_UPDATE);
        } catch (SQLException e) {
            log.error("Error took place while editing finalisation status of task {}\n Message: {}", task.getTaskName(), e.getMessage());
        }

    }

    public void moveTaskDayInDB(Task task, Day day) {
        int dayID = task.getAssignedToDay().getDayID();
        String TASK_MOVE_DAY = "UPDATE \"" + TABLE_TASKS + "\" SET \"" + COLUMN_TASKS_DAYID + "\"=\"" + day.getDayID() +
                "\" WHERE DayID=\"" + dayID + "\" AND Models.CalendarModel.Tasks.Name=\"" + task.getTaskName() + "\"";

        //uzupełnić o modyfikację dla subtasks - wymaga wyszukiwania subtasks o assigned task == zmieniany work task

        try (Statement statement = conn.createStatement()) {
            statement.execute(TASK_MOVE_DAY);
        } catch (SQLException e) {
            log.error("Error took place while editing finalisation status of task {}}\n Message: {}", task.getTaskName(), e.getMessage());
        }

    }

    public void deleteTaskFromDB(Task task) {
        int dayID = task.getAssignedToDay().getDayID();
        String TASK_DELETE = "DELETE FROM \"" + TABLE_TASKS + "\" WHERE DayID=\"" + dayID + "\" AND Models.CalendarModel.Tasks.Name=\"" + task.getTaskName() + "\"";
        String SUBTASKS_DELETE = "DELETE FROM \"" + TABLE_SUBTASKS + "\" WHERE \"" + COLUMN_SUBTASKS_PARENT + "\"=\"" + task.getTaskName()+"\"";
        try (Statement statement = conn.createStatement()) {
            statement.execute(TASK_DELETE);
            if(task.getClass().getSimpleName().equals("WorkTask")){
                statement.execute(SUBTASKS_DELETE);
            }
        } catch (SQLException e) {
            log.error("Error took place while canceling task {} from DB", task.getTaskName());
        }
    }
    public void loadTasksOfDay(Day day) {
        String QUERY_TASKS = "SELECT * FROM \"" + TABLE_TASKS + "\" WHERE \"" + COLUMN_TASKS_DAYID + "\"=\"" + day.getDayID() + "\"" ;

        try(Statement statement = conn.createStatement()){
            statement.execute(QUERY_TASKS);
            ResultSet results = statement.getResultSet();
            int taskNumberVerification=0;
            while(results.next()){
                int dayID = results.getInt(COLUMN_TASKS_DAYID);
                String taskClass = results.getString(COLUMN_TASKS_CLASS);
                String name = results.getString(COLUMN_TASKS_NAME);
                String details = results.getString(COLUMN_TASKS_DETAILS);
                boolean finalised = Boolean.parseBoolean(results.getString(COLUMN_TASKS_FINALISED));
                int points = results.getInt(COLUMN_TASKS_POINTVALUE);
                String deadlineText = results.getString(COLUMN_TASKS_DEADLINE);
                LocalDate deadline = deadlineText.equals("null") ? null : LocalDate.parse(deadlineText);
                int deadlineCounter = results.getInt(COLUMN_TASKS_DEADLINECOUNTER);
                TaskFactory.reloadTask(dayID, taskClass, name, details, finalised, points, deadline, deadlineCounter);
                taskNumberVerification++;
            }
            if(day.getTaskNumber()!=taskNumberVerification) log.error("WARNING: loaded number of tasks is different than recorded in database.\n" +
                    "Should be: " + day.getTaskNumber() +"\t was loaded: " + taskNumberVerification);

        } catch (SQLException e) {
            log.error("Error took place while loading tasks for day: {}", day.getDate());
        }

    }
    public void querySubtasks(Task task){

        int dayID = task.getAssignedToDay().getDayID();
        String QUERY_SUBTASKS = "SELECT * FROM " +TABLE_SUBTASKS + " WHERE " + COLUMN_SUBTASKS_DAYID +"=\"" + dayID + "\" AND " + COLUMN_SUBTASKS_PARENT +"=\"" + task.getTaskName()+"\"";

        try(Statement statement = conn.createStatement()){

            statement.execute(QUERY_SUBTASKS);
            ResultSet results = statement.getResultSet();

            while(results.next()){
                String name = results.getString(COLUMN_SUBTASKS_NAME);
                String details = results.getString(COLUMN_SUBTASKS_DETAILS);
                boolean finalised = Boolean.parseBoolean(results.getString(COLUMN_SUBTASKS_FINALISED));
                WorkTask parent = (WorkTask) task;
                TaskFactory.reloadSubtask(dayID, name, details,finalised, parent);

            }
        }catch (SQLException e) {
            log.error("Error took place while querying subtasks for task: {}}. Message: {}", task.getTaskName(), e.getMessage());
        }

    }
    public boolean taskNotInDB(int dayID, String taskName){
        String QUERY_TASK = "SELECT * FROM " + TABLE_TASKS + " WHERE \"" + COLUMN_TASKS_DAYID +"\"=\"" + dayID + "\" AND \"" + COLUMN_TASKS_NAME +"\"=\"" + taskName+"\"";
        String validation = null;
        try(Statement statement = conn.createStatement()) {
            statement.execute(QUERY_TASK);
            ResultSet results = statement.getResultSet();
            validation = results.getString(COLUMN_TASKS_NAME);


        }catch (SQLException e) {
            return validation==null;
        }
        return validation==null;
    }
    public boolean subtaskNotInDB(WorkTask parent, String taskName){
        String QUERY_SUBTASK = "SELECT * FROM " + TABLE_SUBTASKS + " WHERE \"" + COLUMN_SUBTASKS_PARENT +"\"=\"" + parent.getTaskName() + "\" AND \"" + COLUMN_SUBTASKS_NAME +"\"=\"" + taskName+"\"";
        String validation = null;
        try(Statement statement = conn.createStatement()) {
            statement.execute(QUERY_SUBTASK);
            ResultSet results = statement.getResultSet();
            validation = results.getString(COLUMN_TASKS_NAME);


        }catch (SQLException e) {
            return validation==null;
        }
        return validation==null;
    }
    public void editTaskNameInDB(Task task, String name) {
        int dayID = task.getAssignedToDay().getDayID();
        String NAME_UPDATE = "UPDATE " + TABLE_TASKS + " SET " + COLUMN_TASKS_NAME + "=\"" + name + "\"  WHERE DayID=\"" + dayID + "\" AND \"" + COLUMN_TASKS_NAME + "\"=\"" + task.getTaskName() + "\"";
        String NAME_UPDATE_IN_SUBTASKS = "UPDATE " + TABLE_SUBTASKS + " SET " + COLUMN_SUBTASKS_PARENT + "=\"" + name + "\" WHERE \"" +
                COLUMN_SUBTASKS_PARENT + "\"=\"" + task.getTaskName() + "\"";

        try (Statement statement = conn.createStatement()) {
            statement.execute(NAME_UPDATE);
            if(task instanceof WorkTask){
                statement.execute(NAME_UPDATE_IN_SUBTASKS);
            }
            PlanTilesDatasource.getInstance().updateTaskNameForTiles(task.getAssignedToDay(), task.getTaskName(), name);
        } catch (SQLException e) {
            log.error("Error took place while editing name of task: {}\n Message: {}",task.getTaskName(), e.getMessage());
        }

    }
    public void editTaskDetailsInDB(Task task, String details) {
        int dayID = task.getAssignedToDay().getDayID();
        String DETAILS_UPDATE = "UPDATE " + TABLE_TASKS + " SET " + COLUMN_TASKS_DETAILS + "=\"" + details +
                "\" WHERE DayID=\"" + dayID + "\" AND \"" + COLUMN_TASKS_NAME + "\"=\"" + task.getTaskName() + "\"";

        try (Statement statement = conn.createStatement()) {
            statement.execute(DETAILS_UPDATE);
        } catch (SQLException e) {
            log.error("Error took place while editing details of task: {}\n Message: {}",task.getTaskName(), e.getMessage());
        }

    }
}
