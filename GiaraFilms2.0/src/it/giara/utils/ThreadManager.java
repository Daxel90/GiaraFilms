package it.giara.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager
{
	public static ExecutorService CachedExecutor;
	public static ExecutorService PoolExecutor;
	public static int poolWait = 0;
	public static int poolSize = 10;
	
	static
	{
		CachedExecutor = Executors.newCachedThreadPool();
		PoolExecutor = Executors.newFixedThreadPool(poolSize);
	}
	
	public static void submitCacheTask(Runnable task)
	{
		CachedExecutor.submit(task);
	}
	
	public static void submitPoolTask(final Runnable task)
	{
		poolWait++;
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					task.run();
				}
				finally
				{
					poolWait--;
				}
			}
		};
		
		PoolExecutor.submit(runnable);
	}
	
	public static int getPoolWait()
	{
		return poolWait;
	}
}
