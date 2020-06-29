package ires.baylor.edu.logerrors.matcher;

import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.bson.Document;

import java.io.FileNotFoundException;
<<<<<<< HEAD
=======
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.GeneralSecurityException;
>>>>>>> master
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;


/**
 * Finds multiple matches from the Stack Overflow scraped data to the Log Error given
 *
 * @author Elizabeth
 */
@Slf4j
public class StackOverflowScraperMatcher {
    private static final int PERCENT_MATCH = 85;
    final static String VAR_REGEX = "(\'.*\'|\".*\")";
    final static String VAR_REPLACEMENT = "VAR_VAL";
    static Pattern VARIABLE_DETECTION = Pattern.compile(VAR_REGEX);
    /**
     * Matches the LogError to the StackOverflowQuestion array taken from the Database (Future implementation)
     *
     * @param SOFromDB   List of StackOverflowQuestions taken from Mark's Stack Overflow scraper
     * @param logErrorMsg LogError we wish to find Stack Overflow matches to
     * @return a list of StackOverflowQuestions that match the LogError given
     */
    public static List<ScraperObject> fuzzyMatching(List<ScraperObject> SOFromDB, String logErrorMsg) {
        List<ScraperObject> returnList = new ArrayList<>();
        for (ScraperObject soq : SOFromDB) {
            if (FuzzySearch.tokenSortPartialRatio(logErrorMsg, soq.getTitle()) >= PERCENT_MATCH) {
                returnList.add(soq);
            }
        }
        if (returnList.isEmpty()) {
            log.info("No valid results found - Continue parsing original error message");
        }

        return returnList;
    }

    /**
     * Basic substring matching using text found from SO and data from the MongoDB
     *
     * @param SOFromDB
     * @param logToMatch
     * @return
     */
    public static List<ScraperObject> TextMatching(List<ScraperObject> SOFromDB, LogError logToMatch) {
        List<ScraperObject> returnList = new ArrayList<>();
        String logErrorMsg = logToMatch.getErrorMessage().toUpperCase();
        for (ScraperObject soq : SOFromDB) {
            if(FuzzySearch.tokenSortPartialRatio(logErrorMsg, soq.getTitle().toUpperCase()) >= PERCENT_MATCH) {
                returnList.add(soq);
            }
            /*if (soq.getTitle().toUpperCase().contains(logErrorMsg.toUpperCase())) {
                returnList.add(soq);
            }*/ else {
                for (String text : soq.getText()) {
                    if (text.toUpperCase().contains(logErrorMsg)) {
                        returnList.add(soq);
                        break;
                    }
                }
                if(!returnList.contains(soq)) {
                    for(String code: soq.getCode()) {
                        if(code.toUpperCase().contains(logErrorMsg)) {
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



    public static List<ScraperObject> advancedMatching(List<ScraperObject> SOFromDB, LogError logToMatch) {
        List<ScraperObject> returnList = new ArrayList<>();
        List<ScraperObject> returnListAdvanced = new ArrayList<>();

        String logErrorMsg = logToMatch.getErrorMessage().toUpperCase();
        String logErrorMsgAdvanced = replaceVars(logErrorMsg);
        for (ScraperObject soq : SOFromDB) {
            String title = replaceVars(soq.getTitle());
            if(FuzzySearch.tokenSortPartialRatio(logErrorMsg, soq.getTitle().toUpperCase()) >= PERCENT_MATCH) {
                returnList.add(soq);
            } else if (soq.getTitle().toUpperCase().contains(logErrorMsg.toUpperCase())) {
                returnList.add(soq);
            } else if(FuzzySearch.tokenSortPartialRatio(logErrorMsgAdvanced, title) >= PERCENT_MATCH) {
                returnListAdvanced.add(soq);
            } else {
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
                if(!returnList.contains(soq)) {
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

    /**
     * Receives the path to a scraper file (Changed when database is created) and the current error
     * Calls the FuzzyMatching method to find the similar results
     *
     * @param parameters Path to scraper file and current LogError
     * @return a list of StackOverflowQuestions that match the LogError given
     * @throws FileNotFoundException if the file cannot be opened
     */

    public static List<ScraperObject> matchLog(MatcherControllerParameters parameters) throws IOException, GeneralSecurityException {
        int found = 0;
        while (found < 2) {
            mongoConnector db = new mongoConnector();
            List<Document> documents = db.getAllFrom(db.getCollection("coll_name"));

            List<ScraperObject> obj = convertDocument(documents);
            List<ScraperObject> textMatch = TextMatching(obj, parameters.getCurrentError());
            textMatch.addAll(fuzzyMatching(obj, parameters.getCurrentError().getErrorMessage()));

            List<ScraperObject> bestList = removeDups(textMatch);

            if (bestList.isEmpty() && found < 1) {
                GoogleSearch.search(parameters.getCurrentError().getErrorMessage()).forEach((url -> {
                    try {
                        ScraperConnector.scrapeAndAdd(url.get(1));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
                found++;
            } else {
                found = 2;
                return bestList;
            }
        }
        return new ArrayList<ScraperObject>();
    }

    private static List<ScraperObject> removeDups(List<ScraperObject> textMatch) {
        List<ScraperObject> listWithoutDuplicates = new ArrayList<>(new HashSet<>(textMatch));
        return listWithoutDuplicates;
    }

    private static List<ScraperObject> convertDocument(List<Document> documents) {
        List<ScraperObject> scraper = new ArrayList<>();
        documents.forEach(d -> scraper.add(new ScraperObject(d)));
        return scraper;
    }
}
