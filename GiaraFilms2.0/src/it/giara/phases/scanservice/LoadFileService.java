package it.giara.phases.scanservice;

import java.util.ArrayList;

import it.giara.analyze.FileInfo;
import it.giara.http.HTTPList;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.sql.SQLQuery;
import it.giara.syncdata.NewServerQuery;
import it.giara.utils.FunctionsUtils;
import it.giara.utils.Log;

public class LoadFileService implements Runnable
{
	public static boolean running = false;
	public static boolean downloadingList = false;
	
	public static int NList = 0;
	public static int FileSize = 0;
	public static int FileStatus = 0;
	
	public static int TotalFile = 0;
	public static int newFile = 0;
	public static boolean loadingList = false;
	
	@Override
	public void run()
	{
		Log.log(Log.INFO, "GiaraFilms start LoadFileService");
		running = true;
		
		FunctionsUtils.sleep(4000);
		for (SourceChan s : ListLoader.sources)
		{
			downloadingList = true;
			NList++;
			
			loadingList = true;
			
			HTTPList search = null;
			
			try
			{
				search = new HTTPList(s.link, ".");
			} catch (Exception e)
			{
				e.printStackTrace();
				continue;
			}
			
			loadingList = false;
			Log.log(Log.SCANSERVICE, search.file.size());
			downloadingList = false;
			
			FileSize = search.file.size();
			
			ArrayList<Object[]> data = new ArrayList<Object[]>();
			
			for (int k = 0; k < search.file.size(); k++)
			{
				TotalFile++;
				FileStatus = k;
				String s2 = search.file.get(k);
				String size = search.sizeList.get(k);
				
				if (!s2.endsWith(".avi") && !s2.endsWith(".mkv") && !s2.endsWith(".mp4"))
				{
					continue;
				}
				
				if (SQLQuery.uploaded_file_cache(s2))
				{
					continue;
				}
				newFile++;
				SQLQuery.write_file_cache(s2);
				
				FileInfo fI = new FileInfo(s2, true);
				fI.parseTags();
				
				// Log.log(Log.DEBUG, fI.type.name()+":"+fI.title);
				
				fI.Filename = s2; // remove lowercase modification;
				
				Object[] obj = new Object[2];
				obj[0] = fI;
				obj[1] = size;
				
				data.add(obj);
				
				if (data.size() == 30)
				{
					NewServerQuery.uploadFiles(data);
					data.clear();
				}
			}
			
			NewServerQuery.uploadFiles(data);
			data.clear();
		}
		running = false;
		Log.log(Log.INFO, "GiaraFilms finish LoadFileService");
		
	}
	
}
