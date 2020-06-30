package ires.baylor.edu.logerrors.matcher.strategyPattern;

import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.model.LogError;

import java.util.List;

public interface MatcherAlgorithm {
    public static final int PERCENT_MATCH = 85;

    public List<ScraperObject> match(List<ScraperObject> SOFromDB, LogError logToMatch);
}
