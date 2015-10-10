package it.giara.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.giara.utils.Log;

public class SQLQuerySettings
{
	
	public static void initTable()
	{
		String query = "CREATE TABLE IF NOT EXISTS `Downloads` (" + " `FileName` TEXT NOT NULL UNIQUE, "
				+ "`FileDirectory` TEXT NOT NULL UNIQUE " + ");";
		SQL.ExecuteQuerySettings(query);
		
		query = "CREATE TABLE IF NOT EXISTS `Parameters` (" + "`Key`	TEXT NOT NULL UNIQUE,"
				+ " `Value`	TEXT NOT NULL" + ");";
		SQL.ExecuteQuerySettings(query);
	}
	
	public synchronized static void addDownload(String filename, String directory)
	{
		SQL.ExecuteQuerySettings("INSERT OR IGNORE INTO `Downloads`(`FileName`, `FileDirectory`) VALUES (\""
				+ SQL.escape(filename) + "\", \"" + directory + "\");");
	}
	
	public synchronized static void removeDownload(String filename)
	{
		SQL.ExecuteQuerySettings("DELETE FROM `Downloads` WHERE `FileName` = \"" + filename + "\";");
	}
	
	public synchronized static List<String[]> getCurrentDownloads()
	{
		List<String[]> download = new ArrayList<String[]>();
		
		ResultSet r = SQL.FetchDataSettings("SELECT * FROM `Downloads` WHERE 1");
		try
		{
			if (r.next())
			{
				download.add(new String[] { r.getString("FileName"), r.getString("FileDirectory") });
			}
		} catch (SQLException e)
		{
			Log.stack(Log.DB, e);
		}
		
		return download;
	}
	
	public synchronized static void addParameters(String key, String Value)
	{
		SQL.ExecuteQuerySettings(
				"INSERT OR IGNORE INTO `Parameters`(`Key`, `Value`) VALUES (\"" + key + "\", \"" + Value + "\");");
	}
	
	public synchronized static String getCurrentParameter(String key, String defaultVal)
	{
		
		ResultSet r = SQL.FetchDataSettings("SELECT * FROM `Parameters` WHERE Key = \"" + key + "\"");
		try
		{
			if (r.next())
			{
				return r.getString("Value");
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
	
}
