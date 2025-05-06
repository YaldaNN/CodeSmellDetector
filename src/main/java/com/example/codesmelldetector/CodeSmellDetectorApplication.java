package com.example.codesmelldetector;

import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class CodeSmellDetectorApplication extends Application {

    @Override
    public void start(Stage primaryStage){

        GridPane gridPane = UIComponents.createGridPane();

        Scene scene = new Scene(gridPane, 800, 400);

        scene.getStylesheets().add(this.getClass().getResource("Styles.css").toExternalForm());

        primaryStage.setScene(scene);

        primaryStage.setTitle("Code Smell Detector");

        primaryStage.show();
    }

    public static void main(String[] args) {

        launch();
    }
}