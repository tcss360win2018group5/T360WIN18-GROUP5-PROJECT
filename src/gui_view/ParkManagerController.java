package gui_view;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Job;

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
    Button submitButton;
    @FXML
    Button cancelButton;
    @FXML
    Label submitLabel;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        jobTitle.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
//        this.location.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
//        startDate.setStyle("-fx-border: 0; -fx-border-color: transparent; -fx-focus-color: transparent; -fx-text-box-border: transparent;");
//        endDate.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
//        jobDescription.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
//        maxVolunteers.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
//        contactName.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
//        contactNumber.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
//        contactEmail.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
//        jobRole.setStyle("-fx-focus-color: transparent; -fx-text-box-border: transparent;");
    }

    @FXML
    private void submitInfo() {
        System.out.println("sub");
    }

    public static Job gatherJobInfo(String jobTitle,
                                    String location,
                                    String startDateString,
                                    String endDateString,
                                    String jobDescription,
                                    String maxVolunteers,
                                    String contactName,
                                    String contactNumber,
                                    String contactEmail,
                                    String jobRole) {
        Job newJob = new Job(jobTitle);
        newJob.setMyJobLocation(location);
        // 01234567890
        // 2018-03-07
        // YYYY-MM-DD
        // 01234567890
        // MM/DD/YYYY
        GregorianCalendar startDate =
                // YYYY = MM = DD
                new GregorianCalendar(
                        Integer.parseInt(startDateString.substring(0, 4)),
                        Integer.parseInt(startDateString.substring(5, 7)) - 1, //minus 1 for gregorian calendar purposes (months start at 0)
                        Integer.parseInt(startDateString.substring(8, 10)));
        GregorianCalendar endDate =
                new GregorianCalendar(
                        Integer.parseInt(endDateString.substring(0, 4)),
                        Integer.parseInt(endDateString.substring(5, 7)) - 1, //minus 1 for gregorian calendar purposes (months start at 0)
                        Integer.parseInt(endDateString.substring(8, 10)));
        newJob.setStartDate(startDate);
        newJob.setEndDate(endDate);
        newJob.setMyJobDescription(jobDescription);
        newJob.setMaxVolunteers(Integer.parseInt(maxVolunteers));
        newJob.setMyContactName(contactName);
        newJob.setMyContactNumber(contactNumber);
        newJob.setMyContactEmail(contactEmail);
        newJob.setMyJobRole(jobRole);
        return newJob;
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