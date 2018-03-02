package view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.JobCoordinator;
import model.SystemCoordinator;
import model.Volunteer;

public class LoginController implements Initializable {

    private static final String SYSTEM_COORDINATOR_NAME = "data/SystemCoordinator.ser";
    private static final String JOB_COORDINATOR_NAME = "data/JobCoordinator.ser";
    private final SystemCoordinator mySystemCoordinator;
    private final JobCoordinator myJobCoordinator;

    @FXML
    public AnchorPane rootPane;

    @FXML
    private Label wrongLabel;

    @FXML
    private Label tryagainLabel;

    @FXML
    private Label registerLabel;

    @FXML
    private TextField inputUserNameField;

    @FXML
    private PasswordField inputPasswordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    public LoginController() {
        if (doesFileExist(SYSTEM_COORDINATOR_NAME) && doesFileExist(JOB_COORDINATOR_NAME)) {
            mySystemCoordinator = (SystemCoordinator) restoreObject(SYSTEM_COORDINATOR_NAME);
            myJobCoordinator = (JobCoordinator) restoreObject(JOB_COORDINATOR_NAME);
        }
        else {
            mySystemCoordinator = new SystemCoordinator();
            myJobCoordinator = new JobCoordinator();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        inputUserNameField.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
        inputPasswordField.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
    }

    @FXML
    private void loginButtonClick() {
        if (doesUserExist(inputUserNameField.getText())) {
            int user_access_level = getUserAccessLevel(inputUserNameField.getText());
            String user_name = getUserName(inputUserNameField.getText());
            try {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                CalenderAppGUI calender_gui = new CalenderAppGUI(stage, user_name, user_access_level,
                        mySystemCoordinator, myJobCoordinator);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            mySystemCoordinator.getUsers().stream().forEach(u -> System.out.println(u.getUsername()));
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.0),
                    event -> {
                        wrongLabel.setText("Username or password is wrong");
                        tryagainLabel.setText("Please try again");
                    }));
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.2),
                    event -> {
                        wrongLabel.setText("Username or password is wrong");
                        tryagainLabel.setText("");

                    }));
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.4),
                    event -> {
                        wrongLabel.setText("Username or password is wrong");
                        tryagainLabel.setText("Please try again");
                    }));
            timeline.setCycleCount(1);
            timeline.play();
        }

    }

    private boolean doesUserExist(String theUserName) {
        boolean doesUserExist = false;
        int intValueOfUser = mySystemCoordinator.signIn(theUserName);
        if (intValueOfUser == 0) {
            doesUserExist = true;
        }
        return doesUserExist;
    }

    private int getUserAccessLevel(String theUserName) {
        return mySystemCoordinator.getAccessLevel(theUserName);
    }

    private String getUserName(String theUserName) {
        String return_string;
        if (theUserName.length() == 0) {
            return_string = "Username Not Found";
        } else {
            return_string = theUserName;
        }
        return return_string;
    }

    @FXML
    private void registerUserTransition() {
        try {
            AnchorPane child = FXMLLoader.load(getClass().getResource("LoginRegisterGUI.fxml"));
            rootPane.getChildren().setAll(child);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backButtonTransition() {
        try {
            AnchorPane child = FXMLLoader.load(getClass().getResource("LoginGUI.fxml"));
            rootPane.getChildren().setAll(child);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void createUser() {
        if (inputUserNameField.getText().length() <= 4) {
            wrongLabel.setText("");
            tryagainLabel.setText("User name is shorter than 4 letters");
        }
        else if (!doesUserExist(inputUserNameField.getText())) {
            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0),
                    event -> {
                        mySystemCoordinator.addUser(new Volunteer(inputUserNameField.getText()));
                        saveSystem();
                        wrongLabel.setText("User Created!");
                        tryagainLabel.setText("");
                        registerButton.setDisable(true);
                        inputUserNameField.setDisable(true);
                        inputPasswordField.setDisable(true);
                        backButton.setDisable(true);

                    }));
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.5),
                    event -> {
                        try {
                            AnchorPane child = FXMLLoader.load(getClass().getResource("LoginGUI.fxml"));
                            rootPane.getChildren().setAll(child);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }));
            timeline.setCycleCount(1);
            timeline.play();
        } else {
            tryagainLabel.setText("User Already Exists");
        }
    }


    private boolean doesFileExist(String thisPath) {
        boolean fileExist = false;
        File file = new File(thisPath);
        if (file.exists()) {
            fileExist = true;
        }
        return fileExist;
    }

    private static Object restoreObject(String thisName) {
        Object newObject = null;
        try {
            FileInputStream in = new FileInputStream(thisName);
            ObjectInputStream ois = new ObjectInputStream(in);
            newObject = ois.readObject();
            ois.close();
        }
        catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newObject;
    }

    private void saveSystem() {
        writeObjectToDisk(SYSTEM_COORDINATOR_NAME, mySystemCoordinator);
        writeObjectToDisk(JOB_COORDINATOR_NAME, myJobCoordinator);
    }

    private static void writeObjectToDisk(String thisName, Object thisObject) {
        try {
            FileOutputStream out = new FileOutputStream(thisName);
            ObjectOutputStream oos = new ObjectOutputStream(out);
            oos.writeObject(thisObject);
            oos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}
