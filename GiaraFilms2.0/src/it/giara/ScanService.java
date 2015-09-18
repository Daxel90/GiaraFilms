package it.giara;

import it.giara.analyze.FileInfo;
import it.giara.http.HTTPList;
import it.giara.http.HTTPSearchFilm;
import it.giara.http.HTTPSearchTVSerie;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.sql.SQL;
import it.giara.sql.SQLQuery;
import it.giara.utils.DirUtils;
import it.giara.utils.Log;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ScanService 
{

	public static void main(String[] arg)
	{
		ExecutorService executor = Executors.newFixedThreadPool(10);
		Log.log(Log.INFO, "GiaraFilms News Scanner");
		DirUtils.workDir = new File(".");
		ListLoader.loadSources();
		SQL.connect();
		for(SourceChan s : ListLoader.sources)
		{
			HTTPList search =	new HTTPList(s.link,".");
			Log.log(Log.DEBUG, search.file.size());
				for(final String s2 : search.file)
				{
					if(!SQLQuery.existFile(s2))
					{
						
						Runnable check = new Runnable()
						{
							@Override
							public void run()
							{
								int fileID = SQLQuery.writeFile(s2);
								FileInfo f = new FileInfo(s2);
								
								switch(f.type)
								{
									case Film:
										int cache = SQLQuery.getCacheSearch(f.title,f.type);
										if(cache == -1)
										{
											HTTPSearchFilm httpF =	new HTTPSearchFilm(f.title);
											if(httpF.scheda == null)
												break;
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
										int cache2 = SQLQuery.getCacheSearch(f.title,f.type);
										if(cache2 == -1)
										{
											HTTPSearchTVSerie httpF =	new HTTPSearchTVSerie(f.title);
											if(httpF.scheda == null)
												break;
											int schedaSTV = SQLQuery.writePreSchedaTvSeries(httpF.scheda);
											
											
											SQLQuery.writeCacheSearch(f.title, f.type, schedaSTV);
											SQLQuery.writeFileInfo(fileID, f.type, schedaSTV);
											
											SQLQuery.writeEpisodeInfo(fileID,schedaSTV,f.episode,f.series);
										}
										else
										{
											
											SQLQuery.writeFileInfo(fileID, f.type, cache2);
											SQLQuery.writeEpisodeInfo(fileID,cache2,f.episode,f.series);
										}
										
										break;
									default:
										break;
								}
							}
						};
						
						executor.execute(check);
					}

				}
				
		}
	}
	
	
}
