package ires.baylor.edu.logerrors.matcher.strategy;

import ires.baylor.edu.logerrors.matcher.scraper.ScraperObject;
import ires.baylor.edu.logerrors.matcher.util.mongoConnector;
import ires.baylor.edu.logerrors.model.LogError;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Parent class for all Algorithms. This class is able to get documents from the database
 * and convert the documents into a List of ScraperObjects. The shared function of all the
 * algorithms is the match() function
 */
public abstract class MatcherAlgorithm {
    public static final int PERCENT_MATCH = 85;

    public abstract List<ScraperObject> match(List<ScraperObject> SOFromDB, LogError logToMatch);

    public List<Document> getAllDbDocuments(String qualifier) {
        mongoConnector db = new mongoConnector();
        return db.getAllFrom(db.getCollection("coll_name"));
    }

    public List<ScraperObject> convertDocuments(List<Document> documents) {
        List<ScraperObject> scraper = new ArrayList<>();
        documents.forEach(d -> scraper.add(new ScraperObject(d)));
        return scraper;
    }
}
