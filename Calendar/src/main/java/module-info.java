open module Calendar {
    exports Models.CalendarModel;
    exports Models.CalendarModel.Datasources;
    exports Models.CalendarModel.Days;
    exports Controllers;
    exports Models.CalendarModel.Tasks;

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires slf4j.api;
    requires lombok;

}