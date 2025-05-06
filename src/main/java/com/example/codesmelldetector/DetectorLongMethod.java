package com.example.codesmelldetector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MethodInfo {
    int loc;
    int paramCount;
    List<String> method;
    public MethodInfo(int loc, int paramCount, List<String> method) {

        this.loc = loc;
        this.paramCount = paramCount;
        this.method = method;
    }
}
public class DetectorLongMethod {
    private List<String> fileContent;
    private Map<String, MethodInfo> map = new HashMap<>();
    private String methodName = "";
    private String className = "";
    private int openBrackets = 0;
    private int closeBrackets = 0;
    private int counter = 0;
    private int emptyCounter = 0;
    DetectorLongMethod(List<String> fileContent){
        this.fileContent = fileContent;
    }

    private String extractMethodName(String nameLine){
        nameLine = nameLine.replaceAll("\\w+\\s*<", "<");
        nameLine = nameLine.replaceAll("\\(.*", "");       // Remove everything after the opening parenthesis
        nameLine = nameLine.replaceAll("\\(.*?\\)", "");  // Remove everything within parentheses
        nameLine = nameLine.replaceAll("<.*?>", "");      // Remove generic types
        nameLine = nameLine.replaceAll("\\[.*?\\]", "");  // Remove square brackets and their contents
        nameLine = nameLine.replaceAll("@\\w+", "");      // Remove annotations
        nameLine = nameLine.replaceAll("\\b(public|private|protected|static|void|String|byte|char|double|int|bool|long|short|float|Object|else|while|do)\\s+", "");  // Remove access modifiers and other keywords
        nameLine = nameLine.replaceAll("[ \\t]", "");     // Remove whitespace and tabs
        nameLine = nameLine.replaceAll("[{}]", "");       // Remove curly braces
        nameLine = nameLine.replaceAll("//.*", "");
        return nameLine;
    }

    public void methodFinder(){
        boolean isInMethod = false;
        for (int i = 0; i < fileContent.size(); i++){
            String line = fileContent.get(i);
            if (line.contains("class ")) className = extractClassName(line);

            if (checkName(line)){
                String temp = extractMethodName(line);
                if (!(temp.trim()).isEmpty() && !temp.equals(className)) {
                    methodName = temp;
                    map.put(methodName,new MethodInfo(0,0, new ArrayList<>()));
                    analyzeParameters(line);
                    counter = i;
                    isInMethod = true;}}

            if (isInMethod && line.trim().isEmpty()) emptyCounter++;

            if (isInMethod){
                map.get(methodName).method.add(line);
                if (!countLinesOfCode(line, i)) isInMethod = false;}}
    }
    private boolean checkName(String line){

        int indexOfFirstParenthesis = line.indexOf("(");
        boolean hasPeriodBeforeFirstParenthesis = false;

        if (indexOfFirstParenthesis != -1) {
            String substringBeforeFirstParenthesis = line.substring(0, indexOfFirstParenthesis);
            hasPeriodBeforeFirstParenthesis = substringBeforeFirstParenthesis.contains(".");
        }
        if (!line.contains("class") && !line.contains("=") && !line.contains(";") && !hasPeriodBeforeFirstParenthesis &&
                !line.trim().isEmpty() && !line.trim().startsWith("//") && !line.contains("if") &&
                !line.contains("else") && !line.contains("else if") && !line.contains("try") && !line.contains("catch")) {
            return true;
        }
        return false;
    }

    private String extractClassName(String line) {
        String pattern = "\\bclass\\s+([\\w$]+)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);

        if (m.find()) {
            return m.group(1);
        } else {
            return null;
        }
    }

    private void analyzeParameters(String line){
        DetectorLongParameterList dlpl = new DetectorLongParameterList();
        MethodInfo tempPair = map.get(methodName);
        tempPair.paramCount = dlpl.findParameters(line);
        map.put(methodName,tempPair);
    }

    private boolean countLinesOfCode(String line, int i){

        if(line.contains("{")) openBrackets++;
        if(line.contains("}")) closeBrackets++;

        if(openBrackets == closeBrackets && openBrackets != 0){
            counter = i - counter + 1- emptyCounter;
            MethodInfo temp = map.get(methodName);
            temp.loc = counter;
            map.put(methodName,temp);
            counter = 0;
            openBrackets = 0;
            closeBrackets = 0;
            emptyCounter = 0;
            return false; }
        return true;
    }
    public ArrayList<String> checkLOC(){
        final int LOC_THRESHOLD = 15;
        ArrayList<String> foundSmells = new ArrayList<>();
        for (Map.Entry<String, MethodInfo> entry : map.entrySet()) {
            int tempValue = entry.getValue().loc;
            String tempKey = entry.getKey();
            if (tempValue > LOC_THRESHOLD)
                foundSmells.add(tempKey);
        }
        return foundSmells;
    }

    public Map<String, MethodInfo> getMethodMap(){ return map; }
}
