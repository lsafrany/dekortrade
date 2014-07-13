package hu.dekortrade.client;

import java.util.ArrayList;

public class UserInfo {

	public static String userId = "";

	public static int defaultTab = 0;

	public static ArrayList<String> mainMenu = new ArrayList<String>();

	public static ArrayList<String> menu = new ArrayList<String>();

	public static void clearValues() {
		userId = "";
		mainMenu.clear();
		menu.clear();
	}
}
