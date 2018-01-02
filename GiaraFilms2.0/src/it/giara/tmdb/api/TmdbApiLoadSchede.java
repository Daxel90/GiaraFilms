package it.giara.tmdb.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

import it.giara.analyze.enums.MainType;
import it.giara.tmdb.TMDBScheda;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class TmdbApiLoadSchede
{
	public TMDBScheda scheda;
	
	public TmdbApiLoadSchede(int schedeid, MainType type)
	{
		scheda = new TMDBScheda();
		scheda.ID = schedeid;
		try
		{
			switch (type)
			{
				case SerieTV:
					loadTvSerie(schedeid);
					break;
				case Film:
					loadFilm(schedeid);
					break;
				default:
					break;
			}
		} catch (Exception e)
		{
			Log.stack(Log.NET, e);
		}
		
	}
	
	public void loadTvSerie(int schedeid) throws IOException, JSONException
	{
		int responsecode = 429;
		String url = "http://api.themoviedb.org/3/tv/" + schedeid
				+ "?api_key=b7ea4084f5b8c30f6ae8038649bc26f3&language=it";
				
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
					scheda.title = film.optString("name");
					scheda.release = film.optString("first_air_date");
					scheda.poster = film.optString("poster_path");
					scheda.back = film.optString("backdrop_path");
					scheda.desc = film.optString("overview");
					ArrayList<Integer> gens = new ArrayList<Integer>();
					
					for (int l = 0; l < film.getJSONArray("genres").length(); l++)
					{
						gens.add(film.getJSONArray("genres").getJSONObject(l).getInt("id"));
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
			url = "http://api.themoviedb.org/3/tv/" + schedeid
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
	
	public void loadFilm(int schedeid) throws IOException, JSONException
	{
		
		int responsecode = 429;
		String url = "http://api.themoviedb.org/3/movie/" + schedeid
				+ "?api_key=b7ea4084f5b8c30f6ae8038649bc26f3&language=it";
				
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
				
				scheda.title = film.optString("title");
				scheda.release = film.optString("release_date");
				scheda.poster = film.optString("poster_path");
				scheda.back = film.optString("backdrop_path");
				scheda.desc = film.optString("overview");
				ArrayList<Integer> gens = new ArrayList<Integer>();
				
				for (int l = 0; l < film.getJSONArray("genres").length(); l++)
				{
					gens.add(film.getJSONArray("genres").getJSONObject(l).getInt("id"));
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
			url = "http://api.themoviedb.org/3/movie/" + schedeid
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
					Log.log(Log.TMDBApi, "Response:"+result);
					
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
