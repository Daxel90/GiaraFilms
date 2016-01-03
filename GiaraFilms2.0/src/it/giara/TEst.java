package it.giara;

import it.giara.analyze.enums.MainType;
import it.giara.phases.Settings;
import it.giara.source.ListLoader;
import it.giara.sql.SQL;
import it.giara.sql.SQLQuery;
import it.giara.syncdata.ServerQuery;
import it.giara.utils.HttpPost;
import it.giara.utils.Log;

public class TEst
{
	public static void main(String[] args)
	{
		
		SQL.connect();
		Settings.init();
		ListLoader.loadSources();
		ServerQuery.load150News();
//		ServerQuery.sendScheda(SQLQuery.readScheda(674, MainType.Film));
//		Log.log(Log.DEBUG, ServerQuery.requestScheda(676, MainType.Film));
		
//		ServerQuery.sendFileInfo("testtest", "350M", 61387, MainType.Film);
//		
//		ServerQuery.loadUntil(Integer.parseInt(Settings.getParameter("lastserversync")));
		
	}
}