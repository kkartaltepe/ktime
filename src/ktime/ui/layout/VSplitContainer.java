package ktime.ui.layout;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ktime.data.RunHistory;
import ktime.data.RunMetadata;
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

    public void addSplit(SplitDisplay split) {
        content.getChildren().add(split.getNode());
        splits.add(split);
    }

    public void removeSplit(int numSplit) {
        content.getChildren().remove(numSplit);
        splits.remove(numSplit);
    }

    public void clearSplits() {
        content.getChildren().clear();
        splits.clear();
    }

    @Override
    public void setActualSegmentTime(int splitIndex, Long time) {
        splits.get(splitIndex).setActualTime(time);
//        splits.get(splitIndex).displayLastSegmentDelta();
    }

    @Override
    public void setSegmentEndTime(int splitIndex, Long time) {
        splits.get(splitIndex).setActualSegmentEnd(time);
//        splits.get(splitIndex).displayLastRunDelta();

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
    public void setRunHistory(RunHistory runHistory) {
        clearSplits();
        SplitTimes toDisplay;
        RunMetadata runMetadata = runHistory.getRunMetadata();
        if(runMetadata.displayBestSplits())
            toDisplay = runHistory.getBestSplitTimes();
        else
            toDisplay = runHistory.getBestRunTimes();
        for(int i = 0; i < runMetadata.getNumSegments(); i++) {
            SplitDisplay splitDisplay = new DefaultSplitDisplay();
            splitDisplay.setName(runMetadata.getSplitName(i));
            splitDisplay.setImageUrl(runMetadata.getSplitImageUri(i));
            if(toDisplay != null) {
                splitDisplay.setLastTime(toDisplay.getSegmentTime(i));
                splitDisplay.setLastSegmentEnd(toDisplay.getSegmentEndTime(i));
            }
            addSplit(splitDisplay);
        }
    }

    @Override
    public void reset() {
        for(SplitDisplay splitDisplay : splits) {
            splitDisplay.setActualTime(null);
        }
    }

}
