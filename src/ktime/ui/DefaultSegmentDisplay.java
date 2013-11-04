package ktime.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import ktime.utils.TimeFormatter;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/25/13
 * Time: 11:44 AM
 * To Change this template use File | Settings | File Templates.
 */
public class DefaultSegmentDisplay implements SegmentDisplay {
    @FXML ImageView image;
    @FXML Text name;
    @FXML Text displayTime;
    private Long lastTime, lastSegmentEnd, actualTime, actualSegmentEnd;
    private Node node;

    public DefaultSegmentDisplay() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("defaultSplitDisplay.fxml"));
        loader.setController(this);
        try {
            node = (Node) loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ui fxml");
        }
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public void setImageUri(String imageUrl) {
        image.setImage(new Image(imageUrl));
    }

    @Override
    public void removeImage() {
        image.setImage(null);
    }

    @Override
    public void setName(String name) {
        this.name.setText(name);
    }

    @Override
    public void setLastSegmentTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public void setLastSegmentEnd(Long segmentEnd) {
        this.lastSegmentEnd = segmentEnd;
    }

    @Override
    public void setActualSegmentTime(Long time) {
        this.actualTime = time;
    }

    @Override
    public void activate() {
        node.getStyleClass().add("activeSplit");
    }

    @Override
    public void deactivate() {
        node.getStyleClass().remove("activeSplit");
    }

    @Override
    public void setActualSegmentEnd(Long time) {
        this.actualSegmentEnd = time;
    }

    @Override
    public void displayLastRunDelta() {
        Long delta = computeDelta(lastSegmentEnd, actualSegmentEnd);
        displayTime.setText(TimeFormatter.formatSigned(delta));
        computeSplitStyleClass();
    }

    @Override
    public void displayLastSegmentDelta() {
        Long delta = computeDelta(lastTime, actualTime);
        displayTime.setText(TimeFormatter.formatSigned(delta));
        computeSplitStyleClass();
    }

    private Long computeDelta(Long lastTime, Long currentTime) {
        Long delta;
        if(currentTime != null &&  lastTime != null)
            delta = currentTime - lastTime;
        else if (currentTime != null && lastTime == null)
            delta = 0 - currentTime;
        else
            delta = null; //Skipped unable to compute
        return delta;
    }

    private void computeSplitStyleClass() {
        Long segmentDelta = computeDelta(lastSegmentEnd, actualSegmentEnd);
        Long runDelta = computeDelta(lastTime, actualTime);
        displayTime.getStyleClass().retainAll("text", "splitDisplayTime");
        if(segmentDelta == null || runDelta == null)
            return; //No styling.
        if(segmentDelta > 0 && runDelta > 0)
            displayTime.getStyleClass().add("positiveRunPositiveSeg");
        if(segmentDelta > 0 && runDelta < 0)
            displayTime.getStyleClass().add("positiveRunNegativeSeg");
        if(segmentDelta < 0 && runDelta > 0)
            displayTime.getStyleClass().add("negativeRunPositiveSeg");
        if(segmentDelta < 0 && runDelta < 0)
            displayTime.getStyleClass().add("negativeRunNegativeSeg");

    }

}
