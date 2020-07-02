package ires.baylor.edu.logerrors.matcher;

import ires.baylor.edu.logerrors.matcher.strategyPattern.MatcherAlgorithm;
import ires.baylor.edu.logerrors.matcher.strategyPattern.ScoreTextMatching;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;


/**
 * Finds multiple matches from the Stack Overflow scraped data to the Log Error given
 *
 * @author Elizabeth
 */
@Slf4j
public class StackOverflowScraperMatcher {
    private static MatcherAlgorithm matcher = new ScoreTextMatching();
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
            List<ScraperObject> textMatch = matcher.match(obj, parameters.getCurrentError());
            //textMatch.addAll(fuzzyMatching(obj, parameters.getCurrentError().getErrorMessage()));

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
        List<ScraperObject> listWithoutDuplicates = new ArrayList<>(new LinkedHashSet<>(textMatch));
        return listWithoutDuplicates;
    }

    private static List<ScraperObject> convertDocument(List<Document> documents) {
        List<ScraperObject> scraper = new ArrayList<>();
        documents.forEach(d -> scraper.add(new ScraperObject(d)));
        return scraper;
    }
}
