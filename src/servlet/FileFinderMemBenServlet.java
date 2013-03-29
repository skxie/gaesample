package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;

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

import functions.FileList;

public class FileFinderMemBenServlet extends HttpServlet {

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
    			} else {
    				filelist = value;
    			}
    		} catch (Exception e) {
    			filelist = fileList.getFileList();
    		}
    		
  	    	Random random = new Random();
  	    	double start = System.currentTimeMillis();
  	    	for (int i = 0; i < 10; i++) {
  	    		int pos = random.nextInt(5);
  	    		String filename = filelist.get(pos);
  	    		String value = (String)syncCache.get(filename);
  	    		if (value == null) {
  	    			filename = "/gs/" + BUCKETNAME + "/" + filename;
  	  	  	    	FileService fileService = FileServiceFactory.getFileService();
  	  	       	    AppEngineFile readableFile = new AppEngineFile(filename);
  	  	    	    FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
  	  	       	    BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
  	  	       	    String line = new String();
  	  	       	    String content = new String();
  	  	       	    while ((line = reader.readLine()) != null)
  	  	       	    	 content += line + "\n";
  	  	       	    readChannel.close();
  	  	       	    syncCache.put(filelist.get(pos), content);
  	    		} else {
  	    			String content = value;
  	    		}
  	    	}
  	    	double end = System.currentTimeMillis();
  	    	FileService fileService = FileServiceFactory.getFileService();
    	    GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
    	         .setBucket(BUCKETNAME)
    	         .setKey("fileFinderMemBen")
    	         .setMimeType("text/html")
    	         .setAcl("public_read")
    	         .addUserMetadata("fileId", "fileFinderMemBen");
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
  	    }
    }
}
