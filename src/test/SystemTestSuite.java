package test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  JobCoordinatorTest.class,
  SystemCoordinatorTest.class,
  ParkManagerTest.class,
  VolunteerTest.class
})

public class SystemTestSuite {}
