package hu.dekortrade99.client;

public class CommonUtils {

	public static Integer intCheck(Integer value) {
		if (value == null) value = new Integer("0");
		return value;
	}

	public static Double doubleCheck(Double value) {
		if (value == null) value = new Double("0.0");
		return value;
	}

}
