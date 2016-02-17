package it.giara.source;

import it.giara.utils.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ListLoader
{
	public static ArrayList<SourceChan> sources = new ArrayList<SourceChan>();
	
	static
	{
		System.setProperty("http.agent", "PoWeR-Script");
	}
	
	public static void loadSources()
	{
		try
		{
			URL url = new URL("http://giaratest.altervista.org/giarafilms/database.db");
			
			URLConnection conn = url.openConnection();
			conn.connect();
			
			BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
			String server = "";
			String line;
			
			while ((line = input.readLine()) != null)
			{
				line = line.trim();
				if (line.startsWith("["))
				{
					continue;
				}
				
				String[] prt = line.split("=");
				String[] data = line.split("\\*");
				if (Integer.parseInt(prt[0]) == 0)
				{
					server = data[1];
				}
				else
				{
					sources.add(new SourceChan(server, data[0].split("=")[1], data[1]));
				}
				
			}
			input.close();
			
		} catch (IOException e)
		{
			Log.stack(Log.CONFIG, e);
		}
	}
	
}
