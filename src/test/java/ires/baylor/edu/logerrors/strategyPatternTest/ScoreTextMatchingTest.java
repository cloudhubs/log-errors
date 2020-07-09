package ires.baylor.edu.logerrors.strategyPatternTest;

import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.matcher.strategyPattern.ScoreTextMatching;
import org.junit.jupiter.api.Test;

import java.util.List;

public class ScoreTextMatchingTest implements MatcherAlgorithmTest {
    ScoreTextMatching score = new ScoreTextMatching();
    @Test
    public void basicTest() {
        List<ScraperObject> returnList = score.match(scraperObj, error1);
        for(ScraperObject obj: returnList) {
            System.out.println(obj.getTitle());
        }
        assert returnList.size() == 2;

        assert returnList.get(0).getTitle().equals("How does python unit testing work?");
        assert returnList.get(1).getTitle().equals("Python testing error: file not found");
    }
    @Test
    public void BroadTest() {
        List<ScraperObject> returnList = score.match(scraperObj, error2);
        assert returnList.size() == 3;

        assert returnList.get(0).getTitle().equals("Python testing error: file not found");
        assert returnList.get(1).getTitle().equals("How does python unit testing work?");
        assert returnList.get(2).getTitle().equals("Python flask tutorial");

    }
    @Test
    public void InTextAndCodeTest() {
        List<ScraperObject> returnList = score.match(scraperObj2, broadError);
        assert returnList.size() == 3;
    }


}
