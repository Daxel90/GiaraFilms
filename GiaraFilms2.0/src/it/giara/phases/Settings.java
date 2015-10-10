package it.giara.phases;

import java.util.HashMap;

import it.giara.sql.SQLQuerySettings;
import it.giara.utils.DirUtils;

public class Settings
{
	
	private static HashMap<String, String> config = new HashMap<String, String>();
	
	public static void init()
	{
		config.put("version", SQLQuerySettings.getCurrentParameter("version", "1"));
		config.put("downloadfolder",
				SQLQuerySettings.getCurrentParameter("downloadfolder", DirUtils.getDefaultDownloadDir().getAbsolutePath()));
		config.put("lastdbcheck", SQLQuerySettings.getCurrentParameter("lastdbcheck", "0"));
		
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
}
