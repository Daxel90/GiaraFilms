package it.giara.phases;

import it.giara.analyze.enums.MainType;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.syncdata.ServerQuery;
import it.giara.tmdb.GenereType;
import it.giara.utils.ThreadManager;

public class ListRequest
{
	GenereType genre;
	MainType type;
	AbstractFilmList list;
	
	public ListRequest(GenereType g, MainType t, AbstractFilmList l)
	{
		list = l;
		genre = g;
		type = t;
		Runnable r = new Runnable()
		{
			public void run()
			{
				start();
			}
		};
		ThreadManager.submitCacheTask(r);
	}
	
	public void start()
	{
		if(genre != null)
		{
			ServerQuery.loadSchedeList(genre,type,list);
		}
		
	}
	
}
