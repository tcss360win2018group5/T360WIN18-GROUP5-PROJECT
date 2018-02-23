package view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CalenderAppController implements Initializable {

    private String userName;
    private int accessLevel;

    @FXML
    Button volunteerTestButton;
    @FXML
    Button managerTestButton;
    @FXML
    Button staffTestButton;

    // 2: // Volunteer
    // 1: // Park Manager
    // 0: // Urban Parks Staff Member
    public CalenderAppController() {}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        volunteerTestButton.setDisable(true);
        managerTestButton.setDisable(true);
        staffTestButton.setDisable(true);
        volunteerTestButton.setVisible(false);
    }


    public void setUsername(String theName) {
        userName = theName;
    }

    public void setAccess(int theAccess) {
        accessLevel = theAccess;
    }

    public void reInitializeWithUser() {
        volunteerInit();
        managerInit();
        officeInit();
    }

    private void volunteerInit() {
        if (accessLevel == 2) {
            volunteerTestButton.setDisable(false);
            volunteerTestButton.setVisible(true);
        }
    }

    private void managerInit() {
        if (accessLevel == 1) {
            managerTestButton.setDisable(false);
        }
    }

    private void officeInit() {
        if (accessLevel == 0) {
            staffTestButton.setDisable(false);
        }
    }

    @FXML
    private void exitClick() {
        System.exit(0);
    }

    @FXML
    void print1() {
        System.out.println(userName);
    }

    @FXML
    void print2() {
        System.out.println(accessLevel);
    }
}
