package ktime.ui.layout;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ktime.data.RunHistory;
import ktime.data.SplitTimes;
import ktime.ui.DefaultSplitDisplay;
import ktime.ui.SplitDisplay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/25/13
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class VSplitContainer implements SplitContainer{
    List<SplitDisplay> splits;
    VBox content;

    public VSplitContainer() {
        content = new VBox();
        splits = new ArrayList<SplitDisplay>();
    }

    @Override
    public void addSplits(List<SplitDisplay> splits) {
        for (SplitDisplay split : splits) {
            addSplit(split);
        }
    }

    @Override
    public void addSplit(SplitDisplay split, int index) {
        content.getChildren().add(index, split.getNode());
        splits.add(index, split);
    }

    @Override
    public void addSplit(SplitDisplay split) {
        content.getChildren().add(split.getNode());
        splits.add(split);
    }

    @Override
    public void setActualTime(int splitIndex, Long time) {
        splits.get(splitIndex).setActualTime(time);
    }

    @Override
    public void setActiveSplit(int splitIndex) {
        splits.get(splitIndex).activate();
    }

    @Override
    public int getActiveSplit() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Node getNode() {
        return content;
    }

    @Override
    public void addRunHistory(RunHistory runHistory) {
        SplitTimes toDisplay;
        if(runHistory.displayBestSplits())
            toDisplay = runHistory.getBestSplitTimes();
        else
            toDisplay = runHistory.getBestRunTimes();
        for(int i = 0; i < toDisplay.getNumSplits(); i++) {
            SplitDisplay splitDisplay = new DefaultSplitDisplay();
            splitDisplay.setName(runHistory.getSplitName(i));
            splitDisplay.setImageUrl(runHistory.getSplitUri(i));
            splitDisplay.setTime(toDisplay.getTime(i));
            addSplit(splitDisplay);
        }
    }

}
