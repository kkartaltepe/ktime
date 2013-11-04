package ktime.utils.stopwatch;

import javafx.beans.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 11/3/13
 * Time: 8:40 PM
 * To Change this template use File | Settings | File Templates.
 */
public interface ObservableStopwatch extends Stopwatch, Observable {
    void addListener(StopwatchListener stopwatchListener);
    void removeListener(StopwatchListener stopwatchListener);
}
