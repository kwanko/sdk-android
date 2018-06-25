package fr.kwanko.internal.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static junit.framework.Assert.assertEquals;

/**
 * SourceCode
 * Created by erusu on 6/19/2017.
 */

@RunWith(JUnit4.class)
public class CloseButtonMetadataTest {

    @Test
    public void testGetSize_sizeContainsX(){
        CloseButtonMetadata closeButtonMetadata = new CloseButtonMetadata("noImage","15x15","5px");
        assertEquals(closeButtonMetadata.getWidthDp(),15);
        assertEquals(closeButtonMetadata.getHeightDp(),15);
        assertEquals(closeButtonMetadata.getImageSrc(),"noImage");
    }

    @Test
    public void testGetSize_sizeDoesNotContainsX(){
        CloseButtonMetadata closeButtonMetadata = new CloseButtonMetadata("noImage","15:15","5px");
        assertEquals(closeButtonMetadata.getWidthDp(),40);//default
        assertEquals(closeButtonMetadata.getHeightDp(),40);
    }

    @Test
    public void testGetSize_sizeStringNull(){
        CloseButtonMetadata closeButtonMetadata = new CloseButtonMetadata("noImage",null,"5px");
        assertEquals(closeButtonMetadata.getWidthDp(),40);//default
        assertEquals(closeButtonMetadata.getHeightDp(),40);
    }

    @Test
    public void testGetSize_junkString(){
        CloseButtonMetadata closeButtonMetadata = new CloseButtonMetadata("noImage","junsgndgxrgrgerg","5px");
        assertEquals(closeButtonMetadata.getWidthDp(),40);//default
        assertEquals(closeButtonMetadata.getHeightDp(),40);
    }

    @Test
    public void testGetSize_emptyString(){
        CloseButtonMetadata closeButtonMetadata = new CloseButtonMetadata("noImage","","5px");
        assertEquals(closeButtonMetadata.getWidthDp(),40);//default
        assertEquals(closeButtonMetadata.getHeightDp(),40);
    }

    @Test
    public void getPadding_paddingStringInPx(){
        CloseButtonMetadata closeButtonMetadata = new CloseButtonMetadata("noImage","15x15","5px");
        assertEquals(closeButtonMetadata.getPaddingDp(),5);
    }

    @Test
    public void getPadding_paddingStringNoMetric(){
        CloseButtonMetadata closeButtonMetadata = new CloseButtonMetadata("noImage","15x15","6");
        assertEquals(closeButtonMetadata.getPaddingDp(),6);
    }

    @Test
    public void getPadding_nullString(){
        CloseButtonMetadata closeButtonMetadata = new CloseButtonMetadata("noImage","15x15",null);
        assertEquals(closeButtonMetadata.getPaddingDp(),8);
    }

    @Test
    public void getPadding_junkString(){
        CloseButtonMetadata closeButtonMetadata = new CloseButtonMetadata("noImage","15x15","junkPx");
        assertEquals(closeButtonMetadata.getPaddingDp(),8);
    }

    @Test
    public void getPadding_emptyString(){
        CloseButtonMetadata closeButtonMetadata = new CloseButtonMetadata("noImage","","");
        assertEquals(closeButtonMetadata.getPaddingDp(),8);
    }
}
