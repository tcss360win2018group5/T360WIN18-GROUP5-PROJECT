package test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.Job;
import model.OfficeStaff;
import model.ParkManager;
import model.SystemCoordinator;
import model.User;
import model.Volunteer;

public class SystemCoordinatorTest {
    public SystemCoordinator globalSystemCoordinator;
    public Job globalGenericJob;
    User globalGenericVolunteerJane;
    User globalGenericParkManagerJoe;
    User globalGenericOfficeStaffTammy;

    @Before
    public void setUp() throws Exception {
        globalSystemCoordinator = new SystemCoordinator();
        globalGenericVolunteerJane = new Volunteer("Jane"); //Makes Jane a volunteer.
        globalGenericParkManagerJoe = new ParkManager("Joe"); //Makes Joe a park manager.
        globalGenericOfficeStaffTammy = new OfficeStaff("Tammy");
        globalSystemCoordinator.addUser(globalGenericVolunteerJane);
        globalSystemCoordinator.addUser(globalGenericParkManagerJoe);
        globalSystemCoordinator.addUser(globalGenericOfficeStaffTammy);
        
        
    }


    // Testing User Business Rule 3a.
    // No User can carry out any other user story until they are identified by the system
    @Test
    public final void signIn_isVolunteerInSystem_ShouldReturnTrue() {
        assertTrue(globalSystemCoordinator.signIn(globalGenericVolunteerJane.getUsername()));
    }

    // No User can carry out any other user story until they are identified by the system
    @Test
    public final void signIn_isParkManagerInSystem_ShouldReturnTrue() {
        assertTrue(globalSystemCoordinator.signIn(globalGenericParkManagerJoe.getUsername()));
    }

    // No User can carry out any other user story until they are identified by the system
    @Test
    public final void signIn_isOfficeStaffInSystem_ShouldReturnTrue() {
        assertTrue(globalSystemCoordinator.signIn(globalGenericOfficeStaffTammy.getUsername()));
    }
    
    @Test
    public final void signIn_isNotInSystem_ShouldReturnFalse() {
        Volunteer newVolunteerThatHasNotSignedUp = new Volunteer("SomeVolunteer");
        assertFalse(globalSystemCoordinator
                    .signIn(newVolunteerThatHasNotSignedUp.getUsername()));
    }
}
