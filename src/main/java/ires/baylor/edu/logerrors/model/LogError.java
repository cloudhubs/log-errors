package ires.baylor.edu.logerrors.model;

import lombok.*;

import java.util.ArrayList;
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



    public LogError(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LogError(List<String> traceBacks){
        this.traceBacks = traceBacks;
    }

    public LogError(String errorMessage, String SourceCodeLine) {
        this.errorMessage = errorMessage;
        this.sourceCodeLine = SourceCodeLine;
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

    public List<String> parseImportString(String importLine) {
        List<String> str = new ArrayList<>();
        importLine = importLine.replaceAll("from ", "").replaceAll("import ", "");

        if(importLine.matches(".*as.*")) {
            String[] parsed = importLine.split(", ");
            for(int i = 0; i < parsed.length; i++) {
                String[] wordParse = parsed[i].split(" ");
                for(int j = 0; j < wordParse.length; j++) {
                    if(i == 0 && j == 0) {
                        str.add(wordParse[j].replaceAll(".*\\.", ""));
                    } else if(wordParse[j].matches(" *as *")) {
                        str.remove(str.size() - 1);
                        j++;
                        str.add(wordParse[j].replaceAll(",", ""));

                    } else {
                        str.add(wordParse[j]);
                    }
                }

            }
        } else {
            String [] parsed = importLine.split(" ");
            for(String temp: parsed) {
                if(!str.isEmpty()) {
                    str.add(temp);
                } else {
                    str.add(temp.replaceAll(".*\\.", ""));
                }
            }
        }

        return str;
    }


    public FileStructure getFileFromImport(String importLine) {
        importLine = importLine + ".py";
        if (files != null) {
            for (FileStructure f : files) {
                if (f.getFileName().equalsIgnoreCase(importLine)) {
                    return f;
                }
            }
        }
        return null;
    }
}
