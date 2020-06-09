package ires.baylor.edu.logerrors.parser;

import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LogErrorParser {
    final static String POC_REGEX = "[0-9]{4}(?:-[0-9]{2}){2} (?:[0-9]{2}:){2}[0-9]{2},[0-9]*? ((?:WARNING)|(?:ERROR)) - (.*?\\.py:[0-9]*?) - (.*)";

    public static List<LogError> parseLog(String pathToLogFile) throws FileNotFoundException {
        Pattern linePattern = Pattern.compile(POC_REGEX);
        Scanner scan = new Scanner(new File(pathToLogFile));
        List<LogError> errors = new ArrayList<>();
        String currentLine;
        int numErrors = 0, lineNum = 0;


        while (scan.hasNextLine()) {
            currentLine = scan.nextLine();
            lineNum++;

            Matcher matcher = linePattern.matcher(currentLine);
            if (matcher.matches()) {
                numErrors++;
                log.info("Found Error: " + lineNum);
                errors.add(parseLine(currentLine, lineNum, pathToLogFile));
            }
        }
        return errors;
    }


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


}
