package it.giara;

import it.giara.analyze.FileInfo;
import it.giara.http.HTTPList;
import it.giara.http.HTTPSearchFilm;
import it.giara.http.HTTPSearchTVSerie;
import it.giara.server.Events;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.sql.SQL;
import it.giara.sql.SQLQuery;
import it.giara.utils.Log;
import java.io.File;
import java.util.ArrayList;

public class ServerScan
{
	public static void main(String[] arg)
	{
		Log.log(Log.INFO, "GiaraFilms News Scanner");
	    it.giara.utils.DirUtils.workDir = new File(".");
	    ListLoader.loadSources();
	    SQL.connect();
	    for (SourceChan s : ListLoader.sources)
	    {
		      HTTPList search = new HTTPList(s.link, ".");
		      Log.log(Log.DEBUG, Integer.valueOf(search.file.size()));
		      for (String s2 : search.file)
		      {
			        if (SQLQuery.existFile(s2))
			        	continue;
			        int fileID = SQLQuery.writeFile(s2);
			        Events.discoverNewFile(s2);
			        FileInfo f = new FileInfo(s2);
			
			        switch (f.type.ID)
			        {
				        case 1:
					          int cache = SQLQuery.getCacheSearch(f.title, f.type);
					          if (cache == -1)
					          {
						            HTTPSearchFilm httpF = new HTTPSearchFilm(f.title);
						            if (httpF.scheda == null)
						              continue;
						            Events.writeNewFilmPreScheda(httpF.scheda);
						            int schedaF = SQLQuery.writePreSchedaFilm(httpF.scheda);
						
						            Events.writeNewFileInfo(fileID, f.type, schedaF);
						
						            SQLQuery.writeCacheSearch(f.title, f.type, schedaF);
						            SQLQuery.writeFileInfo(fileID, f.type, schedaF);
					          }
					          else
					          {
						            Events.writeNewFileInfo(fileID, f.type, cache);
						            SQLQuery.writeFileInfo(fileID, f.type, cache);
					          }
					    break;
				        case 2:
					          int cache2 = SQLQuery.getCacheSearch(f.title, f.type);
					          if (cache2 == -1)
					          {
						            HTTPSearchTVSerie httpF = new HTTPSearchTVSerie(f.title);
						            if (httpF.scheda == null)
						            	continue;
						            Events.writeNewSerieTVPreScheda(httpF.scheda);
						            int schedaSTV = SQLQuery.writePreSchedaTvSeries(httpF.scheda);
						
						            Events.writeNewFileInfo(fileID, f.type, schedaSTV);
						
						            SQLQuery.writeCacheSearch(f.title, f.type, schedaSTV);
						            SQLQuery.writeFileInfo(fileID, f.type, schedaSTV);
						
						            Events.writeNewEpisode(fileID, schedaSTV, f.episode, f.series);
						            SQLQuery.writeEpisodeInfo(fileID, schedaSTV, f.episode, f.series);
					          }
					          else
					          {
						            Events.writeNewFileInfo(fileID, f.type, cache2);
						
						            SQLQuery.writeFileInfo(fileID, f.type, cache2);
						            Events.writeNewEpisode(fileID, cache2, f.episode, f.series);
						            SQLQuery.writeEpisodeInfo(fileID, cache2, f.episode, f.series);
					          }
			        }
		      }
	    }
	}
}