package hu.dekortrade99.client;

import java.util.ArrayList;

import hu.dekortrade99.shared.serialized.CikkSer;
import hu.dekortrade99.shared.serialized.CikkaltipusSer;
import hu.dekortrade99.shared.serialized.CikkfotipusSer;
import hu.dekortrade99.shared.serialized.KosarSer;
import hu.dekortrade99.shared.serialized.RendeltSer;
import hu.dekortrade99.shared.serialized.RendeltcikkSer;
import hu.dekortrade99.shared.serialized.UserSer;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DekorTrade99ServiceAsync {

	void getUser(String user, String password, AsyncCallback<UserSer> callback)
			throws IllegalArgumentException;

	void setPassword(String user, String password,
			@SuppressWarnings("rawtypes") AsyncCallback callback)
			throws IllegalArgumentException;

	void getCikkfotipus(AsyncCallback<ArrayList<CikkfotipusSer>> asyncCallback);

	void getCikkaltipus(String fokod,AsyncCallback<ArrayList<CikkaltipusSer>> asyncCallback);

	void getCikk(int page, String fotipus, String altipus,
			AsyncCallback<ArrayList<CikkSer>> asyncCallback);

	void getKosar(String rovidnev,
			AsyncCallback<ArrayList<KosarSer>> asyncCallback);

	void getRendelt(String rovidnev,
			AsyncCallback<ArrayList<RendeltSer>> asyncCallback);

	void getRendeltcikk(String rovidnev, String rendeles,
			AsyncCallback<ArrayList<RendeltcikkSer>> asyncCallback);

	void addKosar(KosarSer kosarSer, AsyncCallback<KosarSer> callback)
			throws IllegalArgumentException;

	void removeKosar(KosarSer kosarSer, AsyncCallback<KosarSer> callback)
			throws IllegalArgumentException;

	void updateKosar(KosarSer kosarSer, AsyncCallback<KosarSer> callback)
			throws IllegalArgumentException;

	void commitKosar(String userid, AsyncCallback<String> callback)
			throws IllegalArgumentException;

	void getKep(String cikkszam,String szinkod,AsyncCallback<ArrayList<String>> asyncCallback);

}
