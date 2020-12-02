package Main;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    public static final Logger log = LoggerFactory.getLogger(Controller.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void handleCalendarButtonClick(){
        try {

            Stage calendarStage = new Stage();
            URL url = new File("com.RadoslawGdynia.GetBetter.Calendar/src/main/resources/GBCalendar.fxml").toURI().toURL();

            Pane root = FXMLLoader.load(url);

            calendarStage.setScene(new Scene(root,1700, 800));
            calendarStage.setTitle("Calendar");
            calendarStage.show();
        }
        catch (Exception e) {
            log.error("MESSAGE OF ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void handleDiaryButtonClick() {
        try {
            Stage diaryStage = new Stage();
            FXMLLoader diaryLoader = new FXMLLoader();
            Pane root = diaryLoader.load(getClass().getResource("com.RadoslawGdynia.GetBetter.Diary/src/main/resources/MyDiary.fxml").openStream());
            diaryStage.setScene(new Scene(root,800, 500));
            diaryStage.setTitle("Diary");
            diaryStage.show();
        }
        catch (Exception e) {
            log.error("Error while opening Diary window. Message: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleExitButtonClick () {
        Platform.exit();
    }


    public void handleTaskStatisticsButtonClick() {
    }
}
