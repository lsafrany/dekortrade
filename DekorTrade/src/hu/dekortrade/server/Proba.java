// http://127.0.0.1:8888/dekortrade/proba

package hu.dekortrade.server;

import hu.dekortrade.client.ClientConstants;
import hu.dekortrade.server.jdo.PMF;
import hu.dekortrade.server.jdo.Rendelt;
import hu.dekortrade.server.jdo.Rendeltcikk;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class Proba extends HttpServlet {

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

			out.append("<h1>Próba</h1>");
			
			Query rendeltQuery = pm.newQuery(Rendelt.class);
			rendeltQuery.deletePersistentAll();
			
			Query rendeltcikkQuery = pm.newQuery(Rendeltcikk.class);
			rendeltcikkQuery.deletePersistentAll();
		
			Rendeltcikk rendeltcikk1 = new Rendeltcikk("Floradekor", "111-111", "71726", "", new Integer(1),  new Integer(2), new Integer(3));
			pm.makePersistent(rendeltcikk1);

			Rendeltcikk rendeltcikk2 = new Rendeltcikk("Floradekor", "111-111", "ED00022", "", new Integer(11),  new Integer(22), new Integer(33));
			pm.makePersistent(rendeltcikk2);
	
			Rendelt rendelt = new Rendelt("Floradekor", "111-111", ClientConstants.INTERNET_ELORENDEL, new Date());
			pm.makePersistent(rendelt);	
			
			Rendeltcikk rendeltcikk3 = new Rendeltcikk("Floradekor", "222-222", "71726", "", new Integer(1),  new Integer(2), new Integer(3));
			pm.makePersistent(rendeltcikk3);

			Rendeltcikk rendeltcikk4 = new Rendeltcikk("Floradekor", "222-222", "ED00022", "", new Integer(11),  new Integer(22), new Integer(33));
			pm.makePersistent(rendeltcikk4);
			
			Rendelt rendelt2 = new Rendelt("Floradekor", "222-222", ClientConstants.INTERNET_ELORENDEL, new Date());
			pm.makePersistent(rendelt2);	
		
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
