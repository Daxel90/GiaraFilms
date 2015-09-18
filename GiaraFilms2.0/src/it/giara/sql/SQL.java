package it.giara.sql;

import it.giara.utils.DirUtils;
import it.giara.utils.Log;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQL 
{
    static Connection c = null;
    static Statement stmt = null;  
	static File db = new File(DirUtils.getWorkingDirectory(),"Database.db");
	
	
	public static void connect()
	{
		try
		{
			System.out.println(db.getAbsolutePath());
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:"+db.getAbsolutePath());
			c.setAutoCommit(true);
			stmt = c.createStatement();
			Log.log(Log.DB, "Accedo al Database");
			SQLQuery.initTable();
		      
		}catch(Exception e)
		{
			Log.log(Log.DB,"IMPOSSIBILE ACCEDERE AL DATABASE");
			Log.stack(Log.DB, e);
		}
	}
	
	
	public static void ExecuteQuery(String query)
	{
		 try {
			stmt.executeUpdate(query);
		} catch (Exception e) 
		{
			Log.log(Log.DB, query);
			Log.stack(Log.DB, e);
		}
	}
	
	public static ResultSet FetchData(String query)
	{
		try {
			return stmt.executeQuery(query);
		} catch (Exception e) 
		{
			Log.log(Log.DB, query);
			Log.stack(Log.DB, e);
			return null;
		}
		
	}
	
	public static String escape(String val)
	{
		if(val == null)
			return "";
		return val.replace("'","\'").replace("\"", " ");
	}
	

}
