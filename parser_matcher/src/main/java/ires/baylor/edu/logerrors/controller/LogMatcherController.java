package ires.baylor.edu.logerrors.controller;

import ires.baylor.edu.logerrors.matcher.scraper.ScraperObject;
import ires.baylor.edu.logerrors.matcher.stackoverflow.StackOverflowScraperMatcher;
import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;


/**
 * Log Matcher Controller. This controller takes a logError as input and matches
 * it to the scraped data currently in the database. If no match is found similar
 * data will be scraped from google and returned.
 */
@Slf4j
@RestController
public class LogMatcherController {

    @GetMapping("/matcher")
    public ResponseEntity<?> resolveErrors(@RequestBody LogError request)  {

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
