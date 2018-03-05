package gui_view;


import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import model.Job;
import model.JobCoordinator;
import model.OfficeStaff;
import model.ParkManager;
import model.SystemCoordinator;
import model.SystemEvents;
import model.Volunteer;


public class CalenderAppController implements Initializable, PropertyChangeListener {

    private static final String SYSTEM_COORDINATOR_NAME = "data/SystemCoordinator.ser";
    private static final String JOB_COORDINATOR_NAME = "data/JobCoordinator.ser";
    private SystemCoordinator mySystemCoordinator;
    private JobCoordinator myJobCoordinator;
    private Volunteer volunteerUser;
    private ParkManager parkManagerUser;
    private OfficeStaff officeStaffUser;
    private String userName;
    private int accessLevel;

    private Pane rightSideChild;
    private TranslateTransition rightOpen;
    private TranslateTransition rightClose;

    private Pane rightJobSystemSideChild;
    private TranslateTransition rightJobSystemOpen;
    private TranslateTransition rightJobSystemClose;

    private Pane rightJobUserSideChild;
    private TranslateTransition rightJobUserOpen;
    private TranslateTransition rightJobUserClose;

    private Pane leftSideChild;
    private TranslateTransition leftOpen;
    private TranslateTransition leftClose;

    private ObservableList<Job> observable_SystemJobs;
    private ObservableList<Job> observable_UserJobs;

    private Job selectedJobFromSystem;
    private Job selectedJobFromUser;

    private VolunteerController volunteerJobSystemController;
    private VolunteerController volunteerUserJobController;

    private ParkManagerController parkManagerUserJobController;
    private OfficeStaffController officeStaffJobSystemController;

    private OfficeStaffController officeStaffChangeSystemConstController;





    @FXML
    AnchorPane rootPane;
    @FXML
    BorderPane borderPane;
    @FXML
    Pane topPane;
//    @FXML
//    ListView<Job> listviewListOfJobs;
    @FXML
    TableView<Job> tableviewListOfJobs;
    @FXML
    TableColumn<Job, String> jobNameTableColumn;
    @FXML
    TableColumn<Job, String> startDateTableColumn;
    @FXML
    TableColumn<Job, String> endDateTableColumn;

    @FXML
    TableView<Job> tableviewUserListOfJobs;
    @FXML
    TableColumn<Job, String> jobNameUserTableColumn;
    @FXML
    TableColumn<Job, String> startDateUserTableColumn;
    @FXML
    TableColumn<Job, String> endDateUserTableColumn;

    @FXML
    Button volunteerTestButton;
    @FXML
    Button openRightMenu;
    @FXML
    Button staffTestButton;
    @FXML
    Button testListButton;

    @FXML
    Label topLabel_welcomeUser;
    @FXML
    Label topLabel_displayDate;

    @FXML
    Label rightLabel_upperTextLabel;
    @FXML
    Label rightLabel_numberOfJobsLabel;
    @FXML
    Label rightLabel_lowerTextLabel;
    @FXML
    Label rightLabel_bottomLabel;

    @FXML
    Label leftLabel_upperTextLabel;
    @FXML
    Label leftLabel_numberOfAvailableJobsLabel;
    @FXML
    Label leftLabel_lowerTextLabel;
    @FXML
    Label leftLabel_bottomLabel;

    // 2: // Volunteer
    // 1: // Park Manager
    // 0: // Urban Parks Staff Member
    public CalenderAppController() {
        observable_SystemJobs = FXCollections.observableArrayList();
        observable_UserJobs = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Leave blank, cannot load side menu with stuff here.
        tableviewListOfJobs.setStyle("-fx-table-cell-border-color: transparent;");
        tableviewUserListOfJobs.setStyle("-fx-table-cell-border-color: transparent;");       
    }

    // Methods to Init GUI variables
    public void setUsername(String theName) {
        userName = theName;
    }
    public void setAccess(int theAccess) {
        accessLevel = theAccess;
    }
    public void setMySystemCoordinator(SystemCoordinator mySystemCoordinator) {
        this.mySystemCoordinator = mySystemCoordinator;
        this.mySystemCoordinator.addPropertyChangeListener(this);
    }
    public void setMyJobCoordinator(JobCoordinator myJobCoordinator) {
        this.myJobCoordinator = myJobCoordinator;
        this.myJobCoordinator.addPropertyChangeListener(this);
    }
    public void reInitializeWithUser() {
        rootPane.setStyle("-fx-background-color: black;");
        borderPane.setStyle("-fx-background-color: white;");
        volunteerInit();
        managerInit();
        officeInit();
        if (accessLevel == 2) {
            observable_SystemJobs.addAll(myJobCoordinator.getSystemJobListing(volunteerUser));
            observable_UserJobs.addAll(volunteerUser.getCurrentJobs());
        } else if (accessLevel == 1) {
            observable_SystemJobs.addAll(myJobCoordinator.getSystemJobListing(parkManagerUser));
            observable_UserJobs.addAll(parkManagerUser.getSubmittedJobs());
        } else if (accessLevel == 0) {
            // Pass
        }

        ///////// System Jobs /////////
        tableviewListOfJobs.setItems(observable_SystemJobs);
        jobNameTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getJobTitle()));
        startDateTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(printJobDate(cell.getValue().getStartDate())));
        endDateTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(printJobDate(cell.getValue().getEndDate())));

        // Add clicking functionality to the table gui_view list
        tableviewListOfJobs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedJobFromSystem = tableviewListOfJobs.getSelectionModel().getSelectedItem();
            if (selectedJobFromSystem != null) {
            } else {
            }
        });

        if (accessLevel == 2 || accessLevel == 0) {
            // Double Click
            tableviewListOfJobs.setOnMousePressed(e -> {
                if (e.getClickCount() == 2 && selectedJobFromSystem != null) {
                    updateJobSystemLabels();
                    rightJobSystemMenuAnimation();
                }
            });
        }

        ///////// User Jobs /////////
        tableviewUserListOfJobs.setItems(observable_UserJobs);
        jobNameUserTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getJobTitle()));
        startDateUserTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(printJobDate(cell.getValue().getStartDate())));
        endDateUserTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(printJobDate(cell.getValue().getEndDate())));

        // Adding Functionality
        tableviewUserListOfJobs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedJobFromUser = tableviewUserListOfJobs.getSelectionModel().getSelectedItem();
            if (selectedJobFromUser != null) {
                updateJobUserLabels();
            } else {
            }
        });

        // Double Click
        tableviewUserListOfJobs.setOnMousePressed(e -> {
            if (e.getClickCount() == 2 && selectedJobFromUser != null) {
                updateJobUserLabels();
                rightJobUserMenuAnimation();
            }
        });

        // Set Top User Label
        String type = "";
        if (mySystemCoordinator.getAccessLevel(userName) == SystemCoordinator.OFFICE_STAFF_ACCESS_LEVEL) {
            type = "an Office Staff user!";
        } else if (mySystemCoordinator.getAccessLevel(userName) == SystemCoordinator.PARK_MANAGER_ACCESS_LEVEL) {
            type = "a Park Manager user!";
        } else if (mySystemCoordinator.getAccessLevel(userName) == SystemCoordinator.VOLUNTEER_ACCESS_LEVEL) {
            type = "a Volunteer user!";
        }
        topLabel_welcomeUser.setText("... | Welcome back to Urban Parks " + userName + "! You are " + type );
        topLabel_displayDate.setText("Today: " + printJobDate(new GregorianCalendar()));


        // Create Right Menu
        createRightMenu();
        createLeftMenu();

        // Create Table Double Clicking Menu
        createJobSystemMenu();
        createJobUserMenu();

        updateJobLabels();
        updateOfficeLabel();
    }

    private void updateJobSystemLabels() {
        if (accessLevel == 2) {
            volunteerJobSystemController.jobTitleLabel
                    .setText(selectedJobFromSystem.getJobTitle());
            volunteerJobSystemController.startDateLabel
                    .setText(volunteerJobSystemController.printJobDate(selectedJobFromSystem.getStartDate()));
            volunteerJobSystemController.endDateLabel
                    .setText(volunteerJobSystemController.printJobDate(selectedJobFromSystem.getEndDate()));
            volunteerJobSystemController.locationLabel
                    .setText(selectedJobFromSystem.getMyJobLocation());
            volunteerJobSystemController.jobRoleLabel
                    .setText(selectedJobFromSystem.getMyJobRole());
            volunteerJobSystemController.maxVolunteersLabel
                    .setText(String.valueOf(selectedJobFromSystem.getCurrentNumberOfVolunteers()));
            volunteerJobSystemController.jobDescriptionLabel
                    .setText(selectedJobFromSystem.getMyJobDescription());
            volunteerJobSystemController.contactNameLabel
                    .setText(selectedJobFromSystem.getMyContactName());
            volunteerJobSystemController.contactEmailLabel
                    .setText(selectedJobFromSystem.getMyContactEmail());
            volunteerJobSystemController.contactNumberLabel
                    .setText(selectedJobFromSystem.getMyContactNumber());
        } else if (accessLevel == 0) {
            officeStaffJobSystemController.jobTitleLabel
                    .setText(selectedJobFromSystem.getJobTitle());
            officeStaffJobSystemController.startDateLabel
                    .setText(officeStaffJobSystemController.printJobDate(selectedJobFromSystem.getStartDate()));
            officeStaffJobSystemController.endDateLabel
                    .setText(officeStaffJobSystemController.printJobDate(selectedJobFromSystem.getEndDate()));
            officeStaffJobSystemController.locationLabel
                    .setText(selectedJobFromSystem.getMyJobLocation());
            officeStaffJobSystemController.jobRoleLabel
                    .setText(selectedJobFromSystem.getMyJobRole());
            officeStaffJobSystemController.maxVolunteersLabel
                    .setText(String.valueOf(selectedJobFromSystem.getCurrentNumberOfVolunteers()));
            officeStaffJobSystemController.jobDescriptionLabel
                    .setText(selectedJobFromSystem.getMyJobDescription());
            officeStaffJobSystemController.contactNameLabel
                    .setText(selectedJobFromSystem.getMyContactName());
            officeStaffJobSystemController.contactEmailLabel
                    .setText(selectedJobFromSystem.getMyContactEmail());
            officeStaffJobSystemController.contactNumberLabel
                    .setText(selectedJobFromSystem.getMyContactNumber());
        }

    }

    private void updateJobUserLabels() {
        if (accessLevel == 2) {
            // Add job to volunteer controller to check for unvolunteer error.
            volunteerUserJobController.setSelectedJob(selectedJobFromUser);
            volunteerUserJobController.jobTitleLabel
                    .setText(selectedJobFromUser.getJobTitle());
            volunteerUserJobController.startDateLabel
                    .setText(volunteerUserJobController.printJobDate(selectedJobFromUser.getStartDate()));
            volunteerUserJobController.endDateLabel
                    .setText(volunteerUserJobController.printJobDate(selectedJobFromUser.getEndDate()));
            volunteerUserJobController.locationLabel
                    .setText(selectedJobFromUser.getMyJobLocation());
            volunteerUserJobController.jobRoleLabel
                    .setText(selectedJobFromUser.getMyJobRole());
            volunteerUserJobController.maxVolunteersLabel
                    .setText(String.valueOf(selectedJobFromUser.getCurrentNumberOfVolunteers()));
            volunteerUserJobController.jobDescriptionLabel
                    .setText(selectedJobFromUser.getMyJobDescription());
            volunteerUserJobController.contactNameLabel
                    .setText(selectedJobFromUser.getMyContactName());
            volunteerUserJobController.contactEmailLabel
                    .setText(selectedJobFromUser.getMyContactEmail());
            volunteerUserJobController.contactNumberLabel
                    .setText(selectedJobFromUser.getMyContactNumber());
            volunteerUserJobController.topErrorMessage
                    .setText("");
            volunteerUserJobController.bottomErrorMessage
                    .setText("");
        } else if (accessLevel == 1) {
            parkManagerUserJobController.jobTitleLabel
                    .setText(selectedJobFromUser.getJobTitle());
            parkManagerUserJobController.startDateLabel
                    .setText(parkManagerUserJobController.printJobDate(selectedJobFromUser.getStartDate()));
            parkManagerUserJobController.endDateLabel
                    .setText(parkManagerUserJobController.printJobDate(selectedJobFromUser.getEndDate()));
            parkManagerUserJobController.locationLabel
                    .setText(selectedJobFromUser.getMyJobLocation());
            parkManagerUserJobController.jobRoleLabel
                    .setText(selectedJobFromUser.getMyJobRole());
            parkManagerUserJobController.maxVolunteersLabel
                    .setText(String.valueOf(selectedJobFromUser.getCurrentNumberOfVolunteers()));
            parkManagerUserJobController.jobDescriptionLabel
                    .setText(selectedJobFromUser.getMyJobDescription());
            parkManagerUserJobController.contactNameLabel
                    .setText(selectedJobFromUser.getMyContactName());
            parkManagerUserJobController.contactEmailLabel
                    .setText(selectedJobFromUser.getMyContactEmail());
            parkManagerUserJobController.contactNumberLabel
                    .setText(selectedJobFromUser.getMyContactNumber());
        }

    }

    private void createJobSystemMenu() {
        try {
            if (accessLevel == 2) { // Volunteer - Apply to Job

                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutVolunteerShowSystemJob.fxml"));
                rightJobSystemSideChild = fxml.load();
                volunteerJobSystemController = fxml.getController();
                // Add button functionality
                volunteerJobSystemController.cancelButton.setOnAction(event -> {
                    rightJobSystemMenuAnimation();
                    backgroundEnable();
                });
                volunteerJobSystemController.submitButton.setOnAction(event -> {
                    if (selectedJobFromSystem.canAcceptVolunteers() && 
                                    volunteerUser.canApplyToJob(selectedJobFromSystem) == 0) {
                        volunteerJobSystemController.topErrorMessage.setText("");
                        volunteerJobSystemController.bottomErrorMessage.setText("");
                        applyToJob();
                        rightJobSystemMenuAnimation();
                    } else {
                        volunteerJobSystemController.playErrorMessage();
                    }
                });

            } else if (accessLevel == 1) { // PM

                // CHANGE ALL
                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutVolunteerShowSystemJob.fxml"));
                rightJobSystemSideChild = fxml.load();
                VolunteerController subController = fxml.getController();
                // Add functionality
                subController.cancelButton.setOnAction(event -> {
                    rightJobSystemMenuAnimation();
                    backgroundEnable();
                });

            } else if (accessLevel == 0) { // Staff

                // CHANGE ALL
                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutOfficeShowSystemJob.fxml"));
                rightJobSystemSideChild = fxml.load();
                officeStaffJobSystemController = fxml.getController();
                // Add functionality
                officeStaffJobSystemController.cancelButton.setOnAction(event -> {
                    rightJobSystemMenuAnimation();
                    backgroundEnable();
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setJobSystemRightPanel();
    }

    private void createJobUserMenu() {
        try {
            if (accessLevel == 2) { // Volunteer - Unvolunteer Job

                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutVolunteerShowOwnJob.fxml"));
                rightJobUserSideChild = fxml.load();
                volunteerUserJobController = fxml.getController();
                // Add functionality

                volunteerUserJobController.cancelButton.setOnAction(event -> {
                    rightJobUserMenuAnimation();
                    backgroundEnable();
                });
                volunteerUserJobController.submitButton.setOnAction(event -> {
                    // Error check to unvolunteer from job.
                    if (volunteerUser.canUnapplyFromJob(volunteerUserJobController.getSelectedJob()) != 0) {
                        
                        System.out.println(volunteerUser.canUnapplyFromJob(volunteerUserJobController.getSelectedJob()));
                        System.out.println(volunteerUser.getCurrentJobs());
                        System.out.println( ((Volunteer) mySystemCoordinator.getUser(userName)).getCurrentJobs());
                        volunteerUserJobController.playErrorMessage();
                    } else {
                        volunteerUserJobController.topErrorMessage.setText("");
                        volunteerUserJobController.bottomErrorMessage.setText("");
                        unapplyFromJob();
                        rightJobUserMenuAnimation();
                    }
                });

            } else if (accessLevel == 1) { // PM - Unsubmit Job

                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutManagerUnsubmitJob.fxml"));
                rightJobUserSideChild = fxml.load();
                parkManagerUserJobController = fxml.getController();
                // Add functionality
                parkManagerUserJobController.cancelButton.setOnAction(event -> {
                    rightJobUserMenuAnimation();
                    backgroundEnable();
                });
                parkManagerUserJobController.submitButton.setOnAction(event -> {
                    if (myJobCoordinator.canUnsubmitJob(selectedJobFromUser) != 0) {
                        parkManagerUserJobController.playErrorMessage();   
                    } else {
                        parkManagerUserJobController.topErrorMessage.setText("");
                        parkManagerUserJobController.bottomErrorMessage.setText("");
                        unsubmitJob();
                        updateJobLabels();
                        rightJobUserMenuAnimation();
                    }
                });

            } else if (accessLevel == 0) { // Office

                // CHANGE ALL
                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutVolunteerShowOwnJob.fxml"));
                rightJobUserSideChild = fxml.load();
                VolunteerController subController = fxml.getController();
                // Add functionality
                subController.cancelButton.setOnAction(event -> {
                    rightJobUserMenuAnimation();
                    backgroundEnable();
                });
                subController.submitButton.setOnAction(event -> {
                    jobConfirmationAnimation("Test");
                    rightJobUserMenuAnimation();
                });

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setJobUserRightPanel();
    }

    private void createRightMenu() {
        try {
            if (accessLevel == 2) { // Volunteer - Pointless for now

                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutVolunteer.fxml"));
                rightSideChild = fxml.load();
                VolunteerController subController = fxml.getController();
                // Add functionality
                subController.cancelButton.setOnAction(event -> {
                    rightMenuAnimation();
                    backgroundEnable();
                });
                subController.submitButton.setOnAction(event -> {
                    jobConfirmationAnimation("Pointless!");
                    rightMenuAnimation();
                });

            } else if (accessLevel == 1) { // Park Manager - Display Submit Job
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutManager.fxml"));
                rightSideChild = fxml.load();
                ParkManagerController subController = fxml.getController();
                // Add functionality
                final boolean numberErrorInput[] = new boolean[1];
                numberErrorInput[0] = false;

                final String[] jobTitle = new String[1];
                final String[] startDate = new String[1];
                final String[] endDate = new String[1];
                final String[] location = new String[1];
                final String[] jobRole = new String[1];
                final int[] maxVolunteers = new int[1];
                final String[] jobDescription = new String[1];
                final String[] contactName = new String[1];
                final String[] contactEmail = new String[1];
                final String[] contactNumber = new String[1];

                subController.jobTitle.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    jobTitle[0] = (s.get());
                });
                subController.startDate.valueProperty().addListener(e ->{
                    ObjectProperty o = (ObjectProperty) e;
                    startDate[0] = (o.get().toString());
                });
                subController.endDate.valueProperty().addListener(e ->{
                    ObjectProperty o = (ObjectProperty) e;
                    endDate[0] = (o.get().toString());
                });
                subController.location.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    location[0] = (s.get());
                });
                subController.jobRole.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    jobRole[0] = (s.get());
                });
                subController.maxVolunteers.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    try {
                        maxVolunteers[0] = Integer.parseInt((s.get()));
                        numberErrorInput[0] = false;
                    } catch (NumberFormatException error) {
                        numberErrorInput[0] = true;
                    }
                });
                subController.jobDescription.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    jobDescription[0] = (s.get());
                });
                subController.contactName.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    contactName[0] = (s.get());
                });
                subController.contactEmail.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    contactEmail[0] = (s.get());
                });
                subController.contactNumber.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    contactNumber[0] = (s.get());
                });

                subController.cancelButton.setOnAction(event -> {
                    rightMenuAnimation();
                    backgroundEnable();
                });
                subController.submitButton.setOnAction(event -> {
                    // Error Checking
                    if (startDate[0] == null || endDate[0] == null ||
                            maxVolunteers[0] < 1 || numberErrorInput[0] ||
                            jobTitle[0] == null) {
                        subController.playErrorMessage();
                    } else if (jobTitle[0].length() <= 0) {
                        subController.playErrorMessage();
                    } 
                    // Passes first error check - can now create job info
                    Job newJob = ParkManagerController.gatherJobInfo(jobTitle[0], location[0],
                            startDate[0], endDate[0], jobDescription[0],
                            maxVolunteers[0], contactName[0],
                            contactNumber[0], contactEmail[0],
                            jobRole[0]);
                    // Second error check
                    if (myJobCoordinator.canSubmitJob(newJob) != 0 || !myJobCoordinator.hasSpaceToAddJobs()) {
                        subController.playErrorMessage();
                    } else if (newJob.getStartDate().after(newJob.getEndDate())) { // date input wrong
                        subController.playErrorMessage();
                    } else { // Submit if passes all error checks
                        subController.topErrorMessage
                                .setText("");
                        subController.bottomErrorMessage
                                .setText("");

                        submitJob(newJob);
                        updateJobLabels();
                        rightMenuAnimation();
                    }
                });

            } else if (accessLevel == 0) { // Staff - Change System Constants
                // CHANGE ALL THIS
                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutStaffChangeConstants.fxml"));
                rightSideChild = fxml.load();
                officeStaffChangeSystemConstController = fxml.getController();
                // Add functionality
                final boolean numberErrorInput[] = new boolean[1];
                numberErrorInput[0] = false;
                final int[] maxPendingJobs = new int[1];
                final GregorianCalendar[] startDate = new GregorianCalendar[1];
                final GregorianCalendar[] endDate = new GregorianCalendar[1];
                officeStaffChangeSystemConstController.setMaxPendingJobs.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    try {
                        maxPendingJobs[0] = Integer.parseInt((s.get()));
                        numberErrorInput[0] = false;
                    } catch (NumberFormatException error) {
                        numberErrorInput[0] = true;
                    }
                });
                officeStaffChangeSystemConstController.startDate.valueProperty().addListener(e ->{
                    ObjectProperty o = (ObjectProperty) e;
                    startDate[0] = officeStaffChangeSystemConstController
                            .convertToGregorianCalender((o.get().toString()));
                });
                officeStaffChangeSystemConstController.endDate.valueProperty().addListener(e ->{
                    ObjectProperty o = (ObjectProperty) e;
                    endDate[0] = officeStaffChangeSystemConstController
                            .convertToGregorianCalender((o.get().toString()));
                });

                officeStaffChangeSystemConstController.cancelButton.setOnAction(event -> {
                    rightMenuAnimation();
                    backgroundEnable();
                });
                officeStaffChangeSystemConstController.submitButton.setOnAction(event -> {
                    try {
                        // Error Checking
                        if (maxPendingJobs[0] < 1 || numberErrorInput[0]) {
                            officeStaffChangeSystemConstController.playErrorMessage();
                        } else { // Submit if passes checks
                            officeStaffChangeSystemConstController.topErrorMessage
                                    .setText("");
                            officeStaffChangeSystemConstController.bottomErrorMessage
                                    .setText("");
                            officeStaffUser.setMaxPendingJobs(maxPendingJobs[0]);
                            myJobCoordinator.setMaximumJobsInSystem(officeStaffUser, officeStaffUser.getMaxPendingJobs());

                            if (startDate[0] != null) {
                                officeStaffUser.setStartDate(startDate[0]);
                            }
                            if (endDate[0] != null) {
                                officeStaffUser.setEndDate(endDate[0]);
                            }

                            updateOfficeLabel();
                            updateJobLabels();
                            rightMenuAnimation();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        setRightPanel();
    }


    private void createLeftMenu() {
        try {
            // Create the side bar
            FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutLogout.fxml"));
            leftSideChild = fxml.load();
            LogoutController subController = fxml.getController();
            // Add functionality
            subController.exitButton.setOnAction(event -> {
                exitClick();
            });
            subController.hideButton.setOnAction(event -> {
                leftMenuAnimation();
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        setLeftPanel();
    }

    private void setRightPanel() {
        // Set the panel off the screen to the right
        rightSideChild.setLayoutX(rootPane.getPrefWidth());
        rootPane.getChildren().add(rightSideChild);
        rightOpen = new TranslateTransition(new Duration(350), rightSideChild);
        rightOpen.setToX(-(rightSideChild.getWidth()));
        rightClose = new TranslateTransition(new Duration(350), rightSideChild);
        rightClose.setToX(-(rightSideChild.getWidth()));
    }

    private void setJobSystemRightPanel() {
        // Set the panel off the screen to the right
        rightJobSystemSideChild.setLayoutX(rootPane.getPrefWidth());
        rootPane.getChildren().add(rightJobSystemSideChild);
        rightJobSystemOpen = new TranslateTransition(new Duration(350), rightJobSystemSideChild);
        rightJobSystemOpen.setToX(-(rightJobSystemSideChild.getWidth()));
        rightJobSystemClose = new TranslateTransition(new Duration(350), rightJobSystemSideChild);
        rightJobSystemClose.setToX(-(rightJobSystemSideChild.getWidth()));
    }

    private void setJobUserRightPanel() {
        // Set the panel off the screen to the right
        rightJobUserSideChild.setLayoutX(rootPane.getPrefWidth());
        rootPane.getChildren().add(rightJobUserSideChild);
        rightJobUserOpen = new TranslateTransition(new Duration(350), rightJobUserSideChild);
        rightJobUserOpen.setToX(-(rightJobUserSideChild.getWidth()));
        rightJobUserClose = new TranslateTransition(new Duration(350), rightJobUserSideChild);
        rightJobUserClose.setToX(-(rightJobUserSideChild.getWidth()));
    }


    private void setLeftPanel() {
        // Set the panel off the screen to the right
        leftSideChild.setLayoutX(-leftSideChild.getPrefWidth());
        rootPane.getChildren().add(leftSideChild);
        leftOpen = new TranslateTransition(new Duration(350), leftSideChild);
        leftOpen.setToX(-(rightSideChild.getWidth()));
        leftClose = new TranslateTransition(new Duration(350), leftSideChild);
        leftClose.setToX(-(rightSideChild.getWidth()));
    }

    private void jobConfirmationAnimation(String whatLabelToShow) {
        try {
            FXMLLoader fxmlLoad = new FXMLLoader(getClass().getResource("showSuccess.fxml"));
            Pane fxml = fxmlLoad.load();

            ShowSuccessLabelController subController = fxmlLoad.getController();
            subController.showLabel.setText(whatLabelToShow);

            rootPane.getChildren().add(fxml);
            fxml.setLayoutX(-fxml.getPrefWidth()*2);
            fxml.setLayoutY(rootPane.getPrefHeight()/3);

            TranslateTransition left = new TranslateTransition(new Duration(350), fxml);

            Timeline timeline = new Timeline();
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.1),
                    event -> {
                        left.setToX((fxml.getWidth()));
                        left.play();
                    }));
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(.5),
                    event -> {
                        left.setToX((fxml.getWidth()*2));
                        left.play();
                    }));
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1.5),
                    event -> {
                        left.setToX((fxml.getWidth()*4));
                        left.play();
                        backgroundEnable();

                    }));
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(2.3),
                    event -> {
                        rootPane.getChildren().remove(fxml);
                    }));
            timeline.setCycleCount(1);
            timeline.play();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Prints date of job in correct format
     */
    private String printJobDate(final GregorianCalendar theJob) {
        return ((theJob.get(Calendar.MONTH) + 1) + "/" + theJob.get(Calendar.DAY_OF_MONTH) + "/"
                + theJob.get(Calendar.YEAR));
    }

    // Methods to save the system.
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


    private void volunteerInit() {
        if (accessLevel == 2) {
            volunteerUser = (Volunteer) mySystemCoordinator.getUser(userName);
        }
    }

    private void managerInit() {
        if (accessLevel == 1) {
            parkManagerUser = (ParkManager) mySystemCoordinator.getUser(userName);
        }
    }

    private void officeInit() {
        if (accessLevel == 0) {
            officeStaffUser = (OfficeStaff) mySystemCoordinator.getUser(userName);
        }
    }
    
    @FXML
    private void exitClick() {
        saveSystem();
        System.exit(0);
    }

    @FXML
    private void applyToJob() {
        if (accessLevel == 2) {
            if (selectedJobFromSystem != null) {
                myJobCoordinator.applyToJob(volunteerUser, selectedJobFromSystem);
            }
        }
        updateJobLabels();
    }

    @FXML
    private void unapplyFromJob() {
        if (accessLevel == 2) {
            if (selectedJobFromUser != null) {
                myJobCoordinator.unapplyFromJob(volunteerUser, selectedJobFromUser);
            }
        }
        updateJobLabels();
    }

    @FXML
    private void submitJob(Job theJob) {
        if (accessLevel == 1) {
            if (theJob != null) {
                myJobCoordinator.submitJob(mySystemCoordinator.getUser(userName), theJob);
            }
        }
        updateJobLabels();
    }

    @FXML
    private void unsubmitJob() {
        if (accessLevel == 1) {
            if (selectedJobFromUser != null) {
                myJobCoordinator.unsubmitJob(parkManagerUser, selectedJobFromUser);
            }
        }
        updateJobLabels();
    }

    @FXML
    private void rightMenuAnimation() {
        if (rightSideChild.getTranslateX()!=0){
            rightOpen.play();
        } else {
            rightClose.setToX(-(rightSideChild.getWidth()));
            backgroundDisable();
            rightClose.play();
        }
    }

    @FXML
    private void rightJobSystemMenuAnimation() {
        if (rightJobSystemSideChild.getTranslateX()!=0){
            rightJobSystemOpen.play();
        } else {
            rightJobSystemClose.setToX(-(rightJobSystemSideChild.getWidth()));
            backgroundDisable();
            rightJobSystemClose.play();
        }
    }

    @FXML
    private void rightJobUserMenuAnimation() {
        if (rightJobUserSideChild.getTranslateX()!=0){
            rightJobUserOpen.play();
        } else {
            rightJobUserClose.setToX(-(rightJobUserSideChild.getWidth()));
            backgroundDisable();
            rightJobUserClose.play();
        }
    }


    @FXML
    private void leftMenuAnimation() {
        if (leftSideChild.getTranslateX()!=0){
            backgroundEnable();
            leftOpen.play();
        } else {
            leftClose.setToX((leftSideChild.getWidth()));
            backgroundDisable();
            leftClose.play();
        }
    }

    private void updateJobLabels() {
        updateSystem();
        if (accessLevel == 2) { // Vol
            updateJobs(SystemCoordinator.VOLUNTEER_ACCESS_LEVEL);
            leftLabel_upperTextLabel.setText("There is");
            // Change to - change - the number of volunteer jobs that can be applied
            int numberOfAvailableJobs = observable_SystemJobs.size();
            leftLabel_numberOfAvailableJobsLabel.setText(String.valueOf(numberOfAvailableJobs));
            leftLabel_lowerTextLabel.setText("Jobs available");
            leftLabel_bottomLabel.setText("Double click below to apply");

            rightLabel_upperTextLabel.setText("You are applied to");
            int numberOfJobs = observable_UserJobs.size();
            rightLabel_numberOfJobsLabel.setText(String.valueOf(numberOfJobs));
            if (numberOfJobs <= 1) {
                rightLabel_lowerTextLabel.setText("Job");
            } else {
                rightLabel_lowerTextLabel.setText("Jobs");
            }
            rightLabel_bottomLabel.setText("Double click below to view details");

            openRightMenu.setDisable(true);

            if (numberOfJobs == 0) {
                tableviewUserListOfJobs.setDisable(true);
            } else {
                tableviewUserListOfJobs.setDisable(false);
            }


        } else if (accessLevel == 1) { // PM
            updateJobs(SystemCoordinator.PARK_MANAGER_ACCESS_LEVEL);
            leftLabel_upperTextLabel.setText("There is");
            // Change to - change - the number of volunteer jobs that can be applied
            int numberOfAvailableJobs = observable_SystemJobs.size();
            leftLabel_numberOfAvailableJobsLabel.setText(String.valueOf(numberOfAvailableJobs));
            leftLabel_lowerTextLabel.setText("Jobs available");
            leftLabel_bottomLabel.setText("");

            rightLabel_upperTextLabel.setText("You submitted");
            int numberOfJobs = observable_UserJobs.size();
            rightLabel_numberOfJobsLabel.setText(String.valueOf(numberOfJobs));
            rightLabel_numberOfJobsLabel.setText(String.valueOf(numberOfJobs));
            if (numberOfJobs <= 1) {
                rightLabel_lowerTextLabel.setText("Job");
            } else {
                rightLabel_lowerTextLabel.setText("Jobs");
            }
            if (numberOfJobs == 0) {
                tableviewUserListOfJobs.setDisable(true);
            } else {
                tableviewUserListOfJobs.setDisable(false);
            }
            
            if (myJobCoordinator.hasSpaceToAddJobs()) {
                openRightMenu.setDisable(false);
                rightLabel_bottomLabel.setText("Click here to add a job");
            } else {
                openRightMenu.setDisable(true);
                rightLabel_bottomLabel.setText("System is full of jobs");
            }

            tableviewListOfJobs.setDisable(true);


        } else if (accessLevel == 0) { // Office
            leftLabel_upperTextLabel.setText("There is");
            // Change to - change - the number of volunteer jobs that can be applied
            int numberOfAvailableJobs = myJobCoordinator.getPendingJobs().size();
            leftLabel_numberOfAvailableJobsLabel.setText(String.valueOf(numberOfAvailableJobs));
            leftLabel_numberOfAvailableJobsLabel.setText(String.valueOf(numberOfAvailableJobs));
            leftLabel_lowerTextLabel.setText("Jobs available");
            leftLabel_bottomLabel.setText("");

            rightLabel_upperTextLabel.setText("Max Pending Jobs");
            int numberOfJobs = officeStaffUser.getMaxPendingJobs();
            rightLabel_numberOfJobsLabel.setText(String.valueOf(numberOfJobs));
            rightLabel_numberOfJobsLabel.setText(String.valueOf(numberOfJobs));
            rightLabel_lowerTextLabel.setText("");
            rightLabel_bottomLabel.setText("Click here make system changes");

            tableviewUserListOfJobs.setDisable(true);

        }
    }

    private void updateOfficeLabel() {
        updateSystem();
        if (accessLevel == 0) {
            officeStaffChangeSystemConstController.currentMaxPendingJobs
                    .setText(String.valueOf(officeStaffUser.getMaxPendingJobs()));
            officeStaffChangeSystemConstController.currentStartDate
                    .setText(printJobDate(officeStaffUser.getStartDate()));
            officeStaffChangeSystemConstController.currentEndDate
                    .setText(printJobDate(officeStaffUser.getEndDate()));

            observable_SystemJobs.clear();
            observable_SystemJobs.addAll(officeStaffUser.getJobsBetween2Dates(myJobCoordinator.getPendingJobs()));
        }
    }
    
    private void updateJobs(int user) {
        updateSystem();
        observable_SystemJobs.clear();
        observable_UserJobs.clear();
        if (user == SystemCoordinator.VOLUNTEER_ACCESS_LEVEL) {
            volunteerInit();
            observable_UserJobs.addAll(myJobCoordinator.getUserJobListing(volunteerUser));
            observable_SystemJobs.addAll(myJobCoordinator.getSystemJobListing(volunteerUser));
        } else if (user == SystemCoordinator.PARK_MANAGER_ACCESS_LEVEL) {
            managerInit();
            observable_UserJobs.addAll(myJobCoordinator.getUserJobListing(parkManagerUser));
            observable_SystemJobs.addAll(myJobCoordinator.getSystemJobListing(parkManagerUser));
        } else {
            officeInit();
        }
        
    }

    private void updateSystem() {
        this.mySystemCoordinator = this.myJobCoordinator.getSystem();
    }
    
    private void backgroundEnable() {
        borderPane.setOpacity(1);
        borderPane.setDisable(false);
    }

    private void backgroundDisable() {
        borderPane.setOpacity(.7);
        borderPane.setDisable(true);
    }

    @Override
    public void propertyChange(PropertyChangeEvent theEvent) {
        String eventName = theEvent.getPropertyName();
        if (eventName == SystemEvents.APPLY_JOB.name()) {
            jobConfirmationAnimation("Applied to Job!");
        } else if (eventName == SystemEvents.UNAPPLY_JOB.name()) {
            jobConfirmationAnimation("Unpplied from Job!");
        } else if (eventName == SystemEvents.SUBMIT_JOB.name()) {
            jobConfirmationAnimation("Added Job!");
        } else if (eventName == SystemEvents.UNSUBMIT_JOB.name()) {
            jobConfirmationAnimation("Removed Job!");
        } else if (eventName == SystemEvents.MAX_JOBS_CHANGE.name()) {
            jobConfirmationAnimation("System Changes Accepted!");
        } else if (eventName == SystemEvents.ERROR.name()) {
            jobConfirmationAnimation("Unable to complete action!");
        }
    }

}
