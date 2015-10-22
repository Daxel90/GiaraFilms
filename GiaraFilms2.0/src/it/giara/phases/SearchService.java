package it.giara.phases;

import it.giara.analyze.FileInfo;
import it.giara.analyze.enums.MainType;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.http.HTTPList;
import it.giara.http.HTTPSearchFilm;
import it.giara.http.HTTPSearchTVSerie;
import it.giara.schede.PreSchedaFilm;
import it.giara.schede.PreSchedaTVSerie;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.sql.SQLQuery;
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
								int IDFile = SQLQuery.getFileId(s2);
								int[] FileInfo = SQLQuery.readFileInfoWithFileID(IDFile);
								if (FileInfo[0] == MainType.Film.ID)
								{
									PreSchedaFilm film = SQLQuery.readPreSchedaFilm(FileInfo[1]);
									if (film != null)
										list.addPreSchedaFilm(film);
								}
								else if (FileInfo[0] == MainType.SerieTV.ID)
								{
									PreSchedaTVSerie tvserie = SQLQuery.readPreSchedaTVSerie(FileInfo[1]);
									if (tvserie != null)
										list.addPreSchedaTVSerie(tvserie);
								}
							}
							else
							{
								Runnable check = new Runnable()
								{
									@Override
									public void run()
									{
										int fileID = SQLQuery.writeFile(s2, size);
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
													httpF.scheda.IdDb = schedaF;
													list.addPreSchedaFilm(httpF.scheda);
													
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
													httpF.scheda.IdDb = schedaSTV;
													SQLQuery.writeCacheSearch(f.title, f.type, schedaSTV);
													SQLQuery.writeFileInfo(fileID, f.type, schedaSTV);
													
													list.addPreSchedaTVSerie(httpF.scheda);
													
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
								
								while (ThreadManager.getPoolSearchWait() >= ThreadManager.poolSize * 2)
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
		ThreadManager.resetThreadSearch();
	}
	
}
