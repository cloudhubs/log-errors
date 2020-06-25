package ires.baylor.edu.logerrors.matcher;

import com.mongodb.client.MongoDatabase;
import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.bson.Document;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;


/**
 * Finds multiple matches from the Stack Overflow scraped data to the Log Error given
 *
 * @author Elizabeth
 */
@Slf4j
public class StackOverflowScraperMatcher {
    private static final int PERCENT_MATCH = 85;

    /**
     * Matches the LogError to the StackOverflowQuestion array taken from the Database (Future implementation)
     *
     * @param SOFromDB   List of StackOverflowQuestions taken from Mark's Stack Overflow scraper
     * @param logToMatch LogError we wish to find Stack Overflow matches to
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
        String logErrorMsg = logToMatch.getErrorMessage();
        for (ScraperObject soq : SOFromDB) {
            for (String text : soq.getText()) {
                if (text.toUpperCase().contains(logErrorMsg.toUpperCase())) {
                    returnList.add(soq);
                    break;
                }
            }
            if (soq.getTitle().toUpperCase().contains(logErrorMsg.toUpperCase())) {
                returnList.add(soq);
            }
        }
        if (returnList.isEmpty()) {
            log.info("No valid results found - Continue parsing original error message");
        }
        return returnList;
    }

    /**
     * Receives the path to a scraper file (Changed when database is created) and the current error
     * Calls the FuzzyMatching method to find the similar results
     *
     * @param parameters Path to scraper file and current LogError
     * @return a list of StackOverflowQuestions that match the LogError given
     * @throws FileNotFoundException if the file cannot be opened
     */
    public static List<ScraperObject> matchLog(TempControllerParametersNoDB parameters) throws IOException, GeneralSecurityException {
        mongoConnector db = new mongoConnector();
        List<Document> documents = db.getAllFrom(db.getCollection("coll_name")); //.forEach((System.out::println));

        List<ScraperObject> obj = convertDocument(documents);
        List<ScraperObject> textMatch = TextMatching(obj, parameters.getCurrentError());
        textMatch.addAll(fuzzyMatching(obj, parameters.getCurrentError().getErrorMessage()));

        if(textMatch.isEmpty()){
            GoogleSearch.search(parameters.getCurrentError().getErrorMessage());
        }

        return removeDups(textMatch);
    }

    private static List<ScraperObject> removeDups(List<ScraperObject> textMatch) {
        for(int i = 0;i < textMatch.size(); i++){
            for(int j = i; j < textMatch.size(); j++){
                if(textMatch.get(i).equals(textMatch.get(j))){
                    textMatch.remove(textMatch.get(j));
                }
            }
        }
        return textMatch;
    }

    private static List<ScraperObject> convertDocument(List<Document> documents) {
        List<ScraperObject> scraper = new ArrayList<>();
        documents.forEach(d -> scraper.add(new ScraperObject(d)));
        return scraper;
    }
}
