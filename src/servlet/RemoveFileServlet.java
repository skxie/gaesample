package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;

public class RemoveFileServlet extends HttpServlet {

	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        this.doPost(request, response);  
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    	try {
  	    	
  	    	String filename = "/gs/" + BUCKETNAME + "/" +req.getParameter("filename");
  	    	res.setContentType("text/plain");
  	    	 FileService fileService = FileServiceFactory.getFileService();
       	     AppEngineFile deletableFile = new AppEngineFile(filename);
       	     fileService.delete(deletableFile);
  	    } catch (IOException ex) {
  	    	res.getWriter().println("No such a file named " + req.getParameter("filename"));
  	    	//throw new ServletException(ex);
  	    }
    }
}
