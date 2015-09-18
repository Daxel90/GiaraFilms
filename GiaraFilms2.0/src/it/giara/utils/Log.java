package it.giara.utils;

import java.io.PrintStream;

public class Log
{
  public static LogType NET = LogType.NETWORK;
  public static LogType FILEINFO = LogType.FILEINFO;
  public static LogType FILMINFO = LogType.FILMINFO;
  public static LogType DEBUG = LogType.DEBUG;
  public static LogType INFO = LogType.INFO;
  public static LogType CONFIG = LogType.CONFIG;
  public static LogType DB = LogType.DB;

  public static void log(LogType l, Object o)
  {
    if ((!l.equals(NET)) && (!l.equals(FILEINFO)))
      System.out.println(o);
  }

  public static void stack(LogType l, Exception e)
  {
    e.printStackTrace();
  }
}