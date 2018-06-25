package fr.kwanko.rest.network;

import android.content.Context;
import android.location.Location;

import fr.kwanko.common.Preconditions;
import fr.kwanko.params.TrackingParamsUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.quote;

/**
 * SourceCode
 * Created by erusu on 2/23/2017.
 */

public class HtmlContentProcessors {

    private List<HtmlProcessor> processors;

    private HtmlContentProcessors(Context context, Location location) {
        processors = new ArrayList<>();
        processors.add(new ReplaceUserIdMacro(context));
        processors.add(new ReplaceLocationMacro(location));
    }

    public static HtmlContentProcessors instance(Context context, Location location) {
        return new HtmlContentProcessors(context, location);
    }

    public String processHtml(String html) {
        String processedHtml = html;
        for (HtmlProcessor processor : processors) {
            processedHtml = processor.process(html);
        }
        return processedHtml;
    }

    private interface HtmlProcessor {

        String process(String html);
    }

    private static class Markup {
        private final String a;
        private final String b;

        private Markup(String a, String b) {
            this.a = a;
            this.b = b;
        }
    }

    /**
     * Replacing markup. Markup is [foo] or {foo} and is case INSENSITIVE.
     */
    public abstract static class AbstractReplaceHtmlProcessor implements HtmlProcessor {

        private final Pattern regex;
        private final List<Markup> pairs = Arrays.asList(new Markup("[", "]"), new Markup("{", "}"));

        public AbstractReplaceHtmlProcessor(String macroName) {
            try {
                StringBuilder sb = new StringBuilder();
                for (Markup markup : pairs) {
                    if (sb.length() != 0) {
                        sb.append("|");
                    }
                    sb.append(quote(markup.a + macroName + markup.b));
                    sb.append("|");
                    sb.append(quote(URLEncoder.encode(markup.a, "UTF-8") + macroName + URLEncoder.encode(markup.b, "UTF-8")));
                }
                regex = Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
            } catch(UnsupportedEncodingException e) {
                throw new IllegalArgumentException(e);
            }
        }

        protected abstract String getValue();

        public String process(String html) {
            String value = getValue();
            if (value != null) {
                return regex.matcher(html).replaceAll(value);
            } else {
                return html;
            }
        }
    }

    private class ReplaceUserIdMacro extends AbstractReplaceHtmlProcessor {
        private Context context;

        private ReplaceUserIdMacro(Context context) {
            super("userID");
            this.context = context;
        }

        @Override
        protected String getValue() {
            return TrackingParamsUtils.getUserId(context);
        }
    }

    private static class ReplaceLatMacro extends AbstractReplaceHtmlProcessor {
        private Location location;

        private ReplaceLatMacro(Location location) {
            super("lat");
            this.location = location;
        }

        @Override
        protected String getValue() {
            return String.valueOf(location.getLatitude());
        }
    }

    private static class ReplaceLngMacro extends AbstractReplaceHtmlProcessor {
        private Location location;

        private ReplaceLngMacro(Location location) {
            super("lng");
            this.location = location;
        }

        @Override
        protected String getValue() {
            return String.valueOf(location.getLongitude());
        }
    }

    public static class ReplaceLocationMacro implements HtmlProcessor {

        private Location location;
        private final ReplaceLatMacro replaceLatMacro;
        private final ReplaceLngMacro replaceLngMacro;

        public ReplaceLocationMacro(Location location) {
            Preconditions.checkNotNull(location);
            this.location = location;
            replaceLatMacro = new ReplaceLatMacro(location);
            replaceLngMacro = new ReplaceLngMacro(location);
        }

        @Override
        public String process(String html) {
            html = replaceLatMacro.process(html);
            html = replaceLngMacro.process(html);
            return html;
        }
    }

}

