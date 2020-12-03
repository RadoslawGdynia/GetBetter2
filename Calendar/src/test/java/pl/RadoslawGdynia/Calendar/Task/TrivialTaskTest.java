package pl.RadoslawGdynia.Calendar.Task;

import pl.RadoslawGdynia.Calendar.Day.Day;
import javafx.beans.property.SimpleStringProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrivialTaskTest {

    public static final Logger log = LoggerFactory.getLogger(TrivialTaskTest.class);
    TrivialTask testTask;


    @BeforeEach
    public void setup() {
        Day taskDay = new Day(LocalDate.now());
        String taskName = "ZadanieTestowe";
        String details = "Szczegóły niniejszego zadania są zawarte wyłącznie w celach testowych";

        testTask = new TrivialTask(taskDay, taskName, details);

        System.out.println("Stworzono instancję klasy Task. Przeprowadzam test.");
    }

    @Test
    void getAssignedToDay() {
        assertEquals(LocalDate.now(), testTask.getAssignedToDay().getDate());
    }

    @Test
    void getTaskName() {
        assertEquals("ZadanieTestowe", testTask.getTaskName());
    }

    @Test
    void getVisibleTaskName() {
        SimpleStringProperty verify = new SimpleStringProperty("ZadanieTestowe");
        assertEquals(verify.get(), testTask.getObservableTaskName().get());
    }

    @Test
    void getDetails() {
        assertEquals("Szczegóły niniejszego zadania są zawarte wyłącznie w celach testowych", testTask.getDetails());
    }

    @Test
    void getVisibleDetails() {
        SimpleStringProperty verify = new SimpleStringProperty("Szczegóły niniejszego zadania są zawarte wyłącznie w celach testowych");
        assertEquals(verify.get(), testTask.getObservableDetails().get());
    }

    @Test
    void getFinalised() {
        assertFalse(testTask.getFinalised());
    }

    @Test
    void getVisibleFinalised() {
        assertFalse(testTask.getObservableFinalised().get());
    }

    @Test
    void setFinalisedTrue() {
        testTask.setFinalised(true);
        assertTrue(testTask.getFinalised());
    }

    @Test
    void setFinalisedFalse() {
        testTask.setFinalised(true);
        System.out.println("Current finalised status: " + testTask.getFinalised());
        testTask.setFinalised(false);
        assertFalse(testTask.getFinalised());

    }

    @Test
    void editTaskName() {
        testTask.editTaskName("Modified task name");
        assertEquals("Modified task name", testTask.getTaskName());
    }

    @Test
    void editTaskDetails() {
        testTask.editTaskDetails("Modified task details");
        assertEquals("Modified task details", testTask.getDetails());
    }

    @Test
    void moveTask() {
        Day day1 = testTask.getAssignedToDay();
        Day day2 = new Day(LocalDate.now().plusDays(2));
        testTask.moveTask(day2);
        assertEquals(0, day1.getTodayTasks().size());
        assertEquals(1, day2.getTodayTasks().size());
        assertEquals(day2, testTask.getAssignedToDay());

    }
    @Test
    void moveTask_toPast() {
        Day day1 = testTask.getAssignedToDay();
        Day day2 = new Day(LocalDate.now().minusDays(2));
        testTask.moveTask(day2);
        assertEquals(1, day1.getTodayTasks().size());
        assertEquals(0, day2.getTodayTasks().size());
        assertEquals(day1, testTask.getAssignedToDay());
    }

    @Test
    void testEqualsShallowCopy() {
        TrivialTask validation = testTask;
        assertTrue(testTask.equals(validation));
    }

    @Test
    void testEqualsSameValues() {
        TrivialTask validation = new TrivialTask(new Day(LocalDate.now()), "ZadanieTestowe", "Szczegóły niniejszego zadania są zawarte wyłącznie w celach testowych");
        assertTrue(testTask.equals(validation));
    }

    @Test
    void testToString() {
        String validation = "TrivialTask;Date;"+LocalDate.now().toString()+";ZadanieTestowe;Szczegóły niniejszego zadania są zawarte wyłącznie w celach testowych;false;";
        assertEquals(validation, testTask.toString());
    }
}

