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
	
	private static LinkedHashMap<String, String> cedulatipus = null;
	
	public static LinkedHashMap<String, String> getCedulaTipus() {

		if (cedulatipus == null) {
			cedulatipus = new LinkedHashMap<String, String>();
			cedulatipus.put(Constants.CEDULA_STATUS_ELORENDELT, commonLabels.cedulaelorendelo());
			cedulatipus.put(Constants.CEDULA_STATUS_VEGLEGESIT, commonLabels.cedulavegleges());
			cedulatipus.put(Constants.CEDULA_STATUS_FIZETENDO_ELORENDELES, commonLabels.fizetendoelorendeles());
			cedulatipus.put(Constants.CEDULA_STATUS_ELORENDELES_FIZETES, commonLabels.elorendelesfizetes());
			cedulatipus.put(Constants.CEDULA_STATUS_FIZETETT_ELORENDELES, commonLabels.fizetettelorendeles());
		}
		return cedulatipus;
	}

}
