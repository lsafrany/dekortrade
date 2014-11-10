package hu.dekortrade.client.kosarraktar;

import java.util.LinkedHashMap;

import com.google.gwt.core.client.GWT;

public class KosarRaktarConstants {

	private static KosarRaktarLabels kosarRaktarLabels = GWT.create(KosarRaktarLabels.class);
	
	public static final String ELORENDELO_AR = "elorendelo_artipus";
	public static final String EXPORT_AR = "export_artipus";
	public static final String PIACI_AR = "piaci_artipus";
	
	private static LinkedHashMap<String, String> artipus = null;

	public static LinkedHashMap<String, String> getArtipus() {

		if (artipus == null) {
			artipus = new LinkedHashMap<String, String>();
			artipus.put(ELORENDELO_AR,
					kosarRaktarLabels.elorendelo_ar());
			artipus.put(EXPORT_AR,
					kosarRaktarLabels.export_ar());
			artipus.put(PIACI_AR ,
					kosarRaktarLabels.piaci_ar());
		}
		return artipus;
	}
}
