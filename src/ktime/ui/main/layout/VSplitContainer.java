package ktime.ui.main.layout;

import javafx.scene.Node;
import javafx.scene.layout.VBox;
import ktime.data.RunHistory;
import ktime.data.RunHistoryListener;
import ktime.data.RunMetadata;
import ktime.data.SplitTimes;
import ktime.ui.main.layout.individual.DefaultSegmentDisplay;
import ktime.ui.main.layout.individual.SegmentDisplay;
import ktime.utils.stopwatch.StopwatchListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/25/13
 * Time: 3:16 PM
 * To Change this template use File | Settings | File Templates.
 */
public class VSplitContainer implements SplitContainer{
    List<SegmentDisplay> segments;
    VBox content;

    public VSplitContainer() {
        content = new VBox();
        segments = new ArrayList<SegmentDisplay>();
    }

    @Override
    public void setActualSegmentTime(int segmentIndex, Long time) {
        segments.get(segmentIndex).setActualSegmentTime(time);
        segments.get(segmentIndex).displayLastRunDelta();
    }

    @Override
    public void setActualSegmentEnd(int segmentIndex, Long time) {
        segments.get(segmentIndex).setActualSegmentEnd(time);
        segments.get(segmentIndex).displayLastRunDelta();
    }

    @Override
    public void setActiveSegment(int segmentIndex) {
        segments.get(segmentIndex).activate();
    }

    @Override
    public int getActiveSegment() {
        return 0;  //To Change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setRunHistory(RunHistory runHistory) {
        clearSegments();
        SplitTimes oldSplits;
        RunMetadata runMetadata = runHistory.getRunMetadata();
        if(runMetadata.displayBestSegments())
            oldSplits = runHistory.getBestSplitTimes();
        else
            oldSplits = runHistory.getBestRunTimes();
        for(int i = 0; i < runMetadata.getNumSegments(); i++) {
            SegmentDisplay segmentDisplay = new DefaultSegmentDisplay();
            segmentDisplay.setName(runMetadata.getSegmentName(i));
            segmentDisplay.setImageUri(runMetadata.getSegmentImageUri(i));
            if(oldSplits != null) {
                segmentDisplay.setLastSegmentTime(oldSplits.getSegmentTime(i));
                segmentDisplay.setLastSegmentEnd(oldSplits.getSegmentEndTime(i));
                segmentDisplay.displayLastRunTimes();
            }
            addSegment(segmentDisplay);
        }
    }


    private void addSegment(SegmentDisplay segment) {
        content.getChildren().add(segment.getNode());
        segments.add(segment);
    }

    private void clearSegments() {
        content.getChildren().clear();
        segments.clear();
    }

    @Override
    public void reset() {
        for(SegmentDisplay segmentDisplay : segments) {
            segmentDisplay.setActualSegmentTime(null);
            segmentDisplay.setActualSegmentEnd(null);
            segmentDisplay.displayLastRunTimes();
        }
    }

    @Override
    public Node getNode() {
        return content;
    }

    @Override
    public StopwatchListener getStopwatchListener() {
        return new StopwatchListener() {
            @Override
            public void onChanged(Change change) {
                switch (change.getChangeType()) {
                    case STOP:
                    case SPLIT:
                    case SKIPSPLIT:
                    case UNSPLIT:
                        setActualSegmentTime(change.getChangedSplit()-1, change.getNewSegmentTime());
                        setActualSegmentEnd(change.getChangedSplit()-1, change.getNewSplitTime());
                        break;
                    case START:
                    case RESET:
                        System.out.println("Resetting container");
                        reset();
                        break;

                }
            }
        };
    }

    @Override
    public RunHistoryListener getRunHistoryListener() {
        return new RunHistoryListener() {
            @Override
            public void onChanged(Change change) {
                switch (change.getChangeType()) {
                    case NEW_ATEMPT:
                        if(change.wasBestRun()) {
                            setLastSegmentTimes(change.getNewAttempt());
                        }
                        //TODO: update appropriate splits times with best split times if needed.
                        break;
                    case DISPLAY_MODE_CHANGE:
                        if(change.isDisplayBestSegments()) {
                            setLastSegmentTimes(change.getNewBestSplits());
                        } else {
                            setLastSegmentTimes(change.getNewAttempt());
                        }
                        break;
                    case RESET_ATTEMPTS:
                        break;
                    case RUN_NAME_CHANGE:
                        break;
                    case SEGMENT_NAME_CHANGE:
                        segments.get(change.getAlteredSegment()).setName(change.getNewSegmentName());
                        break;
                    case SEGMENT_IMAGE_CHANGE:
                        segments.get(change.getAlteredSegment()).setName(change.getNewSegmentImageUri());
                        break;

                }
            }
        };
    }

    private void setLastSegmentTimes(SplitTimes timesToUse) {
        for (int i = 0; i < timesToUse.getNumSegments(); i++) {
            segments.get(i).setLastSegmentEnd(timesToUse.getSegmentEndTime(i));
            segments.get(i).setLastSegmentTime(timesToUse.getSegmentTime(i));
            segments.get(i).displayLastRunTimes();
        }
    }

}
