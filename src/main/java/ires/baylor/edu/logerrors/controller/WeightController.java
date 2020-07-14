package ires.baylor.edu.logerrors.controller;

import ires.baylor.edu.logerrors.model.LogError;
import ires.baylor.edu.logerrors.parser.AssignWeight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
public class WeightController {

    @GetMapping("/weight")
    public ResponseEntity<?> resolveErrors(@RequestBody LogError error) {

        log.info("Request: " + error);

        List<Float> results = AssignWeight.assignWeight(error);

        if (results.isEmpty()) {
            return new ResponseEntity<>(results, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(results, HttpStatus.ACCEPTED);

    }

}
