package it.giara.sql;

import it.giara.analyze.enums.MainType;

public class SQLQueryScanService
{
	
	// ---Cache---
	
	public synchronized static void writeCacheSearch(String search, MainType type, int ID, int year)
	{
		// TODO
	}
	
	//-1 notPresent -2 notFound n exist
	public synchronized static int getCacheSearch(String search, MainType type, int year)
	{
		// TODO
		return -1;
	}
	
}
