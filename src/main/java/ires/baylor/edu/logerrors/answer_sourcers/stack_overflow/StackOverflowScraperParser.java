package ires.baylor.edu.logerrors.answer_sourcers.stack_overflow;

import java.io.Reader;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import lombok.extern.slf4j.Slf4j;

/**
 * Class interprets the JSON response from Andrew Walker's Stack Overflow
 * scraper (as modified by Mark Fuller) into a List of Java Objects.
 * 
 * @author Micah
 */
@Slf4j
public class StackOverflowScraperParser {

	/** Gson instance used to handle the JSON conversion */
	protected static final Gson GSON = new GsonBuilder()
			.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

	/**
	 * Parses the JSON response stored in the given string into a usable object
	 * format.
	 * 
	 * @param inData The JSON statement to parse
	 * @return A list of {@link StackOverflowQuestion StackOverflowQuestions} that
	 *         represents the data in the provided string, or null if an error
	 *         occurred.
	 */
	public List<StackOverflowQuestion> parseQuestions(String data) {
		List<StackOverflowQuestion> qs = null;
		try {
			qs = List.of(GSON.fromJson(data, StackOverflowList.class).items);
		} catch (JsonSyntaxException e) {
			log.error("Error occurred while converting JSON to objects", e);
		}
		return qs;
	}

	/**
	 * Parses the JSON response stored in the given reader into a usable object
	 * format.
	 * 
	 * @param inData The reader containing the JSON statement to parse
	 * @return A list of {@link StackOverflowQuestion StackOverflowQuestions} that
	 *         represents the data in the provided reader, or null if an error
	 *         occurred.
	 */
	public List<StackOverflowQuestion> parseQuestions(Reader data) {
		List<StackOverflowQuestion> qs = null;
		try {
			qs = List.of(GSON.fromJson(data, StackOverflowList.class).items);
		} catch (JsonSyntaxException e) {
			log.error("Error occurred while converting JSON to objects", e);
		} catch (JsonIOException e) {
			log.error("Exception occurred while reading JSON", e);
		}
		return qs;
	}

	/**
	 * Wrapper class for Gson to convert the provided JSON into objects.
	 * 
	 * @author Micah
	 */
	private class StackOverflowList {
		/** The questions included in the provided JSON string */
		StackOverflowQuestion[] items;
	}
}
