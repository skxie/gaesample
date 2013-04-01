package servlet;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import functions.FileList;
import functions.RemoveAllFilesThreaded;

public class RemoveAllFilesMemThreadedBenServlet extends HttpServlet {

	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        this.doPost(request, response);  
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    	try {
    		// retrieve the file list from memcache
  	    	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
  	    	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  	    	FileList fileList = new FileList();
    		ArrayList<String> filelist = new ArrayList<String>();
    		double start = System.currentTimeMillis();
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
    		// split the flie list into 4 sub list
  	    	List<String> filelist0 = filelist.subList(0, 100);
  	    	List<String> filelist1 = filelist.subList(100, 200);
  	    	List<String> filelist2 = filelist.subList(200, 300);
  	    	List<String> filelist3 = filelist.subList(300, 411);
  	    	// each thread remove files in one sub file list
  	    	RemoveAllFilesThreaded[] rft = new RemoveAllFilesThreaded[4];
  	    	rft[0] = new RemoveAllFilesThreaded();
  	    	rft[0].setFilename(filelist0);
  	    	rft[1] = new RemoveAllFilesThreaded();
  	    	rft[1].setFilename(filelist1);
  	    	rft[2] = new RemoveAllFilesThreaded();
  	    	rft[2].setFilename(filelist2);
  	    	rft[3] = new RemoveAllFilesThreaded();
  	    	rft[3].setFilename(filelist3);
  	    	Thread thread0 = ThreadManager.createThreadForCurrentRequest(rft[0]);
  	    	thread0.start();
  	    	Thread thread1 = ThreadManager.createThreadForCurrentRequest(rft[1]);
  	    	thread1.start();
  	    	Thread thread2 = ThreadManager.createThreadForCurrentRequest(rft[2]);
  	    	thread2.start();
  	    	Thread thread3 = ThreadManager.createThreadForCurrentRequest(rft[3]);
  	    	thread3.start();
  	    	thread0.join();
  	    	thread1.join();
  	    	thread2.join();
  	    	thread3.join();
  	    	// delete file list from memcache
  	    	syncCache = MemcacheServiceFactory.getMemcacheService();
  	    	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  	    	syncCache.delete("filelist");
  	    	double end = System.currentTimeMillis();
  	    	// record the experimental results
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
  	    } catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
}
