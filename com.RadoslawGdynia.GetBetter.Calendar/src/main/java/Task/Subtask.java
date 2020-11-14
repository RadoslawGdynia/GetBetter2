package Task;

import Day.Day;

public class Subtask extends Task {
    WorkTask assignedTo;

    public Subtask(Day day, String name, String details, WorkTask assignedTo) {

        this.setAssignedToDay(null);
        this.setTaskName(name);
        this.setDetails(details);
        this.assignedTo = assignedTo;
        this.setPointValue(0);
        assignedTo.addSubtask(this);
    }


    public WorkTask getAssignedTo() {
        return assignedTo;
    }
}
