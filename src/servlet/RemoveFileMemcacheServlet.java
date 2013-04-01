package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import functions.FileList;

public class RemoveFileMemcacheServlet extends HttpServlet {

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
    		String filename = req.getParameter("filename");
    		MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
  	    	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  	    	String value = (String) syncCache.get(filename);
  	    	if (value != null) {
  	    		// delete content of the file from memcache
  	    		syncCache.delete(filename);
  	    	}
  	    	filename = "/gs/" + BUCKETNAME + "/" + filename;
  	    	res.setContentType("text/plain");
  	    	 FileService fileService = FileServiceFactory.getFileService();
       	     AppEngineFile deletableFile = new AppEngineFile(filename);
       	     // delete content of the file
       	     fileService.delete(deletableFile);
       	     // remove the file name from the file list
       	     FileList flist = new FileList();
    	     flist.removeFile(req.getParameter("filename"));
    	     ArrayList<String> flistmem = flist.getFileList();
    	     syncCache.put("filelist", flistmem);
  	    } catch (IOException ex) {
  	    	res.getWriter().println("No such a file named " + req.getParameter("filename"));
  	    }
    }
}
