package ires.baylor.edu.logerrors.matcher.strategy;

import ires.baylor.edu.logerrors.matcher.scraper.ScraperObject;
import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class ScoreTextMatching extends MatcherAlgorithm {
    final static String VAR_REGEX = "(\'.*\'|\".*\")";
    final static String VAR_REPLACEMENT = "VAR_VAL";
    static Pattern VARIABLE_DETECTION = Pattern.compile(VAR_REGEX);

    private static String replaceVars(String str) {
        if (str != null && str.length() > 2 && str.matches("^(\'.*\'|\".*\")$")) {
            str = str.substring(1, str.length() - 2);
        }
        return str.replaceAll(VARIABLE_DETECTION.pattern(), VAR_REPLACEMENT).toUpperCase();
    }

    /**
     * Uses a score based system to return the 10 most similar scraped data objects based
     * on the title, text and code of the scraped object.
     */
    @Override
    public List<ScraperObject> match(List<ScraperObject> SOFromDB, LogError logToMatch) {
        if (SOFromDB == null || logToMatch == null || logToMatch.getErrorMessage() == null) {
            return new ArrayList<>();
        }
        Map<Integer, List<ScraperObject>> ScraperObjectScores = new HashMap<>();
        int highscore = 0;

        String logErrorMsg = logToMatch.getErrorMessage().toUpperCase();
        String logErrorMsgAdvanced = replaceVars(logErrorMsg);
        int matchVal = 0;

        for (ScraperObject soq : SOFromDB) {
            if (soq.getTitle() == null) {
                continue;
            }
            int score = 0;
            String title = replaceVars(soq.getTitle());
            if (soq.getTitle().toUpperCase().contains(logErrorMsg.toUpperCase())) {
                score += 400;
            } else if ((matchVal = FuzzySearch.tokenSortPartialRatio(logErrorMsg, soq.getTitle().toUpperCase())) >= 60) {
                score += Math.round(matchVal / 10.0) * 10 * 2;
            } else if ((matchVal = FuzzySearch.tokenSortPartialRatio(logErrorMsgAdvanced, title)) >= 60) {
                score += Math.round(matchVal / 10.0) * 10 * 2;
            }
            if (soq.getText() != null) {
                for (String text : soq.getText()) {
                    if (text.toUpperCase().contains(logErrorMsg)) {
                        score += 30;
                    } else if (replaceVars(text).contains(logErrorMsgAdvanced)) {
                        score += 20;
                    }
                }
            }
            if (soq.getCode() != null) {
                for (String code : soq.getCode()) {
                    if (code.toUpperCase().contains(logErrorMsg)) {
                        score += 30;
                    } else if (replaceVars(code).contains(logErrorMsgAdvanced)) {
                        score += 20;
                    }
                }
            }


            if (score > highscore) {
                highscore = score;
            }
            if (score > 0) {
                //If the score bracket does not exist then create a new bracket for it
                if (!ScraperObjectScores.containsKey(score)) {
                    ScraperObjectScores.put(score, new ArrayList<>());
                }
                //add the current scraper object to the map
                ScraperObjectScores.get(score).add(soq);
            }


        }

        List<ScraperObject> returnList = new ArrayList<>();

        //Only returns the top 10 results - can change that if we want
        while (highscore >= 0 && returnList.size() < 10) {
            if (ScraperObjectScores.get(highscore) != null) {
                for (ScraperObject soq : ScraperObjectScores.get(highscore)) {
                    if (returnList.size() < 10) {
                        returnList.add(soq);
                    }
                }
            }
            highscore -= 10;
        }

        return returnList;
    }
}
