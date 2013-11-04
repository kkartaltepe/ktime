package ktime.ui.layout;

import javafx.scene.Node;
import ktime.data.RunHistory;
import ktime.utils.stopwatch.StopwatchListener;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/25/13
 * Time: 9:45 PM
 * To Change this template use File | Settings | File Templates.
 */
public interface SplitContainer {
    public void setActualSegmentTime(int splitIndex, Long time);
    public void setActualSegmentEnd(int splitIndex, Long time);
    public void getActiveSegment(int index);
    public int getActiveSegment();
    public Node getNode();

    void setRunHistory(RunHistory runHistory);

    void reset();

    StopwatchListener getStopwatchListener();
}
