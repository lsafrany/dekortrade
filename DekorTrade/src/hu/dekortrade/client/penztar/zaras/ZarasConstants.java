package hu.dekortrade.client.penztar.zaras;

import hu.dekortrade.shared.Constants;
import java.util.LinkedHashMap;
import com.google.gwt.core.client.GWT;

public class ZarasConstants {

	private static ZarasLabels cashCloseLabels = GWT.create(ZarasLabels.class);

	public static final String FIZETES_CEDULA = "fizetes_cedula";
	public static final String FIZETES_VEVO = "fizetes_vevo";
	public static final String FIZETES_VEVONEV = "fizetes_vevonev";
	public static final String FIZETES_TIPUS = "fizetes_tipus";
	public static final String FIZETES_MEGJEGYZES = "fizetes_megjegyzes";
	public static final String FIZETES_PENZTAROS = "fizetes_penztaros";
	public static final String FIZETES_PENZTAROSNEV = "fizetes_penztarosnev";
	public static final String FIZETES_FIZET = "fizetes_fizet";
	public static final String FIZETES_FIZETEUR = "fizetes_fizeteur";
	public static final String FIZETES_FIZETUSD = "fizetes_fizetusd";
	public static final String FIZETES_DATUM = "fizetes_datum";

	public static final String ZARASFIZETES_ZARAS = "zaras_fizetes_zaras";
	public static final String ZARASFIZETES_CEDULA = "zaras_fizetes_cedula";
	public static final String ZARASFIZETES_VEVO = "zaras_fizetes_vevo";
	public static final String ZARASFIZETES_VEVONEV = "zaras_fizetes_vevonev";
	public static final String ZARASFIZETES_TIPUS = "zaras_fizetes_tipus";
	public static final String ZARASFIZETES_MEGJEGYZES = "zaras_fizetes_megjegyzes";
	public static final String ZARASFIZETES_PENZTAROS = "zaras_fizetes_penztaros";
	public static final String ZARASFIZETES_PENZTAROSNEV = "zaras_fizetes_penztarosnev";
	public static final String ZARASFIZETES_FIZET = "zaras_fizetes_fizet";
	public static final String ZARASFIZETES_FIZETEUR = "zaras_fizetes_fizeteur";
	public static final String ZARASFIZETES_FIZETUSD = "zaras_fizetes_fizetusd";
	public static final String ZARASFIZETES_DATUM = "zaras_fizetes_datum";

	private static LinkedHashMap<String, String> fizetestipus = null;

	public static LinkedHashMap<String, String> getFizetesTipus() {

		if (fizetestipus == null) {
			fizetestipus = new LinkedHashMap<String, String>();
			fizetestipus.put(Constants.FIZETES_ELORENDELT,
					cashCloseLabels.elorendeltfizetes());
			fizetestipus.put(Constants.FIZETES_TORLESZTES,
					cashCloseLabels.torlesztes());
			fizetestipus.put(Constants.FIZETES_HAZIPENZTAR,
					cashCloseLabels.hazipenztar());
		}
		return fizetestipus;
	}

}
