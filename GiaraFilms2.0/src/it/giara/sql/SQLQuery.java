package it.giara.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import it.giara.analyze.enums.MainType;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class SQLQuery
{
	
	// ADD RECORD
	
	public static void initTable()
	{
		String query = "CREATE TABLE IF NOT EXISTS `Files` ("
				+ "`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " + "`FileName`	TEXT NOT NULL UNIQUE, "
				+ "`Size` TEXT, " + "`IdScheda`	INTEGER NOT NULL, " + "`Type`	INTEGER NOT NULL, "
				+ "`LastUpdate`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query);
		
		String query5 = "CREATE TABLE IF NOT EXISTS `CacheSearch` ("
				+ "`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " + "`Search`	TEXT NOT NULL UNIQUE, "
				+ "`Type`	INTEGER, " + "`IdResult`	INTEGER, " + "`Year`	INTEGER, "
				+ "`LastUpdate`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query5);
		
		String query6 = "CREATE TABLE IF NOT EXISTS `EpisodeInfo` (" + "`IdFile`	INTEGER UNIQUE, "
				+ "`IdScheda`	INTEGER, " + "`Episode`	INTEGER, " + "`Serie`	INTEGER, " + "`LastUpdate`	INTEGER"
				+ ");";
		SQL.ExecuteQuery(query6);
	}
	
	// Files Table
	
	public synchronized static int writeFile(final String fileName, final String size, final int IdScheda,
			final MainType t, boolean collaborate)
	{
		
		SQL.ExecuteQuery("INSERT OR IGNORE INTO `Files`(`FileName`, `Size`, `IdScheda`, `Type`, `LastUpdate`) VALUES ('"
				+ SQL.escape(fileName) + "', '" + SQL.escape(size) + "', " + IdScheda + ", " + t.ID + ", "
				+ FunctionsUtils.getTime() + ");");
				
		ResultSet r = SQL.FetchData("SELECT * FROM `Files` WHERE `FileName` = '" + SQL.escape(fileName) + "';");
		try
		{
			if (r.next())
			{
				return r.getInt("ID");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -1;
	}
	
	public synchronized static boolean existFile(String fileName)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `Files` WHERE `FileName` = '" + SQL.escape(fileName) + "';");
		try
		{
			if (r.next())
			{
				return true;
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
			Log.log(Log.DB, "SELECT * FROM `Files` WHERE `FileName` = '" + SQL.escape(fileName) + "';");
		}
		return false;
	}
	
	public synchronized static String[] getFileNameAndSize(int id)
	{
		String[] result = new String[2];
		
		ResultSet r = SQL.FetchData("SELECT * FROM `Files` WHERE `ID` = " + id + ";");
		try
		{
			if (r.next())
			{
				result[0] = SQL.unescape(r.getString("FileName"));
				result[1] = SQL.unescape(r.getString("Size"));
				return result;
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		
		result[0] = "";
		result[1] = "";
		return result;
		
	}
	
	public synchronized static int getFileId(String fileName)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `Files` WHERE `FileName` = '" + SQL.escape(fileName) + "';");
		try
		{
			if (r.next())
			{
				return r.getInt("ID");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -1;
	}
	
	public synchronized static int getSchedaId(String fileName)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `Files` WHERE `FileName` = '" + SQL.escape(fileName) + "';");
		try
		{
			if (r.next())
			{
				return r.getInt("IdScheda");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -1;
	}
	
	public synchronized static int getFileType(String fileName)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `Files` WHERE `FileName` = '" + SQL.escape(fileName) + "';");
		try
		{
			if (r.next())
			{
				return r.getInt("Type");
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
		ResultSet r = SQL
				.FetchData("SELECT * FROM `Files` WHERE `IdScheda` = " + schedaID + " AND `Type` = " + Type.ID + ";");
		try
		{
			while (r.next())
			{
				list.add(r.getInt("ID"));
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return list;
	}
	
	// -------------------
	
	// CacheSearch Table
	
	public synchronized static void writeCacheSearch(String search, MainType type, int ID, int year)
	{
		SQL.ExecuteQuery(
				"INSERT OR REPLACE INTO `CacheSearch`(`Search`, `Type`, `IdResult`, `Year`, `LastUpdate`) VALUES ('"
						+ SQL.escape(search) + "', " + type.ID + ", " + ID + ", " + year + ", "
						+ FunctionsUtils.getTime() + ");");
	}
	
	public synchronized static int getCacheSearch(String search, MainType type, int year)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `CacheSearch` WHERE `Search` = '" + SQL.escape(search)
				+ "' AND Type = " + type.ID + " AND Year = " + year + " ;");
		try
		{
			if (r.next())
			{
				if (r.getInt("IdResult") == -1 && r.getInt("LastUpdate") + 60 + 60 + 24 >= FunctionsUtils.getTime())
				{
					return -2;
				}
				return r.getInt("IdResult");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -1;
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
	
	// General Info Query
	
	public synchronized static int getFilmNumber()
	{
		ResultSet r = SQL.FetchData("SELECT count(ID) FROM Schede WHERE `Type` = " + MainType.Film.ID + ";");
		try
		{
			if (r.next())
			{
				return r.getInt("count(ID)");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -1;
	}
	
	public synchronized static int getFileNumber()
	{
		ResultSet r = SQL.FetchData("SELECT count(ID) FROM Files;");
		try
		{
			if (r.next())
			{
				return r.getInt("count(ID)");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -1;
	}
	
	public synchronized static int getEpisodeNumbers()
	{
		ResultSet r = SQL.FetchData("SELECT count(IdFile) FROM EpisodeInfo;");
		try
		{
			if (r.next())
			{
				return r.getInt("count(IdFile)");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return -1;
	}
	
	// -------------------
	
	// Fix
	
	public synchronized static void DbClear()
	{
		SQL.ExecuteQuery("DROP TABLE `Files`");
		SQL.ExecuteQuery("DROP TABLE `Schede`");
		SQL.ExecuteQuery("DROP TABLE `CacheSearch`");
		SQL.ExecuteQuery("DROP TABLE `EpisodeInfo`");
		initTable();
	}
	// -------------------
}
