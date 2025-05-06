package com.example.codesmelldetector;

import java.util.*;

import com.github.javaparser.*;
import com.github.javaparser.ast.body.MethodDeclaration;



public class DetectorDuplicatedCode {

    private Map<String,MethodDeclaration> ASTs = new HashMap<>();
    private Map<String, Set<String>> methodNodesSets = new HashMap<>();
    private static Set<String> duplicatedCodeBlock;
    private Map<String,String> methodSignatures = new HashMap<>();
    private ArrayList<String> toEditMethodNames = new ArrayList<>();
    private Map<String, Set<String>> duplicatedCode = new HashMap<>();
    private String result = "";
    public DetectorDuplicatedCode(Map<String, MethodInfo> initialMap){
        for (Map.Entry<String, MethodInfo> entry : initialMap.entrySet()){
            List<String> list = entry.getValue().method;
            methodSignatures.put(entry.getKey(), list.get(0));
            String methodCode = stringify(list);
            MethodDeclaration temp = buildAST(methodCode);
            ASTs.put(entry.getKey(),temp);
        }
        serializeMethods();
        detectDuplicatedCode();
    }

    private String stringify(List<String> methodLines){
        return String.join("\n", methodLines);
    }

    private MethodDeclaration buildAST(String methodCode){

        try {
            MethodDeclaration methodDeclaration = StaticJavaParser.parseMethodDeclaration(methodCode);
            return methodDeclaration;
        } catch (ParseProblemException e) {
            System.err.println("Error occurred during parsing: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    private void serializeMethods() {

        for (Map.Entry<String, MethodDeclaration> entry : ASTs.entrySet()) {
            Set<String> serializedNodes = NodeSerializer.serializeNodes(entry.getValue());
            methodNodesSets.put(entry.getKey(), serializedNodes);
        }
    }

    private void detectDuplicatedCode() {
        List<String> methodNames = new ArrayList<>(methodNodesSets.keySet());

        for (int i = 0; i < methodNames.size(); i++) {
            String methodName1 = methodNames.get(i);
            Set<String> nodesSet1 = methodNodesSets.get(methodName1);

            for (int j = i + 1; j < methodNames.size(); j++) {
                String methodName2 = methodNames.get(j);
                Set<String> nodesSet2 = methodNodesSets.get(methodName2);

                double similarity = calculateJaccardSimilarity(nodesSet1, nodesSet2);

                if (similarity >= 0.75) {
                    duplicatedCode.put(methodSignatures.get(methodName1), duplicatedCodeBlock);
                    System.out.println("High similarity detected between " + methodName1 + " and " + methodName2 + ": " + similarity);
                    result = result + "- Code duplication detected between methods " + methodName1 + " and " + methodName2 + "!\n";
                    toEditMethodNames.add(methodName1);} else System.out.println("Low similarity detected between " + methodName1 + " and " + methodName2 + ": " + similarity);}}
    }
    private double calculateJaccardSimilarity(Set<String> setA, Set<String> setB) {
        Set<String> intersection = new HashSet<>(setA);
        intersection.retainAll(setB);

        Set<String> union = new HashSet<>(setA);
        union.addAll(setB);

        deserialize(intersection);
        return (double) intersection.size() / union.size();
    }

    private static void deserialize(Set<String> serializedNodes) {
        duplicatedCodeBlock = new HashSet<>();
        for (String node : serializedNodes) {
            String[] parts = node.split(":", 2);

            if (parts.length == 2) duplicatedCodeBlock.add(parts[1].trim());
            else duplicatedCodeBlock.add(parts[0].trim());
        }
    }

    public String getSimilarityResult(){

        return result;
    }

    public boolean containsDuplicatedCode(){
        if (result.length() > 1) return true;
        return false;
    }
    public ArrayList<String> getToEditMethodNames(){
        return toEditMethodNames;
    }

    public List<String> removeDuplication(){
        List<String> fileContent = FileHandler.getFile();
        List<String> refactoredFile = new ArrayList<>();

        String key = "";
        boolean isInMethod = false;

        for (int i = 0; i < fileContent.size(); i++) {
            String line = fileContent.get(i);
            if (methodSignatures.containsValue(line)) isInMethod = false;

            if (!duplicatedCode.containsKey(line)) {
                if (!isInMethod) refactoredFile.add(line);
                else {
                    Set<String> valueSet = duplicatedCode.get(key);
                    boolean check = false;
                    for (String value : valueSet)
                        if (line.contains(value)) check = true;

                    if (!check) refactoredFile.add(line);
                }
            } else {
                isInMethod = true;
                refactoredFile.add(line);
                key = line;
            }
        }
        return refactoredFile;
    }
}
