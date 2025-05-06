package com.example.codesmelldetector;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

public class UIComponents {
    private static Label messageLabel;
    private static Text result = new Text();
    private static TextField addressField;
    private static boolean dupResult;

    private static VBox LeftVBox() {
        messageLabel = new Label();
        Label messageLabel1 = new Label();
        messageLabel1.setText("Welcome to code-smell detector!\n");
        messageLabel1.setAlignment(Pos.CENTER);
        messageLabel1.getStyleClass().add("welcome-label");
        messageLabel.setText("\nWe check your code for the code smells below:\n\n  1. Long Methods\n  2. Long Parameters List\n  3. Code Duplicate\n\nPlease upload your Java file:");

        HBox hBox = hBoxLeft();
        VBox vbox = new VBox(20, messageLabel1, messageLabel, hBox);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPadding(new Insets(0, 0, 0, 20));

        return vbox;
    }

    private static HBox hBoxLeft(){
        Button detectButton = new Button("Detect");
        detectButton.getStyleClass().add("detect-button");
        Button browseButton = new Button("Browse File");
        browseButton.getStyleClass().add("browse-button");

        detectButton.setOnAction(new EventHandlers.DetectButtonClickHandler());
        browseButton.setOnAction(new EventHandlers.BrowseButtonClickHandler());

        addressField = new TextField();
        addressField.setPrefWidth(350);

        HBox hBox = new HBox(10, addressField, browseButton, detectButton);
        return hBox;
    }

    private static VBox RightVBox(String imagePath) {
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(400);
        imageView.setPreserveRatio(true);
        VBox vbox = new VBox(imageView);
        vbox.setAlignment(Pos.CENTER_LEFT);

        return vbox;
    }

    public static GridPane createGridPane() {
        GridPane gridpane = new GridPane();
        gridpane.add(LeftVBox(), 0, 0);
        gridpane.add(RightVBox("file:/Users/yalda/IdeaProjects/CodeSmellDetector/src/main/java/com/example/codesmelldetector/detective.jpg"), 1, 0);
        gridpane.setHgap(10);
        gridpane.setVgap(10);

        setConstraintsGridPane(gridpane);

        return gridpane;
    }

    public static GridPane secondGridPane() {
        GridPane gridpane = new GridPane();
        messageLabel.setText("<<< Result >>\n");

        HBox buttonBox = hBoxSecondGrid();

        VBox vbox = new VBox(35, messageLabel, result, buttonBox);
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPadding(new Insets(0, 0, 0, 20));

        VBox vbox2 = RightVBox("file:/Users/yalda/IdeaProjects/CodeSmellDetector/src/main/java/com/example/codesmelldetector/detective2.jpg");

        gridpane.add(vbox, 0,0);
        gridpane.add(vbox2, 1, 0);
        gridpane.setHgap(10);
        gridpane.setVgap(10);

        setConstraintsGridPane(gridpane);

        return gridpane;
    }

    private static HBox hBoxSecondGrid(){
        Button backButton = new Button("Back");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(new EventHandlers.BackToFirstSceneHandler());

        Button refactorButton = new Button("Refactor Duplication");
        refactorButton.getStyleClass().add("refactor-button");
        refactorButton.setOnAction(new EventHandlers.RefactorDuplicatedCodeHandler());

        result.getStyleClass().add("result-text");
        result.setWrappingWidth(500);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(backButton);

        // Check if the specific result or condition is met
        if (dupResult) buttonBox.getChildren().add(refactorButton);
        return buttonBox;
    }
    public static GridPane thirdGridPane() {
        GridPane gridpane = new GridPane();
        messageLabel.setText("<<< Refactoring Result >>\n");
        result.getStyleClass().add("result-text");
        result.setWrappingWidth(500);

        HBox buttonBox = hBoxThirdGrid();

        VBox vbox = new VBox(35, messageLabel, result, buttonBox);
        vbox.setAlignment(Pos.CENTER_LEFT);

        vbox.setPadding(new Insets(0, 0, 0, 20));

        VBox vbox2 = RightVBox("file:/Users/yalda/IdeaProjects/CodeSmellDetector/src/main/java/com/example/codesmelldetector/detective3.jpg");

        gridpane.add(vbox, 0,0);
        gridpane.add(vbox2, 1, 0);
        gridpane.setHgap(10);
        gridpane.setVgap(10);

        setConstraintsGridPane(gridpane);

        return gridpane;
    }
    private static HBox hBoxThirdGrid(){

        Button backButton = new Button("Back");
        backButton.getStyleClass().add("back-button");
        backButton.setOnAction(new EventHandlers.BackToFirstSceneHandler());
        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().addAll(backButton);
        return buttonBox;
    }
    private static void setConstraintsGridPane(GridPane gridpane){
        // Set constraints to make GridPane take up the entire space
        ColumnConstraints columnConstraints0 = new ColumnConstraints();
        columnConstraints0.setPercentWidth(70);
        ColumnConstraints columnConstraints1 = new ColumnConstraints();
        columnConstraints1.setPercentWidth(30);
        gridpane.getColumnConstraints().addAll(columnConstraints0, columnConstraints1);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(100);
        gridpane.getRowConstraints().add(rowConstraints);
    }
    public static void setResult(String rlt){ result.setText(rlt); }

    public static void setDupResult(boolean r){
        dupResult = r;
    }

    public static void updateAddressField(String text) {
        addressField.setText(text);
    }

    public static void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        alert.getDialogPane().getStylesheets().add(UIComponents.class.getResource("Styles.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("error-alert");
        alert.showAndWait();
    }
}
