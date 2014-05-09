package ktime.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 11/3/13
 * Time: 11:45 PM
 * To change this template use File | Settings | File Templates.
 */
abstract public class AbstractRunHistoryChange implements RunHistoryListener.Change {

    private final RunMetadata metadata;

    public AbstractRunHistoryChange(RunMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean wasAttemptSaved() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getNewNumAttempts() {
        return metadata.getNumAttempts();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public SplitTimes getNewAttempt() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean wasBestRun() {
        return false;
    }

    @Override
    public List<Integer> getChangedBestSegments() {
        return new ArrayList<Integer>();
    }

    @Override
    public SplitTimes getNewBestSplits(){
        return null;
    }

    @Override
    public String getNewRunName() {
        return metadata.getRunName();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getAlteredSegment() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getNewSegmentName() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getNewSegmentImageUri() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RunHistoryListener.DeltaDisplayMode getDeltaDisplayMode() {
        return metadata.getDeltaDisplayMode();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public RunHistoryListener.SplitDisplayMode getSplitDisplayMode() {
        return metadata.getSplitDisplayMode();  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getChangedSplit() {
        return 0;
    }

    public Long getNewSegmentTime(){
        return null;
    }

    public Long getNewSplitTime() {
        return null;
    }
}
