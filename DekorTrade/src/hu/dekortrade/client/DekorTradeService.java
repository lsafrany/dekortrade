package hu.dekortrade.client;

import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzallitoSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoSer;

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
	
}
