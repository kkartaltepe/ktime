package ktime.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/27/13
 * Time: 9:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class RunHistory {
    SplitTimes bestSplitTimes;
    SplitTimes bestRun;
    List<SplitTimes> historicalTimes;
    RunMetadata metadata;

    public RunHistory(int numSegments) {
        historicalTimes = new ArrayList<SplitTimes>();
        metadata = new RunMetadata(numSegments);
    }

    /**
     * reset down to the number of saved attempts we have.
     */
    public void resetAttempts() {
        metadata.setNumAttempts(historicalTimes.size());
    }

    public void addUnsavedAttempt() {
        metadata.setNumAttempts(metadata.numAttempts()+1);
    }

    public void saveAttempt(SplitTimes run) {
        metadata.setNumAttempts(metadata.numAttempts()+1);
        SplitTimes runToSave = new SplitTimes(run);
        System.out.println("Run to save segments" + runToSave.getNumSegments());
        historicalTimes.add(runToSave);
        if(bestSplitTimes == null)
            bestSplitTimes = new SplitTimes(runToSave);
        for (int i = 0; i < runToSave.getNumSegments(); i++) {
            Long time = bestSplitTimes.getSegmentTime(i);
            Long toSaveTime = runToSave.getSegmentTime(i);
            if (time == null ||
                (toSaveTime != null && toSaveTime < time)) {
                bestSplitTimes.setSegmentTime(i, toSaveTime);
            }
        }
        if(bestRun != null) {
            System.out.println("Best: " + bestRun.getTotalTime() + " compared to: " + runToSave.getTotalTime());
        }
        if (bestRun == null || runToSave.getTotalTime() < bestRun.getTotalTime()) {
            bestRun = new SplitTimes(runToSave);
            System.out.println("Best run!!!! " + bestRun.getTotalTime());
        }
    }

    public SplitTimes getBestSplitTimes() {
        return bestSplitTimes != null ? new SplitTimes(bestSplitTimes) : null;
    }

    public List<SplitTimes> getHistoricalTimes() {
        return new ArrayList<SplitTimes>(historicalTimes);
    }

    public SplitTimes getBestRunTimes() {
        return bestRun != null ? new SplitTimes(bestRun) : null;
    }

    public RunMetadata getRunMetadata() {
        return  metadata;
    }
}
