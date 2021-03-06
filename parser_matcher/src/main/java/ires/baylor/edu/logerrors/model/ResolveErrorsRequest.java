package ires.baylor.edu.logerrors.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResolveErrorsRequest {
    String pathToLogFile; // path single log file
    String pathToSourceCodeDirectory; // path to source code directory
}
