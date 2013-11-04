package ktime.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/30/13
 * Time: 9:19 PM
 * To Change this template use File | Settings | File Templates.
 */
public class RunMetadata {
    public static final String NO_IMAGE = "http://pbs.twimg.com/profile_images/378800000644208214/8b82096eee52f15ee59f0f14a5c2d024_normal.png";
    public static final String NO_NAME = "-";
    String runName;
    List<String> segmentNames;
    List<String> segmentImageUris;
    int attempts, numSegments;
    boolean displayBestSegments;
    List<RunHistoryListener> runHistoryListeners;

    public RunMetadata(String name, int numSegments, List<RunHistoryListener> runHistoryListeners) {
        this.runName = name;
        this.numSegments = numSegments;
        segmentImageUris = new ArrayList<String>(numSegments);
        segmentNames = new ArrayList<String>(numSegments);
        for (int i = 0; i < numSegments; i++) {
            segmentImageUris.add(NO_IMAGE);
            segmentNames.add(NO_NAME);
        }
        displayBestSegments = false;
        this.runHistoryListeners = runHistoryListeners;
    }

    public String getSegmentName(int splitNum) {
        return segmentNames.get(splitNum);
    }

    public void setSegmentName(final int segmentNum, final String name) {
        segmentNames.set(segmentNum, name);
        notifyListeners(new AbstractRunHistoryChange(){
            @Override
            public RunHistoryListener.RunHistoryEventType getChangeType() {
                return RunHistoryListener.RunHistoryEventType.SEGMENT_NAME_CHANGE;
            }

            @Override
            public int getAlteredSegment() {
                return segmentNum;
            }

            @Override
            public String getNewSegmentName() {
                return name;
            }
        });
    }

    public String getSegmentImageUri(int splitNum) {
        return segmentImageUris.get(splitNum);
    }

    public void setSegmentImageUri(final int segmentNum, final String uri) {
        segmentImageUris.set(segmentNum, uri);
        notifyListeners(new AbstractRunHistoryChange() {
            @Override
            public RunHistoryListener.RunHistoryEventType getChangeType() {
                return RunHistoryListener.RunHistoryEventType.SEGMENT_IMAGE_CHANGE;
            }

            @Override
            public int getAlteredSegment() {
                return segmentNum;
            }

            public String getNewSegmentImageUri() {
                return uri;
            }
        });
    }

    public boolean displayBestSegments() {
        return displayBestSegments;
    }

    public void setDisplayBestSegments(final boolean displayBestSegments) {
        this.displayBestSegments = displayBestSegments;
        notifyListeners(new AbstractRunHistoryChange() {
            @Override
            public RunHistoryListener.RunHistoryEventType getChangeType() {
                return RunHistoryListener.RunHistoryEventType.DISPLAY_MODE_CHANGE;
            }

            @Override
            public boolean isDisplayBestSegments() {
                return displayBestSegments;
            }
        });
    }

    public int getNumAttempts() {
        return attempts;
    }

    public int getNumSegments() {
        return numSegments;
    }

    public void setNumAttempts(int numAttempts) {
        this.attempts = numAttempts;
    }

    public void notifyListeners(RunHistoryListener.Change change) {
        for (RunHistoryListener listener : runHistoryListeners) {
            listener.onChanged(change);
        }
    }
}
