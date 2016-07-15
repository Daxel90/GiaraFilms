package it.giara.phases;

import it.giara.analyze.enums.MainType;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.sql.SQLQueryScanService;
import it.giara.syncdata.NewServerQuery;
import it.giara.tmdb.GenereType;
import it.giara.utils.ThreadManager;

public class ListRequest
{
	GenereType genre;
	MainType type;
	AbstractFilmList list;
	public MyBoolean running;
	
	public ListRequest(GenereType g, MainType t, AbstractFilmList l)
	{
		list = l;
		genre = g;
		type = t;
		running = new MyBoolean(true);
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
		if (genre != null)
		{
			if (Settings.getParameter("servercollaborate").equals("1"))
				NewServerQuery.loadSchedeList(genre, type, list, running);
				
			else
				SQLQueryScanService.loadSchedeList(genre, type, list);
		}
		else
		{
			if (Settings.getParameter("servercollaborate").equals("1"))
				NewServerQuery.loadAllSchedeList(type, list, running);
			else
				SQLQueryScanService.loadAllSchedeList(type, list);
		}
		
	}
	
	public class MyBoolean
	{
		boolean value = false;
		
		public MyBoolean(boolean v)
		{
			value = v;
		}
		
		public boolean getValue()
		{
			return value;
		}
		
		public void setValue(boolean v)
		{
			value = v;
		}
		
	}
}
