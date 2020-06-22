package ires.baylor.edu.logerrors.matcher;

import com.mongodb.client.MongoDatabase;
import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.bson.Document;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
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
     * @param SOFromDB List of StackOverflowQuestions taken from Mark's Stack Overflow scraper
     * @param logToMatch LogError we wish to find Stack Overflow matches to
     * @return a list of StackOverflowQuestions that match the LogError given
     */
    public static List<StackOverflowQuestion> fuzzyMatching(List<StackOverflowQuestion> SOFromDB, LogError logToMatch)  {
        List<StackOverflowQuestion> returnList = new ArrayList<>();
        String logErrorMsg = logToMatch.getErrorMessage();
        for(StackOverflowQuestion soq: SOFromDB) {
            if(FuzzySearch.tokenSortPartialRatio(logErrorMsg, soq.getTitle()) >= PERCENT_MATCH) {
                returnList.add(soq);
            }
        }
        if(returnList.isEmpty()) {
            log.info("No valid results found - Continue parsing original error message");
        }

        return returnList;
    }

    /**
     * Receives the path to a scraper file (Changed when database is created) and the current error
     * Calls the FuzzyMatching method to find the similar results
     * @param parameters Path to scraper file and current LogError
     * @return a list of StackOverflowQuestions that match the LogError given
     * @throws FileNotFoundException if the file cannot be opened
     */
    public static List<StackOverflowQuestion> matchLog(TempControllerParametersNoDB parameters) throws FileNotFoundException {
        StackOverflowScraperParser parser = new StackOverflowScraperParser();
//        Reader reader = new FileReader(parameters.getPathToscraper());
        mongoConnector db = new mongoConnector();
        List<Document> documents = db.getAllFrom(db.getCollection("testdb")); //.forEach((System.out::println));
        List<StackOverflowQuestion> questions = null;// = parser.parseQuestions(reader);

        return fuzzyMatching(questions, parameters.getCurrentError());
    }
}
