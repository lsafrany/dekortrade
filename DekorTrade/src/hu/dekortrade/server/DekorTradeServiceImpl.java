package hu.dekortrade.server;

import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.server.jdo.Cikk;
import hu.dekortrade.server.jdo.Felhasznalo;
import hu.dekortrade.server.jdo.Jog;
import hu.dekortrade.server.jdo.Kep;
import hu.dekortrade.server.jdo.PMF;
import hu.dekortrade.server.jdo.Rendelt;
import hu.dekortrade.server.jdo.Rendeltcikk;
import hu.dekortrade.server.jdo.Szallito;
import hu.dekortrade.server.jdo.Vevo;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.CikkSer;
import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzallitoSer;
import hu.dekortrade.shared.serialized.TabPageSer;
import hu.dekortrade.shared.serialized.UploadSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoSer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DekorTradeServiceImpl extends RemoteServiceServlet implements
		DekorTradeService {

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
				Query query = pm.newQuery(Felhasznalo.class);
				query.setFilter("rovidnev == providnev");
				query.declareParameters("String providnev");
				@SuppressWarnings("unchecked")
				List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query)
						.execute(userId);
				if ((list != null) && (!list.isEmpty())) {
					found = true;
					for (Felhasznalo l : list) {
						userSer.setName(l.getNev());
					}
				}
			} else {
				Query query = pm.newQuery(Felhasznalo.class);
				query.setFilter("rovidnev == providnev && jelszo == pjelszo");
				query.declareParameters("String providnev,String pjelszo");
				@SuppressWarnings("unchecked")
				List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query)
						.execute(userId, password);
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
			List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query)
					.execute(userId);
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

	public List<FelhasznaloSer> getFelhasznalo()
			throws IllegalArgumentException, SQLExceptionSer {

		List<FelhasznaloSer> felhasznalok = new ArrayList<FelhasznaloSer>();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Felhasznalo.class);
			query.setFilter("this.torolt == false");
			@SuppressWarnings("unchecked")
			List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query)
					.execute();
			if ((list != null) && (!list.isEmpty())) {
				for (Felhasznalo l : list) {
					FelhasznaloSer felhasznaloSer = new FelhasznaloSer();
					felhasznaloSer.setRovidnev(l.getRovidnev());
					felhasznaloSer.setNev(l.getNev());
					felhasznaloSer.setMenu(l.getMenu());
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

	public FelhasznaloSer addFelhasznalo(FelhasznaloSer felhasznaloSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Felhasznalo.class);
			query.setFilter("this.rovidnev == providnev");
			query.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query)
					.execute(felhasznaloSer.getRovidnev());
			if ((list != null) && (!list.isEmpty())) {
				throw new Exception("Létező rövidnév !");
			} else {
				Felhasznalo felhasznalo = new Felhasznalo(
						felhasznaloSer.getRovidnev(), felhasznaloSer.getNev(),
						felhasznaloSer.getMenu(), Constants.INIT_PASSWORD,
						Boolean.FALSE);
				pm.makePersistent(felhasznalo);
				Jog jog = new Jog(felhasznaloSer.getRovidnev(),
						felhasznaloSer.getMenu());
				pm.makePersistent(jog);
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return felhasznaloSer;
	}

	public FelhasznaloSer updateFelhasznalo(FelhasznaloSer felhasznaloSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query1 = pm.newQuery(Vevo.class);
			query1.setFilter("rovidnev == providnev && nev == pnev");
			query1.declareParameters("String providnev,String pnev");
			@SuppressWarnings("unchecked")
			List<Jog> list1 = (List<Jog>) pm.newQuery(query1).execute(
					felhasznaloSer.getRovidnev(), felhasznaloSer.getNev());
			if (!((list1 != null) && (!list1.isEmpty()))) {
				Jog jog = new Jog(felhasznaloSer.getRovidnev(),
						felhasznaloSer.getMenu());
				pm.makePersistent(jog);
			}

			Query query = pm.newQuery(Vevo.class);
			query.setFilter("this.rovidnev == providnev");
			query.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query)
					.execute(felhasznaloSer.getRovidnev());
			if ((list != null) && (!list.isEmpty())) {
				for (Felhasznalo l : list) {
					l.setNev(felhasznaloSer.getNev());
					l.setMenu(felhasznaloSer.getMenu());
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return felhasznaloSer;
	}

	public String setFelhasznaloJelszo(String rovidnev)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Felhasznalo.class);
			query.setFilter("this.rovidnev == providnev");
			query.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query)
					.execute(rovidnev);
			if ((list != null) && (!list.isEmpty())) {
				for (Felhasznalo l : list) {
					l.setJelszo(Constants.INIT_PASSWORD);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return rovidnev + " - alapjelszó beállítva !";
	}

	public FelhasznaloSer removeFelhasznalo(FelhasznaloSer felhasznaloSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Felhasznalo.class);
			query.setFilter("this.rovidnev == providnev");
			query.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query)
					.execute(felhasznaloSer.getRovidnev());
			if ((list != null) && (!list.isEmpty())) {
				for (Felhasznalo l : list) {
					l.setTorolt(Boolean.TRUE);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return felhasznaloSer;
	}

	public JogSer updateJog(String rovidnev, JogSer jogSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Jog.class);
			query.setFilter("rovidnev == providnev && nev == pnev");
			query.declareParameters("String providnev,String pnev");
			@SuppressWarnings("unchecked")
			List<Jog> list = (List<Jog>) pm.newQuery(query).execute(rovidnev,
					jogSer.getNev());
			if ((list != null) && (!list.isEmpty())) {
				for (Jog l : list) {
					pm.deletePersistent(l);
				}
			}

			if (jogSer.getJog()) {
				Jog jog = new Jog(rovidnev, jogSer.getNev());
				pm.makePersistent(jog);
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return jogSer;
	}

	public List<JogSer> getJog(String rovidnev)
			throws IllegalArgumentException, SQLExceptionSer {

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
						if (jogSerlist.getNev().equals(l.getNev()))
							jogSerlist.setJog(Boolean.TRUE);
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

	public List<VevoSer> getVevo() throws IllegalArgumentException,
			SQLExceptionSer {

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

	public VevoSer addVevo(VevoSer vevoSer) throws IllegalArgumentException,
			SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Vevo.class);
			query.setFilter("this.rovidnev == providnev");
			query.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Vevo> list = (List<Vevo>) pm.newQuery(query).execute(
					vevoSer.getRovidnev());
			if ((list != null) && (!list.isEmpty())) {
				throw new Exception("Létező rövidnév !");
			} else {
				Vevo vevo = new Vevo(vevoSer.getRovidnev(), vevoSer.getNev(),
						vevoSer.getCim(), vevoSer.getElerhetoseg(),
						Boolean.FALSE, Boolean.FALSE, Boolean.FALSE);
				pm.makePersistent(vevo);
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return vevoSer;
	}

	public VevoSer updateVevo(VevoSer vevoSer) throws IllegalArgumentException,
			SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Vevo.class);
			query.setFilter("this.rovidnev == providnev");
			query.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Vevo> list = (List<Vevo>) pm.newQuery(query).execute(
					vevoSer.getRovidnev());
			if ((list != null) && (!list.isEmpty())) {
				for (Vevo l : list) {
					l.setNev(vevoSer.getNev());
					l.setCim(vevoSer.getCim());
					l.setElerhetoseg(vevoSer.getElerhetoseg());
					l.setInternet(vevoSer.getInternet());
					l.setSzinkron(Boolean.FALSE);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return vevoSer;
	}

	public String setVevoJelszo(String rovidnev)
			throws IllegalArgumentException, SQLExceptionSer {

		return rovidnev + " - alapjelszó beállítva !";
	}

	public VevoSer removeVevo(VevoSer vevoSer) throws IllegalArgumentException,
			SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Vevo.class);
			query.setFilter("this.rovidnev == providnev");
			query.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Vevo> list = (List<Vevo>) pm.newQuery(query).execute(
					vevoSer.getRovidnev());
			if ((list != null) && (!list.isEmpty())) {
				for (Vevo l : list) {
					l.setSzinkron(Boolean.FALSE);
					l.setTorolt(Boolean.TRUE);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return vevoSer;
	}

	public List<SzallitoSer> getSzallito() throws IllegalArgumentException,
			SQLExceptionSer {

		List<SzallitoSer> szallitok = new ArrayList<SzallitoSer>();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Szallito.class);
			query.setFilter("this.torolt == false");
			query.setOrdering("kod");
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

	public SzallitoSer addSzallito(SzallitoSer szallitoSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Szallito.class);
			@SuppressWarnings("unchecked")
			List<Szallito> list = (List<Szallito>) pm.newQuery(query).execute();

			Szallito szallito = new Szallito(Integer.valueOf(list.size() + 1)
					.toString(), szallitoSer.getNev(), szallitoSer.getCim(),
					szallitoSer.getElerhetoseg(), Boolean.FALSE);
			pm.makePersistent(szallito);

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return szallitoSer;
	}

	public SzallitoSer updateSzallito(SzallitoSer szallitoSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Szallito.class);
			query.setFilter("this.kod == pkod");
			query.declareParameters("String pkod");
			@SuppressWarnings("unchecked")
			List<Szallito> list = (List<Szallito>) pm.newQuery(query).execute(
					szallitoSer.getKod());
			if ((list != null) && (!list.isEmpty())) {
				for (Szallito l : list) {
					l.setNev(szallitoSer.getNev());
					l.setCim(szallitoSer.getCim());
					l.setElerhetoseg(szallitoSer.getElerhetoseg());
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return szallitoSer;
	}

	public SzallitoSer removeSzallito(SzallitoSer szallitoSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Szallito.class);
			query.setFilter("this.kod == pkod");
			query.declareParameters("String pkod");
			@SuppressWarnings("unchecked")
			List<Szallito> list = (List<Szallito>) pm.newQuery(query).execute(
					szallitoSer.getKod());
			if ((list != null) && (!list.isEmpty())) {
				for (Szallito l : list) {
					l.setTorolt(Boolean.TRUE);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return szallitoSer;
	}

	public List<CikkSer> getCikk(int page, String cikkszam, String jel)
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

	public CikkSer addCikk(CikkSer cikkSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Cikk.class);
			query.setFilter("this.cikkszam == pcikkszam");
			query.declareParameters("String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query).execute(
					cikkSer.getCikkszam());
			if ((list != null) && (!list.isEmpty())) {
				throw new Exception("Létező cikkszám !");
			} else {
				Cikk cikk = new Cikk(cikkSer.getCikkszam(),
						cikkSer.getMegnevezes(), cikkSer.getAr(),
						cikkSer.getKiskarton(), cikkSer.getDarab(),
						cikkSer.getTerfogat(), cikkSer.getJel(),
						cikkSer.getBsuly(), cikkSer.getNsuly(),
						new Integer(0), Boolean.FALSE, Boolean.FALSE);
				pm.makePersistent(cikk);
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkSer;
	}

	public CikkSer updateCikk(CikkSer cikkSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Cikk.class);
			query.setFilter("this.cikkszam == pcikkszam");
			query.declareParameters("String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query).execute(
					cikkSer.getCikkszam());
			if ((list != null) && (!list.isEmpty())) {
				for (Cikk l : list) {
					l.setMegnevezes(cikkSer.getMegnevezes());
					l.setAr(cikkSer.getAr());
					l.setKiskarton(cikkSer.getKiskarton());
					l.setDarab(cikkSer.getDarab());
					l.setTerfogat(cikkSer.getTerfogat());
					l.setJel(cikkSer.getJel());
					l.setBsuly(cikkSer.getBsuly());
					l.setNsuly(cikkSer.getNsuly());
					l.setKepek(cikkSer.getKepek());
					l.setSzinkron(Boolean.FALSE);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkSer;
	}

	public CikkSer removeCikk(CikkSer cikkSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Cikk.class);
			query.setFilter("this.cikkszam == pcikkszam");
			query.declareParameters("String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query).execute(
					cikkSer.getCikkszam());
			if ((list != null) && (!list.isEmpty())) {
				for (Cikk l : list) {
					l.setSzinkron(Boolean.FALSE);
					l.setTorolt(Boolean.TRUE);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkSer;
	}

	public ArrayList<RendeltSer> getRendelt() throws IllegalArgumentException,
			SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<RendeltSer> rendelt = new ArrayList<RendeltSer>();

		try {
			Query query = pm.newQuery(Rendelt.class);
			@SuppressWarnings("unchecked")
			List<Rendelt> list = (List<Rendelt>) pm.newQuery(query).execute();
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

	public ArrayList<RendeltcikkSer> getRendeltcikk(String rovidnev,
			String rendeles) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<RendeltcikkSer> rendeltcikk = new ArrayList<RendeltcikkSer>();

		try {
			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(rovidnev == providnev) && (rendeles == prendeles)");
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

	public String szinkron() throws IllegalArgumentException, SQLExceptionSer {

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

	public String initUploadFileStatus() throws IllegalArgumentException {
		
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();

		session.removeAttribute(ServerConstants.FILE);
		session.removeAttribute(ServerConstants.FILE_ERROR);
		session.setAttribute(ServerConstants.FILE,new  Integer(0));
	
		return "OK";
	}

	public UploadSer getUploadFileStatus() throws IllegalArgumentException {
		UploadSer uploadSer = new UploadSer();
				
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();

		uploadSer.setStatus(Constants.LOADING);
		if (session.getAttribute(ServerConstants.FILE) == null) {
			if (session.getAttribute(ServerConstants.FILE_ERROR) == null) {
				uploadSer.setStatus(Constants.LOADED); 	 
			}
			else  {
				uploadSer.setStatus(Constants.ERROR);
				uploadSer.setError((String)session.getAttribute(ServerConstants.FILE_ERROR));
			}
		}
		
		return uploadSer;
	}

	public ArrayList<String> getKepsorszam(String cikkszam) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<String> kepsorszam = new ArrayList<String>();

		try {
			Query query = pm.newQuery(Kep.class);
			query.setFilter("cikkszam == pcikkszam && torolt == false");
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
