package it.giara.phases;

import it.giara.analyze.FileInfo;
import it.giara.http.HTTPList;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.sql.SQLQuery;
import it.giara.tmdb.http.TMDBSearchFilm;
import it.giara.tmdb.http.TMDBSearchTVSerie;
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
		
		Log.log(Log.SCANSERVICE, "Nfile: " + Nfile);
		Log.log(Log.SCANSERVICE, "Nfilm: " + Nfilm);
		Log.log(Log.SCANSERVICE, "NEpisode: " + NEpisode);
		
		for (SourceChan s : ListLoader.sources)
		{
			NList++;
			HaveList = false;
			HTTPList search = new HTTPList(s.link, ".");
			Log.log(Log.SCANSERVICE, search.file.size());
			LSize = search.file.size();
			LStatus = 0;
			HaveList = true;
			for (int k = 0; k < search.file.size(); k++)
			{
				final String s2 = search.file.get(k);
				final String size = search.sizeList.get(k);
				LStatus++;
				if (!SQLQuery.existFile(s2))
				{
					Nfile++;
					Runnable check = new Runnable()
					{
						@Override
						public void run()
						{
							FileInfo f = new FileInfo(s2);
							
							switch (f.type)
							{
								case Film:
									int cache = SQLQuery.getCacheSearch(f.title, f.type);
									
									if (cache == -2)
									{
										SQLQuery.writeFile(s2,size, -1, f.type, true);
										return;
									}
										
									if (cache == -1)
									{
										TMDBSearchFilm httpF = new TMDBSearchFilm(f.title, f.year);
										if (httpF.scheda == null)
										{
											SQLQuery.writeCacheSearch(f.title, f.type, -1);
											SQLQuery.writeFile(s2,size, -1, f.type, true);
											break;
										}
										int schedaID = SQLQuery.writeScheda(httpF.scheda, true);
										
										SQLQuery.writeCacheSearch(f.title, f.type, schedaID);
										SQLQuery.writeFile(s2,size, schedaID, f.type, true);
										Nfilm++;
									}
									else
									{
										SQLQuery.writeFile(s2,size, cache, f.type, true);
									}
									break;
								case SerieTV:
									int cache2 = SQLQuery.getCacheSearch(f.title, f.type);
									
									if (cache2 == -2)
									{
										SQLQuery.writeFile(s2,size, -1, f.type, true);
										return;
									}
										
									if (cache2 == -1)
									{
										TMDBSearchTVSerie httpF = new TMDBSearchTVSerie(f.title);
										if (httpF.scheda == null)
										{
											SQLQuery.writeFile(s2,size, -1, f.type, true);
											SQLQuery.writeCacheSearch(f.title, f.type, -1);
											break;
										}
										int schedaSTV = SQLQuery.writeScheda(httpF.scheda, true);
										
										SQLQuery.writeCacheSearch(f.title, f.type, schedaSTV);
										int FileId = SQLQuery.writeFile(s2,size, schedaSTV, f.type, true);
										
										SQLQuery.writeEpisodeInfo(FileId, schedaSTV, f.episode, f.series);
										NEpisode++;
									}
									else
									{
										int FileId = SQLQuery.writeFile(s2,size, cache2, f.type, true);
										SQLQuery.writeEpisodeInfo(FileId, cache2, f.episode, f.series);
										NEpisode++;
									}
									break;
								default:
									SQLQuery.writeFile(s2,size, -1, f.type, true);
									break;
							}
						}
					};
					while(ThreadManager.getPoolScanServiceWait()>=ThreadManager.poolScanServiceSize)
					{
						FunctionsUtils.sleep(10);
					}
					ThreadManager.submitPoolScanServiceTask(check);
				}
				
			}
			
		}
		
		scanning = false;
	}
	
}
