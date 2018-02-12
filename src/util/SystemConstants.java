
package util;

/**
 * NOTE: ALL CONSTANTS MUST BE DECLARED FINAL AND STATIC. THEY [MUST] BE
 * IMMUTABLE. *
 */
public final class SystemConstants {
    // Constants for Jobs
    public static final int MAXIMUM_JOBS = 20;
    public static final int MAXIMUM_JOB_LENGTH = 3;
    public static final int MAXIMUM_DAYS_AWAY_TO_POST_JOB = 75;

    // Constants for Volunteers
    public static final int MINIMUM_DAYS_BEFORE_JOB_START = 2;

    // Constants for General System
    public static final int DEFAULT_ACCESS_LEVEL = 2;
    public static final int VOLUTNEER_ACCESS_LEVEL = 2;
    public static final int PARK_MANAGER_ACCESS_LEVEL = 1;
    public static final int OFFICE_STAFF_ACCESS_LEVEL = 0;
}
