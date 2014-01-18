package hu.dekortrade.client.system;

import hu.dekortrade.shared.Constants;

import java.util.LinkedHashMap;

public class SystemConstants {

	public static final String FELHASZNALO_ROVIDNEV = "felhasznalo_rovidnev";
	public static final String FELHASZNALO_NEV = "felhasznalo_nev";
	public static final String FELHASZNALO_MENU = "felhasznalo_menu";

	public static final String JOG_NEV = "jog_nev";
	public static final String JOG_JOG = "jog_jog";

	private static LinkedHashMap<String, String> menuk = null;

	public static LinkedHashMap<String, String> getMenuk() {
		if (menuk == null) {
			menuk = new LinkedHashMap<String, String>();
			menuk.put(Constants.MENU_SYSTEM, Constants.MENU_SYSTEM);
			menuk.put(Constants.MENU_BASEDATA, Constants.MENU_BASEDATA);
			menuk.put(Constants.MENU_ORDER, Constants.MENU_ORDER);
		}

		return menuk;
	}

}
