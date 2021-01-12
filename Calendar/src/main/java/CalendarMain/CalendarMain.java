package CalendarMain;

import Models.CalendarModel.Datasources.CalendarDatasource;
import Models.CalendarModel.Days.Day;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class CalendarMain {
    public static final Logger log = LoggerFactory.getLogger(CalendarMain.class);
    private static CalendarMain instance;
    private static List<Day> days;
    
    
    private CalendarMain(){
        days = FXCollections.observableArrayList();
    }
    
    public static CalendarMain getInstance(){
        if(instance==null){
            instance = new CalendarMain();
        }
        return instance;
    }


    public static ObservableList<Day> getDays() {
        ObservableList<Day> viewDays = FXCollections.observableArrayList();
        viewDays.addAll(days);
        return viewDays;
    }
    public void addDay(Day day) throws IOException{
        if(!dayExists(day)){
            days.add(day);
            log.info("Day {} was added to calendar", day.getDate());
        }
        else {
            log.info("This day {} is already in calendar",  day.getDate());
            throw new IOException("Tried to add existing day");
        }
    }
    private boolean dayExists(Day day){
        for(Day comparison : days){
            if(comparison.getDate().isEqual(day.getDate())) return true;
        }
        return false;
    }

    public static int getDayIndex(LocalDate date) {
        try {
            for (Day day : days) {
                if (day.getDate().equals(date)) {
                    return days.indexOf(day);
                }
            }
            log.info("Sought day was not found. Returning day of TODAY");
            for (Day day : days) {
                if (day.getDate().equals(LocalDate.now())) {
                    return days.indexOf(day);
                }
            }
        } catch (NullPointerException e) {
            log.info("No such date in database");
        }
        log.debug("Error took place while searching the days list");
        return 0;
    }

    public static void loadCalendar() {

        try  {
            CalendarDatasource.getInstance().loadDays();
            LocalDate startDate = LocalDate.now().minusYears(1);
            LocalDate endDate = LocalDate.now().plusYears(1);

            if(days.isEmpty()){

                while(startDate.compareTo(endDate)<1){
                    Day entry = new Day(startDate);
                    CalendarMain.getInstance().addDay(entry);
                    startDate = startDate.plusDays(1);
                }
            }
            if(days.get(days.size()-1).getDate().isBefore(LocalDate.now().plusYears(1))){
                LocalDate date = days.get(days.size()-1).getDate().plusDays(1);
                while(date.isBefore(LocalDate.now().plusYears(1))){
                    Day day = new Day(date);
                    CalendarDatasource.getInstance().insertDayToDB(day);
                    date = date.plusDays(1);
                }
            }
        } catch (IOException e) {
            log.debug("Problem with loading database data from GBCalendar level. Message: {}", e.getMessage());
        }
    }
}
