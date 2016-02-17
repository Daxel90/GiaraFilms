package it.giara.phases;

import java.io.File;
import java.util.HashMap;
import java.util.prefs.Preferences;

import it.giara.gui.ChangeLogFrame;
import it.giara.gui.MainFrame;
import it.giara.sql.SQLQuery;
import it.giara.sql.SQLQuerySettings;
import it.giara.utils.DirUtils;
import it.giara.utils.Log;
import it.giara.utils.ThreadManager;

public class Settings
{
	public final static int VERSION = 17;
	private final static int END_PreReleseVersion = Integer.MAX_VALUE;
	private final static int END_BetaVersion = Integer.MAX_VALUE;
	private static HashMap<String, String> config = new HashMap<String, String>();
	
	public static Preferences prop = Preferences.userRoot().node("giarafilms");
	
	public static void init()
	{
		config.put("ProgramVersion", SQLQuerySettings.getCurrentParameter("ProgramVersion", "" + VERSION));
		config.put("DBversion", SQLQuerySettings.getCurrentParameter("DBversion", "" + VERSION));
		config.put("downloadfolder", SQLQuerySettings.getCurrentParameter("downloadfolder",
				DirUtils.getDefaultDownloadDir().getAbsolutePath()));
				
		config.put("scanservice", SQLQuerySettings.getCurrentParameter("scanservice", "1"));
		config.put("scanservicethread", SQLQuerySettings.getCurrentParameter("scanservicethread", "1"));
		config.put("servercollaborate", SQLQuerySettings.getCurrentParameter("servercollaborate", "1"));
		config.put("serversync", SQLQuerySettings.getCurrentParameter("serversync", "0")); // Features
		config.put("lastserversync", SQLQuerySettings.getCurrentParameter("lastserversync", "0")); // Features
		config.put("tos", SQLQuerySettings.getCurrentParameter("tos", "0"));
		
		try
		{
			int n = Integer.parseInt(getParameter("scanservicethread"));
			
			if (n <= 0)
				ThreadManager.poolScanServiceSize = 1;
			else
				ThreadManager.poolScanServiceSize = n;
			
		} catch (NumberFormatException e)
		{
			Log.stack(Log.CONFIG, e);
			ThreadManager.poolScanServiceSize = 1;
			setParameter("scanservicethread", "1");
			config.put("scanservicethread", "1");
		}
		
		if (MainFrame.getInstance() != null)
			MainFrame.getInstance().setTitle(getTitle(VERSION));
	}
	
	public static String getParameter(String key)
	{
		return config.get(key);
	}
	
	public static void setParameter(String key, String value)
	{
		config.put(key, value);
		SQLQuerySettings.updateParameters(key, value);
	}
	
	public static String getTitle(int Version)
	{
		String result = "GiaraFilms 2.0";
		if (Version <= END_PreReleseVersion)
			result = "GiaraFilms 2.0 Dev PreRelese " + Version;
		else if (Version > END_PreReleseVersion)
			result = "GiaraFilms 2.0 Beta " + Version;
		else if (Version > END_BetaVersion)
			result = "GiaraFilms 2.0 Build " + Version;
			
		return result;
	}
	
	public static void UpdateFixer()
	{
		if (Integer.parseInt(Settings.getParameter("ProgramVersion")) < Settings.VERSION)
		{
			Settings.setParameter("ProgramVersion", "" + Settings.VERSION);
			new ChangeLogFrame();
		}
		
		if (Integer.parseInt(Settings.getParameter("DBversion")) < 10)
		{
			new File(DirUtils.getWorkingDirectory(), "FileDatabase.db").delete();
			SQLQuerySettings.TableFix1();
			setParameter("DBversion", "10");
		}
		
		if (Integer.parseInt(Settings.getParameter("DBversion")) < 15)
		{
			SQLQuery.DbClear();
			setParameter("DBversion", "15");
		}
		
		if (Integer.parseInt(Settings.getParameter("DBversion")) < 16)
		{
			SQLQuerySettings.removeParameters("lastdbcheck");
			Settings.setParameter("servercollaborate", "1");
			Settings.setParameter("scanservice", "1");
			setParameter("DBversion", "16");
		}
		
		if(Integer.parseInt(Settings.getParameter("DBversion")) < 17)
		{
			SQLQuery.DbClear();
		}
	}
	
}
