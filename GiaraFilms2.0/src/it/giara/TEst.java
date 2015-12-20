package it.giara;

import it.giara.analyze.FileInfo;
import it.giara.analyze.enums.MainType;
import it.giara.phases.Settings;
import it.giara.phases.UpdateProgram;
import it.giara.source.ListLoader;
import it.giara.sql.SQL;
import it.giara.sql.SQLQuery;
import it.giara.syncdata.ServerQuery;
import it.giara.tmdb.http.TMDBSearchTVSerie;
import it.giara.utils.HttpPost;
import it.giara.utils.Log;

public class TEst
{
	public static void main(String[] args)
	{
		
		FileInfo file = new FileInfo("Death.Race2.Frankenstein.Lives.2011.iTALiAN.BDRip.XviD-TRL.avi");
		
		System.out.println(file.type);
		System.out.println(file.title);
		System.out.println(file.episode);
		System.out.println(file.series);
		
		if(true)
		return;
		
		Log.log(Log.DEBUG, HttpPost.post("http://giaratest.altervista.org/giarafilms/MD5Update.php", "md5", "ccc"));
		
		
		// HTTPSearchFilm test = new HTTPSearchFilm("spider");
		
		// HTTPFilmInfo res = new
		// HTTPFilmInfo("http://www.comingsoon.it/film/spider-man/1058/scheda/");
		// res.getInfo();
		
		SQL.connect();
		Settings.init();
		ListLoader.loadSources();
		
//		ServerQuery.sendScheda(SQLQuery.readScheda(674, MainType.Film));
//		Log.log(Log.DEBUG, ServerQuery.requestScheda(674, MainType.Film));
		
//		ServerQuery.sendFileInfo("testtest", "350M",61387, MainType.Film );
		
		UpdateProgram.checkUpdate(null);
		
//		ServerQuery.loadUntil(Integer.parseInt(Settings.getParameter("lastserversync")));
		
//		TMDBSearchTVSerie res = new TMDBSearchTVSerie("agent of");
		
		// Log.log(Log.DEBUG, test.scheda.link);
		
		// ListLoader.loadSources();
		// SQL.connect();
		// Settings.init();
		// FileSources sources = DownloadManager
		// .getFileSources("Sopravvissuto.The.Martian.2015.iTALiAN.MD.TS.XviD-iNCOMiNG.avi");
		// sources.requestDownload();
		//
	}
}