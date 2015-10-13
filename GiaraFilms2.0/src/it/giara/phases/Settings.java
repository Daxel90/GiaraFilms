package it.giara.phases;

import java.util.HashMap;
import java.util.prefs.Preferences;

import it.giara.gui.MainFrame;
import it.giara.sql.SQLQuerySettings;
import it.giara.utils.DirUtils;

public class Settings
{
	private final static int VERSION = 2;
	private final static int END_PreReleseVersion = Integer.MAX_VALUE;
	private final static int END_BetaVersion = Integer.MAX_VALUE;
	private static HashMap<String, String> config = new HashMap<String, String>();
	
	public static Preferences prop = Preferences.userRoot().node("giarafilms");
	
	public static void init()
	{
		config.put("DBversion", SQLQuerySettings.getCurrentParameter("DBversion", "" + VERSION));
		config.put("downloadfolder", SQLQuerySettings.getCurrentParameter("downloadfolder",
				DirUtils.getDefaultDownloadDir().getAbsolutePath()));
		config.put("lastdbcheck", SQLQuerySettings.getCurrentParameter("lastdbcheck", "0"));
		
		MainFrame.getInstance().setTitle(getTitle(VERSION));
	}
	
	public static String getParameter(String key)
	{
		return config.get(key);
	}
	
	public static void setParameter(String key, String value)
	{
		config.put(key, value);
		SQLQuerySettings.removeParameters(key);
		SQLQuerySettings.addParameters(key, value);
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
}
