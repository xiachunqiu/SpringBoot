package pers.xiachunqiu.obsidian.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public final class DateUtil {
    private DateUtil() {
    }

    public static Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        date = calendar.getTime();
        return date;
    }

    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static String format(String pattern, Date date) {
        return date == null ? "" : (new SimpleDateFormat(pattern)).format(date);
    }

    public static String format(Date date) {
        return date == null ? "" : (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date);
    }

    public static Date parse(String pattern, String text) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        if (text != null && text.trim().length() != 0) {
            return dateFormat.parse(text);
        } else {
            throw new ParseException("传入参数：" + text + "有误。", 0);
        }
    }

    public static Date getDay(Date date, int day) {
        GregorianCalendar gca = new GregorianCalendar();
        gca.setTime(date);
        gca.add(Calendar.DATE, day);
        return gca.getTime();
    }

    public static int daysOfTwo(Date fromDate, Date toDate) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        long to = df.parse(format("yyyy-MM-dd", toDate)).getTime();
        long from = df.parse(format("yyyy-MM-dd", fromDate)).getTime();
        Long days = (to - from) / 86400000L;
        return days.intValue();
    }

    public static long getDifferHours(Date startDate, Date endDate) {
        Calendar[] cal = new Calendar[2];
        Date[] d = new Date[]{endDate, startDate};
        for (int i = 0; i < cal.length; ++i) {
            cal[i] = Calendar.getInstance();
            cal[i].setTime(d[i]);
            cal[i].set(Calendar.HOUR_OF_DAY, 0);
            cal[i].set(Calendar.MINUTE, 0);
            cal[i].set(Calendar.SECOND, 0);
        }
        long m = cal[0].getTime().getTime();
        long n = cal[1].getTime().getTime();
        return (long) ((int) Math.abs((m - n) / 1000L / 3600L / 24L));
    }
}