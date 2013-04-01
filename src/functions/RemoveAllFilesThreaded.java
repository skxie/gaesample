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

public class RemoveAllFilesThreaded implements Runnable {

	public static final String BUCKETNAME = "myhomeworkdataset";
	private List<String> filenames;
	
	@Override
	public void run() {
		// remove files from filenames
		for (String filename : filenames) {
			try {
				String fname = "/gs/" + BUCKETNAME + "/" + filename;
				FileService fileService = FileServiceFactory.getFileService();
				AppEngineFile deletableFile = new AppEngineFile(fname);
				fileService.delete(deletableFile);
			} catch (IOException e) {
			
			}
		}
	}
	
	public void setFilename(List<String> filenames) {
		this.filenames = filenames;
	}

}
