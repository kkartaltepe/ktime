package ktime.data;

import javafx.beans.Observable;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 11/3/13
 * Time: 11:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ObservableRunHistory extends Observable {
    void addListener(RunHistoryListener runHistoryListener);
    void removeListener(RunHistoryListener runHistoryListener);
}
