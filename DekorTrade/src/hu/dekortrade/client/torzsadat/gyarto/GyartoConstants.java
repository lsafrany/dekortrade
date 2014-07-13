package hu.dekortrade.client.torzsadat.gyarto;

import java.util.LinkedHashMap;

public class GyartoConstants {

	public static final String GYARTO_KOD = "gyarto_kod";
	public static final String GYARTO_NEV = "gyarto_nev";
	public static final String GYARTO_CIM = "gyarto_cim";
	public static final String GYARTO_ELERHETOSEG = "gyarto_elerhetoseg";
	public static final String GYARTO_SWIFTKOD = "gyarto_swiftkod";
	public static final String GYARTO_BANKADAT = "gyarto_bankadat";
	public static final String GYARTO_SZAMLASZAM = "gyarto_szamlaszam";
	public static final String GYARTO_EGYENLEG = "gyarto_egyenleg";
	public static final String GYARTO_KEDVEZMENY = "gyarto_kedvezmeny";
	public static final String GYARTO_MEGJEGYZES = "gyarto_megjegyzes";

	private static LinkedHashMap<String, String> kedvezmeny = null;

	public static LinkedHashMap<String, String> getKedvezmeny() {

		if (kedvezmeny == null) {
			kedvezmeny = new LinkedHashMap<String, String>();
			kedvezmeny.put("1", "1");
			kedvezmeny.put("0,6", "0,6");
			kedvezmeny.put("0,4", "0,4");
		}
		return kedvezmeny;
	}

}
