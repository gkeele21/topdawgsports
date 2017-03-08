/*--- formatted by Jindent 2.1, (www.c-lab.de/~jindent) ---*/

package bglib.util;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class declaration
 *
 *
 * @author
 * @version %I%, %G%
 */
public class AuDateUtil
{

	/**
	 * Method declaration
	 *
	 *
	 * @return
	 * @see
	 */
	public static String[] getShortMonths()
	{
		return getShortMonths(Locale.getDefault());
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param locale
	 *
	 * @return
	 * @see
	 */
	public static String[] getShortMonths(Locale locale)
	{
		DateFormatSymbols   dfs = new DateFormatSymbols(locale);

		return dfs.getShortMonths();
	}

	/**
	 * Method declaration
	 *
	 *
	 * @return
	 * @see
	 */
	public static String[] getMonths()
	{
		return getMonths(Locale.getDefault());
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param locale
	 *
	 * @return
	 * @see
	 */
	public static String[] getMonths(Locale locale)
	{
		DateFormatSymbols   dfs = new DateFormatSymbols(locale);

		return dfs.getMonths();
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param year
	 * @param month
	 *
	 * @return
	 * @see
	 */
	public static List getDays(int year, int month)
	{
		List	days = new ArrayList();

		for (int i = 1; i < AuDateUtil.getDaysInMonth(year, month) + 1; i++)
		{
			String  day = AuConvert.integerToString(i);

			days.add(day);
		}

		return days;
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param year
	 * @param month
	 *
	 * @return
	 * @see
	 */
	public static int getDaysInMonth(int year, int month)
	{
		int days = 0;

		if ((month == 0) || (month == 2) || (month == 4) || (month == 6) || (month == 7) || (month == 9) || (month == 11))
		{
			days = 31;
		}
		else if ((month == 3) || (month == 5) || (month == 8) || (month == 10))
		{
			days = 30;
		}
		else if (month == 1)
		{
			if (year % 4 == 0)
			{
				days = 29;
			}
			else
			{
				days = 28;
			}
		}

		return days;
	}

}

