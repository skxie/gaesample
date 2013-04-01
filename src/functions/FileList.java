package functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;

public class FileList {

	public static final String BUCKETNAME = "myhomeworkdataset";
	
	public FileList() {
		
	}
	
	public ArrayList<String> getFileList() throws IOException {
		try {
			//get file list
			ArrayList<String> fileList = new ArrayList<String>();
			String keys = "/gs/" + BUCKETNAME + "/filelist";
  	    	FileService fileService = FileServiceFactory.getFileService();
      	     AppEngineFile readableFile = new AppEngineFile(keys);
      	     FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
      	     BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
      	     String line = new String();
      	     while ((line = reader.readLine()) != null) {
      	    	 fileList.add(line);
      	     }

      	     readChannel.close();
      	     return fileList;
      	     
  	    } catch (IOException ex) {
  	    	throw new IOException(ex);
  	    }
	}
	
	public void addFile(String filename) throws IOException {
		try {
			ArrayList<String> fileList = this.getFileList();
			//add the file name into the file list
			if (!fileList.contains(filename))
				fileList.add(filename);
      	    FileService fileService = FileServiceFactory.getFileService();
     	    GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
   	         	.setBucket(BUCKETNAME)
   	         	.setKey("filelist")
   	         	.setMimeType("text/html")
   	         	.setAcl("public_read")
   	         	.addUserMetadata("fileId", "filelist");
   	        AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
   	        boolean lock = true;
   	        FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lock);
   	        byte[] newline = {'\n'};
   	        for (String st : fileList) {
   	        	writeChannel.write(ByteBuffer.wrap(st.getBytes()));
   	        	writeChannel.write(ByteBuffer.wrap(newline));
   	        }
   	        writeChannel.closeFinally();
  	    } catch (IOException ex) {
  	    	throw new IOException(ex);
  	    }
	}
	
	public void removeFile(String filename) throws IOException {
		try {
			ArrayList<String> fileList = this.getFileList();
      	    FileService fileService = FileServiceFactory.getFileService();
     	    GSFileOptionsBuilder optionsBuilder = new GSFileOptionsBuilder()
   	         	.setBucket(BUCKETNAME)
   	         	.setKey("filelist")
   	         	.setMimeType("text/html")
   	         	.setAcl("public_read")
   	         	.addUserMetadata("fileId", "filelist");
   	        AppEngineFile writableFile = fileService.createNewGSFile(optionsBuilder.build());
   	        boolean lock = true;
   	        FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lock);
   	        byte[] newline = {'\n'};
   	        for (String st : fileList) {
   	        	//delete the file name from the file list
   	        	if (!st.equals(filename)) {
   	        		writeChannel.write(ByteBuffer.wrap(st.getBytes()));
   	        		writeChannel.write(ByteBuffer.wrap(newline));
   	        	}
   	        }
   	        writeChannel.closeFinally();
  	    } catch (IOException ex) {
  	    	throw new IOException(ex);
  	    }
	}
}
