package Main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {



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
            System.out.println("MESSAGE OF ERROR: " + e.getMessage());
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
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleExitButtonClick () {
        Platform.exit();
    }


    public void handleTaskStatisticsButtonClick(ActionEvent event) {
    }
}
