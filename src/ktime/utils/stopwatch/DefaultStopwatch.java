package ktime.utils.stopwatch;

import javafx.beans.InvalidationListener;
import ktime.data.SplitTimes;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/28/13
 * Time: 1:13 AM
 * To Change this template use File | Settings | File Templates.
 */
public class DefaultStopwatch implements ObservableStopwatch {
    private long offset = 0;
    private long normalizingOffset = 0;
    private boolean running = false, paused = false;
    private Long startTime, endTime;
    private List<Long> segmentTimes; //Duration of each split
    private List<Long> splitTimes; //When you split
    private List<StopwatchListener> stopwatchListeners;
    private List<InvalidationListener> invalidationListeners;

    public DefaultStopwatch() {
        endTime = null;
        startTime = null;
        segmentTimes = new ArrayList<Long>();
        splitTimes = new ArrayList<Long>();
        stopwatchListeners = new ArrayList<StopwatchListener>();
        invalidationListeners = new ArrayList<InvalidationListener>();
    }



    @Override
    public void start() {
        start(0);
    }

    @Override
    public void start(long offset) {
        if(running || paused) {
            throw new IllegalStateException("Please stop the stopwatch before starting again");
        }
        this.offset = offset;
        endTime = null;
        normalizingOffset = getTime();
        startTime = 0L;
        segmentTimes.clear();
        splitTimes.clear();
        splitTimes.add(startTime);
        running = true;
        paused = false;
        notifyStopwatchListeners(StopwatchListener.StopwatchEventType.START, startTime, null, splitTimes.size()-1);
    }

    @Override
    public Long split() {
        if(!running) {
            throw new IllegalStateException("start() before trying to split()");
        }
        long splitTime = getNormalizedTime();
        splitTimes.add(splitTime);
        Long segmentTime = calculateSegmentTime(segmentTimes.size());
        segmentTimes.add(segmentTime);
        notifyStopwatchListeners(StopwatchListener.StopwatchEventType.SPLIT, splitTime, segmentTime, splitTimes.size()-1);
        return segmentTime;
    }

    @Override
    public void skipSplit() {
        splitTimes.add(null);
        segmentTimes.add(null);
        notifyStopwatchListeners(StopwatchListener.StopwatchEventType.SKIPSPLIT, null, null, splitTimes.size()-1);
    }

    @Override
    public void unsplit() {
        if(!running || splitTimes.size() < 2) //We will always have at least one segment.
            throw new IllegalStateException("You must split first.");
        splitTimes.remove(splitTimes.size()-1);
        segmentTimes.remove(segmentTimes.size()-1);
        notifyStopwatchListeners(StopwatchListener.StopwatchEventType.UNSPLIT, null, null, splitTimes.size());
    }

    @Override
    public int getNumSegments() { //Span of time from a beginning to an end
        return segmentTimes.size();
    }

    @Override
    public long stop() {
        if(!running)
            throw new IllegalStateException("Stopwatch is not running");
        endTime = getNormalizedTime();
        splitTimes.add(endTime);
        Long segmentTime = calculateSegmentTime(segmentTimes.size());
        segmentTimes.add(segmentTime);
        running = false;
        paused = false;
        notifyStopwatchListeners(StopwatchListener.StopwatchEventType.STOP, endTime, segmentTime, splitTimes.size()-1);
        return endTime + offset; //Because endtime is normalized
    }

    @Override
    public long pause() {
        throw new NotImplementedException();
    }

    @Override
    public void unpause() {
        throw new NotImplementedException();
    }

    @Override
    public long getTotalTime() {
        //TODO: dont have implicit conditions even if it makes it "look" nicer
        if(running)
            return getNormalizedTime() + offset;
        if(endTime != null)
            return endTime + offset;
        else  //Stopped but reset/not started
            return 0;

    }

    @Override
    public Long getCurrentSegmentTime() {
        if(splitTimes.size() == 0)
            return 0L;
        if (!running)
            return segmentTimes.get(segmentTimes.size() -1); //Just get the last segment since we have stopped.
        return getNormalizedTime() - splitTimes.get(splitTimes.size() - 1); //Measure from last split to now.
    }

    @Override
    public Long getSegmentTime(int segmentNum) {
        if(segmentNum < 0 || segmentNum >= segmentTimes.size())
            throw new IllegalArgumentException("Invalid segment (Out of bounds)");
        return segmentTimes.get(segmentNum);
    }

    @Override
    public List<Long> getSegmentTimes() {
        return Collections.unmodifiableList(segmentTimes);
    }

    @Override
    public SplitTimes getSplitTimes() {
        return new SplitTimes(splitTimes);
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isPaused() {
        return paused;
    }

    @Override
    public void reset() {
        offset = 0;
        normalizingOffset = 0;
        endTime = null;
        startTime = null;
        segmentTimes.clear();
        splitTimes.clear();
        running = false;
        paused = false;
        notifyStopwatchListeners(StopwatchListener.StopwatchEventType.RESET, null, null, -1);
    }


    private long getNormalizedTime() {
        return getTime() - normalizingOffset;
    }

    private long getTime() {
        return System.nanoTime();
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

    private void notifyStopwatchListeners(final StopwatchListener.StopwatchEventType eventType,
                                          final Long newSplitTimestamp,
                                          final Long newSegmentTime,
                                          final int changedSplit) {
        StopwatchListener.Change change = new StopwatchListener.Change() {
            @Override
            public boolean isRunning() {
                return running;
            }

            @Override
            public StopwatchListener.StopwatchEventType getChangeType() {
                return eventType;
            }

            @Override
            public int getChangedSplit() {
                return changedSplit;
            }

            @Override
            public Long getNewSegmentTime() {
                return newSegmentTime;
            }

            @Override
            public Long getNewSplitTime() {
                return newSplitTimestamp;
            }
        };
        for(StopwatchListener listener : stopwatchListeners) {
            listener.onChanged(change);
        }
    }

    @Override
    public void addListener(StopwatchListener stopwatchListener) {
        stopwatchListeners.add(stopwatchListener);
    }

    @Override
    public void removeListener(StopwatchListener stopwatchListener) {
        stopwatchListeners.remove(stopwatchListener);
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        invalidationListeners.add(invalidationListener);
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        invalidationListeners.remove(invalidationListener);
    }
}
