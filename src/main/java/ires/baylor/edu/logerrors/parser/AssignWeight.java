package ires.baylor.edu.logerrors.parser;

import ires.baylor.edu.logerrors.model.FileStructure;
import ires.baylor.edu.logerrors.model.LogError;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssignWeight {
    public static final float ORIGINAL_WEIGHT = 1.0f;
    public static final float THIRD = 0.75f;
    public static final float SECOND = 0.5f;
    public static final float PRIMARY = 0.25f;
    private static Float [] floatArray;
    private static LogError error;
    private static List<String> usedVars;


    public static List<Float> assignWeight(LogError currentError) {
        error = currentError;
        char[] errorMsg = currentError.getErrorMessage().toCharArray();
        floatArray = new Float[errorMsg.length];

        for(int i = 0; i < floatArray.length; i++) {
            if(' ' == errorMsg[i]) {
                floatArray[i] = 0.0f;
            } else {
                floatArray[i] = ORIGINAL_WEIGHT;
            }
        }
        
        FileStructure fs = currentError.getCurrentFile(currentError.getSourceCodeFile());
        if(fs != null) {
            modifyWeights(PRIMARY, SECOND, fs);

            FileStructure currentImport = null;
            for(String str: fs.getImports()) {
                List<String> imports = currentError.parseImportString(str);
                if(!imports.isEmpty()) {
                    currentImport = currentError.getFileFromImport(imports.get(0));
                }

                for(String str2: imports) {
                    if(imports.indexOf(str2) != 0) {
                        if(error.getErrorMessage().contains(str2)) {
                            System.out.println("HERE: " + str2);
                            String[] getFullUseArray = error.getErrorMessage().split(" ");
                            String getFullUse = null;
                            for(String substring: getFullUseArray) {
                                if(substring.contains(str2)) {
                                    getFullUse = substring;
                                    break;
                                }
                            }
                            if(getFullUse.contains("=")) {
                                if(getFullUse.matches(".*"+str+".*=.*")) {
                                    System.out.println("First way");
                                    getFullUse = getFullUse.replaceAll("=.*", "");
                                } else {
                                    getFullUse = getFullUse.replaceAll(".*=", "");
                                }
                            }

                            int start = error.getErrorMessage().indexOf(getFullUse);
                            for(int i = start; i < (getFullUse.length()+start); i++) {
                                if(floatArray[i] == ORIGINAL_WEIGHT) {
                                    floatArray[i] = PRIMARY;
                                }
                            }
                        }
                    }

                }

                /*if(currentImport != null) {
                    modifyWeights(SECOND, THIRD, currentImport);
                }*/

            }
        }


        List<Float> weights = Arrays.asList(floatArray);
        return weights;
    }


    private static void modifyWeights(float weight1, float weight2, FileStructure fs) {

        findVarsUsed(error.getSourceCodeLine(), fs);
        for(String str: usedVars) {
            if(error.getErrorMessage().contains(str)) {
                int start = error.getErrorMessage().indexOf(str);
                for(int i = start; i < (str.length()+start); i++) {
                    if(floatArray[i] == ORIGINAL_WEIGHT) {
                        floatArray[i] = weight1;
                    }
                }
            }
        }

        findVarsUsed(error.getErrorMessage(), fs);
        for(String str: usedVars) {
            if(error.getErrorMessage().contains(str)) {
                int start = error.getErrorMessage().indexOf(str);
                for(int i = start; i < (str.length()+start); i++) {
                    if(floatArray[i] == ORIGINAL_WEIGHT) {
                        floatArray[i] = weight2;
                    }
                }
            }
        }
    }


    private static void findVarsUsed(String errorCode, FileStructure fs) {
        usedVars = new ArrayList<>();
        for(String str: fs.getVarNames()) {
            if(errorCode.contains(str) && !errorCode.contains("_"+str) && !errorCode.contains(str+"_")) {
                usedVars.add(str);
            }
        }
        for(String str: fs.getClassNames()) {
            if(errorCode.contains(str) && !errorCode.contains("_"+str) && !errorCode.contains(str+"_")) {
                usedVars.add(str);
            }
        }
        for(String str: fs.getFunctionNames()) {
            if(errorCode.contains(str) && !errorCode.contains("_"+str) && !errorCode.contains(str+"_")) {
                usedVars.add(str);
            }
        }

    }
}
