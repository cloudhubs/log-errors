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
    int lineNumber; // starting line number within the source file

    Boolean isExternal;
    String errorMessage;
    List<String> traceBacks;
    LogError nestedError;

    List<ExternalLink> stackOverflow;
    List<ExternalLink> gitHub;


    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Boolean getExternal() {
        return isExternal;
    }

    public void setExternal(Boolean external) {
        isExternal = external;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<String> getTraceBacks() {
        return traceBacks;
    }

    public void setTraceBacks(List<String> traceBacks) {
        this.traceBacks = traceBacks;
    }

    public LogError getNestedError() {
        return nestedError;
    }

    public void setNestedError(LogError nestedError) {
        this.nestedError = nestedError;
    }

    public List<ExternalLink> getStackOverflow() {
        return stackOverflow;
    }

    public void setStackOverflow(List<ExternalLink> stackOverflow) {
        this.stackOverflow = stackOverflow;
    }

    public List<ExternalLink> getGitHub() {
        return gitHub;
    }

    public void setGitHub(List<ExternalLink> gitHub) {
        this.gitHub = gitHub;
    }
}
