package fr.kwanko.internal.mraid.calendar;

/**
 * SourceCode
 * Created by erusu on 4/21/2017.
 */

class RruleLogic {

    private static final String FREQ_DAILY = "daily";
    private static final String FREQ_WEEKLY = "weekly";
    private static final String FREQ_MONTHLY = "monthly";
    private static final String FREQ_YEARLY = "yearly";
    private static final int MAX_YEAR_DAY = 366;
    private static final int MAX_MONTH = 12;
    private static final String [] WEEK_DAYS = new String[]{
            "SU","MO","TU","WE","TH","FR","SA"
    };
    private static final int MAX_NUMBER_DAYS_IN_MONTH = 31;
    public static final String WKST_SU = "WKST=SU;";

    private final Recurrence recurrence;

    private RruleLogic(Recurrence recurrence){
        this.recurrence = recurrence;
    }

    static RruleLogic with(Recurrence recurrence){
        return new RruleLogic(recurrence);
    }

    String compute(){
        if(recurrence == null){
            return null;
        }
        StringBuilder rule = new StringBuilder();
        if(recurrence.getFrequency() == null ||recurrence.getFrequency().isEmpty()){
            throw new IllegalStateException("recurrence.getFrequency() is null");
        }
        rule.append("FREQ=");
        rule.append(recurrence.getFrequency().toUpperCase());
        rule.append(";");

        if(recurrence.getExpires() != CalendarEvent.INVALID_TIME ){
            rule.append("UNTIL=");
            rule.append(recurrence.getExpires());
            rule.append(";");
        }
        if(recurrence.getInterval() != -1){
            rule.append("INTERVAL=");
            rule.append(recurrence.getInterval());
            rule.append(";");
        }
        if(FREQ_DAILY.equals(recurrence.getFrequency())){
            rule.append(WKST_SU);
            rule.append("FREQ=DAILY;");
        }
        if(FREQ_WEEKLY.equals(recurrence.getFrequency())){
            rule.append(WKST_SU);
            injectByDay(rule);
        }
        if(FREQ_MONTHLY.equals(recurrence.getFrequency())){
            rule.append(WKST_SU);
            injectByMonthDay(rule);
        }
        if(FREQ_YEARLY.equals(recurrence.getFrequency())){
            rule.append(WKST_SU);
            injectByDay(rule);
            injectByMonthDay(rule);
            injectByYearDay(rule);
            injectByMonthYear(rule);
        }

        return rule.toString();
    }

    private void injectByDay(StringBuilder rule){
        if(recurrence.getRecurrencePlan()!=null && recurrence.getRecurrencePlan().containsKey(Recurrence.REC_DAYS_IN_WEEK)){
            String res = translateWeekIntegersToDays(recurrence.getRecurrencePlan().get(Recurrence.REC_DAYS_IN_WEEK));
            rule.append("BYDAY=");
            rule.append(res);
            rule.append(";");
        }
    }

    private void injectByMonthDay(StringBuilder rule){
        if(recurrence.getRecurrencePlan() != null && recurrence.getRecurrencePlan().containsKey(Recurrence.REC_DAYS_IN_MONTH)){
            String res = translateMonthIntegersToDays(recurrence.getRecurrencePlan().get(Recurrence.REC_DAYS_IN_MONTH));
            rule.append("BYMONTHDAY=");
            rule.append(res);
            rule.append(";");
        }
    }

    private void injectByYearDay(StringBuilder rule){
        if(recurrence.getRecurrencePlan() != null && recurrence.getRecurrencePlan().containsKey(Recurrence.REC_DAYS_IN_YEAR)){
            String res = translateYearIntegersToDays(recurrence.getRecurrencePlan().get(Recurrence.REC_DAYS_IN_YEAR));
            rule.append("BYYEARDAY=");
            rule.append(res);
            rule.append(";");
        }
    }

    private void injectByMonthYear(StringBuilder rule){
        if(recurrence.getRecurrencePlan() != null && recurrence.getRecurrencePlan().containsKey(Recurrence.REC_MONTHS_IN_YEAR)){
            String res = translateMonthYearInteger(recurrence.getRecurrencePlan().get(Recurrence.REC_MONTHS_IN_YEAR));
            rule.append("BYMONTH=");
            rule.append(res);
            rule.append(";");
        }
    }

    private String translateWeekIntegersToDays(String param) throws IllegalArgumentException {
        StringBuilder daysResult = new StringBuilder();
        String daysString = cleanFromParenthesis(param);
        boolean[] daysCheckList = new boolean[7];
        String[] days = daysString.split(",");
        int dayNumber;
        for (final String day : days) {
            dayNumber = Integer.parseInt(day);
            dayNumber = dayNumber == 7 ? 0 : dayNumber;
            if (!daysCheckList[dayNumber]) {
                daysResult.append(WEEK_DAYS[dayNumber] + ",");
                daysCheckList[dayNumber] = true;
            }
        }
        if (days.length == 0) {
            throw new IllegalArgumentException("at least one day is expected");
        }
        daysResult.deleteCharAt(daysResult.length() - 1);
        return daysResult.toString();
    }

    private String translateMonthIntegersToDays(String param) throws IllegalArgumentException {
        StringBuilder daysResult = new StringBuilder();
        boolean[] daysAlreadyCounted = new boolean[2 * MAX_NUMBER_DAYS_IN_MONTH + 1];
        String daysString = cleanFromParenthesis(param);
        String[] days = daysString.split(",");
        int dayNumber;
        for (final String day : days) {
            dayNumber = Integer.parseInt(day);
            if (dayNumber == 0 ||
                    dayNumber <= -MAX_NUMBER_DAYS_IN_MONTH ||
                    dayNumber > MAX_NUMBER_DAYS_IN_MONTH) {
                throw new IllegalArgumentException(" invalid day number");
            }
            if (!daysAlreadyCounted[dayNumber + MAX_NUMBER_DAYS_IN_MONTH]) {
                daysResult.append(day);
                daysResult.append(",");
                daysAlreadyCounted[dayNumber + MAX_NUMBER_DAYS_IN_MONTH] = true;
            }
        }
        daysResult.deleteCharAt(daysResult.length() - 1);
        return daysResult.toString();
    }

    private String translateYearIntegersToDays(String param){
        StringBuilder daysResult = new StringBuilder();
        String daysString = cleanFromParenthesis(param);
        String[] days = daysString.split(",");
        for(String d:days){
            int dd = Integer.parseInt(d);
            if(dd == 0 || dd<=-MAX_YEAR_DAY || dd> MAX_YEAR_DAY){
                throw new IllegalArgumentException("invalid day in year = "+dd);
            }
            daysResult.append(dd);
            daysResult.append(",");
        }
        daysResult.deleteCharAt(daysResult.length() - 1);
        return daysResult.toString();
    }

    private String translateMonthYearInteger(String param){
        StringBuilder monthResult = new StringBuilder();
        String monthString = cleanFromParenthesis(param);
        String [] months = monthString.split(",");
        for(String m:months){
            int mm = Integer.parseInt(m);
            if(mm==0 || mm<-MAX_MONTH || mm> MAX_MONTH){
                throw new IllegalArgumentException("invalid month number = "+mm);
            }
            monthResult.append(mm);
        }
        monthResult.deleteCharAt(monthResult.length() - 1);
        return monthResult.toString();
    }

    private String cleanFromParenthesis(String param){
        String res = param;
        if(res.contains("[")){
            res = res.substring(1);
        }
        if(res.contains("]")){
            res = res.substring(0,res.length() - 1);
        }
        return res;
    }


}
