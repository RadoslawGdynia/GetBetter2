package Task;

import Day.Day;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkTaskTest {

    public static final Logger log = LoggerFactory.getLogger(WorkTaskTest.class);
    WorkTask testTask;


    @BeforeEach
    public void setup() {
        Day day = new Day(LocalDate.now());
        final String taskName = "WorkTask1";
        String details = "Details of this task are added solely for the purpose of validation the WorkTask class properties";
        LocalDate deadline = LocalDate.now().plusDays(1);

        testTask = new WorkTask(day,taskName, details, deadline);
        for (int i = 0; i <= 5; i++) {
            String nam = "subtask" + i;
            String det = "details of " + nam;
            testTask.addSubtask(new Subtask(day,nam, det, testTask));

        }
        System.out.println("Instance of class WorkTask was created. Conducting a test");
    }

    @Test
    void getDeadline() {
        assertEquals(LocalDate.now().plusDays(1), testTask.getDeadline());
    }

    @Test
    void getSubtasks() {
        List<Subtask> verification = testTask.getSubtasks();
        assertEquals(6, verification.size());
        assertEquals("subtask4", verification.get(4).getTaskName());
        assertEquals("details of subtask2", verification.get(2).getDetails());

    }

    @Test
    void getDeadlineChangeCounter() {
        int comparison = 1;
        LocalDate changeDeadline = LocalDate.now().plusDays(4);
        testTask.editTaskDeadline(changeDeadline);
        assertEquals(comparison, testTask.getDeadlineChangeCounter());
    }

    @Test
    void setFinalised_toFailNoSubtaskCompleted() {
        assertFalse(testTask.setFinalised(true));
    }
    @Test
    void setFinalised_toSucceed() {
        for(Subtask subtask : testTask.getSubtasks()){
            subtask.setFinalised(true);
        }
        assertTrue(testTask.setFinalised(true));
    }

    @Test
    void editTaskDeadline_correct() {
        LocalDate newDeadline = LocalDate.now().plusWeeks(1);
        testTask.editTaskDeadline(newDeadline);
        assertEquals(newDeadline, testTask.getDeadline());

    }
    @Test
    void editTaskDeadline_exceeded() {
        LocalDate newDeadline1 = LocalDate.now().plusWeeks(1);
        LocalDate newDeadline2 = LocalDate.now().plusWeeks(2);
        LocalDate newDeadline3 = LocalDate.now().plusWeeks(3);
        LocalDate newDeadline4 = LocalDate.now().plusWeeks(4);
        testTask.editTaskDeadline(newDeadline1);
        testTask.editTaskDeadline(newDeadline2);
        testTask.editTaskDeadline(newDeadline3);
        testTask.editTaskDeadline(newDeadline4);
        assertEquals(newDeadline3, testTask.getDeadline());

    }

    @Test
    void testToString() {
    }

    @Test
    void addSubtask_tosucceed() {
        int initialSize = testTask.getSubtasks().size();
        Day day = new Day(LocalDate.now());
        Subtask subtaskTest = new Subtask(day,"subtask6", "Was it added?", testTask);
        testTask.addSubtask(subtaskTest);
        assertEquals(initialSize + 1, testTask.getSubtasks().size());
    }

    @Test
    void addExistingSubtask_toFailOnSecondAttempt() {
        int initialSize = testTask.getSubtasks().size();
        Subtask subtaskTest = new Subtask(new Day(LocalDate.now()), "subtask6", "Was it added?", testTask);
        testTask.addSubtask(subtaskTest);
        testTask.addSubtask(subtaskTest);
        assertEquals(initialSize + 1, testTask.getSubtasks().size());
    }
    @Test
    void cancelSubtask() {
        int initialSize = testTask.getSubtasks().size();
        Subtask subtaskToCancel = testTask.getSubtasks().get(testTask.getSubtasks().size() - 1);
        testTask.cancelSubtask(subtaskToCancel);
        assertEquals(initialSize - 1, testTask.getSubtasks().size());
    }
    @Test
    void cancelSubtask_NonExistent() {
        int initialSize = testTask.getSubtasks().size();
        Day day = new Day(LocalDate.now());
        Subtask subtaskTest = new Subtask(day, "DoesNotExistsInList", "Was it removed?", testTask);
        testTask.cancelSubtask(subtaskTest);
        assertEquals(initialSize, testTask.getSubtasks().size());
    }

    @Test
    void testEquals_WorkTask() {
        Day day = new Day(LocalDate.now());
        WorkTask equalsTest = new WorkTask(day,"NotExistsInList", "Was it removed?", LocalDate.now());
        assertFalse(testTask.equals(equalsTest));
        WorkTask shouldBeEqual = new WorkTask(day,"NotExistsInList", "Was it removed?", LocalDate.now());
        assertTrue(equalsTest.equals(shouldBeEqual));
    }

    @Test
    void testEquals_Object() {
        Object equalsTest = new Day(LocalDate.now());
        assertFalse(testTask.equals(equalsTest));
    }

    @Test
    void compareToTest() {
        Day day = new Day(LocalDate.now());
        WorkTask secondTask = new WorkTask(day, "nazwaDrugiego", "trzeci", LocalDate.now().plusDays(9));
        WorkTask thirdTask = new WorkTask(day, "nazwaTrzeciego", "pierwszy", LocalDate.now());
        List<WorkTask> toSort = new ArrayList<WorkTask>();
        toSort.add(testTask);
        toSort.add(secondTask);
        toSort.add(thirdTask);
        toSort.sort((t1, t2) -> t1.compareTo(t2));
        assertEquals("pierwszy",toSort.get(0).getDetails());
        assertEquals("trzeci",toSort.get(2).getDetails());
    }

}