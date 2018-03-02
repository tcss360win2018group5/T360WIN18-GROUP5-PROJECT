package view;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ParkManagerController implements Initializable {

    @FXML
    TextField jobTitle;
    @FXML
    TextField location;
    @FXML
    DatePicker startDate;
    @FXML
    DatePicker endDate;
    @FXML
    TextArea jobDescription;
    @FXML
    TextField maxVolunteers;
    @FXML
    TextField contactName;
    @FXML
    TextField contactNumber;
    @FXML
    TextField contactEmail;
    @FXML
    TextField jobRole;
    @FXML
    TextArea jobRoleDescription;
    @FXML
    Button submitButton;
    @FXML
    Label submitLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void submitInfo() {
        System.out.println("sub");
    }
}