package it.giara;

import it.giara.download.DownloadManager;
import it.giara.download.FileSources;
import it.giara.source.ListLoader;

public class TEst
{
	public static void main(String[] args)
	{
		ListLoader.loadSources();
		FileSources sources = DownloadManager
				.getFileSources("Sopravvissuto.The.Martian.2015.iTALiAN.MD.TS.XviD-iNCOMiNG.avi");		
		sources.requestDownload();
		
	}
}