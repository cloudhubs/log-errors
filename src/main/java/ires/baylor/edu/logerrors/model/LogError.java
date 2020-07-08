package ires.baylor.edu.logerrors.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LogError {
    String source; // path to log file
    int lineNumber; // starting line number within the log file

    String sourceCodeLine;
    String sourceCodeFile;
    List<FileStructure> files; //List of classes and their respective functions and imports
    List<String> externalPackages; //From requirements.txt file

    List<Float> errorCharWeight;

    Boolean isExternal;
    String errorMessage;
    List<String> traceBacks;
    LogError nestedError;

    List<ExternalLink> stackOverflow;
    List<ExternalLink> gitHub;

    public LogError(List<String> traceBacks){
        this.traceBacks = traceBacks;
    }

    public FileStructure getCurrentFile(String filename) {
        if (files != null) {
            for (FileStructure f : files) {
                if (f.getFileName().equalsIgnoreCase(filename)) {
                    return f;
                }
            }
        }
        return null;
    }

    public FileStructure getFileFromImport(String importLine) {
        String temp = importLine.replaceAll(".*from ","");

        if(importLine.matches("import.*")) {
            temp = temp.replaceAll("import ", "");
        } else if(importLine.matches(".*import.*")) {
            temp = temp.replaceAll(" import.*", "");
        }
        temp = temp.replaceAll(".*\\.", "");
        temp = temp + ".py";
        if (files != null) {
            for (FileStructure f : files) {
                if (f.getFileName().equalsIgnoreCase(temp)) {
                    return f;
                }
            }
        }
        return null;
    }
}
