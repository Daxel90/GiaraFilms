package it.giara.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager 
{
	public static ExecutorService CachedExecutor;
	public static ExecutorService PoolExecutor;
	
	static
	{
		CachedExecutor = Executors.newCachedThreadPool();
		PoolExecutor = Executors.newFixedThreadPool(10);
	}
	
	
	public static void submitCacheTask(Runnable task)
	{
		CachedExecutor.submit(task);
	}
	
	public static void submitPoolTask(Runnable task)
	{
		PoolExecutor.submit(task);
	}
	
}
