package ktime.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.text.Text;
import ktime.data.RunHistory;
import ktime.utils.TimeFormatter;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/28/13
 * Time: 6:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultDetailedSplitDisplay implements DetailedSplitDisplay {
    Node node;
    RunHistory history;
    @FXML private Text currentTime;
    @FXML private Text currentSplitTime;
    @FXML private Text bestSplitTime;
    @FXML private Text previousSplitDelta;


    public DefaultDetailedSplitDisplay() {
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
    public void setDisplayedSplit(int split) {
        currentSplitTime.setText(TimeFormatter.format(history.getBestRunTimes().getTime(split)));
        bestSplitTime.setText(TimeFormatter.format(history.getBestSplitTimes().getTime(split)));
        previousSplitDelta.setText("-");
    }

    @Override
    public Node getNode() {
        return node;
    }
}
