package hu.dekortrade.server;

import hu.dekortrade.server.jdo.Kep;
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
public class Upload extends HttpServlet {

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

				String cikkszam = request.getParameter("cikkszam");
				String szinkod = request.getParameter("szinkod");
				
				Query query = pm.newQuery(Kep.class);
				query.setFilter("(this.cikkszam == pcikkszam) && (this.szinkod == pszinkod) && (this.torolt == false)");
				query.declareParameters("String pcikkszam");
				@SuppressWarnings("unchecked")
				List<Kep> list = (List<Kep>) pm.newQuery(query).execute(
						cikkszam,szinkod);

				Kep kep = new Kep(cikkszam,szinkod,
						new Integer(list.size() + 1).toString(), imageBlob,
						Boolean.FALSE, Boolean.FALSE);
				pm.makePersistent(kep);

				session.removeAttribute(ServerConstants.FILE);
				session.removeAttribute(ServerConstants.FILE_ERROR);
			} else {
				session.removeAttribute(ServerConstants.FILE);
				session.setAttribute(ServerConstants.FILE_ERROR,
						Constants.FILE_SIZE_ERROR);
			}
			response.setContentType("text/plain");
			response.getOutputStream().write("OK!".getBytes());

		} catch (Exception e) {
			session.removeAttribute(ServerConstants.FILE);
			session.setAttribute(ServerConstants.FILE_ERROR,
					Constants.FILE_SAVE_ERROR);
			throw new ServletException();
		} finally {
			pm.close();
		}

	}
}