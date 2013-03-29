package functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;

public class FileList {

	public static final String BUCKETNAME = "myhomeworkdataset";
	
	public FileList() {
		
	}
	
	public ArrayList<String> getFileList() throws IOException {
		try {
			ArrayList<String> fileList = new ArrayList<String>();
			String keys = "/gs/" + BUCKETNAME + "/filelist";
  	    	FileService fileService = FileServiceFactory.getFileService();
      	     AppEngineFile readableFile = new AppEngineFile(keys);
      	     FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
      	     // Again, different standard Java ways of reading from the channel.
      	     BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
      	     String line = new String();
      	     while ((line = reader.readLine()) != null) {
      	    	 //res.getWriter().println(line);
      	    	 fileList.add(line);
      	     }

      	    // line = "The woods are lovely, dark, and deep."
      	     readChannel.close();
      	     return fileList;
      	     
  	    } catch (IOException ex) {
  	    	//res.getWriter().println("No such a file named " + req.getParameter("filename"));
  	    	throw new IOException(ex);
  	    }
	}
}
