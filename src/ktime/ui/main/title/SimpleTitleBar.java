package ktime.ui.main.title;

import javafx.scene.Node;
import ktime.data.RunHistoryListener;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 12/24/13
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SimpleTitleBar {
    public void setTitle(String title);
    public Node getNode();

    public RunHistoryListener getRunHistoryListener();
}
