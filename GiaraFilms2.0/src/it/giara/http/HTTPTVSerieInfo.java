package it.giara.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import it.giara.schede.SchedaTVSerie;
import it.giara.utils.Log;

public class HTTPTVSerieInfo
{
	public SchedaTVSerie serie;
	private String site;
	
	public HTTPTVSerieInfo(String s)
	{
		site = s;
		serie = new SchedaTVSerie();
	}
	
	public void getInfo()
	{
		try
		{
			URL url = new URL(site);
			final URLConnection conn = url.openConnection();
			conn.setRequestProperty("User-Agent", "PoWeR-Script");
			conn.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
			String line;
			boolean descr = false;
			while ((line = rd.readLine()) != null)
			{
				String trimmed = line.trim();
				if (trimmed.startsWith("<!-- Paragrafo -->"))
				{
					descr = true;
					continue;
				}
				
				if (descr)
				{
					if (trimmed.startsWith("</div>"))
					{
						descr = false;
						continue;
					}
					serie.desc += trimmed.replace("<p>", "").replace("</p>", "") + " ";
				}
				
				if (trimmed.startsWith("<img itemprop=\"image\" src=\""))
				{
					String k = trimmed.replace("<img itemprop=\"image\" src=\"", "")
							.split("\"")[0];
					serie.BigImage = k;
					continue;
				}
				
			}
			rd.close();
			
		} catch (Exception e)
		{
			Log.stack(Log.NET, e);
		}
		serie.loading = false;
		
		Log.log(Log.FILEINFO, serie.desc);
		Log.log(Log.FILEINFO, serie.BigImage);
	}
	
}
