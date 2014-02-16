package ktime.utils.data;

import ktime.data.RunHistory;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 2/15/14
 * Time: 10:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class WSplitSaveUtility implements SaveUtility {
    @Override
    public void saveRunHistory(String fileLocation, RunHistory runHistory) throws IOException {
        throw new NotImplementedException();

    }

    @Override
    public RunHistory loadRunHistory(String fileLocation) throws IOException {
        throw new NotImplementedException();
    }
}
