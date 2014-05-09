package ktime.controllers;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ktime.data.RunHistory;
import ktime.ui.main.layout.SplitContainer;
import ktime.ui.main.layout.VSplitContainer;
import ktime.ui.main.layout.individual.DefaultDetailedSegmentDisplay;
import ktime.ui.main.layout.individual.DetailedSegmentDisplay;
import ktime.ui.main.title.DefaultSimpleTitleBar;
import ktime.ui.main.title.SimpleTitleBar;
import ktime.ui.settings.SettingsWindow;
import ktime.utils.data.DefaultSaveUtility;
import ktime.utils.data.SaveUtility;
import ktime.utils.stopwatch.DefaultStopwatch;
import ktime.utils.stopwatch.ObservableStopwatch;

import java.io.IOException;

public class Main extends Application {

    ObservableStopwatch stopwatch = new DefaultStopwatch();
    SaveUtility saveUtility;
    RunHistory runHistory;
    SimpleTitleBar titleBar;
    SplitContainer splitContainer;
    DetailedSegmentDisplay detailedSplit;
    private Scene scene;
    private SettingsWindow settingsWindow;


    @Override
    public void start(final Stage primaryStage) throws Exception{
        saveUtility = new DefaultSaveUtility();
        runHistory = saveUtility.loadRunHistory("test.krh");
        setUpUI();
        detailedSplit.setRunHistory(runHistory);
        stopwatch.addListener(runHistory.getStopwatchListener());
        stopwatch.addListener(splitContainer.getStopwatchListener());
        runHistory.addListener(splitContainer.getRunHistoryListener());
        runHistory.addListener(titleBar.getRunHistoryListener());
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.SPACE) {
                    if (!stopwatch.isRunning()) {
                        stopwatch.start();
                    } else if (stopwatch.getNumSegments() == runHistory.getRunMetadata().getNumSegments() - 1) {
                        stopwatch.stop();
                        runHistory.saveAttempt(stopwatch.getSplitTimes());
                    } else {
                        stopwatch.split();
                    }
                }
                if (keyEvent.getCode() == KeyCode.U) {
                    if(stopwatch.isRunning() && stopwatch.getNumSegments() > 0) {
                        stopwatch.unsplit();
                    }
                }
                if (keyEvent.getCode() == KeyCode.Q) {
                    try {
                        saveUtility.saveRunHistory("test.krh", runHistory);
                    } catch (IOException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    primaryStage.close();
                }
                if (keyEvent.getCode() == KeyCode.S) {
                    Stage settingsStage = new Stage();
                    settingsStage.setScene(new Scene(settingsWindow.getNode()));
                    settingsStage.show();
                }
                if (keyEvent.getCode() == KeyCode.A) {
                    stopwatch.reset();
                }
            }
        });
        new AnimationTimer() {
            @Override
            public void handle(long l) {
                detailedSplit.setCurrentTime(stopwatch.getTotalTime());
                detailedSplit.setCurrentSegmentTime(stopwatch.getCurrentSegmentTime());
            }
        }.start();
        primaryStage.setScene(scene);
//        primaryStage.sizeToScene();
//        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        primaryStage.setMinHeight(primaryStage.getHeight()+24);
        primaryStage.setMinWidth(primaryStage.getWidth()+8);

    }

    private void setUpUI() {
        VBox root;
        try {
            root = FXMLLoader.load(getClass().getResource("main.fxml"));
        } catch (IOException e) {
            throw new RuntimeException("main.fxml not found");
        }
        root.setFillWidth(true);
        titleBar = new DefaultSimpleTitleBar(runHistory);
        root.getChildren().add(titleBar.getNode());
        splitContainer = new VSplitContainer();
        root.getChildren().add(splitContainer.getNode());
        splitContainer.setRunHistory(runHistory);
        detailedSplit = new DefaultDetailedSegmentDisplay();
        root.getChildren().add(detailedSplit.getNode());
        scene = new Scene(root);
        scene.getStylesheets().add("ktime/controllers/test.css");
        settingsWindow = new SettingsWindow();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
