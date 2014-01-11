package hu.dekortrade.client;

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

	public static final LinkedHashMap<String, String> jelek = new LinkedHashMap<String, String>();

	public ClientConstants() {
		super();
	    jelek.put("SS","Szirm.selyemn�v/67029000 ODT");
   		jelek.put("SK","Szirm.kar�csony/95051090 ODT");
		jelek.put("SH","Szirm.h�sv�ti  /95039032 ODT");
		jelek.put("SM","Szirm.m�feny�k /67021000 ODT");
		jelek.put("SP","Szirm.porcel�n /69139010 ODT");
		jelek.put("SG","Szirm.m�a.v+gy./67021000 ODT");
		jelek.put("SB","Szirm.kos�r    /46021091 ODT");
		jelek.put("R1","RJWfullvir�g   /67029000 FLL");
		jelek.put("ST","StanleyRgyvirag/67029000 FLL");
		jelek.put("R2","RJWfullgyumi   /67021000 FLL");
		jelek.put("DS","Dekor.selyemvir/67029000 ODT");
		jelek.put("DK","Dekor.kar�csony/95051090 ODT");
		jelek.put("DH","Dekor.h�sv�ti  /95039032 ODT");
		jelek.put("DM","Dekor.m�feny�  /67021000 ODT");
		jelek.put("DP","Dekor.porcel�n /69139010 ODT");
		jelek.put("DG","Dekor.m�a.v+gy./67021000 ODT");
		jelek.put("Q1","DZRQRviragfull /67029000 FLL");
		jelek.put("DZ","Dekor.dr�tsz�r /67029000 ODT");
	    jelek.put("DT","Dekor.pap�r�ru /48194000 ODT");
	    jelek.put("Q2","DZRQRkosarfull /46021091 FLL");
	    jelek.put("HG","HongGuanValhusv/95059000 FLL");
	    jelek.put("YX","YongXinValhusv./95059000 FLL");
	    jelek.put("U�","�j Dekor �v.vir/7013100 NDT");
	    jelek.put("UB","�j Dekor kos�r/46021091 NDT");
	    jelek.put("US","�j Dekor selyemn/67029000 NDT");
	    jelek.put("UK","�j Dekor kar�cs./95051090 NDT");
	    jelek.put("UH","�j Dekor h�sv�ti/95059000 NDT");
	    jelek.put("UM","�j Dekor m�feny�/67021000 NDT");
	    jelek.put("UP","�j Dekor porcel./69139010 NDT");
	    jelek.put("UG","�j Dekor m�anyag/67021000 NDT");
	    jelek.put("UZ","�j Dekor dr�tsz./67029000 NDT");
	    jelek.put("UT","�j Dekor pap�r�./48194000 NDT");
	    jelek.put("Y1","GoldenBr.virag  /67029000 FLL");
	    jelek.put("TM","ThaichinFu Feny�k/67021000 NDT");
	    jelek.put("WK","Win Seng ker�mi1/69139010");
	    jelek.put("FK","Kar.dekor./95051090 FLL");
	    jelek.put("TJ","TaoJihongSzalmad./95051090 NDT");
	    jelek.put("UY","Gyertyat.f�m/83062990 NDT");
	    jelek.put("UC","D�szgyerty�k/34060019 NDT");
	    jelek.put("UF","K�pkeretek/44140090 NDT");
	    jelek.put("WC","Win Seng gyerty./34060011 NDT");
	    jelek.put("GB","Golden Bridge Y/67029000 NDT");
	    jelek.put("RJ","RJWang Liaoning/67029000 NDT");
	    jelek.put("GY","Giyaga Kar.szalmab+kos�r FLL");
	    jelek.put("WS","Win Seng ker.II/69139010 NDT");
	    jelek.put("FC","Fuzhou ker. /69139010 NDT");
	    jelek.put("MG","SMG ker./69139010 NDT");
	    jelek.put("QF","Dezh.Rq.vir�g/6702900 NDT");
	    jelek.put("QK","Dezh.Rq.kar�/95051090 NDT");
	    jelek.put("RK","RJWang kar�cs/95051090 NDT");
	    jelek.put("RG","RJWang gy�m+z�lds/67021000 NDT");
	    jelek.put("QB","Dezh.Rq.kos�r/46021091 NDT");
	    jelek.put("Y2","GoldenBr.Valhus/95059000 FLL");
	    jelek.put("1S","SMGkerhusVal.full/69139010 FLL");
	    jelek.put("HF","Hengq.vir�g/67029000 FLL");
	    jelek.put("MT","SMG pap�r/48082000 FLL");
	    jelek.put("WI","WinSeng ill�olaj/33012961 NDT");
	    jelek.put("TC","Candels/34060011 FLL");
	    jelek.put("FP","Ker�m.dek./69139010 FLL");
	    jelek.put("FL","FZHLancebutor/83062990 FLL");
	    jelek.put("JH","JamesHua.karvessz/95051090 FLL");
	    jelek.put("3W","3Way.kar.disz/95051090 FLL");
	    jelek.put("V5","Shant.virag/67029000 FLL");
	    jelek.put("MP","Marker pen/9608200000 NDT");
	    jelek.put("FS","Vir�gok/67029000 FLL");
	    jelek.put("FH","H�sv�ti dekor./9503903200FLL");
	    jelek.put("FM","M�feny�/67021000 FLL");
	    jelek.put("FG","M�a+gy�m./67021000 FLL");
	    jelek.put("FD","Szalmadekor/95034990 FLL");
	    jelek.put("FT","Papirtasak/480820000FLL");
	    jelek.put("FX","Textilt�ska/4202229090FLL");
	    jelek.put("FY","Kisb�tor/94036090000FLL");
	    jelek.put("FB","Kosarak/4602109100FLL");
	    jelek.put("F�","M�cstart.�v.vas/8306299000FLL");
	    jelek.put("FF","Fadobozok/4420101900FLL");
	    jelek.put("FR","Konyhaiker./6912001090FLL");
	    jelek.put("FA","Falik�p/4414009000FLL");
	    jelek.put("RR","Ragr�d/3506919090FLL");
	}

}
