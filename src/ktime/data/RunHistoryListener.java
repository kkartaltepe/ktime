package ktime.data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 11/3/13
 * Time: 10:41 PM
 * To change this template use File | Settings | File Templates.
 */
public interface RunHistoryListener {
    public void onChanged(Change change);

    public interface Change {
        public RunHistoryEventType getChangeType();

        public int getChangedSplit();
        public Long getNewSegmentTime();
        public Long getNewSplitTime();

        public boolean wasAttemptSaved();
        public int getNewNumAttempts();
        public SplitTimes getNewAttempt();
        public boolean wasBestRun();
        public List<Integer> getChangedBestSegments();
        public SplitTimes getNewBestSplits();
        public String getNewRunName();
        public int getAlteredSegment();
        public String getNewSegmentName();
        public String getNewSegmentImageUri();
        public DeltaDisplayMode getDeltaDisplayMode();
        public SplitDisplayMode getSplitDisplayMode();

    }

    public enum RunHistoryEventType {
        NEW_ATEMPT, RESET_ATTEMPTS, RUN_NAME_CHANGE, SEGMENT_NAME_CHANGE,
        SEGMENT_IMAGE_CHANGE, DISPLAY_SPLIT_MODE_CHANGE, DISPLAY_DELTA_MODE_CHANGE,
        CURRENT_STATE, START, STOP, RESET, SPLIT, UNSPLIT, SKIPSPLIT
    }

    public enum DeltaDisplayMode {
        BEST_RUN, BEST_SEGMENTS, NONE
    }

    public enum SplitDisplayMode {
        BEST_RUN, BEST_SEGMENTS
    }
}
