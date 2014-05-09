package ktime.ui.main.layout.individual;

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
    public void setImageUri(String imageUri) {
        if(imageUri != null)
            image.setImage(new Image(imageUri));
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
    public void activate() {
        node.getStyleClass().add("activeSplit");
    }

    @Override
    public void deactivate() {
        node.getStyleClass().remove("activeSplit");
    }

    @Override
    public void setDisplayTime(Long time, String style) {
        displayTime.setText(TimeFormatter.format(time));
        displayTime.getStyleClass().retainAll("text", "splitDisplayTime");
        if(style != null)
            displayTime.getStyleClass().add(style);
    }

}
