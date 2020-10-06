package ires.baylor.edu.logerrors.matcher.strategy;

import ires.baylor.edu.logerrors.matcher.scraper.ScraperObject;
import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BasicTextMatching extends MatcherAlgorithm {
    /**
     * Basic substring matching using text found from SO and data from the MongoDB
     */
    @Override
    public List<ScraperObject> match(List<ScraperObject> SOFromDB, LogError logToMatch) {
        if (SOFromDB == null || logToMatch == null || logToMatch.getErrorMessage() == null) {
            return new ArrayList<>();
        }
        List<ScraperObject> returnList = new ArrayList<>();
        String logErrorMsg = logToMatch.getErrorMessage().toUpperCase();
        for (ScraperObject soq : SOFromDB) {
            if (soq.getTitle() == null) {
                continue;
            }
            if (FuzzySearch.tokenSortPartialRatio(logErrorMsg, soq.getTitle().toUpperCase()) >= PERCENT_MATCH) {
                returnList.add(soq);
            }
            /*if (soq.getTitle().toUpperCase().contains(logErrorMsg.toUpperCase())) {
                returnList.add(soq);
            }*/
            else if (soq.getText() != null) {
                for (String text : soq.getText()) {
                    if (text.toUpperCase().contains(logErrorMsg)) {
                        returnList.add(soq);
                        break;
                    }
                }
                if (!returnList.contains(soq) && soq.getCode() != null) {
                    for (String code : soq.getCode()) {
                        if (code.toUpperCase().contains(logErrorMsg)) {
                            returnList.add(soq);
                            break;
                        }
                    }
                }
            }
        }
        if (returnList.isEmpty()) {
            log.info("No valid results found - Continue parsing original error message");
        }
        return returnList;
    }
}
