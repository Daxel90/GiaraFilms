package it.giara.phases;

import it.giara.analyze.FileInfo;
import it.giara.http.HTTPList;
import it.giara.http.HTTPSearchFilm;
import it.giara.http.HTTPSearchTVSerie;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.sql.SQLQuery;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;
import it.giara.utils.ThreadManager;

public class ScanService implements Runnable
{
	public static boolean scanning = false;
	public static boolean HaveList = false;
	public static int Nfile = 0;
	public static int Nfilm = 0;
	public static int NEpisode = 0;
	public static int NList = 0;
	public static int LSize = 0;
	public static int LStatus = 0;
	
	public void run()
	{
		Log.log(Log.INFO, "GiaraFilms Start Scanner");
		scanning = true;
		
		Nfile = SQLQuery.getFileNumber();
		Nfilm = SQLQuery.getFilmNumber();
		NEpisode = SQLQuery.getEpisodeNumbers();
		
		Log.log(Log.DEBUG, "Nfile: " + Nfile);
		Log.log(Log.DEBUG, "Nfilm: " + Nfilm);
		Log.log(Log.DEBUG, "NEpisode: " + NEpisode);
		
		for (SourceChan s : ListLoader.sources)
		{
			NList++;
			HaveList = false;
			HTTPList search = new HTTPList(s.link, ".");
			Log.log(Log.DEBUG, search.file.size());
			LSize = search.file.size();
			LStatus = 0;
			HaveList = true;
			for (final String s2 : search.file)
			{
				LStatus++;
				if (!SQLQuery.existFile(s2))
				{
					Nfile++;
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
										Nfilm++;
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
										NEpisode++;
									}
									else
									{
										SQLQuery.writeFileInfo(fileID, f.type, cache2);
										SQLQuery.writeEpisodeInfo(fileID, cache2, f.episode, f.series);
										NEpisode++;
									}
									
									break;
								default:
									break;
							}
						}
					};
					while(ThreadManager.getPoolWait()>=ThreadManager.poolSize)
					{
						FunctionsUtils.sleep(10);
					}
					ThreadManager.submitPoolTask(check);
				}
				
			}
			
		}
		
		scanning = false;
	}
	
}
