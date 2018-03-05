package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  JobCoordinatorTest.class,
  SystemCoordinatorTest.class,
  OfficeStaffTest.class,
  ParkManagerTest.class,
  VolunteerTest.class,
  JobTest.class
})

public class SystemTestSuite {}
