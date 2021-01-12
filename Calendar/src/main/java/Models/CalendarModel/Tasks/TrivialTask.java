package Models.CalendarModel.Tasks;

import Models.CalendarModel.Days.Day;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrivialTask extends Task {

    public static final Logger log = LoggerFactory.getLogger(TrivialTask.class);

    public TrivialTask(Day day, String name, String details) {
        super(day, name, details);
        day.addTask(this);
    }
    public TrivialTask(Day day, String name, String details, boolean finalised) {
        super(day,name, details);
        this.setFinalised(finalised);
        day.addTask(this);
    }

    @Override
    public SimpleStringProperty getObservableTaskName() {
        return super.getObservableTaskName();
    }

    @Override
    public SimpleStringProperty getObservableDetails() {
        return super.getObservableDetails();
    }

    @Override
    public SimpleBooleanProperty getObservableFinalised() {
        return super.getObservableFinalised();
    }
}
