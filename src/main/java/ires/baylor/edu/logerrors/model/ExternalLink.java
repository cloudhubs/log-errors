package ires.baylor.edu.logerrors.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ExternalLink {
    String link;
    int relevance; // percentage of relevance/matching with associated error
}
