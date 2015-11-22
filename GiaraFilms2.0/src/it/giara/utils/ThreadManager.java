package it.giara.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
public class ThreadManager
{
	public static ExecutorService CachedExecutor;
	public static ExecutorService PoolExecutor;
	
	public static ExecutorService PoolExecutorSearch; // this pool is for Loading List File
	public static ExecutorService PoolExecutorSearchIndicizer; //this pool run file indicizer
	
	public static ScheduledExecutorService  ScheduledExecutorService ; //scheduleThreadPool
	
	public static int poolWait = 0;
	public static int poolSearchWait = 0;
	public static int poolSize = 10;
	
	static
	{
		CachedExecutor = Executors.newCachedThreadPool();
		PoolExecutor = Executors.newFixedThreadPool(poolSize);
		
		PoolExecutorSearch = Executors.newFixedThreadPool(poolSize /2);
		PoolExecutorSearchIndicizer = Executors.newFixedThreadPool(poolSize * 2);
		
		ScheduledExecutorService = Executors.newScheduledThreadPool(2);
	}
	
	// Very Height priority, execute instant task
	public static void submitCacheTask(Runnable task)
	{
		CachedExecutor.submit(task);
	}
	
	// insert task without checking poolWait, create medium priority
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
				} finally
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
	
	//SEARCH SECTION
	
	public static int getPoolSearchWait()
	{
		return poolSearchWait;
	}
	
	public static void submitPoolExecutorSearchIndicizer(final Runnable task)
	{
		poolSearchWait++;
		Runnable runnable = new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					task.run();
				} finally
				{
					poolSearchWait--;
				}
			}
		};
		PoolExecutorSearchIndicizer.submit(runnable);
	}
	
	// Very Height priority, execute instant task
	public static void submitSearchTask(Runnable task)
	{
		PoolExecutorSearch.submit(task);
	}
	
	// ScheduleTask
	public static void submitScheduleTask(Runnable task, int second)
	{
		ScheduledExecutorService.schedule(task, second, TimeUnit.SECONDS);
	}
	
	public static void resetThreadSearch()
	{
		PoolExecutorSearchIndicizer.shutdownNow();
		PoolExecutorSearch.shutdownNow();
		poolSearchWait = 0;
		PoolExecutorSearchIndicizer = Executors.newFixedThreadPool(poolSize * 2);
		PoolExecutorSearch = Executors.newFixedThreadPool(poolSize /2);
	}
}
