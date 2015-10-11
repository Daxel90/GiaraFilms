package it.giara;

import it.giara.download.DownloadManager;
import it.giara.download.FileSources;
import it.giara.phases.Settings;
import it.giara.source.ListLoader;
import it.giara.sql.SQL;

public class TEst
{
	public static void main(String[] args)
	{
		ListLoader.loadSources();
		SQL.connect();
		Settings.init();
		FileSources sources = DownloadManager
				.getFileSources("Sopravvissuto.The.Martian.2015.iTALiAN.MD.TS.XviD-iNCOMiNG.avi");		
		sources.requestDownload();
		
	}
}