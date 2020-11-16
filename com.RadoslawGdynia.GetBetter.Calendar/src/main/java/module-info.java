open module  com.RadoslawGdynia.GetBetter.Calendar {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires slf4j.api;

    exports CalendarControl;
    exports Datasource;
    exports Day;
    exports Controllers;
    exports Task;
    exports Tiles;



}