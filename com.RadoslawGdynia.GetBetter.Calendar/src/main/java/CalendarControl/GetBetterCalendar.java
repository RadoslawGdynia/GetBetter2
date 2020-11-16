package CalendarControl;

import Datasource.CalendarDatasource;
import Day.Day;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class GetBetterCalendar {
    public static final Logger log = LoggerFactory.getLogger(GetBetterCalendar.class);
    private static GetBetterCalendar instance;
    private static List<Day> days;
    
    
    private GetBetterCalendar(){
        days = FXCollections.observableArrayList();
    }
    
    public static GetBetterCalendar getInstance(){
        if(instance==null){
            instance = new GetBetterCalendar();
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
            System.out.println("Day " + day.getDate() + " was added to calendar");
        }
        else {
            System.out.println("This day:" + day.getDate() + " is already in calendar");
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
            System.out.println("Sought day was not found. Returning day of TODAY");
            for (Day day : days) {
                if (day.getDate().equals(LocalDate.now())) {
                    return days.indexOf(day);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("No such date in database");
        }
        System.out.println("Error took place while searching the days list");
        return 0;
    }

    public static void saveCalendar() {


        try {

        } catch (Exception e) {
            System.out.println("Error took place while saving the calendar.\n Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void loadCalendar() {

        try  {
            CalendarDatasource.getInstance().loadDays();
            LocalDate startDate = LocalDate.now().minusYears(1);
            LocalDate endDate = LocalDate.now().plusYears(1);

            if(days.isEmpty()){

                while(startDate.compareTo(endDate)<1){
                    Day entry = new Day(startDate);
                    GetBetterCalendar.getInstance().addDay(entry);
                    startDate = startDate.plusDays(1);
                }
            }
            if(days.get(days.size()-1).getDate().isBefore(LocalDate.now().plusYears(1))){
                LocalDate date = days.get(days.size()-1).getDate().plusDays(1);
                while(days.get(days.size()-1).getDate().isBefore(LocalDate.now().plusYears(1))){
                    Day day = new Day(date);
                    GetBetterCalendar.getInstance().addDay(day);
                    CalendarDatasource.getInstance().insertDayToDB(day);
                    date = date.plusDays(1);
                }
            }
        } catch (Exception f) {
            System.out.println("Brak szukanego pliku");
        }
    }
}
