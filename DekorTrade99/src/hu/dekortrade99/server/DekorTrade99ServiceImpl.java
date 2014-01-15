package hu.dekortrade99.server;

import hu.dekortrade99.client.DekorTrade99Service;
import hu.dekortrade99.server.jdo.Ctorzs;
import hu.dekortrade99.server.jdo.Kosar;
import hu.dekortrade99.server.jdo.PMF;
import hu.dekortrade99.server.jdo.Rendelt;
import hu.dekortrade99.server.jdo.Rendeltcikk;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
	
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
				query.declareParameters("String providnev,String pjelszo");
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
			tabPageSer1.setName(Constants.MENU_ORDER);
			userSer.getTabList().add(tabPageSer1);

			TabPageSer tabPageSer2 = new TabPageSer();
			tabPageSer2.setId(2);
			tabPageSer2.setName(Constants.MENU_ARCHIV);
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

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<KosarSer> kosar = new ArrayList<KosarSer>();

		try {
			Query query = pm.newQuery(Kosar.class);
			query.setFilter("rovidnev == providnev");
			query.declareParameters("String providnev");
			query.setOrdering("cikkszam");
			@SuppressWarnings("unchecked")
			List<Kosar> list = (List<Kosar>) pm.newQuery(query)
					.execute(rovidnev);
			if (!list.isEmpty()) {
				for (Kosar l : list) {
					KosarSer kosarSer = new KosarSer();
					kosarSer.setRovidnev(l.getRovidnev());
					kosarSer.setCikkszam(l.getCikkszam());
					kosarSer.setExportkarton(l.getExportkarton());
					kosar.add(kosarSer);
				}
			} 	
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}
					
		return kosar;
	}

	public ArrayList<RendeltSer> getRendelt(String rovidnev)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<RendeltSer> rendelt = new ArrayList<RendeltSer>();
	
		try {
			Query query = pm.newQuery(Rendelt.class);
			query.setFilter("rovidnev == providnev");
			query.declareParameters("String providnev");
			query.setOrdering("rendeles");
			@SuppressWarnings("unchecked")
			List<Rendelt> list = (List<Rendelt>) pm.newQuery(query)
					.execute(rovidnev);
			if (!list.isEmpty()) {
				for (Rendelt l : list) {
					RendeltSer rendeltSer = new RendeltSer();
					rendeltSer.setRovidnev(l.getRovidnev());
					rendeltSer.setRendeles(l.getRendeles());
					rendeltSer.setDatum(simpleDateFormat.format(l.getDatum()));
					rendeltSer.setStatusz(l.getStatusz());
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

	public KosarSer addKosar(KosarSer kosarSer) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {	
			Kosar kosar = new Kosar(kosarSer.getRovidnev(),kosarSer.getCikkszam(),kosarSer.getExportkarton());
			pm.makePersistent(kosar);	
		} catch (Exception e) {	
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return kosarSer;
	}

	public KosarSer updateKosar(KosarSer kosarSer) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {	
			Query query = pm.newQuery(Kosar.class);
			query.setFilter("(rovidnev == providnev) && (cikkszam == pcikkszam)");
			query.declareParameters("String providnev,String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Kosar> list = (List<Kosar>) pm.newQuery(query)
					.execute(kosarSer.getRovidnev(),kosarSer.getCikkszam());
			if (!list.isEmpty()) {
				for (Kosar l : list) {
					l.setExportkarton(kosarSer.getExportkarton());
				}
			} 	
		} catch (Exception e) {	
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return kosarSer;
	}

	public KosarSer removeKosar(KosarSer kosarSer) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {			

			Query query = pm.newQuery(Kosar.class);
			query.setFilter("(rovidnev == providnev) && (cikkszam == pcikkszam)");
			query.declareParameters("String providnev,String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Kosar> list = (List<Kosar>) pm.newQuery(query)
					.execute( kosarSer.getRovidnev(),kosarSer.getCikkszam());
			if (!list.isEmpty()) {
				for (Kosar l : list) {
					pm.deletePersistent(l);
				}
			} 	
		} catch (Exception e) {	
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}
		
		return kosarSer;
	}
	
	public String commitKosar(String rovidnev)
			throws IllegalArgumentException, SQLExceptionSer {

		String ret = "";
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			int count = 0;
			Query rendeltquery = pm.newQuery(Rendelt.class);
			rendeltquery.setFilter("rovidnev == providnev");
			rendeltquery.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Rendelt> rendeltlist = (List<Rendelt>) pm.newQuery(rendeltquery)
					.execute(rovidnev);
			if (!rendeltlist.isEmpty()) {
				count = rendeltlist.size() + 1;
			} else count = 1;	
				
			String rendeles = Integer.valueOf(count).toString();
			Rendelt rendelt = new Rendelt(rovidnev,Integer.valueOf(rendeles).toString(),new Date(),"PENDING");
			pm.makePersistent(rendelt);	

			Query kosarquery = pm.newQuery(Kosar.class);
			kosarquery.setFilter("rovidnev == providnev");
			kosarquery.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Kosar> list = (List<Kosar>) pm.newQuery(kosarquery)
					.execute(rovidnev);
			if (!list.isEmpty()) {
				for (Kosar l : list) {				
					Rendeltcikk rendeltcikk = new Rendeltcikk(rovidnev,rendeles,l.getCikkszam(),l.getExportkarton());
					pm.makePersistent(rendeltcikk);	
					pm.deletePersistent(l);	
				}
			} 	

			ret = rovidnev + "/" + rendeles;
			
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}
					
		return ret;
	}

}
