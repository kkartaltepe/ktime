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
    SplitTimes displayedRun;
    VBox content;

    public VSplitContainer() {
        content = new VBox();
        segments = new ArrayList<SegmentDisplay>();
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
        RunMetadata runMetadata = runHistory.getRunMetadata();
        displayedRun = runHistory.getSplitsToDisplay();
        for(int i = 0; i < runMetadata.getNumSegments(); i++) {
            SegmentDisplay segmentDisplay = new DefaultSegmentDisplay();
            segmentDisplay.setName(runMetadata.getSegmentName(i));
            segmentDisplay.setImageUri(runMetadata.getSegmentImageUri(i));
            if(displayedRun != null) {
                segmentDisplay.setDisplayTime(displayedRun.getSegmentEndTime(i), null);
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

    private void resetSegments() {
        if(displayedRun != null) {
            for(int i = 0; i < segments.size(); i++) {
                segments.get(i).setDisplayTime(displayedRun.getSegmentEndTime(i), null);
            }
        }

    }

    @Override
    public void reset() {
    }

    @Override
    public Node getNode() {
        return content;
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
                        break;
                    case DISPLAY_DELTA_MODE_CHANGE:
                        switch(change.getDeltaDisplayMode()) {
                            case BEST_RUN:
                            case BEST_SEGMENTS:
                            case NONE:
                        }
                        break;
                    case DISPLAY_SPLIT_MODE_CHANGE:
                        switch(change.getSplitDisplayMode()) {
                            case BEST_SEGMENTS:
                                setLastSegmentTimes(change.getNewBestSplits());
                                break;
                            case BEST_RUN:
                                setLastSegmentTimes(change.getNewAttempt());
                                break;
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

    @Override
    public StopwatchListener getStopwatchListener() {
        return new StopwatchListener() {
            @Override
            public void onChanged(Change change) {
                switch (change.getChangeType()) {
                    case START:
                    case RESET:
                        resetSegments();
                        break;
                    case STOP:
                    case SPLIT: {
                        int changedSegment = change.getChangedSplit() - 1;
                        Long runDelta = change.getNewSplitTime() - displayedRun.getSegmentEndTime(changedSegment);
                        Long segmentDelta = change.getNewSegmentTime() - displayedRun.getSegmentTime(changedSegment);
                        String displayStyle = computeSplitStyleClass(segmentDelta, runDelta);
                        segments.get(changedSegment).setDisplayTime(runDelta, displayStyle);
                        break;
                    } case UNSPLIT: {
                        int changedSegment = change.getChangedSplit() - 1;
                        segments.get(changedSegment).setDisplayTime(displayedRun.getSegmentEndTime(changedSegment), null);
                        break;
                    } case SKIPSPLIT: {
                        int changedSegment = change.getChangedSplit() - 1;
                        segments.get(changedSegment).setDisplayTime(displayedRun.getSegmentEndTime(changedSegment), null);
                        break;
                    }
                }
            }
        };
    }


    private String computeSplitStyleClass(Long segmentDelta, Long runDelta) {
        if(segmentDelta == null || runDelta == null)
            return null; //No styling.
        if(segmentDelta > 0 && runDelta > 0)
            return "positiveRunPositiveSeg";
        else if(segmentDelta > 0 && runDelta < 0)
            return "positiveRunNegativeSeg";
        else if(segmentDelta < 0 && runDelta > 0)
            return "negativeRunPositiveSeg";
//        if(segmentDelta < 0 && runDelta < 0)
        else
            return "negativeRunNegativeSeg";

    }

    private void setLastSegmentTimes(SplitTimes timesToUse) {
        for (int i = 0; i < timesToUse.getNumSegments(); i++) {
            segments.get(i).setDisplayTime(timesToUse.getSegmentEndTime(i), null);
        }
    }

}
