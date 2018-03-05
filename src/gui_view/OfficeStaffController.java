package gui_view;

import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class OfficeStaffController implements Initializable{

    @FXML
    Label currentMaxPendingJobs;

    @FXML
    TextField setMaxPendingJobs;

    @FXML
    Label currentStartDate;

    @FXML
    Label currentEndDate;

    @FXML
    DatePicker startDate;
    @FXML
    DatePicker endDate;

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
    Label topErrorMessage;
    @FXML
    Label bottomErrorMessage;

    @FXML
    Button submitButton;
    @FXML
    Button cancelButton;
    @FXML
    Label submitLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Prints date of job in correct format that matches the GUI format
     */
    public String printJobDate(final GregorianCalendar theJob) {
        return ((theJob.get(Calendar.MONTH) + 1) + "-" +
                theJob.get(Calendar.DAY_OF_MONTH) + "-" +
                theJob.get(Calendar.YEAR));
    }

    public void playErrorMessage() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.0),
                event -> {
                    topErrorMessage
                            .setText("Try Again");
                    bottomErrorMessage
                            .setText("Please enter a valid number above");
                }));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.2),
                event -> {
                    topErrorMessage
                            .setText("");
                    bottomErrorMessage
                            .setText("Please enter a valid number above");
                }));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.4),
                event -> {
                    topErrorMessage
                            .setText("Try Again");
                    bottomErrorMessage
                            .setText("Please enter a valid number above");
                }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public GregorianCalendar convertToGregorianCalender(String aDate) {
        return new GregorianCalendar(
                Integer.parseInt(aDate.substring(0, 4)),
                Integer.parseInt(aDate.substring(5, 7)) - 1, //minus 1 for gregorian calendar purposes (months start at 0)
                Integer.parseInt(aDate.substring(8, 10)));
    }
}
