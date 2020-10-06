package ires.baylor.edu.logerrors.matcher.util;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class mongoConnector {
    MongoDatabase database;

    public mongoConnector() {
        // Creating a Mongo client
        MongoClient mongo = new MongoClient("localhost", 27017);

        // Creating Credentials
        MongoCredential credential = MongoCredential.createCredential("myAdminUser", "testdb",
                "abc123".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("testdb");
        System.out.println("Credentials :: " + credential);

        this.database = database;
    }

    public MongoCollection<Document> getCollection(String collection) {
        return this.database.getCollection(collection);
    }

    public List<Document> getAllFrom(MongoCollection<Document> collection) {
        List<Document> documents = (List<Document>) collection.find().into(new ArrayList<Document>());

        return documents;
    }

    public List<Document> findWithRegex(MongoCollection<Document> collection, String regex) {
        Document codeQuery = new Document();
        codeQuery.append("code", Pattern.compile(regex));
        FindIterable<Document> codeIterable = collection.find(codeQuery);

        Document textQuery = new Document();
        textQuery.append("text", Pattern.compile(regex));
        FindIterable<Document> textIterable = collection.find(textQuery);

        List<Document> code = StreamSupport.stream(codeIterable.spliterator(), false).collect(Collectors.toList());
        List<Document> text = StreamSupport.stream(textIterable.spliterator(), false).collect(Collectors.toList());

        code.addAll(text);

        return code;
    }

    public boolean insertDocument(String collection_name, Document document) {
        try {

            getCollection(collection_name).insertOne(document);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public Document find(MongoCollection<Document> collection, String field, String value) {
        Document codeQuery = new Document();
        codeQuery.append(field, value);
        FindIterable<Document> codeIterable = collection.find(codeQuery);

        List<Document> code = StreamSupport.stream(codeIterable.spliterator(), false).collect(Collectors.toList());

        if (code.size() == 1) {
            return code.get(0);
        } else if (code.size() == 0) {
            return null;
        }

        throw new IndexOutOfBoundsException("There are duplicate values stored: " + value);
    }
}
