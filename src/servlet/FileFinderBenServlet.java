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

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;

import functions.FileList;

public class FileFinderBenServlet extends HttpServlet {

	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {  
        this.doPost(request, response);  
    }
    
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    	try {
    		//retrieve file list
  	    	FileList fileList = new FileList();
  	    	ArrayList<String> filelist = fileList.getFileList();
  	    	
  	    	Random random = new Random();
  	    	double start = System.currentTimeMillis();
  	    	for (int i = 0; i < 1; i++) {
  	    		int pos = random.nextInt(411);
  	    		String filename = "/gs/" + BUCKETNAME + "/" + filelist.get(pos);
  	  	    	FileService fileService = FileServiceFactory.getFileService();
  	       	    AppEngineFile readableFile = new AppEngineFile(filename);
  	    	    FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
  	       	    BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
  	       	    String line = new String();
  	       	    while ((line = reader.readLine()) != null)
  	       	    	 ;
  	       	    readChannel.close();
  	    	}
  	    	double end = System.currentTimeMillis();
  	    	//record execution time
  	    	FileService fileService = FileServiceFactory.getFileService();
    	    GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
    	         .setBucket(BUCKETNAME)
    	         .setKey("fileFinderBen")
    	         .setMimeType("text/html")
    	         .setAcl("public_read")
    	         .addUserMetadata("fileId", "fileFinderBen");
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
