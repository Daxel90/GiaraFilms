package it.giara.download;

import it.giara.http.HTTPFileSources;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.utils.Log;

public class DownloadManager
{
	
	public static void Download(String filename)
	{
	
	}
	
	public static FileSources getFileSources(String filename)
	{
		FileSources result = new FileSources(filename);
		for (int x = 0; x < ListLoader.sources.size(); x++)
		{
			SourceChan chan = ListLoader.sources.get(x);
			new HTTPFileSources(chan, result);
		}
		
		Log.log(Log.DEBUG, result.totalBot);
		
		return result;
	}
	
}
