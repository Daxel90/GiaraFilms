package it.giara.phases;

import it.giara.analyze.FileInfo;
import it.giara.analyze.enums.MainType;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.http.HTTPList;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.sql.SQLQuery;
import it.giara.tmdb.TMDBScheda;
import it.giara.tmdb.api.TmdbApiSearchFilm;
import it.giara.tmdb.api.TmdbApiSearchTVSerie;
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
										FileInfo f = new FileInfo(s2, true);
										Log.log(Log.SearchService, s2);
										switch (f.type)
										{
											case Film:
												int cache = SQLQuery.get_new_cache(f.title, f.type, f.year);
												Log.log(Log.SearchService, "Film");
												if (cache == -1)
													return;
													
												if (cache == -2)
												{
													TmdbApiSearchFilm httpF = new TmdbApiSearchFilm(f.title, f.year);
													if (httpF.scheda == null)
													{
														SQLQuery.write_notupdate_new_cache(f.title, f.type, -1,
																f.year);
														SQLQuery.write_notupdate_File(s2, size, -1, f.type);
														break;
													}
													int schedaID = SQLQuery.writeScheda(httpF.scheda);
													
													list.addScheda(httpF.scheda);
													
													SQLQuery.write_notupdate_new_cache(f.title, f.type, schedaID,
															f.year);
													SQLQuery.write_notupdate_File(s2, size, schedaID, f.type);
												}
												else
												{
													SQLQuery.write_notupdate_File(s2, size, cache, f.type);
												}
												break;
											case SerieTV:
												int cache2 = SQLQuery.get_new_cache(f.title, f.type, f.year);
												Log.log(Log.SearchService, "SerieTV");
												Log.log(Log.SearchService, "cache2:" + cache2);
												if (cache2 == -1)
													return;
													
												if (cache2 == -2)
												{
													TmdbApiSearchTVSerie httpF = new TmdbApiSearchTVSerie(f.title,
															f.year);
													if (httpF.scheda == null)
													{
														SQLQuery.write_notupdate_new_cache(f.title, f.type, -1,
																f.year);
														break;
													}
													int schedaSTV = SQLQuery.writeScheda(httpF.scheda);
													
													SQLQuery.write_notupdate_new_cache(f.title, f.type,
															schedaSTV, f.year);
													int FileId = SQLQuery.write_notupdate_File(s2, size, schedaSTV,
															f.type);
															
													list.addScheda(httpF.scheda);
													
													SQLQuery.writeEpisodeInfo(FileId, schedaSTV, f.episode, f.series);
												}
												else
												{
													int FileId = SQLQuery.write_notupdate_File(s2, size, cache2,
															f.type);
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
						Log.log(Log.SearchService, "Liste Finite:" + endCheckList);
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
