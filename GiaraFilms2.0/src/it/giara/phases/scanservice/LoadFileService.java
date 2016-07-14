package it.giara.phases.scanservice;

import java.util.ArrayList;

import it.giara.analyze.FileInfo;
import it.giara.http.HTTPList;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.syncdata.NewServerQuery;
import it.giara.utils.Log;

public class LoadFileService implements Runnable
{
	public static boolean running = false;
	public static boolean downloadingList = false;
	public static int NList = 0;
	public static int LStatus = 0;
	
	@Override
	public void run()
	{
		Log.log(Log.INFO, "GiaraFilms start LoadFileService");
		running = true;
		
		for (SourceChan s : ListLoader.sources)
		{
			downloadingList = true;
			NList++;
			HTTPList search = new HTTPList(s.link, ".");
			Log.log(Log.SCANSERVICE, search.file.size());
			downloadingList = false;
			
			ArrayList<Object[]> data = new ArrayList<Object[]>();
			for (int k = 0; k < search.file.size(); k++)
			{
				
				LStatus = k;
				String s2 = search.file.get(k);
				String size = search.sizeList.get(k);
				
				if (!s2.endsWith(".avi") && !s2.endsWith(".mkv") && !s2.endsWith(".mp4"))
				{
					continue;
				}
				
				FileInfo fI = new FileInfo(s2, true);
				fI.parseTags();
				
//				Log.log(Log.DEBUG, fI.type.name()+":"+fI.title);
				
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
		
	}
	
}
