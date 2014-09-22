package hu.dekortrade.client;

import hu.dekortrade.shared.serialized.BeszallitottcikkSer;
import hu.dekortrade.shared.serialized.CedulaSer;
import hu.dekortrade.shared.serialized.CedulacikkSer;
import hu.dekortrade.shared.serialized.CikkSelectsSer;
import hu.dekortrade.shared.serialized.CikkSer;
import hu.dekortrade.shared.serialized.CikkaltipusSer;
import hu.dekortrade.shared.serialized.CikkfotipusSer;
import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.FizetesSer;
import hu.dekortrade.shared.serialized.GyartoSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.KosarSer;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SzinkronSer;
import hu.dekortrade.shared.serialized.UploadSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoKosarSer;
import hu.dekortrade.shared.serialized.VevoSer;
import hu.dekortrade.shared.serialized.ZarasEgyenlegSer;
import hu.dekortrade.shared.serialized.ZarasSer;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DekorTradeServiceAsync {

	void getUser(String user, String password, AsyncCallback<UserSer> callback);

	void setPassword(String user, String password,
			@SuppressWarnings("rawtypes") AsyncCallback callback);

	void getJog(String rovidnev, AsyncCallback<List<JogSer>> callback);

	void getFelhasznalo(AsyncCallback<List<FelhasznaloSer>> callback);

	void addFelhasznalo(FelhasznaloSer felhasznaloSer,
			AsyncCallback<FelhasznaloSer> asyncCallback);

	void updateFelhasznalo(FelhasznaloSer felhasznaloSer,
			AsyncCallback<FelhasznaloSer> asyncCallback);

	void setFelhasznaloJelszo(String rovidnev, AsyncCallback<String> callback);

	void removeFelhasznalo(FelhasznaloSer felhasznaloSer,
			AsyncCallback<FelhasznaloSer> asyncCallback);

	void updateJog(String rovidnev, JogSer jogSer,
			AsyncCallback<JogSer> callback);

	void getGyarto(AsyncCallback<List<GyartoSer>> callback);

	void addGyarto(GyartoSer szallitoSer, AsyncCallback<GyartoSer> asyncCallback);

	void updateGyarto(GyartoSer szallitoSer,
			AsyncCallback<GyartoSer> asyncCallback);

	void removeGyarto(GyartoSer szallitoSer,
			AsyncCallback<GyartoSer> asyncCallback);

	void getVevo(AsyncCallback<List<VevoSer>> callback);

	void addVevo(VevoSer vevoSer, AsyncCallback<VevoSer> asyncCallback);

	void updateVevo(VevoSer vevoSer, AsyncCallback<VevoSer> asyncCallback);

	void setVevoJelszo(String rovidnev, AsyncCallback<String> callback);

	void removeVevo(VevoSer vevoSer, AsyncCallback<VevoSer> asyncCallback);

	void getCikk(int page, String fotipus, String altipus, String cikkszam,
			AsyncCallback<List<CikkSer>> asyncCallback);

	void addCikk(CikkSer cikkSer, AsyncCallback<CikkSer> asyncCallback);

	void updateCikk(CikkSer cikkSer, AsyncCallback<CikkSer> asyncCallback);

	void removeCikk(CikkSer ctorzsSer, AsyncCallback<CikkSer> asyncCallback);

	void getRendelt(AsyncCallback<List<RendeltSer>> asyncCallback);

	void getRendeltcikk(String rovidnev, String rendeles,
			AsyncCallback<List<RendeltcikkSer>> asyncCallback);

	void szinkron(AsyncCallback<SzinkronSer> callback);

	void teljesszinkron(AsyncCallback<String> callback);

	void initUploadFileStatus(AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void getUploadFileStatus(AsyncCallback<UploadSer> callback)
			throws IllegalArgumentException;

	void getKep(String cikkszam, String szinkod,
			AsyncCallback<List<String>> asyncCallback);

	void removeKep(String cikkszam, String szinkod, String sorszam,
			AsyncCallback<String> asyncCallback);

	void getCikkfotipus(AsyncCallback<List<CikkfotipusSer>> asyncCallback);

	void addCikkfotipus(CikkfotipusSer cikktipusSer,
			AsyncCallback<CikkfotipusSer> asyncCallback);

	void updateCikkfotipus(CikkfotipusSer cikktipusSer,
			AsyncCallback<CikkfotipusSer> asyncCallback);

	void getCikkaltipus(String fokod,
			AsyncCallback<List<CikkaltipusSer>> asyncCallback);

	void addCikkaltipus(CikkaltipusSer cikktipusSer,
			AsyncCallback<CikkaltipusSer> asyncCallback);

	void updateCikkaltipus(CikkaltipusSer cikktipusSer,
			AsyncCallback<CikkaltipusSer> asyncCallback);

	void getCikkSelects(AsyncCallback<CikkSelectsSer> asyncCallback);

	void getKosarCikk(String elado, String vevo, String menu,
			AsyncCallback<List<KosarSer>> asyncCallback);

	void getVevoKosar(String elado, String menu,
			AsyncCallback<VevoKosarSer> asyncCallback);

	void addKosar(String elado, String vevo, String menu, String cedula,
			AsyncCallback<String> asyncCallback);

	void removeKosar(String elado, String vevo, String menu, String cedula,
			AsyncCallback<String> asyncCallback);

	void addKosarCikk(KosarSer kosarSer, AsyncCallback<KosarSer> asyncCallback);

	void updateKosarCikk(KosarSer kosarSer,
			AsyncCallback<KosarSer> asyncCallback);

	void removeKosarCikk(KosarSer kosarSer,
			AsyncCallback<KosarSer> asyncCallback);

	void importInternet(String elado, String vevo, String rendeles,
			AsyncCallback<String> asyncCallback);

	void createCedula(String elado, String vevo, String menu,
			AsyncCallback<String> asyncCallback);

	void getCedula(String vevo, String menu, String tipus,
			AsyncCallback<List<CedulaSer>> asyncCallback);

	void getCedulacikk(String cedula, String tipus,
			AsyncCallback<List<CedulacikkSer>> asyncCallback);

	void cedulaToKosar(String elado, String vevo, String menu, String cedula,
			AsyncCallback<String> asyncCallback);

	void kosarToCedula(String elado, String vevo, String menu, String cedula,
			Float befizet, Float befizeteur, Float befizetusd,
			AsyncCallback<String> asyncCallback);

	void getFizetes(AsyncCallback<List<FizetesSer>> asyncCallback);

	void getElozoZaras(AsyncCallback<ZarasEgyenlegSer> asyncCallback);

	void createZaras(String penztaros, Float egyenleghuf, Float egyenlegeur,
			Float egyenlegusd, Float kivethuf, Float kiveteur, Float kivetusd,
			AsyncCallback<String> asyncCallback);

	void getZarasFizetes(String zaras,
			AsyncCallback<List<FizetesSer>> asyncCallback);

	void getZaras(AsyncCallback<List<ZarasSer>> asyncCallback);

	void createTorlesztes(String penztaros, String vevo, Float torleszthuf,
			Float torleszteur, Float torlesztusd,
			AsyncCallback<String> asyncCallback);

	void getTorlesztesek(AsyncCallback<List<FizetesSer>> asyncCallback);

	void getTorlesztes(String cedula,
			AsyncCallback<List<FizetesSer>> asyncCallback);

	void getHazi(AsyncCallback<List<FizetesSer>> asyncCallback);

	void addHazi(FizetesSer fizetesSer, AsyncCallback<FizetesSer> asyncCallback);

	void getRendelesSzamolt(String status,AsyncCallback<List<RendeltcikkSer>> asyncCallback);

	void getRendeles(String status,String cikkszam,String szinkod,AsyncCallback<List<RendeltcikkSer>> asyncCallback);

	void getMegrendelt(String status,String cikkszam,String szinkod,AsyncCallback<List<RendeltcikkSer>> asyncCallback);
	
	void updateRendeltcikk(RendeltcikkSer rendeltcikkSer,
			AsyncCallback<RendeltcikkSer> asyncCallback);
	
	void megrendeles(RendeltcikkSer rendeltcikkSer,AsyncCallback<RendeltcikkSer> asyncCallback);

	void getBeszallitottcikk(String cikkszam,String szinkod,AsyncCallback<List<BeszallitottcikkSer>> asyncCallback);

}
