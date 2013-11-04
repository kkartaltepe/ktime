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
 * To change this template use File | Settings | File Templates.
 */
public class DefaultSplitDisplay implements SplitDisplay {
    @FXML ImageView image;
    @FXML Text name;
    @FXML Text displayTime;
    private Long lastTime, lastSegmentEnd, actualTime, actualSegmentEnd;
    private Node node;

    public DefaultSplitDisplay() {
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
    public void setImageUrl(String imageUrl) {
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
    public void setLastTime(Long lastTime) {
        this.lastTime = lastTime;
    }

    @Override
    public void setLastSegmentEnd(Long segmentEnd) {
        this.lastSegmentEnd = segmentEnd;
    }

    @Override
    public void setActualTime(Long time) {
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
         computeDisplayTime(lastSegmentEnd, actualSegmentEnd);
    }

    @Override
    public void displayLastSegmentDelta() {
        computeDisplayTime(lastTime, actualTime);
    }

    private void computeDisplayTime(Long lastTime, Long currentTime) {
        if(currentTime != null &&  lastTime != null)
            this.displayTime.setText(TimeFormatter.formatSigned(lastTime - currentTime));
        else if (currentTime != null && lastTime == null)
            this.displayTime.setText(TimeFormatter.formatSigned(0 - currentTime));
        else
            this.displayTime.setText(TimeFormatter.formatSigned(null)); //Skipped unable to compute
    }

}
