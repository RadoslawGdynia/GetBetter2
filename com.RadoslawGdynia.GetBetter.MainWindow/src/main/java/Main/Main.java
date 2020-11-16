package Main;

import CalendarControl.GetBetterCalendar;
import Datasource.CalendarDatasource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;


public class Main extends Application {

    public static final Logger log = LoggerFactory.getLogger(Main.class);


    @Override
    public void init() throws Exception {
        if(!CalendarDatasource.getInstance().open()) {
            System.out.println("FATAL ERROR during load of database"); //zmienić na okno dialogowe
            Platform.exit();
        }
        GetBetterCalendar.loadCalendar();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        URL url = new File("com.RadoslawGdynia.GetBetter.MainWindow/src/main/resources/GetBetterMain.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        primaryStage.setTitle("GetBetter!");
        primaryStage.setScene(new Scene(root, 400, 600));


        primaryStage.show();
    }


    public static void main(String[] args) {
       launch(args);
      // GetBetterCalendar.saveCalendar();

    }


    @Override
    public void stop() throws Exception {
        CalendarDatasource.getInstance().close();
    }
}
