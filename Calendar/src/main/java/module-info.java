open module Calendar {

    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires slf4j.api;


    exports CalendarMain;
    exports Models.CalendarModel.Datasources;


}