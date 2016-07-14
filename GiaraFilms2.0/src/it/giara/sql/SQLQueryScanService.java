package it.giara.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.giara.analyze.enums.MainType;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class SQLQueryScanService
{
	public static void initScanServiceTable()
	{
		String query5 = "CREATE TABLE IF NOT EXISTS `new_cache` ("
				+ "`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " + "`search`	TEXT NOT NULL UNIQUE, "
				+ "`type`	INTEGER, " + "`id_result`	INTEGER, " + "`year`	INTEGER, "
				+ "`last_update`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query5);
		
	}
	
	// ---Cache---
	
	public synchronized static void writeCacheSearch(String search, MainType type, int ID, int year)
	{
		SQL.ExecuteQuery(
				"INSERT OR REPLACE INTO `new_cache`(`search`, `type`, `id_result`, `year`, `last_update`) VALUES ('"
						+ SQL.escape(search) + "', " + type.ID + ", " + ID + ", " + year + ", "
						+ FunctionsUtils.getTime() + ");");
	}
	
	// -1 noResult -2 check -n exist
	public synchronized static int getCacheSearch(String search, MainType type, int year)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `new_cache` WHERE `search` = '" + SQL.escape(search)
				+ "' AND type = " + type.ID + " AND year = " + year + " ;");
		try
		{
			if (r.next())
			{
				if (r.getInt("id_result") == -1 && r.getInt("last_update") + 60 + 60 + 24 >= FunctionsUtils.getTime())
				{
					return -2;
				}
				return r.getInt("id_result");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -2;
	}
	
}
