package ires.baylor.edu.logerrors;

import ires.baylor.edu.logerrors.matcher.strategyPattern.SubWeight;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class LogErrorsApplicationTests {

    @Test
    void testMatch(){

    }

    @Test
    void testIsStructuralMatch() {
        ArrayList<Double> weights = new ArrayList<Double>(getList());

        String code = "public static void main (String []args) {";

        assert code.length() == weights.size();

        SubWeight sw = new SubWeight(weights);

        Boolean structural = sw.isStructuralMatch(code);

        assert structural;
    }

    private List<Double> getList(){
        ArrayList<Double> weights = new ArrayList<>();
        // test for "public static void main(String [] args) {"
        weights.add(1.0); // public
        weights.add(1.0);
        weights.add(1.0);
        weights.add(1.0);
        weights.add(1.0);
        weights.add(1.0);

        weights.add(0.5); //

        weights.add(1.0); // static
        weights.add(1.0);
        weights.add(1.0);
        weights.add(1.0);
        weights.add(1.0);
        weights.add(1.0);

        weights.add(0.5); //

        weights.add(1.0); // void
        weights.add(1.0);
        weights.add(1.0);
        weights.add(1.0);

        weights.add(0.5); //

        weights.add(1.0); // main
        weights.add(1.0);
        weights.add(1.0);
        weights.add(1.0);

        weights.add(0.5); //

        weights.add(1.0); // (

        weights.add(0.5); // String
        weights.add(0.5);
        weights.add(0.5);
        weights.add(0.5);
        weights.add(0.5);
        weights.add(0.5);

        weights.add(0.5); // " "

        weights.add(0.5); // []
        weights.add(0.5);

        weights.add(0.5); // args
        weights.add(0.5);
        weights.add(0.5);
        weights.add(0.5);

        weights.add(1.0); // (

        weights.add(0.5); // " "

        weights.add(1.0); //  {

        return weights;
    }

}

