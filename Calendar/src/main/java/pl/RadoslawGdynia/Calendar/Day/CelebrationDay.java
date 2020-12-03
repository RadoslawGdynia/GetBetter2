package pl.RadoslawGdynia.Calendar.Day;

import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class designed with Decorator Software design pattern kept in mind.
 * Its purpose is to extend typical day in calendar by allowing to
 * mark it as an important date to remember and add appropriate
 * name and description as its fields
 * */

public class CelebrationDay extends Day {
    public static final Logger log = LoggerFactory.getLogger(CelebrationDay.class);
    private SimpleStringProperty nameOfCelebration = new SimpleStringProperty("");
    private SimpleStringProperty detailsForCelebration = new SimpleStringProperty("");
    private String color;
    private Day propertiesDay;

    public CelebrationDay(Day modifiedDay, String name, String details, String color){
        super(modifiedDay.getDate());
        this.propertiesDay = modifiedDay;
        this.nameOfCelebration.set(name);
        this.detailsForCelebration.set(details);
        this.color = color;
    }




}
