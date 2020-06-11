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

    Boolean isExternal;
    String errorMessage;
    List<String> traceBacks;
    LogError nestedError;

    List<ExternalLink> stackOverflow;
    List<ExternalLink> gitHub;

    public LogError(List<String> traceBacks){
        this.traceBacks = traceBacks;
    }
}
