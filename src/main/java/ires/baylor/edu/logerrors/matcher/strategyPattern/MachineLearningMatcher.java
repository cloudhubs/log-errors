package ires.baylor.edu.logerrors.matcher.strategyPattern;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.model.LogError;
import lombok.Cleanup;
import lombok.var;
import lombok.extern.slf4j.Slf4j;

/**
 * Connects the error scraper to the ML matcher to find relevant solutions
 * 
 * @author Micah
 */
@Slf4j
public class MachineLearningMatcher implements MatcherAlgorithm {

	private static final String TMP_REGEX = "[0-9]{4}(?:-[0-9]{2}){2} (?:[0-9]{2}:){2}[0-9]{2},[0-9]*? ((?:WARNING)|(?:ERROR)) - (.*?\\.py:[0-9]*?) - (.*)";
	private static final Pattern MATCHER = Pattern.compile(TMP_REGEX);

	/** Json deserializer used by the class */
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	/** Address of the machine learning endpoint */
	private static final String PYTHON_ENDPOINT = "localhost:5000/matcher/machine-learning";

	@Override
	public List<ScraperObject> match(List<ScraperObject> SOFromDB, LogError logToMatch) {
		String original = getOriginalError(logToMatch);
		Map<String, ScraperObject> titles = SOFromDB.stream().collect(
				Collectors.<ScraperObject, String, ScraperObject>toMap(k -> ((ScraperObject) k).getTitle(), v -> v));
		try {
			return makeRequest(original, titles);
		} catch (IOException e) {
			log.error("Could not complete request", e);
		}
		return null;
	}

	/**
	 * Retrieve the raw error corresponding to the tokenized error
	 * 
	 * @param tokenized The LogError representing the encountered error
	 * @return The raw error, or an empty String if it could not be retrieved
	 */
	private String getOriginalError(LogError tokenized) {
		StringBuilder orig = new StringBuilder("");
		try (var lines = Files.lines(Paths.get(tokenized.getSource()))) {
			lines.skip(tokenized.getLineNumber() - 1);
			for (String s = lines.findFirst().get(); s != null
					&& !MATCHER.matcher(s).matches(); s = lines.findFirst().get())
				orig.append(s).append('\n');
		} catch (IOException ex) {
			log.error("Error encountered while retrieving raw exception", ex);
		}
		return orig.toString();
	}

	/**
	 * Forwards all pairs to the python backend to find matching solutions
	 * 
	 * @param error  The error message
	 * @param titles The titles of all Stack Overflow posts to consider
	 * @return List of matches the machine learning component found
	 * @throws IOException If there's an IO exception during the connection
	 */
	private List<ScraperObject> makeRequest(String error, Map<String, ScraperObject> objs) throws IOException {
		URL object = new URL(PYTHON_ENDPOINT);
		HttpURLConnection con = (HttpURLConnection) object.openConnection();

		// Set up connection. NOTE: copied from ScraperConnector.java; if that proves
		// buggy, fix me
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setRequestMethod("POST");

		// Create JSON
		JSONObject json = new JSONObject();
		json.put("error", error).put("titles", objs.keySet());

		// Submit
		@Cleanup
		OutputStreamWriter osw = new OutputStreamWriter(con.getOutputStream(), StandardCharsets.UTF_8);
		osw.write(json.toString());
		osw.flush();

		// Parse result
		if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
			try (BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
				String[] fromPython = GSON.fromJson(br, String[].class);
				List<ScraperObject> result = new ArrayList<>(fromPython.length);
				for (String title : fromPython)
					result.add(objs.get(title));
				return result;
			} catch (IOException ex) {
				log.error("Error encountered while retrieving raw exception", ex);
			}
		} else
			log.error(String.format("Unexpected response code %1$d with message \"%2$s\"", con.getResponseCode(), con.getResponseMessage()));
		return null;
	}
}
