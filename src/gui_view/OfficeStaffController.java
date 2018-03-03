package gui_view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class OfficeStaffController implements Initializable{

    @FXML
    Label currentMaxPendingJobs;

    @FXML
    TextField setMaxPendingJobs;


    @FXML
    Button submitButton;
    @FXML
    Button cancelButton;
    @FXML
    Label submitLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
