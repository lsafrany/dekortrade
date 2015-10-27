package hu.dekortrade.client;

import hu.dekortrade.shared.Constants;

public class CommonUtils {

	public static Integer intCheck(Integer value) {
		if (value == null) value = new Integer("0");
		return value;
	}

	public static Long longCheck(Long value) {
		if (value == null) value = new Long("0");
		return value;
	}

	public static Double doubleCheck(Double value) {
		if (value == null) value = new Double("0.0");
		else value = Constants.round(value, 5);
		return value;
	}

}
