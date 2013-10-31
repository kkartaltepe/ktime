package ktime.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/27/13
 * Time: 10:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class SplitTimes {
    List<Long> splits;

    public SplitTimes(int numSplits) {
        splits = new ArrayList<Long>();
        for(int i = 0; i < numSplits; i++) {
            splits.add(null);
        }
    }

    public SplitTimes(SplitTimes run) {
        splits = new ArrayList<Long>(run.splits);
    }

    public void addSplit(int position) {
        splits.add(position, null);
    }

    public void addSplit() {
        addSplit(splits.size());
    }

    public int getNumSplits() {
        return splits.size();
    }

    public Long getTime(int split) {
        return splits.get(split);
    }

    public List<Long> getTimes() {
        return new ArrayList<Long>(splits);
    }

    public void setTimes(List<Long> times) {
        for (int i = 0; i < times.size(); i++) {
            setTime(i, times.get(i));
        }
    }

    public void setTime(int split, Long time) {
        splits.set(split, time);
    }


    public long getTotalTime() {
        long totalTime = 0;
        for(int i = 0; i < splits.size(); i++) {
            Long splitTime = splits.get(i);
            totalTime += splitTime == null ? 0 : splitTime;
        }
        return totalTime;
    }
}
