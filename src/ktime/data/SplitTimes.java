package ktime.data;

import ktime.utils.TimeFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/27/13
 * Time: 10:07 PM
 * To Change this template use File | Settings | File Templates.
 */
public class SplitTimes {
    List<Long> splitTimes;

    public SplitTimes(int numSegments) {
        splitTimes = new ArrayList<Long>();
        for(int i = 0; i < numSegments+1; i++) {
            splitTimes.add(null);
        }
    }

    public SplitTimes(SplitTimes run) {
        this.splitTimes = new ArrayList<Long>(run.splitTimes);
    }

    public SplitTimes(List<Long> splitTimes) {
        this.splitTimes = new ArrayList<Long>(splitTimes);
        normalize();
    }

    public int getNumSegments() {
        return splitTimes.size()-1; //Start and end splitTimes dont count towards segments.
    }

    public Long getSegmentTime(int segment) {
       return calculateSegmentTime(segment);
    }

    public Long getSegmentStartTime(int segment) {
        return splitTimes.get(segment);
    }

    public Long getSegmentEndTime(int segment) {
        return splitTimes.get(segment+1);
    }

    public List<Long> getSegmentTimes() {
        List<Long> segmentTimes = new ArrayList<Long>();
        for(int i = 0; i < getNumSegments(); i++)
            segmentTimes.add(getSegmentTime(i));
        return segmentTimes;
    }

    public void setSegmentTime(int segment, Long time) {
        Long segStart = splitTimes.get(segment);
        if(segStart == null) {
            throw new RuntimeException("Segment has no start time, set the split times or set the previous splits time.");
        }
        long newSegEnd = segStart + time;

        Long nextSegStart = splitTimes.get(getNextNonNullSplit(segment+1));
        if(nextSegStart < newSegEnd) {
            shiftSplits(segment+2, newSegEnd - nextSegStart);
        }

        splitTimes.set(segment+1, newSegEnd);
        normalize();
    }

    private void shiftSplits(int startSplit, long shiftBy) {
        for(int i = startSplit; i < splitTimes.size(); i++ ) {
            if(splitTimes.get(i) != null)
                splitTimes.set(i, splitTimes.get(i)+shiftBy);
        }
        normalize();
    }

    private Long calculateSegmentTime(int segment) {
        if(segment < 0 || segment > splitTimes.size()-2)
            throw new IndexOutOfBoundsException("No such segment exists");

        if(splitTimes.get(segment + 1) == null) //start time
            return null;
        if(splitTimes.get(segment) == null) //end time
            return null;

        return splitTimes.get(segment + 1) - splitTimes.get(segment);
    }

    private int getNextNonNullSplit(int split) {
        while(split < splitTimes.size()) {
            if(splitTimes.get(split) != null)
                return split;
            split++;
        }
        return splitTimes.size() - 1;
    }

    private void normalize() {
        int firstSplit = getNextNonNullSplit(0);
        if(firstSplit == splitTimes.size()-1 && splitTimes.get(firstSplit) != null)
            throw new IllegalStateException("This split time is malformed");
        if(firstSplit == splitTimes.size()-1 && splitTimes.get(firstSplit) == null)
            return;
        long normalizingDelta = 0L - splitTimes.get(firstSplit);
        for(int i = firstSplit; i < splitTimes.size(); i++) {
            Long splitTime = splitTimes.get(i);
            if(splitTime != null) {
                long normalized = splitTime + normalizingDelta;
                splitTimes.set(i, normalized);
            }
        }
    }

    @Override
    public String toString() {
        String ret = "[";
        for(int i = 0; i < splitTimes.size(); i++) {
            ret += TimeFormatter.format(splitTimes.get(i)) + ", ";
        }
        return ret + "]";
    }

    public Long getTotalTime() {
        return splitTimes.get(splitTimes.size()-1) - splitTimes.get(0);
    }
}
