package ktime.ui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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
    private Long time, actualTime;
    private Node node;

    public DefaultSplitDisplay() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("defaultSplitDisplay.fxml"));
        loader.setController(this);
        try {
            node = (Node) loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load ui fxml");
        }
        node.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (node.getStyleClass().contains("activeSplit"))
                    deactivate();
                else
                    activate();

            }
        });
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
    public void setTime(Long time) {
        this.time = time;
        this.displayTime.setText(TimeFormatter.format(time));
    }

    @Override
    public void setActualTime(Long time) {
        this.actualTime = time;
        if(actualTime != null &&  this.time != null)
            this.displayTime.setText(TimeFormatter.formatSigned(this.time - this.actualTime));
        else if (actualTime != null && this.time == null)
            this.displayTime.setText(TimeFormatter.formatSigned(0 - this.actualTime));
        else
            this.displayTime.setText(TimeFormatter.formatSigned(null)); //Skipped unable to compute
    }

    @Override
    public void activate() {
        node.getStyleClass().add("activeSplit");
        System.out.println("Activated split");
    }

    @Override
    public void deactivate() {
        node.getStyleClass().remove("activeSplit");
        System.out.println("Deactivated split");
    }

}
