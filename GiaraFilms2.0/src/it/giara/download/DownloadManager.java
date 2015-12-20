package it.giara.download;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import it.giara.irc.IrcConnection;
import it.giara.utils.ThreadManager;

public class DownloadManager
{
	public static ConcurrentHashMap<String, IrcConnection> servers = new ConcurrentHashMap<String, IrcConnection>();
	public static ConcurrentHashMap<String, FileSources> BotRequest = new ConcurrentHashMap<String, FileSources>();
	public static ConcurrentHashMap<String, FileSources> AllFile = new ConcurrentHashMap<String, FileSources>();
	
	public static void downloadFile(String FileName, String file, boolean paused)
	{
		FileSources sources = DownloadManager.getFileSources(FileName, paused);
		
		sources.waitUntilAllBotListAreLoaded();
		
		sources.requestDownload(new File(file));
	}
	
	public static void downloadFile(String FileName)
	{
		FileSources sources = DownloadManager.getFileSources(FileName, false);
		sources.requestDownload();
	}
	
	public static void downloadCollection(HashMap<Integer, String> collection)
	{
		for (final Entry<Integer, String> file : collection.entrySet())
		{
			Runnable task = new Runnable()
			{
				public void run()
				{
					downloadFile(file.getValue());
				}
			};
			ThreadManager.submitCacheTask(task);
		}
	}
	
	public static FileSources getFileSources(String filename, boolean paused)
	{
		final FileSources result = new FileSources(filename);
		result.paused = paused;
		
		if (paused)
			return result;
			
		result.loadList();
		
		return result;
	}
	
}
