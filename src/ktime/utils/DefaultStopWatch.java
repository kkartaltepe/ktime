package ktime.utils;

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
    private long startTime = 0;
    private long endTime = 0;
    private boolean running = false, paused = false;
    private List<Long> splitTimes;

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
        endTime = 0;
        startTime = getTime();
        splitTimes = new ArrayList<Long>();
        splitTimes.add(startTime);
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
        if(splitTimes.get(splitTimes.size() - 2) == null)
            return null; //Cannot compute length of this split;
        return splitTime - splitTimes.get(splitTimes.size() - 2);
    }

    @Override
    public void skipSplit() {
        splitTimes.add(null);
    }

    @Override
    public void unsplit() {
        if(!running || splitTimes.size() < 2) //We will always have at least one split (read: segment).
            throw new IllegalStateException("You must split first.");
        splitTimes.remove(splitTimes.size()-1);
    }

    @Override
    public int getNumSplits() {
        return splitTimes.size();
    }

    @Override
    public long stop() {
        if(!running)
            throw new IllegalStateException("Stopwatch is not running");
        endTime = getTime();
        splitTimes.add(endTime);
        running = false;
        paused = false;
        return endTime - startTime + offset;
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
            return getTime() - startTime + offset;
        else
            return endTime - startTime + offset;
    }

    @Override
    public Long getCurrentSplitTime() {
        if(splitTimes.size() == 0)
            throw new IllegalStateException("Stopwatch must have been started first.");
        if (!running)
            return splitTimes.get(splitTimes.size() -1) - splitTimes.get(splitTimes.size() -2);
        return getTime() - splitTimes.get(splitTimes.size()-1);
    }


    /**
     * This function gets splits indexed from 0 (even though internally they start at 1)
     * @param splitNum
     * @return
     */
    @Override
    public Long getSplitTime(int splitNum) {
        if(splitNum < 0 || splitNum >= splitTimes.size())
            throw new IllegalArgumentException("split out of bound");
        if(split() == splitTimes.size()-1) //Latest split
            return getCurrentSplitTime();
        if(splitTimes.get(splitNum + 1) == null || splitTimes.get(splitNum) == null)
            return null; //Unable to compute
        return splitTimes.get(splitNum+1) - splitTimes.get(splitNum);
    }

    public List<Long> getSplitTimes() {
        return new ArrayList<Long>(splitTimes);
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public boolean isPaused() {
        return isPaused();
    }


    private long getTime() {
        return System.currentTimeMillis() * 1000 * 1000;
    }
}
