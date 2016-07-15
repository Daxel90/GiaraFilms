package it.giara.syncdata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import it.giara.analyze.FileInfo;
import it.giara.analyze.enums.MainType;
import it.giara.phases.Settings;
import it.giara.sql.SQLQuery;
import it.giara.tmdb.schede.TMDBScheda;
import it.giara.utils.Log;
import it.giara.utils.ThreadManager;

public class ServerQuery
{
	
	public static void sendScheda(TMDBScheda scheda)
	{
		try
		{
			JSONObject json = scheda.getJson();
			
			String data = "action=1&json=" + json.toString();
			
			URL url = new URL("http://giaratest.altervista.org/giarafilms/backend/backend.php");
			URLConnection conn;
			conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStream wr = conn.getOutputStream();
			wr.write(data.getBytes("UTF-8"));
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null)
			{
				Log.log(Log.BACKEND, line);
			}
			wr.close();
			rd.close();
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
	}
	
	public static TMDBScheda requestScheda(int id, MainType type)
	{
		TMDBScheda scheda = null;
		try
		{
			String data = "action=2&scheda_id=" + id + "&type=" + type.ID;
			
			URL url = new URL("http://giaratest.altervista.org/giarafilms/backend/backend.php");
			URLConnection conn;
			conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStream wr = conn.getOutputStream();
			wr.write(data.getBytes("UTF-8"));
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line = "";
			String s;
			while ((s = rd.readLine()) != null)
			{
				line += s;
			}
			Log.log(Log.BACKEND, line);
			JSONObject json = new JSONObject(line);
			scheda = new TMDBScheda();
			scheda.setJson(json);
			wr.close();
			rd.close();
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
		return scheda;
	}
	
	public static JSONArray jsonArray;
	public static boolean ThreadRunning = false;
	
	public static void sendFileInfo(String fileName, String size, int IdScheda, MainType t)
	{
		
		if (IdScheda == -1)
			return;
			
		try
		{
			if (jsonArray == null)
			{
				jsonArray = new JSONArray();
			}
			
			JSONObject json = new JSONObject();
			json.put("filename", fileName);
			json.put("size", size);
			json.put("scheda_id", IdScheda);
			json.put("type", t.ID);
			
			jsonArray.put(json);
			
			if (!ThreadRunning)
			{
				ThreadRunning = true;
				Runnable run = new Runnable()
				{
					@Override
					public void run()
					{
						try
						{
							if (jsonArray.length() > 0)
							{
								Log.log(Log.BACKEND, "Invio " + jsonArray.length() + " informazioni files");
								JSONObject json = new JSONObject();
								json.put("files", jsonArray);
								
								String data = "action=3&json=" + json.toString();
								
								jsonArray = null;
								
								URL url = new URL("http://giaratest.altervista.org/giarafilms/backend/backend.php");
								URLConnection conn;
								conn = url.openConnection();
								conn.setDoOutput(true);
								conn.setDoInput(true);
								OutputStream wr = conn.getOutputStream();
								wr.write(data.getBytes("UTF-8"));
								wr.flush();
								BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
								String line;
								while ((line = rd.readLine()) != null)
								{
									Log.log(Log.BACKEND, line);
								}
								wr.close();
								rd.close();
								
							}
						} catch (Exception e)
						{
							Log.stack(Log.BACKEND, e);
						}
						
						ThreadRunning = false;
					}
					
				};
				
				ThreadManager.submitScheduleTask(run, 3);
			}
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
	}
	
	public static void loadUntil(int lastloaded)
	{
		try
		{
			String data = "action=4&last=" + lastloaded;
			
			URL url = new URL("http://giaratest.altervista.org/giarafilms/backend/backend.php");
			URLConnection conn;
			conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStream wr = conn.getOutputStream();
			wr.write(data.getBytes("UTF-8"));
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			int nfile = 0;
			int lastFile = lastloaded;
			boolean endSync = false;
			while ((line = rd.readLine()) != null)
			{
				Log.log(Log.BACKEND, line);
				if (line.startsWith("EndSync"))
				{
					Settings.setParameter("lastserversync", "" + lastFile);
					endSync = true;
					break;
				}
				if (!line.contains(":--:"))
					break;
					
				String[] part = line.split(":--:");
				int n = Integer.parseInt(part[0]);
				
				lastFile = n;
				
				nfile++;
				if (nfile >= 10)
				{
					nfile = 0;
					Settings.setParameter("lastserversync", "" + lastFile);
				}
			}
			wr.close();
			rd.close();
			
			if (!endSync)
			{
				loadUntil(lastFile);
			}
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
	}
	
	public static void loadFileOfSchede(TMDBScheda scheda)
	{
		try
		{
			String data = "action=8&scheda_id=" + scheda.ID + "&type=" + scheda.type.ID;
			
			URL url = new URL("http://giaratest.altervista.org/giarafilms/backend/backend.php");
			URLConnection conn;
			conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStream wr = conn.getOutputStream();
			wr.write(data.getBytes("UTF-8"));
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null)
			{
				Log.log(Log.BACKEND, line);
				if (!line.contains(":--:"))
					break;
					
				String[] part = line.split(":--:");
				MainType type = MainType.getMainTypeByID(Integer.parseInt(part[4]));
				int id = SQLQuery.writeFile(part[1], part[2], Integer.parseInt(part[3]), type, false);
				
				if (type.equals(MainType.SerieTV))
				{
					FileInfo f = new FileInfo(part[1], false);
					f.paraseTVSeriesEpisode();
					SQLQuery.writeEpisodeInfo(id, Integer.parseInt(part[3]), f.episode, f.series);
				}
				
			}
			wr.close();
			rd.close();
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
	}
}
