package ktime.data;

import javafx.beans.InvalidationListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/27/13
 * Time: 9:54 PM
 * To Change this template use File | Settings | File Templates.
 */
public class RunHistory implements ObservableRunHistory{
    RunMetadata metadata;
    List<SplitTimes> historicalTimes;
    SplitTimes bestSplitTimes;
    SplitTimes bestRun;
    transient List<RunHistoryListener> runHistoryListeners;

    public RunHistory(String name, int numSegments) {
        runHistoryListeners = new ArrayList<RunHistoryListener>();
        historicalTimes = new ArrayList<SplitTimes>();
        metadata = new RunMetadata(name, numSegments, runHistoryListeners);
    }

    /**
     * reset down to the number of saved attempts we have.
     */
    public void resetAttempts() {
        metadata.setNumAttempts(historicalTimes.size());
        notifyListeners(new AbstractRunHistoryChange() {
            @Override
            public RunHistoryListener.RunHistoryEventType getChangeType() {
                return RunHistoryListener.RunHistoryEventType.RESET_ATTEMPTS;
            }

            @Override
            public int getNewNumAttempts() {
                return metadata.getNumAttempts();
            }
        });
    }

    public void addUnsavedAttempt() {
        metadata.setNumAttempts(metadata.getNumAttempts()+1);
        notifyListeners(new AbstractRunHistoryChange() {
            @Override
            public RunHistoryListener.RunHistoryEventType getChangeType() {
                return RunHistoryListener.RunHistoryEventType.NEW_ATEMPT;
            }

            @Override
            public int getNewNumAttempts() {
                return metadata.getNumAttempts();
            }

            @Override
            public boolean wasAttemptSaved() {
                return false;
            }
        });
    }

    public void saveAttempt(SplitTimes run) {
        final List<Integer> newBestSegments = new ArrayList<Integer>();
        final boolean wasBestRun;
        metadata.setNumAttempts(metadata.getNumAttempts()+1);
        final SplitTimes runToSave = new SplitTimes(run);
        historicalTimes.add(runToSave);
        if(bestSplitTimes == null)
            bestSplitTimes = new SplitTimes(runToSave);
        for (int i = 0; i < runToSave.getNumSegments(); i++) {
            Long time = bestSplitTimes.getSegmentTime(i);
            Long toSaveTime = runToSave.getSegmentTime(i);
            if (time == null ||
                (toSaveTime != null && toSaveTime < time)) {
                bestSplitTimes.setSegmentTime(i, toSaveTime);
                newBestSegments.add(i);
            }
        }
        if (bestRun == null || runToSave.getTotalTime() < bestRun.getTotalTime()) {
            bestRun = new SplitTimes(runToSave);
            wasBestRun = true;
        } else {
            wasBestRun = false;
        }
        notifyListeners(new AbstractRunHistoryChange() {
            @Override
            public RunHistoryListener.RunHistoryEventType getChangeType() {
                return RunHistoryListener.RunHistoryEventType.NEW_ATEMPT;
            }

            public boolean wasBestRun() {
                return wasBestRun;
            }

            @Override
            public List<Integer> getChangedBestSegments() {
                return newBestSegments;
            }

            @Override
            public SplitTimes getNewBestSplits() {
                return bestSplitTimes;
            }

            @Override
            public SplitTimes getNewAttempt() {
                return runToSave;
            }

            @Override
            public boolean wasAttemptSaved() {
                return true;
            }
        });
    }

    public SplitTimes getBestSplitTimes() {
        return bestSplitTimes != null ? new SplitTimes(bestSplitTimes) : null;
    }

    /**
     * Not safe
     * @return
     */
    public List<SplitTimes> getHistoricalTimes() {
        return new ArrayList<SplitTimes>(historicalTimes);
    }

    public SplitTimes getBestRunTimes() {
        return bestRun != null ? new SplitTimes(bestRun) : null;
    }

    public RunMetadata getRunMetadata() {
        return metadata;
    }

    private void notifyListeners(RunHistoryListener.Change change) {
        for (RunHistoryListener listener : runHistoryListeners) {
            listener.onChanged(change);
        }
    }

    @Override
    public void addListener(RunHistoryListener runHistoryListener) {
        runHistoryListeners.add(runHistoryListener);
        runHistoryListener.onChanged(new RunHistoryListener.Change() {
            @Override
            public RunHistoryListener.RunHistoryEventType getChangeType() {
                return RunHistoryListener.RunHistoryEventType.CURRENT_STATE;
            }

            @Override
            public boolean wasAttemptSaved() {
                return false;
            }

            @Override
            public int getNewNumAttempts() {
                return metadata.getNumAttempts();
            }

            @Override
            public SplitTimes getNewAttempt() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public boolean wasBestRun() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public List<Integer> getChangedBestSegments() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public SplitTimes getNewBestSplits() {
                return bestSplitTimes;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public String getNewRunName() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
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
            public boolean isDisplayBestSegments() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
    }

    @Override
    public void removeListener(RunHistoryListener runHistoryListener) {
        runHistoryListeners.remove(runHistoryListener);
    }

    @Override
    public void addListener(InvalidationListener invalidationListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void removeListener(InvalidationListener invalidationListener) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
