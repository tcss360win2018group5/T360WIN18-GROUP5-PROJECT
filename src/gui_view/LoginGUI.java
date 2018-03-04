package gui_view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginGUI extends Application {

    public static final String fxmlFile = "LoginGUI.fxml";

    @Override
    public void start(Stage theStage) throws Exception {
        Parent main = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene mainScene = new Scene(main);
        theStage.setScene(mainScene);
        theStage.setResizable(false);
        theStage.show();
        centerTheScreen(theStage);
    }

    public void centerTheScreen(Stage theStage) {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        theStage.setX((screenSize.getWidth() - theStage.getWidth()) / 4);
        theStage.setY((screenSize.getHeight() - theStage.getHeight()) / 4);
    }
}
