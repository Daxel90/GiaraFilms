package it.giara.source;

import it.giara.utils.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class ListLoader
{
	public static ArrayList<SourceChan> sources = new ArrayList<SourceChan>();
	
	public static void loadSources()
	{
		try
		{
			
			URL url = new URL("http://xdccmule.org/GlobalFindEx/DataBase.db");
			BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
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
					sources.add(new SourceChan(server, data[0], data[1]));
					Log.log(Log.CONFIG, data[1]);
				}
				
			}
		} catch (IOException e)
		{
			Log.stack(Log.CONFIG, e);
		}
	}
	
}
