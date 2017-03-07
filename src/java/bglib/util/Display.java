package bglib.util;

import java.text.DecimalFormat;

public class Display {

	private Display() {

	}

	public static String noDecimal(Object x) {

		DecimalFormat df = new DecimalFormat(",##0");
		return df.format(x.toString());

	}

	public static String noDecimal(String x) {

		DecimalFormat df = new DecimalFormat(",##0");
		return df.format(x);

	}

	public static String noDecimal(double x) {

		DecimalFormat df = new DecimalFormat(",##0");
		return df.format(x);

	}

	public static String noDecimal(int x) {

		DecimalFormat df = new DecimalFormat(",##0");
		return df.format(x);

	}

	public static String oneDecimal(Object x) {

		DecimalFormat df = new DecimalFormat(",##0.0");
		return df.format(x.toString());

	}

	public static String oneDecimal(String x) {

		DecimalFormat df = new DecimalFormat(",##0.0");
		return df.format(x);

	}

	public static String oneDecimal(double x) {

		DecimalFormat df = new DecimalFormat(",##0.0");
		return df.format(x);

	}

	public static String oneDecimal(int x) {

		DecimalFormat df = new DecimalFormat(",##0.0");
		return df.format(x);

	}

	public static String twoDecimals(Object x) {

		DecimalFormat df = new DecimalFormat(",##0.00");
		return df.format(x.toString());

	}

	public static String twoDecimals(String x) {

		DecimalFormat df = new DecimalFormat(",##0.00");
		return df.format(x);

	}

	public static String twoDecimals(double x) {

		DecimalFormat df = new DecimalFormat(",##0.00");
		return df.format(x);

	}

    public static String twoDecimalsNoCommas(double x) {
        DecimalFormat df = new DecimalFormat("##0.00");
        return df.format(x);
    }

    public static String twoDecimals(int x) {

		DecimalFormat df = new DecimalFormat(",##0.00");
		return df.format(x);

	}

	public static String threeDecimals(Object x) {

		DecimalFormat df = new DecimalFormat(",##0.000");
		return df.format(x.toString());

	}

	public static String threeDecimals(String x) {

		DecimalFormat df = new DecimalFormat(",##0.000");
		return df.format(x);

	}

	public static String threeDecimals(double x) {

		DecimalFormat df = new DecimalFormat(",##0.000");
		return df.format(x);

	}

	public static String threeDecimals(int x) {

		DecimalFormat df = new DecimalFormat(",##0.000");
		return df.format(x);

	}

}
