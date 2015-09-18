package it.giara;

import java.util.ArrayList;

import it.giara.analyze.FileInfo;
import it.giara.http.HTTPList;
import it.giara.source.ListLoader;
import it.giara.source.SourceChan;
import it.giara.sql.SQL;
import it.giara.utils.Log;

public class MainClass
{
  public static ArrayList<String> sorgenti = new ArrayList<String>();

  public static void main(String[] args)
  {
	    Log.log(Log.INFO, "GiaraFilms");
	    ListLoader.loadSources();
	    SQL.connect();
	    for (SourceChan s : ListLoader.sources)
	    {
		      HTTPList search = new HTTPList(s.link, "misfits");
		      Log.log(Log.DEBUG, Integer.valueOf(search.file.size()));
		      for (String s2 : search.file)
		      {
		    	  new FileInfo(s2);
		      }
	    }
  }
}