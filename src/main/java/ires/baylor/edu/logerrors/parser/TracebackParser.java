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

	/** Pattern of an entry in the trace */
	private Pattern traceSignature;

	/**
	 * Temporary no-arg constructor which matches the POC file
	 */
	public TracebackParser() {
		this(Pattern.compile("File \"([^\"]*)\", line ([0-9]*),.*"));
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
		StringBuilder concatenator = new StringBuilder();
		String line;

		// Get all trace entries
		while (errLog.hasNextLine()) {
			line = errLog.nextLine();

			// Check if the next line was a traceback entry; if not, finish
			if (traceSignature.matcher(line).matches()) {
				// Assemble the statement
				if (errLog.hasNextLine()) {
					concatenator.append(line).append('\n').append(errLog.nextLine());
					line = concatenator.toString();
					concatenator.setLength(0);
				}
				traceback.add(line);
			} else
				break;
		}

		return traceback;
	}
}
