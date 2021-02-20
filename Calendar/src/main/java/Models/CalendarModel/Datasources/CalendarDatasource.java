package Models.CalendarModel.Datasources;

import Models.CalendarModel.Days.Day;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.time.LocalDate;

@Slf4j
public class CalendarDatasource {

    public static final String DB_NAME = "GetBetterCalendarDB.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:P:\\GitHub repozytoria\\Repozytoria Online\\GetBetter\\Calendar\\src\\main\\resources\\" + DB_NAME;
    private Connection conn;
    private static final CalendarDatasource instance = new CalendarDatasource();

    public final String TABLE_DAYS = "CalendarDays";
    public final String COLUMN_DAYS_ID = "_id";
    public final String COLUMN_DAYS_DATE = "Date";
    public final String COLUMN_DAYS_TASKSNUMBER = "Tasks";





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

    public void insertDayToDB(Day day) {
        String DAY_INSERT_START = "INSERT INTO " + TABLE_DAYS + " ( " + COLUMN_DAYS_ID + ", " + COLUMN_DAYS_DATE + ", " + COLUMN_DAYS_TASKSNUMBER + ") VALUES (";
        try (Statement statement = conn.createStatement()) {

            statement.execute(DAY_INSERT_START + day.getDayID() + ", \"" + day.getDate().toString() + "\", " + day.getTodayTasks().size() + ")");

        } catch (SQLException e) {
            log.error("Something bad happened during insertion of new day in DB: {}", e.getMessage());
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
            log.error("Error took place while querying all days");
        }
    }
    public void updateDayTaskNumber(Day day, int taskNumber) {
        String UPDATE_TASK_NUMBER = "UPDATE " + TABLE_DAYS + " SET \"" + COLUMN_DAYS_TASKSNUMBER + "\"=" + taskNumber +" WHERE \"" +
                COLUMN_DAYS_ID + "\"=" + day.getDayID();
        try(Statement statement = conn.createStatement()){
            statement.execute(UPDATE_TASK_NUMBER);
            log.info("New value fo task number for day {} is: {}",day.getDate(), taskNumber);

        } catch (SQLException e) {
            log.error("Problem occurred while updating in DB number of tasks for day: {}", day.getDate());
        }
    }




}
