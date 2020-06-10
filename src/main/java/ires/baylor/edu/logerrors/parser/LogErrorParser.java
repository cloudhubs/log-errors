package ires.baylor.edu.logerrors.parser;

import ires.baylor.edu.logerrors.model.LogError;
import ires.baylor.edu.logerrors.util.PeekableScanner;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;


@Slf4j

/**
 * Parses a log file into a 2d linked list.
 */
public class LogErrorParser {
    final String POC_REGEX = "[0-9]{4}(?:-[0-9]{2}){2} (?:[0-9]{2}:){2}[0-9]{2},[0-9]*? ((?:WARNING)|(?:ERROR)) - (.*?\\.py:[0-9]*?) - (.*)";
    final String ENTRY_REGEX = "[0-9]{4}(?:-[0-9]{2}){2} (?:[0-9]{2}:){2}[0-9]{2},[0-9]*? ((?:WARNING)|(?:ERROR)|(?:DEBUG)|(?:INFO)) - (.*?\\.py:[0-9]*?) - (.*)";
    final String TRACEBACK_REGEX = "File \"([^\"]*)\", line ([0-9]*),.*";
    final String NESTED_REGEX = "During handling of the above exception, another exception occurred:";
    Pattern errorEntryPattern = Pattern.compile(POC_REGEX);
    Pattern tracebackExitPattern = Pattern.compile(ENTRY_REGEX);
    Pattern tracebackEntryPattern = Pattern.compile(TRACEBACK_REGEX);
    Pattern nestedEntryPattern = Pattern.compile(NESTED_REGEX);


    /**
     * Tokenizes the log file.
     *
     * @param pathToLogFile Is the local path to the log file
     * @return A list containing {@link LogError} objects. Which represent the heirarchy of the errors.
     */
    public List<LogError> parseLog(String pathToLogFile) throws FileNotFoundException {
        PeekableScanner scan = new PeekableScanner(new File(pathToLogFile));
        List<LogError> errors = new ArrayList<>();
        String nextLine;
        int lineNum = 0;


        while (scan.hasNextLine()) {
            nextLine = scan.peekLine();
            lineNum++;

            // There are 3 types of lines. Entries, tracebacks and nested notifiers. Check for each and react accordingly
            if (errorEntryPattern.matcher(nextLine).matches()) {

                /** line is considered the beginning of an error. **/
                log.info("Found Entry: " + lineNum);
                errors.add(parseLine(scan.nextLine(), lineNum, pathToLogFile));
            } else if (tracebackEntryPattern.matcher(nextLine).matches()) {

                /** line is the beginning of a traceback segment.**/
                log.info("Traceback located for error");
                ArrayList<String> tracebacks = new ArrayList();
                tracebacks.addAll(addTraceback(scan));
                errors.get(errors.size() - 1).setTraceBacks(tracebacks);
            } else if (nestedEntryPattern.matcher(nextLine).matches()) {

                /** line is the beginning of a nested segment. **/
                log.info("Nested exception located for error");
                // the following lines are to be appended to the last object. In the given file they are all python tracebacks. TODO: update this for new file types.
                errors.get(errors.size() - 1).setNestedError(new LogError(addNested(scan)));
            } else {
                // line is not important and can be skipped
                scan.nextLine();
            }
        }
        return errors;
    }


    /**
     * parseLine: tokenizes the line to be populated into the {@link LogError} object
     * @param currentLine the line to be parsed
     * @param lineNum the position in the file
     * @param pathToLogFile the location on disk
     * @return {@link LogError} is return with populated fields.
     */
    private static LogError parseLine(String currentLine, int lineNum, String pathToLogFile) {
        LogError currentError = new LogError();
        //Create new LogError
        currentError.setLineNumber(lineNum);
        currentError.setSource(pathToLogFile);

        currentError.setIsExternal(false);
        if (currentLine.toUpperCase().matches(".*ERROR.*")) {
            currentError.setIsExternal(true);
        }
        String[] parse = currentLine.split("- ");
        //Includes error source code location
        currentError.setErrorMessage(parse[1] + "- " + parse[2]);


        //etc....

        return currentError;
    }

    /**
     * addNested: begins the parsing for the nested errors. Calls {@inheritDoc addTraceback}
     * @param errLog the scanner representation of the file
     * @return the parsed string that is the traceback array
     */
    private List<String> addNested(PeekableScanner errLog) {
        String line = errLog.nextLine();
        return addTraceback(errLog);
    }

    /**
     * addNested: begins the parsing for the nested errors. Calls {@inheritDoc addTraceback}
     * @param errLog the scanner representation of the file
     * @return the parsed string that is the traceback array
     */
    private List<String> addTraceback(PeekableScanner errLog) {
        List<String> traceback = new ArrayList<>();
        StringBuilder concatenator = new StringBuilder();
        String line;

        // Get all trace entries
        while (errLog.hasNextLine() && !(tracebackExitPattern.matcher(errLog.peekLine()).matches() || nestedEntryPattern.matcher(errLog.peekLine()).matches())) {
            line = errLog.nextLine();

            //the traceback has not finished. continue to read and append
            if (errLog.hasNextLine()) {
                concatenator.append(line).append("\n").append(errLog.nextLine());
                line = concatenator.toString();
                concatenator.setLength(0);
            }
            traceback.add(line);

        }

        return traceback;
    }

}
