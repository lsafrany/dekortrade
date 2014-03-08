// http://127.0.0.1:8888/dekortrade/init

package hu.dekortrade.server;

import hu.dekortrade.shared.serialized.SzinkronSer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class Syncron extends HttpServlet {

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

		out.append("<h1>Syncron - start</h1>");
			
		SzinkronObject szinkronObject = new SzinkronObject();
		SzinkronSer szinkronSer = new SzinkronSer();
		try {
			szinkronSer = szinkronObject.feldolgozas();
			out.append("<p>Vevo : " + szinkronSer.getUploadvevo() + "</p>");	
			out.append("<p>Cikkfotipus : " + szinkronSer.getUploadcikkfotipus() + "</p>");
			out.append("<p>Cikkaltipus : " + szinkronSer.getUploadcikkaltipus() + "</p>");
			out.append("<p>Cikk : " + szinkronSer.getUploadcikk() + "</p>");
			out.append("<p>Kep : " + szinkronSer.getUploadkep() + "</p>");
			out.append("<p>Rendelt : " + szinkronSer.getDownloadrendelt() + "</p>");
		} catch (Exception e) {
			out.append("<h1>Syncron - error</h1>");
		}

		out.append("<h1>Syncron - end</h1>");

		out.append("</body>");
		out.append("</html>");

		out.println();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
