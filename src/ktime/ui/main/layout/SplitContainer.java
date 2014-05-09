package ktime.ui.main.layout;

import javafx.scene.Node;
import ktime.data.RunHistory;
import ktime.data.RunHistoryListener;
import ktime.utils.stopwatch.StopwatchListener;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/25/13
 * Time: 9:45 PM
 * To Change this template use File | Settings | File Templates.
 */
public interface SplitContainer {
    public void setActiveSegment(int index);
    public int getActiveSegment();
    public Node getNode();

    void setRunHistory(RunHistory runHistory);

    void reset();

    RunHistoryListener getRunHistoryListener();
    StopwatchListener getStopwatchListener();
}
