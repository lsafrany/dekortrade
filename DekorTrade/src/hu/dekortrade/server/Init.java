// http://127.0.0.1:8888/dekortrade/init

package hu.dekortrade.server;

import hu.dekortrade.server.jdo.Cedula;
import hu.dekortrade.server.jdo.Cedulacikk;
import hu.dekortrade.server.jdo.Cikk;
import hu.dekortrade.server.jdo.Cikkaltipus;
import hu.dekortrade.server.jdo.Cikkfotipus;
import hu.dekortrade.server.jdo.Felhasznalo;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class Init extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.append("<html>");
		out.append("<head>");
		out.append("<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\">");
		out.append("<meta http-equiv=\"expires\" content=\"Mon, 22 Jul 2002 11:12:01 GMT\">");
		out.append("</head>");
		out.append("<body>");

		out.append("<h1>Init - start</h1>");

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			out.append("<h1>Sorszam</h1>");
			
			Query sorszamQuery = pm.newQuery(Sorszam.class);
			sorszamQuery.deletePersistentAll();

			Sorszam sorszam = new Sorszam ("0", Constants.CEDULA_STATUS_ELORENDELT);
			pm.makePersistent(sorszam);
			
			out.append("<h1>Cedula</h1>");
			
			Query cedulaQuery = pm.newQuery(Cedula.class);
			cedulaQuery.deletePersistentAll();

			out.append("<h1>Cedulacikk</h1>");
			
			Query cedulacikkQuery = pm.newQuery(Cedulacikk.class);
			cedulacikkQuery.deletePersistentAll();
			
			out.append("<h1>Kosár</h1>");
			
			Query kosarQuery = pm.newQuery(Kosar.class);
			kosarQuery.deletePersistentAll();

			out.append("<h1>KosárCikk</h1>");
			
			Query kosarCikkQuery = pm.newQuery(Kosarcikk.class);
			kosarCikkQuery.deletePersistentAll();

			out.append("<h1>Gyártók</h1>");

			Query gyartoQuery = pm.newQuery(Gyarto.class);
			gyartoQuery.deletePersistentAll();

			Gyarto gyarto = new Gyarto("1", "CHAMP HANNA LIMITED",
					"WAH MOU FACTORY BUILDING 5TH FL.", "0085223061083", "","","", new Float(0), "1", "",
					Boolean.FALSE);
			pm.makePersistent(gyarto);

			out.append("<h1>Cikk</h1>");

			int counter = 0;

			Query fotipusQuery = pm.newQuery(Cikkfotipus.class);
			fotipusQuery.deletePersistentAll();

			Cikkfotipus cikkfotipus1 = new Cikkfotipus("1", "Selyemvurágok",
					Boolean.FALSE);
			Cikkfotipus cikkfotipus2 = new Cikkfotipus("2", "Karácsonyi dekor",
					Boolean.FALSE);
			Cikkfotipus cikkfotipus3 = new Cikkfotipus("3",
					"Húsvéti/tavaszi dekor", Boolean.FALSE);

			pm.makePersistent(cikkfotipus1);
			pm.makePersistent(cikkfotipus2);
			pm.makePersistent(cikkfotipus3);

			Query altipusQuery = pm.newQuery(Cikkaltipus.class);
			altipusQuery.deletePersistentAll();

			Cikkaltipus cikkaltipus1 = new Cikkaltipus("1", "1", "Fejvirágok",
					Boolean.FALSE);
			Cikkaltipus cikkaltipus2 = new Cikkaltipus("1", "2", "Szálasok",
					Boolean.FALSE);
			Cikkaltipus cikkaltipus3 = new Cikkaltipus("1", "3", "Csokrok",
					Boolean.FALSE);
			Cikkaltipus cikkaltipus4 = new Cikkaltipus("1", "4", "Futók",
					Boolean.FALSE);
			Cikkaltipus cikkaltipus5 = new Cikkaltipus("1", "5", "Egyéb",
					Boolean.FALSE);

			pm.makePersistent(cikkaltipus1);
			pm.makePersistent(cikkaltipus2);
			pm.makePersistent(cikkaltipus3);
			pm.makePersistent(cikkaltipus4);
			pm.makePersistent(cikkaltipus5);

			Query cikkQuery = pm.newQuery(Cikk.class);
			cikkQuery.deletePersistentAll();

			final InputStream inputStream = Init.class
					.getResourceAsStream("CTORZS.csv");
			final InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream, "UTF-8");
			final BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			while (bufferedReader.ready()) {
				String line = bufferedReader.readLine();
				String[] fields = line.split(";");
				if (fields.length > 4) {
					fields[4] = fields[4].replaceAll(",", ".");
					fields[7] = fields[7].replaceAll(",", ".");
					fields[8] = fields[8].replaceAll(",", ".");
					fields[9] = fields[9].replaceAll(",", ".");
					Cikk cikk = new Cikk(fields[0], fields[1], "1", null, 
							fields[2], "", null, null, null, null, fields[3], null, 
							new Float(0), new Float(0), new Float(0), new Float(0), new Float(2.3), 
							new Float(0), new Float(0), new Float(0), new Float(0), new Float(0), 
						    new Float(fields[4]), new Float(0), new Float(0),
						    new Integer(fields[5]), new Integer(fields[6]),
							new Float(fields[7]), new Float(0), new Float(fields[8]),
							new Float(fields[9]), null, null, Boolean.FALSE, "DB", 0, Boolean.FALSE,
							Boolean.FALSE);
					pm.makePersistent(cikk);
					counter++;
				}
			}

			out.append("<h1>" + counter + "</h1>");

			Query kepQuery = pm.newQuery(Kep.class);
			kepQuery.deletePersistentAll();

			Query felhasznaloQuery = pm.newQuery(Felhasznalo.class);
			felhasznaloQuery.deletePersistentAll();

			Query jogQuery = pm.newQuery(Jog.class);
			jogQuery.deletePersistentAll();

			Query vevoQuery = pm.newQuery(Vevo.class);
			vevoQuery.deletePersistentAll();

			out.append("<h1>Felhasználók</h1>");

			Felhasznalo felhasznalo1 = new Felhasznalo("Szilvi",
					"Naszály Szilvia", Constants.MENU_ORDER,
					Constants.INIT_PASSWORD, Boolean.FALSE);
			Felhasznalo felhasznalo2 = new Felhasznalo("Csabi", "Sántha Csaba",
					Constants.MENU_SYSTEM, Constants.INIT_PASSWORD,
					Boolean.FALSE);
			Felhasznalo felhasznalo3 = new Felhasznalo("Tamás",
					"Koperniczky Tamás", Constants.MENU_BASEDATA,
					Constants.INIT_PASSWORD, Boolean.FALSE);

			pm.makePersistent(felhasznalo1);
			pm.makePersistent(felhasznalo2);
			pm.makePersistent(felhasznalo3);

			Jog jog1 = new Jog("Szilvi", Constants.MENU_ORDER);

			Jog jog2 = new Jog("Csabi", Constants.MENU_SYSTEM);
			Jog jog3 = new Jog("Csabi", Constants.MENU_BASEDATA);
			Jog jog4 = new Jog("Csabi", Constants.MENU_ORDER);
			Jog jog5 = new Jog("Csabi", Constants.MENU_QUERY);
			Jog jog6 = new Jog("Csabi", Constants.MENU_CASH);
			
			Jog jog7 = new Jog("Tamás", Constants.MENU_BASEDATA);

			pm.makePersistent(jog1);

			pm.makePersistent(jog2);
			pm.makePersistent(jog3);
			pm.makePersistent(jog4);
			pm.makePersistent(jog5);
			pm.makePersistent(jog6);

			pm.makePersistent(jog7);

			out.append("<h1>Vevők</h1>");

			Vevo vevo1 = new Vevo("Floradekor", "BELFOLDI", "Flora Dekor",
					"24413 Palics Nikola Tesla 7.", "", 
					new Float(0),new Float(0),new Float(0),
					new Float(0),new Float(0),
					"","",
					new Float(0),new Float(0),
					"hu","",
					Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
			Vevo vevo2 = new Vevo("Floratrade", "EXPORT", "Flora Trade Kft", "", "",
					new Float(0),new Float(0),new Float(0),
					new Float(0),new Float(0),
					"","",
					new Float(0),new Float(0),
					"hu","",					
					Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
			Vevo vevo3 = new Vevo("Berendi", "EXPORT", "S.C. Berendi S.R.L.", 
					"Satu Mare ROMĆNIA", "", 
					new Float(0),new Float(0),new Float(0),
					new Float(0),new Float(0),
					"","",
					new Float(0),new Float(0),
					"hu","",					
					Boolean.FALSE, Boolean.FALSE,Boolean.FALSE);

			pm.makePersistent(vevo1);
			pm.makePersistent(vevo2);
			pm.makePersistent(vevo3);

			Query rendeltQuery = pm.newQuery(Rendelt.class);
			rendeltQuery.deletePersistentAll();

			Query rendeltcikkQuery = pm.newQuery(Rendeltcikk.class);
			rendeltcikkQuery.deletePersistentAll();

		} finally {
			pm.close();
		}

		out.append("<h1>Init - end</h1>");

		out.append("</body>");
		out.append("</html>");

		out.println();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
