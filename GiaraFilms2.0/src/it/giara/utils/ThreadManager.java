package it.giara.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

public class ThreadManager
{
	public static ExecutorService CachedExecutor;
	
	public static ExecutorService PoolExecutorSearch; // this pool is for
														// Loading List File
	public static ExecutorService PoolExecutorSearchIndicizer; // this pool run
																// file
																// indicizer
																// during Search
	
	public static ExecutorService PoolScanServiceExecutor;
	
	public static ExecutorService SystemPoolExecutor;
	public static ScheduledExecutorService ScheduledExecutorService; // scheduleThreadPool
	
	
	public static int poolSearchWait = 0;
	public static int SearchPoolSize = 5; // SearchThreads need to be very fast
	public static int SearchIndicizerPoolSize = 15;
	
	public static int SystemPoolSize = 10;
	public static int SchedulePoolSize = 2;
	
	static
	{
		BasicThreadFactory factory = new BasicThreadFactory.Builder().namingPattern("Cached Executor-%d").build();
		
		CachedExecutor = Executors.newCachedThreadPool(factory);
		ScheduledExecutorService = Executors.newScheduledThreadPool(SchedulePoolSize);
		SystemPoolExecutor = Executors.newFixedThreadPool(SystemPoolSize);
	}
	
	public static void init()
	{
		
		PoolExecutorSearch = Executors.newFixedThreadPool(SearchPoolSize);
		PoolExecutorSearchIndicizer = Executors.newFixedThreadPool(SearchIndicizerPoolSize);
	}
	
	// Very Height priority, execute instant task
	public static void submitCacheTask(Runnable task)
	{
		CachedExecutor.submit(task);
	}
	
	public static void submitSystemTask(Runnable task)
	{
		SystemPoolExecutor.submit(task);
	}
	
	// ScheduleTask
	public static void submitScheduleTask(Runnable task, int second)
	{
		ScheduledExecutorService.schedule(task, second, TimeUnit.SECONDS);
	}
	
	// SEARCH SECTION
	
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
				} catch (Exception e)
				{
					Log.stack(Log.ERROR, e);
				} 
				finally
				{
					poolSearchWait--;
				}
			}
		};
		PoolExecutorSearchIndicizer.submit(runnable);
	}
	
	public static void submitSearchTask(Runnable task)
	{
		PoolExecutorSearch.submit(task);
	}
	
	public static void resetPoolSearch()
	{
		PoolExecutorSearchIndicizer.shutdownNow();
		PoolExecutorSearch.shutdownNow();
		poolSearchWait = 0;
		PoolExecutorSearchIndicizer = Executors.newFixedThreadPool(SearchIndicizerPoolSize);
		PoolExecutorSearch = Executors.newFixedThreadPool(SearchPoolSize);
	}
}
