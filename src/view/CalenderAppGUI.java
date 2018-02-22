package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CalenderAppGUI {

    public static final String fxmlFile = "CalenderAppGUI.fxml";
    Stage subStage;

    public CalenderAppGUI() throws Exception {
        Parent main = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene mainScene = new Scene(main);
        subStage.setScene(mainScene);
        subStage.show();
    }
}
