package it.giara.syncdata;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import it.giara.analyze.FileInfo;
import it.giara.analyze.enums.AddictionalTags;
import it.giara.phases.scanservice.AnalizeFileService;
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
			e.printStackTrace();
		}
		
		String js = jsonData.toString();
		Log.log(Log.DEBUG, js);
		Log.log(Log.DEBUG, js.length());
		byte[] gzipData = GZIPCompression.compress(js);
		
		try
		{
			MultipartUtility multipart = new MultipartUtility(
					"http://giaratest.altervista.org/giarafilms/backend/new_backend.php", "UTF-8");
			multipart.addFormField("action", "1");
			multipart.addByteArrayPart("data", gzipData);
			Log.log(Log.DEBUG, gzipData.length);
			List<String> response = multipart.finish();
			
			for (String line : response)
			{
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
	public static void updateFileInfo(String filename, int schedaID)
	{
	
	}
	
	// ACTION 3
	public static void uploadSchede(TMDBScheda scheda)
	{
	
	}
	
}
