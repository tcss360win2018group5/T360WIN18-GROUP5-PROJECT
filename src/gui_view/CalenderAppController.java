package gui_view;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.*;

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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
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
import model.Volunteer;


public class CalenderAppController implements Initializable {

    private static final String SYSTEM_COORDINATOR_NAME = "data/SystemCoordinator.ser";
    private static final String JOB_COORDINATOR_NAME = "data/JobCoordinator.ser";
    private SystemCoordinator mySystemCoordinator;
    private JobCoordinator myJobCoordinator;
    private Volunteer volunteerUser;
    private ParkManager parkManagerUser;
    private OfficeStaff officeStaffUser;
    private String userName;
    private ArrayList<Job> listOfJobs;
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

    private OfficeStaffController officeStaffChangeSystemConstController;





    @FXML
    AnchorPane rootPane;
    @FXML
    BorderPane borderPane;
    @FXML
    Pane topPane;
    @FXML
    ListView<Job> listviewListOfJobs;
    @FXML
    TableView<Job> tableviewListOfJobs;
    @FXML
    TableColumn<Job, String> jobNameTableColumn;
    @FXML
    TableColumn<Job, String> startDateTableColumn;
    @FXML
    TableColumn<Job, String> endDateTableColumn;

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
        listviewListOfJobs.setStyle("-fx-table-cell-border-color: transparent;");
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
    }
    public void setMyJobCoordinator(JobCoordinator myJobCoordinator) {
        this.myJobCoordinator = myJobCoordinator;
    }
    public void reInitializeWithUser() {
//        volunteerTestButton.setDisable(true);
//        openRightMenu.setDisable(true);
//        staffTestButton.setDisable(true);
//        volunteerTestButton.setVisible(false);
        rootPane.setStyle("-fx-background-color: black;");
        borderPane.setStyle("-fx-background-color: white;");
        volunteerInit();
        managerInit();
        officeInit();
        listOfJobs = myJobCoordinator.getPendingJobs();
        if (accessLevel == 2) {
            observable_SystemJobs.addAll(myJobCoordinator.getPendingJobs());
            observable_UserJobs.addAll(volunteerUser.getCurrentJobs());
        } else if (accessLevel == 1) {
            observable_SystemJobs.addAll(myJobCoordinator.getPendingJobs());
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
                System.out.println(selectedJobFromSystem.getJobTitle());
                System.out.println(selectedJobFromSystem);
            } else {
                System.out.println("NULL (System) Job selected - (probably all system jobs gone?).");
            }
        });

        if (accessLevel == 2) {
            // Double Click
            tableviewListOfJobs.setOnMousePressed(e -> {
                if (e.getClickCount() == 2 && selectedJobFromSystem != null) {
                    System.out.println("System Jobs Hi!");
                    updateJobSystemLabels();
                    rightJobSystemMenuAnimation();
                }
            });
        }

        ///////// User Jobs /////////
        listviewListOfJobs.setItems(observable_UserJobs);
        listviewListOfJobs.setCellFactory(cell -> new ListCell<Job>() {
            @Override
            public void updateItem(Job item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(item.getJobTitle());
                }
            }
        });

        // Adding Functionality
        // Highlight Click
        listviewListOfJobs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedJobFromUser = listviewListOfJobs.getSelectionModel().getSelectedItem();
            if (selectedJobFromUser != null) {
                System.out.println(selectedJobFromUser.getJobTitle());
                System.out.println(selectedJobFromUser);
                updateJobUserLabels();
            } else {
                System.out.println("NULL selected (user) job - (probably unapplied all from all jobs in user?)");
            }
        });
        // Double Click
        listviewListOfJobs.setOnMousePressed(e -> {
            if (e.getClickCount() == 2 && selectedJobFromUser != null) {
                System.out.println("User Jobs HI");
                rightJobUserMenuAnimation();
            }
        });

        // Set Top User Label
        topLabel_welcomeUser.setText("... | Welcome back to Urban Parks " + userName + "!");
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
        }

    }

    private void updateJobUserLabels() {
        if (accessLevel == 2) {
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
                    System.out.println("Right");
                    rightJobSystemMenuAnimation();
                    backgroundEnable();
                });
                volunteerJobSystemController.submitButton.setOnAction(event -> {
                    System.out.println("Right");
                    jobSubmittedAnimation("Applied to Job!");
                    addJobFromSystemToUser();
                    rightJobSystemMenuAnimation();
                });

            } else if (accessLevel == 1) { // PM

                // CHANGE ALL
                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutVolunteerShowSystemJob.fxml"));
                rightJobSystemSideChild = fxml.load();
                VolunteerController subController = fxml.getController();
                // Add functionality
                subController.cancelButton.setOnAction(event -> {
                    System.out.println("Right");
                    rightJobSystemMenuAnimation();
                    backgroundEnable();
                });
                subController.submitButton.setOnAction(event -> {
                    System.out.println("Right");
                    jobSubmittedAnimation("");
                    rightJobSystemMenuAnimation();
                });

            } else if (accessLevel == 0) { // Staff

                // CHANGE ALL
                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutVolunteerShowSystemJob.fxml"));
                rightJobSystemSideChild = fxml.load();
                VolunteerController subController = fxml.getController();
                // Add functionality
                subController.cancelButton.setOnAction(event -> {
                    System.out.println("Right");
                    rightJobSystemMenuAnimation();
                    backgroundEnable();
                });
                subController.submitButton.setOnAction(event -> {
                    System.out.println("Right");
                    jobSubmittedAnimation("");
                    rightJobSystemMenuAnimation();
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
                    System.out.println("Right");
                    rightJobUserMenuAnimation();
                    backgroundEnable();
                });
                volunteerUserJobController.submitButton.setOnAction(event -> {
                    System.out.println("Right");
                    jobSubmittedAnimation("Removed Job!");
                    unvolunteerFromJob();
                    rightJobUserMenuAnimation();
                });

            } else if (accessLevel == 1) { // PM - Unsubmit Job

                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutManagerUnsubmitJob.fxml"));
                rightJobUserSideChild = fxml.load();
                parkManagerUserJobController = fxml.getController();
                // Add functionality
                parkManagerUserJobController.cancelButton.setOnAction(event -> {
                    System.out.println("Right");
                    rightJobUserMenuAnimation();
                    backgroundEnable();
                });
                parkManagerUserJobController.submitButton.setOnAction(event -> {
                    System.out.println("Right");
                    jobSubmittedAnimation("Removed Job!");
                    unsubmitJob();
                    updateJobLabels();
                    rightJobUserMenuAnimation();
                });

            } else if (accessLevel == 0) { // Office

                // CHANGE ALL
                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutVolunteerShowOwnJob.fxml"));
                rightJobUserSideChild = fxml.load();
                VolunteerController subController = fxml.getController();
                // Add functionality
                subController.cancelButton.setOnAction(event -> {
                    System.out.println("Right");
                    rightJobUserMenuAnimation();
                    backgroundEnable();
                });
                subController.submitButton.setOnAction(event -> {
                    System.out.println("Right");
                    jobSubmittedAnimation("Test");
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
                    System.out.println("Right");
                    rightMenuAnimation();
                    backgroundEnable();
                });
                subController.submitButton.setOnAction(event -> {
                    System.out.println("Right");
                    jobSubmittedAnimation("Pointless!");
                    rightMenuAnimation();
                });

            } else if (accessLevel == 1) { // Park Manager - Display Submit Job
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutManager.fxml"));
                rightSideChild = fxml.load();
                ParkManagerController subController = fxml.getController();
                // Add functionality
                final String[] jobTitle = new String[1];
                final String[] startDate = new String[1];
                final String[] endDate = new String[1];
                final String[] location = new String[1];
                final String[] jobRole = new String[1];
                final String[] maxVolunteers = new String[1];
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
                    maxVolunteers[0] = (s.get());
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
                    System.out.println("Manager");
                    Job newJob = ParkManagerController.gatherJobInfo(jobTitle[0], location[0],
                            startDate[0], endDate[0], jobDescription[0],
                            maxVolunteers[0], contactName[0],
                            contactNumber[0], contactEmail[0],
                            jobRole[0]);
                    addJob(newJob);
                    jobSubmittedAnimation("Job Submitted!");
                    updateJobLabels();
                    rightMenuAnimation();

                });

            } else if (accessLevel == 0) { // Staff - Change System Constants
                // CHANGE ALL THIS
                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutStaffChangeConstants.fxml"));
                rightSideChild = fxml.load();
                officeStaffChangeSystemConstController = fxml.getController();
                // Add functionality
                final int[] maxPendingJobs = new int[1];
                final GregorianCalendar[] startDate = new GregorianCalendar[1];
                final GregorianCalendar[] endDate = new GregorianCalendar[1];
                officeStaffChangeSystemConstController.setMaxPendingJobs.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    maxPendingJobs[0] = Integer.parseInt((s.get()));
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
                    System.out.println("Staff");
                    rightMenuAnimation();
                    backgroundEnable();
                });
                officeStaffChangeSystemConstController.submitButton.setOnAction(event -> {
                    System.out.println("Staff");
                    try {
                        officeStaffUser.setMaxPendingJobs(maxPendingJobs[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    jobSubmittedAnimation("System Changes Accepted!");

                    officeStaffUser.setStartDate(startDate[0]);
                    officeStaffUser.setEndDate(endDate[0]);

                    updateOfficeLabel();
                    updateJobLabels();
                    rightMenuAnimation();
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        rightScene = new Scene(rightSideChild);
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
//        leftScene = new Scene(leftSideChild);

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

    private void jobSubmittedAnimation(String whatLabelToShow) {
        try {
            FXMLLoader fxmlLoad = new FXMLLoader(getClass().getResource("showSuccess.fxml"));
            Pane fxml = fxmlLoad.load();

            ShowSuccessLabelController subController = fxmlLoad.getController();
//            if (accessLevel == 2 ) { // Vol
//                subController.showLabel.setText(whatLabelToShow);
//            } else if (accessLevel == 1 ) { // PM
//                subController.showLabel.setText(whatLabelToShow);
//            }
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
//            volunteerTestButton.setDisable(false);
//            volunteerTestButton.setVisible(true);
//            openRightMenu.setDisable(false);
        }
    }

    private void managerInit() {
        if (accessLevel == 1) {
            parkManagerUser = (ParkManager) mySystemCoordinator.getUser(userName);
//            openRightMenu.setDisable(false);
        }
    }

    private void officeInit() {
        if (accessLevel == 0) {
            officeStaffUser = (OfficeStaff) mySystemCoordinator.getUser(userName);
//            staffTestButton.setDisable(false);
        }
    }

    private void addJobFromSystemToUser() {
        volunteerUser.signUpForJob(selectedJobFromSystem);
        observable_UserJobs.add(selectedJobFromSystem);
        updateJobLabels();
    }

    @FXML
    private void populateListWithJobs() {
        volunteerUser.signUpForJob(new Job("Test Job 1"));
        observable_UserJobs.add(new Job("Test Job 1"));
    }

    @FXML
    private void populateListWithJobs2 () {
        Job test2 = new Job("Test Job 2", 2,
                new GregorianCalendar(2018, 03, 11),
                new GregorianCalendar(2018, 03, 11));
        volunteerUser.signUpForJob(test2);
        observable_UserJobs.add(test2);
    }

    @FXML
    private void exitClick() {
        saveSystem();
        System.exit(0);
    }

    @FXML
    void print1() {
        System.out.println(userName);
        volunteerUser.getCurrentJobs().forEach(u -> System.out.println(u.getJobTitle()));
        myJobCoordinator.getPendingJobs().forEach(System.out::println);
    }

    @FXML
    void print2() {
        System.out.println(accessLevel);

//        saveSystem();
    }

    @FXML
    private void applyToJob() {
        if (accessLevel == 2) {
            if (selectedJobFromSystem != null) {
                volunteerUser.signUpForJob(selectedJobFromSystem);
                observable_UserJobs.add(selectedJobFromSystem);
            }
        }
        updateJobLabels();
    }

    @FXML
    private void unvolunteerFromJob() {
        if (accessLevel == 2) {
            if (selectedJobFromUser != null) {
                volunteerUser.unvolunteerJob(selectedJobFromUser);
                observable_UserJobs.remove(selectedJobFromUser);
            }
        }
        updateJobLabels();
    }

    @FXML
    private void addJob(Job theJob) {
        if (accessLevel == 1) {
            parkManagerUser.addCreatedJob(theJob);
            parkManagerUser.addSubmittedJob(theJob);
            observable_UserJobs.add(theJob);
            // Add to jobCoor. as well
            observable_SystemJobs.add(theJob);
            myJobCoordinator.submitJob(theJob);
        }
        updateJobLabels();
    }

    @FXML
    private void unsubmitJob() {
        if (accessLevel == 1) {
            if (selectedJobFromUser != null) {
                Job selectedJob = selectedJobFromUser;
//                parkManagerUser.removeSubmittedJob(selectedJobFromUser);
                observable_UserJobs.remove(selectedJob);
                // Add to jobCoor. as well
                observable_SystemJobs.remove(selectedJob);
                parkManagerUser.unSubmitJob(selectedJob);
                parkManagerUser.removeSubmittedJob(selectedJob);
                myJobCoordinator.unsubmitJob(selectedJob, parkManagerUser);

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
        if (accessLevel == 2) { // Vol
            leftLabel_upperTextLabel.setText("There are");
            // Change to - change - the number of volunteer jobs that can be applied
            int numberOfAvailableJobs = myJobCoordinator.getPendingJobs().size();
            leftLabel_numberOfAvailableJobsLabel.setText(String.valueOf(numberOfAvailableJobs));
            leftLabel_lowerTextLabel.setText("Jobs available");
            leftLabel_bottomLabel.setText("Double click below to apply");

            rightLabel_upperTextLabel.setText("You are applied to");
            int numberOfJobs = volunteerUser.getCurrentJobs().size();
            rightLabel_numberOfJobsLabel.setText(String.valueOf(numberOfJobs));
            rightLabel_lowerTextLabel.setText("Jobs");
            rightLabel_bottomLabel.setText("Double click below to view details");

            openRightMenu.setDisable(true);

            if (numberOfJobs == 0) {
                listviewListOfJobs.setDisable(true);
            } else {
                listviewListOfJobs.setDisable(false);
            }


        } else if (accessLevel == 1) { // PM
            leftLabel_upperTextLabel.setText("There are");
            // Change to - change - the number of volunteer jobs that can be applied
            int numberOfAvailableJobs = myJobCoordinator.getPendingJobs().size();
            leftLabel_numberOfAvailableJobsLabel.setText(String.valueOf(numberOfAvailableJobs));
            leftLabel_lowerTextLabel.setText("Jobs available");
            leftLabel_bottomLabel.setText("");

            rightLabel_upperTextLabel.setText("You submitted");
            int numberOfJobs = parkManagerUser.getSubmittedJobs().size();
            rightLabel_numberOfJobsLabel.setText(String.valueOf(numberOfJobs));
            rightLabel_numberOfJobsLabel.setText(String.valueOf(numberOfJobs));
            rightLabel_lowerTextLabel.setText("Jobs");
            rightLabel_bottomLabel.setText("Click here to add a job");

            if (numberOfJobs == 0) {
                listviewListOfJobs.setDisable(true);
            } else {
                listviewListOfJobs.setDisable(false);
            }


        } else if (accessLevel == 0) { // Office
            leftLabel_upperTextLabel.setText("There are");
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
        }
    }

    private void updateOfficeLabel() {
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

    private void backgroundEnable() {
        borderPane.setOpacity(1);
        borderPane.setDisable(false);
    }

    private void backgroundDisable() {
        borderPane.setOpacity(.7);
        borderPane.setDisable(true);
    }

    @FXML
    private void submitInfo() {
        rightMenuAnimation();
        System.out.println("test");
    }
}
