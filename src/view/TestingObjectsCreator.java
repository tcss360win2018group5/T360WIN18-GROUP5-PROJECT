
package view;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.GregorianCalendar;

import model.Job;
import model.JobCoordinator;
import model.OfficeStaff;
import model.ParkManager;
import model.SystemCoordinator;
import model.Volunteer;
import util.SystemConstants;

public final class TestingObjectsCreator {
    private static Volunteer volunteerTest = new Volunteer("test_volunteer");
    private static ParkManager parkManagerTest = new ParkManager("test_park_manager");
    private static OfficeStaff officeStaffTest = new OfficeStaff("test_officeStf");

    public static void main(String[] args) {
        createTestUsers();
        createMaxJobs();
        createTestUsers();
        createOneLessThanMaxJobs();
        createTestUsers();
        createNoJobs();
        createTestUsers();
        createTestVolunteerSignedUpForOneValidJob();
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
    
    private static void createTestUsers() {
        volunteerTest = new Volunteer("test_volunteer");
        parkManagerTest = new ParkManager("test_park_manager");
        officeStaffTest = new OfficeStaff("test_officeStf");
    }

    private static void createMaxJobs() {
        String jcFileName = "data/testdata/MAX_JOBS.JobCoordinator.ser";
        JobCoordinator jcObject = new JobCoordinator();
        GregorianCalendar date = new GregorianCalendar();

        for (int i = 0; i < SystemConstants.MAXIMUM_JOBS; i++) {
            Job genericJob = new Job("Job " + (i + 1));
            date.add(GregorianCalendar.DAY_OF_YEAR, 1);
            genericJob.setStartDate(date);
            genericJob.setEndDate(date);
            jcObject.addPendingJob(genericJob);
        }
        
        writeObjectToDisk(jcFileName, jcObject);
        
        String scFileName = "data/testdata/MAX_JOBS.SystemCoordinator.ser";
        SystemCoordinator scObject = new SystemCoordinator();
        scObject.addUser(volunteerTest);
        scObject.addUser(parkManagerTest);
        scObject.addUser(officeStaffTest);
        writeObjectToDisk(scFileName, scObject);
    }

    private static void createOneLessThanMaxJobs() {
        String jcFileName = "data/testdata/ONE_LESS_THAN_MAX_JOBS.JobCoordinator.ser";
        JobCoordinator jcObject = new JobCoordinator();
        GregorianCalendar date = new GregorianCalendar();

        for (int i = 0; i < SystemConstants.MAXIMUM_JOBS - 1; i++) {
            Job genericJob = new Job("Job " + (i + 1));
            date.add(GregorianCalendar.DAY_OF_YEAR, 1);
            genericJob.setStartDate(date);
            genericJob.setEndDate(date);
            jcObject.addPendingJob(genericJob);
        }

        writeObjectToDisk(jcFileName, jcObject);
        
        String scFileName = "data/testdata/ONE_LESS_THAN_MAX_JOBS.SystemCoordinator.ser";
        SystemCoordinator scObject = new SystemCoordinator();
        scObject.addUser(volunteerTest);
        scObject.addUser(parkManagerTest);
        scObject.addUser(officeStaffTest);
        writeObjectToDisk(scFileName, scObject);
    }

    private static void createNoJobs() {
        String jcFileName = "data/testdata/NO_JOBS.JobCoordinator.ser";
        JobCoordinator jcObject = new JobCoordinator();

        writeObjectToDisk(jcFileName, jcObject);        
        String scFileName = "data/testdata/NO_JOBS.SystemCoordinator.ser";
        SystemCoordinator scObject = new SystemCoordinator();
        scObject.addUser(volunteerTest);
        scObject.addUser(parkManagerTest);
        scObject.addUser(officeStaffTest);
        writeObjectToDisk(scFileName, scObject);        
    }
    
    private static void createTestVolunteerSignedUpForOneValidJob() {
        String scFileName = "data/testdata/VOLUNTEER_SIGNED_UP_FOR_JOB.SystemCoordinator.ser";
        SystemCoordinator scObject = new SystemCoordinator();
        String jcFileName = "data/testdata/VOLUNTEER_SIGNED_UP_FOR_JOB.JobCoordinator.ser";
        JobCoordinator jcObject = new JobCoordinator();

        scObject.addUser(volunteerTest);
        scObject.addUser(parkManagerTest);
        scObject.addUser(officeStaffTest);
        
        Job validJob = new Job("SomeValidJob");
        GregorianCalendar validDate = new GregorianCalendar();
        validDate.add(GregorianCalendar.DAY_OF_YEAR, 10);
        validJob.setStartDate(validDate);
        validJob.setEndDate(validDate);
        volunteerTest.signUpForJob(validJob);
        jcObject.addPendingJob(validJob);
        Job validJobOverlapsWithOtherJob = new Job("AnotherValidJob");
        validJobOverlapsWithOtherJob.setStartDate(validDate);
        validJobOverlapsWithOtherJob.setEndDate(validDate);
        jcObject.addPendingJob(validJobOverlapsWithOtherJob);

        writeObjectToDisk(scFileName, scObject);
        writeObjectToDisk(jcFileName, jcObject);
    }

}
