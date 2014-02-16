package ktime.utils.data;

import com.google.gson.Gson;
import ktime.data.RunHistory;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 2/15/14
 * Time: 9:46 PM
 * To change this template use File | Settings | File Templates.
 */
public class DefaultSaveUtility implements SaveUtility{
    Gson gson = new Gson();

    public void saveRunHistory(String fileLocation, RunHistory runHistory) throws IOException {
        File fileToSave = new File(fileLocation);
        if(fileToSave.getParentFile() != null && !fileToSave.getParentFile().exists()) {
            fileToSave.getParentFile().mkdirs();
        }
        String serialization = gson.toJson(runHistory);
        FileWriter writer = new FileWriter(fileToSave);
        writer.write(serialization);
        writer.flush();
    }

    public RunHistory loadRunHistory(String fileLocation) throws IOException {
        File fileToLoad = new File(fileLocation);
        if(fileToLoad.exists() && !fileToLoad.isDirectory()) {
            BufferedReader reader = new BufferedReader(new FileReader(fileToLoad));
            return gson.fromJson(reader, RunHistory.class);
        }else {
            return new RunHistory("Testing name", 10);
        }
    }

}
