package ires.baylor.edu.logerrors.controller;

import ires.baylor.edu.logerrors.matcher.ScraperConnector;
import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.matcher.StackOverflowScraperMatcher;
import ires.baylor.edu.logerrors.matcher.TempControllerParametersNoDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@RestController
public class LogMatcherController {
    @GetMapping("/matcher")
    public ResponseEntity<?> resolveErrors(@RequestBody TempControllerParametersNoDB request)  {
        log.info("Request: " + request);
        try {
            List<ScraperObject> results = StackOverflowScraperMatcher.matchLog(request);

            if (results.isEmpty()){
                return new ResponseEntity<>(results, HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(results, HttpStatus.ACCEPTED);

        } catch(GeneralSecurityException | IOException e){
            return new ResponseEntity<>(e.getStackTrace(), HttpStatus.NO_CONTENT);
        }
    }
}
