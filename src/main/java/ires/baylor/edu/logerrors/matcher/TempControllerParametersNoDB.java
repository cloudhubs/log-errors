package ires.baylor.edu.logerrors.matcher;

import ires.baylor.edu.logerrors.model.LogError;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TempControllerParametersNoDB {
    String pathToScraper; // path to scraper output
    LogError currentError; //Current error being matched
    Double variance; // leeway on matcher
}
