package com.company.filehandlingapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final String MAIN_APPLICATION_TITLE = "Item's Management App";
    private static final double MIN_WIDTH = 1250;
    private static final double MIN_HEIGHT = MIN_WIDTH * 0.55;
    private static final double MAX_WIDTH = MIN_WIDTH + Math.round(MIN_WIDTH * 0.4);
    private static final double MAX_HEIGHT = MIN_HEIGHT + Math.round(MAX_WIDTH * 0.4);

    @Override
    public void start(Stage primaryStage) {
        URL mainGUIResource = Main.class.getResource("main-view.fxml");
        if (mainGUIResource != null) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(mainGUIResource);
                Scene scene = new Scene(fxmlLoader.load(), MIN_WIDTH, MIN_HEIGHT);
                primaryStage.setScene(scene);
                primaryStage.setMaxWidth(MAX_WIDTH);
                primaryStage.setMaxHeight(MAX_HEIGHT);
                primaryStage.setMinWidth(MIN_WIDTH);
                primaryStage.setMinHeight(MIN_HEIGHT);
                primaryStage.setTitle(MAIN_APPLICATION_TITLE);
                primaryStage.show();
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        } else {
            LOGGER.info("Resource is null");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}

