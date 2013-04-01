package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Random;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;

import functions.FileFinderThreaded;
import functions.FileList;

public class FileFinderThreadedBenServlet extends HttpServlet {

	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        this.doPost(request, response);  
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    	
    	try {
  	    	FileList fileList = new FileList();
  	    	ArrayList<String> filelist = fileList.getFileList();
  	    	
  	    	Random random = new Random();
  	    	// get 4 random files need to be accessed
  	    	FileFinderThreaded[] ft = new FileFinderThreaded[4];
  	    	for (int i = 0; i < 4; i++) {
  	    		int pos = random.nextInt(411);
  	    		String filename = "/gs/" + BUCKETNAME + "/" + filelist.get(pos);
  	    		ft[i] = new FileFinderThreaded();
  	    		ft[i].setFilename(filename);
  	    	}
  	    	double start = System.currentTimeMillis();
  	    	// each thread file one file
  	    	Thread thread0 = ThreadManager.createThreadForCurrentRequest(ft[0]);
  	    	thread0.start();
  	    	Thread thread1 = ThreadManager.createThreadForCurrentRequest(ft[1]);
  	    	thread1.start();
  	    	Thread thread2 = ThreadManager.createThreadForCurrentRequest(ft[2]);
  	    	thread2.start();
  	    	Thread thread3 = ThreadManager.createThreadForCurrentRequest(ft[3]);
  	    	thread3.start();
  	    	thread0.join();
  	    	thread1.join();
  	    	thread2.join();
  	    	thread3.join();
  	    	double end = System.currentTimeMillis();
  	    	//record execution time
  	    	FileService fileService = FileServiceFactory.getFileService();
    	    GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
    	         .setBucket(BUCKETNAME)
    	         .setKey("fileFinderThreadedBen")
    	         .setMimeType("text/html")
    	         .setAcl("public_read")
    	         .addUserMetadata("fileId", "fileFinderThreadedBen");
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
