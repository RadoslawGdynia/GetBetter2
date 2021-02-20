open module Calendar {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires slf4j.api;
    requires lombok;


    exports CalendarMain;
    exports Models.CalendarModel.Datasources;


}