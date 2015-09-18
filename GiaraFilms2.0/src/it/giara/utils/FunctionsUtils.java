package it.giara.utils;


public class FunctionsUtils 
{

	public static int getTime()
	{
		return (int) (System.currentTimeMillis()/1000);
	}	
	
	public static void sleep(long ms)
	{
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			Log.stack(Log.ERROR, e);
		}
	}
	
	
}
