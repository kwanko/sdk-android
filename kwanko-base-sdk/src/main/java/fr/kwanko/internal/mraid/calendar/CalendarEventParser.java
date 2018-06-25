package fr.kwanko.internal.mraid.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import fr.kwanko.common.KwankoLog;

/**
 * SourceCode
 * Created by erusu on 4/20/2017.
 */

class CalendarEventParser {

    private static final String DESCRIPTION = "description";
    private static final String LOCATION = "location";
    private static final String START = "start";
    private static final String END = "end";
    private static final String TRANSPARENCY = "transparency";
    private static final String TRANSPARENT = "transparent";
    private static final String REC_INTERVAL = "interval";
    private static final String REC_FREQUENCIES = "frequency";

    private static final SimpleDateFormat [] DATE_FORMATTERS = new SimpleDateFormat[]{
         new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZZZZZ", Locale.getDefault()),
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ", Locale.getDefault()),
    };


    CalendarEvent fromStringArgs(String [] args){
        if(args == null){
            KwankoLog.d("CalendarEventParser.fromStringArgs received args  == null");
            return null;
        }
        if(args.length <= 0 || args.length %2 != 0){
            KwankoLog.d("Received wrong arguments args.length = "+args.length);
            return null;
        }
        CalendarEvent event = new CalendarEvent();
        Recurrence recurrence = new Recurrence();
        int index = 0;
        for(String s : args){
            switch (s){
                case DESCRIPTION:
                    event.setDescription(args[index + 1]);
                    break;
                case LOCATION:
                    event.setLocation(args[index + 1]);
                    break;
                case START:
                    event.setStart(parseDate(args[index + 1]));
                    break;
                case END:
                    event.setEnd(parseDate(args[index + 1]));
                    break;
                case TRANSPARENCY:
                    event.setTransparency(parseTransparency(args[index + 1]));
                    break;
                case REC_INTERVAL:
                    recurrence.setInterval(parseInt(args[index + 1], Recurrence.DEFAULT_INTERVAL));
                    break;
                case REC_FREQUENCIES:
                    recurrence.setFrequency(args[index + 1]);
                    break;
                case Recurrence.REC_DAYS_IN_MONTH:
                case Recurrence.REC_DAYS_IN_WEEK:
                case Recurrence.REC_DAYS_IN_YEAR:
                case Recurrence.REC_MONTHS_IN_YEAR:
                    Map<String,String> map = recurrence.getRecurrencePlan();
                    if(map == null){
                        map = new HashMap<>();
                        recurrence.setRecurrencePlan(map);
                    }
                    map.put(s,args[index + 1]);
                    break;
                case Recurrence.EXPIRES:
                    recurrence.setExpires(parseDate(args[index+ 1]));
                    break;
                default:
                    break;
            }

            index++;
        }
        if(recurrence.getFrequency() != null && recurrence.getFrequency().isEmpty()) {
            event.setRecurrence(recurrence);
        }
        return event;
    }

    private long parseDate(String date){
        for(SimpleDateFormat s:DATE_FORMATTERS){
            try {
                Date result = s.parse(date);
                if(result != null){
                    return result.getTime();
                }
            } catch (ParseException e) {
                KwankoLog.e(e);
            }
        }
        return -1;
    }

    private boolean parseTransparency(String transparency){
        return TRANSPARENT.equals(transparency);
    }

    private int parseInt(String value,int defaultValue){
        try {
            return Integer.parseInt(value);
        }catch (NumberFormatException e){
            KwankoLog.e(e);
            return defaultValue;
        }
    }
}
