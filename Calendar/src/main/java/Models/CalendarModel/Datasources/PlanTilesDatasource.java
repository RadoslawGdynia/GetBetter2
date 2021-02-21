package Models.CalendarModel.Datasources;

import Models.CalendarModel.Days.Day;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;

@Slf4j
public class PlanTilesDatasource {


    public static final String DB_NAME = "GetBetterCalendarDB.db";

    public static final String CONNECTION_STRING = "jdbc:sqlite:P:\\GitHub repozytoria\\Repozytoria Online\\GetBetter\\Calendar\\src\\main\\resources\\" + DB_NAME;
    private Connection conn;
    private static final PlanTilesDatasource instance = new PlanTilesDatasource();

    private static final String TABLE_TASKTILES = "TaskTiles";
    public static final String COLUMN_TASKTILES_DATE = "Date";
    public static final String COLUMN_TASKTILES_TASKNAME = "TaskName";
    public static final String COLUMN_TASKTILES_TILEID = "TileID";
    public static final String COLUMN_TASKTILES_COLOR = "Color";

    public static PlanTilesDatasource getInstance() {
        return instance;
    }

    public void open() {
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING);
        } catch (SQLException e) {
            log.error("Could not establish connection between PlanTilesDatasource and database. Message: {}", e.getMessage());
        }
    }

    public void close() {
        try {
            conn.close();
        } catch (SQLException e) {
            log.error("Could not close connection with database");
        }

    }

    public ArrayList<String> queryPlanTilesData(Day day) {
        ArrayList<String> queryResults = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        final String QUERY_PLAN_TILES = "SELECT * FROM " + TABLE_TASKTILES + " WHERE " + COLUMN_TASKTILES_DATE + "=\"" +
                day.getDate() + "\"";
        try (Statement statement = conn.createStatement()) {
            statement.execute(QUERY_PLAN_TILES);
            ResultSet results = statement.getResultSet();

            while (results.next()) {
                sb.append(results.getInt(COLUMN_TASKTILES_TILEID));
                sb.append(";");
                sb.append(results.getString(COLUMN_TASKTILES_TASKNAME));
                sb.append(";");
                sb.append(results.getString(COLUMN_TASKTILES_COLOR));
                queryResults.add(sb.toString());
                sb.delete(0, sb.length());
            }
            return queryResults;
        } catch (SQLException e) {
            log.error("Error during query of data for PlanTiles. Message: " + e.getMessage());
            return queryResults;
        }


    }

    public void updateTaskNameForTiles(Day day, String oldName, String newName) {
        final String UPDATE_TILES = "UPDATE " + TABLE_TASKTILES + " SET " + COLUMN_TASKTILES_TASKNAME + "=\"" + newName + "\" WHERE " + COLUMN_TASKTILES_DATE + "=\"" +
                day.getDate() + "\" AND " + COLUMN_TASKTILES_TASKNAME + "=\"" + oldName + "\"";

        try (Statement statement = conn.createStatement()) {
            statement.execute(UPDATE_TILES);
        } catch (SQLException e) {
            log.error("Error took place while updating task name in Tiles table. Message: {}", e.getMessage());
        }
    }

    public void addTileDataToDB(Day day, String taskName, int ID, String color) {
        final String ADD_TILE_DATA = "INSERT INTO " + TABLE_TASKTILES + " VALUES " + "(\"" + day.getDate() + "\", \"" + taskName + "\", " + ID + ", \"" + color + "\")";

        try (Statement statement = conn.createStatement()) {
            statement.execute(ADD_TILE_DATA);
        } catch (SQLException e) {
            log.error("Error took place while adding Models.CalendarModel.Tiles data to DB. Message: {}", e.getMessage());
        }
    }
}
