package it.giara.sql;

import it.giara.analyze.enums.MainType;
import it.giara.schede.PreSchedaFilm;
import it.giara.schede.PreSchedaTVSerie;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLQuery
{
  public static void initTable()
  {
    String query = "CREATE TABLE IF NOT EXISTS `File` (`ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,`FileName`\tTEXT NOT NULL UNIQUE,`LastUpdate`\tINTEGER NOT NULL);";

    SQL.ExecuteQuery(query);

    String query2 = "CREATE TABLE IF NOT EXISTS `SchedaFilm` (`ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,`Titolo`\tTEXT NOT NULL UNIQUE,`Link`\tTEXT NOT NULL UNIQUE,`Image`\tTEXT,`Anno`\tINTEGER,`Regia`\tTEXT,`Nazionalita`\tTEXT,`Generi`\tTEXT,`LastUpdate`\tINTEGER NOT NULL);";

    SQL.ExecuteQuery(query2);

    String query3 = "CREATE TABLE IF NOT EXISTS `FileInfo` (`IDFile`\tINTEGER NOT NULL,`Type`\tINTEGER NOT NULL,`IdSection`\tINTEGER NOT NULL,`LastUpdate`\tINTEGER NOT NULL);";

    SQL.ExecuteQuery(query3);

    String query4 = "CREATE TABLE IF NOT EXISTS `SchedaTelefilm` (`ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,`Titolo`\tTEXT NOT NULL UNIQUE,`Link`\tTEXT NOT NULL UNIQUE,`Image`\tTEXT,`Anno`\tINTEGER,`Nazionalita`\tTEXT,`Generi`\tTEXT,`LastUpdate`\tINTEGER NOT NULL);";

    SQL.ExecuteQuery(query4);

    String query5 = "CREATE TABLE IF NOT EXISTS `CacheSearch` (`ID`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,`Search`\tTEXT NOT NULL UNIQUE,`Type`\tINTEGER,`IdResult`\tINTEGER);";

    SQL.ExecuteQuery(query5);

    String query6 = "CREATE TABLE IF NOT EXISTS `EpisodeInfo` (`IdFile`\tINTEGER UNIQUE,`IdSerie`\tINTEGER,`Episode`\tINTEGER,`Serie`\tINTEGER,`LastUpdate`\tINTEGER);";

    SQL.ExecuteQuery(query6);
  }

  public static synchronized int writePreSchedaFilm(PreSchedaFilm i)
  {
    SQL.ExecuteQuery("INSERT OR IGNORE INTO `SchedaFilm`(`Titolo`, `Link`, `Image`, `Anno`, `Regia`, `Nazionalita`, `Generi`, `LastUpdate`) VALUES (\"" + SQL.escape(i.Titolo) + "\", \"" + i.link + "\", \"" + i.smallImage + "\", " + i.anno + ", \"" + SQL.escape(i.regia) + "\", \"" + SQL.escape(i.nazionalita) + "\", \"" + SQL.escape(i.getGeneri()) + "\", " + FunctionsUtils.getTime() + ");");
    ResultSet r = SQL.FetchData("SELECT * FROM `SchedaFilm` WHERE `Link` = \"" + i.link + "\";");
    try {
      if (r.next())
      {
        return r.getInt("ID");
      }
    } catch (SQLException e) {
      Log.stack(Log.DB, e);
    }
    return -1;
  }

  public static synchronized int writePreSchedaTvSeries(PreSchedaTVSerie i)
  {
    SQL.ExecuteQuery("INSERT OR IGNORE INTO `SchedaTelefilm`(`Titolo`, `Link`, `Image`, `Anno`, `Nazionalita`, `Generi`, `LastUpdate`) VALUES (\"" + SQL.escape(i.Titolo) + "\", \"" + i.link + "\", \"" + i.smallImage + "\", " + i.anno + ", \"" + SQL.escape(i.nazionalita) + "\", \"" + SQL.escape(i.getGeneri()) + "\", " + FunctionsUtils.getTime() + ");");
    ResultSet r = SQL.FetchData("SELECT * FROM `SchedaTelefilm` WHERE `Link` = \"" + i.link + "\";");
    try {
      if (r.next())
      {
        return r.getInt("ID");
      }
    } catch (SQLException e) {
      Log.stack(Log.DB, e);
    }
    return -1;
  }

  public static synchronized void writeCacheSearch(String search, MainType type, int ID)
  {
    SQL.ExecuteQuery("INSERT OR IGNORE INTO `CacheSearch`(`Search`, `Type`, `IdResult`) VALUES (\"" + SQL.escape(search) + "\", " + type.ID + ", " + ID + ");");
  }

  public static synchronized int getCacheSearch(String search, MainType type)
  {
    ResultSet r = SQL.FetchData("SELECT * FROM `CacheSearch` WHERE `Search` = \"" + SQL.escape(search) + "\" AND Type = " + type.ID + ";");
    try {
      if (r.next())
      {
        return r.getInt("IdResult");
      }
    } catch (SQLException e) {
      Log.stack(Log.DB, e);
    }return -1;
  }

  public static synchronized int writeFile(String fileName)
  {
    SQL.ExecuteQuery("INSERT OR IGNORE INTO `File`(`FileName`, `LastUpdate`) VALUES (\"" + SQL.escape(fileName) + "\", " + FunctionsUtils.getTime() + ");");

    ResultSet r = SQL.FetchData("SELECT * FROM `File` WHERE `FileName` = \"" + SQL.escape(fileName) + "\";");
    try {
      if (r.next())
      {
        return r.getInt("ID");
      }
    } catch (SQLException e) {
      Log.stack(Log.DB, e);
    }return -1;
  }

  public static synchronized boolean existFile(String fileName)
  {
    ResultSet r = SQL.FetchData("SELECT * FROM `File` WHERE `FileName` = \"" + SQL.escape(fileName) + "\";");
    try {
      if (r.next())
      {
        return true;
      }
    } catch (SQLException e) {
      Log.stack(Log.DB, e);
      Log.log(Log.DB, "SELECT * FROM `File` WHERE `FileName` = \"" + SQL.escape(fileName) + "\";");
    }
    return false;
  }

  public static synchronized int getFile(String fileName)
  {
    ResultSet r = SQL.FetchData("SELECT * FROM `File` WHERE `FileName` = \"" + SQL.escape(fileName) + "\";");
    try {
      if (r.next())
      {
        return r.getInt("ID");
      }
    } catch (SQLException e) {
      Log.stack(Log.DB, e);
    }return -1;
  }

  public static synchronized void writeFileInfo(int fileID, MainType t, int schedaId)
  {
    SQL.ExecuteQuery("INSERT OR IGNORE INTO `FileInfo`(`IDFile`, `Type`, `IdSection`, `LastUpdate`) VALUES (" + fileID + ", " + t.ID + ", " + schedaId + ", " + FunctionsUtils.getTime() + ");");
  }

  public static synchronized int[] readFileInfo(int fileID)
  {
    int[] result = new int[2];
    ResultSet r = SQL.FetchData("SELECT * FROM `FileInfo` WHERE `IDFile` = " + fileID + ";");
    try {
      if (r.next())
      {
        result[0] = r.getInt("Type");
        result[1] = r.getInt("IdSection");
        return result;
      }
    } catch (SQLException e) {
      Log.stack(Log.DB, e);
      result[0] = -1;
      result[1] = -1;
    }return result;
  }

  public static synchronized void writeEpisodeInfo(int fileID, int IdSerie, int episode, int serie)
  {
    SQL.ExecuteQuery("INSERT OR IGNORE INTO `EpisodeInfo`(`IdFile`, `IdSerie`, `Episode`, `Serie`, `LastUpdate`) VALUES (" + fileID + ", " + IdSerie + ", " + episode + ", " + serie + ", " + FunctionsUtils.getTime() + ");");
  }
}