package fr.kwanko.common;

import com.google.gson.Gson;

import junit.framework.Assert;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.when;

/**
 * SourceCode
 * Created by erusu on 4/27/2017.
 */

//@PrepareForTest(JSONObject.class)
@PrepareForTest(Josr.class)
@RunWith(PowerMockRunner.class)
public class JosrTest  {


    @Mock
    JSONObject mockJson;

    @Before
    public void init() throws Exception {
        MockitoAnnotations.initMocks(this);
        PowerMockito.whenNew(JSONObject.class).withAnyArguments().thenReturn(mockJson);
        ArrayList<String> keys = new ArrayList<>();
        keys.add("val1");
        keys.add("val2");
        keys.add("val3");
        keys.add("val4");
        keys.add("val5");
        keys.add("val6");
        keys.add("val7");
        when(mockJson.keys()).thenReturn(keys.iterator());
        for(int i=1;i<=7;i++){
            when(mockJson.isNull("val"+i)).thenReturn(false);
        }
        when(mockJson.isNull("val8")).thenReturn(true);
        when(mockJson.get("val1")).thenReturn(1);
        when(mockJson.get("val2")).thenReturn(2.1f);
        when(mockJson.get("val3")).thenReturn(3.1);
        when(mockJson.get("val4")).thenReturn(4);
        when(mockJson.get("val5")).thenReturn("5");
        when(mockJson.get("val6")).thenReturn('6');
        when(mockJson.get("val7")).thenReturn(true);

    }


    @Test
    public void testFromJson_forTestClass(){
        Josr josr = new Josr();
        TestClass test = getMockTestClass();
        String testAsJsonString = test.toJsonString();
        TestClass test2 = josr.fromJson(testAsJsonString,TestClass.class);
        assertEquals(test,test2);
    }

    private TestClass getMockTestClass(){
        TestClass test = new TestClass();
        test.val1 = 1;
        test.val2 = 2.1f;
        test.val3 = 3.1d;
        test.val4 = 4;
        test.val5 = "5";
        test.val6 = '6';
        test.val7 = true;
        test.val8 = null;
        return test;
    }

    public static class TestClass{
        public int val1;
        public float val2;
        public double val3;
        public long val4;
        public String val5;
        public char val6;
        public boolean val7;
        public String val8;

        public String toJsonString(){
            Gson gson = new Gson();
            return gson.toJson(this);
        }

        @Override
        public int hashCode() {
            return (int)(val1 + val2 + val3 + val5.hashCode() + val6 + (val7?1:0));
        }

        @Override
        public boolean equals(Object obj) {
            return obj != null && obj instanceof TestClass && ((TestClass) obj).val1 == val1 &&
                    Float.compare(((TestClass) obj).val2, val2) == 0 &&
                    Double.compare(((TestClass) obj).val3, val3) == 0 &&
                    ((TestClass) obj).val4 == val4 &&
                    ((TestClass) obj).val6 == val6 &&
                    ((TestClass) obj).val7 == val7;
        }
    }
}
