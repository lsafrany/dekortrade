package hu.dekortrade.client;

import hu.dekortrade.shared.serialized.CikkSelectsSer;
import hu.dekortrade.shared.serialized.CikkSer;
import hu.dekortrade.shared.serialized.CikkaltipusSer;
import hu.dekortrade.shared.serialized.CikkfotipusSer;
import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.GyartoSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SzinkronSer;
import hu.dekortrade.shared.serialized.UploadSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoSer;

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

	void addGyarto(GyartoSer szallitoSer,
			AsyncCallback<GyartoSer> asyncCallback);

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

	void getKep(String cikkszam, AsyncCallback<List<String>> asyncCallback);

	void removeKep(String cikkszam, String sorszam,
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

}
