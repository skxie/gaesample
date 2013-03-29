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

public class FileUploadServlet extends HttpServlet {
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
    	      while (iterator.hasNext()) {
    	        FileItemStream item = iterator.next();
    	        InputStream stream = item.openStream();
    	        //BufferedInputStream bfstream = new BufferedInputStream(item.openStream());
    	        if (item.isFormField()) {
    	          //log.warning("Got a form field: " + item.getFieldName());
    	        } else {
    	          //log.warning("Got an uploaded file: " + item.getFieldName() +
    	          //            ", name = " + item.getName());
    	          
    	          String filename = item.getName();
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
          	     //PrintWriter out = new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
          	     
    	          // You now have the filename (item.getName() and the
    	          // contents (which you can read from stream). Here we just
    	          // print them back out to the servlet output stream, but you
    	          // will probably want to do something more interesting (for
    	          // example, wrap them in a Blob and commit them to the
    	          // datastore).
    	          int len;
    	          byte[] buffer = new byte[1024];
    	          //len = bfstream.read(buffer);
    	          //res.getWriter().println(buffer);
    	          //while (len > 0) {
    	          //while ((len = bfstream.read(buffer)) > 0) {
    	          while ((len = stream.read(buffer, 0, 1024)) != -1) {
    	            //res.getOutputStream().write(buffer, 0, len);
    	        	//out.print(buffer);
    	        	  writeChannel.write(ByteBuffer.wrap(buffer, 0, len));
    	        	  //writeChannel.write(ByteBuffer.wrap(info));
    	        	  //res.getOutputStream().write(buffer, 0, len);
    	        	  //res.getOutputStream().write(info, 0, info.length);
    	        	  //res.getWriter().println("-----one buffer -----");
    	        	  //len = bfstream.read(buffer);
    	        	  //buffer = new byte[1024];
    	          }
    	          //out.close();
    	          //String path = writableFile.getFullPath();
    	 	     // Write more to the file in a separate request:
    	 	     //writableFile = new AppEngineFile(path);
    	 	     // Lock the file because we intend to finalize it and
    	 	     // no one else should be able to edit it
    	 	     //lock = true;
    	 	     //writeChannel = fileService.openWriteChannel(writableFile, lock);
                  writeChannel.closeFinally();
                  stream.close();
                  /*
                 filename = "/gs/" + BUCKETNAME + "/" + filename;
         	     AppEngineFile readableFile = new AppEngineFile(filename);
         	     FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
         	     // Again, different standard Java ways of reading from the channel.
         	     BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
         	     String line = new String();
         	     while ((line = reader.readLine()) != null)
         	    	 res.getWriter().println(line);

         	    // line = "The woods are lovely, dark, and deep."
         	     readChannel.close();
         	     */
                  res.getWriter().println(1);
    	        }
    	      }
    	    } catch (Exception ex) {
    	      throw new ServletException(ex);
    	    }
    	  }
    
    /*
    @SuppressWarnings("unchecked")  
    public void doPost(HttpServletRequest request, HttpServletResponse response)  
            throws IOException, ServletException {  
        // 采用apache工具包进行文件上传操作  
        DiskFileItemFactory factory = new DiskFileItemFactory();  
        ServletFileUpload upload = new ServletFileUpload(factory);  
        try {  
            List<FileItem> fileitems = upload.parseRequest(request);  
            for (FileItem item : fileitems) {  
                if (item.isFormField()) {  
                    String name = item.getFieldName();  
                    String value = item.getString();  
                      
                    // 转换下字符集编码  
                    value = new String(value.getBytes("iso-8859-1"), "utf-8");  
                    System.out.println(name + "=" + value);  
                } else {
                	response.setContentType("text/plain");
                    String filename = item.getName();  
                    //System.out.println(filename);  
                    FileService fileService = FileServiceFactory.getFileService();
            	    GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
            	       .setBucket(BUCKETNAME)
            	       .setKey(filename)
            	       .setMimeType("text/html")
            	       .setAcl("public_read")
            	       .addUserMetadata("fileId", filename);
            	    AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
            	    // Open a channel to write to it
            	     boolean lock = false;
            	     FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lock);
            	     // Different standard Java ways of writing to the channel
            	     // are possible. Here we use a PrintWriter:
            	     PrintWriter out = new PrintWriter(Channels.newWriter(writeChannel, "UTF8"));
                    
                    
                    /*
                    ServletContext context = getServletContext();  
                      
                    // 上传的文件存放路径为...\\WebRoot\\upload\\filename  
                    String dir = context.getRealPath("upload");
                    System.out.println(dir);
                    File file = new File(dir, filename);  
                    file.createNewFile();  
                     */
                    // 获得流，读取数据写入文件  
                   // InputStream in = item.getInputStream();  
                    //FileOutputStream fos = new FileOutputStream(file);  
                     /* 
                    int len;  
                    byte[] buffer = new byte[1024];  
                    while ((len = in.read(buffer)) > 0)  
                        //fos.write(buffer, 0, len);  
                    	out.print(buffer);
                    // 关闭资源文件操作  
                    //fos.close(); 
                    out.close();
                    writeChannel.closeFinally();
                    in.close();  
                    // 删除临时文件  
                    item.delete();  
                }  
            }
            response.getWriter().println("Done writing...");
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    */
    
}
