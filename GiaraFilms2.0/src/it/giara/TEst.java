package it.giara;

import it.giara.analyze.FileInfo;
import it.giara.analyze.enums.MainType;
import it.giara.phases.Settings;
import it.giara.source.ListLoader;
import it.giara.sql.SQL;
import it.giara.tmdb.api.TmdbApiLoadSchede;
import it.giara.utils.Log;

public class TEst
{
	public static void main(String[] args)
	{ // TEST
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
		
		FileInfo f = new FileInfo("The Walking Dead - 4x11 - Salvare Il Mondo.mkv", true);
		f.parseTags();
		
		Log.log(Log.DEBUG, f.type);
		Log.log(Log.DEBUG, f.title);
		Log.log(Log.DEBUG, f.series);
		Log.log(Log.DEBUG, f.episode);
		Log.log(Log.DEBUG, f.year);
		//
		Log.log(Log.DEBUG, f.video);
		Log.log(Log.DEBUG, f.audio);
		for (int x = 0; x < f.tags.size(); x++)
			Log.log(Log.DEBUG, f.tags.get(x));
			
		// Log.log(Log.DEBUG, f.year);
		// ServerQuery.sendScheda(SQLQuery.readScheda(674, MainType.Film));
		// Log.log(Log.DEBUG, ServerQuery.requestScheda(676, MainType.Film));
		
		// ServerQuery.sendFileInfo("testtest", "350M", 61387, MainType.Film);
		//
		// ServerQuery.loadUntil(Integer.parseInt(Settings.getParameter("lastserversync")));
		
		// for (final SourceChan s : ListLoader.sources)
		// {
		// HTTPList ht = new HTTPList(s.link,".");
		// Log.log(Log.DEBUG,ht.file.size());
		//
		// }
		
		// NewServerQuery.loadRequestCommand();
		
//		TmdbApiLoadSchede api = new TmdbApiLoadSchede(62560, MainType.SerieTV);
//		System.out.println(api.scheda);
		
		// new LoadFileService().run();
		
		// NewServerQuery.load100News();
		
		// TmdbApiSearchTVSerie film = new TmdbApiSearchTVSerie("breaking bad",
		// -1);
		
		// if (film.scheda != null)
		// {
		// Log.log(Log.DEBUG, film.scheda.toString());
		// }
		//
		
	}
}