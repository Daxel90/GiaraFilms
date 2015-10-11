package it.giara.utils;

import java.util.Random;

public class FunctionsUtils
{
	public static Random r = new Random();
	
	public static int getTime()
	{
		return (int) (System.currentTimeMillis() / 1000);
	}
	
	public static void sleep(long ms)
	{
		try
		{
			Thread.sleep(ms);
		} catch (InterruptedException e)
		{
			Log.stack(Log.ERROR, e);
		}
	}
	
	public static Random getRandom()
	{
		return r;
	}
	
	public static double arrotondamento(double x){
		x = Math.floor(x*100);
		x = x/100;
		return x;
		}
	
}
