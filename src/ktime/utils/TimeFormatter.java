package ktime.utils;

import org.apache.commons.lang3.time.DurationFormatUtils;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/28/13
 * Time: 2:04 AM
 * To Change this template use File | Settings | File Templates.
 */
public class TimeFormatter {

    public static final String NO_TIME = "no time";
    public static final String CANNOT_COMPUTE_DELTA = "CNC Delta";

    /**
     * Will always return a positive value for the length of the represented.
     * @param duration
     * @return
     */
    public static String formatTime(Long duration, boolean signed){
        if(duration == null)
            return NO_TIME;
        String sign = duration < 0 ? "-" : "+";
        duration = Math.abs(duration);
        duration = TimeUnit.NANOSECONDS.toMillis(duration);
        String durationString = DurationFormatUtils.formatDuration(duration, "HH:mm:ss.SSS"); //Does not handle negative durations at all...
        String trimmedString = trimInsignificantDigits(durationString);
        if(signed)
            trimmedString = sign + trimmedString;
        return trimmedString;
    }

    public static String format(Long duration) {
        return formatTime(duration, false);
    }

    public static String formatSigned(Long duration) {
        return formatTime(duration, true);
    }

    public static String formatDelta(Long firstTimeStamp, Long secondTimeStamp) {
        if(firstTimeStamp == null || secondTimeStamp == null)
            return CANNOT_COMPUTE_DELTA;
        return formatTime(firstTimeStamp - secondTimeStamp, true);
    }

    private static String trimInsignificantDigits(String durationString) {
        return durationString.replaceFirst("^(0+:)+0?", "");
    }
}
