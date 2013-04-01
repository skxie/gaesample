package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.logging.Level;

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
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;

public class FileFinderMemcacheServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        this.doPost(request, response);  
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
  	    try {
  	    	//find file from memcache
  	    	String filename = req.getParameter("filename");
  	    	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
  	    	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  	    	res.setContentType("text/plain");
  	    	String value = (String) syncCache.get(filename);
  	    	if (value == null) {  	    	
  	    		//if the file does not exists in memcache
  	    		filename = "/gs/" + BUCKETNAME + "/" + filename;
  	    		FileService fileService = FileServiceFactory.getFileService();
  	       	    AppEngineFile readableFile = new AppEngineFile(filename);
  	       	    int fileSize = fileService.stat(readableFile).getLength().intValue();
	       	    FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
  	       	    BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
  	       	    String line = new String();
  	       	    String content = new String();
  	       	    while ((line = reader.readLine()) != null) {
  	       	    	if (fileSize <= 102400)
  	       	    		content += line + "\n";
  	       	    }
  	       	    res.getWriter().println(content);
  	       	    readChannel.close();  	       
  	       	    //if the file is smaller than 100KB, cache it in memcache
	       	    if (fileSize <= 102400) {
  	       	    	filename = req.getParameter("filename");
  	       	    	syncCache.put(filename, content);
//  	       	    	String test = (String) syncCache.get(filename);
//  	       	    	res.getWriter().println(filename);
//  	       	    	res.getWriter().println(test);
//  	       	    	res.getWriter().println(test.length());
//  	       	    	res.getWriter().println(content.length());
  	       	    }
  	       	    
  	    	} else {
  	    		//retrieve file content from memcache
  	    		res.getWriter().println(value);
  	    	}
  	    	
  	    } catch (IOException ex) {
  	    	res.getWriter().println("No such a file named " + req.getParameter("filename"));
  	    	throw new IOException(ex);
  	    }
  	  }
}
