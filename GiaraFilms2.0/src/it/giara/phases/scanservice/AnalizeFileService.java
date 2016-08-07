package it.giara.phases.scanservice;

import java.util.LinkedList;
import java.util.Queue;

import it.giara.analyze.FileInfo;
import it.giara.analyze.enums.MainType;
import it.giara.sql.SQLQuery;
import it.giara.syncdata.NewServerQuery;
import it.giara.tmdb.api.TmdbApiSearchFilm;
import it.giara.tmdb.api.TmdbApiSearchTVSerie;
import it.giara.tmdb.schede.TMDBScheda;
import it.giara.utils.Log;
import it.giara.utils.ThreadManager;

public class AnalizeFileService implements Runnable
{
	public static boolean running = false;
	public static int checkRequest = 0;
	public static int checked = 0;
	public static Queue<String> pending = new LinkedList<String>();
	
	@Override
	public void run()
	{
		Log.log(Log.INFO, "GiaraFilms start AnalizeFileService");
		running = true;
		
		while (!pending.isEmpty())
		{
			checked++;
			String fileName = pending.poll();
			FileInfo fI = new FileInfo(fileName, true);
			
			int cache = SQLQuery.get_new_cache(fI.title, fI.type, fI.year);
			
			if (cache == -1)
			{
				NewServerQuery.updateFileInfo(fI.title, cache);
				continue;
			}
			else if (cache == -2)
			{
				TMDBScheda scheda = null;
				
				if (fI.type.equals(MainType.Film))
				{
					TmdbApiSearchFilm tmdb = new TmdbApiSearchFilm(fI.title, fI.year);
					scheda = tmdb.scheda;
				}
				else if (fI.type.equals(MainType.SerieTV))
				{
					TmdbApiSearchTVSerie tmdb = new TmdbApiSearchTVSerie(fI.title, fI.year);
					scheda = tmdb.scheda;
				}
				
				if (scheda == null)
				{
					SQLQuery.write_update_new_cache(fI.title, fI.type, -1, fI.year);
					SQLQuery.write_update_new_cache_search(fI.title, fI.type, -1, fI.year);
					NewServerQuery.updateFileInfo(fI.Filename, -1);
					continue;
				}
				int schedaID = scheda.ID;
				
				SQLQuery.write_update_new_cache(fI.title, fI.type, schedaID, fI.year);
				SQLQuery.write_update_new_cache_search(fI.title, fI.type, schedaID, fI.year);
				
				NewServerQuery.uploadSchede(scheda);
				NewServerQuery.updateFileInfo(fI.Filename, schedaID);
			}
			else
			{
				NewServerQuery.updateFileInfo(fI.Filename, cache);
			}
			
		}
		running = false;
	}
	
	public static void addFile(String filename)
	{
		checkRequest++;
		pending.add(filename);
		if (!running)
		{
			running = true;
			ThreadManager.submitCacheTask(new AnalizeFileService());
		}
	}
	
}
