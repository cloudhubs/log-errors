package ires.baylor.edu.logerrors.answer_sourcers.stack_overflow;

import ires.baylor.edu.logerrors.model.LogError;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TempControllerParametersNoDB {
    String pathToscraper; // path to scraper output
    LogError currentError; //Current error being matched
}
