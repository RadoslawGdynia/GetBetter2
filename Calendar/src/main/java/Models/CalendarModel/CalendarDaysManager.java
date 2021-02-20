package Models.CalendarModel;

import Models.CalendarModel.Datasources.CalendarDatasource;
import Models.CalendarModel.Days.Day;
import javafx.collections.FXCollections;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Slf4j
public class CalendarDaysManager {

    private static CalendarDaysManager instance;
    private static final List<Day> calendar = FXCollections.observableArrayList();
    private final CalendarDatasource dayDatabaseManager = CalendarDatasource.getInstance();

    public static CalendarDaysManager getInstance(){
        if(instance==null){
            instance = new CalendarDaysManager();
        }
        return instance;
    }
    public int getDayIndex(LocalDate date) {
        try {
            for (Day day : calendar) {
                if (day.getDate().equals(date)) {
                    return calendar.indexOf(day);
                }
            }
            log.info("Sought day was not found. Returning day of TODAY");
            for (Day day : calendar) {
                if (day.getDate().equals(LocalDate.now())) {
                    return calendar.indexOf(day);
                }
            }
        } catch (NullPointerException e) {
            log.info("No such date in database");
        }
        log.debug("Error took place while searching the days list");
        return 0;
    }

    public Day selectDayFromCalendar(LocalDate dateOfDay){
        int indexOfDay = getDayIndex(dateOfDay);
        return calendar.get(indexOfDay);
    }

    public void loadDaysFromDatabaseToCalendar() {
        try {
            dayDatabaseManager.loadDays();
            LocalDate startDate = LocalDate.now().minusYears(1);
            LocalDate endDate = LocalDate.now().plusYears(1);

            if (calendar.isEmpty()) {
                while (startDate.compareTo(endDate) < 1) {
                    Day nextDayForCalendar = new Day(startDate);
                    addDayToCalendar(nextDayForCalendar);
                    startDate = startDate.plusDays(1);
                }
            }
            boolean loadedLessThanYearAhead = calendar.get(calendar.size() - 1).getDate().isBefore(LocalDate.now().plusYears(1));

            if (loadedLessThanYearAhead) {

                LocalDate dateOfLastDayInCalendar = calendar.get(calendar.size() - 1).getDate();
                LocalDate newDayToAdd = dateOfLastDayInCalendar.plusDays(1);
                boolean notFullYearAheadLoaded = newDayToAdd.isBefore(LocalDate.now().plusYears(1));

                while (notFullYearAheadLoaded) {

                    Day day = new Day(newDayToAdd);
                    CalendarDatasource.getInstance().insertDayToDB(day);
                    newDayToAdd = newDayToAdd.plusDays(1);

                    if(newDayToAdd.isEqual(LocalDate.now().plusYears(1))) notFullYearAheadLoaded = false;
                }
            }
        } catch (IOException e) {
            log.debug("Problem with loading database data from GBCalendar level. Message: {}", e.getMessage());
        }
    }

    private void addDayToCalendar(Day addedDay) throws IOException {
        if(!dayExistsInCalendar(addedDay)){
            calendar.add(addedDay);
            log.info("Day {} was added to calendar", addedDay.getDate());
        }
        else {
            log.info("Day {} is already in calendar",  addedDay.getDate());
            throw new IOException("Tried to add existing day");
        }
    }
    private boolean dayExistsInCalendar(Day day){
        for(Day comparison : calendar){
            if(comparison.getDate().isEqual(day.getDate())) return true;
        }
        return false;
    }

    private CalendarDaysManager() {
    }
}
