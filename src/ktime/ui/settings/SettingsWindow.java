package ktime.ui.settings;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: kurt
 * Date: 1/4/14
 * Time: 8:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class SettingsWindow {

    private Scene scene;
    private Parent root;

    public SettingsWindow() {
        try {
            root = FXMLLoader.load(getClass().getResource("settingsWindow.fxml"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load settings fxml");
        }
        scene = new Scene(root, 100,100);
        scene.getStylesheets().add("ktime/controllers/test.css");
    }

    public Scene getScene() {
        return scene;
    }
}
