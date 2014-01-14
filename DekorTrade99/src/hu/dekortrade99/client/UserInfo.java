package hu.dekortrade99.client;

import java.util.ArrayList;

public class UserInfo {

	public static String userId = "";

	public static Integer orderID = null;

	public static ArrayList<String> order = null;

	public static Integer archiveID = null;

	public static ArrayList<String> archive = null;

	public static void clearValues() {
		userId = "";
		orderID = null;
		order = null;
		archiveID = null;
		archive = null;
	}
}
