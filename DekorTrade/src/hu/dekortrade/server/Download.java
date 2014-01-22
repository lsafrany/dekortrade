package hu.dekortrade.server;

import hu.dekortrade.server.jdo.Kep;
import hu.dekortrade.server.jdo.PMF;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.Blob;

// http://127.0.0.1:8888/dekortrade/download?cikkszam=11CG0771C&sorszam=1

@SuppressWarnings("serial")
public class Download extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String cikkszam = request.getParameter("cikkszam");

		String sorszam = request.getParameter("sorszam");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
				
		try {
			Query query = pm.newQuery(Kep.class);
			query.setFilter("cikkszam == pcikkszam && sorszam == psorszam");
			query.declareParameters("String pcikkszam,String psorszam");
			@SuppressWarnings("unchecked")
			List<Kep> list = (List<Kep>) pm.newQuery(query)
					.execute(cikkszam,sorszam);

			if ((list != null) && (!list.isEmpty())) {
				Blob image = list.get(0).getBlob();
			    response.setContentType("image/jpeg");
			    response.getOutputStream().write(image.getBytes());	
			}
			    
		} catch (Exception e) {
			throw new ServletException();	
		} finally {
			pm.close();
		}		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);	
	}
}
