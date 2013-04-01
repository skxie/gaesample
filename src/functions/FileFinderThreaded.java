package functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;

public class FileFinderThreaded implements Runnable {

	private String filename;
	
	@Override
	public void run() {
		try {
			//find file
			FileService fileService = FileServiceFactory.getFileService();
			AppEngineFile readableFile = new AppEngineFile(filename);
			FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
			BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
			String line = new String();
			while ((line = reader.readLine()) != null)
				;
			readChannel.close();
		} catch (IOException e) {
			
		}
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}

}
