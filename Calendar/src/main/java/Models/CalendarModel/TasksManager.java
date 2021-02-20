package Models.CalendarModel;

public class TasksManager {
    private static TasksManager instance;

    public static TasksManager getInstance(){
        if(instance == null){
            instance = new TasksManager();
        }
        return instance;
    }
    private TasksManager(){
    }
}
