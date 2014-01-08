// http://127.0.0.1:8888/dekortrade99/init

package hu.dekortrade99.server;

import hu.dekortrade99.server.jdo.Ctorzs;
import hu.dekortrade99.server.jdo.PMF;
import hu.dekortrade99.server.jdo.Vevo;
import hu.dekortrade99.shared.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;

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

		int counter = 0;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {

			Query ctorzsQuery = pm.newQuery(Ctorzs.class);
			ctorzsQuery.deletePersistentAll();

			final InputStream inputStream = Init.class
					.getResourceAsStream("CTORZS.csv");
			final InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream,"UTF-8");
			final BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			while (bufferedReader.ready()) {
				String line = bufferedReader.readLine();
				String[] fields = line.split(";");
				fields[2] = fields[2].replaceAll(",", ".");
				fields[5] = fields[5].replaceAll(",", ".");
				fields[7] = fields[7].replaceAll(",", ".");
				fields[8] = fields[8].replaceAll(",", ".");
				Ctorzs ctorzs = new Ctorzs(fields[0], fields[1],
						new BigDecimal(fields[2]), new Integer(fields[3]),
						new Integer(fields[4]), new BigDecimal(fields[5]),
						fields[6], new BigDecimal(fields[7]), new BigDecimal(
								fields[8]), 0, Boolean.FALSE);
				pm.makePersistent(ctorzs);
				counter++;
			}

			out.append("<h1>" + counter + "</h1>");
			
			Query vevoQuery = pm.newQuery(Vevo.class);
			vevoQuery.deletePersistentAll();
	
			out.append("<h1>Floradekor</h1>");
			out.append("<h1>Floratrade</h1>");
			
			Vevo vevo1 = new Vevo("Floradekor",Constants.INIT_PASSWORD,"Flora Dekor",false);
			Vevo vevo2= new Vevo("Floratrade",Constants.INIT_PASSWORD,"Flora Trade Kft",false);
			pm.makePersistent(vevo1);
			pm.makePersistent(vevo2);
			
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
