package servlet;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;

public class FileUploadBenServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;  
	public static final String BUCKETNAME = "myhomeworkdataset";
	  
    public void doGet(HttpServletRequest request, HttpServletResponse response)  
            throws IOException, ServletException {  
        this.doPost(request, response);  
    }  
  
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
    	    try {
    	      ServletFileUpload upload = new ServletFileUpload();
    	      res.setContentType("text/plain");

    	      FileItemIterator iterator = upload.getItemIterator(req);
    	      double start = System.currentTimeMillis();
    	      while (iterator.hasNext()) {
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
    	           while ((len = stream.read(buffer, 0, 1024)) != -1) {
    	             writeChannel.write(ByteBuffer.wrap(buffer, 0, len));
    	          }
    	          writeChannel.closeFinally();
                  stream.close();
    	        }
    	      }
    	      double end = System.currentTimeMillis();
    	      FileService fileService = FileServiceFactory.getFileService();
      	      GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
      	         .setBucket(BUCKETNAME)
      	         .setKey("fileUploadBen")
      	         .setMimeType("text/html")
      	         .setAcl("public_read")
      	         .addUserMetadata("fileId", "fileUploadBen");
      	      AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
      	      boolean lock = true;
      	      FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lock);
      	      double duration = end - start;
      	      String result = "The execution time is " + duration + " ms.";
      	      writeChannel.write(ByteBuffer.wrap(result.getBytes()));
      	      writeChannel.closeFinally();
      	      res.getWriter().println(result);
    	    } catch (Exception ex) {
    	      throw new IOException(ex);
    	    }
    	  }    
}
