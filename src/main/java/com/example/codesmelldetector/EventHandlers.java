package com.example.codesmelldetector;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EventHandlers {

    public static class DetectButtonClickHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {

            String selectedFilePath = BrowseButtonClickHandler.getSelectedFilePath();
            if (selectedFilePath != null) {
                try {
                    FileHandler.readFile(selectedFilePath);
                    List<String> fileContent = FileHandler.getFile();
                    System.out.println(fileContent);
                } catch (IOException e) { e.printStackTrace();}
                EventHandlers.ChangeSceneHandler changeSceneHandler = new EventHandlers.ChangeSceneHandler();
                changeSceneHandler.handle(event);
            }

            if (selectedFilePath == null || selectedFilePath.trim().isEmpty()) {
                UIComponents.showErrorAlert("Error", "Please choose a file before clicking Detect.");}
        }
    }

    public static class BrowseButtonClickHandler implements EventHandler<ActionEvent> {
        private static String selectedFilePath;

        @Override
        public void handle(ActionEvent event){

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Java File");

            // Set extension filter to only allow Java files
            // FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Java Files (*.java)", "*.java");
            // fileChooser.getExtensionFilters().add(extFilter);

            File selectedFile = fileChooser.showOpenDialog(null);

            if (selectedFile != null){
                selectedFilePath = selectedFile.getPath();
                UIComponents.updateAddressField(selectedFilePath);
            }
        }
        public static String getSelectedFilePath() {
            return selectedFilePath;
        }
    }

    private static class ChangeSceneHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Node source = (Node) event.getSource();
            Stage primaryStage = (Stage) source.getScene().getWindow();

            Scene scene = new Scene(UIComponents.secondGridPane(), 800, 400);
            scene.getStylesheets().add(this.getClass().getResource("Styles.css").toExternalForm());
            primaryStage.setScene(scene);
        }
    }
    public static class BackToFirstSceneHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            Node source = (Node) event.getSource();
            Stage primaryStage = (Stage) source.getScene().getWindow();
            // Reset selectedFilePath to null
            BrowseButtonClickHandler.selectedFilePath = null;

            Scene scene = new Scene(UIComponents.createGridPane(), 800, 400);
            scene.getStylesheets().add(this.getClass().getResource("Styles.css").toExternalForm());
            primaryStage.setScene(scene);
        }
    }

    public static class RefactorDuplicatedCodeHandler implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {

            FileHandler.writeFile(BrowseButtonClickHandler.getSelectedFilePath());
            Node source = (Node) event.getSource();
            Stage primaryStage = (Stage) source.getScene().getWindow();

            Scene scene = new Scene(UIComponents.thirdGridPane(), 800, 400);
            scene.getStylesheets().add(this.getClass().getResource("Styles.css").toExternalForm());
            primaryStage.setScene(scene);
            setRefactoredResult(FileHandler.getRefactoredMethods());
        }
        private static void setRefactoredResult(ArrayList<String> refactoredMethods){

            StringBuilder result = new StringBuilder("Your code is refactored by removing duplicated lines of code in method(s): \n - ");

            if (!refactoredMethods.isEmpty()) {
                for (int i = 0; i < refactoredMethods.size(); i++) {
                    result.append(refactoredMethods.get(i));
                    if (i < refactoredMethods.size() - 1) {
                        result.append("\n - ");
                    }
                }
            }
            String finalResult = result.toString();
            finalResult += "\n and saved as a new file in the same folder as your original code.";
            UIComponents.setResult(finalResult);
        }
    }
}
