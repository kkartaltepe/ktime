package ktime.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/28/13
 * Time: 2:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class TimeFormatter {

    public static final String NO_TIME = "no time";

    /**
     * Will always return a positive value for the length of the represented.
     * @param duration
     * @return
     */
    public static String format(Long duration){
        if(duration == null)
            return NO_TIME;
        duration = Math.abs(duration);
        duration = TimeUnit.NANOSECONDS.toMillis(duration);
        String durationString = DurationFormatUtils.formatDuration(duration, "HH:mm:ss.SSS"); //Does not handle negative durations at all...
        return trimInsignificantDigits(durationString);
    }

    public static String formatSigned(Long duration) {
        if(duration == null)
            return NO_TIME;
        String sign = duration < 0 ? "-" : "+";
        return sign + format(duration);
    }

    private static String trimInsignificantDigits(String durationString) {
        return durationString.replaceFirst("^(0+:)+0?", "");
    }
}
