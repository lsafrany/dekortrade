// http://127.0.0.1:8888/dekortrade/syncron

package hu.dekortrade99.server;

import hu.dekortrade99.server.jdo.Cikk;
import hu.dekortrade99.server.jdo.Cikkaltipus;
import hu.dekortrade99.server.jdo.Cikkfotipus;
import hu.dekortrade99.server.jdo.Kep;
import hu.dekortrade99.server.jdo.PMF;
import hu.dekortrade99.server.jdo.Rendelt;
import hu.dekortrade99.server.jdo.Rendeltcikk;
import hu.dekortrade99.server.jdo.Vevo;
import hu.dekortrade99.server.sync.CikkSzinkron;
import hu.dekortrade99.server.sync.CikkaltipusSzinkron;
import hu.dekortrade99.server.sync.CikkfotipusSzinkron;
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
		
		if (akcio.equals("szinkron")) {
			
			log.info("szinkron");
								
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {

				Query vevoQuery = pm.newQuery(Vevo.class);
				vevoQuery.deletePersistentAll();
				
				Query cikkfotipusQuery = pm.newQuery(Cikkfotipus.class);
				cikkfotipusQuery.deletePersistentAll();

				Query cikkaltipusQuery = pm.newQuery(Cikkaltipus.class);
				cikkaltipusQuery.deletePersistentAll();

				Query cikkQuery = pm.newQuery(Cikk.class);
				cikkQuery.deletePersistentAll();
				
				Query kepQuery = pm.newQuery(Kep.class);
				kepQuery.deletePersistentAll();
								
				Query query = pm.newQuery(Rendelt.class);
				@SuppressWarnings("unchecked")
				List<Rendelt> list = (List<Rendelt>) pm.newQuery(query).execute();
				if ((list != null) && (!list.isEmpty())) {
					for (Rendelt l : list) {
						l.setStatusz("PENDING");
					}
				}
					
				out.println("szinkron - OK");
			} catch (Exception e) {
				log.info(e.getMessage());
				out.println("szinkron - ERROR");
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
								l1.setInternet(l.getInternet());
							}
						} else {
							Vevo vevo = new Vevo(l.getRovidnev(),Constants.INIT_PASSWORD,l.getNev(),l.getInternet(),l.getTorolt());
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

		} else if (tabla.equals("cikkfotipus")) {
			
			out.println("Cikkfotipus - START");

			log.info("cikkfotipus");
						
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
			List<CikkfotipusSzinkron> list = mapper.readValue(json,
					new TypeReference<List<CikkfotipusSzinkron>>() {
					});
			
			if ((list != null) && (!list.isEmpty())) {

				PersistenceManager pm = PMF.get().getPersistenceManager();
//				Transaction tx = pm.currentTransaction();
//				tx.begin();
				try {
					for (CikkfotipusSzinkron l : list) {

						Query query = pm.newQuery(Cikkfotipus.class);
						query.setFilter("this.kod == pkod");
						query.declareParameters("String pkod");
						@SuppressWarnings("unchecked")
						List<Cikkfotipus> list1 = (List<Cikkfotipus>) pm.newQuery(query)
								.execute(l.getKod());
						if (!list1.isEmpty()) {
							for (Cikkfotipus l1 : list1) {
								l1.setNev(l.getNev());
								l1.setBlob(l.getBlob());
							}
						} else {

							Cikkfotipus cikkfotipus = new Cikkfotipus(
									l.getKod(),
									l.getNev(),
									l.getBlob());
							pm.makePersistent(cikkfotipus);			
						}
					}
//					tx.commit();
				} catch (Exception e) {
//					tx.rollback();
					log.info(e.getMessage());
					out.println("Cikkfotipus - ERROR");
				} finally {
					pm.close();
				}

			}

			log.info("cikkfotipus - " + list.size());
			
			out.println("Cikkfotips - OK");

		} else if (tabla.equals("cikkaltipus")) {
			
			out.println("Cikkaltipus - START");

			log.info("cikkaltipus");
						
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
			List<CikkaltipusSzinkron> list = mapper.readValue(json,
					new TypeReference<List<CikkaltipusSzinkron>>() {
					});
			
			if ((list != null) && (!list.isEmpty())) {

				PersistenceManager pm = PMF.get().getPersistenceManager();
//				Transaction tx = pm.currentTransaction();
//				tx.begin();
				try {
					for (CikkaltipusSzinkron l : list) {

						Query query = pm.newQuery(Cikkaltipus.class);
						query.setFilter("fokod == pfokod && kod == pkod");
						query.declareParameters("String pfokod,String pkod");
						@SuppressWarnings("unchecked")
						List<Cikkaltipus> list1 = (List<Cikkaltipus>) pm.newQuery(query)
								.execute(l.getFokod(),l.getKod());
						if (!list1.isEmpty()) {
							for (Cikkaltipus l1 : list1) {
								l1.setNev(l.getNev());
								l1.setBlob(l.getBlob());
							}
						} else {

							Cikkaltipus cikkaltipus = new Cikkaltipus(
									l.getFokod(),
									l.getKod(),
									l.getNev(),
									l.getBlob());
							pm.makePersistent(cikkaltipus);			
						}
					}
//					tx.commit();
				} catch (Exception e) {
//					tx.rollback();
					log.info(e.getMessage());
					out.println("Cikkaltipus - ERROR");
				} finally {
					pm.close();
				}

			}

			log.info("cikkaltipus - " + list.size());
			
			out.println("Cikkaltips - OK");

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
								l1.setFotipus(l.getFotipus());
								l1.setAltipus(l.getAltipus());
								l1.setMegnevezes(l.getMegnevezes());
								l1.setAr(l.getAr());
								l1.setKiskarton(l.getKiskarton());
								l1.setDarab(l.getDarab());
								l1.setTerfogat(l.getTerfogat());
								l1.setBsuly(l.getBsuly());
								l1.setNsuly(l.getNsuly());
								l1.setKepek(l.getKepek());
								l1.setTorolt(l.getTorolt());
							}
						} else {

							Cikk cikk = new Cikk(
									l.getFotipus(),
									l.getAltipus(),
									l.getCikkszam(),
									l.getMegnevezes(),
									l.getAr(),
									l.getKiskarton(),
									l.getDarab(),
									l.getTerfogat(),
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