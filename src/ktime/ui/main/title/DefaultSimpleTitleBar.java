package ktime.ui.main.title;

import javafx.scene.Node;
import javafx.scene.control.Label;
import ktime.data.RunHistory;
import ktime.data.RunHistoryListener;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 12/24/13
 * Time: 3:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultSimpleTitleBar implements SimpleTitleBar {

    Label title;

    public DefaultSimpleTitleBar(RunHistory runHistory) {
        title = new Label(runHistory.getRunMetadata().getRunName());
    }

    @Override
    public void setTitle(String title) {
        this.title.setText(title);
    }

    @Override
    public Node getNode() {
        return title;
    }

    @Override
    public RunHistoryListener getRunHistoryListener() {
        return new RunHistoryListener() {
            @Override
            public void onChanged(Change change) {
                if(change.getChangeType() == RunHistoryEventType.RUN_NAME_CHANGE) {
                    title.setText(change.getNewRunName());
                }
            }
        };
    }
}
