package it.giara.tmdb.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import it.giara.analyze.MatchStringAlghorithm;
import it.giara.analyze.enums.MainType;
import it.giara.tmdb.TMDBScheda;
import it.giara.utils.Log;

public class TMDBSearchFilm
{
	
	public TMDBScheda scheda;
	double matchName = -1;
	
	public TMDBSearchFilm(String search, int year)
	{
		Log.log(Log.FILMINFO, search);
		scheda = new TMDBScheda();
		try
		{
			getFileInfo(search, year);
		} catch (Exception e)
		{
			Log.stack(Log.NET, e);
		}
		
		if (scheda == null)
			Log.log(Log.TMDB, search + " NON TROVATO");
			
	}
	
	private void getFileInfo(String title, int year) throws IOException, JSONException
	{
		{
			String result = "{\"films\":";
			URL url = new URL("https://www.themoviedb.org/search/remote/multi?query=" + title.replace(" ", "%20")
					+ "&language=it");
			final URLConnection conn = url.openConnection();
			conn.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
			String line;
			while ((line = rd.readLine()) != null)
			{
				result += line;
			}
			rd.close();
			result = result.trim();
			result += "}";
			Log.log(Log.TMDB, result);
			
			JSONObject json = new JSONObject(result);
			
			if (json.getJSONArray("films").length() < 1)
			{
				scheda = null;
				return;
			}
			
			for (int k = 0; k < json.getJSONArray("films").length(); k++)
			{
				JSONObject film = json.getJSONArray("films").getJSONObject(k);
				
				if (film.getString("media_type").equals("movie"))
				{
					double m = MatchStringAlghorithm.compareStrings(film.optString("name"),title);
					String data = film.optString("release_date");
					int filmyear = -1;
					if(!data.equals("") && data != null && data.contains("-"))
					{
						filmyear = Integer.parseInt(data.split("-")[0]);
					}
					
					if(year != -1 && filmyear == year)
					{
						scheda.ID = film.getInt("id");
						scheda.title = film.optString("name");
						scheda.relese = film.optString("release_date");
						scheda.poster = film.optString("poster_path");
						scheda.back = film.optString("backdrop_path");
						scheda.desc = film.optString("overview");
						ArrayList<Integer> gens = new ArrayList<Integer>();
						
						for (int l = 0; l < film.getJSONArray("genre_ids").length(); l++)
						{
							gens.add(film.getJSONArray("genre_ids").getInt(l));
						}
						scheda.genre_ids = new int[gens.size()];
						Iterator<Integer> iterator = gens.iterator();
						for (int i = 0; i < scheda.genre_ids.length; i++)
						{
							scheda.genre_ids[i] = iterator.next().intValue();
						}
						scheda.vote = film.optDouble("vote_average");
						scheda.type = MainType.Film;
						break;
					}
					
					if(matchName < m)
					{
						matchName = m;
						scheda.ID = film.getInt("id");
						scheda.title = film.optString("name");
						scheda.relese = film.optString("release_date");
						scheda.poster = film.optString("poster_path");
						scheda.back = film.optString("backdrop_path");
						scheda.desc = film.optString("overview");
						ArrayList<Integer> gens = new ArrayList<Integer>();
						
						for (int l = 0; l < film.getJSONArray("genre_ids").length(); l++)
						{
							gens.add(film.getJSONArray("genre_ids").getInt(l));
						}
						scheda.genre_ids = new int[gens.size()];
						Iterator<Integer> iterator = gens.iterator();
						for (int i = 0; i < scheda.genre_ids.length; i++)
						{
							scheda.genre_ids[i] = iterator.next().intValue();
						}
						scheda.vote = film.optDouble("vote_average");
						scheda.type = MainType.Film;
					}
				}
			}
		}
		if (scheda.desc != null && scheda.desc.trim().equals(""))
		{
			String result = "{\"films\":";
			URL url = new URL("https://www.themoviedb.org/search/remote/multi?query=" + title.replace(" ", "%20")
					+ "&language=en");
			final URLConnection conn = url.openConnection();
			conn.connect();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
			String line;
			while ((line = rd.readLine()) != null)
			{
				result += line;
			}
			rd.close();
			result = result.trim();
			result += "}";
			Log.log(Log.TMDB, result);
			
			JSONObject json = new JSONObject(result);
			
			if (json.getJSONArray("films").length() < 1)
			{
				scheda = null;
				return;
			}
			
			for (int k = 0; k < json.getJSONArray("films").length(); k++)
			{
				JSONObject film = json.getJSONArray("films").getJSONObject(k);
				
				if (film.getString("media_type").equals("movie"))
				{
					if (scheda.ID == film.getInt("id"))
					{
						scheda.desc = film.optString("overview");
					}
					break;
				}
			}
			
		}
		Log.log(Log.TMDB, scheda);
		
	}
	
}
