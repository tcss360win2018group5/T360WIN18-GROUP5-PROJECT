package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoginGUI extends Application {

    public static final String fxmlFile = "LoginGUI.fxml";

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent main = FXMLLoader.load(getClass().getResource(fxmlFile));
        Scene mainScene = new Scene(main);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }
}
