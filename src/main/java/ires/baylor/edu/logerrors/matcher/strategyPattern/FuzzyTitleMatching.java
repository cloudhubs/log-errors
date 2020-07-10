package ires.baylor.edu.logerrors.matcher.strategyPattern;

import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class FuzzyTitleMatching implements MatcherAlgorithm {

    /**
     * Matches the LogError to the StackOverflowQuestion array taken from the Database (Future implementation)
     *
     * @param SOFromDB   List of StackOverflowQuestions taken from Mark's Stack Overflow scraper
     * @param logToMatch LogError we wish to find Stack Overflow matches to
     * @return a list of StackOverflowQuestions that match the LogError given
     */
    @Override
    public List<ScraperObject> match(List<ScraperObject> SOFromDB, LogError logToMatch) {
        if(SOFromDB == null || logToMatch == null || logToMatch.getErrorMessage() == null) {
            return new ArrayList<>();
        }
        List<ScraperObject> returnList = new ArrayList<>();
        for (ScraperObject soq : SOFromDB) {
            if(soq.getTitle() == null) {
                continue;
            }
            if (FuzzySearch.tokenSortPartialRatio(logToMatch.getErrorMessage(), soq.getTitle()) >= PERCENT_MATCH) {
                returnList.add(soq);
            }
        }
        if (returnList.isEmpty()) {
            log.info("No valid results found - Continue parsing original error message");
        }

        return returnList;
    }
}
