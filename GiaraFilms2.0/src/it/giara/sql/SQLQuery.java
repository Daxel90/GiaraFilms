package it.giara.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import it.giara.analyze.enums.MainType;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.tmdb.GenereType;
import it.giara.tmdb.TMDBScheda;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class SQLQuery
{
	
	// ADD RECORD
	
	public static void initTable()
	{
		
		String query6 = "CREATE TABLE IF NOT EXISTS `EpisodeInfo` (" + "`IdFile`	INTEGER UNIQUE, "
				+ "`IdScheda`	INTEGER, " + "`Episode`	INTEGER, " + "`Serie`	INTEGER, " + "`LastUpdate`	INTEGER"
				+ ");";
		SQL.ExecuteQuery(query6);
		
		// new tables
		
		String query = "CREATE TABLE IF NOT EXISTS `new_files` ("
				+ "`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " + "`filename`	TEXT NOT NULL UNIQUE, "
				+ "`size` TEXT, " + "`schede_id`	INTEGER NOT NULL, " + "`type`	INTEGER NOT NULL, "
				+ "`last_update`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query);
		
		String query1 = "CREATE TABLE IF NOT EXISTS `new_cache` ("
				+ "`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " + "`search`	TEXT NOT NULL UNIQUE, "
				+ "`type`	INTEGER, " + "`id_result`	INTEGER, " + "`year`	INTEGER, "
				+ "`last_update`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query1);
		
		String query2 = "CREATE TABLE IF NOT EXISTS `new_schede` ("
				+ "`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, "
				+ "`scheda_id`	INTEGER NOT NULL,"
				+ "`title`	TEXT NOT NULL UNIQUE,"
				+ "`release_date`	TEXT,"
				+ "`poster`	TEXT,"
				+ "`background`	TEXT,"
				+ "`description`	TEXT,"
				+ "`genre_ids`	TEXT,"
				+ "`vote`	REAL,"
				+ "`type`	INTEGER NOT NULL,"
				+ "`fallback`	INTEGER NOT NULL,"
				+ "`last_update`	INTEGER NOT NULL"
				+ ");";
		SQL.ExecuteQuery(query2);
		
		String query4 = "CREATE TABLE IF NOT EXISTS `file_cache` ("
				+ "`id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " + "`filename`	TEXT NOT NULL UNIQUE, "
				+ "`lastupload`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query4);
	}
	
	// ----------NEW TABLES----------
	
	// ---cache---
	public synchronized static void write_new_cache(String search, MainType type, int ID, int year)
	{
		SQL.ExecuteQuery(
				"INSERT OR REPLACE INTO `new_cache`(`search`, `type`, `id_result`, `year`, `last_update`) VALUES ('"
						+ SQL.escape(search) + "', " + type.ID + ", " + ID + ", " + year + ", "
						+ FunctionsUtils.getTime() + ");");
	}
	
	// -1 noResult -2 check n exist
	public synchronized static int get_new_cache(String search, MainType type, int year)
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
	
	// ---file_cache---
	public synchronized static void write_file_cache(final String fileName)
	{
		SQL.ExecuteQuery("INSERT OR REPLACE INTO `file_cache`(`filename`, `lastupload`) VALUES ('"
				+ SQL.escape(fileName) + "', " + FunctionsUtils.getTime() + ");");
	}
	
	public synchronized static boolean uploaded_file_cache(String fileName)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `file_cache` WHERE `filename` = '" + SQL.escape(fileName)
				+ "' AND `lastupload` >= " + (FunctionsUtils.getTime() - 60 * 60 * 24 * 30) + ";");
		try
		{
			if (r.next())
			{
				return true;
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return false;
	}
	
	// ---new_schede---
	
	public synchronized static int writeScheda(final TMDBScheda i)
	{
		if (readScheda(i.ID, i.type) == null)
		{
			SQL.ExecuteQuery(
					"INSERT OR REPLACE INTO `new_schede`(`scheda_id`, `title`, `release_date`, `poster`, `background`, `description`, `genre_ids`, `vote`, `type`, `fallback`, `last_update`) VALUES "
							+ "(" + SQL.escape("" + i.ID) + "," + " '" + SQL.escape(i.title) + "'," + " '"
							+ SQL.escape(i.relese) + "'," + " '" + SQL.escape(i.poster) + "', " + " '"
							+ SQL.escape(i.back) + "', " + " '" + SQL.escape(i.desc) + "', " + " '"
							+ SQL.escape(i.getGeneriIDs()) + "', " + i.vote + ", " + i.type.ID + ", " + i.fallback_desc
							+ ", " + FunctionsUtils.getTime() + ");");
		}
		return i.ID;
	}
	
	public synchronized static TMDBScheda readScheda(int IdScheda, MainType t)
	{
		TMDBScheda scheda = new TMDBScheda();
		try
		{
			ResultSet r = SQL.FetchData(
					"SELECT * FROM `new_schede` WHERE `scheda_id` = " + IdScheda + " AND `type` = " + t.ID + ";");
			
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
	
	// -----------------------------
	
	// Files Table
	
	public synchronized static int write_File(final String fileName, final String size, final int IdScheda,
			final MainType t)
	{
		String sql = "INSERT INTO `new_files`(`filename`, `size`, `schede_id`, `type`, `last_update`) VALUES ('"
				+ SQL.escape(fileName) + "', '" + SQL.escape(size) + "', " + IdScheda + ", " + t.ID + ", "
				+ FunctionsUtils.getTime() + ");";
				
		if (existFile(fileName))
		{
			sql = "UPDATE `new_files` SET `type`= " + t.ID + " ,`schede_id`= " + IdScheda + " ,`last_update`= "
					+ FunctionsUtils.getTime() + " WHERE `filename` = '" + SQL.escape(fileName) + "'";
		}
		
		SQL.ExecuteQuery(sql);
		
		ResultSet r = SQL.FetchData("SELECT * FROM `new_files` WHERE `filename` = '" + SQL.escape(fileName) + "';");
		try
		{
			if (r.next())
			{
				return r.getInt("id");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -1;
	}
	
	public synchronized static boolean existFile(String fileName)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `new_files` WHERE `filename` = '" + SQL.escape(fileName) + "';");
		try
		{
			if (r.next())
			{
				return true;
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
			Log.log(Log.DB, "SELECT * FROM `new_files` WHERE `filename` = '" + SQL.escape(fileName) + "';");
		}
		return false;
	}
	
	public synchronized static String[] getFileNameAndSize(int id)
	{
		String[] result = new String[2];
		
		ResultSet r = SQL.FetchData("SELECT * FROM `new_files` WHERE `id` = " + id + ";");
		try
		{
			if (r.next())
			{
				result[0] = SQL.unescape(r.getString("filename"));
				result[1] = SQL.unescape(r.getString("size"));
				return result;
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		
		result[0] = "null";
		result[1] = "0";
		return result;
		
	}
	
	public synchronized static int getFileId(String fileName)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `new_files` WHERE `filename` = '" + SQL.escape(fileName) + "';");
		try
		{
			if (r.next())
			{
				return r.getInt("id");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -1;
	}
	
	public synchronized static int getSchedaId(String fileName)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `new_files` WHERE `filename` = '" + SQL.escape(fileName) + "';");
		try
		{
			if (r.next())
			{
				return r.getInt("schede_id");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -1;
	}
	
	public synchronized static int getFileType(String fileName)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `new_files` WHERE `filename` = '" + SQL.escape(fileName) + "';");
		try
		{
			if (r.next())
			{
				return r.getInt("type");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -1;
	}
	
	public synchronized static ArrayList<Integer> readFileListBySchedeId(int schedaID, MainType Type)
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		ResultSet r = SQL.FetchData(
				"SELECT * FROM `new_files` WHERE `schede_id` = " + schedaID + " AND `type` = " + Type.ID + ";");
		try
		{
			while (r.next())
			{
				list.add(r.getInt("id"));
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return list;
	}
	
	// -------------------
	
	// EpisodeInfo Table
	
	public synchronized static HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> readEpisodeInfoList(int serieID)
	{
		HashMap<Integer, HashMap<Integer, ArrayList<Integer>>> result = new HashMap<Integer, HashMap<Integer, ArrayList<Integer>>>();
		ResultSet r = SQL
				.FetchData("SELECT * FROM `EpisodeInfo` WHERE `IdScheda` = " + serieID + " ORDER BY `Episode`;");
		try
		{
			while (r.next())
			{
				int serie = r.getInt("Serie");
				int episode = r.getInt("Episode");
				int file = r.getInt("IdFile");
				
				if (!result.containsKey(serie))
					result.put(serie, new HashMap<Integer, ArrayList<Integer>>());
				if (!result.get(serie).containsKey(episode))
					result.get(serie).put(episode, new ArrayList<Integer>());
				result.get(serie).get(episode).add(file);
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return result;
	}
	
	public synchronized static void writeEpisodeInfo(int fileID, int IdSerie, int episode, int serie)
	{
		SQL.ExecuteQuery(
				"INSERT OR IGNORE INTO `EpisodeInfo`(`IdFile`, `IdScheda`, `Episode`, `Serie`, `LastUpdate`) VALUES ("
						+ fileID + ", " + IdSerie + ", " + episode + ", " + serie + ", " + FunctionsUtils.getTime()
						+ ");");
	}
	
	// -------------------
	
	// Fix
	
	public synchronized static void DbClear()
	{
		SQL.ExecuteQuery("DROP TABLE IF EXISTS `Files`");
		SQL.ExecuteQuery("DROP TABLE IF EXISTS `Schede`");
		SQL.ExecuteQuery("DROP TABLE IF EXISTS `CacheSearch`");
		SQL.ExecuteQuery("DROP TABLE IF EXISTS `EpisodeInfo`");
		
		SQL.ExecuteQuery("DROP TABLE IF EXISTS `new_cache`");
		SQL.ExecuteQuery("DROP TABLE IF EXISTS `new_schede`");
		SQL.ExecuteQuery("DROP TABLE IF EXISTS `new_cache_search`");
		SQL.ExecuteQuery("DROP TABLE IF EXISTS `file_cache`");
		SQL.ExecuteQuery("DROP TABLE IF EXISTS `new_files`");
		
		initTable();
	}
	// -------------------
}
