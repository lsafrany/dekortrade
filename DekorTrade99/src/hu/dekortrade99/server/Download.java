package hu.dekortrade99.server;

import hu.dekortrade99.server.jdo.Cikkaltipus;
import hu.dekortrade99.server.jdo.Cikkfotipus;
import hu.dekortrade99.server.jdo.Kep;
import hu.dekortrade99.server.jdo.PMF;

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

		String cikkfotipus = request.getParameter("cikkfotipus");

		String cikkaltipus = request.getParameter("cikkaltipus");
		
		if (cikkszam != null) {
			String sorszam = request.getParameter("sorszam");
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
					
			try {
				Query query = pm.newQuery(Kep.class);
				query.setFilter("(this.cikkszam == pcikkszam) && (this.sorszam == psorszam)");
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
		
		if (cikkfotipus != null) {
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
					
			try {
				Query query = pm.newQuery(Cikkfotipus.class);
				query.setFilter("this.kod == pkod");
				query.declareParameters("String pkod");
				@SuppressWarnings("unchecked")
				List<Cikkfotipus> list = (List<Cikkfotipus>) pm.newQuery(query)
						.execute(cikkfotipus);
	
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
		
		if (cikkaltipus != null) {
			
			String kod = request.getParameter("kod");
			
			PersistenceManager pm = PMF.get().getPersistenceManager();
					
			try {
				Query query = pm.newQuery(Cikkaltipus.class);
				query.setFilter("(this.fokod == pfokod) && (this.kod == pkod)");
				query.declareParameters("String pfokod,String pkod");
				@SuppressWarnings("unchecked")
				List<Cikkaltipus> list = (List<Cikkaltipus>) pm.newQuery(query)
						.execute(cikkaltipus,kod);
	
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

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);	
	}
}
