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
import javafx.scene.control.Label;
import javafx.util.Duration;
import model.Job;

public class VolunteerController implements Initializable {

    private Job selectedJob;

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

    public void playErrorMessage() {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.0),
                event -> {
                    topErrorMessage
                            .setText("Error");
                    bottomErrorMessage
                            .setText("Cannot unapply job - call for more information");
                }));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.2),
                event -> {
                    topErrorMessage
                            .setText("");
                    bottomErrorMessage
                            .setText("Cannot unapply job - call for more information");
                }));
        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.4),
                event -> {
                    topErrorMessage
                            .setText("Error");
                    bottomErrorMessage
                            .setText("Cannot unapply job - call for more information");
                }));
        timeline.setCycleCount(1);
        timeline.play();
    }

    public Job getSelectedJob() {
        return selectedJob;
    }

    public void setSelectedJob(Job selectedJob) {
        this.selectedJob = selectedJob;
    }
}
