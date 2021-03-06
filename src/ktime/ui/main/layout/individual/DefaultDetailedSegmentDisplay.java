package ktime.ui.main.layout.individual;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.text.Text;
import ktime.data.RunHistory;
import ktime.data.RunHistoryListener;
import ktime.utils.TimeFormatter;
import ktime.utils.stopwatch.StopwatchListener;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/28/13
 * Time: 6:59 PM
 * To Change this template use File | Settings | File Templates.
 */
public class DefaultDetailedSegmentDisplay implements DetailedSegmentDisplay {
    Node node;
    int currentSplit;
    @FXML private Text currentTime;
    @FXML private Text currentSegmentTime;
    @FXML private Text bestSegmentTime;
    @FXML private Text previousSplitDelta;
    private RunHistory history;


    public DefaultDetailedSegmentDisplay() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("defaultDetailedSplitDisplay.fxml"));
        loader.setController(this);
        try {
            node = (Node) loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ui fxml");
        }
    }

    @Override
    public void setRunHistory(RunHistory history) {
        this.history = history;

    }

    @Override
    public void setCurrentTime(long currentTime) {
        this.currentTime.setText(TimeFormatter.format(currentTime));
    }

    @Override
    public void setCurrentSegmentTime(long currentSegmentTime) {
        this.currentSegmentTime.setText(TimeFormatter.format(currentSegmentTime));
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public RunHistoryListener getRunHistoryListener() {
        return new RunHistoryListener() {
            @Override
            public void onChanged(Change change) {
                switch (change.getChangeType()) {
                    case RESET:
                    case START:
                        previousSplitDelta.setText(TimeFormatter.CANNOT_COMPUTE_DELTA);
                        bestSegmentTime.setText(TimeFormatter.format(history.getBestRunTimes().getSegmentEndTime(0)));
                        break;
                    case SPLIT:
                        bestSegmentTime.setText(TimeFormatter.format(history.getBestRunTimes().getSegmentTime(change.getChangedSplit())));
                    case STOP:
                        Long bestSegmentTime = history.getBestRunTimes().getSegmentTime(change.getChangedSplit() - 1);
                        String formattedDelta = TimeFormatter.formatDelta(bestSegmentTime, change.getNewSegmentTime());
                        previousSplitDelta.setText(formattedDelta);
                        break;
                    case UNSPLIT:
                        //TODO: unsplit stuff (definte unsplit event first...)
                        break;
                    case SKIPSPLIT:
                        previousSplitDelta.setText(TimeFormatter.CANNOT_COMPUTE_DELTA);
                        DefaultDetailedSegmentDisplay.this.bestSegmentTime.setText(TimeFormatter.format(history.getBestRunTimes().getSegmentTime(change.getChangedSplit() - 1)));
                        break;
                }
            }
        };
    }

    public StopwatchListener getStopwatchListener() {
        return new StopwatchListener() {
            @Override
            public void onChanged(Change change) {
                switch (change.getChangeType()) {
                    case START:
                    case RESET:
                        bestSegmentTime.setText(TimeFormatter.format(history.getBestRunTimes().getSegmentTime(0)));
                        break;
                    case STOP:
                    case SPLIT:
                    case UNSPLIT:
                    case SKIPSPLIT:
                        bestSegmentTime.setText(TimeFormatter.format(history.getBestRunTimes().getSegmentTime(change.getChangedSplit()-1)));
                        break;
                }

            }
        };
    }
}
