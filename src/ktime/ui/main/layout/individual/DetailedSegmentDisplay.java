package ktime.ui.main.layout.individual;

import javafx.scene.Node;
import ktime.data.RunHistory;
import ktime.data.RunHistoryListener;
import ktime.utils.stopwatch.StopwatchListener;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/25/13
 * Time: 9:59 PM
 * To Change this template use File | Settings | File Templates.
 */
public interface DetailedSegmentDisplay {
    void setRunHistory(RunHistory history);
    void setCurrentTime(long currentTime);
    void setCurrentSegmentTime(long currentSegmentTime);
    Node getNode();

    RunHistoryListener getRunHistoryListener();
    StopwatchListener getStopwatchListener();
}
