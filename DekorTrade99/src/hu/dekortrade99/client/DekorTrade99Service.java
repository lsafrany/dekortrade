package hu.dekortrade99.client;

import java.util.ArrayList;

import hu.dekortrade99.shared.serialized.CikkSer;
import hu.dekortrade99.shared.serialized.KosarSer;
import hu.dekortrade99.shared.serialized.LoginExceptionSer;
import hu.dekortrade99.shared.serialized.RendeltSer;
import hu.dekortrade99.shared.serialized.RendeltcikkSer;
import hu.dekortrade99.shared.serialized.SQLExceptionSer;
import hu.dekortrade99.shared.serialized.UserSer;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface DekorTrade99Service extends RemoteService {

	UserSer getUser(String user, String password)
			throws IllegalArgumentException, SQLExceptionSer, LoginExceptionSer;

	void setPassword(String user, String password)
			throws IllegalArgumentException, SQLExceptionSer;

	ArrayList<CikkSer> getCikk(int page, String cikkszam, String jel)
			throws IllegalArgumentException, SQLExceptionSer;

	ArrayList<KosarSer> getKosar(String rovidnev)
			throws IllegalArgumentException, SQLExceptionSer;

	ArrayList<RendeltSer> getRendelt(String rovidnev)
			throws IllegalArgumentException, SQLExceptionSer;

	ArrayList<RendeltcikkSer> getRendeltcikk(String rovidnev, String rendeles)
			throws IllegalArgumentException, SQLExceptionSer;

	KosarSer addKosar(KosarSer kosarSer) throws IllegalArgumentException,
			SQLExceptionSer;

	KosarSer removeKosar(KosarSer kosarSer) throws IllegalArgumentException,
			SQLExceptionSer;

	KosarSer updateKosar(KosarSer kosarSer) throws IllegalArgumentException,
			SQLExceptionSer;

	String commitKosar(String rovidnev) throws IllegalArgumentException,
			SQLExceptionSer;

	ArrayList<String> getKep(String cikkszam) throws IllegalArgumentException,SQLExceptionSer;

}
