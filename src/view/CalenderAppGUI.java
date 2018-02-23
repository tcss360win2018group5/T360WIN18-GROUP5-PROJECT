package view;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class CalenderAppGUI extends AnchorPane {

    public static final String fxmlFile = "CalenderAppGUI.fxml";
//    Stage subStage;

    public CalenderAppGUI(Stage theStage, String theUserName, int theUserAccessLevel) throws Exception {

        // 2: // Volunteer
        // 1: // Park Manager
        // 0: // Urban Parks Staff Member
        System.out.println(theUserName);
        System.out.println(theUserAccessLevel);

        Parent main = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene mainScene = new Scene(main);
        theStage.setScene(mainScene);
        theStage.show();
        centerTheScreen(theStage);
    }

    public void centerTheScreen(Stage theStage) {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        theStage.setX((screenSize.getWidth() - theStage.getWidth()) / 2);
        theStage.setY((screenSize.getHeight() - theStage.getHeight()) / 2);
    }
}
