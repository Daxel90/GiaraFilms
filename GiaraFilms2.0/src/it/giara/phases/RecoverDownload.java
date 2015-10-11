package it.giara.phases;

import java.util.List;

import it.giara.download.DownloadManager;
import it.giara.sql.SQLQuerySettings;
import it.giara.utils.ThreadManager;

public class RecoverDownload
{
	
	public static void asyncRestartDownload()
	{
		final List<String[]> list = SQLQuerySettings.getCurrentDownloads();
		
		Runnable run = new Runnable()
		{
			@Override
			public void run()
			{
				for (String[] s : list)
				{
					DownloadManager.downloadFile(s[0], s[1]);
				}
				
			}
		};
		
		ThreadManager.submitCacheTask(run);
		
	}
	
}
