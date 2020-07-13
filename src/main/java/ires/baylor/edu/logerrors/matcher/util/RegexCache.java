package ires.baylor.edu.logerrors.matcher.util;

import ires.baylor.edu.logerrors.matcher.scraper.ScraperObject;
import org.bson.Document;

import java.util.List;

public class RegexCache {
    public static String check(String regex) {
        mongoConnector db = new mongoConnector();
        try {
            Document id = db.find(db.getCollection("regex_cache"), "regex", regex);
            if (id != null) {
                return (String) id.get("id");
            }
        } catch (Exception e) {
            return "";
        }
        return "";
    }

    public static Document get(String id) {
        mongoConnector db = new mongoConnector();
        try {
            Document doc = db.find(db.getCollection("coll_name"), "id", id);
            if (doc != null) {
                return doc;
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    public static void add(String regex, List<ScraperObject> matches) {
        mongoConnector db = new mongoConnector();
        Document doc = new Document();
        doc.append("regex", regex);
        doc.append("so_post", matches);
        db.insertDocument("regex_cache", doc);
    }
}
