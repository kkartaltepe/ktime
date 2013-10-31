package ktime.utils;

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
    int getNumSplits();
    long stop();
    long pause();
    void unpause();
    Long getSplitTime(int splitNum);
    List<Long> getSplitTimes();

    long getTotalTime();
    Long getCurrentSplitTime();

    boolean isRunning();
    boolean isPaused();

}
