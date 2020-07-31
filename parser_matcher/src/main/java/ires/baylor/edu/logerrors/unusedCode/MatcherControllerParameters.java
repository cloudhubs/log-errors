package ires.baylor.edu.logerrors.unusedCode;

import ires.baylor.edu.logerrors.model.LogError;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MatcherControllerParameters {
    String pathToScraper; // path to scraper output
    LogError currentError; //Current error being matched
    Double variance; // leeway on matcher
}
