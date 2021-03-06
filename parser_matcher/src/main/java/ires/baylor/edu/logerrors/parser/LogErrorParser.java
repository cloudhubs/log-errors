package ires.baylor.edu.logerrors.parser;

import ires.baylor.edu.logerrors.model.FileStructure;
import ires.baylor.edu.logerrors.model.LogError;
import ires.baylor.edu.logerrors.model.ResolveErrorsRequest;
import ires.baylor.edu.logerrors.util.PeekableScanner;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Slf4j

/**
 * Parses a log file into a 2d linked list.
 */
public class LogErrorParser {
    final static String POC_REGEX = "[0-9]{4}(?:-[0-9]{2}){2} (?:[0-9]{2}:){2}[0-9]{2},[0-9]*? ((?:WARNING)|(?:ERROR)) - (.*?\\.py:[0-9]*?) - (.*)";
    final static String ENTRY_REGEX = "[0-9]{4}(?:-[0-9]{2}){2} (?:[0-9]{2}:){2}[0-9]{2},[0-9]*? ((?:WARNING)|(?:ERROR)|(?:DEBUG)|(?:INFO)) - (.*?\\.py:[0-9]*?) - (.*)";
    final static String TRACEBACK_REGEX = "File \"([^\"]*)\", line ([0-9]*),.*";
    final static String NESTED_REGEX = "During handling of the above exception, another exception occurred:";
    static Pattern errorEntryPattern = Pattern.compile(POC_REGEX);
    static Pattern tracebackExitPattern = Pattern.compile(ENTRY_REGEX);
    static Pattern tracebackEntryPattern = Pattern.compile(TRACEBACK_REGEX);
    static Pattern nestedEntryPattern = Pattern.compile(NESTED_REGEX);
    static List<FileStructure> commonClassStructure;
    static List<String> commonExternalSources;
    private static int lineNum;
    private static String pathToDir;

    /**
     * Tokenizes the log file.
     *
     * @param inputObject Is the local path to the log file
     * @return A list containing {@link LogError} objects. Which represent the heirarchy of the errors.
     */
    public static List<LogError> parseLog(ResolveErrorsRequest inputObject) throws FileNotFoundException {

        pathToDir = inputObject.getPathToSourceCodeDirectory();
        commonClassStructure = ProjectStructureParser.getClassStructure(pathToDir);
        commonExternalSources = ProjectStructureParser.getRequirements(pathToDir);

        PeekableScanner scan = new PeekableScanner(new File(inputObject.getPathToLogFile()));
        List<LogError> errors = new ArrayList<>();

        lineNum = 0;
        while (scan.hasNextLine()) {
            lineNum++;

            // There are 3 types of lines. Entries, tracebacks and nested notifiers. Check for each and react accordingly
            if (errorEntryPattern.matcher(scan.peekLine()).matches()) {
                /** line is considered the beginning of an error. **/
                log.info("Found Entry: " + lineNum);
                errors.add(parseLine(scan.nextLine(), lineNum, inputObject.getPathToLogFile()));
                //Check if there is a traceback. If so update the error message.
                while (tracebackEntryPattern.matcher(scan.peekLine()).matches()) {
                    /** line is the beginning of a traceback segment.**/
                    log.info("Traceback located for error " + scan.peekLine());
                    LogError deepest = LogErrorParser.getDeepest(errors);
                    List<String> traceback = addTraceback(scan);
                    deepest.setErrorMessage(traceback.get(traceback.size() - 1));
                    deepest.setTraceBacks(traceback);
                    List<String> srcCode = getSourceCodeLine(deepest);
                    deepest.setSourceCodeLine(srcCode.get(0));
                    deepest.setSourceCodeFile(srcCode.get(1));
                    deepest.setErrorCharWeight(AssignWeight.assignWeight(deepest));
                }
            } else if (nestedEntryPattern.matcher(scan.peekLine()).matches()) {
                /** line is the beginning of a nested segment. **/
                log.info("Nested exception located for error");
                scan.nextLine();
                log.info("Traceback located for error " + scan.peekLine());
                LogError deepest = LogErrorParser.getDeepest(errors);
                LogError deepestNested = new LogError();
                List<String> traceback = addTraceback(scan);
                deepestNested.setErrorMessage(traceback.get(traceback.size() - 1));
                deepestNested.setTraceBacks(traceback);
                List<String> srcCode = getSourceCodeLine(deepestNested);
                deepestNested.setSourceCodeLine(srcCode.get(0));
                deepestNested.setSourceCodeFile(srcCode.get(1));
                deepestNested.setLineNumber(lineNum + 1);
                deepestNested.setErrorCharWeight(AssignWeight.assignWeight(deepestNested));
                deepest.setNestedError(deepestNested);

            } else {
                //line is not important and can be skipped
                scan.nextLine();
            }
        }
        return errors;
    }


    /**
     * parseLine: tokenizes the line to be populated into the {@link LogError} object
     *
     * @param currentLine   the line to be parsed
     * @param lineNum       the position in the file
     * @param pathToLogFile the location on disk
     * @return {@link LogError} is return with populated fields.
     */
    private static LogError parseLine(String currentLine, int lineNum, String pathToLogFile) {
        LogError currentError = new LogError();
        //Create new LogError
        currentError.setFiles(commonClassStructure);
        currentError.setExternalPackages(commonExternalSources);
        currentError.setLineNumber(lineNum);
        currentError.setSource(pathToLogFile);

        currentError.setIsExternal(false);
        if (currentLine.toUpperCase().matches(".*ERROR.*")) {
            currentError.setIsExternal(true);
        }
        String[] parse = currentLine.split("- ");
        //Includes error source code location
        currentError.setErrorMessage(parse[1] + "- " + parse[2]);

        return currentError;
    }


    /**
     * addNested: begins the parsing for the nested errors. Calls {@inheritDoc addTraceback}
     *
     * @param errLog the scanner representation of the file
     * @return the parsed string that is the traceback array
     */
    private static List<String> addTraceback(PeekableScanner errLog) {
        List<String> traceback = new ArrayList<>();
        StringBuilder concatenator = new StringBuilder();
        String line;

        // Get all trace entries
        while (errLog.hasNextLine() && !(tracebackExitPattern.matcher(errLog.peekLine()).matches() || nestedEntryPattern.matcher(errLog.peekLine()).matches())) {
            line = errLog.nextLine();
            lineNum++;
            //the traceback has not finished. continue to read and append
            if (errLog.hasNextLine()) {
                concatenator.append(line).append("\n").append(errLog.nextLine());
                lineNum++;
                line = concatenator.toString();
                concatenator.setLength(0);
            }
            traceback.add(line);

        }

        return traceback;
    }

    private static LogError getDeepest(List<LogError> errors) {
        LogError lastError = errors.get(errors.size() - 1);

        while (lastError.getNestedError() != null) {
            lastError = lastError.getNestedError();
        }

        return lastError;
    }


    private static List<String> getSourceCodeLine(LogError current) {

        String currentTrace = null;
        String firstImport = null;
        String fileName = null;
        String firstImportFileName = null;
        List<String> returnStr = new ArrayList<>();


        if (current != null) {
            File folder = new File(pathToDir);

            for (String str : current.getTraceBacks()) {
                String[] strArray = str.split("\n");
                fileName = strArray[0].replaceAll("\",.*", "").replaceAll(".*\".*/", "");
                currentTrace = strArray[1];

                if (currentTrace != null && !currentTrace.isEmpty()) {
                    if (!currentTrace.matches(".*import.*") && !currentTrace.matches(".*Traceback.*")) {
                        break;
                    } else if (firstImport == null && !currentTrace.matches(".*Traceback.*")) {
                        firstImport = currentTrace;
                        firstImportFileName = fileName;
                    }

                }
            }
            if (currentTrace == null || currentTrace.isEmpty()) {
                currentTrace = firstImport;
                fileName = firstImportFileName;
            }
        }
        returnStr.add(currentTrace);
        returnStr.add(fileName);

        return returnStr;
    }


}
