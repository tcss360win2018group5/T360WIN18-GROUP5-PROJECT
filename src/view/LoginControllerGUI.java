package view;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginControllerGUI {

    @FXML
    private TextField inputUsenNameField;

    @FXML
    private PasswordField inputPasswordField;

    public LoginControllerGUI() {

    }

    @FXML
    private void initialize() {

    }

    @FXML
    private void loginButtonClick() {
        System.out.println("OK");
    }
}
