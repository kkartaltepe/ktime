package ktime.ui.layout;

import javafx.scene.Node;
import ktime.data.RunHistory;
import ktime.ui.SplitDisplay;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/25/13
 * Time: 9:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SplitContainer {
    public void addSplits(List<SplitDisplay> splits);
    public void addSplit(SplitDisplay split, int index);
    public void addSplit(SplitDisplay split);
    public void setActualTime(int splitIndex, Long time);
    public void setActiveSplit(int index);
    public int  getActiveSplit();
    public Node getNode();

    void addRunHistory(RunHistory runHistory);
}
