package it.giara.phases;

import it.giara.analyze.FileInfo;
import it.giara.analyze.enums.MainType;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.http.HTTPList;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.sql.SQLQuery;
import it.giara.tmdb.http.TMDBSearchFilm;
import it.giara.tmdb.http.TMDBSearchTVSerie;
import it.giara.tmdb.schede.TMDBScheda;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;
import it.giara.utils.ThreadManager;

public class SearchService
{
	private AbstractFilmList list;
	private String searchString;
	Runnable runnable;
	private boolean run = false;
	
	public int endCheckList = 0;
	public int SizeCheckList = 0;
	
	public SearchService(AbstractFilmList l, String s)
	{
		list = l;
		searchString = s;
		list.loading = true;
		run = true;
		SizeCheckList = ListLoader.sources.size();
		runnable = new Runnable()
		{
			@Override
			public void run()
			{
				search();
			}
		};
		ThreadManager.submitCacheTask(runnable);
	}
	
	public void search()
	{
		Log.log(Log.INFO, "GiaraFilms Start Search");
		
		for (final SourceChan s : ListLoader.sources)
		{
			Runnable listRun = new Runnable()
			{
				public void run()
				{
					try
					{
						if (!run)
							return;
						HTTPList search = new HTTPList(s.link, searchString);
						
						for (int k = 0; k < search.file.size(); k++)
						{
							final String s2 = search.file.get(k);
							final String size = search.sizeList.get(k);
							if (!run)
								return;
								
							list.addFile(new String[] { s2, size });
							if (SQLQuery.existFile(s2))
							{
								int IDScheda = SQLQuery.getSchedaId(s2);
								if (IDScheda == -1)
									continue;
									
								TMDBScheda scheda = SQLQuery.readScheda(IDScheda,
										MainType.getMainTypeByID(SQLQuery.getFileType(s2)));
										
								if (scheda != null)
									list.addScheda(scheda);
									
							}
							else
							{
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
													return;
													
												if (cache == -1)
												{
													TMDBSearchFilm httpF = new TMDBSearchFilm(f.title, f.year);
													if (httpF.scheda == null)
													{
														SQLQuery.writeCacheSearch(f.title, f.type, -1);
														break;
													}
													int schedaID = SQLQuery.writeScheda(httpF.scheda, true);
													
													list.addScheda(httpF.scheda);
													
													SQLQuery.writeCacheSearch(f.title, f.type, schedaID);
													SQLQuery.writeFile(s2, size, schedaID, f.type, true);
												}
												else
												{
													SQLQuery.writeFile(s2, size, cache, f.type, true);
												}
												break;
											case SerieTV:
												int cache2 = SQLQuery.getCacheSearch(f.title, f.type);
												
												if (cache2 == -2)
													return;
													
												if (cache2 == -1)
												{
													TMDBSearchTVSerie httpF = new TMDBSearchTVSerie(f.title);
													if (httpF.scheda == null)
													{
														SQLQuery.writeCacheSearch(f.title, f.type, -1);
														break;
													}
													int schedaSTV = SQLQuery.writeScheda(httpF.scheda, true);
													
													SQLQuery.writeCacheSearch(f.title, f.type, schedaSTV);
													int FileId = SQLQuery.writeFile(s2, size, schedaSTV, f.type, true);
													
													list.addScheda(httpF.scheda);
													
													SQLQuery.writeEpisodeInfo(FileId, schedaSTV, f.episode, f.series);
												}
												else
												{
													int FileId = SQLQuery.writeFile(s2, size, cache2, f.type, true);
													SQLQuery.writeEpisodeInfo(FileId, cache2, f.episode, f.series);
												}
												
												break;
											default:
												break;
										}
									}
								};
								
								while (ThreadManager.getPoolSearchWait() >= ThreadManager.SearchPoolSize)
								{
									FunctionsUtils.sleep(10);
								}
								if (run)
									ThreadManager.submitPoolExecutorSearchIndicizer(check);
							}
							
						}
						
					} finally
					{
						endCheckList++;
						Log.log(Log.DEBUG, "Liste Finite:" + endCheckList);
					}
				}
			};
			
			ThreadManager.submitSearchTask(listRun);
			
		}
		while (ThreadManager.getPoolSearchWait() > 0)
		{
			FunctionsUtils.sleep(10);
		}
		Log.log(Log.INFO, "GiaraFilms End Search");
		list.loading = false;
	}
	
	public void StopService()
	{
		run = false;
		ThreadManager.resetPoolSearch();
	}
	
}
