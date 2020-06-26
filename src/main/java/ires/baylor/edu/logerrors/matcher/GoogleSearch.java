package ires.baylor.edu.logerrors.matcher;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.customsearch.Customsearch;
import com.google.api.services.customsearch.CustomsearchRequestInitializer;
import com.google.api.services.customsearch.model.Result;
import com.google.api.services.customsearch.model.Search;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class GoogleSearch {

    public static ArrayList<ArrayList<String>> search(String error) throws GeneralSecurityException, IOException {
//    public static void main(String[] args) throws GeneralSecurityException, IOException {

        String searchQuery = "test"; //The query to search
        String cx = System.getenv().get("cx"); //Your search engine

        //Instance Customsearch
        Customsearch cs = new Customsearch.Builder(GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(), null)
                .setApplicationName("ScrapeStackOverflow")
                .setGoogleClientRequestInitializer(new CustomsearchRequestInitializer(System.getenv("g-key")))
                .build();

        //Set search parameter
        Customsearch.Cse.List list = cs.cse().list(searchQuery).setCx(cx);

        //Execute search
        Search gResults = list.execute();

        if (gResults.getItems() != null) {
            ArrayList<ArrayList<String>> results = new ArrayList<>();
            for (Result ri : gResults.getItems()) {
                ArrayList<String> result = new ArrayList<>();
                result.add(ri.getTitle());
                result.add(ri.getLink());
                results.add(result);
            }
//            results.forEach(resultList -> resultList.forEach(System.out::println));
            return results;
        }
        return new ArrayList<>();
    }
}
