package hu.dekortrade.client.raktar.kesztlet;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.GWT;

public class KeszletConstants {

	private static KeszletLabels keszletLabels = GWT
			.create(KeszletLabels.class);

	public static final String RAKTAR_PAGE = "raktar_page";
	
	public static final String RAKTAR_ROVANCS = "rovancs";
	
	public static final String KESZLET_FOTIPUS = "raktar_fotipus";
	public static final String KESZLET_ALTIPUS = "raktar_altipus";
	public static final String KESZLET_CIKKSZAM = "raktar_cikkszam";
	public static final String KESZLET_SZINKOD = "raktar_szinkod";

	public static final String KESZLET_MEGNEVEZES = "raktar_megnevezes";

	public static final String KESZLET_ELORAR = "raktar_elorar";
	public static final String KESZLET_AR = "raktar_ar";
	public static final String KESZLET_AREUR = "raktar_areur";
	public static final String KESZLET_KISKARTON = "raktar_kiskarton";
	public static final String KESZLET_DARAB = "raktar_darab";
	public static final String KESZLET_TERFOGAT = "raktar_terfogat";
	public static final String KESZLET_TERFOGATLAB = "raktar_terfogatlab";
	public static final String KESZLET_BSULY = "raktar_bsuly";
	public static final String KESZLET_NSULY = "raktar_nsuly";
	public static final String KESZLET_LEIRAS = "raktar_leiras";
	public static final String KESZLET_MEGJEGYZES = "raktar_megjegyzes";
	public static final String KESZLET_MERTEKEGYSEG = "raktar_mertekegyseg";
	public static final String KESZLET_KEXPORTKARTON = "raktar_kexportkarton";
	public static final String KESZLET_KKISKARTON = "raktar_kkiskarton";
	public static final String KESZLET_KDARAB = "raktar_kdarab";
	public static final String KESZLET_MEXPORTKARTON = "raktar_mexportkarton";
	public static final String KESZLET_MKISKARTON = "raktar_mkiskarton";
	public static final String KESZLET_MDARAB = "raktar_mdarab";
	public static final String KESZLET_HELYKOD = "raktar_helykod";
	
	private static LinkedHashMap<String, String> mertekegyseg = null;

	public static LinkedHashMap<String, String> getMertekegyseg() {

		if (mertekegyseg == null) {
			mertekegyseg = new LinkedHashMap<String, String>();
			mertekegyseg.put("DB", keszletLabels.mertekegyseg_darab());
			mertekegyseg.put("SET", keszletLabels.mertekegyseg_egyseg());
		}
		return mertekegyseg;
	}

	private static LinkedHashMap<String, String> hely = null;

	public static LinkedHashMap<String, String> getHely() {
		if (hely == null) {
			hely = new LinkedHashMap<String, String>();
			hely.put("P1", keszletLabels.hely_polc1());
			hely.put("P2", keszletLabels.hely_polc2());
		}
		return hely;
	}

}
