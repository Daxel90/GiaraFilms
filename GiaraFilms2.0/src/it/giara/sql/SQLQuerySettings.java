package it.giara.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import it.giara.utils.Log;

public class SQLQuerySettings
{
	
	public static void initTable()
	{
		String query = "CREATE TABLE IF NOT EXISTS `Downloads` (" + " `FileName` TEXT NOT NULL UNIQUE, "
				+ "`FileDirectory` TEXT NOT NULL UNIQUE, " + "`status` INT NOT NULL DEFAULT 0 " + ");";
		SQL.ExecuteQuerySettings(query);
		
		query = "CREATE TABLE IF NOT EXISTS `Parameters` (" + "`Key`	TEXT NOT NULL UNIQUE,"
				+ " `Value`	TEXT NOT NULL" + ");";
		SQL.ExecuteQuerySettings(query);
		
//		query = "CREATE TABLE IF NOT EXISTS `Favorite` (" + "`schede_id` INTEGER, " +"`type` INTEGER, " + "`vote` INTEGER," + "`last_update` INTEGER" + ");";
//		SQL.ExecuteQuerySettings(query);
//		
//		query = "CREATE TABLE IF NOT EXISTS `Schedule` (" + "`Key`	TEXT NOT NULL UNIQUE," + " `Value`	TEXT NOT NULL"
//				+ ");";
//		SQL.ExecuteQuerySettings(query);
		
	}
	
	public static void TableFix1()
	{
		String query = "ALTER TABLE `Downloads` ADD `status` INT NOT NULL DEFAULT 0";
		SQL.ExecuteQuerySettings(query);
	}
	
	public synchronized static void addDownload(String filename, String directory)
	{
		SQL.ExecuteQuerySettings("INSERT OR IGNORE INTO `Downloads`(`FileName`, `FileDirectory`) VALUES (\""
				+ SQL.escape(filename) + "\", \"" + SQL.escape(directory) + "\");");
	}
	
	public synchronized static void removeDownload(String filename)
	{
		SQL.ExecuteQuerySettings("DELETE FROM `Downloads` WHERE `FileName` = \"" + SQL.escape(filename) + "\";");
	}
	
	public synchronized static void setStatus(String filename, int status)
	{
		SQL.ExecuteQuerySettings("UPDATE `Downloads` SET `status` = " + status + " WHERE `FileName` = \""
				+ SQL.escape(filename) + "\";");
	}
	
	public synchronized static int getStatus(String filename)
	{
		ResultSet r = SQL
				.FetchDataSettings("SELECT * FROM `Downloads` WHERE `FileName` = \"" + SQL.escape(filename) + "\"");
		try
		{
			if (r.next())
			{
				return r.getInt("status");
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		return 0;
	}
	
	public synchronized static HashMap<String, String[]> getCurrentDownloads()
	{
		
		HashMap<String, String[]> ris = new HashMap<String, String[]>();
		
		ResultSet r = SQL.FetchDataSettings("SELECT * FROM `Downloads` WHERE 1");
		try
		{
			while (r.next())
			{
				ris.put(SQL.unescape(r.getString("FileName")),
						new String[] { SQL.unescape(r.getString("FileDirectory")), "" + r.getInt("status") });
						
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		
		return ris;
	}
	
	public synchronized static void addParameters(String key, String Value)
	{
		SQL.ExecuteQuerySettings("INSERT OR IGNORE INTO `Parameters`(`Key`, `Value`) VALUES (\"" + key + "\", \""
				+ SQL.escape(Value) + "\");");
	}
	
	public synchronized static String getCurrentParameter(String key, String defaultVal)
	{
		
		ResultSet r = SQL.FetchDataSettings("SELECT * FROM `Parameters` WHERE Key = \"" + key + "\"");
		try
		{
			if (r.next())
			{
				return SQL.unescape(r.getString("Value"));
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		addParameters(key, defaultVal);
		return defaultVal;
	}
	
	public synchronized static void removeParameters(String key)
	{
		SQL.ExecuteQuerySettings("DELETE FROM `Parameters` WHERE `Key` = \"" + key + "\";");
	}
	
	public synchronized static void updateParameters(String key, String Val)
	{
		SQL.ExecuteQuerySettings("UPDATE `Parameters` SET `Value`= '" + Val + "' WHERE `Key` = \"" + key + "\";");
	}
	
}
