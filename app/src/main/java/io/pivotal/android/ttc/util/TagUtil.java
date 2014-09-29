package io.pivotal.android.ttc.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import io.pivotal.android.ttc.route.Route;
import io.pivotal.android.ttc.stop.Stop;

public class TagUtil {

    private static SimpleDateFormat formatter;
    private static TimeZone localTimeZone;

    public static String getTag(int hour, int minute, Route route, Stop stop) {

        if (route == null || stop == null) {
            return null;
        }

        final String formattedTime = getFormattedTimeInUtc(hour, minute);
        return String.format("%s_%s_%s", formattedTime, route.tag, stop.stopId);
    }

    private static String getFormattedTimeInUtc(int hour, int minute) {
        final Calendar cal = getCalendar(hour, minute);
        final SimpleDateFormat formatter = getFormatter();
        return formatter.format(cal.getTime());
    }

    private static Calendar getCalendar(int hour, int minute) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeZone(getLocalTimeZone());
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);
        return cal;
    }

    private static SimpleDateFormat getFormatter() {
        if (formatter == null) {
            formatter = new SimpleDateFormat("HHmm", Locale.US);
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        }
        return formatter;
    }

    private static TimeZone getLocalTimeZone() {
        if (localTimeZone == null) {
            localTimeZone = TimeZone.getDefault();
        }
        return localTimeZone;
    }

    // Used in unit tests
    public static void setLocalTimeZone(TimeZone timeZone) {
        localTimeZone = timeZone;
    }
}
