package fr.kwanko.common;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

import static org.junit.Assert.*;

/**
 * Tests of HtmlUtils.
 */
public class HtmlUtilsTest {

    @Test
    public void ajoutEnFinDeBaliseHeadExistante() throws Exception {
        String html = "<head>h</head>";
        String html2 = HtmlUtils.insertScript(html, "TEST");
        assertThat(html2, CoreMatchers.equalTo("<head>hTEST</head>"));
    }

    @Test
    public void ajoutEnFinDeBaliseHeadExistanteEnMajuscule() throws Exception {
        String html = "<HEAD>h</HEAD>";
        String html2 = HtmlUtils.insertScript(html, "TEST");
        assertThat(html2, CoreMatchers.equalTo("<HEAD>hTEST</HEAD>"));
    }

    @Test
    public void ajoutDansUneNouvelleBaliseHeadJusteAvantLeBodyOuvrant() throws Exception {
        String html = "<html><body></body></html>";
        String html2 = HtmlUtils.insertScript(html, "TEST");
        assertThat(html2, CoreMatchers.equalTo("<html><head>TEST</head><body></body></html>"));
    }

    @Test
    public void ajoutEnFinDansLesAutresCas() throws Exception {
        String html = "X";
        String html2 = HtmlUtils.insertScript(html, "TEST");
        assertThat(html2, CoreMatchers.equalTo("XTEST"));
    }
}