// http://127.0.0.1:8888/dekortrade99/init

package hu.dekortrade99.server;

import hu.dekortrade99.server.jdo.Cikk;
import hu.dekortrade99.server.jdo.Kep;
import hu.dekortrade99.server.jdo.PMF;
import hu.dekortrade99.server.jdo.Rendelt;
import hu.dekortrade99.server.jdo.Rendeltcikk;
import hu.dekortrade99.server.jdo.Vevo;

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

			out.append("<h1>Cikk</h1>");
			
			Query cikkQuery = pm.newQuery(Cikk.class);
			cikkQuery.deletePersistentAll();

			out.append("<h1>Kep</h1>");
			
			Query kepQuery = pm.newQuery(Kep.class);
			kepQuery.deletePersistentAll();

			out.append("<h1>Vevo</h1>");
			
			Query vevoQuery = pm.newQuery(Vevo.class);
			vevoQuery.deletePersistentAll();

			out.append("<h1>Rendelt</h1>");
			
			Query rendeltQuery = pm.newQuery(Rendelt.class);
			rendeltQuery.deletePersistentAll();
						
			out.append("<h1>Rendeltcikk</h1>");
			
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
