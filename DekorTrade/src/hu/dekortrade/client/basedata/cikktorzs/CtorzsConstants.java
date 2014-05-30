package hu.dekortrade.client.basedata.cikktorzs;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.GWT;

public class CtorzsConstants {

	private static CtorzsLabels ctorzsLabels = GWT.create(CtorzsLabels.class);
	
	public static final String CTORZS_PAGE = "ctorzs_page";

	public static final String CIKK_FOTIPUS = "ctorzs_fotipus";
	public static final String CIKK_ALTIPUS = "ctorzs_altipus";
	public static final String CIKK_GYARTO = "ctorzs_gyarto";
	public static final String CIKK_GYARTOCIKKSZAM = "ctorzs_gyartocikkszam";
	public static final String CIKK_CIKKSZAM = "ctorzs_cikkszam";
	public static final String CIKK_SZINKOD = "ctorzs_szinkod";
	public static final String CIKK_FELVITELTOL = "ctorzs_felviteltol";
	public static final String CIKK_FELVITELIG = "ctorzs_felvitelig";
	public static final String CIKK_LEJARATTOL = "ctorzs_lejarattol";
	public static final String CIKK_LEJARATIG = "ctorzs_lejaratig";
	
	public static final String CIKK_MEGNEVEZES = "ctorzs_megnevezes";
	public static final String CIKK_VAMTARIFASZAM = "ctorzs_vamtarifaszam";

	public static final String CIKK_FOB = "ctorzs_fob";
	public static final String CIKK_SZALLITAS = "ctorzs_szallitas";
	public static final String CIKK_DDU = "ctorzs_ddu";
	public static final String CIKK_ERSZ = "ctorzs_ersz";
	public static final String CIKK_ELORAR = "ctorzs_elorar";

	public static final String CIKK_UJFOB = "ctorzs_ujfob";
	public static final String CIKK_UJSZALLITAS = "ctorzs_ujszallitas";
	public static final String CIKK_UJDDU = "ctorzs_ujddu";
	public static final String CIKK_UJERSZ = "ctorzs_ujersz";
	public static final String CIKK_UJELORAR = "ctorzs_ujelorar";

	public static final String CIKK_AR = "ctorzs_ar";
	public static final String CIKK_AREUR = "ctorzs_areur";
	public static final String CIKK_ARSZORZO = "ctorzs_arszorzo";
	public static final String CIKK_KISKARTON = "ctorzs_kiskarton";
	public static final String CIKK_DARAB = "ctorzs_darab";
	public static final String CIKK_TERFOGAT = "ctorzs_terfogat";
	public static final String CIKK_TERFOGATLAB = "ctorzs_terfogatlab";
	public static final String CIKK_BSULY = "ctorzs_bsuly";
	public static final String CIKK_NSULY = "ctorzs_nsuly";
	public static final String CIKK_LEIRAS = "ctorzs_leiras";
	public static final String CIKK_MEGJEGYZES = "ctorzs_megjegyzes";
	
	public static final String CIKK_AKCIOS = "ctorzs_akcios";
	public static final String CIKK_MERTEKEGYSEG = "ctorzs_mertekegyseg";
	
	public static final String CIKK_KEPEK = "ctorzs_kepek";

	public static final String KEP_SORSZAM = "kep_sorszam";
	public static final String KEP_KEP = "kep_kep";
	
	private static LinkedHashMap<String, String> mertekegyseg = null;
	
	public static LinkedHashMap<String, String> getMertekegyseg() {

		if (mertekegyseg == null) {
			mertekegyseg = new LinkedHashMap<String, String>();
			mertekegyseg.put("DB", ctorzsLabels.mertekegyseg_darab());
			mertekegyseg.put("SET", ctorzsLabels.mertekegyseg_egyseg());
		}
		return mertekegyseg;
	}

}
