# Code Smell Detector

**Code Smell Detector** is a Java-based application designed to identify and refactor common code smells such as long methods, long parameter lists, and duplicated code within Java files. It uses **JavaFX** for the graphical interface and **Javaparser** to parse Java code and detect duplicated methods using the Abstract Syntax Tree (AST).

## Features

- **Detect Long Methods**: Identifies methods with more than 15 lines of code.
- **Detect Long Parameter Lists**: Identifies methods that accept more than 3 parameters.
- **Detect Duplicated Code**: Detects methods with high similarity using **Jaccard similarity** and suggests refactoring. This detection relies on parsing the code into an Abstract Syntax Tree (AST).
- **JavaFX GUI**: Allows users to upload a Java file and displays the results interactively.

## Technologies Used

- **Java**: Core programming language.
- **JavaFX**: For building the graphical user interface (GUI).
- **Javaparser**: Parses Java source files and builds an **Abstract Syntax Tree (AST)** for duplicated code detection.
- **Jaccard Similarity**: Used to identify duplicated code by comparing method similarity.
- **CSS**: For styling the application.

## Installation

To use this application, clone the repository and follow the instructions below:

1. **Clone the repository**:
    ```bash
    git clone https://github.com/YaldaNN/CodeSmellDetector.git
    ```

2. **Open the project** in your favorite Java IDE (e.g., IntelliJ IDEA or Eclipse).

3. **Build the project** using your IDE's build tools.

4. **Run the application**. The JavaFX application will open where you can upload Java files for analysis.

## How to Use

1. Launch the **Code Smell Detector** application.
2. Click **Browse File** to select a Java file.
3. Once the file is selected, click **Detect** to check for code smells.
4. The application will display any identified code smells (long methods, long parameter lists, duplicated code).
5. If duplicated code is found, you can click **Refactor Duplication** to refactor the code and remove duplication.

## Files and Classes

- **CodeSmellDetectorApplication.java**: The main class that launches the JavaFX application. It sets up the scene and loads the UI components.
- **DetectorLongMethod.java**: Detects methods with more than 15 lines of code.
- **DetectorLongParameterList.java**: Detects methods with more than 3 parameters.
- **DetectorDuplicatedCode.java**: Detects duplicated code using **Jaccard similarity** and AST parsing.
- **EventHandlers.java**: Handles button clicks and interactions within the UI.
- **FileHandler.java**: Reads and writes Java files, and integrates with the detectors to perform analysis.
- **UIComponents.java**: Defines the UI layout, including the file selection dialog, result display, and styling.
- **NodeSerializer.java**: Serializes nodes of Java methods to compare their structure for detecting duplicated code using AST.
- **Styles.css**: Contains the styling rules for the JavaFX application interface.

## Contribution

Feel free to fork this repository, submit pull requests, or open issues if you have any suggestions or find bugs.

## License

This project is open-source and available under the [MIT License](LICENSE).

![Code Smell Detector Interface](src/main/java/com/example/codesmelldetector/detective.jpg)
