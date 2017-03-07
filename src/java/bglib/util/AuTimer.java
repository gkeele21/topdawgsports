/*--- formatted by Jindent 2.1, (www.c-lab.de/~jindent) ---*/

package bglib.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Class declaration
 *
 *
 * @author
 * @version %I%, %G%
 */
public class AuTimer
{
	private static AuTimer  _timer = null;
	private Map				_map;
	private long			_startTime;

	/**
	 * Constructor declaration
	 *
	 * @see
	 */
	private AuTimer() {}

	/**
	 * Method declaration
	 *
	 * @see
	 */
	public static void init()
	{
		if (_timer == null)
		{
			_timer = new AuTimer();
		}

		_timer._map = new HashMap();
	}

	/**
	 * Method declaration
	 *
	 * @see
	 */
	public static void close()
	{
		_timer = null;
	}

	/**
	 * Method declaration
	 *
	 * @see
	 */
	public static void start()
	{
		_timer._startTime = System.currentTimeMillis();
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param name
	 * @see
	 */
	public static void start(String name)
	{
		_timer._map.put(name, AuConvert.longToString(System.currentTimeMillis()));
	}

	/**
	 * Method declaration
	 *
	 *
	 * @return
	 * @see
	 */
	public static long elapsedTime()
	{
		return (System.currentTimeMillis() - _timer._startTime);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param name
	 *
	 * @return
	 * @see
	 */
	public static long elapsedTime(String name)
	{
		long	value = AuConvert.stringToLong((String) _timer._map.get(name));

		return (System.currentTimeMillis() - value);
	}

	/**
	 * Method declaration
	 *
	 *
	 * @param millis
	 * @see
	 */
	public static void pause(long millis)
	{
		long	start = System.currentTimeMillis();

		while ((System.currentTimeMillis() - start) < millis) {}
	}

}

