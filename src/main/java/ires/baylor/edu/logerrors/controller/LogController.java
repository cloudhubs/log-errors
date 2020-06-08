package ires.baylor.edu.logerrors.controller;

import ires.baylor.edu.logerrors.model.LogError;
import ires.baylor.edu.logerrors.model.ResolveErrorsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class LogController {
    @PostMapping("/errors")
    public List<LogError> resolveErrors(@RequestBody ResolveErrorsRequest request) {
        log.info("Request: " + request);
        return null;
    }
}
