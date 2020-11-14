package Task;

import Day.Day;

public class TrivialTask extends Task {

    public TrivialTask(Day day, String name, String details) {
        super(day, name, details);
    }
    public TrivialTask(Day day, String name, String details, boolean finalised) {
        super(day,name, details);
        this.setFinalised(finalised);
    }
}
