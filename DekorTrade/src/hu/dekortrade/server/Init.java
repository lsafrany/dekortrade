// http://127.0.0.1:8888/dekortrade/init

package hu.dekortrade.server;

import hu.dekortrade.server.jdo.Felhasznalo;
import hu.dekortrade.server.jdo.PMF;
import hu.dekortrade.server.jdo.Szallito;
import hu.dekortrade.server.jdo.Vevo;
import hu.dekortrade.shared.Constants;

import java.io.IOException;
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
			
			Query felhasznaloQuery = pm.newQuery(Felhasznalo.class);
			felhasznaloQuery.deletePersistentAll();

			Query szallitoQuery = pm.newQuery(Szallito.class);
			szallitoQuery.deletePersistentAll();

			Query vevoQuery = pm.newQuery(Vevo.class);
			vevoQuery.deletePersistentAll();

			out.append("<h1>Felhasználók</h1>");
			
			Felhasznalo felhasznalo1 = new Felhasznalo("Szilvi","Naszály Szilvia",Constants.INIT_PASSWORD,Boolean.FALSE);
			Felhasznalo felhasznalo2 = new Felhasznalo("Csabi","Sántha Csaba",Constants.INIT_PASSWORD,Boolean.FALSE);
			Felhasznalo felhasznalo3 = new Felhasznalo("Tamás","Koperniczky Tamás",Constants.INIT_PASSWORD,Boolean.FALSE);
			
			pm.makePersistent(felhasznalo1);
			pm.makePersistent(felhasznalo2);
			pm.makePersistent(felhasznalo3);
			
			out.append("<h1>Szállítók</h1>");
			
			Szallito szallito = new Szallito("1","CHAMP HANNA LIMITED","WAH MOU FACTORY BUILDING 5TH FL.","0085223061083",Boolean.FALSE);												
			pm.makePersistent(szallito);			
			
			out.append("<h1>Vevők</h1>");
			
			Vevo vevo = new Vevo("Berendi","S.C. Berendi S.R.L.","Satu Mare ROMĆNIA","",Boolean.FALSE,Boolean.TRUE,Boolean.FALSE);												
			pm.makePersistent(vevo);			
			
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
