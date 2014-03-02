
package hu.dekortrade.server;

import hu.dekortrade.server.jdo.Cikkaltipus;
import hu.dekortrade.server.jdo.PMF;
import hu.dekortrade.shared.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import com.google.appengine.api.datastore.Blob;

@SuppressWarnings("serial")
public class Uploadalkod extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession();
	
		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			ServletFileUpload upload = new ServletFileUpload();
		    FileItemIterator iter = upload.getItemIterator(request);
		    FileItemStream imageItem = iter.next();
		    InputStream imgStream = imageItem.openStream();

		    // construct our entity objects
		    Blob imageBlob = new Blob(IOUtils.toByteArray(imgStream));
		    if (imageBlob.getBytes().length < 999999) {
		    
		    	String kod = request.getParameter("kod");		
		    	
				Query query = pm.newQuery(Cikkaltipus.class);
				query.setFilter("this.kod == pkod");
				query.declareParameters("String pkod");
				@SuppressWarnings("unchecked")
				List<Cikkaltipus> list = (List<Cikkaltipus>) pm.newQuery(query)
						.execute(kod);
								
				if (!list.isEmpty()) {
					for (Cikkaltipus l : list) {
						l.setBlob(imageBlob);
						l.setSzinkron(Boolean.FALSE);
					}
				}
				session.removeAttribute(ServerConstants.FILE);
				session.removeAttribute(ServerConstants.FILE_ERROR);		
		    }
		    else  {
				session.removeAttribute(ServerConstants.FILE);
				session.setAttribute(ServerConstants.FILE_ERROR,Constants.FILE_SIZE_ERROR);
		    }
		    response.setContentType("text/plain");
		    response.getOutputStream().write("OK!".getBytes());

		} catch (Exception e) {
			session.removeAttribute(ServerConstants.FILE);
			session.setAttribute(ServerConstants.FILE_ERROR,Constants.FILE_SAVE_ERROR);
			throw new ServletException();			
		} finally {
		    pm.close();
		}
		
	}
}