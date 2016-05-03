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
	
	static int CurrentDownloading = 0;
	
	public static void DownloadComplete(String filename)
	{
		if(Integer.parseInt(Settings.getParameter("downloadlimitN")) != 1)
				return;
		
		Log.log(Log.IRC, "Complete download: " + filename);
		
		if (CurrentDownloading < Integer.parseInt(Settings.getParameter("downloadlimitN")))
		{
			final TreeMap<String, FileSources> map = new TreeMap<String, FileSources>(DownloadManager.AllFile);
			
			for (Entry<String, FileSources> data : map.entrySet())
			{
				FileSources fs = data.getValue();
				if (fs.waitLoalDownload)
				{
					Log.log(Log.DEBUG, "Avvio" + fs.filename);
					fs.waitLoalDownload = false;
					if (!fs.paused)
					{
						fs.restart();
						break;
					}
				}
			}
		}
	}
	
	public static void DownloadCreate(String filename, FileSources fs)
	{
		if(Integer.parseInt(Settings.getParameter("downloadlimit")) != 1)
			return;
		
		Log.log(Log.DEBUG, fs.paused + " - " + fs.waitLoalDownload + " - prima " + CurrentDownloading);
		if (CurrentDownloading >= Integer.parseInt(Settings.getParameter("downloadlimitN")))
		{
			fs.waitLoalDownload = true;
			Log.log(Log.DEBUG,"set waiting");
		}
		
		if (!fs.paused && CurrentDownloading < Integer.parseInt(Settings.getParameter("downloadlimitN")))
			CurrentDownloading++;
		Log.log(Log.DEBUG, fs.paused + " - " + fs.waitLoalDownload + " -dopo " + CurrentDownloading);
	}
	
	public static void DownloadStart(String filename, FileSources fs)
	{
		if(Integer.parseInt(Settings.getParameter("downloadlimit")) != 1)
			return;
		
		Log.log(Log.IRC, "Start Download: " + filename);
		Log.log(Log.DEBUG, fs.paused + " - " + fs.waitLoalDownload + " - " + CurrentDownloading);
		if (CurrentDownloading >= Integer.parseInt(Settings.getParameter("downloadlimitN")))
		{
			fs.waitLoalDownload = true;
			Log.log(Log.DEBUG,"set waiting");
		}
		
		if(!fs.paused && !fs.waitLoalDownload)
		CurrentDownloading++;
	}
	
	public static void DownloadRestart(String filename, FileSources fs)
	{
		if(Integer.parseInt(Settings.getParameter("downloadlimit")) != 1)
			return;
		
		Log.log(Log.IRC, "Start Download: " + filename);
		Log.log(Log.DEBUG, fs.paused + " - " + fs.waitLoalDownload + " - " + CurrentDownloading);
		if (CurrentDownloading >= Integer.parseInt(Settings.getParameter("downloadlimitN")))
		{
			fs.waitLoalDownload = true;
			Log.log(Log.DEBUG,"set waiting");
		}
		
		if(!fs.paused && !fs.waitLoalDownload)
		CurrentDownloading++;
	}
	
	
	public static void DownloadPause(String filename, FileSources file)
	{
		if(Integer.parseInt(Settings.getParameter("downloadlimit")) != 1)
			return;
		
		Log.log(Log.IRC, "Pause download: " + filename);
		
		if (!file.waitLoalDownload)
			CurrentDownloading--;
			
		Log.log(Log.DEBUG, file.paused + " - " + file.waitLoalDownload + " - " + CurrentDownloading);
		
		if (CurrentDownloading < Integer.parseInt(Settings.getParameter("downloadlimitN")))
		{
			file.waitLoalDownload = true;
			final TreeMap<String, FileSources> map = new TreeMap<String, FileSources>(DownloadManager.AllFile);
			
			for (Entry<String, FileSources> data : map.entrySet())
			{
				FileSources fs = data.getValue();
				if (fs.waitLoalDownload)
				{
					Log.log(Log.DEBUG, "Avvio" + fs.filename);
					fs.waitLoalDownload = false;
					if (!fs.paused)
					{
						fs.restart();
						break;
					}
				}
			}
		}
	}
	
}
