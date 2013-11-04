package ktime.ui.layout;

import javafx.scene.Node;
import ktime.data.RunHistory;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/25/13
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SplitContainer {
    public void setActualSegmentTime(int splitIndex, Long time);
    public void setSegmentEndTime(int splitIndex, Long time);
    public void setActiveSplit(int index);
    public int  getActiveSplit();
    public Node getNode();

    void setRunHistory(RunHistory runHistory);

    void reset();
}
