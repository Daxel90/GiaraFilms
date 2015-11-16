package it.giara.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import it.giara.analyze.enums.MainType;
import it.giara.tmdb.schede.TMDBScheda;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class SQLQuery
{
	
	// ADD RECORD
	
	public static void initTable()
	{
		String query = "CREATE TABLE IF NOT EXISTS `Files` ("
				+ "`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " 
				+ "`FileName`	TEXT NOT NULL UNIQUE, "
				+ "`Size` TEXT, " 
				+ "`IdScheda`	INTEGER NOT NULL, "
				+ "`Type`	INTEGER NOT NULL, " 
				+ "`LastUpdate`	INTEGER NOT NULL" 
				+ ");";
		SQL.ExecuteQuery(query);
		
		String query2 = "CREATE TABLE IF NOT EXISTS `Schede` ("
				+ "`ID`	INTEGER NOT NULL PRIMARY KEY, " 
				+ "`Title`	TEXT NOT NULL UNIQUE, "
				+ "`Relese`	TEXT, " 
				+ "`Poster`	TEXT, " 
				+ "`Back`	TEXT, "
				+ "`Desc`	TEXT, " 
				+ "`GenID`	TEXT, " 
				+ "`Vote`	REAL, "
				+ "`Type`	INTEGER NOT NULL, " 
				+ "`LastUpdate`	INTEGER NOT NULL" 
				+ ");";
		SQL.ExecuteQuery(query2);
		
		String query5 = "CREATE TABLE IF NOT EXISTS `CacheSearch` ("
				+ "`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE, " 
				+ "`Search`	TEXT NOT NULL UNIQUE, "
				+ "`Type`	INTEGER, " 
				+ "`IdResult`	INTEGER, " 
				+ "`LastUpdate`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query5);
		
		String query6 = "CREATE TABLE IF NOT EXISTS `EpisodeInfo` (" 
		+ "`IdFile`	INTEGER UNIQUE, "
		+ "`IdScheda`	INTEGER, " 
		+ "`Episode`	INTEGER, " 
		+ "`Serie`	INTEGER, " 
		+ "`LastUpdate`	INTEGER"
				+ ");";
		SQL.ExecuteQuery(query6);
	}
	
	// Files Table
	
	public synchronized static int writeFile(String fileName, String size, int IdScheda, MainType t)
	{
		SQL.ExecuteQuery("INSERT OR IGNORE INTO `Files`(`FileName`, `Size`, `IdScheda`, `Type`, `LastUpdate`) VALUES ('" + SQL.escape(fileName)
				+ "', '"+SQL.escape(size)+"', "+IdScheda+", "+t.ID+", " + FunctionsUtils.getTime() + ");");
				
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
	
	//-------------------
	
	// Schede Table
	
	public synchronized static int writeScheda(TMDBScheda i)
	{
		if(readScheda(i.ID,i.type) == null)
		SQL.ExecuteQuery(
				"INSERT OR IGNORE INTO `Schede`(`ID`, `Title`, `Relese`, `Poster`, `Back`, `Desc`, `GenID`, `Vote`, `Type`, `LastUpdate`) VALUES "
				+ "("+ SQL.escape(""+i.ID) + ","
						+ " '" + SQL.escape(i.title) + "',"
						+ " '" + SQL.escape(i.relese) + "',"
						+ " '" + SQL.escape(i.poster) + "', "
						+ " '" + SQL.escape(i.back) + "', "
						+ " '" + SQL.escape(i.desc) + "', "
						+ " '" + SQL.escape(i.getGeneriIDs()) + "', "
						+ i.vote + ", "
						+ i.type.ID + ", "
						+ FunctionsUtils.getTime() + ");");
		
		return i.ID;
	}
	
	public synchronized static TMDBScheda readScheda(int IdScheda, MainType t)
	{
		TMDBScheda scheda = new TMDBScheda();
		ResultSet r = SQL.FetchData("SELECT * FROM `Schede` WHERE `ID` = " + IdScheda + " AND `Type` = "+t.ID+";");
		try
		{
			if (r.next())
			{
				scheda.ID = r.getInt("ID");
				scheda.title = SQL.unescape(r.getString("Title"));
				scheda.relese = SQL.unescape(r.getString("Relese"));
				scheda.poster = SQL.unescape(r.getString("Poster"));
				scheda.back = SQL.unescape(r.getString("Back"));
				scheda.desc = SQL.unescape(r.getString("Desc"));
				scheda.setGeneriIDs(SQL.unescape(r.getString("GenID")));
				scheda.vote = r.getDouble("Vote");
				scheda.type = MainType.getMainTypeByID(r.getInt("Type"));
			}
			else
				return null;
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return scheda;
	}
	
	//-------------------
	
	// CacheSearch Table
	
	public synchronized static void writeCacheSearch(String search, MainType type, int ID)
	{
		SQL.ExecuteQuery("INSERT OR REPLACE INTO `CacheSearch`(`Search`, `Type`, `IdResult`, `LastUpdate`) VALUES ('"
				+ SQL.escape(search) + "', " + type.ID + ", " + ID + ", " + FunctionsUtils.getTime() + ");");
	}
	
	public synchronized static int getCacheSearch(String search, MainType type)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `CacheSearch` WHERE `Search` = '" + SQL.escape(search)
				+ "' AND Type = " + type.ID + ";");
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

	//-------------------
	
	// EpisodeInfo Table
	
	public synchronized static HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> readEpisodeInfoList(int serieID)
	{
		HashMap<Integer,HashMap<Integer,ArrayList<Integer>>> result = new HashMap<Integer,HashMap<Integer,ArrayList<Integer>>>();
		ResultSet r = SQL.FetchData("SELECT * FROM `EpisodeInfo` WHERE `IdScheda` = " + serieID + ";");
		try
		{
			while (r.next())
			{
				int serie = r.getInt("Serie");
				int episode = r.getInt("Episode");
				int file = r.getInt("IdFile");
				
				if(!result.containsKey(serie))
					result.put(serie, new HashMap<Integer,ArrayList<Integer>>());
				if(!result.get(serie).containsKey(episode))
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
	
	//-------------------
	
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
	
	//-------------------
}
