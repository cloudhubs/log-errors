package ires.baylor.edu.logerrors.unusedCode;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Java Object representation of one entry in the response given by Andrew
 * Walker's Stack Overflow scraper (as modified by Mark Fuller).
 * <p>
 * <em>NOTE:</em>Not all fields from the response are stored. This is because
 * there will be MANY questions, each with many fields, and thus fields deemed
 * irrelevant are not recorded to conserve memory. If these are later determined
 * to contain important information, they will be added at that time.
 * 
 * @author Micah
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class StackOverflowQuestion {
	/** Tags of the question */
	String[] tags;

	/** Whether the question was answered */
	boolean isAnswered;

	/** How many views the question got */
	int viewCount;

	/** Community-assigned score of the question */
	int score;

	/** ID of the question */
	int questionId;

	/** Link to the Stack Overflow post */
	String link;

	/** Title of the question */
	String title;
}
