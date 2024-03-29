package servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.fileupload.FileItem;
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

public class FileUploadMemThreadedBenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;  
	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws IOException, ServletException {  
        this.doPost(request, response);  
    }  
  
    public void doPost(HttpServletRequest req, HttpServletResponse res)
    	      throws ServletException, IOException {
    	    try {
    	      ServletFileUpload upload = new ServletFileUpload();
    	      res.setContentType("text/plain");

    	      FileItemIterator iterator = upload.getItemIterator(req);
    	      double start = System.currentTimeMillis();
    	      
    	      while (iterator.hasNext()) {
    	    	  //get the file need to be uploaded
    	        FileItemStream item = iterator.next();
    	        InputStream stream = item.openStream();
    	        if (item.isFormField()) {
    	          
    	        } else {
    	          
    	          String filename = item.getName();
    	          FileService fileService = FileServiceFactory.getFileService();
          	      GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
          	         .setBucket(BUCKETNAME)
          	         .setKey(filename)
          	         .setMimeType("text/html")
          	         .setAcl("public_read")
          	         .addUserMetadata("fileId", filename);
          	      AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
          	      boolean lock = true;
          	      FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lock);
          	      int len;
    	          byte[] buffer = new byte[1024];
    	          // write files from loal to Google Cloud Storage
    	          while ((len = stream.read(buffer, 0, 1024)) != -1) {
    	            writeChannel.write(ByteBuffer.wrap(buffer, 0, len));
    	          }
    	          writeChannel.closeFinally();
                  stream.close();
    	        }
    	      }
    	      double end = System.currentTimeMillis();
      	      double duration = end - start;
    	      String result = "The start time is " + start + " the end time is " + end + " he execution time is " + duration + " ms.";
    	      res.getWriter().println(result);
    	    } catch (Exception ex) {
    	      throw new ServletException(ex);
    	    }
    	  }    
}
