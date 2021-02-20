package Models.CalendarModel.CalendarAreasModels;

public class TaskManagementModel {
    private static TaskManagementModel instance;

    public static TaskManagementModel getInstance(){
        if(instance==null){
            instance = new TaskManagementModel();
        }
        return instance;
    }
}
