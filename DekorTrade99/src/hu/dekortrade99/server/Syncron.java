// http://127.0.0.1:8888/dekortrade/syncron

package hu.dekortrade99.server;

import hu.dekortrade99.server.jdo.Cikk;
import hu.dekortrade99.server.jdo.Kep;
import hu.dekortrade99.server.jdo.PMF;
import hu.dekortrade99.server.jdo.Rendelt;
import hu.dekortrade99.server.jdo.Rendeltcikk;
import hu.dekortrade99.server.jdo.Vevo;
import hu.dekortrade99.server.sync.CikkSzinkron;
import hu.dekortrade99.server.sync.KepSzinkron;
import hu.dekortrade99.server.sync.RendeltSzinkron;
import hu.dekortrade99.server.sync.RendeltcikkSzinkron;
import hu.dekortrade99.server.sync.VevoSzinkron;
import hu.dekortrade99.shared.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

@SuppressWarnings("serial")
public class Syncron extends HttpServlet {
	
	private static final Logger log = Logger.getLogger(Syncron.class.getName()); 
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		String akcio = request.getParameter("akcio");

		if (akcio.equals("vevojelszo")) {
		
			log.info("vevojelszo");

			String rovidnev = request.getParameter("rovidnev");
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Query query = pm.newQuery(Vevo.class);
				query.setFilter("this.rovidnev == providnev");
				query.declareParameters("String providnev");
				@SuppressWarnings("unchecked")
				List<Vevo> list = (List<Vevo>) pm.newQuery(query).execute(
						rovidnev);
				if ((list != null) && (!list.isEmpty())) {
					for (Vevo l : list) {
						l.setJelszo(Constants.INIT_PASSWORD);
					}
				}
				pm.flush();
				out.println("vevojelszo - OK");
			} catch (Exception e) {
				log.info(e.getMessage());
				out.println("vevojelszo - ERROR");
			} finally {
				pm.close();
			}
		}

		if (akcio.equals("rendelt")) {
			
			log.info("rendelt");
			
			List<RendeltSzinkron> rendeltSzinkronList = new ArrayList<RendeltSzinkron>(); 
					
			PersistenceManager pm = PMF.get().getPersistenceManager();
//			Transaction tx = pm.currentTransaction();
//			tx.begin();
			try {

				Query query = pm.newQuery(Rendelt.class);
				query.setFilter("this.statusz == pstatusz");
				query.declareParameters("String pstatusz");
				query.setRange(0, 50);
				@SuppressWarnings("unchecked")
				List<Rendelt> list = (List<Rendelt>) pm.newQuery(query).execute(
						"PENDING");
				if ((list != null) && (!list.isEmpty())) {
					for (Rendelt l : list) {
					
						Query query1 = pm.newQuery(Rendeltcikk.class);
						query1.setFilter("(rovidnev == providnev) && (rendeles == prendeles)");
						query1.declareParameters("String providnev,String prendeles");
						@SuppressWarnings("unchecked")
						List<Rendeltcikk> list1 = (List<Rendeltcikk>) pm.newQuery(query1).execute(l.getRovidnev(),l.getRendeles());
						
						List<RendeltcikkSzinkron> rendeltcikkSzinkronList = new ArrayList<RendeltcikkSzinkron>(); 
						
						if ((list1 != null) && (!list1.isEmpty())) {
							for (Rendeltcikk l1 : list1) {
								RendeltcikkSzinkron rendeltcikkSzinkron = new RendeltcikkSzinkron(l1.getRovidnev(),l1.getRendeles(),l1.getCikkszam(),l1.getExportkarton());
								rendeltcikkSzinkronList.add(rendeltcikkSzinkron);
							}
						}
						l.setStatusz("PROCESSED");
						RendeltSzinkron	rendeltSzinkron = new RendeltSzinkron(l.getRovidnev(),l.getRendeles(),l.getDatum(),rendeltcikkSzinkronList);
						rendeltSzinkronList.add(rendeltSzinkron);
					}
				}
			
				ObjectMapper mapper = new ObjectMapper();
					
				out.println(mapper.writeValueAsString(rendeltSzinkronList));
//				tx.commit();
			} catch (Exception e) {
//				tx.rollback();
				log.info(e.getMessage());
				out.println("rendelt - ERROR");
			} finally {
				pm.close();
			}			
		}
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String tabla = request.getParameter("tabla");

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();

		if (tabla.equals("vevo")) {

			out.println("Vevo - START");
			
			log.info("vevo");
			
			// 1. get received JSON data from request
			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getInputStream(),"UTF-8"));
			String json = "";
			if (br != null) {
				json = br.readLine();
			}

			// 2. initiate jackson mapper
			ObjectMapper mapper = new ObjectMapper();

			// 3. Convert received JSON to Article
			List<VevoSzinkron> list = mapper.readValue(json,
					new TypeReference<List<VevoSzinkron>>() {
					});
			
			if ((list != null) && (!list.isEmpty())) {

				PersistenceManager pm = PMF.get().getPersistenceManager();
//				Transaction tx = pm.currentTransaction();
//				tx.begin();
				try {
					for (VevoSzinkron l : list) {

						Query query = pm.newQuery(Vevo.class);
						query.setFilter("this.rovidnev == providnev");
						query.declareParameters("String providnev");
						@SuppressWarnings("unchecked")
						List<Vevo> list1 = (List<Vevo>) pm.newQuery(query)
								.execute(l.getRovidnev());
						if (!list1.isEmpty()) {
							for (Vevo l1 : list1) {
								l1.setNev(l.getNev());
								l1.setTorolt(l.getTorolt());
							}
						} else {
							Vevo vevo = new Vevo(l.getRovidnev(),Constants.INIT_PASSWORD,l.getNev(),l.getTorolt());
							pm.makePersistent(vevo);
						}
					}
//					tx.commit();
				} catch (Exception e) {
//					tx.rollback();
					log.info(e.getMessage());
					out.println("Vevo - ERROR");
				} finally {
					pm.close();
				}

			}
			
			log.info("vevo - " + list.size());
			
			out.println("Vevo - OK");

		} else if (tabla.equals("cikk")) {
			
			out.println("Cikk - START");

			log.info("cikk");
						
			// 1. get received JSON data from request
			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getInputStream(),"UTF-8"));
			String json = "";
			if (br != null) {
				json = br.readLine();
			}

			// 2. initiate jackson mapper
			ObjectMapper mapper = new ObjectMapper();

			// 3. Convert received JSON to Article
			List<CikkSzinkron> list = mapper.readValue(json,
					new TypeReference<List<CikkSzinkron>>() {
					});
			
			if ((list != null) && (!list.isEmpty())) {

				PersistenceManager pm = PMF.get().getPersistenceManager();
//				Transaction tx = pm.currentTransaction();
//				tx.begin();
				try {
					for (CikkSzinkron l : list) {

						Query query = pm.newQuery(Cikk.class);
						query.setFilter("this.cikkszam == pcikkszam");
						query.declareParameters("String pcikkszam");
						@SuppressWarnings("unchecked")
						List<Cikk> list1 = (List<Cikk>) pm.newQuery(query)
								.execute(l.getCikkszam());
						if (!list1.isEmpty()) {
							for (Cikk l1 : list1) {
								l1.setMegnevezes(l.getMegnevezes());
								l1.setAr(l.getAr());
								l1.setKiskarton(l.getKiskarton());
								l1.setDarab(l.getDarab());
								l1.setTerfogat(l.getTerfogat());
								l1.setJel(l.getJel());
								l1.setBsuly(l.getBsuly());
								l1.setNsuly(l.getNsuly());
								l1.setKepek(l.getKepek());
								l1.setTorolt(l.getTorolt());
							}
						} else {

							Cikk cikk = new Cikk(l.getCikkszam(),
									l.getMegnevezes(),
									l.getAr(),
									l.getKiskarton(),
									l.getDarab(),
									l.getTerfogat(),
									l.getJel(),
									l.getBsuly(),
									l.getNsuly(),
									l.getKepek(),
									l.getTorolt());
							pm.makePersistent(cikk);			
						}
					}
//					tx.commit();
				} catch (Exception e) {
//					tx.rollback();
					log.info(e.getMessage());
					out.println("Cikk - ERROR");
				} finally {
					pm.close();
				}

			}

			log.info("cikk - " + list.size());
			
			out.println("Cikk - OK");

		} else if (tabla.equals("kep")) {

			out.println("Kep - START");

			log.info("kep");
			
			// 1. get received JSON data from request
			BufferedReader br = new BufferedReader(new InputStreamReader(
					request.getInputStream()));
			String json = "";
			if (br != null) {
				json = br.readLine();
			}

			// 2. initiate jackson mapper
			ObjectMapper mapper = new ObjectMapper();

			// 3. Convert received JSON to Article
			List<KepSzinkron> list = mapper.readValue(json,
					new TypeReference<List<KepSzinkron>>() {
					});
			
			if ((list != null) && (!list.isEmpty())) {

				PersistenceManager pm = PMF.get().getPersistenceManager();
//				Transaction tx = pm.currentTransaction();
//				tx.begin();
				try {
					for (KepSzinkron l : list) {

						Query query = pm.newQuery(Kep.class);
						query.setFilter("cikkszam == pcikkszam && sorszam == psorszam");
						query.declareParameters("String pcikkszam,String psorszam");
						@SuppressWarnings("unchecked")
						List<Kep> list1 = (List<Kep>) pm.newQuery(query)
								.execute(l.getCikkszam(), l.getSorszam());
						if (!list1.isEmpty()) {
							for (Kep l1 : list1) {
								l1.setBlob(l.getBlob());
								l1.setTorolt(l.getTorolt());
							}
						} else {
							Kep kep = new Kep(l.getCikkszam(),l.getSorszam(),l.getBlob(),l.getTorolt());
							pm.makePersistent(kep);
						}
					}
//					tx.commit();
				} catch (Exception e) {
//					tx.rollback();
					log.info(e.getMessage());
					out.println("Kep - ERROR");
				} finally {
					pm.close();
				}

			}

			log.info("kep - " + list.size());
			
			out.println("Kep - OK");

		}
	}
}