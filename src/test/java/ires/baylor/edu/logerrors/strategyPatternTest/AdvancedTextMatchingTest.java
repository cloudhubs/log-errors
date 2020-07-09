package ires.baylor.edu.logerrors.strategyPatternTest;

import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.matcher.strategyPattern.AdvancedTextMatching;
import org.junit.jupiter.api.Test;

import java.util.List;

public class AdvancedTextMatchingTest implements MatcherAlgorithmTest {
    AdvancedTextMatching advanced = new AdvancedTextMatching();
    @Test
    public void basicTest() {
        List<ScraperObject> returnList = advanced.match(scraperObj, error1);
        assert returnList.size() == 1;
        for(ScraperObject obj: returnList) {
            assert obj.getTitle().equals("How does python unit testing work?");
        }
    }
    @Test
    public void BroadTest() {
        List<ScraperObject> returnList = advanced.match(scraperObj, broadError);
        assert returnList.size() == 3;
        for(ScraperObject obj: returnList) {
            assert obj.getTitle().matches(".*(P|p)ython.*");
        }
    }
    @Test
    public void InTextAndCodeTest() {
        List<ScraperObject> returnList = advanced.match(scraperObj2, broadError);
        assert returnList.size() == 3;
    }
}
