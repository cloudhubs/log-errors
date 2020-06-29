package ires.baylor.edu.logerrors.matcher;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class ScraperConnector {

    public static void main(String [] args) throws IOException {
        System.out.println(ScraperConnector.scrapeAndAdd("http://stackoverflow.com/questions/21404252/post-request-send-json-data-java-httpurlconnection"));
    }

    private final static String _mongo_url = "http://localhost:5000/mongo/test/url";

    public static String scrapeAndAdd(String new_url) throws IOException {
            return ScraperConnector.executePOST(new_url);
    }

    private static String executePOST(String new_url) throws IOException {
        // Establish connection
        URL object = new URL(_mongo_url);

        HttpURLConnection con = (HttpURLConnection) object.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Accept", "application/json");
        con.setRequestMethod("POST");

        //Build JSON
        JSONObject parent = new JSONObject();
        parent.put("url", new_url);

        OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
        wr.write(parent.toString());
        wr.flush();

        //display what returns the POST request
        StringBuilder sb = new StringBuilder();
        int HttpResult = con.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            return new JSONObject(sb.toString()).getString("id");
        } else {
            return con.getResponseMessage();
        }
    }
}
