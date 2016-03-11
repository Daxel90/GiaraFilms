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
	
	public static double arrotondamento(double x)
	{
		x = Math.floor(x * 100);
		x = x / 100;
		return x;
	}
	
	public static int[] truncateArray(int[] data, int val)
	{
		int[] ris = new int[val];
		for (int x = 0; x < val; x++)
		{
			ris[x] = data[x];
		}
		
		return ris;
	}
	
	public static int[] shuffleArray(int[] data)
	{
		int[] array = data.clone();
		int count = array.length;
		for (int i = count; i > 1; i--)
		{
			swap(array, i - 1, getRandom().nextInt(i));
		}
		return array;
	}
	
	private static void swap(int[] array, int i, int j)
	{
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}
	
}
