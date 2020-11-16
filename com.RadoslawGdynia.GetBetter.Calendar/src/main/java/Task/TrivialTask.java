package Task;

import Day.Day;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TrivialTask extends Task {

    public static final Logger log = LoggerFactory.getLogger(TrivialTask.class);

    public TrivialTask(Day day, String name, String details) {
        super(day, name, details);
    }
    public TrivialTask(Day day, String name, String details, boolean finalised) {
        super(day,name, details);
        this.setFinalised(finalised);
    }
}
