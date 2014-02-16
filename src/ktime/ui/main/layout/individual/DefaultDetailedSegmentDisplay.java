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
    RunHistory history;
    int currentSplit;
    @FXML private Text currentTime;
    @FXML private Text currentSegmentTime;
    @FXML private Text bestSegmentTime;
    @FXML private Text previousSplitDelta;


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
    public StopwatchListener getStopwatchListener() {
        return new StopwatchListener() {
            @Override
            public void onChanged(Change change) {
                if (history.getBestRunTimes() == null)
                    return; //If there is no previous run to compare against we cant do much
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

    @Override
    public RunHistoryListener getRunHistoryListener() {
        //Should this remain?
        return new RunHistoryListener() {
            @Override
            public void onChanged(Change change) {
                switch (change.getChangeType()) {
                    case NEW_ATEMPT:
                    case DISPLAY_MODE_CHANGE:
                    case RESET_ATTEMPTS:
                    case RUN_NAME_CHANGE:
                    case SEGMENT_NAME_CHANGE:
                    case SEGMENT_IMAGE_CHANGE:
                        break;

                }
            }
        };
    }
}
