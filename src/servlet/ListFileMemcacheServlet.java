package servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
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
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.apphosting.api.search.DocumentPb.Document.Storage;

import functions.FileList;

public class ListFileMemcacheServlet extends HttpServlet {

	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        this.doPost(request, response);  
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
  	    try {
  	    	// retrive file list from memcache
  	    	res.setContentType("text/plain");
  	    	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
  	    	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  	    	FileList fileList = new FileList();
  	    	ArrayList<String> filelist = new ArrayList<String>();
  	    	try {
    			ArrayList<String> value = (ArrayList<String>)syncCache.get("filelist");
    			if (value == null) {
    				//if file list does not exist in memcache, read it from disk and cache it
    				filelist = fileList.getFileList();
    				syncCache.put("filelist", filelist);
    			} else {
    				for (String st : value) {
   	       	    	 res.getWriter().println(st);
   	       	     	}
    			}
    		} catch (Exception e) {
    			filelist = fileList.getFileList();
    			syncCache.put("filelist", filelist);
    		}
  	    } catch (IOException ex) {
  	    	throw new IOException(ex);
  	    } catch (ClassCastException e) {
  	    	
  	    }
  	  }
}
