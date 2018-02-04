package util;

import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

/** Wrapper class to make finding date differences easier. */

public final class CalendarCalculator {    
    public static int getDifferenceInDays(GregorianCalendar theFirstDate, 
                                          GregorianCalendar theSecondDate) {
        long milliTime = theFirstDate.getTimeInMillis() - theSecondDate.getTimeInMillis();
        TimeUnit.MILLISECONDS.convert(milliTime, TimeUnit.DAYS);
        return (int) milliTime;
    }   
}
