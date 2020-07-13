package ires.baylor.edu.logerrors.matcher.strategy;

import ires.baylor.edu.logerrors.matcher.scraper.ScraperObject;
import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
public class AdvancedTextMatching extends MatcherAlgorithm{
    final static String VAR_REGEX = "(\'.*\'|\".*\")";
    final static String VAR_REPLACEMENT = "VAR_VAL";
    static Pattern VARIABLE_DETECTION = Pattern.compile(VAR_REGEX);


    @Override
    public List<ScraperObject> match(List<ScraperObject> SOFromDB, LogError logToMatch) {
        if(SOFromDB == null || logToMatch == null || logToMatch.getErrorMessage() == null) {
            return new ArrayList<>();
        }
        List<ScraperObject> returnList = new ArrayList<>();
        List<ScraperObject> returnListAdvanced = new ArrayList<>();

        String logErrorMsg = logToMatch.getErrorMessage().toUpperCase();
        String logErrorMsgAdvanced = replaceVars(logErrorMsg);
        for (ScraperObject soq : SOFromDB) {
            if(soq.getTitle() == null) {
                continue;
            }
            String title = replaceVars(soq.getTitle());
            if(FuzzySearch.tokenSortPartialRatio(logErrorMsg, soq.getTitle().toUpperCase()) >= PERCENT_MATCH) {
                returnList.add(soq);
            } else if (soq.getTitle().toUpperCase().contains(logErrorMsg.toUpperCase())) {
                returnList.add(soq);
            } else if(FuzzySearch.tokenSortPartialRatio(logErrorMsgAdvanced, title) >= PERCENT_MATCH) {
                returnListAdvanced.add(soq);
            } else {
                if(soq.getText() != null) {
                    for (String text : soq.getText()) {
                        if (text.toUpperCase().contains(logErrorMsg)) {
                            returnList.add(soq);
                            break;
                        }
                        text = replaceVars(text);
                        if (text.contains(logErrorMsgAdvanced)) {
                            returnListAdvanced.add(soq);
                            break;
                        }
                    }
                }
                if(!returnList.contains(soq) && soq.getCode() != null) {
                    for(String code: soq.getCode()) {
                        if(code.toUpperCase().contains(logErrorMsg)) {
                            returnList.add(soq);
                            break;
                        }
                        code = replaceVars(code);
                        if(code.contains(logErrorMsgAdvanced)) {
                            returnListAdvanced.add(soq);
                            break;
                        }
                    }
                }
            }

        }
        if (returnList.isEmpty()) {
            log.info("No valid results found - Return advanced list with " + returnListAdvanced.size() + " results");
            return returnListAdvanced;
        }
        return returnList;
    }

    private static String replaceVars(String str) {
        if (str.matches("^(\'.*\'|\".*\")$")) {
            str = str.substring(1, str.length()-2);
        }
        return str.replaceAll(VARIABLE_DETECTION.pattern(), VAR_REPLACEMENT).toUpperCase();
    }
}
