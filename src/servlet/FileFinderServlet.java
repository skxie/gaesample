package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;

public class FileFinderServlet extends HttpServlet {

	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {  
        this.doPost(request, response);  
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
  	    try {
  	    	
  	    	String filename = "/gs/" + BUCKETNAME + "/" +req.getParameter("filename");
  	    	res.setContentType("text/plain");
  	    	FileService fileService = FileServiceFactory.getFileService();
       	     AppEngineFile readableFile = new AppEngineFile(filename);
       	     FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
       	     // Again, different standard Java ways of reading from the channel.
       	     BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
       	     String line = new String();
       	     while ((line = reader.readLine()) != null)
       	    	 res.getWriter().println(line);

       	    // line = "The woods are lovely, dark, and deep."
       	     readChannel.close();
  	    } catch (IOException ex) {
  	    	res.getWriter().println("No such a file named " + req.getParameter("filename"));
  	    	//throw new ServletException(ex);
  	    }
  	  }
}
