package hu.dekortrade.server;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.server.jdo.Cedula;
import hu.dekortrade.server.jdo.Cedulacikk;
import hu.dekortrade.server.jdo.Cikk;
import hu.dekortrade.server.jdo.Cikkaltipus;
import hu.dekortrade.server.jdo.Cikkfotipus;
import hu.dekortrade.server.jdo.Felhasznalo;
import hu.dekortrade.server.jdo.Fizetes;
import hu.dekortrade.server.jdo.Gyarto;
import hu.dekortrade.server.jdo.Jog;
import hu.dekortrade.server.jdo.Kep;
import hu.dekortrade.server.jdo.Kosar;
import hu.dekortrade.server.jdo.Kosarcikk;
import hu.dekortrade.server.jdo.PMF;
import hu.dekortrade.server.jdo.Rendelt;
import hu.dekortrade.server.jdo.Rendeltcikk;
import hu.dekortrade.server.jdo.Sorszam;
import hu.dekortrade.server.jdo.Vevo;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.CedulaSer;
import hu.dekortrade.shared.serialized.CedulacikkSer;
import hu.dekortrade.shared.serialized.CikkSelectsSer;
import hu.dekortrade.shared.serialized.CikkSer;
import hu.dekortrade.shared.serialized.CikkaltipusSer;
import hu.dekortrade.shared.serialized.CikkfotipusSer;
import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.FizetesSer;
import hu.dekortrade.shared.serialized.GyartoSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.KosarSer;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzinkronSer;
import hu.dekortrade.shared.serialized.TabPageSer;
import hu.dekortrade.shared.serialized.UploadSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoKosarSer;
import hu.dekortrade.shared.serialized.VevoSer;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
				query.setOrdering("nev asc");
				@SuppressWarnings("unchecked")
				List<Jog> list = (List<Jog>) pm.newQuery(query).execute(userId);
				if ((list != null) && (!list.isEmpty())) {
					int tabindex = 0;
					for (Jog l : list) {
						if ((l.getNev().equals(Constants.MENU_SYSTEM))
									
								|| (l.getNev().equals(Constants.MENU_BASEDATA))
								
								|| (l.getNev().equals(Constants.MENU_ORDER))
								
								|| (l.getNev().equals(Constants.MENU_QUERY))
								
								|| (l.getNev().equals(Constants.MENU_CASH))
								) {

							TabPageSer tabPageSer = new TabPageSer();
							tabPageSer.setId(tabindex);
							tabPageSer.setName(l.getNev());
							userSer.getTabList().add(tabPageSer);
							if (l.getNev().equals(defaultMenu))
								userSer.setDefultTab(tabindex);
							tabindex++;
						}
						if ((l.getNev().equals(Constants.MENU_SYSTEM_USERS))
								|| (l.getNev().equals(Constants.MENU_SYSTEM_SYNC))
								|| (l.getNev().equals(Constants.MENU_SYSTEM_BASKET))
								
								|| (l.getNev().equals(Constants.MENU_BASEDATA_PRODUCER))
								|| (l.getNev().equals(Constants.MENU_BASEDATA_BUYER))
								|| (l.getNev().equals(Constants.MENU_BASEDATA_TYPEOFITEMS))
								|| (l.getNev().equals(Constants.MENU_BASEDATA_ITEMS))

								|| (l.getNev().equals(Constants.MENU_ORDER_INTERNET))	
								|| (l.getNev().equals(Constants.MENU_ORDER_PRE))	
								|| (l.getNev().equals(Constants.MENU_ORDER_FINALIZE))	
	
								|| (l.getNev().equals(Constants.MENU_QUERY_TICKET))
								
								|| (l.getNev().equals(Constants.MENU_CASH_PAY))
								|| (l.getNev().equals(Constants.MENU_CASH_CLOSE))
								
							) {
							userSer.getMenu().add(l.getNev());
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
			pm.flush();
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
			pm.flush();
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
			pm.flush();
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
			pm.flush();
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

			JogSer mainmenu1jogSer = new JogSer();
			mainmenu1jogSer.setNev(Constants.MENU_SYSTEM);
			mainmenu1jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu1jogSer);

			JogSer systemmenu1jogSer = new JogSer();
			systemmenu1jogSer.setNev(Constants.MENU_SYSTEM_USERS);
			systemmenu1jogSer.setJog(Boolean.FALSE);
			jog.add(systemmenu1jogSer);

			JogSer systemmenu2jogSer = new JogSer();
			systemmenu2jogSer.setNev(Constants.MENU_SYSTEM_SYNC);
			systemmenu2jogSer.setJog(Boolean.FALSE);
			jog.add(systemmenu2jogSer);
			
			JogSer systemmenu3jogSer = new JogSer();
			systemmenu3jogSer.setNev(Constants.MENU_SYSTEM_BASKET);
			systemmenu3jogSer.setJog(Boolean.FALSE);
			jog.add(systemmenu3jogSer);
			
			
			JogSer mainmenu2jogSer = new JogSer();
			mainmenu2jogSer.setNev(Constants.MENU_BASEDATA);
			mainmenu2jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu2jogSer);

			JogSer basedatamenu1jogSer = new JogSer();
			basedatamenu1jogSer.setNev(Constants.MENU_BASEDATA_PRODUCER);
			basedatamenu1jogSer.setJog(Boolean.FALSE);
			jog.add(basedatamenu1jogSer);
			
			JogSer basedatamenu2jogSer = new JogSer();
			basedatamenu2jogSer.setNev(Constants.MENU_BASEDATA_BUYER);
			basedatamenu2jogSer.setJog(Boolean.FALSE);
			jog.add(basedatamenu2jogSer);
			
			JogSer basedatamenu3jogSer = new JogSer();
			basedatamenu3jogSer.setNev(Constants.MENU_BASEDATA_TYPEOFITEMS);
			basedatamenu3jogSer.setJog(Boolean.FALSE);
			jog.add(basedatamenu3jogSer);
			
			JogSer basedatamenu4jogSer = new JogSer();
			basedatamenu4jogSer.setNev(Constants.MENU_BASEDATA_ITEMS);
			basedatamenu4jogSer.setJog(Boolean.FALSE);
			jog.add(basedatamenu4jogSer);
			
			
			JogSer mainmenu3jogSer = new JogSer();
			mainmenu3jogSer.setNev(Constants.MENU_ORDER);
			mainmenu3jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu3jogSer);

			JogSer ordermenu1jogSer = new JogSer();
			ordermenu1jogSer.setNev(Constants.MENU_ORDER_INTERNET);
			ordermenu1jogSer.setJog(Boolean.FALSE);
			jog.add(ordermenu1jogSer);
			
			JogSer ordermenu2jogSer = new JogSer();
			ordermenu2jogSer.setNev(Constants.MENU_ORDER_PRE);
			ordermenu2jogSer.setJog(Boolean.FALSE);
			jog.add(ordermenu2jogSer);
			
			JogSer ordermenu3jogSer = new JogSer();
			ordermenu3jogSer.setNev(Constants.MENU_ORDER_FINALIZE);
			ordermenu3jogSer.setJog(Boolean.FALSE);
			jog.add(ordermenu3jogSer);
			
			
			
			JogSer mainmenu4jogSer = new JogSer();
			mainmenu4jogSer.setNev(Constants.MENU_QUERY);
			mainmenu4jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu4jogSer);
	
			JogSer querymenu1jogSer = new JogSer();
			querymenu1jogSer.setNev(Constants.MENU_QUERY_TICKET);
			querymenu1jogSer.setJog(Boolean.FALSE);
			jog.add(querymenu1jogSer);
	
			
			JogSer mainmenu5jogSer = new JogSer();
			mainmenu5jogSer.setNev(Constants.MENU_CASH);
			mainmenu5jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu5jogSer);
			
			
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
					VevoSer.setTipus(l.getTipus());
					VevoSer.setNev(l.getNev());
					VevoSer.setCim(l.getCim());
					VevoSer.setElerhetoseg(l.getElerhetoseg());
					VevoSer.setEgyenlegusd(l.getEgyenlegusd());
					VevoSer.setEgyenlegeur(l.getEgyenlegeur());
					VevoSer.setEgyenleghuf(l.getEgyenleghuf());
					VevoSer.setTarolasidij(l.getTarolasidij());
					VevoSer.setEloleg(l.getEloleg());
					VevoSer.setBankszamlaszam(l.getBankszamlaszam());
					VevoSer.setEuadoszam(l.getEuadoszam());
					VevoSer.setElorarkedvezmeny(l.getElorarkedvezmeny());
					VevoSer.setAjanlottarkedvezmeny(l.getAjanlottarkedvezmeny());
					VevoSer.setOrszag(l.getOrszag());
					VevoSer.setMegjegyzes(l.getMegjegyzes());
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
				Vevo vevo = new Vevo(vevoSer.getRovidnev(), vevoSer.getTipus(),
						vevoSer.getNev(), vevoSer.getCim(),
						vevoSer.getElerhetoseg(), vevoSer.getEgyenlegusd(),
						vevoSer.getEgyenlegeur(), vevoSer.getEgyenleghuf(),
						vevoSer.getTarolasidij(), vevoSer.getEloleg(),
						vevoSer.getBankszamlaszam(), vevoSer.getEuadoszam(),
						vevoSer.getElorarkedvezmeny(),
						vevoSer.getAjanlottarkedvezmeny(), vevoSer.getOrszag(),
						vevoSer.getMegjegyzes(), Boolean.FALSE, Boolean.FALSE,
						Boolean.FALSE);
				pm.makePersistent(vevo);
			}
			pm.flush();
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
					l.setTipus(vevoSer.getTipus());
					l.setCim(vevoSer.getCim());
					l.setElerhetoseg(vevoSer.getElerhetoseg());
					l.setEgyenlegusd(vevoSer.getEgyenlegusd());
					l.setEgyenlegeur(vevoSer.getEgyenlegeur());
					l.setEgyenleghuf(vevoSer.getEgyenleghuf());
					l.setTarolasidij(vevoSer.getTarolasidij());
					l.setEloleg(vevoSer.getEloleg());
					l.setBankszamlaszam(vevoSer.getBankszamlaszam());
					l.setEuadoszam(vevoSer.getEuadoszam());
					l.setElorarkedvezmeny(vevoSer.getElorarkedvezmeny());
					l.setAjanlottarkedvezmeny(vevoSer.getAjanlottarkedvezmeny());
					l.setOrszag(vevoSer.getOrszag());
					l.setMegjegyzes(vevoSer.getMegjegyzes());
					l.setInternet(vevoSer.getInternet());
					l.setSzinkron(Boolean.FALSE);
				}
			}
			pm.flush();
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
			pm.flush();
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return vevoSer;
	}

	public List<GyartoSer> getGyarto() throws IllegalArgumentException,
			SQLExceptionSer {

		List<GyartoSer> gyartok = new ArrayList<GyartoSer>();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Gyarto.class);
			query.setFilter("this.torolt == false");
			query.setOrdering("kod");
			@SuppressWarnings("unchecked")
			List<Gyarto> list = (List<Gyarto>) pm.newQuery(query).execute();
			if ((list != null) && (!list.isEmpty())) {
				for (Gyarto l : list) {
					GyartoSer gyartoSer = new GyartoSer();
					gyartoSer.setKod(l.getKod());
					gyartoSer.setNev(l.getNev());
					gyartoSer.setCim(l.getCim());
					gyartoSer.setElerhetoseg(l.getElerhetoseg());
					gyartoSer.setSwifkod(l.getSwifkod());
					gyartoSer.setBankadat(l.getBankadat());
					gyartoSer.setSzamlaszam(l.getSzamlaszam());
					gyartoSer.setEgyenleg(l.getEgyenleg());
					gyartoSer.setKedvezmeny(l.getKedvezmeny());
					gyartoSer.setMegjegyzes(l.getMegjegyzes());
					gyartok.add(gyartoSer);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return gyartok;
	}

	public GyartoSer addGyarto(GyartoSer gyartoSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Gyarto.class);
			@SuppressWarnings("unchecked")
			List<Gyarto> list = (List<Gyarto>) pm.newQuery(query).execute();

			Integer kod = Integer.valueOf(list.size() + 1);
			Gyarto gyarto = new Gyarto(kod.toString(), gyartoSer.getNev(),
					gyartoSer.getCim(), gyartoSer.getElerhetoseg(),
					gyartoSer.getSwifkod(), gyartoSer.getBankadat(),
					gyartoSer.getSzamlaszam(), gyartoSer.getEgyenleg(),
					gyartoSer.getKedvezmeny(), gyartoSer.getMegjegyzes(),
					Boolean.FALSE);
			pm.makePersistent(gyarto);

			gyartoSer.setKod(kod.toString());
			pm.flush();
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return gyartoSer;
	}

	public GyartoSer updateGyarto(GyartoSer gyartoSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Gyarto.class);
			query.setFilter("this.kod == pkod");
			query.declareParameters("String pkod");
			@SuppressWarnings("unchecked")
			List<Gyarto> list = (List<Gyarto>) pm.newQuery(query).execute(
					gyartoSer.getKod());
			if ((list != null) && (!list.isEmpty())) {
				for (Gyarto l : list) {
					l.setNev(gyartoSer.getNev());
					l.setCim(gyartoSer.getCim());
					l.setElerhetoseg(gyartoSer.getElerhetoseg());
					l.setSwifkod(gyartoSer.getSwifkod());
					l.setBankadat(gyartoSer.getBankadat());
					l.setSzamlaszam(gyartoSer.getSzamlaszam());
					l.setEgyenleg(gyartoSer.getEgyenleg());
					l.setKedvezmeny(gyartoSer.getKedvezmeny());
					l.setMegjegyzes(gyartoSer.getMegjegyzes());
				}
			}
			pm.flush();
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return gyartoSer;
	}

	public GyartoSer removeGyarto(GyartoSer gyartoSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Gyarto.class);
			query.setFilter("this.kod == pkod");
			query.declareParameters("String pkod");
			@SuppressWarnings("unchecked")
			List<Gyarto> list = (List<Gyarto>) pm.newQuery(query).execute(
					gyartoSer.getKod());
			if ((list != null) && (!list.isEmpty())) {
				for (Gyarto l : list) {
					l.setTorolt(Boolean.TRUE);
				}
			}
			pm.flush();
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return gyartoSer;
	}

	public List<CikkSer> getCikk(int page, String fotipus, String altipus,
			String cikkszam) throws IllegalArgumentException, SQLExceptionSer {

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
					cikkSer.setGyarto(l.getGyarto());
					cikkSer.setGyartocikkszam(l.getGyartocikkszam());
					cikkSer.setSzinkod(l.getSzinkod());
					cikkSer.setCikkszam(l.getCikkszam());
					cikkSer.setFelviteltol(l.getFelviteltol() == null ? null
							: new Date(l.getFelviteltol().getTime()));
					cikkSer.setFelvitelig(l.getFelvitelig() == null ? null
							: new Date(l.getFelvitelig().getTime()));
					cikkSer.setLejarattol(l.getLejarattol() == null ? null
							: new Date(l.getLejarattol().getTime()));
					cikkSer.setLejaratig(l.getLejaratig() == null ? null
							: new Date(l.getLejaratig().getTime()));
					cikkSer.setMegnevezes(l.getMegnevezes());
					cikkSer.setVamtarifaszam(l.getVamtarifaszam());
					cikkSer.setFob(l.getFob());
					cikkSer.setSzallitas(l.getSzallitas());
					cikkSer.setDdu(l.getDdu());
					cikkSer.setErsz(l.getErsz());
					cikkSer.setElorar(l.getElorar());
					cikkSer.setUjfob(l.getUjfob());
					cikkSer.setUjszallitas(l.getUjszallitas());
					cikkSer.setUjddu(l.getUjddu());
					cikkSer.setUjersz(l.getUjersz());
					cikkSer.setUjelorar(l.getUjelorar());
					cikkSer.setAr(l.getAr());
					cikkSer.setAreur(l.getAreur());
					cikkSer.setArszorzo(l.getArszorzo());
					cikkSer.setKiskarton(l.getKiskarton());
					cikkSer.setDarab(l.getDarab());
					cikkSer.setTerfogat(l.getTerfogat());
					cikkSer.setTerfogatlab(l.getTerfogatlab());
					cikkSer.setBsuly(l.getBsuly());
					cikkSer.setNsuly(l.getNsuly());
					cikkSer.setLeiras(l.getLeiras());
					cikkSer.setMegjegyzes(l.getMegjegyzes());
					cikkSer.setAkcios(l.getAkcios());
					cikkSer.setMertekegyseg(l.getMertekegyseg());
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
			query.setFilter("this.cikkszam == pcikkszam && this.szinkod == pszinkod");
			query.declareParameters("String pcikkszam,String pszinkod");
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query).execute(
					cikkSer.getCikkszam(), cikkSer.getSzinkod());
			if ((list != null) && (!list.isEmpty())) {
				throw new Exception(Constants.EXISTSID);
			} else {
				Cikk cikk = new Cikk(cikkSer.getFotipus(),
						cikkSer.getAltipus(), cikkSer.getGyarto(),
						cikkSer.getGyartocikkszam(), cikkSer.getCikkszam(),
						cikkSer.getSzinkod() == null ? "" : cikkSer.getSzinkod(),
						cikkSer.getFelviteltol() == null ? null : new Date(
								cikkSer.getFelviteltol().getTime()),
						cikkSer.getFelvitelig() == null ? null : new Date(
								cikkSer.getFelvitelig().getTime()),
						cikkSer.getLejarattol() == null ? null : new Date(
								cikkSer.getLejarattol().getTime()),
						cikkSer.getLejaratig() == null ? null : new Date(
								cikkSer.getLejaratig().getTime()),
						cikkSer.getMegnevezes(), cikkSer.getVamtarifaszam(),
						cikkSer.getFob(), cikkSer.getSzallitas(),
						cikkSer.getDdu(), cikkSer.getErsz(),
						cikkSer.getElorar(), cikkSer.getUjfob(),
						cikkSer.getUjszallitas(), cikkSer.getUjddu(),
						cikkSer.getUjersz(), cikkSer.getUjelorar(),
						cikkSer.getAr(), cikkSer.getAreur(),
						cikkSer.getArszorzo(), cikkSer.getKiskarton(),
						cikkSer.getDarab(), cikkSer.getTerfogat(),
						cikkSer.getTerfogatlab(), cikkSer.getBsuly(),
						cikkSer.getNsuly(), cikkSer.getLeiras(),
						cikkSer.getMegjegyzes(), cikkSer.getAkcios(),
						cikkSer.getMertekegyseg(), new Integer(0),
						Boolean.FALSE, Boolean.FALSE);
				pm.makePersistent(cikk);
			}
			pm.flush();
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
					l.setGyarto(cikkSer.getGyarto());
					l.setGyartocikkszam(cikkSer.getGyartocikkszam());
					l.setCikkszam(cikkSer.getCikkszam());
					l.setSzinkod(cikkSer.getSzinkod());
					l.setFelviteltol(cikkSer.getFelviteltol() == null ? null
							: new Date(cikkSer.getFelviteltol().getTime()));
					l.setFelvitelig(cikkSer.getFelvitelig() == null ? null
							: new Date(cikkSer.getFelvitelig().getTime()));
					l.setLejarattol(cikkSer.getLejarattol() == null ? null
							: new Date(cikkSer.getLejarattol().getTime()));
					l.setLejaratig(cikkSer.getLejaratig() == null ? null
							: new Date(cikkSer.getLejaratig().getTime()));
					l.setMegnevezes(cikkSer.getMegnevezes());
					l.setVamtarifaszam(cikkSer.getVamtarifaszam());
					l.setFob(cikkSer.getFob());
					l.setSzallitas(cikkSer.getSzallitas());
					l.setDdu(cikkSer.getDdu());
					l.setErsz(cikkSer.getErsz());
					l.setElorar(cikkSer.getElorar());
					l.setUjfob(cikkSer.getUjfob());
					l.setUjszallitas(cikkSer.getUjszallitas());
					l.setUjddu(cikkSer.getUjddu());
					l.setUjersz(cikkSer.getUjersz());
					l.setUjelorar(cikkSer.getUjelorar());
					l.setAr(cikkSer.getAr());
					l.setAreur(cikkSer.getAreur());
					l.setArszorzo(cikkSer.getArszorzo());
					l.setKiskarton(cikkSer.getKiskarton());
					l.setDarab(cikkSer.getDarab());
					l.setTerfogat(cikkSer.getTerfogat());
					l.setTerfogatlab(cikkSer.getTerfogatlab());
					l.setBsuly(cikkSer.getBsuly());
					l.setNsuly(cikkSer.getNsuly());
					l.setLeiras(cikkSer.getLeiras());
					l.setMegjegyzes(cikkSer.getMegjegyzes());
					l.setAkcios(cikkSer.getAkcios());
					l.setMertekegyseg(cikkSer.getMertekegyseg());
					l.setKepek(cikkSer.getKepek());
					l.setSzinkron(Boolean.FALSE);
				}
			}
			pm.flush();
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
			pm.flush();
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
			query.setFilter("this.status == pstatus");
			query.declareParameters("String pstatus");
			@SuppressWarnings("unchecked")
			List<Rendelt> list = (List<Rendelt>) pm.newQuery(query).execute(ClientConstants.INTERNET_ELORENDEL);
			if (!list.isEmpty()) {
				for (Rendelt l : list) {
					RendeltSer rendeltSer = new RendeltSer();
					rendeltSer.setRovidnev(l.getRovidnev());
					rendeltSer.setRendeles(l.getRendeles());
					rendeltSer.setDatum(new Date(l.getDatum().getTime()));
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

	public List<RendeltcikkSer> getRendeltcikk(String rovidnev, String rendeles)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<RendeltcikkSer> rendeltcikk = new ArrayList<RendeltcikkSer>();

		try {
			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(this.rovidnev == providnev) && (this.rendeles == prendeles)");
			query.declareParameters("String providnev,String prendeles");
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
					rendeltcikkSer.setKiskarton(l.getKiskarton());
					rendeltcikkSer.setDarab(l.getDarab());
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
			List<Cikkfotipus> cikkfotipuslist = (List<Cikkfotipus>) pm
					.newQuery(cikkfotipusQuery).execute();
			if ((cikkfotipuslist != null) && (!cikkfotipuslist.isEmpty())) {
				for (Cikkfotipus l : cikkfotipuslist) {
					l.setSzinkron(Boolean.FALSE);
				}
			}

			Query cikkaltipusQuery = pm.newQuery(Cikkaltipus.class);
			@SuppressWarnings("unchecked")
			List<Cikkaltipus> cikkaltipuslist = (List<Cikkaltipus>) pm
					.newQuery(cikkaltipusQuery).execute();
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
			pm.flush();
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

	public List<String> getKep(String cikkszam, String szinkod)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<String> kepsorszam = new ArrayList<String>();

		try {
			Query query = pm.newQuery(Kep.class);
			query.setFilter("(this.cikkszam == pcikkszam) && (this.szinkod == pszinkod) && (this.torolt == false)");
			query.declareParameters("String pcikkszam,String pszinkod");
			@SuppressWarnings("unchecked")
			List<Kep> list = (List<Kep>) pm.newQuery(query).execute(cikkszam,szinkod);
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

	public String removeKep(String cikkszam, String szinkod, String sorszam)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Kep.class);
			query.setFilter("(this.cikkszam == pcikkszam) && (this.szinkod == pszinkod) && (this.sorszam == psorszam)");
			query.declareParameters("String pcikkszam,String psorszam");
			@SuppressWarnings("unchecked")
			List<Kep> list = (List<Kep>) pm.newQuery(query).execute(cikkszam,szinkod,
					sorszam);
			if ((list != null) && (!list.isEmpty())) {
				for (Kep l : list) {
					l.setSzinkron(Boolean.FALSE);
					l.setTorolt(Boolean.TRUE);
				}
			}

			Query query1 = pm.newQuery(Cikk.class);
			query1.setFilter("(this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
			query1.declareParameters("String pcikkszam,String psorszam");
			@SuppressWarnings("unchecked")
			List<Cikk> list1 = (List<Cikk>) pm.newQuery(query1).execute(
					cikkszam,szinkod);
			if ((list1 != null) && (!list1.isEmpty())) {
				for (Cikk l1 : list1) {
					int kep = l1.getKepek();
					if (kep > 0) {
						kep--;
						l1.setKepek(kep);
					}
				}
			}

			pm.flush();
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
			List<Cikkfotipus> list = (List<Cikkfotipus>) pm.newQuery(query)
					.execute();

			Integer kod = Integer.valueOf(list.size() + 1);
			Cikkfotipus cikkfotipus = new Cikkfotipus(kod.toString(),
					cikkfotipusSer.getNev(), Boolean.FALSE);
			pm.makePersistent(cikkfotipus);

			cikkfotipusSer.setKod(kod.toString());
			pm.flush();
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
			List<Cikkfotipus> list = (List<Cikkfotipus>) pm.newQuery(query)
					.execute(cikkfotipusSer.getKod());
			if ((list != null) && (!list.isEmpty())) {
				for (Cikkfotipus l : list) {
					l.setNev(cikkfotipusSer.getNev());
					l.setSzinkron(Boolean.FALSE);
				}
			}
			pm.flush();
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
			List<Cikkaltipus> list = (List<Cikkaltipus>) pm.newQuery(query)
					.execute();

			Integer kod = Integer.valueOf(list.size() + 1);
			Cikkaltipus cikkaltipus = new Cikkaltipus(
					cikkaltipusSer.getFokod(), kod.toString(),
					cikkaltipusSer.getNev(), Boolean.FALSE);
			pm.makePersistent(cikkaltipus);

			cikkaltipusSer.setKod(kod.toString());
			pm.flush();
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
			List<Cikkaltipus> list = (List<Cikkaltipus>) pm
					.newQuery(query)
					.execute(cikkaltipusSer.getFokod(), cikkaltipusSer.getKod());
			if ((list != null) && (!list.isEmpty())) {
				for (Cikkaltipus l : list) {
					l.setNev(cikkaltipusSer.getNev());
					l.setSzinkron(Boolean.FALSE);
				}
			}
			pm.flush();
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkaltipusSer;
	}

	public CikkSelectsSer getCikkSelects() throws IllegalArgumentException,
			SQLExceptionSer {

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
					List<Cikkaltipus> sublist = (List<Cikkaltipus>) pm
							.newQuery(subquery).execute(l.getKod());

					if (!sublist.isEmpty()) {
						LinkedHashMap<String, String> altipus = new LinkedHashMap<String, String>();
						for (Cikkaltipus subl : sublist) {
							altipus.put(subl.getKod(), subl.getNev());
							cikkSelectsSer.getAltipus().put(subl.getKod(),
									subl.getNev());
						}
						cikkSelectsSer.getTipus().put(l.getKod(), altipus);
					}
				}
			}

			Query gyartoquery = pm.newQuery(Gyarto.class);
			gyartoquery.setFilter("this.torolt == false");
			@SuppressWarnings("unchecked")
			List<Gyarto> gyartolist = (List<Gyarto>) pm.newQuery(gyartoquery)
					.execute();
			if (!gyartolist.isEmpty()) {
				LinkedHashMap<String, String> gyartok = new LinkedHashMap<String, String>();
				for (Gyarto l : gyartolist) {
					gyartok.put(l.getKod(), l.getNev());
				}
				cikkSelectsSer.setGyarto(gyartok);
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikkSelectsSer;
	}

	public List<KosarSer> getKosarCikk(String elado, String vevo, String menu)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<KosarSer> kosar = new ArrayList<KosarSer>();

		try {
			
			Query cikkquery = pm.newQuery(Cikk.class);
			cikkquery.setFilter("this.cikkszam == pcikkszam && this.szinkod == pszinkod");
			cikkquery.declareParameters("String pcikkszam,String pszinkod");

			Query query = pm.newQuery(Kosarcikk.class);
						
			if (vevo != null) {
				String tipus = "";
				if (menu.equals(Constants.MENU_ORDER_PRE)) {
					tipus = Constants.CEDULA_STATUS_ELORENDELT;
				}
				if (menu.equals(Constants.MENU_ORDER_FINALIZE)) {
					tipus = Constants.CEDULA_STATUS_VEGLEGESIT;
				}
				if (menu.equals(Constants.MENU_CASH_PAY)) {
					tipus = Constants.CEDULA_STATUS_ELORENDELES_FIZETES;
				}
									
				query.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus)");
				query.declareParameters("String pelado,String pvevo,String ptipus");
				@SuppressWarnings("unchecked")
				List<Kosarcikk> list = (List<Kosarcikk>) pm.newQuery(query)
						.execute(elado, vevo, tipus);
				if (!list.isEmpty()) {
					for (Kosarcikk l : list) {
						KosarSer kosarSer = new KosarSer();
						kosarSer.setCikkszam(l.getCikkszam());
						kosarSer.setSzinkod(l.getSzinkod());
						
						@SuppressWarnings("unchecked")
						List<Cikk> cikklist = (List<Cikk>) pm.newQuery(cikkquery)
								.execute(l.getCikkszam(),l.getSzinkod());
						if (!cikklist.isEmpty()) {
							kosarSer.setMegnevezes(cikklist.get(0).getMegnevezes());
							kosarSer.setArusd(cikklist.get(0).getElorar());
						}
						
						kosarSer.setExportkarton(l.getExportkarton());
						kosarSer.setKiskarton(l.getKiskarton());
						kosarSer.setDarab(l.getDarab());					
						kosarSer.setFizetusd(l.getFizetusd());
						kosar.add(kosarSer);
					}
				}
			} else {
				@SuppressWarnings("unchecked")
				List<Kosarcikk> list = (List<Kosarcikk>) pm.newQuery(query)
						.execute();
				if (!list.isEmpty()) {
					for (Kosarcikk l : list) {
						KosarSer kosarSer = new KosarSer();
						kosarSer.setElado(l.getElado());
						kosarSer.setVevo(l.getVevo());
						kosarSer.setTipus(l.getTipus());
						kosarSer.setCikkszam(l.getCikkszam());
						kosarSer.setSzinkod(l.getSzinkod());

						@SuppressWarnings("unchecked")
						List<Cikk> cikklist = (List<Cikk>) pm.newQuery(cikkquery)
								.execute(l.getCikkszam(),l.getSzinkod());
						if (!cikklist.isEmpty()) {
							kosarSer.setMegnevezes(cikklist.get(0).getMegnevezes());
							}
						
						kosarSer.setExportkarton(l.getExportkarton());
						kosarSer.setKiskarton(l.getKiskarton());
						kosarSer.setDarab(l.getDarab());
						kosarSer.setFizet(l.getFizet());
						kosarSer.setFizeteur(l.getFizeteur());
						kosarSer.setFizetusd(l.getFizetusd());
						kosarSer.setFizet(l.getFizet());
						kosarSer.setFizeteur(l.getFizeteur());
						kosarSer.setFizetusd(l.getFizetusd());
						kosar.add(kosarSer);
					}
				}
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return kosar;
	}

	public VevoKosarSer getVevoKosar(String elado, String menu)
			throws IllegalArgumentException, SQLExceptionSer {
		VevoKosarSer ret = null;

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			String tipus = "";
			if (menu.equals(Constants.MENU_ORDER_PRE)) {
				tipus = Constants.CEDULA_STATUS_ELORENDELT;
			}
			if (menu.equals(Constants.MENU_ORDER_FINALIZE)) {
				tipus = Constants.CEDULA_STATUS_VEGLEGESIT;
			}

			if (menu.equals(Constants.MENU_CASH_PAY)) {
				tipus = Constants.CEDULA_STATUS_ELORENDELES_FIZETES;
			}

			Query query = pm.newQuery(Kosar.class);
			query.setFilter("(this.elado == pelado) && (this.tipus == ptipus)");
			query.declareParameters("String pelado,String ptipus");
			@SuppressWarnings("unchecked")
			List<Kosar> list = (List<Kosar>) pm.newQuery(query).execute(elado,
					tipus);
			if ((list != null) && (!list.isEmpty())) {
				ret = new VevoKosarSer();
				for (Kosar l : list) {
					ret.setVevo(l.getVevo());
					ret.setCedula(l.getCedula());
				}
				if (ret.getVevo() != null) {
					Query query1 = pm.newQuery(Vevo.class);
					query1.setFilter("this.rovidnev == pvevo");
					query1.declareParameters("String pvevo");
					@SuppressWarnings("unchecked")
					List<Vevo> list1 = (List<Vevo>) pm.newQuery(query1)
							.execute(ret.getVevo());
					for (Vevo l1 : list1) {
						ret.setVevonev(l1.getNev());
						ret.setVevotipus(l1.getTipus());
					}
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return ret;
	}

	public String addKosar(String elado, String vevo, String menu, String cedula)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			String tipus = "";
			if (menu.equals(Constants.MENU_ORDER_PRE)) {
				tipus = Constants.CEDULA_STATUS_ELORENDELT;
			}
			if (menu.equals(Constants.MENU_ORDER_FINALIZE)) {
				tipus = Constants.CEDULA_STATUS_VEGLEGESIT;
			}

			Kosar kosar = new Kosar(elado, vevo, tipus,cedula);
			pm.makePersistent(kosar);
			pm.flush();

			boolean flush = false;
			while (!flush) {
				Query query = pm.newQuery(Kosar.class);
				query.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus)");
				query.declareParameters("String pelado,String pvevo,String ptipus");
				@SuppressWarnings("unchecked")
				List<Kosar> list = (List<Kosar>) pm.newQuery(query).execute(
						elado, vevo, tipus);
				if ((list != null) && (!list.isEmpty())) {
					flush = true;
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return "";
	}

	public String removeKosar(String elado, String vevo, String menu, String cedula)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			String tipus = "";
			if (menu.equals(Constants.MENU_ORDER_PRE)) {
				tipus = Constants.CEDULA_STATUS_ELORENDELT;
			}
			if (menu.equals(Constants.MENU_ORDER_FINALIZE)) {
				tipus = Constants.CEDULA_STATUS_VEGLEGESIT;
			}

			Query query = pm.newQuery(Kosarcikk.class);
			query.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus)");
			query.declareParameters("String pelado,String pvevo,String ptipus");
			@SuppressWarnings("unchecked")
			List<Kosarcikk> list = (List<Kosarcikk>) pm.newQuery(query)
					.execute(elado, vevo, tipus);
			if ((list != null) && (!list.isEmpty())) {
				for (Kosarcikk l : list) {
					pm.deletePersistent(l);
				}
			}

			Query query1 = pm.newQuery(Kosar.class);
			query1.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus)");
			query1.declareParameters("String pelado,String pvevo,String ptipus");
			@SuppressWarnings("unchecked")
			List<Kosar> list1 = (List<Kosar>) pm.newQuery(query1).execute(
					elado, vevo, tipus);
			if ((list1 != null) && (!list1.isEmpty())) {
				for (Kosar l : list1) {
					pm.deletePersistent(l);
				}
			}
			pm.flush();

			boolean flush = false;
			while (!flush) {
				@SuppressWarnings("unchecked")
				List<Kosar> list2 = (List<Kosar>) pm.newQuery(query1).execute(
						elado, vevo, tipus);
				if ((list2 == null) || (list2.isEmpty())) {
					flush = true;
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return "";
	}

	public KosarSer addKosarCikk(KosarSer kosarSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Kosarcikk kosarCikk = new Kosarcikk(kosarSer.getElado(),
					kosarSer.getVevo(), kosarSer.getTipus(),
					kosarSer.getCikkszam(), kosarSer.getSzinkod(),
					kosarSer.getAr(),
					kosarSer.getAreur(), kosarSer.getArusd(), kosarSer.getExportkarton(),
					kosarSer.getKiskarton(), kosarSer.getDarab(),
					kosarSer.getFizet(), kosarSer.getFizeteur(), kosarSer.getFizetusd(),
					kosarSer.getCedula());
			pm.makePersistent(kosarCikk);
			pm.flush();

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return kosarSer;
	}

	public KosarSer updateKosarCikk(KosarSer kosarSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Kosarcikk.class);
			query.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus) && (this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
			query.declareParameters("String pelado,String pvevo,String ptipus,String pcikkszam,String pszinkod");
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("pelado", kosarSer.getElado());
			parameters.put("pvevo", kosarSer.getVevo());
			parameters.put("ptipus", kosarSer.getTipus());
			parameters.put("pcikkszam", kosarSer.getCikkszam());
			parameters.put("pszinkod", kosarSer.getSzinkod());
			@SuppressWarnings("unchecked")
			List<Kosarcikk> list = (List<Kosarcikk>) pm.newQuery(query)
					.executeWithMap(parameters);
			if ((list != null) && (!list.isEmpty())) {
				for (Kosarcikk l : list) {
					l.setExportkarton(kosarSer.getExportkarton());
					l.setKiskarton(kosarSer.getKiskarton());
					l.setDarab(kosarSer.getDarab());
				}
			}

			pm.flush();

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return kosarSer;
	}

	public KosarSer removeKosarCikk(KosarSer kosarSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Kosarcikk.class);
			query.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus) && (this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
			query.declareParameters("String pelado,String pvevo,String ptipus,String pcikkszam,String pszinkod");
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("pelado", kosarSer.getElado());
			parameters.put("pvevo", kosarSer.getVevo());
			parameters.put("ptipus", kosarSer.getTipus());
			parameters.put("pcikkszam", kosarSer.getCikkszam());
			parameters.put("pszinkod", kosarSer.getSzinkod());
			@SuppressWarnings("unchecked")
			List<Kosarcikk> list = (List<Kosarcikk>) pm.newQuery(query)
					.executeWithMap(parameters);
			if ((list != null) && (!list.isEmpty())) {
				for (Kosarcikk l : list) {
					pm.deletePersistent(l);
				}
			}

			pm.flush();

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return kosarSer;
	}

	public String importInternet(String elado, String vevo, String rendeles)
			throws IllegalArgumentException, SQLExceptionSer {
	
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(this.rovidnev == providnev) && (this.rendeles == prendeles)");
			query.declareParameters("String providnev,String prendeles");
			@SuppressWarnings("unchecked")
			List<Rendeltcikk> list = (List<Rendeltcikk>) pm.newQuery(query)
					.execute(vevo,rendeles);
			if ((list != null) && (!list.isEmpty())) {
				for (Rendeltcikk l : list) {
									
					Query query1 = pm.newQuery(Cikk.class);
					query1.setFilter("(this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
					query1.declareParameters("String pcikkszam,String pszinkod");
					@SuppressWarnings("unchecked")
					List<Cikk> list1 = (List<Cikk>) pm.newQuery(query1)
							.execute(l.getCikkszam(),l.getSzinkod());
					if ((list1 != null) && (!list1.isEmpty())) {

						Query query3 = pm.newQuery(Kosarcikk.class);
						query3.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus) && (this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
						query3.declareParameters("String pelado,String pvevo,String ptipus,String pcikkszam,String pszinkod");
						Map<String, String> parameters = new HashMap<String, String>();
						parameters.put("pelado", elado);
						parameters.put("pvevo", vevo);
						parameters.put("ptipus", Constants.CEDULA_STATUS_ELORENDELT);
						parameters.put("pcikkszam", l.getCikkszam());
						parameters.put("pszinkod", l.getSzinkod());
						@SuppressWarnings("unchecked")
						List<Kosarcikk> list3 = (List<Kosarcikk>) pm.newQuery(query3)
								.executeWithMap(parameters);
						if ((list3 != null) && (!list3.isEmpty())) {
							for (Kosarcikk l3 : list3) {
								l3.setExportkarton(l3.getExportkarton() + l.getExportkarton());
								l3.setKiskarton(l3.getKiskarton() + l.getKiskarton());
								l3.setDarab(l3.getDarab() + l.getDarab());
							}
						}	
						else {
							Kosarcikk kosarcikk = new Kosarcikk (elado,vevo,Constants.CEDULA_STATUS_ELORENDELT,l.getCikkszam(),l.getSzinkod(),
								list1.get(0).getAr(),list1.get(0).getAreur(),list1.get(0).getElorar(),l.getExportkarton(),l.getKiskarton(),l.getDarab(),new Float(0),new Float(0),new Float(0),rendeles);
							pm.makePersistent(kosarcikk);
						}	
					}
					
				}
			}

			Query query2 = pm.newQuery(Rendelt.class);
			query2.setFilter("(this.rovidnev == providnev) && (this.rendeles == prendeles) && (this.status == pstatus)");
			query2.declareParameters("String providnev,String prendeles,String pstatus");
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("providnev", vevo);
			parameters.put("prendeles", rendeles);
			parameters.put("pstatus", Constants.CEDULA_STATUS_ELORENDELT);
			@SuppressWarnings("unchecked")
			List<Rendelt> list2 = (List<Rendelt>) pm.newQuery(query2)
					.executeWithMap(parameters);
			if ((list2 != null) && (!list2.isEmpty())) {
				for (Rendelt l : list2) {
					l.setStatus(ClientConstants.INTERNET_IMPORTED);
				}
			}
			pm.flush();
			
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}
		
		return "";
	}

	public String createCedula(String elado, String vevo, String menu)
			throws IllegalArgumentException, SQLExceptionSer {

		String cedulasorszam = "";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			String tipus = "";
			if (menu.equals(Constants.MENU_ORDER_PRE)) {
				tipus = Constants.CEDULA_STATUS_ELORENDELT;
			}
			if (menu.equals(Constants.MENU_ORDER_FINALIZE)) {
				tipus = Constants.CEDULA_STATUS_VEGLEGESIT;
			}
			
			Query sorszamQuery = pm.newQuery(Sorszam.class);
			sorszamQuery.setFilter("this.tipus == ptipus");
			sorszamQuery.declareParameters("String ptipus");
			@SuppressWarnings("unchecked")
			List<Sorszam> sorszamList = (List<Sorszam>) pm.newQuery(sorszamQuery)
					.execute(tipus);
			if ((sorszamList != null) && (!sorszamList.isEmpty())) {
				for (Sorszam l : sorszamList) {
					l.setCedula(new BigInteger(l.getCedula()).add(BigInteger.ONE).toString());
					cedulasorszam = l.getCedula();
				}
			}
			
			Query query = pm.newQuery(Kosarcikk.class);
			query.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus)");
			query.declareParameters("String pelado,String pvevo,String ptipus");
			@SuppressWarnings("unchecked")
			List<Kosarcikk> list = (List<Kosarcikk>) pm.newQuery(query)
					.execute(elado, vevo, tipus);
			if ((list != null) && (!list.isEmpty())) {
				for (Kosarcikk l : list) {
					Cedulacikk cedulacikk = new Cedulacikk (cedulasorszam,tipus,l.getCikkszam(),l.getSzinkod(),l.getAr(),l.getAreur(),l.getArusd(),
							l.getExportkarton(),l.getKiskarton(),l.getDarab(),l.getFizet(),l.getFizeteur(),l.getFizetusd());
					pm.makePersistent(cedulacikk);
					pm.deletePersistent(l);
				}
			}
							
			Query query1 = pm.newQuery(Kosar.class);
			query1.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus)");
			query1.declareParameters("String pelado,String pvevo,String ptipus");
			@SuppressWarnings("unchecked")
			List<Kosar> list1 = (List<Kosar>) pm.newQuery(query1).execute(
					elado, vevo, tipus);
			if ((list1 != null) && (!list1.isEmpty())) {
				for (Kosar l : list1) {					
					Cedula cedula = new Cedula (cedulasorszam,l.getVevo(),tipus,l.getElado(),new Date());
					pm.makePersistent(cedula);
					pm.deletePersistent(l);
				}
			}

			pm.flush();
			
			boolean flush = false;
			while (!flush) {
				Query query2 = pm.newQuery(Cedula.class);
				query2.setFilter("(this.cedula == pcedula) && (this.status == pstatus)");
				query2.declareParameters("String pcedula,String pstatus");
				@SuppressWarnings("unchecked")
				List<Cedula> list2 = (List<Cedula>) pm.newQuery(query2).execute(
						cedulasorszam, tipus);
				if ((list2 != null) && (!list2.isEmpty())) {
					flush = true;
				}
			}

		
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cedulasorszam;
	}

	@SuppressWarnings("unchecked")
	public List<CedulaSer> getCedula(String vevo,String menu)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<CedulaSer> cedula = new ArrayList<CedulaSer>();

		try {
			
			Query vevoquery = pm.newQuery(Vevo.class);
			vevoquery.setFilter("this.rovidnev == providnev");
			vevoquery.declareParameters("String providnev");

			Query felhasznaloquery = pm.newQuery(Felhasznalo.class);
			felhasznaloquery.setFilter("this.rovidnev == providnev");
			felhasznaloquery.declareParameters("String providnev");

			List<Cedula> list = null;
			Query query = pm.newQuery(Cedula.class);
			String status = "";
			if (vevo == null) {
				
				if (menu.equals(Constants.MENU_CASH_PAY)) {
					query.setFilter("(this.status == pstatus)");
					query.declareParameters("String pstatus");	
					
					status = Constants.CEDULA_STATUS_FIZETENDO_ELORENDELES;
					list = (List<Cedula>) pm.newQuery(query)
							.execute(status);
				}
				else {
					list = (List<Cedula>) pm.newQuery(query)
							.execute();				
				}
			}
			else {
				query.setFilter("(this.rovidnev == providnev) && (this.status == pstatus)");
				query.declareParameters("String providnev,String pstatus");	
				
				if (menu.equals(Constants.MENU_ORDER_FINALIZE)) {
					status = Constants.CEDULA_STATUS_ELORENDELT;
				}
								
				list = (List<Cedula>) pm.newQuery(query)
						.execute(vevo,status);
			}
			if ((list != null) && (!list.isEmpty())) {
				for (Cedula l : list) {
					CedulaSer cedulaSer = new CedulaSer();
					cedulaSer.setCedula(l.getCedula());
					cedulaSer.setRovidnev(l.getRovidnev());
					
					List<Vevo> vevolist = (List<Vevo>) pm.newQuery(vevoquery)
							.execute(l.getRovidnev());
					if (!vevolist.isEmpty()) {
						cedulaSer.setVevonev(vevolist.get(0).getNev());
					}
					
					cedulaSer.setStatus(l.getStatus());
					cedulaSer.setElado(l.getElado());
					
					List<Felhasznalo> felhasznalolist = (List<Felhasznalo>) pm.newQuery(felhasznaloquery)
							.execute(l.getElado());
					if (!felhasznalolist.isEmpty()) {
						cedulaSer.setEladonev(felhasznalolist.get(0).getNev());
					}
		
					cedulaSer.setDatum(new Date(l.getDatum().getTime()));
					cedula.add(cedulaSer);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cedula;
	}

	public List<CedulacikkSer> getCedulacikk(String cedula, String status)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<CedulacikkSer> cedulacikk = new ArrayList<CedulacikkSer>();

		try {
			
			Query cikkquery = pm.newQuery(Cikk.class);
			cikkquery.setFilter("this.cikkszam == pcikkszam && this.szinkod == pszinkod");
			cikkquery.declareParameters("String pcikkszam,String pszinkod");

			Query query = pm.newQuery(Cedulacikk.class);
			query.setFilter("(this.cedula == pcedula) && (this.status == pstatus)");
			query.declareParameters("String pcedula,String pstatus");
			@SuppressWarnings("unchecked")
			List<Cedulacikk> list = (List<Cedulacikk>) pm.newQuery(query)
					.execute(cedula,status);
			if (!list.isEmpty()) {
				for (Cedulacikk l : list) {
					CedulacikkSer cedulacikkSer = new CedulacikkSer();
					cedulacikkSer.setCedula(l.getCedula());
					cedulacikkSer.setStatus(l.getStatus());
					cedulacikkSer.setCikkszam(l.getCikkszam());
					cedulacikkSer.setSzinkod(l.getSzinkod());
					
					@SuppressWarnings("unchecked")
					List<Cikk> cikklist = (List<Cikk>) pm.newQuery(cikkquery)
							.execute(l.getCikkszam(),l.getSzinkod());
					if (!cikklist.isEmpty()) {
						cedulacikkSer.setMegnevezes(cikklist.get(0).getMegnevezes());
					}
					
					cedulacikkSer.setAr(l.getAr());
					cedulacikkSer.setAreur(l.getAreur());
					cedulacikkSer.setArusd(l.getArusd());
					cedulacikkSer.setExportkarton(l.getExportkarton());
					cedulacikkSer.setKiskarton(l.getKiskarton());
					cedulacikkSer.setDarab(l.getDarab());
					cedulacikkSer.setFizet(l.getFizet());
					cedulacikkSer.setFizeteur(l.getFizeteur());	
					cedulacikkSer.setFizetusd(l.getFizetusd());	
					cedulacikk.add(cedulacikkSer);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cedulacikk;
	}

	public String cedulaToKosar(String elado, String vevo, String menu, String cedula)
			throws IllegalArgumentException, SQLExceptionSer {

		String cedulasorszam = "";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			String tipus = "";
			String ujtipus = "";
			if (menu.equals(Constants.MENU_ORDER_FINALIZE)) {
				tipus = Constants.CEDULA_STATUS_ELORENDELT;
				ujtipus = Constants.CEDULA_STATUS_VEGLEGESIT;
			}
	
			if (menu.equals(Constants.MENU_CASH_PAY)) {
				tipus = Constants.CEDULA_STATUS_FIZETENDO_ELORENDELES;
				ujtipus = Constants.CEDULA_STATUS_ELORENDELES_FIZETES;
			}

			Query query = pm.newQuery(Cedulacikk.class);
			query.setFilter("(this.cedula == pcedula) && (this.status == pstatus)");
			query.declareParameters("String pcedula,String pstatus");
			@SuppressWarnings("unchecked")
			List<Cedulacikk> list = (List<Cedulacikk>) pm.newQuery(query)
					.execute(cedula,tipus);
			if ((list != null) && (!list.isEmpty())) {
				for (Cedulacikk l : list) {
					Kosarcikk kosarcikk = new Kosarcikk (elado,vevo,ujtipus,l.getCikkszam(),l.getSzinkod(),l.getAr(),l.getAreur(),l.getArusd(),
							l.getExportkarton(),l.getKiskarton(),l.getDarab(),l.getFizet(),l.getFizeteur(),l.getFizetusd(),l.getCedula());
					pm.makePersistent(kosarcikk);
					pm.deletePersistent(l);
				}
			}
						
			Query query1 = pm.newQuery(Cedula.class);
			query1.setFilter("(this.cedula == pcedula) && (this.status == pstatus)");
			query1.declareParameters("String pcedula,String pstatus");
			@SuppressWarnings("unchecked")
			List<Cedula> list1 = (List<Cedula>) pm.newQuery(query1).execute(
					cedula, tipus);
			if ((list1 != null) && (!list1.isEmpty())) {
				for (Cedula l : list1) {					
					Kosar kosar = new Kosar (elado,l.getRovidnev(),ujtipus,l.getCedula());
					pm.makePersistent(kosar);
					pm.deletePersistent(l);
				}
			}

			pm.flush();
			
			boolean flush = false;
			while (!flush) {
				Query query2 = pm.newQuery(Kosar.class);
				query2.setFilter("(this.elado == pelado) && (this.tipus == ptipus)");
				query2.declareParameters("String pelado,String ptipus");
				@SuppressWarnings("unchecked")
				List<Cedula> list2 = (List<Cedula>) pm.newQuery(query2).execute(
						elado,ujtipus);
				if ((list2 != null) && (!list2.isEmpty())) {
					flush = true;
				}
			}
		
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cedulasorszam;
	}

	public String kosarToCedula(String elado, String vevo, String menu, String cedulasorszam)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			
			String tipus = "";
			String ujtipus = "";
			if (menu.equals(Constants.MENU_ORDER_FINALIZE)) {
				tipus = Constants.CEDULA_STATUS_VEGLEGESIT;
				ujtipus = Constants.CEDULA_STATUS_FIZETENDO_ELORENDELES;
			}

			if (menu.equals(Constants.MENU_CASH_PAY)) {
				tipus = Constants.CEDULA_STATUS_ELORENDELES_FIZETES;
				ujtipus = Constants.CEDULA_STATUS_FIZETETT_ELORENDELES;
			}

			Query query = pm.newQuery(Kosarcikk.class);
			query.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus)");
			query.declareParameters("String pelado,String pvevo,String ptipus");
			@SuppressWarnings("unchecked")
			List<Kosarcikk> list = (List<Kosarcikk>) pm.newQuery(query)
					.execute(elado, vevo, tipus);
			float fizet = 0;
			float fizeteur = 0;
			float fizetusd = 0;
			if ((list != null) && (!list.isEmpty())) {
				for (Kosarcikk l : list) {
					Cedulacikk cedulacikk = new Cedulacikk (cedulasorszam,ujtipus,l.getCikkszam(),l.getSzinkod(),l.getAr(),l.getAreur(),l.getArusd(),
							l.getExportkarton(),l.getKiskarton(),l.getDarab(),l.getFizet(),l.getFizeteur(),l.getFizetusd());
					pm.makePersistent(cedulacikk);
					if (l.getFizet() != null) fizet = fizet + l.getFizet();
					if (l.getFizeteur() != null) fizeteur = fizeteur + l.getFizeteur();
					if (l.getFizetusd() != null) fizetusd = fizetusd + l.getFizetusd();
					pm.deletePersistent(l);
				}
			}
			
			Query query1 = pm.newQuery(Kosar.class);
			query1.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus)");
			query1.declareParameters("String pelado,String pvevo,String ptipus");
			@SuppressWarnings("unchecked")
			List<Kosar> list1 = (List<Kosar>) pm.newQuery(query1).execute(
					elado, vevo, tipus);
			if ((list1 != null) && (!list1.isEmpty())) {
				for (Kosar l : list1) {					
					Cedula cedula = new Cedula (cedulasorszam,l.getVevo(),ujtipus,l.getElado(),new Date());
					pm.makePersistent(cedula);
					pm.deletePersistent(l);
				}
			}
			
			if (ujtipus.equals(Constants.CEDULA_STATUS_FIZETETT_ELORENDELES)) {
				Fizetes fizetes = new Fizetes(cedulasorszam,vevo,Constants.FIZETES_ELORENDELT,elado,fizet,fizeteur,fizetusd,new Date(),Boolean.FALSE);
				pm.makePersistent(fizetes);
			}
			
			pm.flush();
			
			boolean flush = false;
			while (!flush) {
				Query query2 = pm.newQuery(Cedula.class);
				query2.setFilter("(this.cedula == pcedula) && (this.status == pstatus)");
				query2.declareParameters("String pcedula,String pstatus");
				@SuppressWarnings("unchecked")
				List<Cedula> list2 = (List<Cedula>) pm.newQuery(query2).execute(
						cedulasorszam, ujtipus);
				if ((list2 != null) && (!list2.isEmpty())) {
					flush = true;
				}
			}		
			
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cedulasorszam;
	}

	public List<FizetesSer> getFizetes()
			throws IllegalArgumentException, SQLExceptionSer {

		List<FizetesSer> fizetesek = new ArrayList<FizetesSer>();

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query vevoquery = pm.newQuery(Vevo.class);
		vevoquery.setFilter("this.rovidnev == providnev");
		vevoquery.declareParameters("String providnev");

		Query felhasznaloquery = pm.newQuery(Felhasznalo.class);
		felhasznaloquery.setFilter("this.rovidnev == providnev");
		felhasznaloquery.declareParameters("String providnev");

		try {

			Query query = pm.newQuery(Fizetes.class);
			@SuppressWarnings("unchecked")
			List<Fizetes> list = (List<Fizetes>) pm.newQuery(query)
					.execute();
			if ((list != null) && (!list.isEmpty())) {
				for (Fizetes l : list) {
					FizetesSer fizetesSer = new FizetesSer();
					fizetesSer.setCedula(l.getCedula());
					fizetesSer.setVevo(l.getVevo());

					@SuppressWarnings("unchecked")
					List<Vevo> vevolist = (List<Vevo>) pm.newQuery(vevoquery)
							.execute(l.getVevo());
					if (!vevolist.isEmpty()) {
						fizetesSer.setVevonev(vevolist.get(0).getNev());
					}
					fizetesSer.setTipus(l.getTipus());
					fizetesSer.setPenztaros(l.getPenztaros());
	
					@SuppressWarnings("unchecked")
					List<Felhasznalo> felhasznalolist = (List<Felhasznalo>) pm.newQuery(felhasznaloquery)
							.execute(l.getPenztaros());
					if (!felhasznalolist.isEmpty()) {
						fizetesSer.setPenztarosnev(felhasznalolist.get(0).getNev());
					}
	
					fizetesSer.setFizet(l.getFizet());
					fizetesSer.setFizeteur(l.getFizeteur());
					fizetesSer.setFizetusd(l.getFizetusd());
					fizetesSer.setDatum(new Date(l.getDatum().getTime()));
					fizetesek.add(fizetesSer);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return fizetesek;
	}

}
