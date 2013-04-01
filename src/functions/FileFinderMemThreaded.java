package functions;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.channels.Channels;
import java.util.logging.Level;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.memcache.ErrorHandlers;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

public class FileFinderMemThreaded implements Runnable {

	private String filename;
	public static final String BUCKETNAME = "myhomeworkdataset";
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			//retrieve file content from memcache
			MemcacheService syncCache = MemcacheServiceFactory.getMemcacheService();
			syncCache.setErrorHandler(ErrorHandlers.getConsistentLogAndContinue(Level.INFO));
			String value = (String)syncCache.get(filename);
			if (value == null) {
				//if not exists, read from Google Cloud Storage
				String pathname = "/gs/" + BUCKETNAME + "/" + filename;
				FileService fileService = FileServiceFactory.getFileService();
				AppEngineFile readableFile = new AppEngineFile(pathname);
				FileReadChannel readChannel = fileService.openReadChannel(readableFile, false);
				BufferedReader reader = new BufferedReader(Channels.newReader(readChannel, "UTF8"));
				String line = new String();
				String content = new String();
				while ((line = reader.readLine()) != null)
					content += line + "\n";
				readChannel.close();
				syncCache.put(filename, content);
			} else {
				String content = value;
			}
		} catch (IOException e) {
			
		}
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
