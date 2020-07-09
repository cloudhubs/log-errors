package ires.baylor.edu.logerrors.strategyPatternTest;

import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.model.LogError;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public interface MatcherAlgorithmTest {
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

    LogError error1 = new LogError("Python unit testing: How does it work?");
    LogError broadError = new LogError("Python");
    LogError error2 = new LogError("Python testing");

}
