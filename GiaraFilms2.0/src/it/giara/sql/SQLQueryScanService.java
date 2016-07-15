package it.giara.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.giara.analyze.enums.MainType;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.tmdb.GenereType;
import it.giara.tmdb.schede.TMDBScheda;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class SQLQueryScanService
{
	public static void initScanServiceTable()
	{
		String query1 = "CREATE TABLE IF NOT EXISTS `new_cache` ("
				+ "`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " + "`search`	TEXT NOT NULL UNIQUE, "
				+ "`type`	INTEGER, " + "`id_result`	INTEGER, " + "`year`	INTEGER, "
				+ "`last_update`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query1);
		
		String query2 = "CREATE TABLE IF NOT EXISTS `new_schede` (`scheda_id`	INTEGER NOT NULL,`title`	TEXT NOT NULL UNIQUE,`release_date`	TEXT,`poster`	TEXT,`background`	TEXT,`description`	TEXT,`genre_ids`	TEXT,`vote`	REAL,`type`	INTEGER NOT NULL,`fallback`	INTEGER NOT NULL,`last_update`	INTEGER NOT NULL,PRIMARY KEY(scheda_id));";
		SQL.ExecuteQuery(query2);
		
	}
	
	// ---Cache---
	
	public synchronized static void writeCacheSearch(String search, MainType type, int ID, int year)
	{
		SQL.ExecuteQuery(
				"INSERT OR REPLACE INTO `new_cache`(`search`, `type`, `id_result`, `year`, `last_update`) VALUES ('"
						+ SQL.escape(search) + "', " + type.ID + ", " + ID + ", " + year + ", "
						+ FunctionsUtils.getTime() + ");");
	}
	
	// -1 noResult -2 check n exist
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
	
	// ---Schede Table---
	
	public synchronized static int writeScheda(final TMDBScheda i)
	{
		if (readScheda(i.ID, i.type) == null)
		{
			SQL.ExecuteQuery(
					"INSERT OR IGNORE INTO `new_schede`(`scheda_id`, `title`, `release_date`, `poster`, `background`, `description`, `genre_ids`, `vote`, `type`, `fallback`, `last_update`) VALUES "
							+ "(" + SQL.escape("" + i.ID) + "," + " '" + SQL.escape(i.title) + "'," + " '"
							+ SQL.escape(i.relese) + "'," + " '" + SQL.escape(i.poster) + "', " + " '"
							+ SQL.escape(i.back) + "', " + " '" + SQL.escape(i.desc) + "', " + " '"
							+ SQL.escape(i.getGeneriIDs()) + "', " + i.vote + ", " + i.type.ID + ", "
							+ i.fallback_desc + ", " + FunctionsUtils.getTime() + ");");
		}
		return i.ID;
	}
	
	public synchronized static TMDBScheda readScheda(int IdScheda, MainType t)
	{
		TMDBScheda scheda = new TMDBScheda();
		ResultSet r = SQL.FetchData(
				"SELECT * FROM `new_schede` WHERE `scheda_id` = " + IdScheda + " AND `type` = " + t.ID + ";");
		try
		{
			if (r.next())
			{
				scheda.ID = r.getInt("scheda_id");
				scheda.title = SQL.unescape(r.getString("title"));
				scheda.relese = SQL.unescape(r.getString("release_date"));
				scheda.poster = SQL.unescape(r.getString("poster"));
				scheda.back = SQL.unescape(r.getString("background"));
				scheda.desc = SQL.unescape(r.getString("description"));
				scheda.setGeneriIDs(SQL.unescape(r.getString("genre_ids")));
				scheda.vote = r.getDouble("vote");
				scheda.type = MainType.getMainTypeByID(r.getInt("type"));
				scheda.fallback_desc = r.getInt("fallback");
			}
			else
				return null;
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return scheda;
	}
	
	public synchronized static void loadSchedeList(GenereType g, MainType t, AbstractFilmList l)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `new_schede` WHERE `type` = " + t.ID + " AND `genre_ids` LIKE '%"
				+ g.Id + "%' ORDER BY `release_date` DESC;");
		try
		{
			while (r.next())
			{
				TMDBScheda scheda = new TMDBScheda();
				scheda.ID = r.getInt("scheda_id");
				scheda.title = SQL.unescape(r.getString("title"));
				scheda.relese = SQL.unescape(r.getString("release_date"));
				scheda.poster = SQL.unescape(r.getString("poster"));
				scheda.back = SQL.unescape(r.getString("background"));
				scheda.desc = SQL.unescape(r.getString("description"));
				scheda.setGeneriIDs(SQL.unescape(r.getString("genre_ids")));
				scheda.vote = r.getDouble("vote");
				scheda.type = MainType.getMainTypeByID(r.getInt("type"));
				scheda.fallback_desc = r.getInt("fallback");
				
				l.addScheda(scheda);
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
	}
	
	public synchronized static void loadAllSchedeList(MainType t, AbstractFilmList l)
	{
		ResultSet r = SQL
				.FetchData("SELECT * FROM `new_schede` WHERE `type` = " + t.ID + " ORDER BY `release_date` DESC;");
		try
		{
			while (r.next())
			{
				TMDBScheda scheda = new TMDBScheda();
				scheda.ID = r.getInt("scheda_id");
				scheda.title = SQL.unescape(r.getString("title"));
				scheda.relese = SQL.unescape(r.getString("release_date"));
				scheda.poster = SQL.unescape(r.getString("poster"));
				scheda.back = SQL.unescape(r.getString("background"));
				scheda.desc = SQL.unescape(r.getString("description"));
				scheda.setGeneriIDs(SQL.unescape(r.getString("genre_ids")));
				scheda.vote = r.getDouble("vote");
				scheda.type = MainType.getMainTypeByID(r.getInt("type"));
				scheda.fallback_desc = r.getInt("fallback");
				
				l.addScheda(scheda);
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
	}
	
}
