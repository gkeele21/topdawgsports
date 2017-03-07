/*--- formatted by Jindent 2.1, (www.c-lab.de/~jindent) ---*/

package bglib.util;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.StringTokenizer;

/**
 * Class declaration
 *
 *
 * @author
 * @version %I%, %G%
 */
public class AuConvert
{

	/**
	 * Constructor declaration
	 *
	 * @see
	 */
	public AuConvert() {}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static int stringToInteger(String value)
	{
		return AuConvert.stringToInteger(value, 0);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param defaultValue
	 *
	 * @return
	 * @see
	 */
	public static int stringToInteger(String value, int defaultValue)
	{
		try
		{
			return Integer.parseInt(value);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static long stringToLong(String value)
	{
		return AuConvert.stringToLong(value, 0);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param defaultValue
	 *
	 * @return
	 * @see
	 */
	public static long stringToLong(String value, long defaultValue)
	{
		try
		{
			return Long.parseLong(value);
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static float stringToFloat(String value)
	{
		return AuConvert.stringToFloat(value, 0);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param defaultValue
	 *
	 * @return
	 * @see
	 */
	public static float stringToFloat(String value, float defaultValue)
	{
		try
		{
			return Float.valueOf(value).floatValue();
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static double stringToDouble(String value)
	{
		return AuConvert.stringToDouble(value, 0.0);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param defaultValue
	 *
	 * @return
	 * @see
	 */
	public static double stringToDouble(String value, double defaultValue)
	{
		try
		{
			return Double.valueOf(value).doubleValue();
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static boolean stringToBoolean(String value)
	{
		return AuConvert.stringToBoolean(value, false);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param defaultValue
	 *
	 * @return
	 * @see
	 */
	public static boolean stringToBoolean(String value, boolean defaultValue)
	{
		try
		{
			if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t") || value.equalsIgnoreCase("on"))
			{
				return true;
			}
			else
			{
				return defaultValue;
			}
		}
		catch (Exception e)
		{
			return defaultValue;
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static String toString(String value)
	{
		return AuConvert.toString(value, "");
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param defaultValue
	 *
	 * @return
	 * @see
	 */
	public static String toString(String value, String defaultValue)
	{
		if (value == null)
		{
			return defaultValue;
		}
		else
		{
			return value;
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param values
	 * @param quote
	 *
	 * @return
	 * @see
	 */
	public static String toString(String[] values, String quote)
	{
		if (values == null)
		{
			return null;
		}
		else
		{
			StringBuffer	sb = new StringBuffer();

			sb.append(quote);
			sb.append(values[0]);
			sb.append(quote);

			for (int i = 1; i < values.length; i++)
			{
				sb.append(",");
				sb.append(quote);
				sb.append(values[i]);
				sb.append(quote);
			}

			return sb.toString();
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static String[] toStringArray(String value)
	{
		return toStringArray(value, ",");
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param delims
	 *
	 * @return
	 * @see
	 */
	public static String[] toStringArray(String value, String delims)
	{
		StringTokenizer st = new StringTokenizer(value, delims);
		int				count = st.countTokens();
		String[]		values = new String[count];
		String			sToken = "";
		int				i = 0;

		while (st.hasMoreTokens())
		{
			sToken = st.nextToken();
			values[i] = sToken;
			i++;
		}

		return values;
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static String booleanToString(boolean value)
	{
		if (value)
		{
			return "true";
		}
		else
		{
			return "false";
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static String integerToString(int value)
	{
		return String.valueOf(value);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param minimumFractionDigits
	 * @param maximumFractionDigits
	 *
	 * @return
	 * @see
	 */
	public static String integerToString(int value, int minimumFractionDigits, int maximumFractionDigits)
	{
		return AuConvert.integerToString(value, minimumFractionDigits, maximumFractionDigits, Locale.getDefault());
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param minimumFractionDigits
	 * @param maximumFractionDigits
	 * @param locale
	 *
	 * @return
	 * @see
	 */
	public static String integerToString(int value, int minimumFractionDigits, int maximumFractionDigits, Locale locale)
	{
		NumberFormat	nf = NumberFormat.getInstance(locale);

		nf.setMinimumFractionDigits(minimumFractionDigits);
		nf.setMaximumFractionDigits(maximumFractionDigits);

		return nf.format(value);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static String longToString(long value)
	{
		return String.valueOf(value);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param minimumFractionDigits
	 * @param maximumFractionDigits
	 *
	 * @return
	 * @see
	 */
	public static String longToString(long value, int minimumFractionDigits, int maximumFractionDigits)
	{
		return AuConvert.longToString(value, minimumFractionDigits, maximumFractionDigits, Locale.getDefault());
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param minimumFractionDigits
	 * @param maximumFractionDigits
	 * @param locale
	 *
	 * @return
	 * @see
	 */
	public static String longToString(long value, int minimumFractionDigits, int maximumFractionDigits, Locale locale)
	{
		NumberFormat	nf = NumberFormat.getInstance(locale);

		nf.setMinimumFractionDigits(minimumFractionDigits);
		nf.setMaximumFractionDigits(maximumFractionDigits);

		return nf.format(value);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static String floatToString(float value)
	{
		return String.valueOf(value);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param minimumFractionDigits
	 * @param maximumFractionDigits
	 *
	 * @return
	 * @see
	 */
	public static String floatToString(float value, int minimumFractionDigits, int maximumFractionDigits)
	{
		return AuConvert.floatToString(value, minimumFractionDigits, maximumFractionDigits, Locale.getDefault());
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param minimumFractionDigits
	 * @param maximumFractionDigits
	 * @param locale
	 *
	 * @return
	 * @see
	 */
	public static String floatToString(float value, int minimumFractionDigits, int maximumFractionDigits, Locale locale)
	{
		NumberFormat	nf = NumberFormat.getInstance(locale);

		nf.setMinimumFractionDigits(minimumFractionDigits);
		nf.setMaximumFractionDigits(maximumFractionDigits);

		return nf.format(value);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static String doubleToString(double value)
	{
		return String.valueOf(value);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param minimumFractionDigits
	 * @param maximumFractionDigits
	 *
	 * @return
	 * @see
	 */
	public static String doubleToString(double value, int minimumFractionDigits, int maximumFractionDigits)
	{
		return AuConvert.doubleToString(value, minimumFractionDigits, maximumFractionDigits, Locale.getDefault());
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param minimumFractionDigits
	 * @param maximumFractionDigits
	 * @param locale
	 *
	 * @return
	 * @see
	 */
	public static String doubleToString(double value, int minimumFractionDigits, int maximumFractionDigits, Locale locale)
	{
		NumberFormat	nf = NumberFormat.getInstance(locale);

		nf.setMinimumFractionDigits(minimumFractionDigits);
		nf.setMaximumFractionDigits(maximumFractionDigits);

		return nf.format(value);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param minimumFractionDigits
	 * @param maximumFractionDigits
	 *
	 * @return
	 * @see
	 */
	public static String currencyToString(double value, int minimumFractionDigits, int maximumFractionDigits)
	{
		return AuConvert.currencyToString(value, minimumFractionDigits, maximumFractionDigits, Locale.getDefault());
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param minimumFractionDigits
	 * @param maximumFractionDigits
	 * @param locale
	 *
	 * @return
	 * @see
	 */
	public static String currencyToString(double value, int minimumFractionDigits, int maximumFractionDigits, Locale locale)
	{
		NumberFormat	nf = NumberFormat.getCurrencyInstance(locale);

		nf.setMinimumFractionDigits(minimumFractionDigits);
		nf.setMaximumFractionDigits(maximumFractionDigits);

		return nf.format(value);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static String toField(String value)
	{
		if (value == null)
		{
			return "";
		}

		return AuUtil.replace(value, "'", "''");
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param addQuotes
	 *
	 * @return
	 * @see
	 */
	public static String toField(String value, boolean addQuotes)
	{
		return AuUtil.between(AuConvert.toField(value), "'", "'", true);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static String toHtmlSpace(String value)
	{
		if (AuUtil.isEmpty(value))
		{
			return "&nbsp;";
		}
		else
		{
			return value;
		}
	}

}

