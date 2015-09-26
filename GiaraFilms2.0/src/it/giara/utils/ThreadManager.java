package it.giara.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadManager
{
	public static ExecutorService CachedExecutor;
	public static ExecutorService PoolExecutor;
	public static ExecutorService PoolExecutorHightPriority;
	public static int poolWait = 0;
	public static int poolHeightWait = 0;
	public static int poolSize = 10;
	
	
	static
	{
		CachedExecutor = Executors.newCachedThreadPool();
		PoolExecutor = Executors.newFixedThreadPool(poolSize);
		PoolExecutorHightPriority = Executors.newFixedThreadPool(poolSize*2);
	}
	
	//Very Hight priority, execute instant task
	public static void submitCacheTask(Runnable task)
	{
		CachedExecutor.submit(task);
	}
	
	// insert task without check poolWait, create medium priority
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
	
	//pool for Hight Priority
	public static void submitPoolTaskHightPriority(final Runnable task)
	{
		poolHeightWait++;
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
					poolHeightWait--;
				}
			}
		};
		PoolExecutorHightPriority.submit(runnable);
	}
	
	public static int getPoolWait()
	{
		return poolWait;
	}
	
	public static int getPoolHeightWait()
	{
		return poolHeightWait;
	}
}
