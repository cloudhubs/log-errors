package ires.baylor.edu.logerrors.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.tools.sjavac.Log;

import ires.baylor.edu.logerrors.matcher.MatcherControllerParameters;
import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.matcher.StackOverflowScraperMatcher;
import ires.baylor.edu.logerrors.matcher.mongoConnector;
import ires.baylor.edu.logerrors.matcher.strategyPattern.MachineLearningMatcher;
import ires.baylor.edu.logerrors.model.LogError;
import ires.baylor.edu.logerrors.model.ResolveErrorsRequest;
import ires.baylor.edu.logerrors.parser.LogErrorParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MachineLearningTrainerController {

	/** Handles all JSON conversions */
	private static final Gson writer = new GsonBuilder().setPrettyPrinting().create();

	/** DB for the controller */
	private final mongoConnector db = new mongoConnector();

	/**
	 * Encapsulates needed information from the scraper
	 * 
	 * @author Micah
	 */
	private static class TestData {
		String url;
		String title;
		String trace;

		public TestData(String url, String title, String trace) {
			this.url = url;
			this.title = normalize(title);
			this.trace = normalize(trace);
		}

		private String normalize(String s) {
			return s.replaceAll("[^a-zA-Z0-9]", " ").replaceAll("[ ]+", " ").toLowerCase();
		}
	}

	/**
	 * Endpoint for converting the contents of the mongo DB into valid training data
	 * 
	 * @return A JSON list of valid training entries
	 */
	@GetMapping("/machine-learning/data")
	public ResponseEntity<?> getData() {
		return new ResponseEntity<>(parseFromDB(), HttpStatus.ACCEPTED);
	}

	/** Temporary, format from ad-hoc dipta scraper */
	class Temp {
		public String link, snippet;
	}

	/**
	 * Testing method to allow the translator to be run from the command line
	 * 
	 * @param args CL args
	 */
	public static void main(String[] args) {
//		try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("./ml_good_data.json"))) {
//			bw.write(new MachineLearningTrainerController().parseFromDB());
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
		Gson gson = new GsonBuilder().create();
		List<TestData> result = new ArrayList<>();
		final Pattern start = Pattern.compile("Traceback");
		final Pattern end = Pattern.compile(".*?Error:.*");
		
		Temp[] tmps;
		for (int i = 66; i < 88; i++) {
			System.out.println(i);
			try (BufferedReader br = Files.newBufferedReader(Paths.get("./machine_learning/data/good_snippets" + i + ".json"))) {
				tmps = gson.fromJson(br, Temp[].class);
				for (Temp tmp : tmps) {
					String[] title = tmp.link.split("/");
					
					// Get trace
					Matcher st = start.matcher(tmp.snippet),
							ed = end.matcher(tmp.snippet);
					st.find();
					String trace;
					if (ed.find(st.start())) {
						int ndx;
						do {
							ndx = ed.end();
						} while (ed.find(ndx));
						trace = tmp.snippet.substring(st.start(), ndx);
					} else
						trace = tmp.snippet.substring(st.start());
					result.add(new TestData(tmp.link, title[title.length - 1], trace));
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("./train_attempt_2c.json"))) {
			bw.write(gson.toJson(result));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Goes through the database and transforms all entries into the format
	 * requested by the Python side of the ML matcher.
	 */
	private String parseFromDB() {
		return getJsonForML(getGoodQuestions());
	}

	private String getJsonForML(List<ScraperObject> objs) {
		List<TestData> td = objs.stream()
				.map(obj -> new TestData(obj.getUrl(), obj.getTitle(), getStackTrace(obj.getCode())))
				.collect(Collectors.toList());
		return writer.toJson(td);
	}

	/**
	 * Filters to good questions
	 */
	private List<ScraperObject> getGoodQuestions() {
		return db.getAllFrom(db.getCollection("coll_name")).stream().map(d -> new ScraperObject(d))
				.filter(e -> e.getText().size() > 1 && containsStackTrace(e)).collect(Collectors.toList());
	}

	private static final Pattern TRACE = Pattern.compile("Traceback");

	/**
	 * Stupid simple check for a stack trace
	 * 
	 * @param obj The object to find a stack trace in
	 * @return Whether or not it has a stack trace
	 */
	private boolean containsStackTrace(ScraperObject obj) {
		return containsStackTrace(obj.getText().get(0));
	}

	/**
	 * Stupid simple check for a stack trace
	 * 
	 * @param s The text to check for a stack trace
	 * @return Whether or not it has a stack trace
	 */
	private boolean containsStackTrace(String s) {
		return TRACE.matcher(s).find();
	}

	/**
	 * Find the stack trace from the code array. Returns the first trace found.
	 * 
	 * @param code The code snippets from the Stack Overflow discussion
	 * @return The stack trace from the main question.
	 */
	private String getStackTrace(List<String> code) {
		for (String snippet : code)
			if (containsStackTrace(snippet)) {
				return "Traceback" + snippet.split("Traceback")[1];
			}
		return null;
	}

	/**
	 * Takes in a file that has a cached array of LogErrors (in JSON format), and
	 * runs the naive matcher to get results for each.
	 * 
	 * @param filenameOfErrors Name of file that stores the errors
	 */
	private void populateDB(String filenameOfErrors) {
		try (BufferedReader br = Files.newBufferedReader(Paths.get(filenameOfErrors))) {
			LogError[] errs = new Gson().fromJson(br, LogError[].class);

			// Create parameters
			MatcherControllerParameters mcp = new MatcherControllerParameters();
			mcp.setPathToScraper("");
			mcp.setVariance(0.8);

			// Fill database
			int i = 0;
			for (LogError err : errs) {
				try {
					log.info("Run " + (++i) + "/" + errs.length);
					mcp.setCurrentError(err);
					StackOverflowScraperMatcher.matchLog(mcp);
					Thread.sleep(3000);
				} catch (IOException | InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
