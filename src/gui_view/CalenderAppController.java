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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

    private Job selectedJobFromSystem;
    private Job selectedJobFromUser;

    private Pane rightSideChild;
    private Scene rightScene;
    private Pane leftSideChild;
    private Scene leftScene;
    private TranslateTransition rightOpen;
    private TranslateTransition rightClose;
    private TranslateTransition leftOpen;
    private TranslateTransition leftClose;

    ObservableList<Job> observable_SystemJobs;
    ObservableList<Job> observable_UserJobs;


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
    Button managerTestButton;
    @FXML
    Button staffTestButton;
    @FXML
    Button testListButton;
    @FXML
    Label numberOfJobsLabel;

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
//        managerTestButton.setDisable(true);
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
            observable_UserJobs.addAll(parkManagerUser.getCreatedJobs());
        } else if (accessLevel == 0) {
            // pass
        }

        // User Jobs
        listviewListOfJobs.setItems(observable_UserJobs);
        listviewListOfJobs.setCellFactory(cell -> new ListCell<>() {
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
            System.out.println(selectedJobFromUser.getJobTitle());
            System.out.println(selectedJobFromUser);

        });
        // Double Click
        listviewListOfJobs.setOnMousePressed(e -> {
            if (e.getClickCount() == 2) {
                System.out.println("User Jobs HI");
//                createUserJobMenu();
                rightMenuAnimation();
            }
        });


        // System Jobs
        tableviewListOfJobs.setItems(observable_SystemJobs);
        jobNameTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getJobTitle()));
        startDateTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(printJobDate(cell.getValue().getStartDate())));
        endDateTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(printJobDate(cell.getValue().getEndDate())));

        // Add clicking functionality to the table gui_view list
        tableviewListOfJobs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedJobFromSystem = tableviewListOfJobs.getSelectionModel().getSelectedItem();
            System.out.println(selectedJobFromSystem.getJobTitle());
            System.out.println(selectedJobFromSystem);
        });

        // Double Click
        tableviewListOfJobs.setOnMousePressed(e -> {
            if (e.getClickCount() == 2) {
                System.out.println("System Jobs Hi!");
//                createSystemJobMenu();
                rightMenuAnimation();
            }
        });

        // Create Right Menu
        createRightMenu();
        createLeftMenu();

        updateJobLabels();

    }

    private void createSystemJobMenu() {
        try {
            if (accessLevel == 2) { // Volunteer
                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutVolunteer.fxml"));
                rightSideChild = fxml.load();
                VolunteerController subController = fxml.getController();
                // Add functionality
                subController.submitButton.setOnAction(event -> {
                    System.out.println("Right");
                    jobSubmittedAnimation();
                    rightMenuAnimation();
                });
                subController.cancelButton.setOnAction(event -> {
                    System.out.println("Right");
                    rightMenuAnimation();
                    backgroundEnable();
                });
            } else if (accessLevel == 1) { // Park Manager
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutManager.fxml"));
                rightSideChild = fxml.load();
                ParkManagerController subController = fxml.getController();
                // Add functionality


                subController.submitButton.setOnAction(event -> {
                    System.out.println("Right");

                    rightMenuAnimation();

                });
                subController.cancelButton.setOnAction(event -> {
                    rightMenuAnimation();
                    backgroundEnable();
                });

            } else if (accessLevel == 0) {
                // pass
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        setRightPanel();
    }

    private void createUserJobMenu() {
        try {
            if (accessLevel == 2) { // Volunteer
                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutVolunteer.fxml"));
                rightSideChild = fxml.load();
                VolunteerController subController = fxml.getController();
                // Add functionality
                subController.submitButton.setOnAction(event -> {
                    System.out.println("Right");
                    jobSubmittedAnimation();
                    rightMenuAnimation();
                });
                subController.cancelButton.setOnAction(event -> {
                    System.out.println("Right");
                    rightMenuAnimation();
                    backgroundEnable();
                });
            } else if (accessLevel == 1) { // Park Manager
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutManager.fxml"));
                rightSideChild = fxml.load();
                ParkManagerController subController = fxml.getController();
                // Add functionality


                subController.submitButton.setOnAction(event -> {
                    System.out.println("Right");

                    rightMenuAnimation();

                });
                subController.cancelButton.setOnAction(event -> {
                    rightMenuAnimation();
                    backgroundEnable();
                });

            } else if (accessLevel == 0) {
                // pass
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Set the panel off the screen to the right
        setRightPanel();
    }

    private void createRightMenu() {
        try {
            if (accessLevel == 2) { // Volunteer
                // Create the side bar
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutVolunteer.fxml"));
                rightSideChild = fxml.load();
                VolunteerController subController = fxml.getController();
                // Add functionality
                subController.submitButton.setOnAction(event -> {
                    System.out.println("Right");
                    jobSubmittedAnimation();
                    rightMenuAnimation();
                });
                subController.cancelButton.setOnAction(event -> {
                    System.out.println("Right");
                    rightMenuAnimation();
                    backgroundEnable();
                });
            } else if (accessLevel == 1) { // Park Manager
                FXMLLoader fxml = new FXMLLoader(getClass().getResource("SlideoutManager.fxml"));
                rightSideChild = fxml.load();
                ParkManagerController subController = fxml.getController();
                // Add functionality
                final String[] jobTitle = new String[1];
                final String[] location = new String[1];
                final String[] startDate = new String[1];
                final String[] endDate = new String[1];
                final String[] jobDescription = new String[1];
                final String[] maxVolunteers = new String[1];
                final String[] contactName = new String[1];
                final String[] contactNumber = new String[1];
                final String[] contactEmail = new String[1];
                final String[] jobRole = new String[1];
                final String[] jobRoleDescription = new String[1];

                subController.jobTitle.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    jobTitle[0] = (s.get());
                });
                subController.location.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    location[0] = (s.get());
                });
                subController.startDate.valueProperty().addListener(e ->{
                    ObjectProperty o = (ObjectProperty) e;
                    startDate[0] = (o.get().toString());
                });
                subController.endDate.valueProperty().addListener(e ->{
                    ObjectProperty o = (ObjectProperty) e;
                    endDate[0] = (o.get().toString());
                });
                subController.jobDescription.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    jobDescription[0] = (s.get());
                });
                subController.maxVolunteers.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    maxVolunteers[0] = (s.get());
                });
                subController.contactName.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    contactName[0] = (s.get());
                });
                subController.contactNumber.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    contactNumber[0] = (s.get());
                });
                subController.contactEmail.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    contactEmail[0] = (s.get());
                });
                subController.jobRole.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    jobRole[0] = (s.get());
                });
                subController.jobRoleDescription.textProperty().addListener(e ->{
                    StringProperty s = (StringProperty) e;
                    jobRoleDescription[0] = (s.get());
                });
                subController.submitButton.setOnAction(event -> {
                    System.out.println("Right");
                    Job newJob = ParkManagerController.gatherJobInfo(jobTitle[0], location[0],
                            startDate[0], endDate[0], jobDescription[0],
                            maxVolunteers[0], contactName[0],
                            contactNumber[0], contactEmail[0],
                            jobRole[0], jobRoleDescription[0]);
                    parkManagerUser.addCreatedJob(newJob);
                    jobSubmittedAnimation();
                    observable_SystemJobs.add(newJob);
                    rightMenuAnimation();

                });
                subController.cancelButton.setOnAction(event -> {
                    rightMenuAnimation();
                    backgroundEnable();
                });

            } else if (accessLevel == 0) {
                // pass
            }

//                @Override
//                public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newValue) {
//                    System.out.println("HI");
//                }
//                    @Override
//                    public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
//                        changeContent(newValue);
//                    }
//                });
//
//                contentController.submitLabel().addListener(new ChangeListener<String>() {
//                    @Override
//                    public void changed(ObservableValue<? extends String> obs, String oldValue, String newValue) {
//                        changeContent(newValue);
//                    }
//                });
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

    private void setLeftPanel() {
        // Set the panel off the screen to the right
        leftSideChild.setLayoutX(-leftSideChild.getPrefWidth());
        rootPane.getChildren().add(leftSideChild);
        leftOpen = new TranslateTransition(new Duration(350), leftSideChild);
        leftOpen.setToX(-(rightSideChild.getWidth()));
        leftClose = new TranslateTransition(new Duration(350), leftSideChild);
        leftClose.setToX(-(rightSideChild.getWidth()));
    }

    private void jobSubmittedAnimation() {
        try {
            FXMLLoader fxmlLoad = new FXMLLoader(getClass().getResource("showSuccess.fxml"));
            Pane fxml = fxmlLoad.load();

            ShowSuccessLabelController subController = fxmlLoad.getController();
            if (accessLevel == 2 ) { // Vol
                subController.showLabel.setText("Applied to Job!");
            } else if (accessLevel == 1 ) { // PM
                subController.showLabel.setText("Job Submitted!");
            }
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
//            managerTestButton.setDisable(false);
        }
    }

    private void managerInit() {
        if (accessLevel == 1) {
            parkManagerUser = (ParkManager) mySystemCoordinator.getUser(userName);
//            managerTestButton.setDisable(false);
        }
    }

    private void officeInit() {
        if (accessLevel == 0) {
            officeStaffUser = (OfficeStaff) mySystemCoordinator.getUser(userName);
//            staffTestButton.setDisable(false);
        }
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
        if (accessLevel == 2) {
            int numberOfJobs = volunteerUser.getCurrentJobs().size();
            numberOfJobsLabel.setText(String.valueOf(numberOfJobs));
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