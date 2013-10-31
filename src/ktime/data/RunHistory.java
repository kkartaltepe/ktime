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
    private int attempts;

    public RunHistory(int numSplits) {
        bestSplitTimes = new SplitTimes(numSplits);
        bestRun = new SplitTimes(numSplits);
        historicalTimes = new ArrayList<SplitTimes>();
        attempts = 0;
        metadata = new RunMetadata(numSplits);
    }

    /**
     * reset down to the number of saved attempts we have.
     */
    public void resetAttempts() {
        attempts = historicalTimes.size();
    }

    public void addUnsavedAttempt() {
        attempts++;
    }

    public void saveAttempt(SplitTimes run) {
        attempts++;
        SplitTimes runToSave = new SplitTimes(run);
        historicalTimes.add(runToSave);
        for (int i = 0; i < runToSave.getNumSplits(); i++) {
            Long time = bestSplitTimes.getTime(i);
            Long toSaveTime = runToSave.getTime(i);
            if (time == null ||
                (toSaveTime != null && toSaveTime < time)) {
                bestSplitTimes.setTime(i, toSaveTime);
            }
        }
        if(runToSave.getTotalTime() < bestRun.getTotalTime()) {
            bestRun = runToSave;
        }
    }

    public int getNumAttempts() {
        return attempts;
    }

    public SplitTimes getBestSplitTimes() {
        return new SplitTimes(bestSplitTimes);
    }

    public List<SplitTimes> getHistoricalTimes() {
        return new ArrayList<SplitTimes>(historicalTimes);
    }

    public SplitTimes getBestRunTimes() {
        return new SplitTimes(bestRun);
    }

    public String getSplitName(int splitNum) {
        return metadata.getSplitName(splitNum);
    }

    public String getSplitUri(int splitNum) {
        return metadata.getSplitImageUri(splitNum);
    }

    public boolean displayBestSplits() {
        return metadata.displayBestSplits();
    }
}
