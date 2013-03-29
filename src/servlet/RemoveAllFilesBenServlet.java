package servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import functions.FileList;

public class RemoveAllFilesBenServlet extends HttpServlet {

	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        this.doPost(request, response);  
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    	try {
  	    	FileList fileList = new FileList();
  	    	ArrayList<String> filelist = fileList.getFileList();
  	    	double start = System.currentTimeMillis();
  	    	for (String filename : filelist) {
  	    		String fname = "/gs/" + BUCKETNAME + "/" + filename;
  	    		res.setContentType("text/plain");
  	    		FileService fileService = FileServiceFactory.getFileService();
  	    		AppEngineFile deletableFile = new AppEngineFile(fname);
  	    		fileService.delete(deletableFile);
  	    	}
  	    	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
  	    	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  	    	syncCache.delete("filelist");
  	    	double end = System.currentTimeMillis();
  	    	FileService fileService = FileServiceFactory.getFileService();
    	    GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
    	         .setBucket(BUCKETNAME)
    	         .setKey("removeAllFilesBen")
    	         .setMimeType("text/html")
    	         .setAcl("public_read")
    	         .addUserMetadata("fileId", "removeAllFilesBen");
    	    AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
    	    boolean lock = true;
    	    FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lock);
    	    double duration = end - start;
	        String result = "The execution time is " + duration + " ms.";
	        writeChannel.write(ByteBuffer.wrap(result.getBytes()));
	        writeChannel.closeFinally();
	        res.getWriter().println(result);
  	    	
  	    } catch (IOException ex) {
  	    	res.getWriter().println("No such a file");
  	    	//throw new ServletException(ex);
  	    }
    }
}
