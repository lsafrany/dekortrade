// http://127.0.0.1:8888/dekortrade/upload

package hu.dekortrade.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@SuppressWarnings("serial")
public class Upload extends HttpServlet {
	
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
        throws ServletException, IOException {

    	FileItem file = null;	   
	    boolean isMultipart = ServletFileUpload.isMultipartContent(req);  
	    if (isMultipart) {    
	    	FileItemFactory factory = new DiskFileItemFactory();
	    	ServletFileUpload upload = new ServletFileUpload(factory);   
	    	try {
	    		
				@SuppressWarnings("rawtypes")
				Iterator i = upload.parseRequest(req).iterator();
	    		while (i.hasNext()) {		    		   
	    			FileItem f = (FileItem)i.next();
	    			if (!f.isFormField()) {
	    				file = f;
	    			}
	    		}		    		

	    		String fileName = file.getName();
	    		if (fileName.contains("/"))	fileName = fileName.substring(fileName.lastIndexOf("/")+1);
	    		else if (fileName.contains("\\")) fileName = fileName.substring(fileName.lastIndexOf("\\")+1);
    		
	    	} catch (FileUploadException e) {  
	    		System.out.println("FileUploadException" + e);            
	    	}		               
		    }		    
	
		res.setHeader("cache-control", "no-cache");
		
		PrintWriter out = res.getWriter();
		out.append("<html>");
		out.append("<head>");
		out.append("</head>");
		out.append("<body>");
		out.append("</body>");
		out.append("</html>");			
	        
    }
}

