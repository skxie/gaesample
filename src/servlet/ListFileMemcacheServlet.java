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
  	    	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
  	    	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  	    	FileList fileList = new FileList();
  	    	ArrayList<String> filelist = new ArrayList<String>();
  	    	try {
    			ArrayList<String> value = (ArrayList<String>)syncCache.get("filelist");
    			if (value == null) {
    				filelist = fileList.getFileList();
    				syncCache.put("filelist", filelist);
    			} else {
    				//filelist = value;
    				for (String st : value) {
   	       	    	 res.getWriter().println(st);
   	       	     	}
    			}
    		} catch (Exception e) {
    			filelist = fileList.getFileList();
    			syncCache.put("filelist", filelist);
    		}
  	    	//String value = (String) syncCache.get("filelist4");
  	    	/*
  	    	if (value == null) {
  	    		String keys = "/gs/" + BUCKETNAME + "/filelist";
  	  	    	res.setContentType("text/plain");
  	  	    	FileService fileService = FileServiceFactory.getFileService();
  	      	     AppEngineFile readableFile = new AppEngineFile(keys);
  	      	     FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
  	      	     // Again, different standard Java ways of reading from the channel.
  	      	     BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
  	      	     String line = new String();
  	      	     ArrayList<String> fileList = new ArrayList<String>();
  	      	     while ((line = reader.readLine()) != null) {
  	      	    	 //res.getWriter().println(line);
  	      	    	 fileList.add(line);
  	      	     }
  	      	     readChannel.close();
	       	     syncCache.put("filelist4", fileList);
	       	     /*
	       	     res.getWriter().println(fileList.size());
	       	     ArrayList<String> test = (ArrayList<String>) syncCache.get("filelist4");
	       	     fileService = FileServiceFactory.getFileService();
	       	     GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
      	         .setBucket(BUCKETNAME)
      	         .setKey("test")
      	         .setMimeType("text/html")
      	         .setAcl("public_read")
      	         .addUserMetadata("fileId", "test");
	       	     AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
      	    // Open a channel to write to it
	       	     boolean lock = true;
	       	     FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lock);
	       	     for (String st : test) {
	       	    	 writeChannel.write(ByteBuffer.wrap(st.getBytes()));
	       	     }
	       	     writeChannel.closeFinally();
	       	     //res.getWriter().println(filename);
	       	     //res.getWriter().println(test);
	       	     //res.getWriter().println(test.length());
	       	     //res.getWriter().println(content.length());
	       	      
	       	      
  	    	} else {
  	    		for (String st : value) {
	       	    	 res.getWriter().println(st);
	       	     }
  	    	}
  	    	* */
      	     
  	    } catch (IOException ex) {
  	    	//res.getWriter().println("No such a file named " + req.getParameter("filename"));
  	    	throw new IOException(ex);
  	    } catch (ClassCastException e) {
  	    	
  	    }
  	  }
}
