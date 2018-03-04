package gui_view;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import model.JobCoordinator;
import model.SystemCoordinator;


public class CalenderAppGUI extends AnchorPane {

    private static final String fxmlFile = "CalenderAppGUI.fxml";

    /**
     * precondition: a user exists inside system coordinator.
     */
    public CalenderAppGUI(Stage theStage, String theUserName, int theUserAccessLevel,
                          SystemCoordinator mySystemCoordinator,
                          JobCoordinator myJobCoordinator) throws Exception {

        // 2: // Volunteer
        // 1: // Park Manager
        // 0: // Urban Parks Staff Member

        // Setup the GUI
        FXMLLoader thisGUI = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent main = thisGUI.load();
        CalenderAppController calender_controller = thisGUI.getController();

        // Init the values in the controller, cannot send through constructor
        calender_controller.setUsername(theUserName);
        calender_controller.setAccess(theUserAccessLevel);
        calender_controller.setMySystemCoordinator(mySystemCoordinator);
        calender_controller.setMyJobCoordinator(myJobCoordinator);
        calender_controller.reInitializeWithUser();


        // Display
        Scene mainScene = new Scene(main);
        theStage.setScene(mainScene);
        theStage.setResizable(false);
        theStage.show();
        centerTheScreen(theStage);
    }

    public void centerTheScreen(Stage theStage) {
        Rectangle2D screenSize = Screen.getPrimary().getVisualBounds();
        theStage.setX((screenSize.getWidth() - theStage.getWidth()) / 2);
        theStage.setY((screenSize.getHeight() - theStage.getHeight()) / 2);
    }


    private void volunteerInit() {

    }

    private void managerInit() {

    }

    private void officeInit() {

    }
}
