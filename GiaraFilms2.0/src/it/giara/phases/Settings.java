package it.giara.phases;

import java.util.HashMap;
import java.util.prefs.Preferences;

import it.giara.gui.ChangeLogFrame;
import it.giara.gui.MainFrame;
import it.giara.sql.SQLQuery;
import it.giara.sql.SQLQuerySettings;
import it.giara.utils.DirUtils;

public class Settings
{
	public final static int VERSION = 27;
	private final static int END_PreReleaseVersion = Integer.MAX_VALUE;
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
		config.put("servercollaborate", SQLQuerySettings.getCurrentParameter("servercollaborate", "1"));
		config.put("removecompleted", SQLQuerySettings.getCurrentParameter("removecompleted", "0"));
		config.put("tos", SQLQuerySettings.getCurrentParameter("tos", "0"));
		
		config.put("downloadlimit", SQLQuerySettings.getCurrentParameter("downloadlimit", "0"));
		config.put("downloadlimitN", SQLQuerySettings.getCurrentParameter("downloadlimitN", "4"));
		
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
	
	public static boolean getBoolean(String key)
	{
		return config.get(key).equals("1");
	}
	
	public static void setBoolean(String key, boolean bval)
	{
		String value = "0";
		
		if (bval)
			value = "1";
			
		config.put(key, value);
		SQLQuerySettings.updateParameters(key, value);
	}
	
	public static String getTitle(int Version)
	{
		String result = "GiaraFilms 2.0";
		if (Version <= END_PreReleaseVersion)
			result = "GiaraFilms 2.0 Dev PreRelease " + Version;
		else if (Version > END_PreReleaseVersion)
			result = "GiaraFilms 2.0 Beta " + Version;
		else if (Version > END_BetaVersion)
			result = "GiaraFilms 2.0 Build " + Version;
			
		return result;
	}
	
	public static void UpdateFixer()
	{
		
		if(Integer.parseInt(Settings.getParameter("ProgramVersion")) < 21)
		{
			setBoolean("scanservice", true);
			setBoolean("servercollaborate", true);
			SQLQuerySettings.removeParameters("serversync");
			SQLQuerySettings.removeParameters("lastserversync");
			SQLQuerySettings.removeParameters("scanservicethread");
			SQLQuerySettings.removeParameters("lastdbcheck");
			prop.putBoolean("Savelog", false);
		}
		
		if (Integer.parseInt(Settings.getParameter("DBversion")) < 27)
		{
			SQLQuery.DbClear();
			setParameter("DBversion", "26");
		}
		
		if (Integer.parseInt(Settings.getParameter("ProgramVersion")) < Settings.VERSION)
		{
			Settings.setParameter("ProgramVersion", "" + Settings.VERSION);
			new ChangeLogFrame();
		}
	}
	
}
