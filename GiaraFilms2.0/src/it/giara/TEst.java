package it.giara;

import java.io.IOException;
import java.util.List;

import it.giara.analyze.FileInfo;
import it.giara.phases.Settings;
import it.giara.phases.scanservice.LoadFileService;
import it.giara.source.ListLoader;
import it.giara.sql.SQL;
import it.giara.syncdata.NewServerQuery;
import it.giara.tmdb.api.TmdbApiSearchTVSerie;
import it.giara.utils.GZIPCompression;
import it.giara.utils.Log;
import it.giara.utils.MultipartUtility;

public class TEst
{
	public static void main(String[] args)
	{
		// ThreadManager.submitCacheTask(new InitializeRunnable(null));
		/*
		 * String s =
		 * "Fast.And.Furious.Solo.Parti.Originali.2009.iTALiAN.DVDRip.XviD-Republic.CD2.avi";
		 * 
		 * FileInfo fI = new FileInfo(s,true);
		 * 
		 * 
		 * if(1==1) return;
		 */
		
		SQL.connect();
		//
		// SQLQuery.DbClear();
		//
		Settings.init();
		ListLoader.loadSources();
		// ServerQuery.load150News();
		
		// FileInfo f = new
		// FileInfo("Dheepan.Una.Nuova.Vita.2015.iTALiAN.AC3.DvdRip.XviD-FoRaCrEw.avi",
		// true);
		// f.parseTags();
		//
		// Log.log(Log.DEBUG, f.type);
		// Log.log(Log.DEBUG, f.title);
		// Log.log(Log.DEBUG, f.series);
		// Log.log(Log.DEBUG, f.episode);
		// Log.log(Log.DEBUG, f.year);
		//
		// Log.log(Log.DEBUG, f.video);
		// Log.log(Log.DEBUG, f.audio);
		// for (int x = 0; x < f.tags.size(); x++)
		// Log.log(Log.DEBUG, f.tags.get(x));
		
		// Log.log(Log.DEBUG, f.year);
		// ServerQuery.sendScheda(SQLQuery.readScheda(674, MainType.Film));
		// Log.log(Log.DEBUG, ServerQuery.requestScheda(676, MainType.Film));
		
		// ServerQuery.sendFileInfo("testtest", "350M", 61387, MainType.Film);
		//
		// ServerQuery.loadUntil(Integer.parseInt(Settings.getParameter("lastserversync")));
		
		// for (final SourceChan s : ListLoader.sources)
		// {
		// Log.log(Log.DEBUG, s.getStatus()+" "+s.server + " " + s.chan );
		//
		// }
		
//		new LoadFileService().run();
		
		NewServerQuery.load150News();
		
		// TmdbApiSearchTVSerie film = new TmdbApiSearchTVSerie("breaking bad",
		// -1);
		
		// if (film.scheda != null)
		// {
		// Log.log(Log.DEBUG, film.scheda.toString());
		// }
		//

		
	}
}