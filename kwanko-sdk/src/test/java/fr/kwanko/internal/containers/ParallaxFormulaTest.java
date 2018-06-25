package fr.kwanko.internal.containers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;

/**
 * SourceCode
 * Created by erusu on 5/8/2017.
 */

@RunWith(JUnit4.class)
public class ParallaxFormulaTest {

    private static final int Y_BIG = 5125;
    private static final int DELTA_H = 1584;
    private static final int DElTA_SMALL_H = 1920;
    private static final int I_BIG = 525;
    private ParallaxFormula formula;

    @Before
    public void init(){
        formula = new ParallaxFormula();
        formula.initConstants(DElTA_SMALL_H,DELTA_H,Y_BIG,I_BIG);
    }

    @Test
    public void printFormulaResults(){
        for(int i = (Y_BIG + I_BIG);i>=(Y_BIG - DELTA_H );i--){
            System.out.println("f("+i+")"+" = "+formula.compute(i));
        }
    }

    @Test
    public void testCompute_isTrueFirstSystemCondition() {
        assertEquals((int)formula.compute(Y_BIG - DELTA_H),(I_BIG + DElTA_SMALL_H));
    }

    private float ih(){
        return (I_BIG * DElTA_SMALL_H)/(float)DELTA_H;
    }

    @Test
    public void testCompute_isTrueForSecondSystemCondition(){
        assertEquals((int)formula.compute(Y_BIG + I_BIG),0);
    }
}
