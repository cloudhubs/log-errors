package ires.baylor.edu.logerrors.strategyPatternTest;

import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.matcher.strategyPattern.MatcherAlgorithm;
import ires.baylor.edu.logerrors.model.LogError;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public abstract class MatcherAlgorithmTest {
    public static MatcherAlgorithm matcher;


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
