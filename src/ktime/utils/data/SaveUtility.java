package ktime.utils.data;

import ktime.data.RunHistory;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 2/15/14
 * Time: 10:19 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SaveUtility {
    public void saveRunHistory(String fileLocation, RunHistory runHistory) throws IOException;
    public RunHistory loadRunHistory(String fileLocation) throws IOException;
}
