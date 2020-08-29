package ires.baylor.edu.logerrors.strategyPatternTest;


import ires.baylor.edu.logerrors.matcher.scraper.ScraperObject;
import ires.baylor.edu.logerrors.matcher.strategy.MatcherAlgorithm;
import ires.baylor.edu.logerrors.model.LogError;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public abstract class MatcherAlgorithmTest {
    public static MatcherAlgorithm matcher;
    String fillerString = "We need a really long filler string here - putting it before and after the acutal text \n" +
            "This is because Fuzzy matching breaks down after a little while, especially with a lot of test\t" +
            "the plan is to create an applicable matching environment";
    //String str3 = "Error Message: AttributeError: 'NoneType' object has no attribute 'split'";
    String str3 = "no";
    String str1 = "Message Error: AttributeError: 'NoneType' object has no attribute 'split'";
    String str2 = "Error Message: AttributeError: 'System' object has no attribute 'split'";
    String str4 = "Error Message: AttributeError: 'NoneType' has no attribute 'split'";
    String str5 = "";

    List<ScraperObject> scraperObj = new ArrayList<>() {{
        add(new ScraperObject("How does python unit testing work?",new ArrayList<>(){{add("Text test to see what matches");}}, new ArrayList<>(){{add("");}}));
        add(new ScraperObject("Python flask tutorial",new ArrayList<>(){{add("Text test to see what matches");}}, new ArrayList<>(){{add("");}}));
        add(new ScraperObject("Python testing error: file not found",new ArrayList<>(){{add("Text test to see what matches");}}, new ArrayList<>(){{add("");}}));
    }};
    List<ScraperObject> scraperObj2 = new ArrayList<>() {{
        add(new ScraperObject("Nothing Useful except text1",new ArrayList<>(){{add("Python keywords: Python unit testing: How does it work?");}}, new ArrayList<>(){{add("");}}));
        add(new ScraperObject("Nothing Useful except text2",new ArrayList<>(){{add("Python keywords: Python unit testing it work?");}}, new ArrayList<>(){{add("");}}));
        add(new ScraperObject("Nothing Useful except text3",new ArrayList<>(){{add("nothing");}}, new ArrayList<>(){{add("python unit testing: How does it work? WE NEED TO KNOW");}}));
    }};
    List<ScraperObject> scraperNull = new ArrayList<>();
    List<ScraperObject> scraperTitleNull = new ArrayList<>() {{
        add(new ScraperObject(new ArrayList<>(){{add("nothing");}}, new ArrayList<>(){{add("python unit testing: How does it work? WE NEED TO KNOW");}}));

    }};
    List<ScraperObject> scraperTextNull = new ArrayList<>() {{
        add(new ScraperObject("Nothing Useful except text3", new ArrayList<>(){{add("python unit testing: How does it work? WE NEED TO KNOW");}}));

    }};
    LogError error1 = new LogError("Python unit testing: How does it work?");
    LogError broadError = new LogError("Python");
    LogError error2 = new LogError("Python testing");
    LogError errorSyntax = new LogError("\"Python unit testing:\"\" How does it work?\"");
    LogError errorNull = new LogError();

    LogError RealError = new LogError("Error Message: AttributeError: 'NoneType' object has no attribute 'split'", "Not Important");

    List<ScraperObject> Results = new ArrayList<>() {{
        add(new ScraperObject("1", new ArrayList<>(){{add(fillerString + str1 + fillerString);add(fillerString);}}));
        add(new ScraperObject("2", new ArrayList<>(){{add(fillerString + str2 + fillerString);add(fillerString);}}));
        add(new ScraperObject("3", new ArrayList<>(){{add(fillerString + str3 + fillerString);add(fillerString);}}));
        add(new ScraperObject("4", new ArrayList<>(){{add(fillerString + str4 + fillerString);add(fillerString);}}));
        add(new ScraperObject("5", new ArrayList<>(){{add(fillerString + str5 + fillerString);add(fillerString);}}));
    }};
    List<ScraperObject> TitleResults = new ArrayList<>() {{
        add(new ScraperObject(str1));
        add(new ScraperObject(str2));
        add(new ScraperObject(str3));
        add(new ScraperObject(str4));
        add(new ScraperObject(str5));
    }};
    @Test
    void nullTest1() {
        try {
            matcher.match(null, error1);
        } catch(Exception e) {
            assert false;
        }
        assert true;
    }
    @Test
    void nullTest2() {
        try {
            matcher.match(scraperObj, null);
        } catch(Exception e) {
            assert false;
        }
        assert true;
    }
    @Test
    void advancedNullTest1() {
        try {
            matcher.match(scraperNull, error1);
        } catch(Exception e) {
            assert false;
        }
        assert true;
    }

    @Test
    void advancedNullTest2() {
        try {
            matcher.match(scraperObj, errorNull);
        } catch(Exception e) {
            assert false;
        }
        assert true;
    }
    @Test
    void scraperTitleNullTest() {
        try {
            matcher.match(scraperTitleNull, error1);
        } catch(Exception e) {
            assert false;
        }
        assert true;
    }
    @Test
    void scraperTextNullTest() {
        try {
            matcher.match(scraperTextNull, error1);
        } catch(Exception e) {
            assert false;
        }
        assert true;
    }

}
