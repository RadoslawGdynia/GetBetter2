package Day;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

public class WorkDay extends Day {

    public static final Logger log = LoggerFactory.getLogger(WorkDay.class);


    public WorkDay(LocalDate date) {
        super(date);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WorkDay)) return false;
        WorkDay day = (WorkDay) o;
        return this.getDate().equals(day.getDate());
    }
}