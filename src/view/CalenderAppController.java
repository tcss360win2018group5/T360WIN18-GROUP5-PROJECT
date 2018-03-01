package view;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.*;

import javafx.animation.TranslateTransition;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
    private Scene rightScene;
    private Pane leftSideChild;
    private Scene leftScene;
    private TranslateTransition rightOpen;
    private TranslateTransition rightClose;
    private TranslateTransition leftOpen;
    private TranslateTransition leftClose;

    ObservableList<Job> observableListOfJobs;

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

    // 2: // Volunteer
    // 1: // Park Manager
    // 0: // Urban Parks Staff Member
    public CalenderAppController() {
        observableListOfJobs = FXCollections.observableArrayList();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        volunteerTestButton.setDisable(true);
        managerTestButton.setDisable(true);
        staffTestButton.setDisable(true);
        volunteerTestButton.setVisible(false);
        rootPane.setStyle("-fx-background-color: black;");
        borderPane.setStyle("-fx-background-color: white;");
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
        volunteerInit();
        managerInit();
        officeInit();
        listOfJobs = myJobCoordinator.getPendingJobs();
        observableListOfJobs.addAll(volunteerUser.getCurrentJobs());
        listviewListOfJobs.setItems(observableListOfJobs);
        tableviewListOfJobs.setItems(observableListOfJobs);
        jobNameTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(cell.getValue().getJobTitle()));
        startDateTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(printJobDate(cell.getValue().getStartDate())));
        endDateTableColumn.setCellValueFactory(cell -> new ReadOnlyStringWrapper(printJobDate(cell.getValue().getEndDate())));
        // Add clicking functionality to the table view list
        tableviewListOfJobs.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Job test = tableviewListOfJobs.getSelectionModel().getSelectedItem();
            System.out.println(test);
        });
        // Create Right Menu
        createRightMenu();
        createLeftMenu();


    }

    private void createRightMenu() {
        try {
            // Create the side bar
            rightSideChild = FXMLLoader.load(getClass().getResource("SlideoutVolunteer.fxml"));
            rightScene = new Scene(rightSideChild);
            // Set the panel off the screen to the right
            rightSideChild.setLayoutX(rootPane.getPrefWidth());
            rootPane.getChildren().add(rightSideChild);
            rightOpen = new TranslateTransition(new Duration(500), rightSideChild);
            rightOpen.setToX(-(rightSideChild.getWidth()));
            rightClose = new TranslateTransition(new Duration(500), rightSideChild);
            rightClose.setToX(-(rightSideChild.getWidth()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createLeftMenu() {
        try {
            // Create the side bar
            leftSideChild = FXMLLoader.load(getClass().getResource("SlideoutVolunteer.fxml"));
            leftScene = new Scene(leftSideChild);
            // Set the panel off the screen to the right
            leftSideChild.setLayoutX(-leftSideChild.getPrefWidth());
            rootPane.getChildren().add(leftSideChild);
            leftOpen = new TranslateTransition(new Duration(500), leftSideChild);
            leftOpen.setToX(-(rightSideChild.getWidth()));
            leftClose = new TranslateTransition(new Duration(500), leftSideChild);
            leftClose.setToX(-(rightSideChild.getWidth()));
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
            volunteerTestButton.setDisable(false);
            volunteerTestButton.setVisible(true);
            managerTestButton.setDisable(false);
        }
    }

    private void managerInit() {
        if (accessLevel == 1) {
            parkManagerUser = (ParkManager) mySystemCoordinator.getUser(userName);
            managerTestButton.setDisable(false);
        }
    }

    private void officeInit() {
        if (accessLevel == 0) {
            officeStaffUser = (OfficeStaff) mySystemCoordinator.getUser(userName);
            staffTestButton.setDisable(false);
        }
    }

    @FXML
    private void populateListWithJobs() {
        volunteerUser.signUpForJob(new Job("Test Job 1"));
        observableListOfJobs.add(new Job("Test Job 1"));
    }

    @FXML
    private void populateListWithJobs2 () {
        Job test2 = new Job("Test Job 2", 2,
                new GregorianCalendar(2018, 03, 11),
                new GregorianCalendar(2018, 03, 11));
        volunteerUser.signUpForJob(test2);
        observableListOfJobs.add(test2);
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
    private void rightMenuAnimation() {
        if (rightSideChild.getTranslateX()!=0){
            borderPane.setOpacity(1);
            borderPane.setDisable(false);
            rightOpen.play();
        } else {
            rightClose.setToX(-(rightSideChild.getWidth()));
            borderPane.setOpacity(.7);
            borderPane.setDisable(true);
            rightClose.play();
        }
    }

    @FXML
    private void leftMenuAnimation() {
        if (leftSideChild.getTranslateX()!=0){
            borderPane.setOpacity(1);
            borderPane.setDisable(false);
            leftOpen.play();
        } else {
            leftClose.setToX((leftSideChild.getWidth()));
            borderPane.setOpacity(.7);
            borderPane.setDisable(true);
            leftClose.play();
        }
    }
}
