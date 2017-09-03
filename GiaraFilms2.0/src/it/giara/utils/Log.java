package it.giara.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import it.giara.phases.Settings;

public class Log
{
	public static LogType ERROR = LogType.ERROR;
	public static LogType NET = LogType.NETWORK;
	public static LogType FILEINFO = LogType.FILEINFO;
	public static LogType FILMINFO = LogType.FILMINFO;
	public static LogType DEBUG = LogType.DEBUG;
	public static LogType INFO = LogType.INFO;
	public static LogType CONFIG = LogType.CONFIG;
	public static LogType DB = LogType.DB;
	public static LogType GUI = LogType.GUI;
	public static LogType IMAGE = LogType.IMAGE;
	public static LogType IRC = LogType.IRC;
	public static LogType DOWNLOAD = LogType.DOWNLOAD;
	public static LogType SCANSERVICE = LogType.SCANSERVICE;
	public static LogType TMDB = LogType.TMDB;
	public static LogType TMDBApi = LogType.TMDBApi;
	public static LogType BACKEND = LogType.BACKEND;
	public static LogType SearchService = LogType.SearchService;
	
	public static void log(LogType l, Object o)
	{
		if(l.print || Settings.prop.getBoolean("Savelog", false))
		{
			System.out.println("["+l.name()+"] "+o);
			if (o != null)
				writeLog("["+l.name()+"] "+o.toString());
		}
	}
	
	public static void stack(LogType l, Exception e)
	{
		e.printStackTrace();
		log(l, "--------------------------");
		log(l, e.getMessage());
		for (StackTraceElement s : e.getStackTrace())
		{
			log(l, s.toString());
		}
		log(l, "--------------------------");
	}
	
	public static void writeLog(String text)
	{
		if (!Settings.prop.getBoolean("Savelog", false))
			return;
			
		FileWriter fWriter = null;
		BufferedWriter writer = null;
		try
		{
			File f = new File(DirUtils.getWorkingDirectory(), "log.txt");
			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			fWriter = new FileWriter(f, true);
			writer = new BufferedWriter(fWriter);
			
			writer.append(text);
			writer.newLine();
			writer.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
}

enum LogType
{
	NETWORK(false), 
	DEBUG(true), 
	INFO(true), 
	CONFIG(true), 
	FILEINFO(false), 
	FILMINFO(false), 
	DB(true), 
	GUI(true), 
	ERROR(true), 
	IMAGE(false), 
	IRC(false), 
	DOWNLOAD(false), 
	SCANSERVICE(false), 
	TMDB(false), 
	BACKEND(false),
	TMDBApi(false),
	SearchService(false);
	
	LogType(boolean p)
	{
		print = p;
	}
	
	boolean print = true;
}