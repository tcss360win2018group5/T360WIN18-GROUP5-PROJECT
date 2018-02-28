package view;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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

    ObservableList<Job> observableListOfJobs;
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
        tableviewListOfJobs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Job>() {
            @Override
            public void changed(ObservableValue<? extends Job> observable, Job oldValue, Job newValue) {
                Job test = tableviewListOfJobs.getSelectionModel().getSelectedItem();
                System.out.println(test);
            }
        });

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



}
