package it.giara.syncdata;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.giara.analyze.FileInfo;
import it.giara.analyze.enums.AddictionalTags;
import it.giara.analyze.enums.MainType;
import it.giara.gui.utils.AbstractFilmList;
import it.giara.phases.ListRequest;
import it.giara.phases.scanservice.AnalizeFileService;
import it.giara.sql.SQLQuery;
import it.giara.sql.SQLQueryScanService;
import it.giara.tmdb.GenereType;
import it.giara.tmdb.schede.TMDBScheda;
import it.giara.utils.GZIPCompression;
import it.giara.utils.Log;
import it.giara.utils.MultipartUtility;

public class NewServerQuery
{
	
	// ACTION 1
	// ArrayList<Object[FileInfo,String]>
	public static void uploadFiles(ArrayList<Object[]> data)
	{
		JSONObject jsonData = new JSONObject();
		
		try
		{
			JSONArray jsonArray = new JSONArray();
			
			for (int id = 0; id < data.size(); id++)
			{
				Object[] obj = data.get(id);
				
				FileInfo fI = (FileInfo) obj[0];
				String size = (String) obj[1];
				
				JSONObject json = new JSONObject();
				json.put("id", id);
				json.put("filename", fI.Filename);
				json.put("size", size);
				json.put("type", fI.type.ID);
				json.put("video", fI.video.tag);
				json.put("audio", fI.audio.tag);
				
				String tags = "";
				
				for (AddictionalTags tag : fI.tags)
				{
					tags += tag.tag + " ";
				}
				
				tags = tags.trim();
				
				json.put("tags", tags);
				
				jsonArray.put(json);
			}
			
			jsonData.put("files", jsonArray);
		} catch (JSONException e)
		{
			Log.stack(Log.ERROR, e);
			e.printStackTrace();
		}
		
		String js = jsonData.toString();
		// Log.log(Log.DEBUG, js);
		// Log.log(Log.DEBUG, js.length());
		byte[] gzipData = GZIPCompression.compress(js);
		
		try
		{
			MultipartUtility multipart = new MultipartUtility(
					"http://giaratest.altervista.org/giarafilms/backend/new_backend.php", "UTF-8");
			multipart.addFormField("action", "1");
			multipart.addByteArrayPart("data", gzipData);
//			Log.log(Log.DEBUG, gzipData.length);
			List<String> response = multipart.finish();
			
			for (String line : response)
			{
				Log.log(Log.DEBUG, line);
				if (line.startsWith("check"))
				{
					int id = Integer.parseInt(line.trim().split(":")[1]);
					AnalizeFileService.addFile(((FileInfo) data.get(id)[0]).Filename);
				}
			}
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
		
	}
	
	// ACTION 2
	public static void uploadSchede(TMDBScheda scheda)
	{
		String js = scheda.getJson().toString();
		
		byte[] gzipData = GZIPCompression.compress(js);
		
		try
		{
			MultipartUtility multipart = new MultipartUtility(
					"http://giaratest.altervista.org/giarafilms/backend/new_backend.php", "UTF-8");
			multipart.addFormField("action", "2");
			multipart.addByteArrayPart("data", gzipData);
//			Log.log(Log.DEBUG, gzipData.length);
			List<String> response = multipart.finish();
			
			for (String line : response)
			{
				Log.log(Log.DEBUG, line);
			}
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
		
	}
	
	// ACTION 3
	public static void updateFileInfo(String filename, int schedaID)
	{
		
		JSONObject jsonData = new JSONObject();
		
		try
		{
			jsonData.put("filename", filename);
			jsonData.put("schedaID", schedaID);
		} catch (JSONException e1)
		{
			e1.printStackTrace();
		}
		
		String js = jsonData.toString();
		byte[] gzipData = GZIPCompression.compress(js);
		
		try
		{
			MultipartUtility multipart = new MultipartUtility(
					"http://giaratest.altervista.org/giarafilms/backend/new_backend.php", "UTF-8");
			multipart.addFormField("action", "3");
			multipart.addByteArrayPart("data", gzipData);
			Log.log(Log.DEBUG, gzipData.length);
			List<String> response = multipart.finish();
			
			for (String line : response)
			{
				Log.log(Log.DEBUG, line);
			}
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
		
	}
	
	// ACTION 4
	public static TMDBScheda requestScheda(int id, MainType type)
	{
		TMDBScheda scheda = null;
		try
		{
			MultipartUtility multipart = new MultipartUtility(
					"http://giaratest.altervista.org/giarafilms/backend/new_backend.php", "UTF-8");
			multipart.addFormField("action", "4");
			multipart.addFormField("schede_id", id + "");
			multipart.addFormField("type", type.ID + "");
			List<String> response = multipart.finish();
			String line = "";
			for (String s : response)
			{
				line += s;
			}
			
			JSONObject json = new JSONObject(line);
			scheda = new TMDBScheda();
			scheda.setJson(json);
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
		return scheda;
	}
	
	// ACTION 5
	public static boolean newsLoaded = false;
	public static int[] news = new int[100];
	
	public static void load100News()
	{
		try
		{
			String data = "action=5";
			
			URL url = new URL("http://giaratest.altervista.org/giarafilms/backend/new_backend.php");
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
				if (line.contains("EndNews"))
					newsLoaded = true;
				if (!line.contains(":--:"))
					break;
					
				String[] part = line.split(":--:");
				int n = Integer.parseInt(part[0]);
				
				JSONObject json = new JSONObject(part[1]);
				TMDBScheda scheda = new TMDBScheda();
				scheda.setJson(json);
				news[n] = SQLQueryScanService.writeScheda(scheda);
			}
			wr.close();
			rd.close();
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
	}

	//ACTION 6
	public static void loadSchedeList(GenereType g, MainType t, AbstractFilmList l, ListRequest.MyBoolean running)
	{
		try
		{
			String data = "action=6&type="+t.ID+"&genre_ids="+g.Id;
			
			URL url = new URL("http://giaratest.altervista.org/giarafilms/backend/new_backend.php");
			URLConnection conn;
			conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStream wr = conn.getOutputStream();
			wr.write(data.getBytes("UTF-8"));
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null && running.getValue())
			{
				Log.log(Log.BACKEND, line);
				if (!line.contains(":--:"))
					break;
					
				String[] part = line.split(":--:");
				
				JSONObject json = new JSONObject(part[1]);
				TMDBScheda scheda = new TMDBScheda();
				scheda.setJson(json);
				SQLQueryScanService.writeScheda(scheda);
				l.addScheda(scheda);
				
			}
			wr.close();
			rd.close();
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
	}

	//ACTION 7
	public static void loadAllSchedeList(MainType t, AbstractFilmList l, ListRequest.MyBoolean running)
	{
		try
		{
			String data = "action=7&type="+t.ID;
			
			URL url = new URL("http://giaratest.altervista.org/giarafilms/backend/new_backend.php");
			URLConnection conn;
			conn = url.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStream wr = conn.getOutputStream();
			wr.write(data.getBytes("UTF-8"));
			wr.flush();
			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = rd.readLine()) != null && running.getValue())
			{
				Log.log(Log.BACKEND, line);
				if (!line.contains(":--:"))
					break;
					
				String[] part = line.split(":--:");
				
				JSONObject json = new JSONObject(part[1]);
				TMDBScheda scheda = new TMDBScheda();
				scheda.setJson(json);
				SQLQueryScanService.writeScheda(scheda);
				l.addScheda(scheda);
			}
			wr.close();
			rd.close();
			
		} catch (Exception e)
		{
			Log.stack(Log.BACKEND, e);
		}
	}

	//ACTION 8
	public static void loadFileOfSchede(TMDBScheda scheda)
	{
		try
		{
			String data = "action=8&scheda_id=" + scheda.ID+"&type="+scheda.type.ID;
			
			URL url = new URL("http://giaratest.altervista.org/giarafilms/backend/new_backend.php");
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
				
				if(type.equals(MainType.SerieTV))
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
