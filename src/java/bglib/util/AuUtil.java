/*--- formatted by Jindent 2.1, (www.c-lab.de/~jindent) ---*/

package bglib.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Class declaration
 *
 *
 * @author
 * @version %I%, %G%
 */
public class AuUtil
{

	/**
	 * Constructor declaration
	 *
	 * @see
	 */
	public AuUtil() {}

	/**
	 * Method declaration
	 *
	 *
	 * @param os
	 *
	 * @return
	 * @see
	 */
	public static String getLineSeparator(boolean os)
	{
		if (os)
		{
			return System.getProperties().getProperty("line.separator");
		}
		else
		{
			return "\r\n";
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
	public static String getCharValues(String value)
	{
		String  temp = "";

		for (int i = 0; i < value.length(); i++)
		{
			temp += "," + (int) value.charAt(i);
		}

		if (value.length() > 0)
		{
			value = temp.substring(1);
		}
		else
		{
			value = temp;
		}

		return (value);
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
	public static boolean isEmpty(String value)
	{
		if (value == null)
		{
			return true;
		}

		if (value.trim().equals(""))
		{
			return true;
		}

		return false;
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param count
	 *
	 * @return
	 * @see
	 */
	public static String repeat(String value, int count)
	{
		if (AuUtil.isEmpty(value))
		{
			return "";
		}
		else
		{
			StringBuffer	sb = new StringBuffer();

			for (int i = 0; i < count; i++)
			{
				sb.append(value);
			}

			return sb.toString();
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param prefix
	 * @param suffix
	 * @param force
	 *
	 * @return
	 * @see
	 */
	public static String between(String value, String prefix, String suffix, boolean force)
	{
		StringBuffer	sb = new StringBuffer();

		if (force ||!value.startsWith(prefix))
		{
			sb.append(prefix);
		}

		sb.append(value);

		if (force ||!value.endsWith(suffix))
		{
			sb.append(suffix);
		}

		return sb.toString();
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param chars
	 *
	 * @return
	 * @see
	 */
	public static String removeChars(String value, String chars)
	{
		try
		{
			StringTokenizer st = new StringTokenizer(value, chars);

			if (st.countTokens() == 0)
			{
				return value;
			}
			else
			{
				String			token = null;
				StringBuffer	sb = new StringBuffer();

				while (st.hasMoreTokens())
				{
					token = st.nextToken();

					sb.append(token);
				}

				return sb.toString();
			}
		}
		catch (Exception e)
		{
			return value;
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param source
	 * @param search
	 * @param replace
	 *
	 * @return
	 * @see
	 */
	public static String replace(String source, String search, String replace)
	{
		int		spot;
		String  returnString;
		String  origSource = new String(source);

		spot = source.indexOf(search);

		if (spot > -1)
		{
			returnString = "";
		}
		else
		{
			returnString = source;
		}

		while (spot > -1)
		{
			if (spot == source.length() + 1)
			{
				returnString = returnString.concat(source.substring(0, source.length() - 1).concat(replace));
				source = "";
			}
			else if (spot > 0)
			{
				returnString = returnString.concat(source.substring(0, spot).concat(replace));
				source = source.substring(spot + search.length(), source.length());
			}
			else
			{
				returnString = returnString.concat(replace);
				source = source.substring(spot + search.length(), source.length());
			}

			spot = source.indexOf(search);
		}

		if (!source.equals(origSource))
		{
			return returnString.concat(source);
		}
		else
		{
			return returnString;
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param vars
	 * @param delim
	 *
	 * @return
	 * @see
	 */
	public static String replace(String value, Properties vars, String delim)
	{
		try
		{
			StringTokenizer st = new StringTokenizer(value, delim);

			if (st.countTokens() == 0)
			{
				return value;
			}
			else
			{
				String			token = null;
				StringBuffer	sb = new StringBuffer();

				while (st.hasMoreTokens())
				{
					token = st.nextToken();

					String  var = vars.getProperty(token, null);

					if (var == null)
					{
						var = vars.getProperty(delim + token + delim, null);
					}

					if (var == null)
					{
						sb.append(token);
					}
					else
					{
						sb.append(var);
					}
				}

				return sb.toString();
			}
		}
		catch (Exception e)
		{
			return value;
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param trim
	 * @see
	 */
	public static void trimRight(StringBuffer value, String trim)
	{
		if (value.toString().endsWith(trim))
		{
			value.setLength(value.length() - trim.length());
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param trim
	 *
	 * @return
	 * @see
	 */
	public static String trimRight(String value, String trim)
	{
		if (value.endsWith(trim))
		{
			value = value.substring(0, value.length() - trim.length());
		}

		return value;
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param trim
	 *
	 * @return
	 * @see
	 */
	public static String trim(String value, String trim)
	{
		value = trimLeft(value, trim);
		value = trimRight(value, trim);

		return value;
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param trim
	 *
	 * @return
	 * @see
	 */
	public static String trimLeft(String value, String trim)
	{
		if (value.startsWith(trim))
		{
			value = value.substring(trim.length());
		}

		return value;
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param pad
	 * @param length
	 *
	 * @return
	 * @see
	 */
	public static String padLeft(String value, String pad, int length)
	{
		int diff = length - value.length();

		if (diff > 0)
		{
			return repeat(pad, diff) + value;
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
	 * @param value
	 * @param len
	 *
	 * @return
	 * @see
	 */
	public static String leftString(String value, int len)
	{
		if (value == null)
		{
			return "";
		}
		else
		{
			if (len > value.length())
			{
				return value;
			}
			else
			{
				return value.substring(0, len);
			}
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param pad
	 * @param length
	 *
	 * @return
	 * @see
	 */
	public static String padRight(String value, String pad, int length)
	{
		int diff = length - value.length();

		if (diff > 0)
		{
			return value + repeat(pad, diff);
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
	 * @param t
	 *
	 * @return
	 * @see
	 */
	public static String getPrintStackTrace(Throwable t)
	{
		StringWriter	sw = new StringWriter();
		PrintWriter		pw = new PrintWriter(sw);

		t.printStackTrace(pw);

		return sw.toString();
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
	public static boolean isNumber(String value)
	{
		try
		{
			Long.parseLong(value);

			return true;
		}
		catch (Exception e)
		{
			return false;
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
	public static boolean isEven(long value)
	{
		return ((value % 2) == 0);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param delim
	 *
	 * @return
	 * @see
	 */
	public static String extractParent(String value, String delim)
	{
		int index = value.lastIndexOf(delim);

		if (index == -1)
		{
			return "";
		}
		else
		{
			if (index == 0)
			{
				if (value.length() == delim.length())
				{
					return "";
				}
				else
				{
					return delim;
				}
			}
			else
			{
				return value.substring(0, index);
			}
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param value
	 * @param delim
	 *
	 * @return
	 * @see
	 */
	public static String extractFirst(String value, String delim)
	{
		int index = value.indexOf(delim);

		if (index == -1)
		{
			return value;
		}
		else
		{
			if (index == 0)
			{
				return "";
			}
			else
			{
				return value.substring(0, index);
			}
		}
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param find
	 * @param value
	 *
	 * @return
	 * @see
	 */
	public static boolean in(String find, String value)
	{
		return (in(find, value, ","));
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param find
	 * @param value
	 * @param delims
	 *
	 * @return
	 * @see
	 */
	public static boolean in(String find, String value, String delims)
	{
		StringTokenizer st = new StringTokenizer(value, delims);
		String			token = "";

		while (st.hasMoreTokens())
		{
			token = st.nextToken();

			if (token.equals(find))
			{
				return (true);
			}
		}

		return false;
	}

}

