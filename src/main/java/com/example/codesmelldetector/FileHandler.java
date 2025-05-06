package com.example.codesmelldetector;

import java.io.*;
import java.util.*;

public class FileHandler {
    private static List<String> fileContent;
    private static List<String> refactoredFile;
    private static ArrayList<String> refactoredMethods;
    public static void readFile(String filePath) throws IOException {

        fileContent = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            startAnalyzing();
        } catch (IOException e) {
            UIComponents.showErrorAlert("Error", "An error occurred while reading the file. Please check if the file exists and try again.");
        }
    }
    public static List<String> getFile(){
        return fileContent;
    }

    public static void writeFile(String path){

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.substring(0,path.length()-5) + "_new.java"))) {

            for (int i = 0; i < refactoredFile.size(); i++) {
                String line = refactoredFile.get(i);
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.err.println("An error occurred while writing to the file: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private static void startAnalyzing(){

        DetectorLongMethod dlm = new DetectorLongMethod(fileContent);
        dlm.methodFinder();
        ArrayList<String> locResult = dlm.checkLOC();

        DetectorLongParameterList dlpl = new DetectorLongParameterList();
        ArrayList<String> paramResult = dlpl.checkParams(dlm.getMethodMap());

        DetectorDuplicatedCode ddc = new DetectorDuplicatedCode(dlm.getMethodMap());
        String dupResult = ddc.getSimilarityResult();

        printResult(locResult,paramResult, dupResult);
        UIComponents.setDupResult(ddc.containsDuplicatedCode());

        refactoredMethods = ddc.getToEditMethodNames();
        refactoredFile = ddc.removeDuplication();
    }
    private static void printResult(ArrayList<String> locResult, ArrayList<String> paramResult, String dupResult) {
        StringBuilder result = new StringBuilder("Code smell detected!\n\n");
        boolean foundSmell = false;

        if (!locResult.isEmpty()) {
            result.append("- Method(s) '").append(locResult).append("' has more than 15 lines of code!\n\n");
            foundSmell = true;}
        if (!paramResult.isEmpty()) {
            result.append("- Method(s) '").append(paramResult).append("' has more than 3 parameters!\n\n");
            foundSmell = true;}
        if (dupResult.length() > 1) {
            result.append(dupResult);
            foundSmell = true;}

        if (!foundSmell) result = new StringBuilder("No code smell detected! Great job!");

        UIComponents.setResult(result.toString());
    }

    public static ArrayList<String> getRefactoredMethods(){
        return refactoredMethods;
    }
}
