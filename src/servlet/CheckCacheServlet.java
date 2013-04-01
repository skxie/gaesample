package servlet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
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
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class CheckCacheServlet extends HttpServlet {

	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {  
        this.doPost(request, response);  
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
  	    //find whether the file exists in memecache first
  	    MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
  	    syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  	    res.setContentType("text/plain");
  	    String value = (String) syncCache.get(req.getParameter("filename"));
  	    if (value == null) {
  	    	//if not in memache, return no
  	    	res.getWriter().println("No such a file named " + req.getParameter("filename") + " in memcache.");
  	    } else {
  	    	// get file from memcache
  	    	res.getWriter().println("The file " + req.getParameter("filename") + " exists in memcache.");
  	    }
  	  }
}
