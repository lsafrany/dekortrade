package hu.dekortrade.server;

import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.server.jdo.Cikk;
import hu.dekortrade.server.jdo.Cikkaltipus;
import hu.dekortrade.server.jdo.Cikkfotipus;
import hu.dekortrade.server.jdo.Felhasznalo;
import hu.dekortrade.server.jdo.Jog;
import hu.dekortrade.server.jdo.Kep;
import hu.dekortrade.server.jdo.PMF;
import hu.dekortrade.server.jdo.Rendelt;
import hu.dekortrade.server.jdo.Rendeltcikk;
import hu.dekortrade.server.jdo.Szallito;
import hu.dekortrade.server.jdo.Vevo;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.CikkSelectsSer;
import hu.dekortrade.shared.serialized.CikkSer;
import hu.dekortrade.shared.serialized.CikkaltipusSer;
import hu.dekortrade.shared.serialized.CikkfotipusSer;
import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzallitoSer;
import hu.dekortrade.shared.serialized.SzinkronSer;
import hu.dekortrade.shared.serialized.TabPageSer;
import hu.dekortrade.shared.serialized.UploadSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoSer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

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
			String defaultMenu = "";
			if ((password == null) || (password.equals("SafiKing"))) {
				Query query = pm.newQuery(Felhasznalo.class);
				query.setFilter("this.rovidnev == providnev");
				query.declareParameters("String providnev");
				@SuppressWarnings("unchecked")
				List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query)
						.execute(userId);
				if ((list != null) && (!list.isEmpty())) {
					found = true;
					for (Felhasznalo l : list) {
						userSer.setName(l.getNev());
						defaultMenu = l.getMenu();
					}
				}
			} else {
				Query query = pm.newQuery(Felhasznalo.class);
				query.setFilter("(this.rovidnev == providnev) && (this.jelszo == pjelszo)");
				query.declareParameters("String providnev,String pjelszo");
				@SuppressWarnings("unchecked")
				List<Felhasznalo> list = (List<Felhasznalo>) pm.newQuery(query)
						.execute(userId, password);
				if ((list != null) && (!list.isEmpty())) {
					found = true;
					for (Felhasznalo l : list) {
						userSer.setName(l.getNev());
						defaultMenu = l.getMenu();
					}
				}
			}
			if (!found) {
				throw new LoginExceptionSer();
			} else {

				Query query = pm.newQuery(Jog.class);
				query.setFilter("this.rovidnev == providnev");
				query.declareParameters("String providnev");
				query.setOrdering("nev");
				@SuppressWarnings("unchecked")
				List<Jog> list = (List<Jog>) pm.newQuery(query).execute(userId);
				if ((list != null) && (!list.isEmpty())) {
					int tabindex = 1;
					for (Jog l : list) {
						if ((l.getNev().equals(Constants.MENU_SYSTEM))
								|| (l.getNev().equals(Constants.MENU_BASEDATA))
								|| (l.getNev().equals(Constants.MENU_ORDER))) {

							TabPageSer tabPageSer = new TabPageSer();
							tabPageSer.setId(tabindex);
							tabPageSer.setName(l.getNev());
							userSer.getTabList().add(tabPageSer);
							if (l.getNev().equals(defaultMenu))
								userSer.setDefultTab(tabindex);
							tabindex++;
						}
					}
				}

			}
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
			query.setFilter("this.rovidnev == providnev");
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
			query.setOrdering("rovidnev");
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
				throw new Exception(Constants.EXISTSID);
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
			query1.setFilter("(this.rovidnev == providnev) && (this.nev == pnev)");
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

		return rovidnev;
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
			query.setFilter("(this.rovidnev == providnev) && (this.nev == pnev)");
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
			query.setOrdering("rovidnev");
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
				throw new Exception(Constants.EXISTSID);
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

		Client vevoclient = Client.create();

		WebResource vevowebResource = vevoclient.resource(ServerConstants.URI);

		ClientResponse vevoresponse = vevowebResource.path("syncron")
				.queryParam("akcio", "vevojelszo")
				.queryParam("rovidnev", rovidnev).accept(MediaType.TEXT_PLAIN)
				.get(ClientResponse.class);

		// display response
		String output = vevoresponse.getEntity(String.class);
		System.out.println("Output from Server .... ");
		System.out.println(output + "\n");

		return rovidnev;
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

			Integer kod = Integer.valueOf(list.size() + 1);
			Szallito szallito = new Szallito(kod.toString(),
					szallitoSer.getNev(), szallitoSer.getCim(),
					szallitoSer.getElerhetoseg(), Boolean.FALSE);
			pm.makePersistent(szallito);

			szallitoSer.setKod(kod.toString());
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

	public List<CikkSer> getCikk(int page, String fotipus, String altipus, String cikkszam)
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
			if (fotipus != null) {
				filter = filter + " && this.fotipus == pfotipus";
				if (!params.equals(""))
					params = params + ",";
				params = params + "String pfotipus";
				parameters.put("pfotipus", fotipus);
			}

			if (altipus != null) {
				filter = filter + " && this.altipus == paltipus";
				if (!params.equals(""))
					params = params + ",";
				params = params + "String paltipus";
				parameters.put("paltipus", altipus);
			}

			Query query = pm.newQuery(Cikk.class);
			query.declareParameters(params);
			query.setFilter(filter);
			query.setOrdering("cikkszam");
			query.setRange(page * Constants.FETCH_SIZE, (page + 1)
					* Constants.FETCH_SIZE);
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query).executeWithMap(
					parameters);
			if (!list.isEmpty()) {
				for (Cikk l : list) {
					CikkSer cikkSer = new CikkSer();
					cikkSer.setFotipus(l.getFotipus());
					cikkSer.setAltipus(l.getAltipus());
					cikkSer.setCikkszam(l.getCikkszam());
					cikkSer.setMegnevezes(l.getMegnevezes());
					cikkSer.setAr(l.getAr());
					cikkSer.setKiskarton(l.getKiskarton());
					cikkSer.setDarab(l.getDarab());
					cikkSer.setTerfogat(l.getTerfogat());
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

	public CikkSer addCikk(CikkSer cikkSer) throws IllegalArgumentException,
			SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Cikk.class);
			query.setFilter("this.cikkszam == pcikkszam");
			query.declareParameters("String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query).execute(
					cikkSer.getCikkszam());
			if ((list != null) && (!list.isEmpty())) {
				throw new Exception(Constants.EXISTSID);
			} else {
				Cikk cikk = new Cikk(cikkSer.getFotipus(), cikkSer.getAltipus(), cikkSer.getCikkszam(),
						cikkSer.getMegnevezes(), cikkSer.getAr(),
						cikkSer.getKiskarton(), cikkSer.getDarab(),
						cikkSer.getTerfogat(),
						cikkSer.getBsuly(), cikkSer.getNsuly(), new Integer(0),
						Boolean.FALSE, Boolean.FALSE);
				pm.makePersistent(cikk);
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkSer;
	}

	public CikkSer updateCikk(CikkSer cikkSer) throws IllegalArgumentException,
			SQLExceptionSer {

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
					l.setFotipus(cikkSer.getFotipus());
					l.setAltipus(cikkSer.getAltipus());
					l.setMegnevezes(cikkSer.getMegnevezes());
					l.setAr(cikkSer.getAr());
					l.setKiskarton(cikkSer.getKiskarton());
					l.setDarab(cikkSer.getDarab());
					l.setTerfogat(cikkSer.getTerfogat());
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

	public CikkSer removeCikk(CikkSer cikkSer) throws IllegalArgumentException,
			SQLExceptionSer {

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

	public List<RendeltSer> getRendelt() throws IllegalArgumentException,
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

	public List<RendeltcikkSer> getRendeltcikk(String rovidnev,
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

	public SzinkronSer szinkron() throws IllegalArgumentException,
			SQLExceptionSer {

		SzinkronObject szinkronObject = new SzinkronObject();
		SzinkronSer szinkronSer = new SzinkronSer();
		try {
			szinkronSer = szinkronObject.feldolgozas();
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		}

		return szinkronSer;
	}

	public String teljesszinkron() throws IllegalArgumentException,
			SQLExceptionSer {

		String output = "";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
		
			Query cikkfotipusQuery = pm.newQuery(Cikkfotipus.class);
			@SuppressWarnings("unchecked")
			List<Cikkfotipus> cikkfotipuslist = (List<Cikkfotipus>) pm.newQuery(cikkfotipusQuery).execute();
			if ((cikkfotipuslist != null) && (!cikkfotipuslist.isEmpty())) {
				for (Cikkfotipus l : cikkfotipuslist) {
					l.setSzinkron(Boolean.FALSE);
				}
			}

			Query cikkaltipusQuery = pm.newQuery(Cikkaltipus.class);
			@SuppressWarnings("unchecked")
			List<Cikkaltipus> cikkaltipuslist = (List<Cikkaltipus>) pm.newQuery(cikkaltipusQuery).execute();
			if ((cikkaltipuslist != null) && (!cikkaltipuslist.isEmpty())) {
				for (Cikkaltipus l : cikkaltipuslist) {
					l.setSzinkron(Boolean.FALSE);
				}
			}

			Query cikkQuery = pm.newQuery(Cikk.class);
			@SuppressWarnings("unchecked")
			List<Cikk> cikklist = (List<Cikk>) pm.newQuery(cikkQuery).execute();
			if ((cikklist != null) && (!cikklist.isEmpty())) {
				for (Cikk l : cikklist) {
					l.setSzinkron(Boolean.FALSE);
				}
			}

			Query kepQuery = pm.newQuery(Kep.class);
			@SuppressWarnings("unchecked")
			List<Kep> keplist = (List<Kep>) pm.newQuery(kepQuery).execute();
			if ((keplist != null) && (!keplist.isEmpty())) {
				for (Kep l : keplist) {
					l.setSzinkron(Boolean.FALSE);
				}
			}

			Query vevoQuery = pm.newQuery(Vevo.class);
			@SuppressWarnings("unchecked")
			List<Vevo> vevolist = (List<Vevo>) pm.newQuery(vevoQuery).execute();
			if ((vevolist != null) && (!vevolist.isEmpty())) {
				for (Vevo l : vevolist) {
					l.setSzinkron(Boolean.FALSE);
				}
			}

			Query rendeltQuery = pm.newQuery(Rendelt.class);
			rendeltQuery.deletePersistentAll();

			Query rendeltcikkQuery = pm.newQuery(Rendeltcikk.class);
			rendeltcikkQuery.deletePersistentAll();

			Client client = Client.create();
			client.setConnectTimeout(ServerConstants.TIMEOUT);

			WebResource webResource = client.resource(ServerConstants.URI);

			ClientResponse response = webResource.path("syncron")
					.queryParam("akcio", "szinkron").type(MediaType.TEXT_PLAIN)
					.get(ClientResponse.class);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			output = response.getEntity(String.class);

		} finally {
			pm.close();
		}

		return output;
	}

	public String initUploadFileStatus() throws IllegalArgumentException {

		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();

		session.removeAttribute(ServerConstants.FILE);
		session.removeAttribute(ServerConstants.FILE_ERROR);
		session.setAttribute(ServerConstants.FILE, new Integer(0));

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
			} else {
				uploadSer.setStatus(Constants.ERROR);
				uploadSer.setError((String) session
						.getAttribute(ServerConstants.FILE_ERROR));
			}
		}

		return uploadSer;
	}

	public List<String> getKep(String cikkszam)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<String> kepsorszam = new ArrayList<String>();

		try {
			Query query = pm.newQuery(Kep.class);
			query.setFilter("(this.cikkszam == pcikkszam) && (this.torolt == false)");
			query.declareParameters("String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Kep> list = (List<Kep>) pm.newQuery(query).execute(cikkszam);
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

	public String removeKep(String cikkszam, String sorszam)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Kep.class);
			query.setFilter("(this.cikkszam == pcikkszam) && (this.sorszam == psorszam)");
			query.declareParameters("String pcikkszam,String psorszam");
			@SuppressWarnings("unchecked")
			List<Kep> list = (List<Kep>) pm.newQuery(query).execute(cikkszam,
					sorszam);
			if ((list != null) && (!list.isEmpty())) {
				for (Kep l : list) {
					l.setSzinkron(Boolean.FALSE);
					l.setTorolt(Boolean.TRUE);
				}
			}

			Query query1 = pm.newQuery(Cikk.class);
			query1.setFilter("cikkszam == pcikkszam");
			query1.declareParameters("String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Cikk> list1 = (List<Cikk>) pm.newQuery(query1).execute(
					cikkszam);
			if ((list1 != null) && (!list1.isEmpty())) {
				for (Cikk l1 : list1) {
					int kep = l1.getKepek();
					if (kep > 0) {
						kep--;
						l1.setKepek(kep);
					}
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return "OK";
	}

	public List<CikkfotipusSer> getCikkfotipus()
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<CikkfotipusSer> cikkfotipus = new ArrayList<CikkfotipusSer>();

		try {
			Query query = pm.newQuery(Cikkfotipus.class);
			@SuppressWarnings("unchecked")
			List<Cikkfotipus> list = (List<Cikkfotipus>) pm.newQuery(query)
					.execute();
			if (!list.isEmpty()) {
				for (Cikkfotipus l : list) {
					CikkfotipusSer cikkfotipusSer = new CikkfotipusSer();
					cikkfotipusSer.setKod(l.getKod());
					cikkfotipusSer.setNev(l.getNev());
					cikkfotipus.add(cikkfotipusSer);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkfotipus;
	}

	public CikkfotipusSer addCikkfotipus(CikkfotipusSer cikkfotipusSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Cikkfotipus.class);
			@SuppressWarnings("unchecked")
			List<Cikkfotipus> list = (List<Cikkfotipus>) pm.newQuery(query).execute();

			Integer kod = Integer.valueOf(list.size() + 1);
			Cikkfotipus cikkfotipus = new Cikkfotipus(kod.toString(),
					cikkfotipusSer.getNev(),Boolean.FALSE);
			pm.makePersistent(cikkfotipus);

			cikkfotipusSer.setKod(kod.toString());
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkfotipusSer;
	}

	public CikkfotipusSer updateCikkfotipus(CikkfotipusSer cikkfotipusSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Cikkfotipus.class);
			query.setFilter("this.kod == pkod");
			query.declareParameters("String pkod");
			@SuppressWarnings("unchecked")
			List<Cikkfotipus> list = (List<Cikkfotipus>) pm.newQuery(query).execute(
					cikkfotipusSer.getKod());
			if ((list != null) && (!list.isEmpty())) {
				for (Cikkfotipus l : list) {
					l.setNev(cikkfotipusSer.getNev());
					l.setSzinkron(Boolean.FALSE);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkfotipusSer;
	}

	public List<CikkaltipusSer> getCikkaltipus(String fokod)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<CikkaltipusSer> cikkaltipus = new ArrayList<CikkaltipusSer>();

		try {
			Query query = pm.newQuery(Cikkaltipus.class);
			query.setFilter("this.fokod == pfokod");
			query.declareParameters("String pfokod");
			@SuppressWarnings("unchecked")
			List<Cikkaltipus> list = (List<Cikkaltipus>) pm.newQuery(query)
					.execute(fokod);
			if (!list.isEmpty()) {
				for (Cikkaltipus l : list) {
					CikkaltipusSer cikkaltipusSer = new CikkaltipusSer();
					cikkaltipusSer.setFokod(l.getFokod());
					cikkaltipusSer.setKod(l.getKod());
					cikkaltipusSer.setNev(l.getNev());
					cikkaltipus.add(cikkaltipusSer);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkaltipus;
	}

	public CikkaltipusSer addCikkaltipus(CikkaltipusSer cikkaltipusSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Cikkaltipus.class);
			@SuppressWarnings("unchecked")
			List<Cikkaltipus> list = (List<Cikkaltipus>) pm.newQuery(query).execute();

			Integer kod = Integer.valueOf(list.size() + 1);
			Cikkaltipus cikkaltipus = new Cikkaltipus(cikkaltipusSer.getFokod(),kod.toString(),
					cikkaltipusSer.getNev(),Boolean.FALSE);
			pm.makePersistent(cikkaltipus);

			cikkaltipusSer.setKod(kod.toString());
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkaltipusSer;
	}

	public CikkaltipusSer updateCikkaltipus(CikkaltipusSer cikkaltipusSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Cikkaltipus.class);
			query.setFilter("(this.fokod == pfokod) && (this.kod == pkod)");
			query.declareParameters("String pfokod,String pkod");
			@SuppressWarnings("unchecked")
			List<Cikkaltipus> list = (List<Cikkaltipus>) pm.newQuery(query).execute(
					cikkaltipusSer.getFokod(),cikkaltipusSer.getKod());
			if ((list != null) && (!list.isEmpty())) {
				for (Cikkaltipus l : list) {
					l.setNev(cikkaltipusSer.getNev());
					l.setSzinkron(Boolean.FALSE);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkaltipusSer;
	}

	public CikkSelectsSer getCikkSelects()
			throws IllegalArgumentException, SQLExceptionSer {
		
		CikkSelectsSer cikkSelectsSer = new CikkSelectsSer();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			Query query = pm.newQuery(Cikkfotipus.class);
			@SuppressWarnings("unchecked")
			List<Cikkfotipus> list = (List<Cikkfotipus>) pm.newQuery(query)
					.execute();
			if (!list.isEmpty()) {
				for (Cikkfotipus l : list) {
					cikkSelectsSer.getFotipus().put(l.getKod(), l.getNev());
					
					Query subquery = pm.newQuery(Cikkaltipus.class);
					subquery.setFilter("this.fokod == pfokod");
					subquery.declareParameters("String pfokod");
					@SuppressWarnings("unchecked")
					List<Cikkaltipus> sublist = (List<Cikkaltipus>) pm.newQuery(subquery)
							.execute(l.getKod());
					
					if (!sublist.isEmpty()) {
						LinkedHashMap<String, String> altipus = new LinkedHashMap<String, String>();
						for (Cikkaltipus subl :sublist) {
							altipus.put(subl.getKod(), subl.getNev());
							cikkSelectsSer.getAltipus().put(subl.getKod(), subl.getNev());
						}	
						cikkSelectsSer.getTipus().put(l.getKod(),altipus);
					}
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}
		
		return cikkSelectsSer;
	}

}
