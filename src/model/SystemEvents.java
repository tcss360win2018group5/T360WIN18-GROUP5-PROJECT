package model;

/** An list of possible events thrown by the backend for use by the frontend. */
public enum SystemEvents {
    SUBMIT_JOB,
    UNSUBMIT_JOB,
    APPLY_JOB,
    UNAPPLY_JOB,
    SIGNIN,
    MAX_JOBS_CHANGE,
    ERROR;
}
