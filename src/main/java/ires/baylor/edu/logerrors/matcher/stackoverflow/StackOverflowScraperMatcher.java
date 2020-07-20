package ires.baylor.edu.logerrors.matcher.stackoverflow;

import ires.baylor.edu.logerrors.matcher.scraper.ScraperConnector;
import ires.baylor.edu.logerrors.matcher.scraper.ScraperObject;
import ires.baylor.edu.logerrors.matcher.strategy.MatcherAlgorithm;
import ires.baylor.edu.logerrors.matcher.strategy.ScoreTextMatching;
import ires.baylor.edu.logerrors.matcher.util.GoogleSearch;
import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;



/**
 * Finds multiple matches from the Stack Overflow scraped data to the Log Error given
 *
 * @author Elizabeth
 */
@Slf4j
public class StackOverflowScraperMatcher {
    /** Can use any of the strategy methods for the matcher*/
    private static MatcherAlgorithm matcher = new ScoreTextMatching();


    /**
     * Receives the path to a scraper file (Changed when database is created) and the current error
     * Calls the FuzzyMatching method to find the similar results
     *
     * @param currentError Current LogError
     * @return a list of StackOverflowQuestions that match the LogError given
     * @throws IOException if the file cannot be opened
     */
    public static List<ScraperObject> matchLog(LogError currentError) throws IOException, GeneralSecurityException {
        int found = 0;

        while (found < 2) {
            List<Document> documents = matcher.getAllDbDocuments("");
            List<ScraperObject> obj = matcher.convertDocuments(documents);

            List<ScraperObject> textMatch = matcher.match(obj, currentError);
            List<ScraperObject> bestList = removeDups(textMatch);

            if (bestList.isEmpty() && found < 1) {
                GoogleSearch.search(currentError.getErrorMessage()).forEach((url -> {
                    try {
                        ScraperConnector.scrapeAndAdd(url.get(1));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
                found++;
            } else {
                return bestList;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Removes the duplicates from the list given. This function is able to keep the list order
     * @param textMatch List of ScraperObjects with duplicates
     * @return List of ScraperObjects without duplicates
     */
    private static List<ScraperObject> removeDups(List<ScraperObject> textMatch) {
        return new ArrayList<>(new LinkedHashSet<>(textMatch));
    }
}
