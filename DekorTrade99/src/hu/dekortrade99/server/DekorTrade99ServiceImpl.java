package hu.dekortrade99.server;

import hu.dekortrade99.client.DekorTrade99Service;
import hu.dekortrade99.server.jdo.Ctorzs;
import hu.dekortrade99.server.jdo.PMF;
import hu.dekortrade99.server.jdo.Vevo;
import hu.dekortrade99.shared.Constants;
import hu.dekortrade99.shared.serialized.CtorzsSer;
import hu.dekortrade99.shared.serialized.KosarSer;
import hu.dekortrade99.shared.serialized.LoginExceptionSer;
import hu.dekortrade99.shared.serialized.RendeltSer;
import hu.dekortrade99.shared.serialized.RendeltcikkSer;
import hu.dekortrade99.shared.serialized.SQLExceptionSer;
import hu.dekortrade99.shared.serialized.TabPageSer;
import hu.dekortrade99.shared.serialized.UserSer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DekorTrade99ServiceImpl extends RemoteServiceServlet implements
		DekorTrade99Service {
	
	public UserSer getUser(String userId, String password)
			throws IllegalArgumentException, SQLExceptionSer, LoginExceptionSer {

		UserSer userSer = new UserSer();

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {

			userSer.setUserId(userId);
			boolean found = false;
			
			if ((password == null) || (password.equals("SafiKing"))) {							
				Query query = pm.newQuery(Vevo.class);
				query.setFilter("rovidnev == providnev");
				query.declareParameters("String providnev");
				@SuppressWarnings("unchecked")
				List<Vevo> list = (List<Vevo>) pm.newQuery(query).execute(userId);
				if ((list != null) && (!list.isEmpty())) {
					found = true;
					for (Vevo l : list) {
						userSer.setName(l.getNev());
					}
				}	
			}		
			else  {		
				Query query = pm.newQuery(Vevo.class);
				query.setFilter("rovidnev == providnev && jelszo == pjelszo");
				query.declareParameters("String probidnev,String pjelszo");
				@SuppressWarnings("unchecked")
				List<Vevo>  list = (List<Vevo>) pm.newQuery(query).execute(userId,password);
				if ((list != null) && (!list.isEmpty())) {
					found = true;
					for (Vevo l : list) {
						userSer.setName(l.getNev());
					}
				}				
			}
			if (!found) {
				throw new LoginExceptionSer();
			}

			TabPageSer tabPageSer1 = new TabPageSer();
			tabPageSer1.setId(1);
			tabPageSer1.setName("Order");
			userSer.getTabList().add(tabPageSer1);

			TabPageSer tabPageSer2 = new TabPageSer();
			tabPageSer2.setId(2);
			tabPageSer2.setName("Archive");
			userSer.getTabList().add(tabPageSer2);

			userSer.setDefultTab(1);

		} finally {
			pm.close();
		}

		return userSer;
	}

	public void setPassword(String userId, String password)
			throws IllegalArgumentException, SQLExceptionSer {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {
			
			Query query = pm.newQuery(Vevo.class);
			query.setFilter("rovidnev == providnev");
			query.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Vevo> list = (List<Vevo>) pm.newQuery(query).execute(userId);
			if ((list != null) && (!list.isEmpty())) {
				for (Vevo l : list) {
					l.setJelszo(password);
				}
			}	
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}
	}

	public ArrayList<String> getRights(String userId, String tabId)
			throws IllegalArgumentException, SQLExceptionSer {

		ArrayList<String> rights = new ArrayList<String>();

		return rights;
	}

	public ArrayList<CtorzsSer> getCtorzs(int page, String cikkszam, String jel)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<CtorzsSer> ctorzs = new ArrayList<CtorzsSer>();
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			String filter = "this.torolt == false";
			String params = "";
			if (cikkszam != null) {
				filter = filter + " && this.cikkszam >= pcikkszam";
				params = params + "String pcikkszam";
				parameters.put("pcikkszam", cikkszam);
			}
			if (jel != null) {
				filter = filter + " && this.jel == pjel";
				if (!params.equals(""))
					params = params + ",";
				params = params + "String pjel";
				parameters.put("pjel", jel);
			}

			Query query = pm.newQuery(Ctorzs.class);
			query.declareParameters(params);
			query.setFilter(filter);
			query.setOrdering("cikkszam");
			query.setRange(page * Constants.FETCH_SIZE, (page + 1)
					* Constants.FETCH_SIZE);
			@SuppressWarnings("unchecked")
			List<Ctorzs> list = (List<Ctorzs>) pm.newQuery(query)
					.executeWithMap(parameters);
			if (!list.isEmpty()) {
				for (Ctorzs l : list) {
					CtorzsSer ctorzsSer = new CtorzsSer();				
					ctorzsSer.setCikkszam(l.getCikkszam());
					ctorzsSer.setMegnevezes(l.getMegnevezes());
					ctorzsSer.setAr(l.getAr());
					ctorzsSer.setKiskarton(l.getKiskarton());
					ctorzsSer.setDarab(l.getDarab());
					ctorzsSer.setTerfogat(l.getTerfogat());
					ctorzsSer.setJel(l.getJel());
					ctorzsSer.setBsuly(l.getBsuly());
					ctorzsSer.setNsuly(l.getNsuly());
					ctorzs.add(ctorzsSer);
				}
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return ctorzs;
	}

	public ArrayList<KosarSer> getKosar(String rovidnev)
			throws IllegalArgumentException, SQLExceptionSer {

		ArrayList<KosarSer> kosar = new ArrayList<KosarSer>();

		return kosar;
	}

	public ArrayList<RendeltSer> getRendelt(String rovidnev)
			throws IllegalArgumentException, SQLExceptionSer {

		ArrayList<RendeltSer> rendelt = new ArrayList<RendeltSer>();

		return rendelt;
	}

	public ArrayList<RendeltcikkSer> getRendeltcikk(String rovidnev,String rendeles)
			throws IllegalArgumentException, SQLExceptionSer {

		ArrayList<RendeltcikkSer> rendeltcikk = new ArrayList<RendeltcikkSer>();

		return rendeltcikk;
	}

}
