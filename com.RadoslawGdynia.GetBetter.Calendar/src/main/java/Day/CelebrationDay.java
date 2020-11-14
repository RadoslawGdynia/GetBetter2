package Day;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class designed with Decorator Software design pattern kept in mind.
 * Its purpose is to extend typical day in calendar by allowing to
 * mark it as an important date to remember and add appropriate
 * name and description as its fields
 * */

public class CelebrationDay extends Day {
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
