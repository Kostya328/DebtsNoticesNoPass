package check.debts.debts_notices.mapper;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class ConvertHelper {
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final String MOSCOW_TIME_ZONE = "Europe/Moscow";
    public static final String NULL_DATE_STR = "1901-01-01 00:00:00";
    public static Date NULL_DATE;

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT);
    private static final SimpleDateFormat DATE_TIME_FORMATTER = new SimpleDateFormat(DATE_TIME_FORMAT);

    static {
        var timeZone = TimeZone.getTimeZone(MOSCOW_TIME_ZONE);
        DATE_FORMATTER.setTimeZone(timeZone);
        DATE_TIME_FORMATTER.setTimeZone(timeZone);
        NULL_DATE = stringDateToDate(NULL_DATE_STR);
    }

    public static Date stringDateToDate(String date) {
        if (!StringUtils.isBlank(date)) try {
            return DATE_FORMATTER.parse(date);
        } catch (Exception ignored) {
        }

        return NULL_DATE;
    }

    public static Date stringDateTimeToDate(String dateTime) {
        if (!StringUtils.isBlank(dateTime)) try {
            return DATE_TIME_FORMATTER.parse(dateTime);
        } catch (Exception ignored) {
        }

        return NULL_DATE;
    }
}
