package ires.baylor.edu.logerrors.strategyPatternTest;

import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.matcher.strategyPattern.FuzzyTitleMatching;
import org.junit.jupiter.api.Test;

import java.util.List;

public class FuzzyTitleMatchingTest implements MatcherAlgorithmTest {
    FuzzyTitleMatching fuzzy = new FuzzyTitleMatching();

    @Test
    public void basicTest() {
        List<ScraperObject> returnList = fuzzy.match(scraperObj, error1);
        assert returnList.size() == 1;
        for(ScraperObject obj: returnList) {
            assert obj.getTitle().equals("How does python unit testing work?");
        }
    }
    @Test
    public void BroadTest() {
        List<ScraperObject> returnList = fuzzy.match(scraperObj, broadError);
        assert returnList.size() == 3;
        for(ScraperObject obj: returnList) {
            assert obj.getTitle().matches(".*(P|p)ython.*");
        }
    }


}
