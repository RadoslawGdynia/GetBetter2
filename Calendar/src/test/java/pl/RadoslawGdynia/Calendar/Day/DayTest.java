package pl.RadoslawGdynia.Calendar.Day;

import pl.RadoslawGdynia.Calendar.Task.Task;
import pl.RadoslawGdynia.Calendar.Task.TrivialTask;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DayTest {
    public static final Logger log = LoggerFactory.getLogger(DayTest.class);
    Day test;

    @org.junit.jupiter.api.BeforeEach
    public void setup() {

        test = new Day(LocalDate.now());
        TrivialTask task1 = new TrivialTask(test,"name1", "details1");
        TrivialTask task3 = new TrivialTask(test,"name3", "details3");
        TrivialTask task4 = new TrivialTask(test,"name4", "details4");
        TrivialTask task5 = new TrivialTask(test,"name2", "details2");


        System.out.println("Instance of class Day was created. Conducting a test");
    }

    @Test
    void getDate() {
    }

    @Test
    void getTodayTrivialTasks() {
       ObservableList<Task> copy = test.getTodayTasks();
       int test = 4;
       assertEquals(test, copy.size());
    }

    @Test
    void addTrivialTask() {
        int initialNumberTrivialTasks = test.getTodayTasks().size();
        TrivialTask task1 = new TrivialTask(test, "name4", "details1");
        TrivialTask task3 = new TrivialTask(test,"name35", "details35");
        TrivialTask task4 = new TrivialTask(test,"name55", "details55");
        test.addTask(task1);
        test.addTask(task3);
        test.addTask(task4);
        assertEquals(initialNumberTrivialTasks+3, test.getTodayTasks().size());
    }
    @Test
    void addTrivialTask_Alreadyin() {
        int initialNumberTrivialTasks = test.getTodayTasks().size();

        TrivialTask task1 = new TrivialTask(test,"name1", "details1");
        TrivialTask task3 = new TrivialTask(test, "name3", "details3");
        TrivialTask task4 = new TrivialTask(test,"name4", "details4");
        test.addTask(task1);
        test.addTask(task3);
        test.addTask(task4);
        assertEquals(initialNumberTrivialTasks, test.getTodayTasks().size());
    }

    @Test
    void removeTrivialTask() {
        int initialSize = test.getTodayTasks().size();
        TrivialTask task1 = new TrivialTask(test,"nazwa1", "opis1");
        test.removeTask(task1);
        assertEquals(initialSize-1, test.getTodayTasks().size());
    }


    @Test
    void removeNonExistentTrivialTask() {
        int initialSize = test.getTodayTasks().size();
        TrivialTask task7 = new TrivialTask(test, "nazwa7", "opis7");
        test.removeTask(task7);
        assertEquals(initialSize, test.getTodayTasks().size());
    }

    @Test
    void removeTrivialTaskEmptyList() {
        test.getTodayTasks().clear();
        int initialSize = test.getTodayTasks().size();
        System.out.println("Initial size = " + initialSize);
        TrivialTask task1 = new TrivialTask(test,"nazwa1", "opis1");
        assertFalse(test.removeTask(task1));
    }
    @Test
    void testToString() {

        System.out.println(test.toString());
    }

}