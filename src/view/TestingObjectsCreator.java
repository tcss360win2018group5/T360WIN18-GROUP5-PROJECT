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
        createTestUsers();
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

}
