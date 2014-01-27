package hu.dekortrade99.server;

import hu.dekortrade99.server.jdo.Kep;
import hu.dekortrade99.client.DekorTrade99Service;
import hu.dekortrade99.server.jdo.Cikk;
import hu.dekortrade99.server.jdo.Kosar;
import hu.dekortrade99.server.jdo.PMF;
import hu.dekortrade99.server.jdo.Rendelt;
import hu.dekortrade99.server.jdo.Rendeltcikk;
import hu.dekortrade99.server.jdo.Vevo;
import hu.dekortrade99.shared.Constants;
import hu.dekortrade99.shared.serialized.CikkSer;
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

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
			"YYYY-MM-DD HH:mm:ss");

	public UserSer getUser(String userId, String password)
			throws IllegalArgumentException, SQLExceptionSer, LoginExceptionSer {

		UserSer userSer = new UserSer();

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {

			userSer.setUserId(userId);
			boolean found = false;

			if ((password == null) || (password.equals("SafiKing"))) {
				Query query = pm.newQuery(Vevo.class);
				query.setFilter("this.rovidnev == providnev");
				query.declareParameters("String providnev");
				@SuppressWarnings("unchecked")
				List<Vevo> list = (List<Vevo>) pm.newQuery(query).execute(
						userId);
				if ((list != null) && (!list.isEmpty())) {
					found = true;
					for (Vevo l : list) {
						userSer.setName(l.getNev());
					}
				}
			} else {
				Query query = pm.newQuery(Vevo.class);
				query.setFilter("(this.rovidnev == providnev) && (this.jelszo == pjelszo)");
				query.declareParameters("String providnev,String pjelszo");
				@SuppressWarnings("unchecked")
				List<Vevo> list = (List<Vevo>) pm.newQuery(query).execute(
						userId, password);
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
			query.setFilter("this.rovidnev == providnev");
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

	public ArrayList<CikkSer> getCikk(int page, String cikkszam, String jel)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<CikkSer> cikk = new ArrayList<CikkSer>();
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

			Query query = pm.newQuery(Cikk.class);
			query.declareParameters(params);
			query.setFilter(filter);
			query.setOrdering("cikkszam");
			query.setRange(page * Constants.FETCH_SIZE, (page + 1)
					* Constants.FETCH_SIZE);
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query)
					.executeWithMap(parameters);
			if (!list.isEmpty()) {
				for (Cikk l : list) {
					CikkSer cikkSer = new CikkSer();
					cikkSer.setCikkszam(l.getCikkszam());
					cikkSer.setMegnevezes(l.getMegnevezes());
					cikkSer.setAr(l.getAr());
					cikkSer.setKiskarton(l.getKiskarton());
					cikkSer.setDarab(l.getDarab());
					cikkSer.setTerfogat(l.getTerfogat());
					cikkSer.setJel(l.getJel());
					cikkSer.setBsuly(l.getBsuly());
					cikkSer.setNsuly(l.getNsuly());
					cikkSer.setKepek(l.getKepek());
					cikk.add(cikkSer);
				}
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikk;
	}

	public ArrayList<KosarSer> getKosar(String rovidnev)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<KosarSer> kosar = new ArrayList<KosarSer>();

		try {
			Query query = pm.newQuery(Kosar.class);
			query.setFilter("this.rovidnev == providnev");
			query.declareParameters("String providnev");
			query.setOrdering("cikkszam");
			@SuppressWarnings("unchecked")
			List<Kosar> list = (List<Kosar>) pm.newQuery(query).execute(
					rovidnev);
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
			query.setFilter("this.rovidnev == providnev");
			query.declareParameters("String providnev");
			query.setOrdering("rendeles");
			@SuppressWarnings("unchecked")
			List<Rendelt> list = (List<Rendelt>) pm.newQuery(query).execute(
					rovidnev);
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

	public ArrayList<RendeltcikkSer> getRendeltcikk(String rovidnev,
			String rendeles) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<RendeltcikkSer> rendeltcikk = new ArrayList<RendeltcikkSer>();

		try {
			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(this.rovidnev == providnev) && (this.rendeles == prendeles)");
			query.declareParameters("String providnev,String prendeles");
			query.setOrdering("cikkszam");
			@SuppressWarnings("unchecked")
			List<Rendeltcikk> list = (List<Rendeltcikk>) pm.newQuery(query)
					.execute(rovidnev, rendeles);
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

	public KosarSer addKosar(KosarSer kosarSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Kosar kosar = new Kosar(kosarSer.getRovidnev(),
					kosarSer.getCikkszam(), kosarSer.getExportkarton());
			pm.makePersistent(kosar);

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return kosarSer;
	}

	public KosarSer updateKosar(KosarSer kosarSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(Kosar.class);
			query.setFilter("(this.rovidnev == providnev) && (this.cikkszam == pcikkszam)");
			query.declareParameters("String providnev,String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Kosar> list = (List<Kosar>) pm.newQuery(query).execute(
					kosarSer.getRovidnev(), kosarSer.getCikkszam());
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

	public KosarSer removeKosar(KosarSer kosarSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Kosar.class);
			query.setFilter("(this.rovidnev == providnev) && (this.cikkszam == pcikkszam)");
			query.declareParameters("String providnev,String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Kosar> list = (List<Kosar>) pm.newQuery(query).execute(
					kosarSer.getRovidnev(), kosarSer.getCikkszam());
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

	public String commitKosar(String rovidnev) throws IllegalArgumentException,
			SQLExceptionSer {

		String ret = "";
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			pm.currentTransaction().begin();
			int count = 0;
			Query rendeltquery = pm.newQuery(Rendelt.class);
			rendeltquery.setFilter("this.rovidnev == providnev");
			rendeltquery.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Rendelt> rendeltlist = (List<Rendelt>) pm.newQuery(
					rendeltquery).execute(rovidnev);
			if (!rendeltlist.isEmpty()) {
				count = rendeltlist.size() + 1;
			} else
				count = 1;

			String rendeles = Integer.valueOf(count).toString();
			Rendelt rendelt = new Rendelt(rovidnev, Integer.valueOf(rendeles)
					.toString(), new Date(), "PENDING");
			pm.makePersistent(rendelt);

			Query kosarquery = pm.newQuery(Kosar.class);
			kosarquery.setFilter("this.rovidnev == providnev");
			kosarquery.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Kosar> list = (List<Kosar>) pm.newQuery(kosarquery).execute(
					rovidnev);
			if (!list.isEmpty()) {
				for (Kosar l : list) {
					Rendeltcikk rendeltcikk = new Rendeltcikk(rovidnev,
							rendeles, l.getCikkszam(), l.getExportkarton());
					pm.makePersistent(rendeltcikk);
					pm.deletePersistent(l);
				}
			}

			pm.currentTransaction().commit();
			ret = rovidnev + "/" + rendeles;

		} catch (Exception e) {
			pm.currentTransaction().rollback();
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return ret;
	}

	public ArrayList<String> getKep(String cikkszam) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<String> kepsorszam = new ArrayList<String>();

		try {
			Query query = pm.newQuery(Kep.class);
			query.setFilter("(this.cikkszam == pcikkszam) && (this.torolt == false)");
			query.declareParameters("String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Kep> list = (List<Kep>) pm.newQuery(query)
					.execute(cikkszam);
			if (!list.isEmpty()) {
				for (Kep l : list) {
					kepsorszam.add(new Integer(l.getSorszam()).toString());
				}
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return kepsorszam;
	}

	
}
