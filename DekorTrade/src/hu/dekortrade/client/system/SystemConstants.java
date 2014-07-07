package hu.dekortrade.client.system;

import hu.dekortrade.client.CommonLabels;
import hu.dekortrade.shared.Constants;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.GWT;

public class SystemConstants {

	private static CommonLabels commonLabels = GWT.create(CommonLabels.class);
	
	public static final String FELHASZNALO_ROVIDNEV = "felhasznalo_rovidnev";
	public static final String FELHASZNALO_NEV = "felhasznalo_nev";
	public static final String FELHASZNALO_MENU = "felhasznalo_menu";

	public static final String JOG_NEV = "jog_nev";
	public static final String JOG_JOG = "jog_jog";
	
	private static LinkedHashMap<String, String> menu = null;
	
	public static LinkedHashMap<String, String> getMenu() {
		if (menu == null) {
			menu = new LinkedHashMap<String, String>();
			menu.put(Constants.MENU_SYSTEM, commonLabels.menu_system());
			menu.put(Constants.MENU_SYSTEM_USERS, commonLabels.menu_felhasznalok());
			menu.put(Constants.MENU_SYSTEM_SYNC, commonLabels.menu_szinkron());
			menu.put(Constants.MENU_SYSTEM_BASKET, commonLabels.menu_kosar());
			
			menu.put(Constants.MENU_BASEDATA, commonLabels.menu_basedata());
			menu.put(Constants.MENU_BASEDATA_PRODUCER, commonLabels.menu_gyartok());
			menu.put(Constants.MENU_BASEDATA_BUYER, commonLabels.menu_vevok());
			menu.put(Constants.MENU_BASEDATA_TYPEOFITEMS, commonLabels.menu_cikktipusok());
			menu.put(Constants.MENU_BASEDATA_ITEMS, commonLabels.menu_cikkek());
			
			menu.put(Constants.MENU_ORDER, commonLabels.menu_order());
			menu.put(Constants.MENU_ORDER_INTERNET, commonLabels.menu_internetorder());
			menu.put(Constants.MENU_ORDER_PRE, commonLabels.menu_preorder());
			menu.put(Constants.MENU_ORDER_FINALIZE, commonLabels.menu_finalizeorder());
			
			menu.put(Constants.MENU_CASH, commonLabels.menu_cash());
			
			menu.put(Constants.MENU_QUERY, commonLabels.menu_query());
			menu.put(Constants.MENU_QUERY_TICKET, commonLabels.menu_cedula());
		}
	
		return menu;
	}

	private static LinkedHashMap<String, String> mainmenu = null;
	
	public static LinkedHashMap<String, String> getMainMenu() {
		if (mainmenu == null) {
			mainmenu = new LinkedHashMap<String, String>();
			mainmenu.put(Constants.MENU_SYSTEM, commonLabels.menu_system());
			mainmenu.put(Constants.MENU_BASEDATA, commonLabels.menu_basedata());
			mainmenu.put(Constants.MENU_ORDER, commonLabels.menu_order());
			mainmenu.put(Constants.MENU_CASH, commonLabels.menu_cash());
		}

		return mainmenu;
	}

}
