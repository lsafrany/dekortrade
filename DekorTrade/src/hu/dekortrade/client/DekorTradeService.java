package hu.dekortrade.client;

import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzallitoSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoSer;
import hu.dekortrade.shared.serialized.CtorzsSer;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.RendeltcikkSer;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("service")
public interface DekorTradeService extends RemoteService {
	
	UserSer getUser(String user, String password)
			throws IllegalArgumentException, SQLExceptionSer, LoginExceptionSer;

	void setPassword(String user, String password)
			throws IllegalArgumentException, SQLExceptionSer;

	List<FelhasznaloSer> getFelhasznalo() throws IllegalArgumentException, SQLExceptionSer;

	List<JogSer> getJog(String rovidnev) throws IllegalArgumentException, SQLExceptionSer;
	
	List<SzallitoSer> getSzallito() throws IllegalArgumentException, SQLExceptionSer;
	
	List<VevoSer> getVevo() throws IllegalArgumentException, SQLExceptionSer;
	
	List<CtorzsSer> getCtorzs(int page, String cikkszam, String jel)
			throws IllegalArgumentException, SQLExceptionSer;

	ArrayList<RendeltSer> getRendelt()
			throws IllegalArgumentException, SQLExceptionSer;

	ArrayList<RendeltcikkSer> getRendeltcikk(String rovidnev,String rendeles)
			throws IllegalArgumentException, SQLExceptionSer;

	String szinkron() throws IllegalArgumentException, SQLExceptionSer;
	
}
