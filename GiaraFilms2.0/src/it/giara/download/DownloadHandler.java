package it.giara.download;

import java.util.Map.Entry;
import java.util.TreeMap;

import it.giara.phases.Settings;
import it.giara.utils.Log;

public class DownloadHandler
{
	
	public static void fileNotAvailable(String fileName)
	{
		Log.log(Log.ERROR, "Siamo Spiacenti ma il file " + fileName + " non è al momento disponibile");
		// TODO
	}
	
	public static void FailServerConnect(String error)
	{
		Log.log(Log.ERROR, error);
		// TODO
	}
	
	public static void noRequestFileFromThisBot(String error)
	{
		Log.log(Log.ERROR, error);
		// TODO
	}
	
	public static void BotSendWrongFile(String file1, String file2)
	{
		Log.log(Log.ERROR, "-" + file1 + "- != -" + file2 + "-");
		// TODO
	}
	
	public static void alreadyInDownload(String filename)
	{
		Log.log(Log.ERROR, "Il file: " + filename + " è già in download");
		// TODO
	}
	
	public static void BotSetInList(String BotName)
	{
		if (!Settings.getBoolean("downloadlimit"))
			return;
			
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading + " Il Bot ti ha messo in lista: " + BotName);
		if (CurrentDownloading > 0)
			CurrentDownloading--;
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading);
	}
	
	static int CurrentDownloading = 0;
	
	public static void DownloadComplete(String filename)
	{
		if (!Settings.getBoolean("downloadlimit"))
			return;
			
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading + " Complete download: " + filename);
		if (CurrentDownloading > 0)
			CurrentDownloading--;
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading);
		
		if (CurrentDownloading < Integer.parseInt(Settings.getParameter("downloadlimitN")))
		{
			final TreeMap<String, FileSources> map = new TreeMap<String, FileSources>(DownloadManager.AllFile);
			
			for (Entry<String, FileSources> data : map.entrySet())
			{
				FileSources fs = data.getValue();
				if (fs.waitLoalDownload)
				{
					Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading + " Avvio" + fs.filename);
					fs.waitLoalDownload = false;
					if (!fs.paused)
					{
						fs.restart();
						break;
					}
				}
			}
		}
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading);
	}
	
	public static void TransmissionFailed(String filename)
	{
		if (!Settings.getBoolean("downloadlimit"))
			return;
			
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading + " TransmissionFailed: " + filename);
		if (CurrentDownloading > 0)
			CurrentDownloading--;
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading);
	}
	
	public static void DownloadCreate(String filename, FileSources fs)
	{
		if (!Settings.getBoolean("downloadlimit"))
			return;
			
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading + " DownloadCreate: " + filename);
		
		if (CurrentDownloading >= Integer.parseInt(Settings.getParameter("downloadlimitN")))
		{
			fs.waitLoalDownload = true;
			Log.log(Log.DOWNLOAD, "set waiting");
		}
		else if (!fs.paused)
		{
			Log.log(Log.DOWNLOAD, "set starting");
		}
		
		if (!fs.paused && CurrentDownloading < Integer.parseInt(Settings.getParameter("downloadlimitN")))
			CurrentDownloading++;
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading);
	}
	
	public static void DownloadRestart(String filename, FileSources fs)
	{
		if (!Settings.getBoolean("downloadlimit"))
			return;
			
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading + " Start Download: " + filename);
		
		if (CurrentDownloading >= Integer.parseInt(Settings.getParameter("downloadlimitN")))
		{
			fs.waitLoalDownload = true;
			Log.log(Log.DOWNLOAD, "set waiting");
		}
		else if (!fs.paused)
		{
			Log.log(Log.DOWNLOAD, "set starting");
		}
		
		if (!fs.paused && !fs.waitLoalDownload)
			CurrentDownloading++;
			
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading);
	}
	
	public static void DownloadPause(String filename, FileSources file)
	{
		if (!Settings.getBoolean("downloadlimit"))
			return;
			
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading + " Pause download: " + filename);
		
		if (!file.waitLoalDownload && CurrentDownloading > 0)
			CurrentDownloading--;
			
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading);
		
		if (CurrentDownloading < Integer.parseInt(Settings.getParameter("downloadlimitN")))
		{
			file.waitLoalDownload = true;
			final TreeMap<String, FileSources> map = new TreeMap<String, FileSources>(DownloadManager.AllFile);
			
			for (Entry<String, FileSources> data : map.entrySet())
			{
				FileSources fs = data.getValue();
				if (fs.waitLoalDownload)
				{
					Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading + " Avvio" + fs.filename);
					fs.waitLoalDownload = false;
					if (!fs.paused)
					{
						fs.restart();
						break;
					}
				}
			}
		}
		Log.log(Log.DOWNLOAD, "CD:" + CurrentDownloading);
	}
	
}
