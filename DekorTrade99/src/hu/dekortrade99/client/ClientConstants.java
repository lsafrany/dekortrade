package hu.dekortrade99.client;

import java.util.LinkedHashMap;

public class ClientConstants {

	public static final String SERVER_ERROR = "server_error";
	public static final String SERVER_SQLERROR = "server_sqlerror";

	public static final String FETCH_SIZE_ERROR = "red";

	public static final String COOKIE = "DekorTrade99";
	public static final int COOKIE_EXPIRE = 30;

	public static final int PROGRESS_WIDTH = 180;
	public static final int PROGRESS_HEIGTH = 29;
	public static final int PROGRESS_SCHEDULE = 500;
	public static final int PROGRESS_START = 0;
	public static final int PROGRESS_STOP = -1;

	public static final int CACHE = 3600;

	private static LinkedHashMap<String, String> jelek = null;

	public static LinkedHashMap<String, String> getJelek() {
		if (jelek == null) {
			jelek = new LinkedHashMap<String, String>();
			jelek.put("SS", "Szirm.selyemnáv/67029000 ODT");
			jelek.put("SK", "Szirm.karácsony/95051090 ODT");
			jelek.put("SH", "Szirm.húsvéti  /95039032 ODT");
			jelek.put("SM", "Szirm.műfenyők /67021000 ODT");
			jelek.put("SP", "Szirm.porcelán /69139010 ODT");
			jelek.put("SG", "Szirm.müa.vágy./67021000 ODT");
			jelek.put("SB", "Szirm.kosár    /46021091 ODT");
			jelek.put("R1", "RJWfullvirág   /67029000 FLL");
			jelek.put("ST", "StanleyRgyvirag/67029000 FLL");
			jelek.put("R2", "RJWfullgyumi   /67021000 FLL");
			jelek.put("DS", "Dekor.selyemvir/67029000 ODT");
			jelek.put("DK", "Dekor.karácsony/95051090 ODT");
			jelek.put("DH", "Dekor.húsvéti  /95039032 ODT");
			jelek.put("DM", "Dekor.műfenyő  /67021000 ODT");
			jelek.put("DP", "Dekor.porcelán /69139010 ODT");
			jelek.put("DG", "Dekor.müa.v+gy./67021000 ODT");
			jelek.put("Q1", "DZRQRviragfull /67029000 FLL");
			jelek.put("DZ", "Dekor.drótszár /67029000 ODT");
			jelek.put("DT", "Dekor.papíráru /48194000 ODT");
			jelek.put("Q2", "DZRQRkosarfull /46021091 FLL");
			jelek.put("HG", "HongGuanValhusv/95059000 FLL");
			jelek.put("YX", "YongXinValhusv./95059000 FLL");
			jelek.put("UÜ", "Új Dekor üv.vir/7013100 NDT");
			jelek.put("UB", "Új Dekor kosár/46021091 NDT");
			jelek.put("US", "Új Dekor selyemn/67029000 NDT");
			jelek.put("UK", "Új Dekor karács./95051090 NDT");
			jelek.put("UH", "Új Dekor húsvéti/95059000 NDT");
			jelek.put("UM", "Új Dekor műfenyő/67021000 NDT");
			jelek.put("UP", "Új Dekor porcel./69139010 NDT");
			jelek.put("UG", "Új Dekor műanyag/67021000 NDT");
			jelek.put("UZ", "Új Dekor drótsz./67029000 NDT");
			jelek.put("UT", "Új Dekor papír./48194000 NDT");
			jelek.put("Y1", "GoldenBr.virag  /67029000 FLL");
			jelek.put("TM", "ThaichinFu Fenyők/67021000 NDT");
			jelek.put("WK", "Win Seng kerámi1/69139010");
			jelek.put("FK", "Kar.dekor./95051090 FLL");
			jelek.put("TJ", "TaoJihongSzalmad./95051090 NDT");
			jelek.put("UY", "Gyertyat.fém/83062990 NDT");
			jelek.put("UC", "Díszgyertyák/34060019 NDT");
			jelek.put("UF", "Képkeretek/44140090 NDT");
			jelek.put("WC", "Win Seng gyerty./34060011 NDT");
			jelek.put("GB", "Golden Bridge Y/67029000 NDT");
			jelek.put("RJ", "RJWang Liaoning/67029000 NDT");
			jelek.put("GY", "Giyaga Kar.szalmab+kosár FLL");
			jelek.put("WS", "Win Seng ker.II/69139010 NDT");
			jelek.put("FC", "Fuzhou ker. /69139010 NDT");
			jelek.put("MG", "SMG ker./69139010 NDT");
			jelek.put("QF", "Dezh.Rq.virág/6702900 NDT");
			jelek.put("QK", "Dezh.Rq.kará/95051090 NDT");
			jelek.put("RK", "RJWang karács/95051090 NDT");
			jelek.put("RG", "RJWang gyüm+zölds/67021000 NDT");
			jelek.put("QB", "Dezh.Rq.kosár/46021091 NDT");
			jelek.put("Y2", "GoldenBr.Valhus/95059000 FLL");
			jelek.put("1S", "SMGkerhusVal.full/69139010 FLL");
			jelek.put("HF", "Hengq.virág/67029000 FLL");
			jelek.put("MT", "SMG papír/48082000 FLL");
			jelek.put("WI", "WinSeng illóolaj/33012961 NDT");
			jelek.put("TC", "Candels/34060011 FLL");
			jelek.put("FP", "Kerám.dek./69139010 FLL");
			jelek.put("FL", "FZHLancebutor/83062990 FLL");
			jelek.put("JH", "JamesHua.karvessz/95051090 FLL");
			jelek.put("3W", "3Way.kar.disz/95051090 FLL");
			jelek.put("V5", "Shant.virag/67029000 FLL");
			jelek.put("MP", "Marker pen/9608200000 NDT");
			jelek.put("FS", "Virágok/67029000 FLL");
			jelek.put("FH", "Húsvéti dekor./9503903200FLL");
			jelek.put("FM", "Műfenyő/67021000 FLL");
			jelek.put("FG", "Műa+gyöm./67021000 FLL");
			jelek.put("FD", "Szalmadekor/95034990 FLL");
			jelek.put("FT", "Papirtasak/480820000FLL");
			jelek.put("FX", "Textiltáska/4202229090FLL");
			jelek.put("FY", "Kisbútor/94036090000FLL");
			jelek.put("FB", "Kosarak/4602109100FLL");
			jelek.put("FÜ", "Mécstart.üv.vas/8306299000FLL");
			jelek.put("FF", "Fadobozok/4420101900FLL");
			jelek.put("FR", "Konyhaiker./6912001090FLL");
			jelek.put("FA", "Falikép/4414009000FLL");
			jelek.put("RR", "Ragrúd/3506919090FLL");
		}

		return jelek;
	}

}
