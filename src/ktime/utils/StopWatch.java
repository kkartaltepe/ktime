package ktime.utils;

import ktime.data.SplitTimes;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/27/13
 * Time: 10:21 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StopWatch {
    void start();
    void start(long startFrom);
    Long split();
    void skipSplit();
    void unsplit();
    int getNumSegments();
    long stop();
    long pause();
    void unpause();
    Long getSegmentTime(int splitNum);
    List<Long> getSegmentTimes();
    SplitTimes getSplitTimes();

    long getTotalTime();
    Long getCurrentSegmentTime();

    boolean isRunning();
    boolean isPaused();

    void reset();
}
