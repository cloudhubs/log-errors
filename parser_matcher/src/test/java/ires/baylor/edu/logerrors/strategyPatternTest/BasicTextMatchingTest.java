package ires.baylor.edu.logerrors.strategyPatternTest;

import ires.baylor.edu.logerrors.matcher.scraper.ScraperObject;
import ires.baylor.edu.logerrors.matcher.strategy.BasicTextMatching;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class BasicTextMatchingTest extends MatcherAlgorithmTest {
    @BeforeAll
    public static void init() {
        matcher = new BasicTextMatching();
    }

    @Test
    public void basicTest() {
        List<ScraperObject> returnList = matcher.match(scraperObj, error1);
        assert returnList.size() == 1;
        for(ScraperObject obj: returnList) {
            assert obj.getTitle().equals("How does python unit testing work?");
        }
    }
    @Test
    public void BroadTest() {
        List<ScraperObject> returnList = matcher.match(scraperObj, broadError);
        assert returnList.size() == 3;
        for(ScraperObject obj: returnList) {
            assert obj.getTitle().matches(".*(P|p)ython.*");
        }
    }
    @Test
    public void InTextAndCodeTest() {
        List<ScraperObject> returnList = matcher.match(scraperObj2, broadError);
        assert returnList.size() == 3;
    }

}
