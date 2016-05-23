package it.giara.phases;

import java.util.Map.Entry;
import java.util.TreeMap;

import it.giara.download.DownloadManager;
import it.giara.download.FileSources;
import it.giara.sql.SQLQuerySettings;
import it.giara.utils.ThreadManager;

public class RecoverDownload
{
	
	public static void asyncRestartDownload()
	{
		final TreeMap<String, String[]> map = new TreeMap<String, String[]>(SQLQuerySettings.getCurrentDownloads());
		
		Runnable run = new Runnable()
		{
			@Override
			public void run()
			{
				for (Entry<String, String[]> data : map.entrySet())
				{
					DownloadManager.downloadFile(data.getKey(), data.getValue()[0], true);
				}
				
				for (Entry<String, String[]> data : map.entrySet())
				{
					if (Integer.parseInt(data.getValue()[1]) != 1)
					{
						FileSources fs = DownloadManager.AllFile.get(data.getKey());
						fs.paused = false;
						if (!fs.waitLoalDownload)
							fs.restart();
					}
				}
				
			}
		};
		
		ThreadManager.submitCacheTask(run);
		
	}
	
}
