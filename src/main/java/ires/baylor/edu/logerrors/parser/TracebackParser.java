package ires.baylor.edu.logerrors.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;

/**
 * Temporary class to parse tracebacks. Will likely have its logic and methods
 * extracted to another class.
 * 
 * @author Micah
 */
@AllArgsConstructor
public class TracebackParser {

	/** Pattern heralds an entry in the trace */
	private Pattern traceSignature;

	/** Pattern to use to extract the trace for the results */
	private Pattern traceEntry;

	/**
	 * Temporary no-arg constructor which matches the POC file
	 */
	public TracebackParser() {
		this(Pattern.compile("File \"([^\"]*)\", line ([0-9]*),.*"),
				Pattern.compile("File.*[\\n\\r]{1,2}.*", Pattern.MULTILINE));
	}

	/**
	 * Interprets the data in the scanner to read and record a traceback
	 * 
	 * @param errLog The source of the log, stopping right after the initial error
	 *               line has been read
	 * 
	 * @return A list containing the lines representing the traceback in the
	 *         {@link Scanner}
	 */
	public List<String> addTraceback(Scanner errLog) {
		List<String> traceback = new ArrayList<>();
		String line;
		Matcher match;

		// Get all trace entries
		while (errLog.hasNext(traceSignature)) {
			line = errLog.nextLine();

			// Use pattern to extract data from the line
			match = traceSignature.matcher(line);
			match.matches();
			line = errLog.next(traceEntry);
			traceback.add(line);
		}

		return traceback;
	}
}
