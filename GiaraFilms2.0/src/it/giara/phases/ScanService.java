package it.giara.phases;

import it.giara.analyze.FileInfo;
import it.giara.http.HTTPList;
import it.giara.http.HTTPSearchFilm;
import it.giara.http.HTTPSearchTVSerie;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.sql.SQLQuery;
import it.giara.utils.Log;
import it.giara.utils.ThreadManager;

public class ScanService implements Runnable
{
	public static boolean scanning = false;
	
	public void run()
	{
		Log.log(Log.INFO, "GiaraFilms Start Scanner");
		scanning = true;
		
		for (SourceChan s : ListLoader.sources)
		{
			HTTPList search = new HTTPList(s.link, ".");
			Log.log(Log.DEBUG, search.file.size());
			for (final String s2 : search.file)
			{
				if (!SQLQuery.existFile(s2))
				{
					
					Runnable check = new Runnable()
					{
						@Override
						public void run()
						{
							int fileID = SQLQuery.writeFile(s2);
							FileInfo f = new FileInfo(s2);
							
							switch (f.type)
							{
								case Film:
									int cache = SQLQuery.getCacheSearch(f.title, f.type);
									
									if (cache == -2)
										return;
										
									if (cache == -1)
									{
										HTTPSearchFilm httpF = new HTTPSearchFilm(f.title);
										if (httpF.scheda == null)
										{
											SQLQuery.writeCacheSearch(f.title, f.type, -1);
											break;
										}
										int schedaF = SQLQuery.writePreSchedaFilm(httpF.scheda);
										
										SQLQuery.writeCacheSearch(f.title, f.type, schedaF);
										SQLQuery.writeFileInfo(fileID, f.type, schedaF);
									}
									else
									{
										SQLQuery.writeFileInfo(fileID, f.type, cache);
									}
									break;
								case SerieTV:
									int cache2 = SQLQuery.getCacheSearch(f.title, f.type);
									
									if (cache2 == -2)
										return;
										
									if (cache2 == -1)
									{
										HTTPSearchTVSerie httpF = new HTTPSearchTVSerie(f.title);
										if (httpF.scheda == null)
										{
											SQLQuery.writeCacheSearch(f.title, f.type, -1);
											break;
										}
										int schedaSTV = SQLQuery.writePreSchedaTvSeries(httpF.scheda);
										
										SQLQuery.writeCacheSearch(f.title, f.type, schedaSTV);
										SQLQuery.writeFileInfo(fileID, f.type, schedaSTV);
										
										SQLQuery.writeEpisodeInfo(fileID, schedaSTV, f.episode, f.series);
									}
									else
									{
										
										SQLQuery.writeFileInfo(fileID, f.type, cache2);
										SQLQuery.writeEpisodeInfo(fileID, cache2, f.episode, f.series);
									}
									
									break;
								default:
									break;
							}
						}
					};
					ThreadManager.submitPoolTask(check);
				}
				
			}
			
		}
	}
	
}
