module com.example.codesmelldetector {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.github.javaparser.core;

    opens com.example.codesmelldetector to javafx.fxml;
    exports com.example.codesmelldetector;
}
