package it.giara.tmdb.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import it.giara.analyze.MatchStringAlghorithm;
import it.giara.analyze.enums.MainType;
import it.giara.tmdb.TMDBScheda;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class TmdbApiSearchTVSerie
{
	public TMDBScheda scheda;
	double matchName = -1;
	
	public TmdbApiSearchTVSerie(String search, int year)
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
			Log.log(Log.TMDBApi, search + " NON TROVATO");
			
	}
	
	private void getFileInfo(String title, int year) throws IOException, JSONException
	{
		int responsecode = 429;
		String url = "http://api.themoviedb.org/3/search/tv/?api_key=b7ea4084f5b8c30f6ae8038649bc26f3&language=it&query="
				+ URLEncoder.encode(title, "UTF-8");
				
		while (responsecode == 429) // 429 is the code of query limit reach
		{
			URL obj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			
			conn.setConnectTimeout(12000);
			conn.setRequestMethod("GET");
			
			responsecode = conn.getResponseCode();
			
			if (responsecode == 200)
			{
				Log.log(Log.TMDBApi, "Response:" + responsecode);
				String result = "";
				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
				String line;
				while ((line = rd.readLine()) != null)
				{
					result += line;
				}
				rd.close();
				result = result.trim();
				Log.log(Log.TMDBApi, result);
				
				JSONObject json = new JSONObject(result);
				
				if (json.getJSONArray("results").length() < 1)
				{
					scheda = null;
					return;
				}
				
				for (int k = 0; k < json.getJSONArray("results").length(); k++)
				{
					JSONObject film = json.getJSONArray("results").getJSONObject(k);
					
					double m = MatchStringAlghorithm.compareStrings(film.optString("name"), title);
					String data = film.optString("first_air_date");
					int filmyear = -1;
					if (!data.equals("") && data != null && data.contains("-"))
					{
						filmyear = Integer.parseInt(data.split("-")[0]);
					}
					
					if (year >= 1900 && year <= 2020 && filmyear == year && matchName < m)
					{
						scheda.ID = film.getInt("id");
						scheda.title = film.optString("name");
						scheda.release = film.optString("first_air_date");
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
						scheda.type = MainType.SerieTV;
						break;
					}
					
					if (matchName < m)
					{
						matchName = m;
						scheda.ID = film.getInt("id");
						scheda.title = film.optString("name");
						scheda.release = film.optString("first_air_date");
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
						scheda.type = MainType.SerieTV;
					}
				}
				
			}
			else
			{
				Log.log(Log.TMDBApi, "Response:" + responsecode);
				FunctionsUtils.sleep(1000);
			}
			conn.disconnect();
		}
		
		if (scheda.desc == null || (scheda.desc != null && scheda.desc.trim().equals("")))
		{
			Log.log(Log.TMDBApi, "FallBack in Inglese");
			scheda.fallback_desc = 1;
			
			responsecode = 429;
			url = "http://api.themoviedb.org/3/tv/" + scheda.ID
					+ "?api_key=b7ea4084f5b8c30f6ae8038649bc26f3&language=en";
			while (responsecode == 429) // 429 is the code of query limit reach
			{
				URL obj = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
				
				conn.setConnectTimeout(12000);
				conn.setRequestMethod("GET");
				
				responsecode = conn.getResponseCode();
				
				if (responsecode == 200)
				{
					Log.log(Log.TMDBApi, "Response:" + responsecode);
					String result = "";
					BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
					String line;
					while ((line = rd.readLine()) != null)
					{
						result += line;
					}
					rd.close();
					result = result.trim();
					Log.log(Log.TMDBApi, result);
					
					JSONObject film = new JSONObject(result);
					
					if (scheda.ID == film.getInt("id"))
					{
						scheda.desc = film.optString("overview");
						if (scheda.poster == null || (scheda.poster != null && scheda.poster.trim().equals("")))
						{
							scheda.poster = film.optString("poster_path");
						}
						if (scheda.back == null || (scheda.back != null && scheda.back.trim().equals("")))
						{
							scheda.back = film.optString("backdrop_path");
						}
					}
					
				}
				else
				{
					Log.log(Log.TMDBApi, "Response:" + responsecode);
					FunctionsUtils.sleep(1000);
				}
				conn.disconnect();
			}
		}
		
		Log.log(Log.TMDBApi, "Film: " + scheda.title);
	}
}
