package gui_view;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class VolunteerController implements Initializable {

    @FXML
    Label jobTitleLabel;
    @FXML
    Label locationLabel;
    @FXML
    Label startDateLabel;
    @FXML
    Label endDateLabel;
    @FXML
    Label jobDescriptionLabel;
    @FXML
    Label maxVolunteersLabel;
    @FXML
    Label contactNameLabel;
    @FXML
    Label contactNumberLabel;
    @FXML
    Label contactEmailLabel;
    @FXML
    Label jobRoleLabel;

    @FXML
    Button submitButton;
    @FXML
    Button cancelButton;
    @FXML
    Label submitLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void submitInfo() {
        //System.out.println("sub");
    }

    /**
     * Prints date of job in correct format that matches the GUI format
     */
    public String printJobDate(final GregorianCalendar theJob) {
        return ((theJob.get(Calendar.MONTH) + 1) + "-" +
                theJob.get(Calendar.DAY_OF_MONTH) + "-" +
                theJob.get(Calendar.YEAR));
    }
}
