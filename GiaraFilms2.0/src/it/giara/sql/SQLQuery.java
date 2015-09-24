package it.giara.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

import it.giara.analyze.enums.MainType;
import it.giara.schede.PreSchedaFilm;
import it.giara.schede.PreSchedaTVSerie;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class SQLQuery
{
	
	// ADD RECORD
	
	public static void initTable()
	{
		String query = "CREATE TABLE IF NOT EXISTS `File` ("
				+ "`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," + "`FileName`	TEXT NOT NULL UNIQUE,"
				+ "`LastUpdate`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query);
		
		String query2 = "CREATE TABLE IF NOT EXISTS `SchedaFilm` ("
				+ "`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," + "`Titolo`	TEXT NOT NULL UNIQUE,"
				+ "`Link`	TEXT NOT NULL UNIQUE," + "`Image`	TEXT," + "`Anno`	INTEGER," + "`Regia`	TEXT,"
				+ "`Nazionalita`	TEXT," + "`Generi`	TEXT," + "`LastUpdate`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query2);
		
		String query3 = "CREATE TABLE IF NOT EXISTS `FileInfo` (" + "`IDFile`	INTEGER NOT NULL,"
				+ "`Type`	INTEGER NOT NULL," + "`IdSection`	INTEGER NOT NULL," + "`LastUpdate`	INTEGER NOT NULL"
				+ ");";
		SQL.ExecuteQuery(query3);
		
		String query4 = "CREATE TABLE IF NOT EXISTS `SchedaTelefilm` ("
				+ "`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," + "`Titolo`	TEXT NOT NULL UNIQUE,"
				+ "`Link`	TEXT NOT NULL UNIQUE," + "`Image`	TEXT," + "`Anno`	INTEGER," + "`Nazionalita`	TEXT,"
				+ "`Generi`	TEXT," + "`LastUpdate`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query4);
		
		String query5 = "CREATE TABLE IF NOT EXISTS `CacheSearch` ("
				+ "`ID`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE," + "`Search`	TEXT NOT NULL UNIQUE,"
				+ "`Type`	INTEGER," + "`IdResult`	INTEGER," + "`LastUpdate`	INTEGER NOT NULL" + ");";
		SQL.ExecuteQuery(query5);
		
		String query6 = "CREATE TABLE IF NOT EXISTS `EpisodeInfo` (" + "`IdFile`	INTEGER UNIQUE,"
				+ "`IdSerie`	INTEGER," + "`Episode`	INTEGER," + "`Serie`	INTEGER," + "`LastUpdate`	INTEGER"
				+ ");";
		SQL.ExecuteQuery(query6);
	}
	
	public synchronized static int writePreSchedaFilm(PreSchedaFilm i)
	{
		SQL.ExecuteQuery(
				"INSERT OR IGNORE INTO `SchedaFilm`(`Titolo`, `Link`, `Image`, `Anno`, `Regia`, `Nazionalita`, `Generi`, `LastUpdate`) VALUES (\""
						+ SQL.escape(i.Titolo) + "\", \"" + i.link + "\", \"" + i.smallImage + "\", " + i.anno + ", \""
						+ SQL.escape(i.regia) + "\", \"" + SQL.escape(i.nazionalita) + "\", \""
						+ SQL.escape(i.getGeneri()) + "\", " + FunctionsUtils.getTime() + ");");
		ResultSet r = SQL.FetchData("SELECT * FROM `SchedaFilm` WHERE `Link` = \"" + i.link + "\";");
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
	
	public synchronized static int writePreSchedaTvSeries(PreSchedaTVSerie i)
	{
		SQL.ExecuteQuery(
				"INSERT OR IGNORE INTO `SchedaTelefilm`(`Titolo`, `Link`, `Image`, `Anno`, `Nazionalita`, `Generi`, `LastUpdate`) VALUES (\""
						+ SQL.escape(i.Titolo) + "\", \"" + i.link + "\", \"" + i.smallImage + "\", " + i.anno + ", \""
						+ SQL.escape(i.nazionalita) + "\", \"" + SQL.escape(i.getGeneri()) + "\", "
						+ FunctionsUtils.getTime() + ");");
		ResultSet r = SQL.FetchData("SELECT * FROM `SchedaTelefilm` WHERE `Link` = \"" + i.link + "\";");
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
	
	public synchronized static PreSchedaTVSerie readPreSchedaTVSerie(int IdSection)
	{
		PreSchedaTVSerie scheda = new PreSchedaTVSerie();
		ResultSet r = SQL.FetchData("SELECT * FROM `SchedaTelefilm` WHERE `ID` = " + IdSection + ";");
		try
		{
			if (r.next())
			{
				scheda.Titolo = r.getNString("Titolo");
				scheda.link = r.getNString("Link");
				scheda.smallImage = r.getNString("Image");
				scheda.anno = r.getInt("Anno");
				scheda.nazionalita = r.getNString("Nazionalita");
				scheda.setGeneri(r.getNString("Nazionalita"));
			}
			else
				return null;
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return scheda;
	}
	
	public synchronized static void writeCacheSearch(String search, MainType type, int ID)
	{
		SQL.ExecuteQuery("INSERT OR REPLACE INTO `CacheSearch`(`Search`, `Type`, `IdResult`, `LastUpdate`) VALUES (\""
				+ SQL.escape(search) + "\", " + type.ID + ", " + ID + ", " + FunctionsUtils.getTime() + ");");
	}
	
	public synchronized static int getCacheSearch(String search, MainType type)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `CacheSearch` WHERE `Search` = \"" + SQL.escape(search)
				+ "\" AND Type = " + type.ID + ";");
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
	
	public synchronized static int writeFile(String fileName)
	{
		SQL.ExecuteQuery("INSERT OR IGNORE INTO `File`(`FileName`, `LastUpdate`) VALUES (\"" + SQL.escape(fileName)
				+ "\", " + FunctionsUtils.getTime() + ");");
				
		ResultSet r = SQL.FetchData("SELECT * FROM `File` WHERE `FileName` = \"" + SQL.escape(fileName) + "\";");
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
		ResultSet r = SQL.FetchData("SELECT * FROM `File` WHERE `FileName` = \"" + SQL.escape(fileName) + "\";");
		try
		{
			if (r.next())
			{
				return true;
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
			Log.log(Log.DB, "SELECT * FROM `File` WHERE `FileName` = \"" + SQL.escape(fileName) + "\";");
		}
		return false;
	}
	
	public synchronized static int getFile(String fileName)
	{
		ResultSet r = SQL.FetchData("SELECT * FROM `File` WHERE `FileName` = \"" + SQL.escape(fileName) + "\";");
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
	
	public synchronized static void writeFileInfo(int fileID, MainType t, int schedaId)
	{
		SQL.ExecuteQuery("INSERT OR IGNORE INTO `FileInfo`(`IDFile`, `Type`, `IdSection`, `LastUpdate`) VALUES ("
				+ fileID + ", " + t.ID + ", " + schedaId + ", " + FunctionsUtils.getTime() + ");");
	}
	
	public synchronized static int[] readFileInfo(int fileID)
	{
		int[] result = new int[2];
		ResultSet r = SQL.FetchData("SELECT * FROM `FileInfo` WHERE `IDFile` = " + fileID + ";");
		try
		{
			if (r.next())
			{
				result[0] = r.getInt("Type");
				result[1] = r.getInt("IdSection");
				return result;
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		result[0] = -1;
		result[1] = -1;
		return result;
	}
	
	public synchronized static void writeEpisodeInfo(int fileID, int IdSerie, int episode, int serie)
	{
		SQL.ExecuteQuery(
				"INSERT OR IGNORE INTO `EpisodeInfo`(`IdFile`, `IdSerie`, `Episode`, `Serie`, `LastUpdate`) VALUES ("
						+ fileID + ", " + IdSerie + ", " + episode + ", " + serie + ", " + FunctionsUtils.getTime()
						+ ");");
	}
	
	public synchronized static int getFilmNumber()
	{
		ResultSet r = SQL.FetchData("SELECT count(ID) FROM SchedaFilm;");
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
		ResultSet r = SQL.FetchData("SELECT count(ID) FROM File;");
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
	
}
