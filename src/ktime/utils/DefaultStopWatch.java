package ktime.utils;

import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import ktime.data.SplitTimes;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/28/13
 * Time: 1:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultStopWatch implements StopWatch {
    private long offset = 0;
    private boolean running = false, paused = false;
    private ReadOnlyObjectWrapper<Long> startTime, endTime;
    private ReadOnlyListWrapper<Long> segmentTimes; //Duration of each split
    private List<Long> splitTimes; //When you split

    public DefaultStopWatch() {
        endTime = new ReadOnlyObjectWrapper<Long>(null);
        startTime = new ReadOnlyObjectWrapper<Long>(null);
        segmentTimes = new ReadOnlyListWrapper<Long>(FXCollections.<Long>observableArrayList());
        splitTimes = new ArrayList<Long>();
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
        endTime.set(null);
        startTime.set(getTime());
        segmentTimes.clear();
        splitTimes.clear();
        splitTimes.add(startTime.get());
        running = true;
        paused = false;
    }

    @Override
    public Long split() {
        if(!running) {
            throw new IllegalStateException("start() before trying to split()");
        }
        long splitTime = getTime();
        splitTimes.add(splitTime);
        Long segmentTime = calculateSegmentTime(segmentTimes.size());
        segmentTimes.add(segmentTime);
        return segmentTime;
    }

    @Override
    public void skipSplit() {
        splitTimes.add(null);
        segmentTimes.add(null);
    }

    @Override
    public void unsplit() {
        if(!running || splitTimes.size() < 2) //We will always have at least one split (read: segment).
            throw new IllegalStateException("You must split first.");
        splitTimes.remove(splitTimes.size()-1);
        segmentTimes.remove(segmentTimes.size()-1);
    }

    @Override
    public int getNumSegments() { //Segments colloquial called splits
        return segmentTimes.size();
    }

    @Override
    public long stop() {
        if(!running)
            throw new IllegalStateException("Stopwatch is not running");
        endTime.set(getTime());
        splitTimes.add(endTime.get());
        Long segmentTime = calculateSegmentTime(segmentTimes.size());
        segmentTimes.add(segmentTime);
        running = false;
        paused = false;
        return endTime.get() - startTime.get() + offset;
    }

    @Override
    public long pause() {
        throw new NotImplementedException();
//        if(!running || paused)
//            throw new IllegalStateException("Stopwatch is not running or already paused");
//        offset += System.nanoTime() - startTime;
//        startTime = 0;
//        running = false;
//        return offset;
    }

    @Override
    public void unpause() {
        throw new NotImplementedException();
//        if(!running || !paused)
//            throw new IllegalStateException("Attempted to unpause stopwatch taht wasnt paused or wasnt running");
//        startTime = System.nanoTime();
//        running = true;
    }

    @Override
    public long getTotalTime() {
        if(running)
            return getTime() - startTime.get() + offset;
        if (startTime.get() != null)
            return endTime.get() - startTime.get() + offset;
        else  //Stopped but reset/not started
            return 0;

    }

    @Override
    public Long getCurrentSegmentTime() {
        if(splitTimes.size() == 0)
            throw new IllegalStateException("Stopwatch must have been started first.");
        if (!running)
            return segmentTimes.get(segmentTimes.size() -1); //Just get the last segment since we have stopped.
        return getTime() - splitTimes.get(splitTimes.size()-1); //Measure from last split to now.
    }

    @Override
    public Long getSegmentTime(int segmentNum) {
        if(segmentNum < 0 || segmentNum >= segmentTimes.size())
            throw new IllegalArgumentException("Invalid segment (Out of bounds)");
        return segmentTimes.get(segmentNum);
    }

    @Override
    public List<Long> getSegmentTimes() {
        return segmentTimes.getReadOnlyProperty().get();
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
        endTime.set(null);
        startTime.set(null);
        segmentTimes.clear();
        splitTimes.clear();
        running = false;
        paused = false;
    }

    private long getTime() {
//        return System.currentTimeMillis() * 1000 * 1000;
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

}
