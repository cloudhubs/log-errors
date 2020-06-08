package ires.baylor.edu.logerrors.model;

import java.util.List;

public class LogError {
    String source; // path to log file
    int lineNumber; // starting line number within the source file

    String errorMessage;
    List<String> traceBacks;
    LogError nestedError;

    List<ExternalLink> stackOverflow;
    List<ExternalLink> gitHub;
}
