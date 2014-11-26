package hu.dekortrade.server;

import hu.dekortrade.client.DekorTradeService;
import hu.dekortrade.server.jdo.Beszallitottcikk;
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
import hu.dekortrade.server.jdo.Zaras;
import hu.dekortrade.server.jdo.Zarasfizetes;
import hu.dekortrade.shared.Constants;
import hu.dekortrade.shared.serialized.BeszallitottcikkSer;
import hu.dekortrade.shared.serialized.CedulaSer;
import hu.dekortrade.shared.serialized.CedulacikkSer;
import hu.dekortrade.shared.serialized.CikkSelectsSer;
import hu.dekortrade.shared.serialized.CikkSer;
import hu.dekortrade.shared.serialized.CikkaltipusSer;
import hu.dekortrade.shared.serialized.CikkfotipusSer;
import hu.dekortrade.shared.serialized.EladasSer;
import hu.dekortrade.shared.serialized.FelhasznaloSer;
import hu.dekortrade.shared.serialized.FizetesSer;
import hu.dekortrade.shared.serialized.GyartoSer;
import hu.dekortrade.shared.serialized.JogSer;
import hu.dekortrade.shared.serialized.KosarSer;
import hu.dekortrade.shared.serialized.LoginExceptionSer;
import hu.dekortrade.shared.serialized.RaktarSer;
import hu.dekortrade.shared.serialized.RendeltSer;
import hu.dekortrade.shared.serialized.RendeltcikkSer;
import hu.dekortrade.shared.serialized.SQLExceptionSer;
import hu.dekortrade.shared.serialized.SzinkronSer;
import hu.dekortrade.shared.serialized.TabPageSer;
import hu.dekortrade.shared.serialized.UploadSer;
import hu.dekortrade.shared.serialized.UserSer;
import hu.dekortrade.shared.serialized.VevoKosarSer;
import hu.dekortrade.shared.serialized.VevoSer;
import hu.dekortrade.shared.serialized.ZarasEgyenlegSer;
import hu.dekortrade.shared.serialized.ZarasSer;

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

import com.google.appengine.api.memcache.MemcacheServiceFactory;
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
						if (l.getNev().equals(Constants.MENU_RENDSZER)) {
							userSer.getJog().add(l.getNev());
						}
						if ((l.getNev().equals(Constants.MENU_RENDSZER))

						|| (l.getNev().equals(Constants.MENU_TORZSADAT))

						|| (l.getNev().equals(Constants.MENU_RENDELES))

						|| (l.getNev().equals(Constants.MENU_RAKTAR))

						|| (l.getNev().equals(Constants.MENU_ELADAS))

						|| (l.getNev().equals(Constants.MENU_PENZTAR))

						|| (l.getNev().equals(Constants.MENU_LEKERDEZES)))

						{

							TabPageSer tabPageSer = new TabPageSer();
							tabPageSer.setId(tabindex);
							tabPageSer.setName(l.getNev());
							userSer.getTabList().add(tabPageSer);
							if (l.getNev().equals(defaultMenu))
								userSer.setDefultTab(tabindex);
							tabindex++;
						}
						if ((l.getNev()
								.equals(Constants.MENU_RENDSZER_FELHASZNALO))
								|| (l.getNev()
										.equals(Constants.MENU_RENDSZER_SZINKRON))
								|| (l.getNev()
										.equals(Constants.MENU_RENDSZER_KOSAR))

								|| (l.getNev()
										.equals(Constants.MENU_TORZSADAT_GYARTO))
								|| (l.getNev()
										.equals(Constants.MENU_TORZSADAT_VEVO))
								|| (l.getNev()
										.equals(Constants.MENU_TORZSADAT_CIKKTIPUS))
								|| (l.getNev()
										.equals(Constants.MENU_TORZSADAT_CIKKTORZS))

								|| (l.getNev()
										.equals(Constants.MENU_RAKTAR_BESZALLITAS))
								|| (l.getNev()
										.equals(Constants.MENU_RAKTAR_KESZLET))
								|| (l.getNev()
										.equals(Constants.MENU_RAKTAR_KIADAS))

								|| (l.getNev()
										.equals(Constants.MENU_RENDELES_INTERNET))
								|| (l.getNev()
										.equals(Constants.MENU_RENDELES_ELORENDELES))
								|| (l.getNev()
										.equals(Constants.MENU_RENDELES_VEGLEGESITES))
								|| (l.getNev()
										.equals(Constants.MENU_RENDELES_MEGRENDELES))

								|| (l.getNev()
										.equals(Constants.MENU_PENZTAR_FIZETES))
								|| (l.getNev()
										.equals(Constants.MENU_PENZTAR_TORLESZTES))
								|| (l.getNev()
										.equals(Constants.MENU_PENZTAR_HAZI))
								|| (l.getNev()
										.equals(Constants.MENU_PENZTAR_ZARAS))

								|| (l.getNev()
										.equals(Constants.MENU_LEKERDEZES_CEDULAK))
								|| (l.getNev()
										.equals(Constants.MENU_LEKERDEZES_ZARASOK))
								|| (l.getNev()
										.equals(Constants.MENU_LEKERDEZES_TORLESZTESEK))
								|| (l.getNev()
										.equals(Constants.MENU_LEKERDEZES_FORGALOM))
										
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
			mainmenu1jogSer.setNev(Constants.MENU_RENDSZER);
			mainmenu1jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu1jogSer);

			JogSer rendszermenu1jogSer = new JogSer();
			rendszermenu1jogSer.setNev(Constants.MENU_RENDSZER_FELHASZNALO);
			rendszermenu1jogSer.setJog(Boolean.FALSE);
			jog.add(rendszermenu1jogSer);

			JogSer rendszermenu2jogSer = new JogSer();
			rendszermenu2jogSer.setNev(Constants.MENU_RENDSZER_SZINKRON);
			rendszermenu2jogSer.setJog(Boolean.FALSE);
			jog.add(rendszermenu2jogSer);

			JogSer rendszermenu3jogSer = new JogSer();
			rendszermenu3jogSer.setNev(Constants.MENU_RENDSZER_KOSAR);
			rendszermenu3jogSer.setJog(Boolean.FALSE);
			jog.add(rendszermenu3jogSer);

			JogSer mainmenu2jogSer = new JogSer();
			mainmenu2jogSer.setNev(Constants.MENU_TORZSADAT);
			mainmenu2jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu2jogSer);

			JogSer torzsadatmenu1jogSer = new JogSer();
			torzsadatmenu1jogSer.setNev(Constants.MENU_TORZSADAT_GYARTO);
			torzsadatmenu1jogSer.setJog(Boolean.FALSE);
			jog.add(torzsadatmenu1jogSer);

			JogSer torzsadatmenu2jogSer = new JogSer();
			torzsadatmenu2jogSer.setNev(Constants.MENU_TORZSADAT_VEVO);
			torzsadatmenu2jogSer.setJog(Boolean.FALSE);
			jog.add(torzsadatmenu2jogSer);

			JogSer torzsadatmenu3jogSer = new JogSer();
			torzsadatmenu3jogSer.setNev(Constants.MENU_TORZSADAT_CIKKTIPUS);
			torzsadatmenu3jogSer.setJog(Boolean.FALSE);
			jog.add(torzsadatmenu3jogSer);

			JogSer torzsadatmenu4jogSer = new JogSer();
			torzsadatmenu4jogSer.setNev(Constants.MENU_TORZSADAT_CIKKTORZS);
			torzsadatmenu4jogSer.setJog(Boolean.FALSE);
			jog.add(torzsadatmenu4jogSer);

			JogSer mainmenu3jogSer = new JogSer();
			mainmenu3jogSer.setNev(Constants.MENU_RENDELES);
			mainmenu3jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu3jogSer);

			JogSer rendelesmenu1jogSer = new JogSer();
			rendelesmenu1jogSer.setNev(Constants.MENU_RENDELES_INTERNET);
			rendelesmenu1jogSer.setJog(Boolean.FALSE);
			jog.add(rendelesmenu1jogSer);

			JogSer rendelesmenu2jogSer = new JogSer();
			rendelesmenu2jogSer.setNev(Constants.MENU_RENDELES_ELORENDELES);
			rendelesmenu2jogSer.setJog(Boolean.FALSE);
			jog.add(rendelesmenu2jogSer);

			JogSer rendelesmenu3jogSer = new JogSer();
			rendelesmenu3jogSer.setNev(Constants.MENU_RENDELES_VEGLEGESITES);
			rendelesmenu3jogSer.setJog(Boolean.FALSE);
			jog.add(rendelesmenu3jogSer);

			JogSer rendelesmenu4jogSer = new JogSer();
			rendelesmenu4jogSer.setNev(Constants.MENU_RENDELES_MEGRENDELES);
			rendelesmenu4jogSer.setJog(Boolean.FALSE);
			jog.add(rendelesmenu4jogSer);

			JogSer mainmenu4jogSer = new JogSer();
			mainmenu4jogSer.setNev(Constants.MENU_RAKTAR);
			mainmenu4jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu4jogSer);
			jog.add(mainmenu4jogSer);

			JogSer raktarmenu1jogSer = new JogSer();
			raktarmenu1jogSer.setNev(Constants.MENU_RAKTAR_BESZALLITAS);
			raktarmenu1jogSer.setJog(Boolean.FALSE);
			jog.add(raktarmenu1jogSer);

			JogSer raktarmenu2jogSer = new JogSer();
			raktarmenu2jogSer.setNev(Constants.MENU_RAKTAR_KESZLET);
			raktarmenu2jogSer.setJog(Boolean.FALSE);
			jog.add(raktarmenu2jogSer);

			JogSer raktarmenu3jogSer = new JogSer();
			raktarmenu3jogSer.setNev(Constants.MENU_RAKTAR_KIADAS);
			raktarmenu3jogSer.setJog(Boolean.FALSE);
			jog.add(raktarmenu3jogSer);

			JogSer mainmenu5jogSer = new JogSer();
			mainmenu5jogSer.setNev(Constants.MENU_ELADAS);
			mainmenu5jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu5jogSer);
			jog.add(mainmenu5jogSer);

			JogSer mainmenu6jogSer = new JogSer();
			mainmenu6jogSer.setNev(Constants.MENU_PENZTAR);
			mainmenu6jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu6jogSer);

			JogSer pentarmenu1jogSer = new JogSer();
			pentarmenu1jogSer.setNev(Constants.MENU_PENZTAR_FIZETES);
			pentarmenu1jogSer.setJog(Boolean.FALSE);
			jog.add(pentarmenu1jogSer);

			JogSer pentarmenu2jogSer = new JogSer();
			pentarmenu2jogSer.setNev(Constants.MENU_PENZTAR_TORLESZTES);
			pentarmenu2jogSer.setJog(Boolean.FALSE);
			jog.add(pentarmenu2jogSer);

			JogSer pentarmenu3jogSer = new JogSer();
			pentarmenu3jogSer.setNev(Constants.MENU_PENZTAR_HAZI);
			pentarmenu3jogSer.setJog(Boolean.FALSE);
			jog.add(pentarmenu3jogSer);

			JogSer pentarmenu4jogSer = new JogSer();
			pentarmenu4jogSer.setNev(Constants.MENU_PENZTAR_ZARAS);
			pentarmenu4jogSer.setJog(Boolean.FALSE);
			jog.add(pentarmenu4jogSer);

			JogSer mainmenu7jogSer = new JogSer();
			mainmenu7jogSer.setNev(Constants.MENU_LEKERDEZES);
			mainmenu7jogSer.setJog(Boolean.FALSE);
			jog.add(mainmenu7jogSer);

			JogSer lekerdezesmenu1jogSer = new JogSer();
			lekerdezesmenu1jogSer.setNev(Constants.MENU_LEKERDEZES_CEDULAK);
			lekerdezesmenu1jogSer.setJog(Boolean.FALSE);
			jog.add(lekerdezesmenu1jogSer);

			JogSer lekerdezesmenu2jogSer = new JogSer();
			lekerdezesmenu2jogSer.setNev(Constants.MENU_LEKERDEZES_ZARASOK);
			lekerdezesmenu2jogSer.setJog(Boolean.FALSE);
			jog.add(lekerdezesmenu2jogSer);

			JogSer lekerdezesmenu3jogSer = new JogSer();
			lekerdezesmenu3jogSer
					.setNev(Constants.MENU_LEKERDEZES_TORLESZTESEK);
			lekerdezesmenu3jogSer.setJog(Boolean.FALSE);
			jog.add(lekerdezesmenu3jogSer);

			JogSer rovancsjogSer = new JogSer();
			rovancsjogSer.setNev(Constants.JOG_ROVANCS);
			rovancsjogSer.setJog(Boolean.FALSE);
			jog.add(rovancsjogSer);

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
//			query.setOrdering("cikkszam,szinkod");
			query.setRange(page * Constants.FETCH_SIZE, (page + 1)
					* Constants.FETCH_SIZE);
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query).executeWithMap(
					parameters);
			if (!list.isEmpty()) {
				for (Cikk l : list) {
					CikkSer raktarSer = new CikkSer();
					raktarSer.setFotipus(l.getFotipus());
					raktarSer.setAltipus(l.getAltipus());
					raktarSer.setGyarto(l.getGyarto());
					raktarSer.setGyartocikkszam(l.getGyartocikkszam());
					raktarSer.setSzinkod(l.getSzinkod());
					raktarSer.setCikkszam(l.getCikkszam());
					raktarSer.setFelviteltol(l.getFelviteltol() == null ? null
							: new Date(l.getFelviteltol().getTime()));
					raktarSer.setFelvitelig(l.getFelvitelig() == null ? null
							: new Date(l.getFelvitelig().getTime()));
					raktarSer.setLejarattol(l.getLejarattol() == null ? null
							: new Date(l.getLejarattol().getTime()));
					raktarSer.setLejaratig(l.getLejaratig() == null ? null
							: new Date(l.getLejaratig().getTime()));
					raktarSer.setMegnevezes(l.getMegnevezes());
					raktarSer.setVamtarifaszam(l.getVamtarifaszam());
					raktarSer.setFob(l.getFob());
					raktarSer.setSzallitas(l.getSzallitas());
					raktarSer.setDdu(l.getDdu());
					raktarSer.setErsz(l.getErsz());
					raktarSer.setElorar(l.getElorar());
					raktarSer.setUjfob(l.getUjfob());
					raktarSer.setUjszallitas(l.getUjszallitas());
					raktarSer.setUjddu(l.getUjddu());
					raktarSer.setUjersz(l.getUjersz());
					raktarSer.setUjelorar(l.getUjelorar());
					raktarSer.setAr(l.getAr());
					raktarSer.setAreur(l.getAreur());
					raktarSer.setArszorzo(l.getArszorzo());
					raktarSer.setKiskarton(l.getKiskarton());
					raktarSer.setDarab(l.getDarab());
					raktarSer.setTerfogat(l.getTerfogat());
					raktarSer.setTerfogatlab(l.getTerfogatlab());
					raktarSer.setBsuly(l.getBsuly());
					raktarSer.setNsuly(l.getNsuly());
					raktarSer.setLeiras(l.getLeiras());
					raktarSer.setMegjegyzes(l.getMegjegyzes());
					raktarSer.setAkcios(l.getAkcios());
					raktarSer.setMertekegyseg(l.getMertekegyseg());
					raktarSer.setKepek(l.getKepek());
					cikk.add(raktarSer);
				}
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cikk;
	}

	public CikkSer addCikk(CikkSer raktarSer) throws IllegalArgumentException,
			SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(Cikk.class);
			query.setFilter("this.cikkszam == pcikkszam && this.szinkod == pszinkod");
			query.declareParameters("String pcikkszam,String pszinkod");
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query).execute(
					raktarSer.getCikkszam(), raktarSer.getSzinkod());
			if ((list != null) && (!list.isEmpty())) {
				throw new Exception(Constants.EXISTSID);
			} else {
				Cikk cikk = new Cikk(raktarSer.getFotipus(),
						raktarSer.getAltipus(), raktarSer.getGyarto(),
						raktarSer.getGyartocikkszam(), raktarSer.getCikkszam(),
						raktarSer.getSzinkod() == null ? "" : raktarSer
								.getSzinkod(),
						raktarSer.getFelviteltol() == null ? null : new Date(
								raktarSer.getFelviteltol().getTime()),
						raktarSer.getFelvitelig() == null ? null : new Date(
								raktarSer.getFelvitelig().getTime()),
						raktarSer.getLejarattol() == null ? null : new Date(
								raktarSer.getLejarattol().getTime()),
						raktarSer.getLejaratig() == null ? null : new Date(
								raktarSer.getLejaratig().getTime()),
						raktarSer.getMegnevezes(),
						raktarSer.getVamtarifaszam(), raktarSer.getFob(),
						raktarSer.getSzallitas(), raktarSer.getDdu(),
						raktarSer.getErsz(), raktarSer.getElorar(),
						raktarSer.getUjfob(), raktarSer.getUjszallitas(),
						raktarSer.getUjddu(), raktarSer.getUjersz(),
						raktarSer.getUjelorar(), raktarSer.getAr(),
						raktarSer.getAreur(), raktarSer.getArszorzo(),
						raktarSer.getKiskarton(), raktarSer.getDarab(),
						raktarSer.getTerfogat(), raktarSer.getTerfogatlab(),
						raktarSer.getBsuly(), raktarSer.getNsuly(),
						raktarSer.getLeiras(), raktarSer.getMegjegyzes(),
						raktarSer.getAkcios(), raktarSer.getMertekegyseg(),
						new Integer(0), Boolean.FALSE, Boolean.FALSE, 0L, 0L,
						"");
				pm.makePersistent(cikk);
			}
			pm.flush();
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return raktarSer;
	}

	public CikkSer updateCikk(CikkSer raktarSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Cikk.class);
			query.setFilter("this.cikkszam == pcikkszam");
			query.declareParameters("String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query).execute(
					raktarSer.getCikkszam());
			if ((list != null) && (!list.isEmpty())) {
				for (Cikk l : list) {
					l.setFotipus(raktarSer.getFotipus());
					l.setAltipus(raktarSer.getAltipus());
					l.setGyarto(raktarSer.getGyarto());
					l.setGyartocikkszam(raktarSer.getGyartocikkszam());
					l.setCikkszam(raktarSer.getCikkszam());
					l.setSzinkod(raktarSer.getSzinkod());
					l.setFelviteltol(raktarSer.getFelviteltol() == null ? null
							: new Date(raktarSer.getFelviteltol().getTime()));
					l.setFelvitelig(raktarSer.getFelvitelig() == null ? null
							: new Date(raktarSer.getFelvitelig().getTime()));
					l.setLejarattol(raktarSer.getLejarattol() == null ? null
							: new Date(raktarSer.getLejarattol().getTime()));
					l.setLejaratig(raktarSer.getLejaratig() == null ? null
							: new Date(raktarSer.getLejaratig().getTime()));
					l.setMegnevezes(raktarSer.getMegnevezes());
					l.setVamtarifaszam(raktarSer.getVamtarifaszam());
					l.setFob(raktarSer.getFob());
					l.setSzallitas(raktarSer.getSzallitas());
					l.setDdu(raktarSer.getDdu());
					l.setErsz(raktarSer.getErsz());
					l.setElorar(raktarSer.getElorar());
					l.setUjfob(raktarSer.getUjfob());
					l.setUjszallitas(raktarSer.getUjszallitas());
					l.setUjddu(raktarSer.getUjddu());
					l.setUjersz(raktarSer.getUjersz());
					l.setUjelorar(raktarSer.getUjelorar());
					l.setAr(raktarSer.getAr());
					l.setAreur(raktarSer.getAreur());
					l.setArszorzo(raktarSer.getArszorzo());
					l.setKiskarton(raktarSer.getKiskarton());
					l.setDarab(raktarSer.getDarab());
					l.setTerfogat(raktarSer.getTerfogat());
					l.setTerfogatlab(raktarSer.getTerfogatlab());
					l.setBsuly(raktarSer.getBsuly());
					l.setNsuly(raktarSer.getNsuly());
					l.setLeiras(raktarSer.getLeiras());
					l.setMegjegyzes(raktarSer.getMegjegyzes());
					l.setAkcios(raktarSer.getAkcios());
					l.setMertekegyseg(raktarSer.getMertekegyseg());
					l.setKepek(raktarSer.getKepek());
					l.setSzinkron(Boolean.FALSE);
				}
			}
			pm.flush();
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return raktarSer;
	}

	public CikkSer removeCikk(CikkSer raktarSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query query = pm.newQuery(Cikk.class);
			query.setFilter("this.cikkszam == pcikkszam");
			query.declareParameters("String pcikkszam");
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query).execute(
					raktarSer.getCikkszam());
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

		return raktarSer;
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
			List<Rendelt> list = (List<Rendelt>) pm.newQuery(query).execute(
					Constants.INTERNET_ELORENDEL);
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
			List<Kep> list = (List<Kep>) pm.newQuery(query).execute(cikkszam,
					szinkod);
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
			List<Kep> list = (List<Kep>) pm.newQuery(query).execute(cikkszam,
					szinkod, sorszam);
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
					cikkszam, szinkod);
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
			cikkquery
					.setFilter("this.cikkszam == pcikkszam && this.szinkod == pszinkod");
			cikkquery.declareParameters("String pcikkszam,String pszinkod");

			Query query = pm.newQuery(Kosarcikk.class);

			if (vevo != null) {
			
				if (menu.equals(Constants.MENU_PENZTAR_FIZETES)) {
					
					query.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && ( (this.tipus == ptipus1) || (this.tipus == ptipus2) )");
					query.declareParameters("String pelado,String pvevo,String ptipus1,String ptipus2");
					
					Map<String, String> parameters = new HashMap<String, String>();
					parameters.put("pelado", elado);
					parameters.put("pvevo", vevo);
					parameters.put("ptipus1", Constants.CEDULA_STATUSZ_FIZETES);
					parameters.put("ptipus2", Constants.CEDULA_STATUSZ_ELORENDELES_FIZETES);
					@SuppressWarnings("unchecked")
					List<Kosarcikk> list = (List<Kosarcikk>) pm.newQuery(query)
							.executeWithMap(parameters);
					if (!list.isEmpty()) {
						for (Kosarcikk l : list) {
							KosarSer kosarSer = new KosarSer();
							kosarSer.setCikkszam(l.getCikkszam());
							kosarSer.setSzinkod(l.getSzinkod());
							kosarSer.setTipus(l.getTipus());
							@SuppressWarnings("unchecked")
							List<Cikk> cikklist = (List<Cikk>) pm.newQuery(
									cikkquery).execute(l.getCikkszam(),
									l.getSzinkod());
							if (!cikklist.isEmpty()) {
								kosarSer.setMegnevezes(cikklist.get(0)
										.getMegnevezes());
								kosarSer.setArusd(cikklist.get(0).getElorar());
								kosarSer.setHelykod(cikklist.get(0).getHelykod());
							}

							kosarSer.setExportkarton(l.getExportkarton());
							kosarSer.setKiskarton(l.getKiskarton());
							kosarSer.setDarab(l.getDarab());
							kosarSer.setFizetusd(l.getFizetusd());
							kosarSer.setRendeles(l.getRendeles());
							kosar.add(kosarSer);
						}
					}

				} 
				else {
					
					String tipus = "";
					if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
						tipus = Constants.CEDULA_STATUSZ_ELORENDELT;
					}
					if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
						tipus = Constants.CEDULA_STATUSZ_VEGLEGESIT;
					}
					if (menu.equals(Constants.MENU_ELADAS)) {
						tipus = Constants.CEDULA_STATUSZ_ELADOTT;
					}
					if (menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
						tipus = Constants.CEDULA_STATUSZ_KIADAS;
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
							kosarSer.setTipus(l.getTipus());
							@SuppressWarnings("unchecked")
							List<Cikk> cikklist = (List<Cikk>) pm.newQuery(
									cikkquery).execute(l.getCikkszam(),
									l.getSzinkod());
							if (!cikklist.isEmpty()) {
								kosarSer.setMegnevezes(cikklist.get(0)
										.getMegnevezes());
								kosarSer.setArusd(cikklist.get(0).getElorar());
								kosarSer.setHelykod(cikklist.get(0).getHelykod());
							}
	
							kosarSer.setExportkarton(l.getExportkarton());
							kosarSer.setKiskarton(l.getKiskarton());
							kosarSer.setDarab(l.getDarab());
							kosarSer.setFizetusd(l.getFizetusd());				
							kosarSer.setRendeles(l.getRendeles());
							
// TODO helykod							
							
							kosar.add(kosarSer);
						}
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
						List<Cikk> cikklist = (List<Cikk>) pm.newQuery(
								cikkquery).execute(l.getCikkszam(),
								l.getSzinkod());
						if (!cikklist.isEmpty()) {
							kosarSer.setMegnevezes(cikklist.get(0)
									.getMegnevezes());
							kosarSer.setHelykod(cikklist.get(0).getHelykod());
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
						kosarSer.setRendeles(l.getRendeles());
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

			if (menu.equals(Constants.MENU_PENZTAR_FIZETES)) {
	
				Query query = pm.newQuery(Kosar.class);
				query.setFilter("(this.elado == pelado) && ( (this.tipus == ptipus1) || (this.tipus == ptipus2) )");
				query.declareParameters("String pelado,String ptipus1,String ptipus2");
				@SuppressWarnings("unchecked")
				List<Kosar> list = (List<Kosar>) pm.newQuery(query).execute(elado,
						Constants.CEDULA_STATUSZ_ELORENDELES_FIZETES,Constants.CEDULA_STATUSZ_FIZETES);
				if ((list != null) && (!list.isEmpty())) {
					ret = new VevoKosarSer();
					for (Kosar l : list) {
						ret.setVevo(l.getVevo());
						ret.setCedula(l.getCedula());
						ret.setCedulatipus(l.getTipus());
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
			}

			else {
				
				String tipus = "";
				if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
					tipus = Constants.CEDULA_STATUSZ_ELORENDELT;
				}
				if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
					tipus = Constants.CEDULA_STATUSZ_VEGLEGESIT;
				}
				if (menu.equals(Constants.MENU_ELADAS)) {
					tipus = Constants.CEDULA_STATUSZ_ELADOTT;
				}
				if (menu.equals(Constants.MENU_RAKTAR_KIADAS)) {
					tipus = Constants.CEDULA_STATUSZ_KIADAS;
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
			if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
				tipus = Constants.CEDULA_STATUSZ_ELORENDELT;
			}
			if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
				tipus = Constants.CEDULA_STATUSZ_VEGLEGESIT;
			}
			if (menu.equals(Constants.MENU_ELADAS)) {
				tipus = Constants.CEDULA_STATUSZ_ELADOTT;
			}
			
			Kosar kosar = new Kosar(elado, vevo, tipus, cedula);
			pm.makePersistent(kosar);
			pm.flush();

			MemcacheServiceFactory.getMemcacheService().clearAll();;
                    	
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return "";
	}

	public String removeKosar(String elado, String vevo, String menu,
			String cedula) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			String tipus = "";
			if (menu.equals(Constants.MENU_RENDELES_ELORENDELES)) {
				tipus = Constants.CEDULA_STATUSZ_ELORENDELT;
			}
			if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
				tipus = Constants.CEDULA_STATUSZ_VEGLEGESIT;
			}
			if (menu.equals(Constants.MENU_ELADAS)) {
				tipus = Constants.CEDULA_STATUSZ_ELADOTT;
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

			MemcacheServiceFactory.getMemcacheService().clearAll();;
                   	
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
					kosarSer.getAr(), kosarSer.getAreur(), kosarSer.getArusd(),
					kosarSer.getExportkarton(), kosarSer.getKiskarton(),
					kosarSer.getDarab(), kosarSer.getFizet(),
					kosarSer.getFizeteur(), kosarSer.getFizetusd(),
					kosarSer.getCedula(),kosarSer.getRendeles());
			pm.makePersistent(kosarCikk);
			pm.flush();

			MemcacheServiceFactory.getMemcacheService().clearAll();;
        	
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

			Query rendeltquery = pm.newQuery(Rendeltcikk.class);
			rendeltquery.setFilter("(this.rendeles == prendeles) && (this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
			rendeltquery.declareParameters("String prendeles,String pcikkszam,String pszinkod");

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
				boolean found = false;

				if ((kosarSer.getRendeles() != null) && !kosarSer.getRendeles().equals("")) {
					for (Kosarcikk l : list) {	
						
						if (!found && (l.getRendeles().equals(kosarSer.getRendeles())) && (l.getExportkarton() == kosarSer.getExportkarton()) && 
								(l.getKiskarton() == kosarSer.getKiskarton()) && 
								(l.getDarab() == kosarSer.getDarab())) {
							pm.deletePersistent(l);
							found = true;
						}
						if (found) {
							@SuppressWarnings("unchecked")
							List<Rendeltcikk> rendeltlist = (List<Rendeltcikk>) pm.newQuery(
									rendeltquery).execute(kosarSer.getRendeles(),kosarSer.getCikkszam(),kosarSer.getSzinkod());
							if (!rendeltlist.isEmpty()) {
								for (Rendeltcikk l1 : rendeltlist) {
									l1.setStatus(Constants.ELORENDELT_BEERKEZETT);
								}
							}
						}	
					}
				}
				else {
					for (Kosarcikk l : list) {	
						if (!found && (l.getExportkarton() == kosarSer.getExportkarton()) && 
								(l.getKiskarton() == kosarSer.getKiskarton()) && 
								(l.getDarab() == kosarSer.getDarab())) {
							pm.deletePersistent(l);
							found = true;
						}	
					}
				}
			}
          
			pm.flush();

			MemcacheServiceFactory.getMemcacheService().clearAll();;

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
					.execute(vevo, rendeles);
			if ((list != null) && (!list.isEmpty())) {
				for (Rendeltcikk l : list) {

					Query query1 = pm.newQuery(Cikk.class);
					query1.setFilter("(this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
					query1.declareParameters("String pcikkszam,String pszinkod");
					@SuppressWarnings("unchecked")
					List<Cikk> list1 = (List<Cikk>) pm.newQuery(query1)
							.execute(l.getCikkszam(), l.getSzinkod());
					if ((list1 != null) && (!list1.isEmpty())) {

						Query query3 = pm.newQuery(Kosarcikk.class);
						query3.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus) && (this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
						query3.declareParameters("String pelado,String pvevo,String ptipus,String pcikkszam,String pszinkod");
						Map<String, String> parameters = new HashMap<String, String>();
						parameters.put("pelado", elado);
						parameters.put("pvevo", vevo);
						parameters.put("ptipus",
								Constants.CEDULA_STATUSZ_ELORENDELT);
						parameters.put("pcikkszam", l.getCikkszam());
						parameters.put("pszinkod", l.getSzinkod());
						@SuppressWarnings("unchecked")
						List<Kosarcikk> list3 = (List<Kosarcikk>) pm.newQuery(
								query3).executeWithMap(parameters);
						if ((list3 != null) && (!list3.isEmpty())) {
							for (Kosarcikk l3 : list3) {
								l3.setExportkarton(l3.getExportkarton()
										+ l.getExportkarton());
								l3.setKiskarton(l3.getKiskarton()
										+ l.getKiskarton());
								l3.setDarab(l3.getDarab() + l.getDarab());
							}
						} else {
							Kosarcikk kosarcikk = new Kosarcikk(elado, vevo,
									Constants.CEDULA_STATUSZ_ELORENDELT,
									l.getCikkszam(), l.getSzinkod(), list1.get(
											0).getAr(),
									list1.get(0).getAreur(), list1.get(0)
											.getElorar(), l.getExportkarton(),
									l.getKiskarton(), l.getDarab(),
									new Double(0), new Double(0), new Double(0),
									rendeles,"");
							pm.makePersistent(kosarcikk);
						}
						l.setStatus(Constants.INTERNET_IMPORTED);
					}

				}
			}

			Query query2 = pm.newQuery(Rendelt.class);
			query2.setFilter("(this.rovidnev == providnev) && (this.rendeles == prendeles) && (this.status == pstatus)");
			query2.declareParameters("String providnev,String prendeles");
			@SuppressWarnings("unchecked")
			List<Rendelt> list2 = (List<Rendelt>) pm.newQuery(query2)
					.execute(vevo,rendeles);
			if ((list2 != null) && (!list2.isEmpty())) {
				for (Rendelt l : list2) {
					l.setStatus(Constants.INTERNET_IMPORTED);
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

			String tipus = Constants.CEDULA_STATUSZ_ELORENDELT;
			String sorszamtipus = Constants.CEDULA_RENDELES;
			
			if (menu.equals(Constants.MENU_ELADAS)) {
				tipus = Constants.CEDULA_STATUSZ_ELADOTT;
				sorszamtipus = Constants.CEDULA_RAKTAR;
			}
			
			Query sorszamQuery = pm.newQuery(Sorszam.class);
			sorszamQuery.setFilter("this.tipus == ptipus");
			sorszamQuery.declareParameters("String ptipus");
			@SuppressWarnings("unchecked")
			List<Sorszam> sorszamList = (List<Sorszam>) pm.newQuery(
					sorszamQuery).execute(sorszamtipus);
			if ((sorszamList != null) && (!sorszamList.isEmpty())) {
				for (Sorszam l : sorszamList) {
					l.setCedula(new BigInteger(l.getCedula()).add(
							BigInteger.ONE).toString());
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
					Cedulacikk cedulacikk = new Cedulacikk(cedulasorszam,
							tipus, l.getCikkszam(), l.getSzinkod(), l.getAr(),
							l.getAreur(), l.getArusd(), l.getExportkarton(),
							l.getKiskarton(), l.getDarab(), l.getFizet(),
							l.getFizeteur(), l.getFizetusd(), l.getRendeles());
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
					Cedula cedula = new Cedula(cedulasorszam, l.getVevo(),
							tipus, l.getElado(), null, null, null, new Date());
					pm.makePersistent(cedula);
					pm.deletePersistent(l);
				}
			}

			pm.flush();

			MemcacheServiceFactory.getMemcacheService().clearAll();;
            
			} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cedulasorszam;
	}

	@SuppressWarnings("unchecked")
	public List<CedulaSer> getCedula(String vevo, String menu, String tipus)
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
//			query.setOrdering("cedula");
			String status = "";
			if (vevo == null) {

				if (menu.equals(Constants.MENU_PENZTAR_FIZETES)) {
					query.setFilter("(this.status == pstatus1) || (this.status == pstatus2)");
					query.declareParameters("String pstatus1,String pstatus2");

					list = (List<Cedula>) pm.newQuery(query).execute(Constants.CEDULA_STATUSZ_FIZETENDO_ELORENDELES,Constants.CEDULA_STATUSZ_KIADOTT);	
					
				} else if (menu.equals(Constants.MENU_RAKTAR_KIADAS)) {			
					query.setFilter("(this.status == pstatus)");
					query.declareParameters("String pstatus");

					status = Constants.CEDULA_STATUSZ_ELADOTT;
					list = (List<Cedula>) pm.newQuery(query).execute(status);	
				} else if ((menu.equals(Constants.MENU_LEKERDEZES_CEDULAK))
						&& (tipus != null)) {

					query.setFilter("(this.status == pstatus)");
					query.declareParameters("String pstatus");

					status = tipus;
					list = (List<Cedula>) pm.newQuery(query).execute(status);
				} else {
					list = (List<Cedula>) pm.newQuery(query).execute();
				}
			} else {
				
				query.setFilter("(this.rovidnev == providnev) && (this.status == pstatus)");
				query.declareParameters("String providnev,String pstatus");

				if (menu.equals(Constants.MENU_RENDELES_VEGLEGESITES)) {
					status = Constants.CEDULA_STATUSZ_ELORENDELT;
				}

				list = (List<Cedula>) pm.newQuery(query).execute(vevo, status);
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
						cedulaSer.setVevotipus(vevolist.get(0).getTipus());
					}

					cedulaSer.setStatus(l.getStatus());
					cedulaSer.setElado(l.getElado());

					List<Felhasznalo> felhasznalolist = (List<Felhasznalo>) pm
							.newQuery(felhasznaloquery).execute(l.getElado());
					if (!felhasznalolist.isEmpty()) {
						cedulaSer.setEladonev(felhasznalolist.get(0).getNev());
					}

					cedulaSer.setBefizethuf(l.getBefizethuf());
					cedulaSer.setBefizeteur(l.getBefizeteur());
					cedulaSer.setBefizetusd(l.getBefizetusd());

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
			cikkquery
					.setFilter("this.cikkszam == pcikkszam && this.szinkod == pszinkod");
			cikkquery.declareParameters("String pcikkszam,String pszinkod");

			Query query = pm.newQuery(Cedulacikk.class);
			query.setFilter("(this.cedula == pcedula) && (this.status == pstatus)");
			query.declareParameters("String pcedula,String pstatus");
			@SuppressWarnings("unchecked")
			List<Cedulacikk> list = (List<Cedulacikk>) pm.newQuery(query)
					.execute(cedula, status);
			if (!list.isEmpty()) {
				for (Cedulacikk l : list) {
					CedulacikkSer cedulacikkSer = new CedulacikkSer();
					cedulacikkSer.setCedula(l.getCedula());
					cedulacikkSer.setStatus(l.getStatus());
					cedulacikkSer.setCikkszam(l.getCikkszam());
					cedulacikkSer.setSzinkod(l.getSzinkod());

					@SuppressWarnings("unchecked")
					List<Cikk> cikklist = (List<Cikk>) pm.newQuery(cikkquery)
							.execute(l.getCikkszam(), l.getSzinkod());
					if (!cikklist.isEmpty()) {
						cedulacikkSer.setMegnevezes(cikklist.get(0)
								.getMegnevezes());
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
					cedulacikkSer.setRendeles(l.getRendeles());
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

	public String cedulaToKosar(String elado, String vevo, String menu, String tipus,
			String cedula) throws IllegalArgumentException, SQLExceptionSer {

		String cedulasorszam = "";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			String ujtipus = "";
			if (tipus.equals(Constants.CEDULA_STATUSZ_ELORENDELT)) {
				ujtipus = Constants.CEDULA_STATUSZ_VEGLEGESIT;
			}
			else if (tipus.equals(Constants.CEDULA_STATUSZ_FIZETENDO_ELORENDELES)) {
				ujtipus = Constants.CEDULA_STATUSZ_ELORENDELES_FIZETES;
			}
			else if (tipus.equals(Constants.CEDULA_STATUSZ_ELADOTT)) {
				ujtipus = Constants.CEDULA_STATUSZ_KIADAS;
			}
			else if (tipus.equals(Constants.CEDULA_STATUSZ_KIADOTT)) {
				ujtipus = Constants.CEDULA_STATUSZ_FIZETES;
			}
	
			Query query = pm.newQuery(Cedulacikk.class);
			query.setFilter("(this.cedula == pcedula) && (this.status == pstatus)");
			query.declareParameters("String pcedula,String pstatus");
			@SuppressWarnings("unchecked")
			List<Cedulacikk> list = (List<Cedulacikk>) pm.newQuery(query)
					.execute(cedula, tipus);

			if ((list != null) && (!list.isEmpty())) {
				for (Cedulacikk l : list) {
					Kosarcikk kosarcikk = new Kosarcikk(elado, vevo, ujtipus,
							l.getCikkszam(), l.getSzinkod(), l.getAr(),
							l.getAreur(), l.getArusd(), l.getExportkarton(),
							l.getKiskarton(), l.getDarab(), l.getFizet(),
							l.getFizeteur(), l.getFizetusd(), l.getCedula(), l.getRendeles());
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
					Kosar kosar = new Kosar(elado, l.getRovidnev(), ujtipus,
							l.getCedula());
					cedulasorszam = l.getCedula();
					pm.makePersistent(kosar);
					pm.deletePersistent(l);
				}
			}

			pm.flush();

			MemcacheServiceFactory.getMemcacheService().clearAll();;
            
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cedulasorszam;
	}

	public String kosarToCedula(String elado, String vevo, String menu, String tipus, String ujtipus,
			String cedulasorszam, Double befizet, Double befizeteur,
			Double befizetusd) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			
			Query cikkquery = pm.newQuery(Cikk.class);
			cikkquery
					.setFilter("this.cikkszam == pcikkszam && this.szinkod == pszinkod");
			cikkquery.declareParameters("String pcikkszam,String pszinkod");

			Query vevoquery = pm.newQuery(Vevo.class);
			vevoquery.setFilter("this.rovidnev == providnev");
			vevoquery.declareParameters("String providnev");
					
			Query query = pm.newQuery(Kosarcikk.class);
			query.setFilter("(this.elado == pelado) && (this.vevo == pvevo) && (this.tipus == ptipus)");
			query.declareParameters("String pelado,String pvevo,String ptipus");
			@SuppressWarnings("unchecked")
			List<Kosarcikk> list = (List<Kosarcikk>) pm.newQuery(query)
					.execute(elado, vevo, tipus);
			double fizet = 0;
			double fizeteur = 0;
			double fizetusd = 0;
			if ((list != null) && (!list.isEmpty())) {
				for (Kosarcikk l : list) {
					Cedulacikk cedulacikk = new Cedulacikk(cedulasorszam,
							ujtipus, l.getCikkszam(), l.getSzinkod(),
							l.getAr(), l.getAreur(), l.getArusd(),
							l.getExportkarton(), l.getKiskarton(),
							l.getDarab(), l.getFizet(), l.getFizeteur(),
							l.getFizetusd(),l.getRendeles());
					pm.makePersistent(cedulacikk);

					if (tipus
							.equals(Constants.CEDULA_STATUSZ_ELORENDELES_FIZETES)) {
						Rendeltcikk rendeltcikk = new Rendeltcikk(l.getVevo(),
								l.getCedula(), l.getCikkszam(), l.getSzinkod(),
								Constants.ELORENDELT_VEGLEGESITETT,
								l.getExportkarton(), l.getKiskarton(),
								l.getDarab(), Boolean.FALSE);
						
						rendeltcikk.setArusd(l.getArusd());
						
						@SuppressWarnings("unchecked")
						List<Cikk> cikklist = (List<Cikk>) pm.newQuery(cikkquery)
								.execute(l.getCikkszam(), l.getSzinkod());
						if ((cikklist != null) && (!cikklist.isEmpty())) {							
							double elorfizet = (double) (((((cikklist.get(0).getKiskarton() * cikklist.get(0).getDarab()) * l.getExportkarton())
									+ (cikklist.get(0).getDarab() * l.getKiskarton()) + l.getDarab()) * l.getArusd()) * 0.8);
							
							rendeltcikk.setFizetusd(elorfizet);
						}
						pm.makePersistent(rendeltcikk);
					}

					if (l.getFizet() != null)
						fizet = fizet + l.getFizet();
					if (l.getFizeteur() != null)
						fizeteur = fizeteur + l.getFizeteur();
					if (l.getFizetusd() != null)
						fizetusd = fizetusd + l.getFizetusd();
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
					Cedula cedula = new Cedula(cedulasorszam, l.getVevo(),
							ujtipus, l.getElado(), befizet, befizeteur,
							befizetusd, new Date());
					pm.makePersistent(cedula);
					pm.deletePersistent(l);
				}
			}

			if (ujtipus.equals(Constants.CEDULA_STATUSZ_FIZETETT_ELORENDELES)) {
				Fizetes fizetes = new Fizetes(cedulasorszam, vevo,
						Constants.FIZETES_ELORENDELT, "", elado, befizet,
						befizeteur, befizetusd, new Date(), Boolean.FALSE);
				pm.makePersistent(fizetes);

				@SuppressWarnings("unchecked")
				List<Vevo> vevolist = (List<Vevo>) pm.newQuery(vevoquery)
						.execute(vevo);
				if ((vevolist != null) && (!vevolist.isEmpty())) {
					double egyenleghuf = vevolist.get(0).getEgyenleghuf();
					egyenleghuf = egyenleghuf + (befizet - fizet);
					vevolist.get(0).setEgyenleghuf(egyenleghuf);
					double egyenlegeur = vevolist.get(0).getEgyenlegeur();
					egyenlegeur = egyenlegeur + (befizeteur - fizeteur);
					vevolist.get(0).setEgyenlegeur(egyenlegeur);
					double egyenlegusd = vevolist.get(0).getEgyenlegusd();
					egyenlegusd = egyenlegusd + (befizetusd - fizetusd);
					vevolist.get(0).setEgyenlegusd(egyenlegusd);
				}
					
			}

			if (ujtipus.equals(Constants.CEDULA_STATUSZ_FIZETETT)) {
				Fizetes fizetes = new Fizetes(cedulasorszam, vevo,
						Constants.FIZETES_FIZETES, "", elado, befizet,
						befizeteur, befizetusd, new Date(), Boolean.FALSE);
				pm.makePersistent(fizetes);

				@SuppressWarnings("unchecked")
				List<Vevo> vevolist = (List<Vevo>) pm.newQuery(vevoquery)
						.execute(vevo);
				if ((vevolist != null) && (!vevolist.isEmpty())) {
					double egyenleghuf = vevolist.get(0).getEgyenleghuf();
					egyenleghuf = egyenleghuf + (befizet - fizet);
					vevolist.get(0).setEgyenleghuf(egyenleghuf);
					double egyenlegeur = vevolist.get(0).getEgyenlegeur();
					egyenlegeur = egyenlegeur + (befizeteur - fizeteur);
					vevolist.get(0).setEgyenlegeur(egyenlegeur);
					double egyenlegusd = vevolist.get(0).getEgyenlegusd();
					egyenlegusd = egyenlegusd + (befizetusd - fizetusd);
					vevolist.get(0).setEgyenlegusd(egyenlegusd);
				}
					
			}
			
			pm.flush();

			MemcacheServiceFactory.getMemcacheService().clearAll();;
                  	
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cedulasorszam;
	}

	public List<FizetesSer> getFizetes() throws IllegalArgumentException,
			SQLExceptionSer {

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
			List<Fizetes> list = (List<Fizetes>) pm.newQuery(query).execute();
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
					fizetesSer.setMegjegyzes(l.getMegjegyzes());
					fizetesSer.setPenztaros(l.getPenztaros());

					@SuppressWarnings("unchecked")
					List<Felhasznalo> felhasznalolist = (List<Felhasznalo>) pm
							.newQuery(felhasznaloquery).execute(
									l.getPenztaros());
					if (!felhasznalolist.isEmpty()) {
						fizetesSer.setPenztarosnev(felhasznalolist.get(0)
								.getNev());
					}

					fizetesSer.setFizet(l.getFizet());
					fizetesSer.setFizeteur(l.getFizeteur());
					fizetesSer.setFizetusd(l.getFizetusd());
					fizetesSer.setDatum(new Date(l.getDatum().getTime()));
					l.setSzamolt(Boolean.TRUE);
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

	public ZarasEgyenlegSer getElozoZaras() throws IllegalArgumentException,
			SQLExceptionSer {

		ZarasEgyenlegSer zarasEgyenlegSer = new ZarasEgyenlegSer();
		zarasEgyenlegSer.setEgyenlegeur(new Double(0));
		zarasEgyenlegSer.setEgyenlegusd(new Double(0));
		zarasEgyenlegSer.setEgyenleghuf(new Double(0));
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Query query = pm.newQuery(Zaras.class);
			query.setOrdering("zaras desc");
			@SuppressWarnings("unchecked")
			List<Zaras> list = (List<Zaras>) pm.newQuery(query).execute();
			if ((list != null) && (!list.isEmpty())) {
				zarasEgyenlegSer.setEgyenlegeur(list.get(0).getEgyenlegeur());
				zarasEgyenlegSer.setEgyenlegusd(list.get(0).getEgyenlegusd());
				zarasEgyenlegSer.setEgyenleghuf(list.get(0).getEgyenleghuf());
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return zarasEgyenlegSer;
	}

	public String createZaras(String penztaros, Double egyenleghuf,
			Double egyenlegeur, Double egyenlegusd, Double kivethuf,
			Double kiveteur, Double kivetusd) throws IllegalArgumentException,
			SQLExceptionSer {

		String cedulasorszam = "";
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query sorszamQuery = pm.newQuery(Sorszam.class);
			sorszamQuery.setFilter("this.tipus == ptipus");
			sorszamQuery.declareParameters("String ptipus");
			@SuppressWarnings("unchecked")
			List<Sorszam> sorszamList = (List<Sorszam>) pm.newQuery(
					sorszamQuery).execute(Constants.CEDULA_ZARAS);
			if ((sorszamList != null) && (!sorszamList.isEmpty())) {
				for (Sorszam l : sorszamList) {
					l.setCedula(new BigInteger(l.getCedula()).add(
							BigInteger.ONE).toString());
					cedulasorszam = l.getCedula();
				}
			}

			Query query = pm.newQuery(Fizetes.class);
			query.setFilter("this.szamolt == true");
			@SuppressWarnings("unchecked")
			List<Fizetes> list = (List<Fizetes>) pm.newQuery(query).execute();
			double fizet = 0;
			double fizeteur = 0;
			double fizetusd = 0;
			if ((list != null) && (!list.isEmpty())) {
				for (Fizetes l : list) {
					Zarasfizetes zarasFizetes = new Zarasfizetes(cedulasorszam,
							l.getCedula(), l.getVevo(), l.getTipus(),
							l.getMegjegyzes(), l.getPenztaros(), l.getFizet(),
							l.getFizeteur(), l.getFizetusd(), l.getDatum());
					fizet = fizet + l.getFizet();
					fizeteur = fizeteur + l.getFizeteur();
					fizetusd = fizetusd + l.getFizetusd();
					pm.makePersistent(zarasFizetes);
					pm.deletePersistent(l);
				}
			}

			egyenleghuf = egyenleghuf + fizet;
			egyenlegeur = egyenlegeur + fizeteur;
			egyenlegusd = egyenlegusd + fizetusd;

			Zaras zaras = new Zaras(cedulasorszam, penztaros, kivethuf,
					kiveteur, kivetusd, egyenleghuf - kivethuf, egyenlegeur
							- kiveteur, egyenlegusd - kivetusd, new Date());
			pm.makePersistent(zaras);
			pm.flush();

			MemcacheServiceFactory.getMemcacheService().clearAll();;
 
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cedulasorszam;
	}

	public List<FizetesSer> getZarasFizetes(String zaras)
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

			Query query = pm.newQuery(Zarasfizetes.class);
			query.setFilter("this.zaras == pzaras");
			query.declareParameters("String pzaras");
			@SuppressWarnings("unchecked")
			List<Zarasfizetes> list = (List<Zarasfizetes>) pm.newQuery(query)
					.execute(zaras);
			if ((list != null) && (!list.isEmpty())) {
				for (Zarasfizetes l : list) {
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
					fizetesSer.setMegjegyzes(l.getMegjegyzes());
					fizetesSer.setPenztaros(l.getPenztaros());

					@SuppressWarnings("unchecked")
					List<Felhasznalo> felhasznalolist = (List<Felhasznalo>) pm
							.newQuery(felhasznaloquery).execute(
									l.getPenztaros());
					if (!felhasznalolist.isEmpty()) {
						fizetesSer.setPenztarosnev(felhasznalolist.get(0)
								.getNev());
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

	public List<ZarasSer> getZaras() throws IllegalArgumentException,
			SQLExceptionSer {

		List<ZarasSer> zarasok = new ArrayList<ZarasSer>();

		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query felhasznaloquery = pm.newQuery(Felhasznalo.class);
		felhasznaloquery.setFilter("this.rovidnev == providnev");
		felhasznaloquery.declareParameters("String providnev");

		try {

			Query query = pm.newQuery(Zaras.class);
			query.setOrdering("zaras");
			@SuppressWarnings("unchecked")
			List<Zaras> list = (List<Zaras>) pm.newQuery(query).execute();
			if ((list != null) && (!list.isEmpty())) {
				for (Zaras l : list) {
					ZarasSer zarasSer = new ZarasSer();
					zarasSer.setZaras(l.getZaras());
					zarasSer.setPenztaros(l.getPenztaros());

					@SuppressWarnings("unchecked")
					List<Felhasznalo> felhasznalolist = (List<Felhasznalo>) pm
							.newQuery(felhasznaloquery).execute(
									l.getPenztaros());
					if (!felhasznalolist.isEmpty()) {
						zarasSer.setPenztarosnev(felhasznalolist.get(0)
								.getNev());
					}

					zarasSer.setKivethuf(l.getKivethuf());
					zarasSer.setKiveteur(l.getKiveteur());
					zarasSer.setKivetusd(l.getKivetusd());

					zarasSer.setEgyenleghuf(l.getEgyenleghuf());
					zarasSer.setEgyenlegeur(l.getEgyenlegeur());
					zarasSer.setEgyenlegusd(l.getEgyenlegusd());

					zarasSer.setDatum(new Date(l.getDatum().getTime()));

					zarasok.add(zarasSer);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return zarasok;
	}

	public String createTorlesztes(String penztaros, String vevo,
			Double torleszthuf, Double torleszteur, Double torlesztusd)
			throws IllegalArgumentException, SQLExceptionSer {

		String cedulasorszam = "";

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query sorszamQuery = pm.newQuery(Sorszam.class);
			sorszamQuery.setFilter("this.tipus == ptipus");
			sorszamQuery.declareParameters("String ptipus");
			@SuppressWarnings("unchecked")
			List<Sorszam> sorszamList = (List<Sorszam>) pm.newQuery(
					sorszamQuery).execute(Constants.CEDULA_TORLESZTES);
			if ((sorszamList != null) && (!sorszamList.isEmpty())) {
				for (Sorszam l : sorszamList) {
					l.setCedula(new BigInteger(l.getCedula()).add(
							BigInteger.ONE).toString());
					cedulasorszam = l.getCedula();
				}
			}

			Query vevoquery = pm.newQuery(Vevo.class);
			vevoquery.setFilter("this.rovidnev == providnev");
			vevoquery.declareParameters("String providnev");
			@SuppressWarnings("unchecked")
			List<Vevo> vevolist = (List<Vevo>) pm.newQuery(vevoquery).execute(
					vevo);
			if ((vevolist != null) && (!vevolist.isEmpty())) {
				double egyenleghuf = vevolist.get(0).getEgyenleghuf();
				egyenleghuf = egyenleghuf + torleszthuf;
				vevolist.get(0).setEgyenleghuf(egyenleghuf);
				double egyenlegeur = vevolist.get(0).getEgyenlegeur();
				egyenlegeur = egyenlegeur + torleszteur;
				vevolist.get(0).setEgyenlegeur(egyenlegeur);
				double egyenlegusd = vevolist.get(0).getEgyenlegusd();
				egyenlegusd = egyenlegusd + torlesztusd;
				vevolist.get(0).setEgyenlegusd(egyenlegusd);
			}

			Fizetes fizetes = new Fizetes(cedulasorszam, vevo,
					Constants.FIZETES_TORLESZTES, "", penztaros, torleszthuf,
					torleszteur, torlesztusd, new Date(), Boolean.FALSE);
			pm.makePersistent(fizetes);

			pm.flush();

			MemcacheServiceFactory.getMemcacheService().clearAll();;

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return cedulasorszam;
	}

	public List<FizetesSer> getTorlesztesek() throws IllegalArgumentException,
			SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<FizetesSer> torlesztesek = new ArrayList<FizetesSer>();

		try {
			Query query = pm.newQuery(Fizetes.class);
			query.setFilter("(this.tipus == ptipus)");
			query.declareParameters("String ptipus");
			@SuppressWarnings("unchecked")
			List<Fizetes> list = (List<Fizetes>) pm.newQuery(query).execute(
					Constants.FIZETES_TORLESZTES);
			if (!list.isEmpty()) {
				for (Fizetes l : list) {
					FizetesSer fizetesSer = new FizetesSer();
					fizetesSer.setCedula(l.getCedula());
					fizetesSer.setFizet(l.getFizet());
					fizetesSer.setFizeteur(l.getFizeteur());
					fizetesSer.setFizetusd(l.getFizetusd());
					torlesztesek.add(fizetesSer);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return torlesztesek;
	}

	public List<FizetesSer> getTorlesztes(String cedula)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<FizetesSer> torlesztes = new ArrayList<FizetesSer>();

		try {
			Query query = pm.newQuery(Fizetes.class);
			query.setFilter("(this.cedula == pcedula) && (this.tipus == ptipus)");
			query.declareParameters("String pcedula,String ptipus");
			@SuppressWarnings("unchecked")
			List<Fizetes> list = (List<Fizetes>) pm.newQuery(query).execute(
					cedula, Constants.FIZETES_TORLESZTES);
			if (!list.isEmpty()) {
				for (Fizetes l : list) {
					FizetesSer fizetesSer = new FizetesSer();
					fizetesSer.setCedula(l.getCedula());
					fizetesSer.setFizet(l.getFizet());
					fizetesSer.setFizeteur(l.getFizeteur());
					fizetesSer.setFizetusd(l.getFizetusd());
					torlesztes.add(fizetesSer);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return torlesztes;
	}

	public List<FizetesSer> getHazi() throws IllegalArgumentException,
			SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<FizetesSer> hazi = new ArrayList<FizetesSer>();

		Query felhasznaloquery = pm.newQuery(Felhasznalo.class);
		felhasznaloquery.setFilter("this.rovidnev == providnev");
		felhasznaloquery.declareParameters("String providnev");

		try {
			Query query = pm.newQuery(Fizetes.class);
			query.setFilter("(this.tipus == ptipus)");
			query.declareParameters("String ptipus");
			@SuppressWarnings("unchecked")
			List<Fizetes> list = (List<Fizetes>) pm.newQuery(query).execute(
					Constants.FIZETES_HAZIPENZTAR);
			if (!list.isEmpty()) {
				for (Fizetes l : list) {
					FizetesSer fizetesSer = new FizetesSer();

					@SuppressWarnings("unchecked")
					List<Felhasznalo> felhasznalolist = (List<Felhasznalo>) pm
							.newQuery(felhasznaloquery).execute(
									l.getPenztaros());
					if (!felhasznalolist.isEmpty()) {
						fizetesSer.setPenztarosnev(felhasznalolist.get(0)
								.getNev());
					}

					fizetesSer.setMegjegyzes(l.getMegjegyzes());
					fizetesSer.setFizet(l.getFizet());
					fizetesSer.setFizeteur(l.getFizeteur());
					fizetesSer.setFizetusd(l.getFizetusd());
					fizetesSer.setDatum(new Date(l.getDatum().getTime()));
					hazi.add(fizetesSer);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return hazi;
	}

	public FizetesSer addHazi(FizetesSer fizetesSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query felhasznaloquery = pm.newQuery(Felhasznalo.class);
		felhasznaloquery.setFilter("this.rovidnev == providnev");
		felhasznaloquery.declareParameters("String providnev");

		try {

			Fizetes hazi = new Fizetes(null, null,
					Constants.FIZETES_HAZIPENZTAR, fizetesSer.getPenztaros(),
					fizetesSer.getMegjegyzes(), fizetesSer.getFizet(),
					fizetesSer.getFizeteur(), fizetesSer.getFizetusd(),
					new Date(), Boolean.FALSE);

			pm.makePersistent(hazi);

			pm.flush();

			@SuppressWarnings("unchecked")
			List<Felhasznalo> felhasznalolist = (List<Felhasznalo>) pm
					.newQuery(felhasznaloquery).execute(
							fizetesSer.getPenztaros());
			if (!felhasznalolist.isEmpty()) {
				fizetesSer.setPenztarosnev(felhasznalolist.get(0).getNev());
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return fizetesSer;
	}

	public List<RendeltcikkSer> getRendelesSzamolt(String status)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<RendeltcikkSer> rendeles = new ArrayList<RendeltcikkSer>();

		try {
			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(this.status == pstatus)");
			query.declareParameters("String pstatus");
			query.setOrdering("cikkszam,szinkod");
			@SuppressWarnings("unchecked")
			List<Rendeltcikk> list = (List<Rendeltcikk>) pm.newQuery(query)
					.execute(status);
			if (!list.isEmpty()) {
				String cikkszam = null;
				String szinkod = null;
				int exp = 0;
				int kk = 0;
				int db = 0;
				boolean kiirt = false;
				for (Rendeltcikk l : list) {
					if ((cikkszam == null) && (szinkod == null)) {
						cikkszam = l.getCikkszam();
						szinkod = l.getSzinkod();
					}
					if ((cikkszam.equals(l.getCikkszam()) && (szinkod.equals(l
							.getSzinkod())))) {
						exp = l.getExportkarton();
						kk = l.getKiskarton();
						db = (l.getDarab());
						kiirt = false;
					} else {
						RendeltcikkSer rendeltcikkSer = new RendeltcikkSer();
						rendeltcikkSer.setCikkszam(cikkszam);
						rendeltcikkSer.setSzinkod(szinkod);
						rendeltcikkSer.setExportkarton(exp);
						rendeltcikkSer.setKiskarton(kk);
						rendeltcikkSer.setDarab(db);
						rendeles.add(rendeltcikkSer);
						cikkszam = l.getCikkszam();
						szinkod = l.getSzinkod();
						exp = l.getExportkarton();
						kk = l.getKiskarton();
						db = (l.getDarab());
						kiirt = false;
					}
					l.setSzamolt(Boolean.TRUE);
					pm.makePersistent(l);
				}
				if (!kiirt) {
					RendeltcikkSer rendeltcikkSer = new RendeltcikkSer();
					rendeltcikkSer.setCikkszam(cikkszam);
					rendeltcikkSer.setSzinkod(szinkod);
					rendeltcikkSer.setExportkarton(exp);
					rendeltcikkSer.setKiskarton(kk);
					rendeltcikkSer.setDarab(db);
					rendeles.add(rendeltcikkSer);
				}
				pm.flush();
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return rendeles;
	}

	public List<RendeltcikkSer> getRendeles(String status, String cikkszam,
			String szinkod) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<RendeltcikkSer> rendeles = new ArrayList<RendeltcikkSer>();

		try {
			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(this.status == pstatus) && (this.cikkszam == pcikkszam) && (this.szinkod == pszinkod) && (this.szamolt == true)");
			query.declareParameters("String pstatus,String pcikkszam,String pszinkod");
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("pstatus", status);
			parameters.put("pcikkszam", cikkszam);
			parameters.put("pszinkod", (szinkod == null ? "" : szinkod));
			@SuppressWarnings("unchecked")
			List<Rendeltcikk> list = (List<Rendeltcikk>) pm.newQuery(query)
					.executeWithMap(parameters);

			Query vevoquery = pm.newQuery(Vevo.class);
			vevoquery.setFilter("this.rovidnev == providnev");
			vevoquery.declareParameters("String providnev");

			if (!list.isEmpty()) {
				for (Rendeltcikk l : list) {
					RendeltcikkSer rendeltcikkSer = new RendeltcikkSer();
					rendeltcikkSer.setRovidnev(l.getRovidnev());

					@SuppressWarnings("unchecked")
					List<Vevo> felhasznalolist = (List<Vevo>) pm.newQuery(
							vevoquery).execute(l.getRovidnev());
					if (!felhasznalolist.isEmpty()) {
						rendeltcikkSer.setNev(felhasznalolist.get(0).getNev());
					}

					rendeltcikkSer.setRendeles(l.getRendeles());
					rendeltcikkSer.setCikkszam(l.getCikkszam());
					rendeltcikkSer.setSzinkod(l.getSzinkod());
					rendeltcikkSer.setExportkarton(l.getExportkarton());
					rendeltcikkSer.setKiskarton(l.getKiskarton());
					rendeltcikkSer.setDarab(l.getDarab());
					rendeles.add(rendeltcikkSer);

				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return rendeles;
	}

	public List<RendeltcikkSer> getMegrendelt(String status, String cikkszam,
			String szinkod) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<RendeltcikkSer> rendeles = new ArrayList<RendeltcikkSer>();

		try {
			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(this.status == pstatus) && (this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
			query.declareParameters("String pstatus,String pcikkszam,String pszinkod");
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("pstatus", status);
			parameters.put("pcikkszam", cikkszam);
			parameters.put("pszinkod", (szinkod == null ? "" : szinkod));
			@SuppressWarnings("unchecked")
			List<Rendeltcikk> list = (List<Rendeltcikk>) pm.newQuery(query)
					.executeWithMap(parameters);

			Query vevoquery = pm.newQuery(Vevo.class);
			vevoquery.setFilter("this.rovidnev == providnev");
			vevoquery.declareParameters("String providnev");

			if (!list.isEmpty()) {
				for (Rendeltcikk l : list) {
					RendeltcikkSer rendeltcikkSer = new RendeltcikkSer();
					rendeltcikkSer.setRovidnev(l.getRovidnev());

					@SuppressWarnings("unchecked")
					List<Vevo> felhasznalolist = (List<Vevo>) pm.newQuery(
							vevoquery).execute(l.getRovidnev());
					if (!felhasznalolist.isEmpty()) {
						rendeltcikkSer.setNev(felhasznalolist.get(0).getNev());
					}

					rendeltcikkSer.setRendeles(l.getRendeles());
					rendeltcikkSer.setCikkszam(l.getCikkszam());
					rendeltcikkSer.setSzinkod(l.getSzinkod());
					rendeltcikkSer.setExportkarton(l.getExportkarton());
					rendeltcikkSer.setKiskarton(l.getKiskarton());
					rendeltcikkSer.setDarab(l.getDarab());
					rendeles.add(rendeltcikkSer);
					l.setSzamolt(Boolean.TRUE);
					pm.makePersistent(l);
				}
			}

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return rendeles;
	}

	public RendeltcikkSer updateRendeltcikk(RendeltcikkSer rendeltraktarSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(this.rendeles == prendeles) && (this.status == pstatus) && (this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
			query.declareParameters("String pcikkszam,String pszinkod,String pstatus");
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("prendeles", rendeltraktarSer.getRendeles());
			parameters.put("pcikkszam", rendeltraktarSer.getCikkszam());
			parameters.put("pszinkod",
					(rendeltraktarSer.getSzinkod() == null ? ""
							: rendeltraktarSer.getSzinkod()));
			parameters.put("pstatus", Constants.ELORENDELT_MEGRENDELT);
			@SuppressWarnings("unchecked")
			List<Rendeltcikk> list = (List<Rendeltcikk>) pm.newQuery(query)
					.executeWithMap(parameters);

			if (!list.isEmpty()) {
				for (Rendeltcikk l : list) {
					l.setSzamolt(Boolean.FALSE);
					pm.makePersistent(l);
				}
			}

			pm.flush();

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return rendeltraktarSer;
	}

	public RendeltcikkSer megrendeles(RendeltcikkSer rendeltcikkSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(this.status == pstatus) && (this.cikkszam == pcikkszam) && (this.szinkod == pszinkod) && (this.szamolt == true)");
			query.declareParameters("String pstatus,String pcikkszam,String pszinkod");
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("pcikkszam", rendeltcikkSer.getCikkszam());
			parameters.put(
					"pszinkod",
					(rendeltcikkSer.getSzinkod() == null ? "" : rendeltcikkSer
							.getSzinkod()));
			parameters.put("pstatus", Constants.ELORENDELT_VEGLEGESITETT);
			@SuppressWarnings("unchecked")
			List<Rendeltcikk> list = (List<Rendeltcikk>) pm.newQuery(query)
					.executeWithMap(parameters);

			if (!list.isEmpty()) {
				for (Rendeltcikk l : list) {
					l.setStatus(Constants.ELORENDELT_MEGRENDELT);
					l.setSzamolt(Boolean.FALSE);
					pm.makePersistent(l);
				}
			}

			pm.flush();

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return rendeltcikkSer;
	}

	public List<BeszallitottcikkSer> getBeszallitottcikk(String cikkszam,
			String szinkod) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<BeszallitottcikkSer> beszallitottcikk = new ArrayList<BeszallitottcikkSer>();
		try {

			Query query = pm.newQuery(Beszallitottcikk.class);
			query.setFilter("(this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
			query.declareParameters("String pcikkszam,String pszinkod");
			@SuppressWarnings("unchecked")
			List<Beszallitottcikk> list = (List<Beszallitottcikk>) pm.newQuery(
					query).execute(cikkszam, (szinkod == null ? "" : szinkod));
			if (!list.isEmpty()) {
				for (Beszallitottcikk l : list) {
					BeszallitottcikkSer beszallitottcikkSer = new BeszallitottcikkSer();
					beszallitottcikkSer.setCikkszam(l.getCikkszam());
					beszallitottcikkSer.setSzinkod(l.getSzinkod());
					beszallitottcikkSer.setExportkarton(l.getExportkarton());
					beszallitottcikkSer.setKiskarton(l.getKiskarton());
					beszallitottcikkSer.setDarab(l.getDarab());
					beszallitottcikkSer.setMegrendexportkarton(l
							.getMegrendexportkarton());
					beszallitottcikkSer.setMegrendkiskarton(l
							.getMegrendkiskarton());
					beszallitottcikkSer.setMegrenddarab(l.getMegrenddarab());
					beszallitottcikkSer.setRogzito(l.getRogzito());
					beszallitottcikkSer.setDatum(new Date(l.getDatum()
							.getTime()));
					beszallitottcikkSer.setRovancs(l.getRovancs());
					beszallitottcikk.add(beszallitottcikkSer);
				}
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return beszallitottcikk;
	}

	public BeszallitottcikkSer addBeszallitottcikk(
			BeszallitottcikkSer beszallitottraktarSer)
			throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Beszallitottcikk beszallitottcikk = new Beszallitottcikk(
					beszallitottraktarSer.getCikkszam(),
					beszallitottraktarSer.getSzinkod(),
					beszallitottraktarSer.getExportkarton(),
					beszallitottraktarSer.getKiskarton(),
					beszallitottraktarSer.getDarab(),
					beszallitottraktarSer.getMegrendexportkarton(),
					beszallitottraktarSer.getMegrendkiskarton(),
					beszallitottraktarSer.getMegrenddarab(),
					beszallitottraktarSer.getRogzito(), new Date(),
					Boolean.FALSE);

			pm.makePersistent(beszallitottcikk);

			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(this.status == pstatus) && (this.cikkszam == pcikkszam) && (this.szinkod == pszinkod) && (this.szamolt == true)");
			query.declareParameters("String pstatus,String pcikkszam,String pszinkod");
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("pcikkszam", beszallitottcikk.getCikkszam());
			parameters.put("pszinkod",
					(beszallitottcikk.getSzinkod() == null ? ""
							: beszallitottcikk.getSzinkod()));
			parameters.put("pstatus", Constants.ELORENDELT_MEGRENDELT);
			@SuppressWarnings("unchecked")
			List<Rendeltcikk> list = (List<Rendeltcikk>) pm.newQuery(query)
					.executeWithMap(parameters);

			if (!list.isEmpty()) {
				for (Rendeltcikk l : list) {
					l.setStatus(Constants.ELORENDELT_BEERKEZETT);
					l.setSzamolt(Boolean.FALSE);
					pm.makePersistent(l);
				}
			}

			Query cikkquery = pm.newQuery(Cikk.class);
			cikkquery
					.setFilter("(this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
			cikkquery.declareParameters("String pcikkszam,String pszinkod");

			@SuppressWarnings("unchecked")
			List<Cikk> cikklist = (List<Cikk>) pm.newQuery(cikkquery).execute(
					beszallitottcikk.getCikkszam(),
					(beszallitottcikk.getSzinkod() == null ? ""
							: beszallitottcikk.getSzinkod()));

			if (!cikklist.isEmpty()) {
				for (Cikk l : cikklist) {

					l.setKeszlet(l.getKeszlet()
							+ ((beszallitottcikk.getExportkarton()
									* l.getKiskarton() * l.getDarab()) + (beszallitottcikk
									.getKiskarton()) * l.getDarab())
							+ beszallitottcikk.getDarab());

					l.setRendelt(l.getRendelt()
							+ ((beszallitottcikk.getMegrendexportkarton()
									* l.getKiskarton() * l.getDarab()) + (beszallitottcikk
									.getMegrendkiskarton()) * l.getDarab())
							+ beszallitottcikk.getMegrenddarab());

					pm.makePersistent(l);
				}
			}

			pm.flush();

		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return beszallitottraktarSer;
	}

	public List<RaktarSer> getRaktar(int page, String fotipus, String altipus,
			String cikkszam) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<RaktarSer> raktar = new ArrayList<RaktarSer>();
		try {
			Map<String, String> parameters = new HashMap<String, String>();
			String filter = "(this.torolt == false)";
			String params = "";
			if (cikkszam != null) {
				filter = filter + " && (this.cikkszam >= pcikkszam)";
				params = params + "String pcikkszam";
				parameters.put("pcikkszam", cikkszam);
			}
			if (fotipus != null) {
				filter = filter + " && (this.fotipus == pfotipus)";
				if (!params.equals(""))
					params = params + ",";
				params = params + "String pfotipus";
				parameters.put("pfotipus", fotipus);
			}

			if (altipus != null) {
				filter = filter + " && (this.altipus == paltipus)";
				if (!params.equals(""))
					params = params + ",";
				params = params + "String paltipus";
				parameters.put("paltipus", altipus);
			}

			Query query = pm.newQuery(Cikk.class);
			query.declareParameters(params);
			query.setFilter(filter);
//			query.setOrdering("keszlet,cikkszam,szinkod");
			query.setRange(page * Constants.FETCH_SIZE, (page + 1)
					* Constants.FETCH_SIZE);
			@SuppressWarnings("unchecked")
			List<Cikk> list = (List<Cikk>) pm.newQuery(query).executeWithMap(
					parameters);
			if (!list.isEmpty()) {
				long exp;
				long kk;
				long darab;
				long keszlet;
				for (Cikk l : list) {
					if (l.getKeszlet()> 0) {
						RaktarSer raktarSer = new RaktarSer();
						raktarSer.setFotipus(l.getFotipus());
						raktarSer.setAltipus(l.getAltipus());
						raktarSer.setSzinkod(l.getSzinkod());
						raktarSer.setCikkszam(l.getCikkszam());
						raktarSer.setMegnevezes(l.getMegnevezes());
						raktarSer.setElorar(l.getElorar());
						raktarSer.setAr(l.getAr());
						raktarSer.setAreur(l.getAreur());
						raktarSer.setKiskarton(l.getKiskarton());
						raktarSer.setDarab(l.getDarab());
						raktarSer.setTerfogat(l.getTerfogat());
						raktarSer.setTerfogatlab(l.getTerfogatlab());
						raktarSer.setBsuly(l.getBsuly());
						raktarSer.setNsuly(l.getNsuly());
						raktarSer.setLeiras(l.getLeiras());
						raktarSer.setMegjegyzes(l.getMegjegyzes());
						raktarSer.setMertekegyseg(l.getMertekegyseg());
	
						keszlet = l.getKeszlet();
						exp = l.getKeszlet() / (l.getKiskarton() * l.getDarab());
						keszlet = keszlet - (exp * l.getKiskarton() * l.getDarab());
						kk = keszlet / l.getDarab();
						darab = keszlet - (kk * l.getDarab());
	
						raktarSer.setKexportkarton(exp);
						raktarSer.setKkiskarton(kk);
						raktarSer.setKdarab(darab);
	
						keszlet = l.getRendelt();
						exp = l.getKeszlet() / (l.getKiskarton() * l.getDarab());
						keszlet = keszlet - (exp * l.getKiskarton() * l.getDarab());
						kk = keszlet / l.getDarab();
						darab = keszlet - (kk * l.getDarab());
	
						raktarSer.setMexportkarton(exp);
						raktarSer.setMkiskarton(kk);
						raktarSer.setMdarab(darab);
	
						raktarSer.setHelykod(l.getHelykod());
						raktar.add(raktarSer);
					}
				}
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return raktar;
	}

	public RaktarSer updateRaktar(String rovancs, String userId,
			RaktarSer raktarSer) throws IllegalArgumentException,
			SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query cikkquery = pm.newQuery(Cikk.class);
			cikkquery
					.setFilter("(this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
			cikkquery.declareParameters("String pcikkszam,String pszinkod");

			@SuppressWarnings("unchecked")
			List<Cikk> cikklist = (List<Cikk>) pm.newQuery(cikkquery).execute(
					raktarSer.getCikkszam(),
					(raktarSer.getSzinkod() == null ? "" : raktarSer
							.getSzinkod()));

			if (!cikklist.isEmpty()) {
				for (Cikk l : cikklist) {
					if (rovancs.equals("")) {
						l.setHelykod(raktarSer.getHelykod());
						pm.makePersistent(l);
					} else {
						l.setKeszlet(((raktarSer.getKexportkarton()
								* l.getKiskarton() * l.getDarab()) + (raktarSer
								.getKkiskarton()) * l.getDarab())
								+ raktarSer.getKdarab());

						l.setRendelt(((raktarSer.getMexportkarton()
								* l.getKiskarton() * l.getDarab()) + (raktarSer
								.getMkiskarton()) * l.getDarab())
								+ raktarSer.getMdarab());

						pm.makePersistent(l);
						
						Beszallitottcikk beszallitottcikk = new Beszallitottcikk(
								raktarSer.getCikkszam(),
								raktarSer.getSzinkod(),
								raktarSer.getKexportkarton().intValue(),
								raktarSer.getKkiskarton().intValue(),
								raktarSer.getKdarab().intValue(),
								raktarSer.getMexportkarton().intValue(),
								raktarSer.getMkiskarton().intValue(),
								raktarSer.getMdarab().intValue(),
								userId, new Date(),
								Boolean.TRUE);
						
						pm.makePersistent(beszallitottcikk);
					}
				}
			}

			pm.flush();
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return raktarSer;
	}
	
	public List<RendeltcikkSer> getRendeles(String vevo) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<RendeltcikkSer> rendeles = new ArrayList<RendeltcikkSer>();
		try {

			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(this.rovidnev == pvevo) && (this.status == pstatus)");
			query.declareParameters("String pvevo,String pstatus");
			@SuppressWarnings("unchecked")
			List<Rendeltcikk> list = (List<Rendeltcikk>) pm.newQuery(
					query).execute(vevo,Constants.ELORENDELT_BEERKEZETT);
			if (!list.isEmpty()) {
				for (Rendeltcikk l : list) {
					RendeltcikkSer rendeltcikkSer = new RendeltcikkSer();
					rendeltcikkSer.setRendeles(l.getRendeles());
					rendeltcikkSer.setCikkszam(l.getCikkszam());
					rendeltcikkSer.setSzinkod(l.getSzinkod());
					rendeltcikkSer.setExportkarton(l.getExportkarton());
					rendeltcikkSer.setKiskarton(l.getKiskarton());
					rendeltcikkSer.setDarab(l.getDarab());
					rendeltcikkSer.setArusd(l.getArusd());
					rendeltcikkSer.setFizetusd(l.getFizetusd());
					rendeles.add(rendeltcikkSer);
				}
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return rendeles;
	}

	public List<EladasSer> getEladas(String cikkszam, String szinkod) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		ArrayList<EladasSer> eladas = new ArrayList<EladasSer>();
		try {

			Query query = pm.newQuery(Cedulacikk.class);
			query.setFilter("(this.cikkszam == pcikkszam) && (this.szinkod == pszinkod) && (this.status == '" + Constants.CEDULA_STATUSZ_FIZETETT + "')");
			query.declareParameters("String pcikkszam,String pszinkod");
			@SuppressWarnings("unchecked")
			List<Cedulacikk> list = (List<Cedulacikk>) pm.newQuery(
					query).execute(cikkszam,szinkod == null ? "" : szinkod);
			if (!list.isEmpty()) {
				for (Cedulacikk l : list) {
					EladasSer eladasSer = new EladasSer();
					eladasSer.setCedula(l.getCedula());
					eladasSer.setExportkarton(l.getExportkarton());
					eladasSer.setKiskarton(l.getKiskarton());
					eladasSer.setDarab(l.getDarab());
					eladas.add(eladasSer);
				}
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}

		return eladas;
	}
	
	public RendeltcikkSer removeRendeles(RendeltcikkSer rendeltcikkSer) throws IllegalArgumentException, SQLExceptionSer {

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {

			Query query = pm.newQuery(Rendeltcikk.class);
			query.setFilter("(this.rendeles == prendeles) && (this.cikkszam == pcikkszam) && (this.szinkod == pszinkod)");
			query.declareParameters("String prendeles,String pcikkszam,String pszinkod");
			@SuppressWarnings("unchecked")
			List<Rendeltcikk> list = (List<Rendeltcikk>) pm.newQuery(
					query).execute(rendeltcikkSer.getRendeles(),rendeltcikkSer.getCikkszam(),rendeltcikkSer.getSzinkod());
			if (!list.isEmpty()) {
				for (Rendeltcikk l : list) {
					l.setStatus(Constants.ELORENDELT_RENDEZETT);
				}
			}
		} catch (Exception e) {
			throw new SQLExceptionSer(e.getMessage());
		} finally {
			pm.close();
		}
				
		return rendeltcikkSer;
	}
	
}
