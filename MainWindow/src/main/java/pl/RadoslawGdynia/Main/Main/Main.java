package pl.RadoslawGdynia.Main.Main;

import pl.RadoslawGdynia.Calendar.Calendar.GetBetterCalendar;
import pl.RadoslawGdynia.Calendar.Datasources.CalendarDatasource;
import pl.RadoslawGdynia.Calendar.Datasources.PlanTilesDatasource;
import pl.RadoslawGdynia.Calendar.Datasources.TaskDatasource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;


public class Main extends Application {

    public static final Logger log = LoggerFactory.getLogger(Main.class);


    @Override
    public void init() {
        if(!CalendarDatasource.getInstance().open()) {
            Platform.exit();
            log.error("Connection to DataBase could not be established");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Connection with database could not be established. Please contact Radosław Gdynia");
            alert.showAndWait();
        }
        TaskDatasource.getInstance().open();
        PlanTilesDatasource.getInstance().open();
        GetBetterCalendar.loadCalendar();
    }

    @Override
    public void start(Stage primaryStage){
        try {
            URL url = new File("MainWindow/src/main/resources/GetBetterMain.fxml").toURI().toURL();
            Parent root = FXMLLoader.load(url);
            primaryStage.setTitle("GetBetter!");
            primaryStage.setScene(new Scene(root, 400, 600));

            primaryStage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("GetBetter was unable to initiate. Please contact with Radosław Gdynia to solve the issue");
            alert.showAndWait();
        }
    }


    public static void main(String[] args) {
       launch(args);

    }


    @Override
    public void stop() {
        CalendarDatasource.getInstance().close();
        TaskDatasource.getInstance().close();
        PlanTilesDatasource.getInstance().close();

    }
}
