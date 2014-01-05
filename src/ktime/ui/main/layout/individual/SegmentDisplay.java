package ktime.ui.main.layout.individual;

import javafx.scene.Node;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 10/25/13
 * Time: 9:51 PM
 * To Change this template use File | Settings | File Templates.
 */
public interface SegmentDisplay {
    Node getNode();

    void setImageUri(String imageUrl);

    void removeImage();

    void setName(String name);

    void setLastSegmentTime(Long time);

    void setActualSegmentTime(Long time);

    void activate();
    void deactivate();

    void setActualSegmentEnd(Long time);

    void setLastSegmentEnd(Long segmentEnd);

    void displayLastRunDelta();
    void displayLastSegmentDelta();
    void displayLastRunTimes();
}
