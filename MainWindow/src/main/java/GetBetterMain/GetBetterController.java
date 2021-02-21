package GetBetterMain;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Slf4j
public class GetBetterController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
    public void handleCalendarButtonClick(){
        try {

            Stage calendarStage = new Stage();
            URL url = new File("Calendar/src/main/resources/GBCalendar.fxml").toURI().toURL();

            Pane root = FXMLLoader.load(url);

            calendarStage.setScene(new Scene(root,1700, 800));
            calendarStage.setTitle("GetBetter - GBCalendar");
            calendarStage.show();
        }
        catch (Exception e) {
            log.error("MESSAGE OF ERROR: {}", e.getMessage());
            //e.printStackTrace();
        }
    }
    public void handleDiaryButtonClick() {
        try {
            Stage diaryStage = new Stage();
            FXMLLoader diaryLoader = new FXMLLoader();
            Pane root = diaryLoader.load(getClass().getResource("Diary/src/main/resources/MyDiary.fxml").openStream());
            diaryStage.setScene(new Scene(root,800, 500));
            diaryStage.setTitle("Diary");
            diaryStage.show();
        }
        catch (Exception e) {
            log.error("Error while opening Diary window. Message: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleExitButtonClick () {
        Platform.exit();
    }


    public void handleTaskStatisticsButtonClick() {
    }
}
