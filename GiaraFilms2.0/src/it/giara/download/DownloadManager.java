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
		
		sources.requestDownload(new File(file));
	}
	
	public static void downloadFile(String FileName, boolean paused)
	{
		FileSources sources = DownloadManager.getFileSources(FileName, paused);
		
		sources.requestDownload();
		
	}
	
	public static void downloadCollection(final HashMap<Integer, String> collection)
	{
		Runnable task = new Runnable()
		{
			public void run()
			{
				for (final Entry<Integer, String> file : collection.entrySet())
				{
					downloadFile(file.getValue(),true);
				}
				for (final Entry<Integer, String> file : collection.entrySet())
				{
					FileSources fs = AllFile.get(file.getValue());
					fs.paused = false;
					if (!fs.waitLoalDownload)
						fs.restart();
				}
			}
		};
		ThreadManager.submitCacheTask(task);
	}
	
	public static FileSources getFileSources(String filename, boolean paused)
	{
		final FileSources result = new FileSources(filename);
		result.paused = paused;
		
		DownloadHandler.DownloadCreate(filename, result);
		
		if (result.paused || result.waitLoalDownload)
		{
			return result;
		}
		
		result.loadList();
		
		return result;
	}
	
}
