package ktime.ui;

import javafx.scene.Node;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/25/13
 * Time: 9:51 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SplitDisplay {
    Node getNode();

    void setImageUrl(String imageUrl);

    void removeImage();

    void setName(String name);

    void setTime(Long time);

    void setActualTime(Long time);

    void activate();
    void deactivate();
}
