package it.giara.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import it.giara.schede.SchedaFilm;
import it.giara.utils.Log;

public class HTTPFilmInfo
{
	public SchedaFilm film;
	private String site;
	
	public HTTPFilmInfo(String s)
	{
		site = s;
		film = new SchedaFilm();	
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
					film.desc += trimmed.replace("<p>", "").replace("</p>", "") + " ";
					if (trimmed.contains("</p>"))
					{
						descr = false;
						continue;
					}
				}
				if(trimmed.startsWith("<div id=\"voto-pubblico\" class=\"voto voto-pubblico col-xs-12 col-sm-4 h6\" data-rating=\""))
				{
					String  k = trimmed.replace("<div id=\"voto-pubblico\" class=\"voto voto-pubblico col-xs-12 col-sm-4 h6\" data-rating=\"", "").split("\">")[0];
					film.vote = Float.parseFloat(k);
					continue;
				}
				if(trimmed.startsWith("<img itemprop=\"image\" src=\""))
				{
					String  k = trimmed.replace("<img itemprop=\"image\" src=\"", "").split("\"")[0];
					film.BigImage = k;
					continue;
				}
				
			}
			rd.close();
			
		} catch (Exception e)
		{
			Log.stack(Log.NET, e);
		}
		film.loading = false;
		
		Log.log(Log.FILEINFO,film.desc);
		Log.log(Log.FILEINFO,film.vote);
		Log.log(Log.FILEINFO,film.BigImage);
	}
	
}
