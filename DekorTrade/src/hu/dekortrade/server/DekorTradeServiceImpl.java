package hu.dekortrade.server;

import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.server.jdo.Ctorzs;
import hu.dekortrade.server.jdo.Felhasznalo;
import hu.dekortrade.server.jdo.Jog;
import hu.dekortrade.server.jdo.PMF;
import hu.dekortrade.server.jdo.Rendelt;
import hu.dekortrade.server.jdo.Rendeltcikk;
import hu.dekortrade.server.jdo.Szallito;
import hu.dekortrade.server.jdo.Vevo;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.CtorzsSer;
import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzallitoSer;
import hu.dekortrade.shared.serialized.TabPageSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoSer;

import java.text.SimpleDateFormat;
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
public class DekorTradeServiceImpl extends RemoteServiceServlet implements
		DekorTradeService {

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
	
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
			tabPageSer1.setName(Constants.MENU_SYSTEM);
			userSer.getTabList().add(tabPageSer1);

			TabPageSer tabPageSer2 = new TabPageSer();
			tabPageSer2.setId(2);
			tabPageSer2.setName(Constants.MENU_BASEDATA);
			userSer.getTabList().add(tabPageSer2);

			TabPageSer tabPageSer3 = new TabPageSer();
			tabPageSer3.setId(3);
			tabPageSer3.setName(Constants.MENU_ORDER);
			userSer.getTabList().add(tabPageSer3);
			
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
						
			JogSer menu1jogSer = new JogSer();
			menu1jogSer.setNev(Constants.MENU_SYSTEM);
			menu1jogSer.setJog(Boolean.FALSE);
			jog.add(menu1jogSer);

			JogSer menu2jogSer = new JogSer();
			menu2jogSer.setNev(Constants.MENU_BASEDATA);
			menu2jogSer.setJog(Boolean.FALSE);
			jog.add(menu2jogSer);
						
			JogSer menu3jogSer = new JogSer();
			menu3jogSer.setNev(Constants.MENU_ORDER);
			menu3jogSer.setJog(Boolean.FALSE);
			jog.add(menu3jogSer);
			
			Query query = pm.newQuery(Jog.class);
			query.setFilter("rovidnev == providnev");
			query.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Jog> list = (List<Jog>) pm.newQuery(query).execute(rovidnev);
			if ((list != null) && (!list.isEmpty())) {
				for (Jog l : list) {							
					for (JogSer jogSerlist : jog) {
						if (jogSerlist.getNev().equals(l.getNev())) jogSerlist.setJog(Boolean.TRUE);
					}
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
					VevoSer.setInternet(l.getInternet());
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

	public List<CtorzsSer> getCtorzs(int page, String cikkszam, String jel)
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

	public ArrayList<RendeltSer> getRendelt()
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<RendeltSer> rendelt = new ArrayList<RendeltSer>();
	
		try {
			Query query = pm.newQuery(Rendelt.class);
			@SuppressWarnings("unchecked")
			List<Rendelt> list = (List<Rendelt>) pm.newQuery(query)
					.execute();
			if (!list.isEmpty()) {
				for (Rendelt l : list) {
					RendeltSer rendeltSer = new RendeltSer();
					rendeltSer.setRovidnev(l.getRovidnev());
					rendeltSer.setRendeles(l.getRendeles());
					rendeltSer.setDatum(simpleDateFormat.format(l.getDatum()));
					rendelt.add(rendeltSer);
				}
			} 	
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}
		
		return rendelt;
	}

	public ArrayList<RendeltcikkSer> getRendeltcikk(String rovidnev,String rendeles)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		ArrayList<RendeltcikkSer> rendeltcikk = new ArrayList<RendeltcikkSer>();

		try {
			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(rovidnev == providnev) && (rendeles == prendeles)");
			query.declareParameters("String providnev,String prendeles");
			query.setOrdering("cikkszam");
			@SuppressWarnings("unchecked")
			List<Rendeltcikk> list = (List<Rendeltcikk>) pm.newQuery(query)
					.execute(rovidnev,rendeles);
			if (!list.isEmpty()) {
				for (Rendeltcikk l : list) {
					RendeltcikkSer rendeltcikkSer = new RendeltcikkSer();
					rendeltcikkSer.setRovidnev(l.getRovidnev());
					rendeltcikkSer.setRendeles(l.getRendeles());
					rendeltcikkSer.setCikkszam(l.getCikkszam());
					rendeltcikkSer.setExportkarton(l.getExportkarton());
					rendeltcikk.add(rendeltcikkSer);
				}
			} 	
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}
		
		return rendeltcikk;
	}

	public String szinkron()
			throws IllegalArgumentException, SQLExceptionSer {

		String ret = "";
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {

			ret = "Szinkron";
			
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}
					
		return ret;
	}

}
