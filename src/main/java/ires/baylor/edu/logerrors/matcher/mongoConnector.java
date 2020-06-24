package ires.baylor.edu.logerrors.matcher;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

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
}