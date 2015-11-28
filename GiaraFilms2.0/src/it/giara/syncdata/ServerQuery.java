package it.giara.syncdata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

import it.giara.analyze.enums.MainType;
import it.giara.tmdb.schede.TMDBScheda;
import it.giara.utils.Log;

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
			String data = "action=2&id=" + id + "&type=" + type.ID;
			
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
	
	public static void sendFileInfo(String fileName, String size, int IdScheda, MainType t)
	{
		try
		{
			JSONObject json = new JSONObject();
			
			json.put("fileName", fileName);
			json.put("size", size);
			json.put("IdScheda", IdScheda);
			json.put("type", t.ID);
			
			String data = "action=3&json=" + json.toString();
			
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
	
	public static void loadUntil(int linebreak)
	{
		try
		{
			String data = "action=4";
			
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
				if(!line.contains(":--:"))
					break;
				
				String[] part = line.split(":--:");
				int n = Integer.parseInt(part[0]);
				
				if(n == linebreak)
					break;
			}
			wr.close();
			rd.close();
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
	}
}
