package hu.dekortrade.client.basedata.vevo;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.GWT;

public class VevoConstants {

	private static VevoLabels vevoLabels = GWT.create(VevoLabels.class);
	
	public static final String VEVO_ROVIDNEV = "vevo_rovidnev";
	public static final String VEVO_TIPUS = "vevo_tipus";
	public static final String VEVO_NEV = "vevo_nev";
	public static final String VEVO_CIM = "vevo_cim";
	public static final String VEVO_ELERHETOSEG = "vevo_elerhetoseg";
	public static final String VEVO_INTERNET = "vevo_internet";

	public static final String VEVO_EGYENLEG_USD = "vevo_egyenleg_usd";
	public static final String VEVO_EGYENLEG_EUR = "vevo_egyenleg_eur";
	public static final String VEVO_EGYENLEG_HUF = "vevo_egyenleg_huf";
	
	public static final String VEVO_TAROLASIDIJ = "vevo_tarolasidij";
	public static final String VEVO_ELOLEG = "vevo_eloleg";
	
	public static final String VEVO_BANKSZAMLASZAM = "vevo_bankszamlaszam";
	public static final String VEVO_EUADOSZAM = "vevo_euadoszam";
	
	public static final String VEVO_ELORARKEDVEZMENY = "vevo_elorarkedvezmeny";
	public static final String VEVO_AJANLOTTARKEDVEZMENY = "vevo_ajanlottarkedvezmeny";
	
	public static final String VEVO_ORSZAG = "vevo_orszag";
	
	public static final String VEVO_MEGJEGYZES = "vevo_megjegyzes";
	
	private static LinkedHashMap<String, String> tipus = null;
	
	public static LinkedHashMap<String, String> getTipus() {

		if (tipus == null) {
			tipus = new LinkedHashMap<String, String>();
			tipus.put("BELFOLDI", vevoLabels.belfoldi());
			tipus.put("ELORENDELO", vevoLabels.elorendelo());
			tipus.put("EXPORT", vevoLabels.export());
		}
		return tipus;
	}

	private static LinkedHashMap<String, String> orszag = null;
	
	public static LinkedHashMap<String, String> getOrszag() {

		if (orszag == null) {
			orszag = new LinkedHashMap<String, String>();
			orszag.put("hu", vevoLabels.magyarorszag());
		}
		return orszag;
	}

}
