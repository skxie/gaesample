package servlet;

import java.io.BufferedReader;
import java.io.File;
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
import com.google.apphosting.api.search.DocumentPb.Document.Storage;

public class ListFileServlet extends HttpServlet {

	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        this.doPost(request, response);  
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
  	    try {
  	    	// retrieve file list
  	    	String keys = "/gs/" + BUCKETNAME + "/filelist";
  	    	res.setContentType("text/plain");
  	    	FileService fileService = FileServiceFactory.getFileService();
      	     AppEngineFile readableFile = new AppEngineFile(keys);
      	     FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
      	     BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
      	     String line = new String();
      	     //get each file name
      	     while ((line = reader.readLine()) != null) {
      	    	 res.getWriter().println(line);
      	     }
      	     readChannel.close();
  	    } catch (IOException ex) {
  	    	//res.getWriter().println("No such a file named " + req.getParameter("filename"));
  	    	throw new IOException(ex);
  	    }
  	  }
}
