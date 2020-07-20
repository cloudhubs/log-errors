package ires.baylor.edu.logerrors.controller;

import ires.baylor.edu.logerrors.model.ResolveErrorsRequest;
import ires.baylor.edu.logerrors.parser.LogErrorParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

/**
 * Error controller. This controller takes a log file and a source code directory
 * and returns all of the errors found inside the log file in the LogError structure
 */
@Slf4j
@RestController
public class LogController {
    @PostMapping("/errors")
    public ResponseEntity<?> resolveErrors(@RequestBody ResolveErrorsRequest request)  {
        log.info("Request: " + request);
        try {
            return new ResponseEntity<>(LogErrorParser.parseLog(request), HttpStatus.ACCEPTED);
        }
        catch(FileNotFoundException e){
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.NO_CONTENT);
        }
    }
}
