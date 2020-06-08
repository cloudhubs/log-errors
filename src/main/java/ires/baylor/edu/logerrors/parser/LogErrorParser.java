package ires.baylor.edu.logerrors.parser;

import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public class LogErrorParser {
    Scanner scan;
    String POC_REGEX = "[0-9]{4}(?:-[0-9]{2}){2} (?:[0-9]{2}:){2}[0-9]{2},[0-9]*? ((?:WARNING)|(?:ERROR)) - (.*?\\.py:[0-9]*?) - (.*)";
    int numErrors;
    int lineNum;

    public List<LogError> parseLog(String pathToLogFile) {

        List<LogError> errors = new ArrayList<>();
        File file = new File(pathToLogFile);
        try {
            scan = new Scanner(file);
        } catch (FileNotFoundException e) {
            log.info("Unable to create scanner");
        }

        String currentLine;
        numErrors = lineNum = 0;
        while(scan.hasNextLine()) {
            currentLine = scan.nextLine();
            lineNum++;
            if(currentLine.matches(POC_REGEX)) {
                numErrors++;
                log.info("Found Error: " + numErrors);
                errors.add(parseLine(currentLine));

            }
        }
        return errors;
    }


    private LogError parseLine(String currentLine) {
        LogError currentError = new LogError();
        //Create new LogError
        currentError.setLineNumber(lineNum);

        currentError.setExternal(false);
        if(currentLine.toUpperCase().matches(".*ERROR.*")) {
            currentError.setExternal(true);
        }
        String[] parse = currentLine.split("- ");
        //Includes error source code location
        currentError.setErrorMessage(parse[1] + "- " + parse[2]);


        //etc....

        return currentError;
    }


}
