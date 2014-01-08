package hu.dekortrade.server;

import java.util.ArrayList;

import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.TabPageSer;
import hu.dekortrade.shared.serialized.UserSer;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DekorTradeServiceImpl extends RemoteServiceServlet implements
		DekorTradeService {

	public UserSer getUser(String userId, String password)
			throws IllegalArgumentException, SQLExceptionSer, LoginExceptionSer {

		UserSer userSer = new UserSer();

		try {

			userSer.setUserId(userId.toUpperCase());

			if (password != null) {
				if (true==true) {
					userSer.setName("Safi");
				} else {
					throw new LoginExceptionSer();
				}
			} else {
				userSer.setName("SAFI");
			}

			TabPageSer tabPageSer1 = new TabPageSer();
			tabPageSer1.setId(1);
			tabPageSer1.setName("System");
			userSer.getTabList().add(tabPageSer1);
						
			userSer.setDefultTab(1);
			
		} catch (Exception e) {			
			throw new SQLExceptionSer(e.getMessage());						
		} finally {
		}	
		
		return userSer;
	}

	public void setPassword(String userId, String password)
			throws IllegalArgumentException, SQLExceptionSer {

	}

	public ArrayList<String> getRights(String userId, String tabId) throws IllegalArgumentException, SQLExceptionSer {

		ArrayList<String> rights = new ArrayList<String>();				

		return rights;
	}

}
