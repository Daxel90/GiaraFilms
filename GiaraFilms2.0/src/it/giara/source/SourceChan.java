package it.giara.source;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import it.giara.download.BotPackage;
import it.giara.utils.Log;

public class SourceChan
{
	public String server;
	public String chan;
	public String link;
	
	public SourceChan(String s, String a, String b)
	{
		server = s;
		chan = a;
		link = b;
	}
	
	
	public String getStatus()
	{
			try
			{
				URL url = new URL(link+"?m=1&q=ita");
				final URLConnection conn = url.openConnection();
				conn.setRequestProperty("User-Agent", "PoWeR-Script");
				conn.setConnectTimeout(1000);
				conn.connect();
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
				String line;
				int counter = 0;
				while ((line = rd.readLine()) != null)
				{
					if (!line.startsWith("#"))
						continue;
						
					line = line.trim();
					
					while (line.contains("  "))
						line = line.replace("  ", " ");
						
					String[] info = line.split(" ");
					if (info.length < 4)
						continue;
					
					rd.close();
					return "";
					
				}
				rd.close();
				
			} catch (Exception e)
			{
				e.printStackTrace();
				return "OFFLINE";
			}
			return "OFFLINE";
	}
}
