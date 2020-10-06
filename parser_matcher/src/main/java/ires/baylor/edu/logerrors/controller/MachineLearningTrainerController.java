package ires.baylor.edu.logerrors.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ires.baylor.edu.logerrors.matcher.scraper.ScraperObject;
import ires.baylor.edu.logerrors.matcher.stackoverflow.StackOverflowScraperMatcher;
import ires.baylor.edu.logerrors.matcher.util.mongoConnector;
import ires.baylor.edu.logerrors.model.LogError;
import ires.baylor.edu.logerrors.unusedCode.MatcherControllerParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class MachineLearningTrainerController {

    /**
     * Handles all JSON conversions
     */
    private static final Gson writer = new GsonBuilder().setPrettyPrinting().create();
    private static final String TRACE_RAW = "Traceback";
    private static final Pattern TRACE = Pattern.compile(TRACE_RAW);
    /**
     * DB for the controller
     */
    private final mongoConnector db = new mongoConnector();

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
        Set<String> record = new HashSet<>();
        List<TestData> result = new ArrayList<>();
        final Pattern start = Pattern.compile(TRACE_RAW);
        final Pattern end = Pattern.compile(".*?Error:.*");

        for (int segment = 0; segment < 4; segment++) {
            Temp[] tmps;
            for (int i = segment * 22; i < (segment + 1) * 22; i++) {
                System.out.println(i);
                try (BufferedReader br = Files
                        .newBufferedReader(Paths.get("./machine_learning/data/good_snippets" + i + ".json"))) {
                    tmps = gson.fromJson(br, Temp[].class);
                    for (Temp tmp : tmps) {
                        // Filter already-discovered entries
                        String[] title = tmp.link.split("/");
                        String trueTitle = title[title.length - 1];
                        if (record.contains(trueTitle))
                            continue;
                        else
                            record.add(trueTitle);

                        // Find stack trace, if it exists
                        try {
                            // Find start of trace and ensure there are no other traces in this snippet
                            Matcher st = start.matcher(tmp.snippet), ed = end.matcher(tmp.snippet);
                            st.find();
                            int startIndex = st.start();
                            if (st.find()) {
//								System.err.println("Bad entry, continue");
                                continue;
                            }

                            // Find end of trace
                            String trace;
                            if (ed.find(startIndex)) {
                                int ndx;
                                do {
                                    ndx = ed.end();
                                } while (ed.find(ndx));
                                trace = tmp.snippet.substring(startIndex, ndx);
                            } else
                                trace = tmp.snippet.substring(startIndex);
                            result.add(new TestData(tmp.link, title[title.length - 1], trace));
                        } catch (IllegalStateException ex) {
                            System.err.println(String.format("No trace for \"%1$s\"", tmp.snippet));
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            try (BufferedWriter bw = Files.newBufferedWriter(Paths.get("./train_attempt_3_" + segment + ".json"))) {
                bw.write(gson.toJson(result));
            } catch (IOException e) {
                e.printStackTrace();
            }
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
                return TRACE_RAW + snippet.split(TRACE_RAW, 2)[1];
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
//					StackOverflowScraperMatcher.matchLog(mcp);
                    StackOverflowScraperMatcher.matchLog(err);
                    Thread.sleep(3000);
                } catch (IOException | InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
     * Temporary, format from ad-hoc dipta scraper
     */
    class Temp {
        public String link, snippet;
    }
}
