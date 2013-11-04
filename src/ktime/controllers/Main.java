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
import ktime.ui.DefaultDetailedSplitDisplay;
import ktime.ui.DetailedSplitDisplay;
import ktime.ui.layout.SplitContainer;
import ktime.ui.layout.VSplitContainer;
import ktime.utils.stopwatch.DefaultStopwatch;
import ktime.utils.stopwatch.ObservableStopwatch;

import java.io.IOException;

public class Main extends Application {

    ObservableStopwatch stopwatch = new DefaultStopwatch();
    RunHistory runHistory;
    SplitContainer splitContainer;
    DetailedSplitDisplay detailedSplit;
    private Scene scene;


    @Override
    public void start(final Stage primaryStage) throws Exception{
        runHistory = new RunHistory("Testing name", 10);
        setUpUI();
        stopwatch.addListener(splitContainer.getStopwatchListener());
        runHistory.addListener(splitContainer.getRunHistoryListener());
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
                if (keyEvent.getCode() == KeyCode.Q) {
                    primaryStage.close();
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
