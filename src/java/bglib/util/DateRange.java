package bglib.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Class declaration
 *
 *
 * @author
 * @version %I%, %G%
 */
public class DateRange
{
    private static final List<String> PATTERNS = new ArrayList<String>(Arrays.asList(new String[] {
            BGConstants.PLAYDATE_PATTERN, "yyyy-MM", "yyyy"
    }));

    private AuDate _From, _To;

    public DateRange(AuDate from, AuDate to) {
        _From = new AuDate(from);
        _To = new AuDate(to);

        checkDates();
    }

    public DateRange(String from, String to) {
        parse(from, to);
        checkDates();
    }

    private void checkDates() {
        if (_From==null || _From.isNull()) {
            _From = new AuDate(AuDate.MIN_DATE);
        }
        if (_To==null || _To.isNull()) {
            _To = new AuDate(AuDate.MAX_DATE);
        }
    }

    private void parse(String from, String to) {
        if (from!=null) {
            _From = new AuDate(from, BGConstants.PLAYDATETIME_PATTERN);
            if (_From.isNull()) {
                _From = new AuDate(from, BGConstants.PLAYDATE_PATTERN);
                if (_From.isNull()) {
                    _From = new AuDate(from, "yyyy-MM");
                    if (_From.isNull()) {
                        _From = new AuDate(from, "yyyy");
                        if (_From.isNull()==false) { // got a year in From field
                            if (to==null || to.length()==0) {
                                _To = new AuDate(_From);
                                _To.add(Calendar.YEAR, 1);
                            }
                        }
                    } else { // Got a month in From field
                        if (to==null || to.length()==0) {
                            _To = new AuDate(_From);
                            _To.add(Calendar.MONTH, 1);
                        }
                    }
                }
            }
        }

        if ((_To==null || _To.isNull()) && to!=null) {
            _To = new AuDate(to, BGConstants.PLAYDATETIME_PATTERN);
            if (_To.isNull()) {
                _To = new AuDate(to, BGConstants.PLAYDATE_PATTERN);
                if (_To.isNull()) {
                    _To = new AuDate(to, "yyyy-MM");
                    if (_To.isNull()) {
                        _To = new AuDate(to, "yyyy");
                        if (_To.isNull()==false) { // for "to" dates, go to last day of year
                            _To.add(Calendar.YEAR, 1);
                            _To.add(Calendar.DAY_OF_YEAR, -1);
                        }
                    } else { // for "to" dates, go to last day of month
                        _To.add(Calendar.MONTH, 1);
                        _To.add(Calendar.DAY_OF_YEAR, -1);
                    }
                }
            }
        }
    }

    public AuDate getFromDate() { return _From; }
    public AuDate getToDate() { return _To; }


}
