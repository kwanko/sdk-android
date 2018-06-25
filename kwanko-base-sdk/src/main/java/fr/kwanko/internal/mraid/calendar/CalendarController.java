package fr.kwanko.internal.mraid.calendar;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;

import fr.kwanko.common.KwankoLog;

/**
 * SourceCode
 * Created by erusu on 4/20/2017.
 */

public class CalendarController {

    private CalendarEventParser parser;
    private Context context;

    public CalendarController(Context context) {
        this.context = context;
        parser = new CalendarEventParser();
    }

    public void manageCalendarEventFromArgs(final String[] args) {
        writeEventToAndroidCalendar(parser.fromStringArgs(args));
    }

    private void writeEventToAndroidCalendar(CalendarEvent event) {
        if (event == null) {
            KwankoLog.d("Received a null calendarEvent");
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setType("vnd.android.cursor.item/event");
            intent.putExtra(Events.DESCRIPTION, event.getSummary());
            intent.putExtra(Events.TITLE, event.getDescription());
            intent.putExtra(Events._ID, event.getId());
            if (event.isStartDateValid()) {
                intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, event.getStart());
            }
            if (event.isEndDateValid()) {
                intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, event.getEnd());
            }
            intent.putExtra(Events.EVENT_LOCATION, event.getLocation());
            intent.putExtra(Events.AVAILABILITY, event.isTransparent() ?
                    Events.AVAILABILITY_FREE :
                    Events.AVAILABILITY_BUSY);
            String rrule = RruleLogic.with(event.getRecurrence()).compute();
            if (event.getRecurrence() != null && rrule != null) {
                intent.putExtra(Events.RRULE, rrule);
                KwankoLog.d(rrule);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }catch (ActivityNotFoundException e) {
            KwankoLog.e("no calendar app installed",e);
        } catch (IllegalArgumentException e) {
            KwankoLog.e("create calendar: invalid parameters ", e);
        } catch (Exception e) {
            KwankoLog.e("could not create calendar event",e);
        }
    }

}
