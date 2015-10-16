package it.giara.download;

import java.io.File;
import java.util.HashMap;

import it.giara.irc.IrcConnection;

public class DownloadManager
{
	public static HashMap<String, IrcConnection> servers = new HashMap<String, IrcConnection>();
	public static HashMap<String, FileSources> BotRequest = new HashMap<String, FileSources>();
	public static HashMap<String, FileSources> AllFile = new HashMap<String, FileSources>();
	
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
