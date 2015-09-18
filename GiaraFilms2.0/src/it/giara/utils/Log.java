package it.giara.utils;

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
	
	public static void log(LogType l, Object o)
	{
		if (!l.equals(NET) && !l.equals(FILEINFO))
			System.out.println(o);
	}
	
	public static void stack(LogType l, Exception e)
	{
		e.printStackTrace();
	}
	
}

enum LogType
{
	NETWORK, DEBUG, INFO, CONFIG, FILEINFO, FILMINFO, DB, GUI, ERROR
}