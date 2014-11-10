// http://127.0.0.1:8888/dekortrade/init

package hu.dekortrade.server;

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

			Sorszam elorendeltsorszam = new Sorszam("0",
					Constants.CEDULA_RENDELES);
			Sorszam zarassorszam = new Sorszam("0", Constants.CEDULA_ZARAS);
			Sorszam torlesztessorszam = new Sorszam("0", Constants.CEDULA_TORLESZTES);		
			Sorszam raktarsorszam = new Sorszam("0", Constants.CEDULA_RAKTAR);		
			pm.makePersistent(elorendeltsorszam);
			pm.makePersistent(zarassorszam);
			pm.makePersistent(torlesztessorszam);
			pm.makePersistent(raktarsorszam);
			
			out.append("<h1>Zaras</h1>");

			Query zarasQuery = pm.newQuery(Zaras.class);
			zarasQuery.deletePersistentAll();

			out.append("<h1>Zarasfizetes</h1>");

			Query zarasfizetesQuery = pm.newQuery(Zarasfizetes.class);
			zarasfizetesQuery.deletePersistentAll();

			out.append("<h1>Fizetes</h1>");

			Query fizetesQuery = pm.newQuery(Fizetes.class);
			fizetesQuery.deletePersistentAll();

			out.append("<h1>Cedula</h1>");

			Query cedulaQuery = pm.newQuery(Cedula.class);
			cedulaQuery.deletePersistentAll();

			out.append("<h1>Cedulacikk</h1>");

			Query cedulacikkQuery = pm.newQuery(Cedulacikk.class);
			cedulacikkQuery.deletePersistentAll();

			out.append("<h1>Kosár</h1>");

			Query kosarQuery = pm.newQuery(Kosar.class);
			kosarQuery.deletePersistentAll();

			out.append("<h1>Kosárcikk</h1>");

			Query kosarCikkQuery = pm.newQuery(Kosarcikk.class);
			kosarCikkQuery.deletePersistentAll();

			out.append("<h1>Gyártók</h1>");

			Query gyartoQuery = pm.newQuery(Gyarto.class);
			gyartoQuery.deletePersistentAll();

			Gyarto gyarto = new Gyarto("1", "CHAMP HANNA LIMITED",
					"WAH MOU FACTORY BUILDING 5TH FL.", "0085223061083", "",
					"", "", new Double(0), "1", "", Boolean.FALSE);
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
							fields[2], "", null, null, null, null, fields[3],
							null, new Double(0), new Double(0), new Double(0),
							new Double(0), new Double(2.3), new Double(0),
							new Double(0), new Double(0), new Double(0),
							new Double(0), new Double(fields[4]), new Double(0),
							new Double(0), new Integer(fields[5]), new Integer(
									fields[6]), new Double(fields[7]),
							new Double(0), new Double(fields[8]), new Double(
									fields[9]), null, null, Boolean.FALSE,
							"DB", 0, Boolean.FALSE, Boolean.FALSE,0L,0L,"");
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
					"Naszály Szilvia", Constants.MENU_RENDELES,
					Constants.INIT_PASSWORD, Boolean.FALSE);
			Felhasznalo felhasznalo2 = new Felhasznalo("Csabi", "Sántha Csaba",
					Constants.MENU_RENDSZER, Constants.INIT_PASSWORD,
					Boolean.FALSE);
			Felhasznalo felhasznalo3 = new Felhasznalo("Tamás",
					"Koperniczky Tamás", Constants.MENU_PENZTAR,
					Constants.INIT_PASSWORD, Boolean.FALSE);

			pm.makePersistent(felhasznalo1);
			pm.makePersistent(felhasznalo2);
			pm.makePersistent(felhasznalo3);

			Jog szilvijog1 = new Jog("Szilvi", Constants.MENU_RENDELES);
			Jog szilvijog2 = new Jog("Szilvi",
					Constants.MENU_RENDELES_ELORENDELES);

			Jog csabijog1 = new Jog("Csabi", Constants.MENU_RENDSZER);
			Jog csabijog2 = new Jog("Csabi",
					Constants.MENU_RENDSZER_FELHASZNALO);
			Jog csabijog3 = new Jog("Csabi", Constants.MENU_RENDSZER_SZINKRON);
			Jog csabijog4 = new Jog("Csabi", Constants.MENU_RENDSZER_KOSAR);

			Jog csabijog5 = new Jog("Csabi", Constants.MENU_TORZSADAT);
			Jog csabijog6 = new Jog("Csabi", Constants.MENU_TORZSADAT_GYARTO);
			Jog csabijog7 = new Jog("Csabi", Constants.MENU_TORZSADAT_VEVO);
			Jog csabijog8 = new Jog("Csabi", Constants.MENU_TORZSADAT_CIKKTIPUS);
			Jog csabijog9 = new Jog("Csabi", Constants.MENU_TORZSADAT_CIKKTORZS);

			Jog csabijog10 = new Jog("Csabi", Constants.MENU_RENDELES);
			Jog csabijog11 = new Jog("Csabi", Constants.MENU_RENDELES_INTERNET);
			Jog csabijog12 = new Jog("Csabi",
					Constants.MENU_RENDELES_ELORENDELES);
			Jog csabijog13 = new Jog("Csabi",
					Constants.MENU_RENDELES_VEGLEGESITES);
			Jog csabijog14 = new Jog("Csabi",
					Constants.MENU_RENDELES_MEGRENDELES);

			Jog csabijog15 = new Jog("Csabi", Constants.MENU_RAKTAR);
			Jog csabijog16 = new Jog("Csabi", Constants.MENU_RAKTAR_BESZALLITAS);
			Jog csabijog17 = new Jog("Csabi", Constants.MENU_RAKTAR_KESZLET);
			Jog csabijog18 = new Jog("Csabi", Constants.MENU_RAKTAR_KIADAS);

			Jog csabijog19 = new Jog("Csabi", Constants.MENU_ELADAS);

			Jog csabijog20 = new Jog("Csabi", Constants.MENU_LEKERDEZES);
			Jog csabijog21 = new Jog("Csabi", Constants.MENU_LEKERDEZES_CEDULAK);
			Jog csabijog22 = new Jog("Csabi",
					Constants.MENU_LEKERDEZES_TORLESZTESEK);
			Jog csabijog23 = new Jog("Csabi", Constants.MENU_LEKERDEZES_ZARASOK);
			Jog csabijog24 = new Jog("Csabi",
					Constants.MENU_LEKERDEZES_FORGALOM);

			Jog csabijog25 = new Jog("Csabi", Constants.MENU_PENZTAR);
			Jog csabijog26 = new Jog("Csabi", Constants.MENU_PENZTAR_FIZETES);
			Jog csabijog27 = new Jog("Csabi", Constants.MENU_PENZTAR_TORLESZTES);
			Jog csabijog28 = new Jog("Csabi", Constants.MENU_PENZTAR_HAZI);
			Jog csabijog29 = new Jog("Csabi", Constants.MENU_PENZTAR_ZARAS);

			Jog csabijog30 = new Jog("Csabi", Constants.JOG_ROVANCS);
			
			Jog tamasjog1 = new Jog("Tamás", Constants.MENU_PENZTAR);

			pm.makePersistent(szilvijog1);
			pm.makePersistent(szilvijog2);

			pm.makePersistent(csabijog1);
			pm.makePersistent(csabijog2);
			pm.makePersistent(csabijog3);
			pm.makePersistent(csabijog4);
			pm.makePersistent(csabijog5);
			pm.makePersistent(csabijog6);
			pm.makePersistent(csabijog7);
			pm.makePersistent(csabijog8);
			pm.makePersistent(csabijog9);
			pm.makePersistent(csabijog10);
			pm.makePersistent(csabijog11);
			pm.makePersistent(csabijog12);
			pm.makePersistent(csabijog13);
			pm.makePersistent(csabijog14);
			pm.makePersistent(csabijog15);
			pm.makePersistent(csabijog16);
			pm.makePersistent(csabijog17);
			pm.makePersistent(csabijog18);
			pm.makePersistent(csabijog19);
			pm.makePersistent(csabijog20);
			pm.makePersistent(csabijog21);
			pm.makePersistent(csabijog22);
			pm.makePersistent(csabijog23);
			pm.makePersistent(csabijog24);
			pm.makePersistent(csabijog25);
			pm.makePersistent(csabijog26);
			pm.makePersistent(csabijog27);
			pm.makePersistent(csabijog28);
			pm.makePersistent(csabijog29);
			pm.makePersistent(csabijog30);
			
			pm.makePersistent(tamasjog1);

			out.append("<h1>Vevők</h1>");

			Vevo vevo1 = new Vevo("Floradekor", "BELFOLDI", "Flora Dekor",
					"24413 Palics Nikola Tesla 7.", "", new Double(0),
					new Double(0), new Double(0), new Double(0), new Double(0), "",
					"", new Double(0), new Double(0), "hu", "", Boolean.TRUE,
					Boolean.FALSE, Boolean.FALSE);
			Vevo vevo2 = new Vevo("Floratrade", "EXPORT", "Flora Trade Kft",
					"", "", new Double(0), new Double(0), new Double(0),
					new Double(0), new Double(0), "", "", new Double(0),
					new Double(0), "hu", "", Boolean.TRUE, Boolean.FALSE,
					Boolean.FALSE);
			Vevo vevo3 = new Vevo("Berendi", "EXPORT", "S.C. Berendi S.R.L.",
					"Satu Mare ROMĆNIA", "", new Double(0), new Double(0),
					new Double(0), new Double(0), new Double(0), "", "",
					new Double(0), new Double(0), "hu", "", Boolean.FALSE,
					Boolean.FALSE, Boolean.FALSE);

			pm.makePersistent(vevo1);
			pm.makePersistent(vevo2);
			pm.makePersistent(vevo3);

			Query rendeltQuery = pm.newQuery(Rendelt.class);
			rendeltQuery.deletePersistentAll();

			Query rendeltcikkQuery = pm.newQuery(Rendeltcikk.class);
			rendeltcikkQuery.deletePersistentAll();

			Query beszallitottcikkQuery = pm.newQuery(Beszallitottcikk.class);
			beszallitottcikkQuery.deletePersistentAll();

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
