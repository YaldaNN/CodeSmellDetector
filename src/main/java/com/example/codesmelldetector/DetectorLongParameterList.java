package com.example.codesmelldetector;

import java.util.ArrayList;
import java.util.Map;

public class DetectorLongParameterList {
    public int findParameters(String line){
        int startIndex;
        int endIndex;
        String insideParentheses = "";

        if (line.contains("(") && line.contains(")")) {
            startIndex = line.indexOf("(");
            endIndex = line.indexOf(")");
            insideParentheses = line.substring(startIndex + 1, endIndex);
        }
        String[] parameters = insideParentheses.split(",");
        return parameters.length;
    }

    public ArrayList<String> checkParams(Map<String, MethodInfo> map){
        final int PARAMETER_THRESHOLD = 3;
        ArrayList<String> foundSmells = new ArrayList<>();
        for (Map.Entry<String, MethodInfo> entry : map.entrySet()) {
            int tempValue = entry.getValue().paramCount;
            String tempKey = entry.getKey();

            if (tempValue > PARAMETER_THRESHOLD)
                foundSmells.add(tempKey);
        }
        return foundSmells;
    }
}
