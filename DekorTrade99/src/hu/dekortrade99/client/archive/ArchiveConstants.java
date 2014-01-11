package hu.dekortrade99.client.archive;

import java.util.LinkedHashMap;

public class ArchiveConstants {

	public static final String RENDELT_ROVIDNEV = "rendelt_rovidnev";
	public static final String RENDELT_RENDELES = "rendelt_rendeles";
	public static final String RENDELT_DATUM = "rendelt_datum";
	public static final String RENDELT_STATUSZ = "rendelt_statusz";

	public static final String RENDELTCIKK_ROVIDNEV = "rendeltcikk_rovidnev";
	public static final String RENDELTCIKK_RENDELES = "rendeltcikk_rendeles";
	public static final String RENDELTCIKK_CIKKSZAM = "rendeltcikk_cikkszam";
	public static final String RENDELTCIKK_EXPORTKARTON = "rendeltcikk_exportkarton";

	private static LinkedHashMap<String, String> statusz = null;
	
	public static LinkedHashMap<String, String> getStatusz() {
		if (statusz == null) {
			statusz = new LinkedHashMap<String, String>();
		    statusz.put("PENDING","Elküldött");
	   		statusz.put("PROCESSED","Feldolgozott");
		}
			
		return statusz;
	}

}
