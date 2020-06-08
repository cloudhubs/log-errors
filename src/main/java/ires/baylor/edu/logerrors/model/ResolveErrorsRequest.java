package ires.baylor.edu.logerrors.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResolveErrorsRequest {
    String pathToLogFile; // path single log file
    String pathToLogDirectory; // directory path containing multiple log files

    public String getPathToLogFile() {
        return pathToLogFile;
    }

    public void setPathToLogFile(String pathToLogFile) {
        this.pathToLogFile = pathToLogFile;
    }

    public String getPathToLogDirectory() {
        return pathToLogDirectory;
    }

    public void setPathToLogDirectory(String pathToLogDirectory) {
        this.pathToLogDirectory = pathToLogDirectory;
    }
}
