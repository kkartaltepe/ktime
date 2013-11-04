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
    List<String> splitNames;
    List<String> splitImagesUris;
    int attempts, numSegments;
    boolean displayBestSplits;

    public RunMetadata(int numSegments) {
        this.numSegments = numSegments;
        splitImagesUris = new ArrayList<String>(numSegments);
        splitNames = new ArrayList<String>(numSegments);
        for (int i = 0; i < numSegments; i++) {
            splitImagesUris.add(NO_IMAGE);
            splitNames.add(NO_NAME);
        }
        displayBestSplits = false;
    }

    public String getSegmentName(int splitNum) {
        return splitNames.get(splitNum);
    }

    public String getSegmentImageUri(int splitNum) {
        return splitImagesUris.get(splitNum);
    }

    public boolean displayBestSegments() {
        return displayBestSplits;
    }

    public int numAttempts() {
        return attempts;
    }

    public int getNumSegments() {
        return numSegments;
    }

    public void setNumAttempts(int numAttempts) {
        this.attempts = numAttempts;
    }
}
