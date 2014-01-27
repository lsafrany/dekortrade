// http://127.0.0.1:8888/dekortrade/init

package hu.dekortrade.server;

import hu.dekortrade.server.jdo.Cikk;
import hu.dekortrade.server.jdo.Felhasznalo;
import hu.dekortrade.server.jdo.Jog;
import hu.dekortrade.server.jdo.PMF;
import hu.dekortrade.server.jdo.Rendelt;
import hu.dekortrade.server.jdo.Rendeltcikk;
import hu.dekortrade.server.jdo.Szallito;
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

			int counter = 0;

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
				fields[2] = fields[2].replaceAll(",", ".");
				fields[5] = fields[5].replaceAll(",", ".");
				fields[7] = fields[7].replaceAll(",", ".");
				fields[8] = fields[8].replaceAll(",", ".");
				Cikk cikk = new Cikk(fields[0], fields[1],
						new Double(fields[2]), new Integer(fields[3]),
						new Integer(fields[4]), new Double(fields[5]),
						fields[6], new Double(fields[7]), new Double(
								fields[8]), 0, Boolean.FALSE, Boolean.FALSE);
				pm.makePersistent(cikk);
				counter++;
			}

			out.append("<h1>" + counter + "</h1>");

			Query kepQuery = pm.newQuery(Kep.class);
			kepQuery.deletePersistentAll();

			Query felhasznaloQuery = pm.newQuery(Felhasznalo.class);
			felhasznaloQuery.deletePersistentAll();

			Query jogQuery = pm.newQuery(Jog.class);
			jogQuery.deletePersistentAll();

			Query szallitoQuery = pm.newQuery(Szallito.class);
			szallitoQuery.deletePersistentAll();

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

			Jog jog5 = new Jog("Tamás", Constants.MENU_BASEDATA);

			pm.makePersistent(jog1);

			pm.makePersistent(jog2);
			pm.makePersistent(jog3);
			pm.makePersistent(jog4);

			pm.makePersistent(jog5);

			out.append("<h1>Szállítók</h1>");

			Szallito szallito = new Szallito("1", "CHAMP HANNA LIMITED",
					"WAH MOU FACTORY BUILDING 5TH FL.", "0085223061083",
					Boolean.FALSE);
			pm.makePersistent(szallito);

			out.append("<h1>Vevők</h1>");

			Vevo vevo1 = new Vevo("Floradekor", "Flora Dekor",
					"24413 Palics Nikola Tesla 7.", "", Boolean.TRUE,
					Boolean.FALSE, Boolean.FALSE);
			Vevo vevo2 = new Vevo("Floratrade", "Flora Trade Kft", "", "",
					Boolean.TRUE, Boolean.FALSE, Boolean.FALSE);
			Vevo vevo3 = new Vevo("Berendi", "S.C. Berendi S.R.L.",
					"Satu Mare ROMĆNIA", "", Boolean.FALSE, Boolean.FALSE,
					Boolean.FALSE);

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
