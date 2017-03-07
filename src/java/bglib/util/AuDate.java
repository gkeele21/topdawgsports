/*--- formatted by Jindent 2.1, (www.c-lab.de/~jindent) ---*/

package bglib.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Class declaration
 *
 *
 * @author
 * @version %I%, %G%
 */
public class AuDate implements Serializable
{
    public static final String  FORMAT_ORACLE = "dd-MMM-yy";
    public static final AuDate MIN_DATE = new AuDate(0);
    public static final AuDate MAX_DATE = new AuDate("3000-01-01", BGConstants.PLAYDATE_PATTERN);
    private boolean				_isNull = false;
    private SimpleDateFormat	_format = new SimpleDateFormat();
    private Calendar			_cal = _format.getCalendar();

    /**
     * Method declaration
     *
     *
     * @param date
     *
     * @return
     * @see
     */
    public static AuDate getInstance(Date date)
    {
        return new AuDate(date);
    }

    /**
     * Method declaration
     *
     *
     * @param date
     *
     * @return
     * @see
     */
    public static AuDate getInstance(AuDate date)
    {
        return new AuDate(date);
    }

    /**
     * Method declaration
     *
     *
     * @return
     * @see
     */
    public static AuDate getInstance()
    {
        return new AuDate();
    }

    /**
     * Method declaration
     *
     *
     * @param year
     * @param month
     * @param day
     *
     * @return
     * @see
     */
    public static AuDate getInstance(int year, int month, int day)
    {
        return new AuDate(year, month, day);
    }

    /**
     * Method declaration
     *
     *
     * @param calendar
     *
     * @return
     * @see
     */
    public static AuDate getInstance(Calendar calendar)
    {
        return new AuDate(calendar);
    }

    /**
     * Method declaration
     *
     *
     * @param dateValue
     *
     * @return
     * @see
     */
    public static AuDate getInstance(long dateValue)
    {
        return new AuDate(dateValue);
    }

    /**
     * Method declaration
     *
     *
     * @param julianValue
     *
     * @return
     * @see
     */
    public static AuDate getInstance(String julianValue)
    {
        return new AuDate(julianValue);
    }

    /**
     * Method declaration
     *
     *
     * @param dateString
     * @param pattern
     *
     * @return
     * @see
     */
    public static AuDate getInstance(String dateString, String pattern)
    {
        return new AuDate(dateString, pattern);
    }

    /**
     * Constructor declaration
     *
     * @see
     */
    public AuDate()
    {
        this.setDate();
//        setDate("2006-09-01", BGConstants.PLAYDATE_PATTERN);
    }

    /**
     * Constructor declaration
     *
     *
     * @param dateString
     * @param pattern
     * @see
     */
    public AuDate(String dateString, String pattern)
    {
        this.setDate(dateString, pattern);
    }

    /**
     * Constructor declaration
     *
     *
     * @param julianString
     * @see
     */
    public AuDate(String julianString)
    {
        this.setDate(julianString);
    }

    /**
     * Constructor declaration
     *
     *
     * @param date
     * @see
     */
    public AuDate(Date date)
    {
        this.setDate(date);
    }

    /**
     * Constructor declaration
     *
     *
     * @param date
     * @see
     */
    public AuDate(AuDate date)
    {
        this.setDate(date);
    }

    /**
     * Constructor declaration
     *
     *
     * @param calendar
     * @see
     */
    public AuDate(Calendar calendar)
    {
        this.setCalendar(calendar);
    }

    /**
     * Constructor declaration
     *
     *
     * @param year
     * @param month
     * @param day
     * @see
     */
    public AuDate(int year, int month, int day)
    {
        this.setDate(year, month, day);
    }

    /**
     * Constructor declaration
     *
     *
     * @param dateValue
     * @see
     */
    public AuDate(long dateValue)
    {
        this.setDate(dateValue);
    }

    /**
     * Method declaration
     *
     *
     * @return
     * @see
     */
    public boolean isNull()
    {
        return _isNull;
    }

    public boolean isValid() {
        return (get(Calendar.YEAR) >= 1900) && (get(Calendar.YEAR) < 2500);
    }

    /**
     * Method declaration
     *
     *
     * @return
     * @see
     */
    public Calendar getCalendar()
    {
        if (_isNull)
        {
            return null;
        }

        return _cal;
    }

    /**
     * Method declaration
     *
     *
     * @param calendar
     * @see
     */
    public void setCalendar(Calendar calendar)
    {
        _isNull = (calendar == null);

        if (_isNull)
        {
            return;
        }

        _cal = calendar;
    }

    /**
     * Method declaration
     *
     *
     * @return
     * @see
     */
    public Date getTime()
    {
        if (_isNull)
        {
            return null;
        }

        return _cal.getTime();
    }

    /**
     * Method declaration
     *
     * @see
     */
    public void setDate()
    {
        _isNull = false;
        long now = System.currentTimeMillis();
        _cal.setTimeInMillis(now);
//        _cal.add(Calendar.MILLISECOND, TimeZone.getTimeZone("America/New_York").getOffset(now)-TimeZone.getDefault().getOffset(now));
    }

    /**
     * Method declaration
     *
     *
     * @param date
     * @see
     */
    public void setDate(Date date)
    {
        _isNull = (date == null);

        if (_isNull)
        {
            return;
        }

        _cal.setTime(date);
    }

    /**
     * Method declaration
     *
     *
     * @param date
     * @see
     */
    public void setDate(AuDate date)
    {
        _isNull = (date == null);

        if (_isNull)
        {
            return;
        }

        _cal.setTime(date.getTime());
    }

    /**
     * Method declaration
     *
     *
     * @param dateValue
     * @see
     */
    public void setDate(long dateValue)
    {
        _isNull = false;

        Date	date = new Date(dateValue);

        _cal.setTime(date);
    }

    /**
     * Method declaration
     *
     *
     * @param year
     * @param month
     * @param day
     * @see
     */
    public void setDate(int year, int month, int day)
    {
        _isNull = false;

        _cal.set(year, month, day);
    }

    /**
     * Method declaration
     *
     *
     * @param dateString
     * @param pattern
     * @see
     */
    public void setDate(String dateString, String pattern)
    {
        Date	date;

        _format.applyPattern(pattern);

        try
        {
            date = _format.parse(dateString);

            _cal.setTime(date);

            _isNull = false;
        }
        catch (java.text.ParseException e)
        {
            _isNull = true;
        }
    }

    /**
     * Method declaration
     *
     *
     * @param julianString
     * @see
     */
    public void setDate(String julianString)
    {
        if (AuConvert.stringToInteger(julianString, 0) == 0)
        {
            _isNull = true;
        }
        else
        {
            try
            {
                int length = julianString.length();
                int year = 1900 + AuConvert.stringToInteger(julianString.substring(0, length - 3));
                int days = AuConvert.stringToInteger(julianString.substring(length - 3));

                _cal.set(Calendar.YEAR, year);
                _cal.set(Calendar.DAY_OF_YEAR, days);

                _isNull = false;
            }
            catch (Exception e)
            {
                _isNull = true;
            }
        }
    }

    /**
     * Method declaration
     *
     *
     * @return
     * @see
     */
    public TimeZone getTimeZone()
    {
        if (_isNull)
        {
            return null;
        }

        return _cal.getTimeZone();
    }

    /**
     * Method declaration
     *
     *
     * @param timeZone
     * @see
     */
    public void setTimeZone(TimeZone timeZone)
    {
        if (_isNull)
        {
            return;
        }

        _cal.setTimeZone(timeZone);
    }

    /**
     * Method declaration
     *
     *
     * @return
     * @see
     */
    public java.sql.Date getSqlDate()
    {
        if (_isNull)
        {
            return null;
        }

        return new java.sql.Date(_cal.getTime().getTime());
    }

    /**
     * Method declaration
     *
     *
     * @return
     * @see
     */
    public long getDateInMillis()
    {
        if (_isNull)
        {
            return 0;
        }

        return _cal.getTime().getTime();
    }

    /**
     * Method declaration
     *
     *
     * @param field
     *
     * @return
     * @see
     */
    public int get(int field)
    {
        if (_isNull)
        {
            return -1;
        }

        return _cal.get(field);
    }

    /**
     * Method declaration
     *
     *
     * @param field
     * @param value
     * @see
     */
    public void set(int field, int value)
    {
        if (_isNull)
        {
            return;
        }

        _cal.set(field, value);
    }

    /**
     * Method declaration
     *
     *
     * @param field
     * @param value
     * @see
     */
    public void add(int field, int value)
    {
        if (_isNull)
        {
            return;
        }

        _cal.add(field, value);
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     * @param ignoreTime
     *
     * @return
     * @see
     */
    public boolean before(AuDate auDate, boolean ignoreTime)
    {
        if (_isNull)
        {
            return false;
        }

        if (ignoreTime)
        {
            AuDate  d1 = new AuDate(this.get(Calendar.YEAR), this.get(Calendar.MONTH), this.get(Calendar.DATE));
            AuDate  d2 = new AuDate(auDate.get(Calendar.YEAR), auDate.get(Calendar.MONTH), auDate.get(Calendar.DATE));

            return d1.getCalendar().before(d2.getCalendar());
        }
        else
        {
            return _cal.before(auDate.getCalendar());
        }
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     * @param ignoreTime
     *
     * @return
     * @see
     */
    public boolean after(AuDate auDate, boolean ignoreTime)
    {
        if (_isNull)
        {
            return false;
        }

        if (ignoreTime)
        {
            AuDate  d1 = new AuDate(this.get(Calendar.YEAR), this.get(Calendar.MONTH), this.get(Calendar.DATE));
            AuDate  d2 = new AuDate(auDate.get(Calendar.YEAR), auDate.get(Calendar.MONTH), auDate.get(Calendar.DATE));

            return d1.getCalendar().after(d2.getCalendar());
        }
        else
        {
            return _cal.after(auDate.getCalendar());
        }
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     * @param ignoreTime
     *
     * @return
     * @see
     */
    public boolean equals(AuDate auDate, boolean ignoreTime)
    {
        if (_isNull)
        {
            return false;
        }

        if (ignoreTime)
        {
            AuDate  d1 = new AuDate(this.get(Calendar.YEAR), this.get(Calendar.MONTH), this.get(Calendar.DATE));
            AuDate  d2 = new AuDate(auDate.get(Calendar.YEAR), auDate.get(Calendar.MONTH), auDate.get(Calendar.DATE));

            return d1.getCalendar().equals(d2.getCalendar());
        }
        else
        {
            return _cal.equals(auDate.getCalendar());
        }
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     *
     * @return
     * @see
     */
    public long getAge(AuDate auDate)
    {
        if (_isNull)
        {
            return 0;
        }

        if (this.get(Calendar.MONTH) == auDate.get(Calendar.MONTH))
        {
            if ((this.get(Calendar.YEAR) < auDate.get(Calendar.YEAR)) && (this.get(Calendar.DATE) > auDate.get(Calendar.DATE)))
            {
                return this.getAgeInYears(auDate) - 1;
            }
            else if ((this.get(Calendar.YEAR) > auDate.get(Calendar.YEAR)) && (this.get(Calendar.DATE) < auDate.get(Calendar.DATE)))
            {
                return this.getAgeInYears(auDate) + 1;
            }
            else
            {
                return this.getAgeInYears(auDate);
            }
        }
        else
        {
            return this.getAgeInYears(auDate);
        }
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     *
     * @return
     * @see
     */
    public String getAgeDetail(AuDate auDate)
    {
        long	value = getAgeInMillis(auDate);		// Milliseconds
        long	mill = value % 1000;

        value = value / 1000;		// Seconds

        long	sec = value % 60;

        value = value / 60;		// Minutes

        long	min = value % 60;

        value = value / 60;		// Hours

        long	hour = value % 24;

        value = value / 24;		// Days

        long	day = value % 24;

        return day + "d " + hour + ":" + min + ":" + sec;
    }

    /**
     * Method declaration
     *
     *
     * @param field
     * @param auDate
     *
     * @return
     * @see
     */
    public long getAge(int field, AuDate auDate)
    {
        if (_isNull)
        {
            return 0;
        }

        long	value = 0;

        switch (field)
        {

            case Calendar.MILLISECOND:
                value = this.getAgeInMillis(auDate);

                break;

            case Calendar.SECOND:
                value = this.getAgeInSeconds(auDate);

                break;

            case Calendar.MINUTE:
                value = this.getAgeInMinutes(auDate);

                break;

            case Calendar.HOUR:
                value = this.getAgeInHours(auDate);

                break;

            case Calendar.DATE:
                value = this.getAgeInDays(auDate);

                break;

            case Calendar.WEEK_OF_MONTH:

            case Calendar.WEEK_OF_YEAR:
                value = this.getAgeInWeeks(auDate);

                break;

            case Calendar.MONTH:
                value = this.getAgeInMonths(auDate);

                break;

            case Calendar.YEAR:
                value = this.getAgeInYears(auDate);

                break;
        }

        return value;
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     *
     * @return
     * @see
     */
    private long getAgeInMillis(AuDate auDate)
    {
        return (auDate.getDateInMillis() - this.getDateInMillis());
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     *
     * @return
     * @see
     */
    private long getAgeInSeconds(AuDate auDate)
    {
        return this.getAgeInMillis(auDate) / 1000;
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     *
     * @return
     * @see
     */
    private long getAgeInMinutes(AuDate auDate)
    {
        return this.getAgeInSeconds(auDate) / 60;
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     *
     * @return
     * @see
     */
    private long getAgeInHours(AuDate auDate)
    {
        return this.getAgeInMinutes(auDate) / 60;
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     *
     * @return
     * @see
     */
    private long getAgeInDays(AuDate auDate)
    {
        return this.getAgeInHours(auDate) / 24;
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     *
     * @return
     * @see
     */
    private long getAgeInWeeks(AuDate auDate)
    {
        return this.getAgeInDays(auDate) / 7;
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     *
     * @return
     * @see
     */
    private long getAgeInMonths(AuDate auDate)
    {
        int y = (auDate.get(Calendar.YEAR) - this.get(Calendar.YEAR)) * 12;
        int m = auDate.get(Calendar.MONTH) - this.get(Calendar.MONTH);

        return y + m;
    }

    /**
     * Method declaration
     *
     *
     * @param auDate
     *
     * @return
     * @see
     */
    private long getAgeInYears(AuDate auDate)
    {
        return this.getAgeInMonths(auDate) / 12;
    }

    /**
     * Method declaration
     *
     *
     * @param pattern
     *
     * @return
     * @see
     */
    public String toString(String pattern)
    {
        if (_isNull)
        {
            return "";
        }


        _format.applyPattern(pattern);

        return _format.format(_cal.getTime());
    }

    public String toString(String pattern, Locale locale)
    {
        if (_isNull)
        {
            return "";
        }

        _format = new SimpleDateFormat(pattern, locale);

        return _format.format(_cal.getTime());
    }

    /**
     * Method declaration
     *
     *
     * @return
     * @see
     */
    public String toString()
    {
        if (_isNull)
        {
            return "";
        }

        return _cal.getTime().toString();
    }

    /**
     * Method declaration
     *
     *
     * @return
     * @see
     */
    public String toPattern()
    {
        return _format.toPattern();
    }

    /**
     * Method declaration
     *
     *
     * @return
     * @see
     */
    public String toLocalizedPattern()
    {
        return _format.toLocalizedPattern();
    }

    /**
     * Method declaration
     *
     *
     * @return
     * @see
     */
    public String toJulian()
    {
        if (_isNull)
        {
            return "";
        }

        if (get(Calendar.YEAR) < 2000)
        {
            return toString("0yyDDD");
        }
        else
        {
            return toString("1yyDDD");
        }
    }

    public int hashCode() {
        return _cal.hashCode();
    }

    public boolean equals(Object other) {
        if (other!=null && !(other instanceof AuDate)) {
            return false;
        }

        if ((other==null || ((AuDate)other).isNull()) && isNull()) {
            return true;
        }

        if (other==null) {
            return false;
        }

        return _cal.equals(((AuDate)other).getCalendar());
    }

}

