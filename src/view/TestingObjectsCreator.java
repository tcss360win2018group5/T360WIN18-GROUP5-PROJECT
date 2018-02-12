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

    
    public static void main(String[] args) {
        createMaxJobs();
        createOneLessThanMaxJobs();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void createMaxJobs() {
        String jcFileName = "data/testdata/JobCoordinator.MAXIMUMJOBS.ser";
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
    }
    
    private static void createOneLessThanMaxJobs() {
        String jcFileName = "data/testdata/JobCoordinator.ONELESSTHANMAXIMUMJOBS.ser";
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
    }
    
    private static void createNoJobs() {
        String jcFileName = "data/testdata/JobCoordinator.NOJOBS.ser";
        JobCoordinator jcObject = new JobCoordinator();
        
        writeObjectToDisk(jcFileName, jcObject);
    }
    
    private static void createTestUsers() {
        String scFileName = "data/testdata/SystemCoordinator.TESTUSERS.ser";
        SystemCoordinator scObject = new SystemCoordinator();
        
        Volunteer volunteerTest = new Volunteer("volunteerTest");
        ParkManager parkManagerTest = new ParkManager("parkManagerTest");
        OfficeStaff officeStaffTest = new OfficeStaff("officeStaffTest");
        
        scObject.addUser(volunteerTest);
        scObject.addUser(parkManagerTest);
        scObject.addUser(officeStaffTest);
        
        writeObjectToDisk(scFileName, scObject);
    }
    
    private static void createTestVolunteerSignedUpForOneValidJob() {
        String scFileName = "data/testdata/SystemCoordinator.VOLUNTEERWITHSIGNEDUPJOB.ser";
        SystemCoordinator scObject = new SystemCoordinator();
        String jcFileName = "data/testdata/JobCoordinator.VOLUNTEERWITHSIGNEDUPJOB.ser";
        JobCoordinator jcObject = new JobCoordinator();
        
        Volunteer volunteerTest = new Volunteer("volunteerTest");
        
        scObject.addUser(volunteerTest);
        
        Job validJob = new Job("SomeValidJob");
        GregorianCalendar validDate = new GregorianCalendar();
        validDate.add(GregorianCalendar.DAY_OF_YEAR, 10);
        validJob.setStartDate(validDate);
        validJob.setEndDate(validDate);
        volunteerTest.signUpForJob(validJob);
        jcObject.addPendingJob(validJob);
        
        writeObjectToDisk(scFileName, scObject);
        writeObjectToDisk(jcFileName, jcObject);
    }

}
