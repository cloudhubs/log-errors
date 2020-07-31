package ires.baylor.edu.logerrors.strategyPatternTest;


import ires.baylor.edu.logerrors.matcher.scraper.ScraperObject;
import ires.baylor.edu.logerrors.matcher.strategy.SubWeight;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class WeightTextMatchingTest extends MatcherAlgorithmTest {
    static List<Double> weights = new ArrayList<>() {
        {
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(1.0);
            add(0.0);
            add(0.0);  add(1.0); add(1.0); add(1.0); add(1.0); add(1.0); add(1.0); add(1.0); add(1.0); add(0.0);
            add(0.0);
            add(1.0); add(1.0); add(1.0); add(1.0); add(1.0); add(1.0);
            add(0.0);
            add(1.0); add(1.0); add(1.0);  add(0.0); add(1.0); add(1.0);  add(0.0);
            add(1.0); add(1.0); add(1.0); add(1.0); add(1.0); add(1.0); add(1.0); add(1.0); add(1.0);
            add(0.0); add(0.0);  add(1.0); add(1.0); add(1.0); add(1.0); add(1.0); add(0.0);
        };
    };

//AttributeError: 'NoneType' object has no attribute 'split'

    @BeforeAll
    public static void init() {
        matcher = new SubWeight(weights);
    }

    @Test
    public void realTest() {
        List<ScraperObject> returnList = matcher.match(Results, RealError);
        System.out.println("Weight Alg");
        for(ScraperObject obj: returnList) {
            System.out.println(obj.getTitle());
        }
        assert true;
    }
}
