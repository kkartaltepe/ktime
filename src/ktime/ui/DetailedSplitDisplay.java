package ktime.ui;

import javafx.scene.Node;
import ktime.data.RunHistory;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/25/13
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DetailedSplitDisplay {
    public void setRunHistory(RunHistory history);
    public void setCurrentTime(long currentTime);
    public void setDisplayedSplit(int split);
    public Node getNode();
}
