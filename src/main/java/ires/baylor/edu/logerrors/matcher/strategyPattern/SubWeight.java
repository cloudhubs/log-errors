package ires.baylor.edu.logerrors.matcher.strategyPattern;

import com.google.common.base.Splitter;
import ires.baylor.edu.logerrors.matcher.ScraperObject;
import ires.baylor.edu.logerrors.model.LogError;
import lombok.extern.slf4j.Slf4j;
import me.xdrop.fuzzywuzzy.FuzzySearch;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.text.similarity.LevenshteinDistance;


public class SubWeight implements MatcherAlgorithm {
    private static final String _whitespace = "\\s*";
    private static final String _wild = ".*";
    private static Double _max_regex_score;
    private final int _worst_score = 80;
    List<Double> weights;

    public SubWeight(List<Double> weights) {
        this.weights = weights;
        _max_regex_score = Collections.max(this.weights);
    }

    public Boolean isStructuralMatch(String code) {
        StringBuilder sb = new StringBuilder();

        boolean wild = false;

        for (int i = 0; i < this.weights.size(); i++) {
            if (this.weights.get(i) < _max_regex_score) {
                // it is not essential and therefore does not need to be apart of the regex, so we add any char

                if (!wild) {
                    sb.append(_wild);
                    wild = true;
                }

            } else {
                // it is essential, needs to be added to the regex
                String c = String.valueOf(code.charAt(i));

                if (c.equals("(") ||
                        c.equals(")") ||
                        c.equals("{") ||
                        c.equals("}") ||
                        c.equals(":") ||
                        c.equals(",") ||
                        c.equals("\\")) {

                    c = "\\" + c;
                }

                sb.append(_whitespace).append(c).append(_whitespace);
                wild = false;
            }
        }

        System.out.println(sb.toString());

        Pattern p = Pattern.compile(sb.toString());
        Matcher m = p.matcher(code);

        return m.find();
    }

    private double weightedMatch(String entity, String against) {
        double score = this._worst_score;
        // First we apply regex to the string to see if the format is the same
        if (!isStructuralMatch(against)) {
            return score;
        }

        // if the structure is correct then apply needleman-wunsch
        // this.weights is a list of integer values indicating the importance of each char. Lets assume they are
        // between 0, meaning not important and 1, meaning important
        // we need to tokenize the weights into essential and non-essential sections.
        LevenshteinDistance distance = new LevenshteinDistance();
        score = (double) distance.apply(entity, against);

        return score;
    }

    private double matchListAgainst(List<String> body, String against) {
        double max_matching_score = this._worst_score;
        for (String paragraph : body) {
            double new_score = weightedMatch(paragraph, against);
            max_matching_score = Math.max(max_matching_score, new_score);
        }

        return max_matching_score;
    }

    @Override
    public List<ScraperObject> match(List<ScraperObject> SOFromDB, LogError logToMatch) {
        List<ScraperObject> matched = new ArrayList<>();

        for (ScraperObject dbEntry : SOFromDB) {

            if (logToMatch.getErrorMessage().length() != this.weights.size()) {
                throw new IllegalArgumentException("The length of the weight and the length of the message(logError.getErrorMessage()) do not match");
            }

            //check entries against threshold
            if (PERCENT_MATCH <= matchListAgainst(dbEntry.getCode(), logToMatch.getErrorMessage())) {
                // add to list
                matched.add(dbEntry);
            }

            if (PERCENT_MATCH <= matchListAgainst(dbEntry.getText(), logToMatch.getErrorMessage())) {
                // add to lsit
                matched.add(dbEntry);

            }
        }

        return matched;
    }
}
