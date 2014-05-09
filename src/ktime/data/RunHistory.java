package ktime.data;

import javafx.beans.InvalidationListener;
import ktime.utils.stopwatch.StopwatchListener;

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


    public RunHistory() {
        runHistoryListeners = new ArrayList<RunHistoryListener>();
        historicalTimes = new ArrayList<SplitTimes>();
        metadata = new RunMetadata(null, 0, runHistoryListeners);
    }

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
        notifyListeners(new AbstractRunHistoryChange(metadata) {
            @Override
            public RunHistoryListener.RunHistoryEventType getChangeType() {
                return RunHistoryListener.RunHistoryEventType.RESET_ATTEMPTS;
            }
        });
    }

    public void addUnsavedAttempt() {
        metadata.setNumAttempts(metadata.getNumAttempts()+1);
        notifyListeners(new AbstractRunHistoryChange(metadata) {
            @Override
            public RunHistoryListener.RunHistoryEventType getChangeType() {
                return RunHistoryListener.RunHistoryEventType.NEW_ATEMPT;
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
        notifyListeners(new AbstractRunHistoryChange(metadata) {
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
                return getBestSplitTimes();
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

    public SplitTimes getSplitsToDisplay() {
        switch (metadata.getSplitDisplayMode()) {
            case BEST_SEGMENTS:
                return getBestSplitTimes();
            case BEST_RUN:
            default:
                return getBestRunTimes();
        }
    }

    private void notifyListeners(RunHistoryListener.Change change) {
        for (RunHistoryListener listener : runHistoryListeners) {
            listener.onChanged(change);
        }
    }

    @Override
    public void addListener(RunHistoryListener runHistoryListener) {
        runHistoryListeners.add(runHistoryListener);
        runHistoryListener.onChanged(new AbstractRunHistoryChange(metadata) {
            @Override
            public RunHistoryListener.RunHistoryEventType getChangeType() {
                return RunHistoryListener.RunHistoryEventType.CURRENT_STATE;
            }

            @Override
            public SplitTimes getNewBestSplits() {
                return bestSplitTimes;  //To change body of implemented methods use File | Settings | File Templates.
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

    public StopwatchListener getStopwatchListener() {
        return new StopwatchListener() {
            @Override
            public void onChanged(Change change) {
                switch (change.getChangeType()) {
                    case START:

                        break;
                    case STOP:
                        break;
                    case RESET:
                        break;
                    case SPLIT:
                        break;
                    case UNSPLIT:
                        break;
                    case SKIPSPLIT:
                        break;
                }
            }
        };
    }
}
