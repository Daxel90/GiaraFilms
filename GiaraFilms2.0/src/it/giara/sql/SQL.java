package it.giara.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import it.giara.utils.DirUtils;
import it.giara.utils.Log;

public class SQL
{
	static Connection c = null;
	static Statement stmt = null;
	static File db = new File(DirUtils.getWorkingDirectory(), "FileDatabase.db");
	
	static Connection cSett = null;
	static Statement stmtSett = null;
	static File settings = new File(DirUtils.getWorkingDirectory(), "Settings.db");
	
	public static void connect()
	{
		try
		{
			System.out.println(db.getAbsolutePath());
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:" + db.getAbsolutePath());
			c.setAutoCommit(true);
			stmt = c.createStatement();
			Log.log(Log.DB, "Accedo al Database File");
			SQLQuery.initTable();
			
		} catch (Exception e)
		{
			Log.log(Log.DB, "IMPOSSIBILE ACCEDERE AL DATABASE DEI FILE");
			Log.stack(Log.DB, e);
		}
		
		try
		{
			System.out.println(settings.getAbsolutePath());
			Class.forName("org.sqlite.JDBC");
			cSett = DriverManager.getConnection("jdbc:sqlite:" + settings.getAbsolutePath());
			cSett.setAutoCommit(true);
			stmtSett = cSett.createStatement();
			Log.log(Log.DB, "Accedo al Database Settings");
			SQLQuerySettings.initTable();
			
		} catch (Exception e)
		{
			Log.log(Log.DB, "IMPOSSIBILE ACCEDERE AL DATABASE");
			Log.stack(Log.DB, e);
		}
	}
	
	// FILE DB
	public static void ExecuteQuery(String query)
	{
		try
		{
			stmt.executeUpdate(query);
		} catch (Exception e)
		{
			Log.log(Log.DB, query);
			Log.stack(Log.DB, e);
		}
	}
	
	public static ResultSet FetchData(String query)
	{
		try
		{
			return stmt.executeQuery(query);
		} catch (Exception e)
		{
			Log.log(Log.DB, query);
			Log.stack(Log.DB, e);
			return null;
		}
		
	}
	
	// SETTINGS DB
	public static void ExecuteQuerySettings(String query)
	{
		try
		{
			stmtSett.executeUpdate(query);
		} catch (Exception e)
		{
			Log.log(Log.DB, query);
			Log.stack(Log.DB, e);
		}
	}
	
	public static ResultSet FetchDataSettings(String query)
	{
		try
		{
			return stmtSett.executeQuery(query);
		} catch (Exception e)
		{
			Log.log(Log.DB, query);
			Log.stack(Log.DB, e);
			return null;
		}
		
	}
	
	public static String escape(String val)
	{
		if (val == null)
			return "";
		return val.replace("'", "\'").replace("\"", " ");
	}
	
}
