package ktime;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
        currentRun = new SplitTimes(10);
        runHistory.saveAttempt(currentRun);
        currentRun = runHistory.getBestSplitTimes();
        setUpUI();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.SPACE) {
                    if (!stopwatch.isRunning()) {
                        stopwatch.start();
                    } else if (stopwatch.getNumSplits() == currentRun.getNumSplits()) {
                        splitContainer.setActualTime(stopwatch.getNumSplits() - 2, stopwatch.getCurrentSplitTime());
                    } else {
                        splitContainer.setActualTime(stopwatch.getNumSplits() - 1, stopwatch.split());
                    }
                }
                if (keyEvent.getCode() == KeyCode.Q) {
                    primaryStage.close();
                }
                if (keyEvent.getCode() == KeyCode.T) {
                }
            }
        });
        scene.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                detailedSplit.setCurrentTime(stopwatch.getTotalTime());
            }
        });
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
        splitContainer.addRunHistory(runHistory);
        detailedSplit = new DefaultDetailedSplitDisplay();
        root.getChildren().add(detailedSplit.getNode());
        scene = new Scene(root);
        scene.getStylesheets().add("ktime/test.css");

    }


    public static void main(String[] args) {
        launch(args);
    }
}
