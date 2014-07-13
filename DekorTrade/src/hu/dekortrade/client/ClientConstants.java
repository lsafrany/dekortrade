package hu.dekortrade.client;

import hu.dekortrade.shared.Constants;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.GWT;

public class ClientConstants {

	private static CommonLabels commonLabels = GWT.create(CommonLabels.class);

	public static final String SERVER_ERROR = "server_error";
	public static final String SERVER_SQLERROR = "server_sqlerror";
	public static final String DATA_ERROR = "data_error";

	public static final String FETCH_SIZE_ERROR = "red";

	public static final String COOKIE = "DekorTrade";
	public static final int COOKIE_EXPIRE = 30;

	public static final int PROGRESS_WIDTH = 180;
	public static final int PROGRESS_HEIGTH = 29;
	public static final int PROGRESS_SCHEDULE = 500;
	public static final int PROGRESS_START = 0;
	public static final int PROGRESS_STOP = -1;

	public static final int CACHE = 3600;

	public static final String INTERNET_IMPORTED = "IMPORTED";
	public static final String INTERNET_ELORENDEL = "ELORENDEL";

	private static LinkedHashMap<String, String> menu = null;

	public static LinkedHashMap<String, String> getMenu() {
		if (menu == null) {
			menu = new LinkedHashMap<String, String>();
			menu.put(Constants.MENU_RENDSZER, commonLabels.menu_rendszer());
			menu.put(Constants.MENU_RENDSZER_FELHASZNALO,
					commonLabels.menu_rendszer_felhasznalo());
			menu.put(Constants.MENU_RENDSZER_SZINKRON,
					commonLabels.menu_rendszer_szinkron());
			menu.put(Constants.MENU_RENDSZER_KOSAR,
					commonLabels.menu_rendszer_kosar());

			menu.put(Constants.MENU_TORZSADAT, commonLabels.menu_torzsadat());
			menu.put(Constants.MENU_TORZSADAT_GYARTO,
					commonLabels.menu_torzsadat_gyarto());
			menu.put(Constants.MENU_TORZSADAT_VEVO,
					commonLabels.menu_torzsadat_vevo());
			menu.put(Constants.MENU_TORZSADAT_CIKKTIPUS,
					commonLabels.menu_torzsadat_cikktipus());
			menu.put(Constants.MENU_TORZSADAT_CIKKTORZS,
					commonLabels.menu_torzsadat_cikktorzs());

			menu.put(Constants.MENU_RENDELES, commonLabels.menu_rendeles());
			menu.put(Constants.MENU_RENDELES_INTERNET,
					commonLabels.menu_rendeles_internet());
			menu.put(Constants.MENU_RENDELES_ELORENDELES,
					commonLabels.menu_rendeles_elorendeles());
			menu.put(Constants.MENU_RENDELES_VEGLEGESITES,
					commonLabels.menu_rendeles_veglegesites());
			menu.put(Constants.MENU_RENDELES_MEGRENDELES,
					commonLabels.menu_rendeles_megrendeles());

			menu.put(Constants.MENU_RAKTAR, commonLabels.menu_raktar());
			menu.put(Constants.MENU_RAKTAR_BESZALLITAS,
					commonLabels.menu_raktar_beszallitas());
			menu.put(Constants.MENU_RAKTAR_KESZLET,
					commonLabels.menu_raktar_keszlet());
			menu.put(Constants.MENU_RAKTAR_KIADAS,
					commonLabels.menu_raktar_kiadas());

			menu.put(Constants.MENU_ELADAS, commonLabels.menu_eladas());

			menu.put(Constants.MENU_PENZTAR, commonLabels.menu_penztar());
			menu.put(Constants.MENU_PENZTAR_FIZETES,
					commonLabels.menu_penztar_fizetes());
			menu.put(Constants.MENU_PENZTAR_TORLESZTES,
					commonLabels.menu_penztar_torlesztes());
			menu.put(Constants.MENU_PENZTAR_HAZI,
					commonLabels.menu_penztar_hazi());
			menu.put(Constants.MENU_PENZTAR_ZARAS,
					commonLabels.menu_penztar_zaras());

			menu.put(Constants.MENU_LEKERDEZES, commonLabels.menu_lekerdezes());
			menu.put(Constants.MENU_LEKERDEZES_CEDULAK,
					commonLabels.menu_lekerdezes_cedulak());
			menu.put(Constants.MENU_LEKERDEZES_ZARASOK,
					commonLabels.menu_lekerdezes_zarasok());
			menu.put(Constants.MENU_LEKERDEZES_TORLESZTESEK,
					commonLabels.menu_lekerdezes_torlesztesek());

		}

		return menu;
	}

	private static LinkedHashMap<String, String> mainmenu = null;

	public static LinkedHashMap<String, String> getMainMenu() {
		if (mainmenu == null) {
			mainmenu = new LinkedHashMap<String, String>();
			mainmenu.put(Constants.MENU_RENDSZER, commonLabels.menu_rendszer());
			mainmenu.put(Constants.MENU_TORZSADAT,
					commonLabels.menu_torzsadat());
			mainmenu.put(Constants.MENU_RENDELES, commonLabels.menu_rendeles());
			mainmenu.put(Constants.MENU_PENZTAR, commonLabels.menu_penztar());
		}

		return mainmenu;
	}

	private static LinkedHashMap<String, String> cedulatipus = null;

	public static LinkedHashMap<String, String> getCedulaTipus() {

		if (cedulatipus == null) {
			cedulatipus = new LinkedHashMap<String, String>();
			cedulatipus.put(Constants.CEDULA_STATUSZ_ELORENDELT,
					commonLabels.cedulaelorendelo());
			cedulatipus.put(Constants.CEDULA_STATUSZ_VEGLEGESIT,
					commonLabels.cedulavegleges());
			cedulatipus.put(Constants.CEDULA_STATUSZ_FIZETENDO_ELORENDELES,
					commonLabels.fizetendoelorendeles());
			cedulatipus.put(Constants.CEDULA_STATUSZ_ELORENDELES_FIZETES,
					commonLabels.elorendelesfizetes());
			cedulatipus.put(Constants.CEDULA_STATUSZ_FIZETETT_ELORENDELES,
					commonLabels.fizetettelorendeles());
		}
		return cedulatipus;
	}

}
