package ires.baylor.edu.logerrors.controller;

import ires.baylor.edu.logerrors.answer_sourcers.stack_overflow.StackOverflowScraperMatcher;
import ires.baylor.edu.logerrors.answer_sourcers.stack_overflow.TempControllerParametersNoDB;
import ires.baylor.edu.logerrors.model.ResolveErrorsRequest;
import ires.baylor.edu.logerrors.parser.LogErrorParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;


@Slf4j
@RestController
public class LogMatcherController {
    @PostMapping("/matcher")
    public ResponseEntity<?> resolveErrors(@RequestBody TempControllerParametersNoDB request)  {
        log.info("Request: " + request);
        try {
            return new ResponseEntity<>(StackOverflowScraperMatcher.matchLog(request), HttpStatus.ACCEPTED);
        }
        catch(FileNotFoundException e){
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.NO_CONTENT);
        }
    }
}
