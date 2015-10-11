package it.giara.download;

import java.io.File;
import java.util.HashMap;

import it.giara.http.HTTPFileSources;
import it.giara.irc.IrcConnection;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.utils.Log;
import it.giara.utils.ThreadManager;

public class DownloadManager
{
	public static HashMap<String, IrcConnection> servers = new HashMap<String, IrcConnection>();
	public static HashMap<String, FileSources> BotRequest = new HashMap<String, FileSources>();
	
	public static void downloadFile(String FileName, String file)
	{
		FileSources sources = DownloadManager.getFileSources(FileName);
		sources.requestDownload(new File(file));
	}
	
	public static FileSources getFileSources(String filename)
	{
		final FileSources result = new FileSources(filename);
		
		Runnable run = new Runnable()
		{
			
			@Override
			public void run()
			{
				
				for (int x = 0; x < ListLoader.sources.size(); x++)
				{
					SourceChan chan = ListLoader.sources.get(x);
					new HTTPFileSources(chan, result);
				}
				result.loadingBotList = false;
				
				Log.log(Log.DEBUG, result.totalBot);
			}
		};
		
		ThreadManager.submitCacheTask(run);
		
		return result;
	}
	
}
