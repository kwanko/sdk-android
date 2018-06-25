package fr.kwanko.common;

import android.text.Html;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.quote;

/**
 * Utils for html manipulation.
 */
public final class HtmlUtils {

    private static final Pattern headClosed = Pattern.compile(quote("</head>"), Pattern.CASE_INSENSITIVE);
    private static final Pattern bodyOpen = Pattern.compile(quote("<body"), Pattern.CASE_INSENSITIVE);

    private HtmlUtils() {
    }

    /**
     * Insert script in head markup of html.
     */
    public static String insertScript(String html, String script) {
        Matcher matcherHeadClose = headClosed.matcher(html);
        Matcher matcherBodyOpen = bodyOpen.matcher(html);
        if (matcherHeadClose.find()) {
            return matcherHeadClose.replaceFirst(script + matcherHeadClose.group());
        } else if (matcherBodyOpen.find()) {
            return matcherBodyOpen.replaceFirst("<head>" + script + "</head>" + matcherBodyOpen.group());
        }
        return html + script;
    }

}
