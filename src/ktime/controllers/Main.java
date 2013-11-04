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
import ktime.data.SplitTimes;
import ktime.ui.DefaultDetailedSplitDisplay;
import ktime.ui.DetailedSplitDisplay;
import ktime.ui.layout.SplitContainer;
import ktime.ui.layout.VSplitContainer;
import ktime.utils.DefaultStopWatch;
import ktime.utils.StopWatch;

import java.io.IOException;

public class Main extends Application {

    StopWatch stopwatch = new DefaultStopWatch();
    RunHistory runHistory;
    SplitTimes currentRun;
    SplitContainer splitContainer;
    DetailedSplitDisplay detailedSplit;
    private Scene scene;


    @Override
    public void start(final Stage primaryStage) throws Exception{
        runHistory = new RunHistory(10);
        setUpUI();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.SPACE) {
                    if (!stopwatch.isRunning()) {
                        stopwatch.start();
                    } else if (stopwatch.getNumSegments() == runHistory.getRunMetadata().getNumSegments()-1) {
                        stopwatch.stop();
                        int currentSegment = stopwatch.getNumSegments()-1;
                        splitContainer.setSegmentEndTime(currentSegment, stopwatch.getSplitTimes().getSegmentEndTime(currentSegment));
                        splitContainer.setActualSegmentTime(currentSegment, stopwatch.getCurrentSegmentTime());
                        runHistory.saveAttempt(stopwatch.getSplitTimes());
                    } else {
                        Long segmentTime = stopwatch.split();
                        int currentSegment = stopwatch.getNumSegments()-1;
                        splitContainer.setSegmentEndTime(currentSegment, stopwatch.getSplitTimes().getSegmentEndTime(currentSegment));
                        splitContainer.setActualSegmentTime(currentSegment, segmentTime);
                    }
                }
                if (keyEvent.getCode() == KeyCode.Q) {
                    primaryStage.close();
                }
                if (keyEvent.getCode() == KeyCode.A) {
                    stopwatch.reset();
                    splitContainer.reset();
                    splitContainer.setRunHistory(runHistory);
                }
            }
        });
        new AnimationTimer() {
            @Override
            public void handle(long l) {
                detailedSplit.setCurrentTime(stopwatch.getTotalTime());
            }
        }.start();
        primaryStage.setScene(scene);
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
        splitContainer = new VSplitContainer();
        root.getChildren().add(splitContainer.getNode());
        splitContainer.setRunHistory(runHistory);
        detailedSplit = new DefaultDetailedSplitDisplay();
        root.getChildren().add(detailedSplit.getNode());
        scene = new Scene(root);
        scene.getStylesheets().add("ktime/controllers/test.css");

    }


    public static void main(String[] args) {
        launch(args);
    }

    public void handle(long l) {
        detailedSplit.setCurrentTime(stopwatch.getTotalTime());
    }
}
