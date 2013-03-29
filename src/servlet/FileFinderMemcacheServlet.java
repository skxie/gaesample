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
  	    	
  	    	String filename = req.getParameter("filename");
  	    	MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
  	    	syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
  	    	res.setContentType("text/plain");
  	    	String value = (String) syncCache.get(filename);
  	    	if (value == null) {
  	    		res.getWriter().println("Wrong");
  	    		
  	    		filename = "/gs/" + BUCKETNAME + "/" + filename;
  	    		FileService fileService = FileServiceFactory.getFileService();
  	       	    AppEngineFile readableFile = new AppEngineFile(filename);
  	       	    int fileSize = fileService.stat(readableFile).getLength().intValue();
  	       	    res.getWriter().println("The file size is" + fileSize);
	       	    FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
  	       	    // Again, different standard Java ways of reading from the channel.
  	       	    BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
  	       	    String line = new String();
  	       	    String content = new String();
  	       	    while ((line = reader.readLine()) != null) {
  	       	    	if (fileSize <= 102400)
  	       	    		content += line + "\n";
  	       	    	//res.getWriter().println(line);
  	       	    }
  	       	    
  	       	    res.getWriter().println("!" + content + "!");
  	       	    // line = "The woods are lovely, dark, and deep."
  	       	    readChannel.close();
  	       	    //res.getWriter().println("The file size is" + fileSize);
  	       	    
	       	    if (fileSize <= 102400) {
  	       	    	filename = req.getParameter("filename");
  	       	    	syncCache.put(filename, content);
  	       	    	String test = (String) syncCache.get(filename);
  	       	    	res.getWriter().println(filename);
  	       	    	res.getWriter().println(test);
  	       	    	res.getWriter().println(test.length());
  	       	    	res.getWriter().println(content.length());
  	       	    }
  	       	    
  	    	} else {
  	    		res.getWriter().println(syncCache.get(filename).getClass().getName());
  	    		res.getWriter().println(value.length());
  	    		res.getWriter().println(value);
  	    		/*
  	    		filename = "test";
  	             FileService fileService = FileServiceFactory.getFileService();
        	      GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
        	         .setBucket(BUCKETNAME)
        	         .setKey(filename)
        	         .setMimeType("text/html")
        	         .setAcl("public_read")
        	         .addUserMetadata("fileId", filename);
        	      AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
        	    // Open a channel to write to it
        	     boolean lock = true;
        	     FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lock);
        	     // Different standard Java ways of writing to the channel
        	     // are possible. Here we use a PrintWriter:
        	     PrintWriter out = new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
  	          // You now have the filename (item.getName() and the
  	          // contents (which you can read from stream). Here we just
  	          // print them back out to the servlet output stream, but you
  	          // will probably want to do something more interesting (for
  	          // example, wrap them in a Blob and commit them to the
  	          // datastore).
  	          int len;
  	          writeChannel.write(ByteBuffer.wrap(value.getBytes()));
  	          //byte[] buffer = new byte[1024];
  	          //while ((len = stream.read(buffer, 0, buffer.length)) != -1) {
  	            //res.getOutputStream().write(buffer, 0, len);
  	        	//out.print(buffer);
  	        	//  writeChannel.write(ByteBuffer.wrap(buffer));
  	          //}
  	          out.close();
  	          //String path = writableFile.getFullPath();
  	 	     // Write more to the file in a separate request:
  	 	     //writableFile = new AppEngineFile(path);
  	 	     // Lock the file because we intend to finalize it and
  	 	     // no one else should be able to edit it
  	 	     //lock = true;
  	 	     //writeChannel = fileService.openWriteChannel(writableFile, lock);
                writeChannel.closeFinally();
               // stream.close();
                * */
                
  	    	}
  	    	
  	    } catch (IOException ex) {
  	    	res.getWriter().println("No such a file named " + req.getParameter("filename"));
  	    	throw new IOException(ex);
  	    }
  	  }
}
