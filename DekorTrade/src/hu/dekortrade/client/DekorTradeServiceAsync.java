package hu.dekortrade.client;

import hu.dekortrade.shared.serialized.UserSer;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface DekorTradeServiceAsync {
	
	void getUser(String user, String password, AsyncCallback<UserSer> callback)
			throws IllegalArgumentException;

	void setPassword(String user, String password,
			@SuppressWarnings("rawtypes") AsyncCallback callback)
			throws IllegalArgumentException;

	void getRights(String userId, String tabId, AsyncCallback<ArrayList<String>> callback)
			throws IllegalArgumentException;

}
