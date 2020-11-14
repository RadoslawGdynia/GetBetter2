package Datasource;

import Day.Day;
import Task.Subtask;
import Task.Task;
import Task.WorkTask;

import java.sql.*;
import java.time.LocalDate;

public class CalendarDatasource {

    public static final String DB_NAME = "GetBetterCalendarDB.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:N:\\Programowanie\\GitHub repozytoria\\Repozytoria Online\\com.RadoslawGdynia.GetBetter\\com.RadoslawGdynia.GetBetter.Calendar\\src\\main\\resources\\" + DB_NAME;
    private Connection conn;
    private static CalendarDatasource instance = new CalendarDatasource();

    public final String CREATE_DAYTABLE_STRING = "CREATE TABLE IF NOT EXISTS CalendarDays (_id INTEGER, Day INTEGER, Month INTEGER, Year INTEGER, Tasks INTEGER)";

    public final String TABLE_DAYS = "CalendarDays";
    public final String COLUMN_DAYS_ID = "_id";
    public final String COLUMN_DAYS_DATE = "Date";
    public final String COLUMN_DAYS_TASKSNUMBER = "Tasks";


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


    private CalendarDatasource() {
    }

    public static CalendarDatasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
            return true;

        } catch (SQLException e) {
            System.out.println("Could not get connection with database. Message: " + e.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            System.out.println("Couldnt close connection. WHAT IS HAPPENING?!");
        }
    }

    public void insertDayToDB(Day day) {
        String DAY_INSERT_START = "INSERT INTO " + TABLE_DAYS + " ( " + COLUMN_DAYS_ID + ", " + COLUMN_DAYS_DATE + ", " + COLUMN_DAYS_TASKSNUMBER + ") VALUES (";
        try (Statement statement = conn.createStatement()) {

            statement.execute(DAY_INSERT_START + day.getDayID() + ", \"" + day.getDate().toString() + "\", " + day.getTodayTasks().size() + ")");

        } catch (SQLException e) {
            System.out.println("Something bad happened during insertion of new day in DB: " + e.getMessage());
        }

    }

    public void addTaskToDB(Task task) {
        int dayID = task.getAssignedToDay().getDayID();
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO Tasks (DayID, Class, Name, Details, Finalised, PointValue, Deadline, DeadlineCounter)  VALUES ( ");
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
            sb.append(workTask.getDeadline());
            if (((WorkTask) task).getSubtasks().size() > 0) {
                for (Subtask sub : ((WorkTask) task).getSubtasks()) {
                    addSubtaskToDB(task, sub);
                }
            }
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
            System.out.println("Error took place while saving task " + task.getTaskName() + " to the DB \n Message: " + e.getMessage());
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
            System.out.println("Error during saving subtask " + task.getTaskName() + " of WorkTask " + parentName);
        }

    }

    public void editTaskNAMEandDETAILSInDB(Task task, String name, String details) {
        int dayID = task.getAssignedToDay().getDayID();
        String NAME_UPDATE = "UPDATE \"" + TABLE_TASKS + "\" SET \"" + COLUMN_TASKS_NAME + "\"=\"" + name + "\", \"" + COLUMN_TASKS_DETAILS + "\"=\"" + details +
                "\" WHERE DayID=\"" + dayID + "\" AND Tasks.Name=\"" + task.getTaskName() + "\"";

        try (Statement statement = conn.createStatement()) {
            statement.execute(NAME_UPDATE);

        } catch (SQLException e) {
            System.out.println("Error took place while editing name and details of task: " + task.getTaskName() + "\n Message: " + e.getMessage());
        }

    }

    public void editTaskDEADLINEInDB(Task task, LocalDate date) {
        int dayID = task.getAssignedToDay().getDayID();
        String DEADLINE_UPDATE = "UPDATE \"" + TABLE_TASKS + "\" SET \"" + COLUMN_TASKS_DEADLINE + "\"=\"" + date.toString() +
                "\" WHERE DayID=\"" + dayID + "\" AND Tasks.Name=\"" + task.getTaskName() + "\"";

        try (Statement statement = conn.createStatement()) {
            if (task.getClass().getSimpleName().equals("WorkTask")) {
                statement.execute(DEADLINE_UPDATE);
            } else {
                System.out.println("Only WorkTasks may have their deadlines changed");
            }

        } catch (SQLException e) {
            System.out.println("Error took place while editing deadline of task " + task.getTaskName() + "\n Message: " + e.getMessage());
        }

    }

    public void editTaskFINALISEDInDB(Task task, boolean update) {
        int dayID = task.getAssignedToDay().getDayID();
        String FINALISED_UPDATE = "UPDATE \"" + TABLE_TASKS + "\" SET \"" + COLUMN_TASKS_FINALISED + "\"=\"" + update +
                "\" WHERE DayID=\"" + dayID + "\" AND Tasks.Name=\"" + task.getTaskName() + "\"";

        try (Statement statement = conn.createStatement()) {
            statement.execute(FINALISED_UPDATE);
        } catch (SQLException e) {
            System.out.println("Error took place while editing finalisation status of task " + task.getTaskName() + "\n Message: " + e.getMessage());
        }

    }

    public void moveTaskDayInDB(Task task, Day day) {
        int dayID = task.getAssignedToDay().getDayID();
        String TASK_MOVE_DAY = "UPDATE \"" + TABLE_TASKS + "\" SET \"" + COLUMN_TASKS_DAYID + "\"=\"" + day.getDayID() +
                "\" WHERE DayID=\"" + dayID + "\" AND Tasks.Name=\"" + task.getTaskName() + "\"";

        //uzupełnić o modyfikację dla subtasks - wymaga wyszukiwania subtasks o assigned task == zmieniany work task

        try (Statement statement = conn.createStatement()) {
            statement.execute(TASK_MOVE_DAY);
        } catch (SQLException e) {
            System.out.println("Error took place while editing finalisation status of task " + task.getTaskName() + "\n Message: " + e.getMessage());
        }

    }

    public void deleteTaskFromDB(Task task) {
        int dayID = task.getAssignedToDay().getDayID();
        String TASK_DELETE = "DELETE FROM \"" + TABLE_TASKS + "\" WHERE DayID=\"" + dayID + "\" AND Tasks.Name=\"" + task.getTaskName() + "\"";
        try (Statement statement = conn.createStatement()) {
            statement.execute(TASK_DELETE);

        } catch (SQLException e) {
            System.out.println("Error took place while canceling task " + task.getTaskName() + " from DB");
        }
    }

    public void loadDays() {
        String QUERY_ALL_DAYS = "SELECT * FROM \"" + TABLE_DAYS + "\"";
        try (Statement statement = conn.createStatement()) {


            statement.execute(QUERY_ALL_DAYS);
            ResultSet resultSet = statement.getResultSet();
            while (resultSet.next()) {
                int dayID = resultSet.getInt(COLUMN_DAYS_ID);
                LocalDate date = LocalDate.parse(resultSet.getString(COLUMN_DAYS_DATE));
                int taskNumber = resultSet.getInt(COLUMN_DAYS_TASKSNUMBER);
                Day day = new Day(dayID, date, taskNumber);
            }
            resultSet.close();

        } catch (SQLException e) {
            System.out.println("Error took place while querying all days");
        }
    }

    public void loadTasksOfDay(Day day) {
        String QUERY_TASKS = "SELECT * FROM \"" + TABLE_TASKS + "\" WHERE \"" + COLUMN_TASKS_DAYID + "\"=\"" + day.getDayID() + "\"" ;

        try(Statement statement = conn.createStatement()){
            statement.execute(QUERY_TASKS);
            ResultSet results = statement.getResultSet();
            while(results.next()){
                int dayID = results.getInt(COLUMN_TASKS_DAYID);
                String taskClass = results.getString(COLUMN_TASKS_CLASS);
                String name = results.getString(COLUMN_TASKS_NAME);
                String details = results.getString(COLUMN_TASKS_DETAILS);
                boolean finalised = Boolean.parseBoolean(results.getString(COLUMN_TASKS_FINALISED));
                int points = results.getInt(COLUMN_TASKS_POINTVALUE);
                LocalDate deadline = LocalDate.parse(results.getString(COLUMN_TASKS_DEADLINE));
                int deadlineCounter = results.getInt(COLUMN_TASKS_DEADLINECOUNTER);
                TaskFactory.createTask(dayID, taskClass, name, details, finalised, points, deadline, deadlineCounter);
            }

        } catch (SQLException e) {
            System.out.println("Error took place while loading tasks for day: " + day.getDate());
        }

    }
    public void querySubtasks(Task task){

        int dayID = task.getAssignedToDay().getDayID();
        String QUERY_SUBTASKS = "SELECT * FROM Subtasks WHERE DayID=\"" + dayID + "\" AND ParentName=\"" + task.getTaskName()+"\"";

        try(Statement statement = conn.createStatement()){
            statement.execute(QUERY_SUBTASKS);
            ResultSet results = statement.getResultSet();
            while(results.next()){
                String name = results.getString(COLUMN_SUBTASKS_NAME);
                String details = results.getString(COLUMN_SUBTASKS_DETAILS);
                boolean finalised = Boolean.parseBoolean(results.getString(COLUMN_SUBTASKS_FINALISED));
                WorkTask parent = (WorkTask) task;
                TaskFactory.createSubtask(dayID, name, details,finalised, parent);

            }
        }catch (SQLException e) {
            System.out.println("Error took place while querying subtasks for task: " + task.getTaskName() );
        }

    }


}
