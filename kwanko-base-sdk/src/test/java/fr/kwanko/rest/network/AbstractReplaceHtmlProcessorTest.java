package fr.kwanko.rest.network;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import org.hamcrest.CoreMatchers;
import org.junit.Test;


import static org.junit.Assert.*;

/**
 * Tests of AbstractReplaceHtmlProcessor.
 */
public class AbstractReplaceHtmlProcessorTest {

    static class SimpleReplaceHtmlProcessor extends HtmlContentProcessors.AbstractReplaceHtmlProcessor {

        public SimpleReplaceHtmlProcessor() {
            super("foo");
        }

        @Override
        protected String getValue() {
            return "bar";
        }
    }

    private final SimpleReplaceHtmlProcessor replaceUserIdMacro = new SimpleReplaceHtmlProcessor();
    private final Context context = null;


    @Test
    public void remplaceWithCrochet() throws Exception {
        String process = replaceUserIdMacro.process("a[foo]");
        assertThat(process, CoreMatchers.equalTo("abar"));
    }

    @Test
    public void remplaceWithBracet() throws Exception {
        String process = replaceUserIdMacro.process("a{foo}");
        assertThat(process, CoreMatchers.equalTo("abar"));
    }

    @Test
    public void remplaceCaseInsensitive() throws Exception {
        String process = replaceUserIdMacro.process("a[FOO]");
        assertThat(process, CoreMatchers.equalTo("abar"));
    }

    @Test
    public void remplaceWithEncodedCrochet() throws Exception {
        String process = replaceUserIdMacro.process("%5Bfoo%5D");
        assertThat(process, CoreMatchers.equalTo("bar"));
    }

    @Test
    public void remplaceWithEncodedBracket() throws Exception {
        String process = replaceUserIdMacro.process("%7Bfoo%7D");
        assertThat(process, CoreMatchers.equalTo("bar"));
    }
}