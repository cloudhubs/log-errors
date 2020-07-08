package ires.baylor.edu.logerrors.matcher.strategyPattern;

import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.matcher.mongoConnector;
import ires.baylor.edu.logerrors.model.LogError;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import org.bson.Document;


public abstract class MatcherAlgorithm {
    public static final int PERCENT_MATCH = 85;

    public abstract List<ScraperObject> match(List<ScraperObject> SOFromDB, LogError logToMatch);

    public List<Document> getAllDbDocuments(String qualifier){
        // no qualifier used in the abstract class implementation

        mongoConnector db = new mongoConnector();
        return db.getAllFrom(db.getCollection("coll_name"));
    }

    public List<ScraperObject> convertDocuments(List<Document> documents){
        List<ScraperObject> scraper = new ArrayList<>();
        documents.forEach(d -> scraper.add(new ScraperObject(d)));
        return scraper;
    }
}
