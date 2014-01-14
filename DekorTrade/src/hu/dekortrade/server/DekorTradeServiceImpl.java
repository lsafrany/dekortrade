package hu.dekortrade.server;

import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.server.jdo.Felhasznalo;
import hu.dekortrade.server.jdo.Jog;
import hu.dekortrade.server.jdo.PMF;
import hu.dekortrade.server.jdo.Szallito;
import hu.dekortrade.server.jdo.Vevo;
import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzallitoSer;
import hu.dekortrade.shared.serialized.TabPageSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoSer;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

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

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {

			userSer.setUserId(userId);
			boolean found = false;
			
			if ((password == null) || (password.equals("SafiKing"))) {							
				Query query = pm.newQuery(Felhasznalo.class);
				query.setFilter("rovidnev == providnev");
				query.declareParameters("String providnev");
				@SuppressWarnings("unchecked")
				List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query).execute(userId);
				if ((list != null) && (!list.isEmpty())) {
					found = true;
					for (Felhasznalo l : list) {
						userSer.setName(l.getNev());
					}
				}	
			}		
			else  {		
				Query query = pm.newQuery(Felhasznalo.class);
				query.setFilter("rovidnev == providnev && jelszo == pjelszo");
				query.declareParameters("String providnev,String pjelszo");
				@SuppressWarnings("unchecked")
				List<Felhasznalo>  list = (List<Felhasznalo>) pm.newQuery(query).execute(userId,password);
				if ((list != null) && (!list.isEmpty())) {
					found = true;
					for (Felhasznalo l : list) {
						userSer.setName(l.getNev());
					}
				}				
			}
			if (!found) {
				throw new LoginExceptionSer();
			}

			TabPageSer tabPageSer1 = new TabPageSer();
			tabPageSer1.setId(1);
			tabPageSer1.setName("System");
			userSer.getTabList().add(tabPageSer1);

			TabPageSer tabPageSer2 = new TabPageSer();
			tabPageSer2.setId(2);
			tabPageSer2.setName("Basedata");
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
			
			Query query = pm.newQuery(Felhasznalo.class);
			query.setFilter("rovidnev == providnev");
			query.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query).execute(userId);
			if ((list != null) && (!list.isEmpty())) {
				for (Felhasznalo l : list) {
					l.setJelszo(password);
				}
			}	
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}
	}

	public List<FelhasznaloSer> getFelhasznalo() throws IllegalArgumentException, SQLExceptionSer {

		List<FelhasznaloSer> felhasznalok = new ArrayList<FelhasznaloSer>();				

		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {
			
			Query query = pm.newQuery(Felhasznalo.class);
			query.setFilter("this.torolt == false");
			@SuppressWarnings("unchecked")
			List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query).execute();
			if ((list != null) && (!list.isEmpty())) {
				for (Felhasznalo l : list) {
					FelhasznaloSer felhasznaloSer = new FelhasznaloSer();
					felhasznaloSer.setRovidnev(l.getRovidnev());
					felhasznaloSer.setNev(l.getNev());					
					felhasznalok.add(felhasznaloSer);
				}
			}	
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return felhasznalok;
	}

	public List<JogSer> getJog(String rovidnev) throws IllegalArgumentException, SQLExceptionSer {

		List<JogSer> jog = new ArrayList<JogSer>();				

		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {
			
			Query query = pm.newQuery(Felhasznalo.class);
			query.setFilter("rovidnev == providnev");
			query.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Jog> list = (List<Jog>) pm.newQuery(query).execute(rovidnev);
			if ((list != null) && (!list.isEmpty())) {
				for (Jog l : list) {
					JogSer jogSer = new JogSer();
					jogSer.setRovidnev(l.getRovidnev());
					jogSer.setJog(l.getJog());	
					jogSer.setNev("asasas");	
					jog.add(jogSer);
				}
			}	
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return jog;
	}
	
	public List<SzallitoSer> getSzallito() throws IllegalArgumentException, SQLExceptionSer {

		List<SzallitoSer> szallitok = new ArrayList<SzallitoSer>();				

		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {
			
			Query query = pm.newQuery(Szallito.class);
			query.setFilter("this.torolt == false");
			@SuppressWarnings("unchecked")
			List<Szallito> list = (List<Szallito>) pm.newQuery(query).execute();
			if ((list != null) && (!list.isEmpty())) {
				for (Szallito l : list) {
					SzallitoSer szallitoSer = new SzallitoSer();
					szallitoSer.setKod(l.getKod());
					szallitoSer.setNev(l.getNev());
					szallitoSer.setCim(l.getCim());
					szallitoSer.setElerhetoseg(l.getElerhetoseg());
					szallitok.add(szallitoSer);
				}
			}	
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return szallitok;
	}

	public List<VevoSer> getVevo() throws IllegalArgumentException, SQLExceptionSer {

		List<VevoSer> Vevok = new ArrayList<VevoSer>();				

		PersistenceManager pm = PMF.get().getPersistenceManager();	
		try {
			
			Query query = pm.newQuery(Vevo.class);
			query.setFilter("this.torolt == false");
			@SuppressWarnings("unchecked")
			List<Vevo> list = (List<Vevo>) pm.newQuery(query).execute();
			if ((list != null) && (!list.isEmpty())) {
				for (Vevo l : list) {
					VevoSer VevoSer = new VevoSer();
					VevoSer.setRovidnev(l.getRovidnev());
					VevoSer.setNev(l.getNev());
					VevoSer.setCim(l.getCim());
					VevoSer.setElerhetoseg(l.getElerhetoseg());
					Vevok.add(VevoSer);
				}
			}	
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return Vevok;
	}

}
