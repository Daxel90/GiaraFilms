package it.giara;

import it.giara.phases.Settings;
import it.giara.source.ListLoader;
import it.giara.sql.SQL;
import it.giara.tmdb.api.TmdbApiSearchTVSerie;
import it.giara.utils.Log;

public class TEst
{
	public static void main(String[] args)
	{
		// ThreadManager.submitCacheTask(new InitializeRunnable(null));
		
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
		
		TmdbApiSearchTVSerie film = new TmdbApiSearchTVSerie("breaking bad", -1);
		
		if (film.scheda != null)
		{
			Log.log(Log.DEBUG, film.scheda.toString());
		}
		
	}
}