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
	public static HashMap<String, FileSources> AllFile = new HashMap<String, FileSources>();
	
	public static void downloadFile(String FileName, String file)
	{
		FileSources sources = DownloadManager.getFileSources(FileName);
		sources.requestDownload(new File(file));
	}
	
	public static void downloadFile(String FileName)
	{
		FileSources sources = DownloadManager.getFileSources(FileName);
		sources.requestDownload();
	}
	
	public static FileSources getFileSources(String filename)
	{
		final FileSources result = new FileSources(filename);
		
		result.loadingBotList = ListLoader.sources.size();
		for (int x = 0; x < ListLoader.sources.size(); x++)
		{
			final int K = x;
			Runnable run = new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						SourceChan chan = ListLoader.sources.get(K);
						new HTTPFileSources(chan, result);
					} finally
					{
						result.loadingBotList--;
					}
				}
			};
			
			ThreadManager.submitCacheTask(run);
		}
		
		Log.log(Log.DEBUG, result.totalBot);
		
		return result;
	}
	
}
